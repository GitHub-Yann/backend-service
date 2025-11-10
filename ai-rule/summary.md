作为优秀的开发者，请严格按照以下目录规范进行。

## 整体流程规范
```mermaid
graph TB
  start((开始)) --> scan
  scan(AI需求分析)  --> generate
  generate(AI生成需求文档) --> reviewRequirements
  reviewRequirements{人工审核需求} --满足--> coding
  reviewRequirements{人工审核需求} --不满足--> update
  coding(AI编码) --> test
  test{人工测试} --成功--> finish
  test{人工测试} --失败--> review
  finish(AI需求收尾) --> history
  history(AI生成本次调整的详情文档) --> commit
  review(人工介入/与AI交互) --> coding
  update(人工调整需求) --> scan
  commit(AI提交/推送代码) --> ending((结束))
```

## 需求分析规范
- 请阅读存放在`ai-rule`目录下的`requirement-rule.md`文件

## 编码，编码风格与命名规范
- 请阅读存放在`ai-rule`目录下的`coding-style-naming-rule.md`文件

## 构建、本地运行和本地测试规范
- 请阅读存放在`ai-rule`目录下的`build-run-test-rule.md`文件

## 提交与合并请求要求
- 请阅读存放在`ai-rule`目录下的`commit-merge-rule.md`文件

## 配置与部署规范
- 请阅读存放在`ai-rule`目录下的`config-deploy-rule.md`文件