# Repository Guidelines

## 项目结构与模块组织
- `pom.xml` 定义 Spring Boot 2.7.14 的 Maven 构建与 Java 8 兼容范围。
- `src/main/java/org/yann/eureka/client/demo` 存放入口 `Application` 及 REST/Thymeleaf 控制器（如 `HelloServiceController`、`WebController`）。
- `src/main/resources` 包含运行配置 `application.yml`、日志配置 `logging-config.xml`、Thymeleaf 模板 `templates/index.html` 以及静态资源 `static/img/*`。
- `src/test/java` 预留测试目录；新增用例请按功能子包和类名划分。

## 构建、测试与本地运行命令
- `mvn clean package`：清理并编译打包，可用于构建产物交付。
- `mvn spring-boot:run`：以开发模式启动服务，默认监听配置文件中的端口。
- `mvn test`：执行全部测试；首次添加测试前请确保已引入 `spring-boot-starter-test` 依赖。
- 本项目需在 `JAVA_HOME` 指向 JDK 8 的环境下运行。

## 编码风格与命名约定
- Java 源码保持与现有文件一致的制表符缩进、注解置于类或方法上方、行宽控制在 120 列以内。
- 包名沿用 `org.yann.eureka.client.demo` 层级，新增模块按领域创建子包。
- REST 控制器使用 `@RestController` 或 `@Controller`，响应模型统一放在 `controller` 包或相邻包中。
- 日志使用 `LoggerFactory.getLogger(...)` 创建的 `private static final Logger`，输出文本请写成可检索的英文短语。

## 测试指引
- 目前不需要你进行该项目的单元测试

## 提交与合并请求要求
- 当提交代码的时候，请给我生成一个当前改动的描述文件，按如下格式：
  - 文件目录：在根目录下创建一个history文件夹，如果存在直接使用
  - 文件名：yyyy-MM-dd-HH-mm-ss-{改动简要描述}.md
  - 文件内容：以markdowngen格式描述本次改动的内容，主要修改了哪些文件，以及改动的详细描述

## 配置与部署提示
- 目前不需要你进行配置和部署