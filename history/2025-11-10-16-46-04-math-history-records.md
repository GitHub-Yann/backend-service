# 本次改动概述
- 为数学表达式计算接口增加历史记录功能，支持记录最近的计算信息并在前端展示。
- 在 `application.yml` 中引入 `math.history.*` 配置项，支持开关、容量与本地 JSON 文件持久化。
- math-expression 页面新增历史面板，可在计算后实时刷新记录，亦可手动重新加载。
- 引入单元测试 `MathHistoryServiceTest` 覆盖环形缓存容量与 limit 行为，保障基础逻辑。

# 关键改动
## 服务端历史记录链路
- 新增 `MathHistoryRecord` / `MathHistoryProperties` / `MathHistoryService`，通过 `ArrayDeque + ReentrantReadWriteLock` 维护环形缓冲区，异步刷写 `data/math-history.json`。
- 在 `MathExpressionController` 中注入 `MathHistoryService`，所有成功与失败路径均调用 `recordHistory` 写入原始表达式、结果/错误、耗时、IP；新增 `GET /api/math/history?limit=` 接口校验请求并返回最新列表。

```java
private void recordHistory(...) {
    MathHistoryRecord record = new MathHistoryRecord();
    record.setRawExpression(rawExpression);
    record.setExpression(normalizedExpression);
    record.setSuccess(success);
    record.setResult(result);
    record.setErrorCode(errorCode);
    record.setErrorMsg(errorMsg);
    record.setRequestIp(requestIp);
    record.setEvaluatedAt(Instant.now());
    record.setDurationMillis(TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNano));
    historyService.append(record);
}
```

## 前端与配置
- `math-expression.html` 新增历史列表、刷新按钮及渲染逻辑；在提交后自动调用 `loadHistory()`，失败时提示具体原因。
- `application.yml` 增加注释与默认配置，方便通过配置调整历史记录能力。
- `pom.xml` 引入 `spring-boot-starter-test`，并编写 `MathHistoryServiceTest` 验证容量淘汰与 limit 处理。

# 测试
- `mvn -q test`