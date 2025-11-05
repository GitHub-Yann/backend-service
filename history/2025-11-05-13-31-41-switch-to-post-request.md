# Summary
- 将数学表达式接口改为POST请求并支持JSON请求体

# Files
- src/main/java/org/yann/eureka/client/demo/controller/MathExpressionController.java

# Details
- 使用@PostMapping处理请求并增加ExpressionRequest请求体封装
- 调整输入校验逻辑以适配请求体形式，仍复用原有表达式解析流程
- 编译校验通过，确保接口变更不会破坏现有计算结果
