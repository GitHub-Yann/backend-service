package org.yann.eureka.client.demo.controller;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/math")
public class MathExpressionController {

	private static final Logger LOGGER = LoggerFactory.getLogger(MathExpressionController.class);
	private static final Pattern ALLOWED_CHAR_PATTERN = Pattern.compile("^[0-9+\\-*/().\\s]+$");
	private static final MathContext DIVIDE_CONTEXT = MathContext.DECIMAL128;

	@GetMapping("/calculate")
	public BaseResponse calculate(@RequestParam("expression") String expression) {
		if (!StringUtils.hasText(expression)) {
			return BaseResponse.ERROR("表达式不能为空");
		}
		String trimmed = expression.trim();
		if (!ALLOWED_CHAR_PATTERN.matcher(trimmed).matches()) {
			return BaseResponse.ERROR("表达式包含非法字符，只允许数字、加减乘除、小数点和括号");
		}
		try {
			validateParentheses(trimmed);
			List<String> tokens = tokenize(trimmed);
			BigDecimal result = evaluate(tokens);
			Map<String, Object> data = new HashMap<>();
			data.put("expression", expression);
			data.put("result", result.stripTrailingZeros().toPlainString());
			return BaseResponse.OK("Calculation succeeded", data);
		} catch (IllegalArgumentException ex) {
			return BaseResponse.ERROR(ex.getMessage());
		} catch (Exception ex) {
			LOGGER.error("Failed to evaluate expression '{}'", expression, ex);
			return BaseResponse.ERROR("表达式计算失败: " + ex.getMessage());
		}
	}

	private void validateParentheses(String expression) {
		int count = 0;
		for (char ch : expression.toCharArray()) {
			if (ch == '(') {
				count++;
			} else if (ch == ')') {
				count--;
				if (count < 0) {
					throw new IllegalArgumentException("括号不匹配，存在多余的右括号");
				}
			}
		}
		if (count != 0) {
			throw new IllegalArgumentException("括号不匹配，存在未关闭的左括号");
		}
	}

	private List<String> tokenize(String expression) {
		List<String> tokens = new ArrayList<>();
		int length = expression.length();
		int index = 0;
		while (index < length) {
			char current = expression.charAt(index);
			if (Character.isWhitespace(current)) {
				index++;
				continue;
			}
			if (isOperator(current)) {
				if ((current == '-' || current == '+') && isUnaryOperator(tokens)) {
					int start = index;
					index++;
					while (index < length && Character.isWhitespace(expression.charAt(index))) {
						index++;
					}
					if (index >= length) {
						throw new IllegalArgumentException("表达式以运算符结尾");
					}
					if (expression.charAt(index) == '(') {
						tokens.add("0");
						tokens.add(String.valueOf(current));
						continue;
					}
					String numberToken = parseNumber(expression, index);
					index += numberToken.length();
					tokens.add(current == '-' ? "-" + numberToken : numberToken);
					continue;
				}
				tokens.add(String.valueOf(current));
				index++;
				continue;
			}
			if (current == '(' || current == ')') {
				tokens.add(String.valueOf(current));
				index++;
				continue;
			}
			if (Character.isDigit(current) || current == '.') {
				String numberToken = parseNumber(expression, index);
				index += numberToken.length();
				tokens.add(numberToken);
				continue;
			}
			throw new IllegalArgumentException("无法识别的字符: " + current);
		}
		if (tokens.isEmpty()) {
			throw new IllegalArgumentException("表达式不能为空");
		}
		if (isOperatorToken(tokens.get(tokens.size() - 1))) {
			throw new IllegalArgumentException("表达式不能以运算符结尾");
		}
		return tokens;
	}

	private String parseNumber(String expression, int startIndex) {
		int length = expression.length();
		StringBuilder builder = new StringBuilder();
		boolean decimalPointFound = false;
		int index = startIndex;
		while (index < length) {
			char current = expression.charAt(index);
			if (Character.isDigit(current)) {
				builder.append(current);
				index++;
				continue;
			}
			if (current == '.') {
				if (decimalPointFound) {
					throw new IllegalArgumentException("数字中包含多个小数点");
				}
				if (builder.length() == 0) {
					builder.append('0');
				}
				decimalPointFound = true;
				builder.append(current);
				index++;
				continue;
			}
			break;
		}
		if (builder.length() == 0) {
			throw new IllegalArgumentException("数字解析失败，检查是否缺少数字");
		}
		if (builder.charAt(builder.length() - 1) == '.') {
			throw new IllegalArgumentException("数字不能以小数点结尾");
		}
		return builder.toString();
	}

	private boolean isUnaryOperator(List<String> tokens) {
		if (tokens.isEmpty()) {
			return true;
		}
		String lastToken = tokens.get(tokens.size() - 1);
		return "(".equals(lastToken) || isOperatorToken(lastToken);
	}

	private boolean isOperator(char ch) {
		return ch == '+' || ch == '-' || ch == '*' || ch == '/';
	}

	private boolean isOperatorToken(String token) {
		return "+".equals(token) || "-".equals(token) || "*".equals(token) || "/".equals(token);
	}

	private BigDecimal evaluate(List<String> tokens) {
		Deque<BigDecimal> values = new ArrayDeque<>();
		Deque<String> operators = new ArrayDeque<>();
		for (String token : tokens) {
			if ("(".equals(token)) {
				operators.push(token);
				continue;
			}
			if (")".equals(token)) {
				while (!operators.isEmpty() && !"(".equals(operators.peek())) {
					applyOperator(values, operators.pop());
				}
				if (operators.isEmpty() || !"(".equals(operators.pop())) {
					throw new IllegalArgumentException("括号不匹配，缺少对应的左括号");
				}
				continue;
			}
			if (isOperatorToken(token)) {
				while (!operators.isEmpty() && isOperatorToken(operators.peek())
						&& precedence(operators.peek()) >= precedence(token)) {
					applyOperator(values, operators.pop());
				}
				operators.push(token);
				continue;
			}
			values.push(new BigDecimal(token));
		}
		while (!operators.isEmpty()) {
			String operator = operators.pop();
			if ("(".equals(operator)) {
				throw new IllegalArgumentException("括号不匹配，存在未关闭的左括号");
			}
			applyOperator(values, operator);
		}
		if (values.size() != 1) {
			throw new IllegalArgumentException("表达式解析失败，请检查运算符和数字的数量");
		}
		return values.pop();
	}

	private int precedence(String operator) {
		if ("+".equals(operator) || "-".equals(operator)) {
			return 1;
		}
		if ("*".equals(operator) || "/".equals(operator)) {
			return 2;
		}
		return 0;
	}

	private void applyOperator(Deque<BigDecimal> values, String operator) {
		if (values.size() < 2) {
			throw new IllegalArgumentException("运算符 '" + operator + "' 缺少对应的操作数");
		}
		BigDecimal right = values.pop();
		BigDecimal left = values.pop();
		switch (operator) {
		case "+":
			values.push(left.add(right));
			break;
		case "-":
			values.push(left.subtract(right));
			break;
		case "*":
			values.push(left.multiply(right));
			break;
		case "/":
			if (right.compareTo(BigDecimal.ZERO) == 0) {
				throw new IllegalArgumentException("存在除以零的情况");
			}
			values.push(left.divide(right, DIVIDE_CONTEXT));
			break;
		default:
			throw new IllegalArgumentException("不支持的运算符: " + operator);
		}
	}
}
