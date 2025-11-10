# 数学表达式日志与异常包装增强

## 关键改动
- `requirements/tasks.md`：将“2025-11-10-数学表达式日志注释等增加”标记为已完成，符合流程要求。
- `src/main/java/org/yann/eureka/client/demo/controller/BaseResponse.java`：新增 `errorCode` 字段与默认错误码常量，统一成功/失败响应结构，支撑前端细分错误场景。
- `src/main/java/org/yann/eureka/client/demo/controller/MathExpressionController.java`：补充日志、注释与人类可读的中文提示，所有校验/计算异常都转换为 `BaseResponse`，避免 500 直出。
- `src/main/java/org/yann/eureka/client/demo/controller/GlobalExceptionHandler.java`（新）：集中捕获 `IllegalArgumentException` 和未知异常，返回友好提示并记录日志。
- `src/main/resources/templates/math-expression.html`：更新交互文案，解析新的 `errorCode`/`errorMsg` 字段，网络错误和空响应也会显示明确提示。

## 测试验证
- `mvn -q -DskipTests package`
