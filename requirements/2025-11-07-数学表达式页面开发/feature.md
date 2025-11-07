需求主题：数学表达式页面
需求实现：
- 在前端页面提供数学表达式输入框与触发计算的按钮，提交内容时以POST请求调用`/api/math/calculate`接口。
- 页面使用JavaScript异步请求接口，根据返回的成功或失败消息在结果区域内展示计算值或错误提示。
- 对输入做基础合法性校验，空值时直接在前端提示，避免无效请求。
- 页面整体采用简单布局，便于后续扩展历史记录或示例表达式等功能。
需求改动：
- 新增`src/main/resources/templates/math-expression.html`模板文件，承载输入表单与结果展示区域。
- 视需要新增`src/main/resources/static/js/math-expression.js`或内联脚本处理事件与接口交互。
- 如果当前项目尚未提供视图控制器，需要新增一个返回模板视图的控制器类，例如`MathExpressionPageController`。
- 根据前端脚本需求，可能引入自定义样式文件至`src/main/resources/static/css/`目录。
分析时间：2025-11-07 15:36:34
