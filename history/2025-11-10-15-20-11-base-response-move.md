# BaseResponse 挪至 util 包

## 关键改动
- 新增 `org.yann.eureka.client.demo.util.BaseResponse`，并删除 controller 包内的旧实现，便于复用和包结构整洁。
- `HelloServiceController`、`MathExpressionController` 及 `GlobalExceptionHandler` 同步引用新的包路径。

## 测试
- `mvn -q -DskipTests package`
