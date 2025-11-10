# 需求主题
数学表达式计算接口需要补充日志、注释，并提供一致的异常包装，避免出现 500 直接返回给前端的情况，保证可观测性与可维护性。

# 需求实现
1. 日志完善：在 `MathExpressionController` 的 `calculate` 流程中记录入口参数、校验失败、计算成功与失败等关键信息，异常时输出上下文，避免敏感信息泄露。
2. 注释补充：为控制器和复杂方法（如 `tokenize`、`evaluate`、`applyOperator` 等）补充精炼的 JavaDoc/关键代码块注释，说明算法要点和边界条件，方便后续维护。
3. 异常包装：为常见的参数错误、业务校验失败与未知异常提供统一的响应包装，不再让未捕获异常抛到框架导致 500；可通过新增 `@RestControllerAdvice` 或控制器内局部 `@ExceptionHandler` 转换为 `BaseResponse`，并按场景设置合理的 HTTP 状态。
4. 错误码与提示：为 `BaseResponse` 增加可选的错误码或细化错误信息，区分用户可感知的提示与系统日志，保证前端可根据提示进行展示与处理。
5. 兜底策略：在计算流程内部保留对 `IllegalArgumentException` 等已知异常的捕获，同时将不可预期异常交给统一异常处理，返回“系统繁忙请稍后再试”之类的友好提示。

# 需求改动
- `src/main/java/org/yann/eureka/client/demo/controller/MathExpressionController.java`：补充日志、注释以及更精细的异常捕获/返回。
- `src/main/java/org/yann/eureka/client/demo/controller/BaseResponse.java`：如需提供错误码或统一消息字段，扩展响应结构及静态工厂方法。
- `src/main/java/org/yann/eureka/client/demo/util/GlobalExceptionHandler.java`（新）：集中处理未捕获异常，转换为 `BaseResponse` 并记录日志。
- `src/main/resources/templates/math-expression.html`（如有需要）：根据新的错误提示格式，调整前端展示文案。

# 分析时间
2025-11-10 13:35:42
