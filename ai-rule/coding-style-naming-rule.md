## 编码流程规范
1. 开始开发需求的时机，满足下面两个条件
  - 1.1 接收到允许对任务进行开发的指令
  - 1.2 当前需求在`requirements`目录下的`tasks.md`文件中的状态为`待开发`，`开发中`或者`待测试`
2. 执行开发前，先将`requirements`目录下的`tasks.md`中的当前需求的状态改成`开发中`，不修改其他记录
3. 开发过程中，一定要注意的事项：
  - 要添加日志，特别是关键业务逻辑节点
  - 方法，类，关键的逻辑块请添加注释
4. 开发完成后，
  - 4.1 一定将`requirements`目录下的`tasks.md`中的当前需求的状态改成`待测试`
  - 4.2 接收人工代码审核，可能人工审核会要求返工，那么直接按照要求，进行调整

## 项目结构与模块组织规范
开发过程中一定按照如下的项目结构
- `pom.xml` 定义了项目依赖关系。
- `src/main/java` 类的存放入口
  - `controller` 目录（模块/子包）存放 REST 控制器类，例如 `UserController`
  - `service` 目录（模块/子包）存放业务逻辑类，例如 `UserService`
  - `dao` 目录（模块/子包）存放数据访问接口类，例如 `UserDao`
  - `model` 目录（模块/子包）存放数据模型类，例如 `UserModel`，`ProductModel`
  - `util` 目录（模块/子包）存放工具类，常量类，全局的处理器类，例如 `StringUtils`，`HttpUtils`
  - `config` 目录（模块/子包）存放配置类，例如 `CustomizedConfig`，`TaskJobConfig`
  - `exception` 目录（模块/子包）存放异常类，例如 `CustomizedException`
  - `filter` 目录（模块/子包）存放过滤器类，拦截器类，例如 `CorsFilter`，`AuthFilter`
- `src/main/resources` 资源文件的存放入口
  - 运行配置，例如： `bootstrap.yml`,`application.yml`,`application-production.yml`
  - 日志配置 `logging-config.xml`
  - Thymeleaf 模板存放在`templates`目录下，例如：`templates/index.html` 
  - 静态资源 存放在`static`目录下，例如：`static/img/*`
- `src/test/java` 预留测试目录；新增用例请按功能子包和类名划分。

## 编码风格与命名规范
- Java 源码保持与现有文件一致的制表符缩进、注解置于类或方法上方、行宽控制在 120 列以内。
- 包名沿用现有的层级，新增模块按领域创建子包。
  - 例如当前项目有模块（子包）为 `org.projecta.controller`，`org.projecta.service`，`org.projecta.dao`。
    - 那么主包的层级就是 `org.projecta`。
    - 如果新增工具类的模块（子包），那么工具类所在的包名就是 `org.projecta.util`。
    - 如果新增配置文件的模块（子包），那么配置文件所在的包名就是 `org.projecta.config`。

## 常规的开发规范
- 日志
  - 请使用 `LoggerFactory.getLogger(...)` 创建的 `private static final Logger`，输出文本请写成可检索的英文短语。

