# 全局异常处理器归位

## 关键改动
- 将 `GlobalExceptionHandler` 移动到 `org.yann.eureka.client.demo.util` 包，更贴合通用工具/横切逻辑定位，并补充对 `BaseResponse` 的显式引用。
- 更新需求分析文档，修正异常处理器的新路径，保持文档与代码一致性。

## 测试
- `mvn -q -DskipTests package`

