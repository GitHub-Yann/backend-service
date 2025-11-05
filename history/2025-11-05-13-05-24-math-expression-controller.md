# Summary
- 新增数学表达式计算接口控制器

# Files
- src/main/java/org/yann/eureka/client/demo/controller/MathExpressionController.java

# Details
- 基于双栈算法解析表达式，支持括号和一元加减并以高精度返回结果
- 增加非法字符、括号不匹配、重复小数点等校验并输出清晰错误信息
- 检测除零场景并记录异常日志，同时响应中返回原始表达式与字符串化结果
