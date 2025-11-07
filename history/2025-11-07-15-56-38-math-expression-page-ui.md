## 本次改动
- 新增 `math-expression` 模板页面，提供表达式输入、计算按钮及结果展示区域。
- 在 `WebController` 中添加访问 `/math-expression` 的页面入口。
- 前端脚本通过 `fetch` 调用 `/api/math/calculate` 接口，处理成功与失败反馈。

## 影响范围
- `src/main/resources/templates/math-expression.html`
- `src/main/java/org/yann/eureka/client/demo/controller/WebController.java`
- `requirements/tasks.md`

## 测试情况
- 手动访问 `/math-expression`，提交示例表达式并确认结果展示无误。
