## 编码流程规范
1. 你什么时候开始开发需求，满足下面两个条件
  - 1.1 接收到允许对任务进行开发的指令
  - 1.2 当前需求在`requirements`目录下的`tasks.md`文件中的状态为`待开发`，`开发中`或者`待测试`
2. 当你开始执行开发前，先将`requirements`目录下的`tasks.md`中的当前需求的状态改成`开发中`，不修改其他记录
3. 在你开发的过程中，一定要注意的事项：
  - 要添加日志，特别是关键业务逻辑节点
  - 方法，类，关键的逻辑块请添加注释
4. 当你开发完成后，
  - 4.1 一定将`requirements`目录下的`tasks.md`中的当前需求的状态改成`待测试`
  - 4.2 接收人工代码审核，可能人工审核会要求返工，那么直接按照要求，进行调整

## 项目结构与模块组织规范
### 1. 如果你在开发java项目，开发过程中一定按照如下的项目结构
```
project-x/          # java项目根目录
├── src/
│   ├── main/
│   │   ├── java/   # java源代码目录
│   │   │   └── package-path/     # java中的主包路径，例如 com.example.project
│   │   │       ├── controller/   # 该目录（模块/子包）存放 REST 控制器类，例如 `UserController`，完整的地址是：`com.example.project.controller.UserController`
│   │   │       ├── service/      # 该目录（模块/子包）存放业务逻辑类，例如 `UserService`，完整的地址是：`com.example.project.service.UserService`
│   │   │       ├── dao/          # 该目录（模块/子包）存放数据访问接口类，例如 `UserDao`，完整的地址是：`com.example.project.dao.UserDao`
│   │   │       ├── model/        # 该目录（模块/子包）存放数据模型类，例如 `UserModel`，完整的地址是：`com.example.project.model.UserModel`
│   │   │       ├── util/         # 该目录（模块/子包）存放工具类，常量类，全局的处理器类，例如 `HttpUtils`，完整地址是：`com.example.project.util.HttpUtils`
│   │   │       ├── config/       # 该目录（模块/子包）存放配置类，例如 `CustomizedConfig`，完整的地址是：`com.example.project.config.CustomizedConfig`
│   │   │       ├── exception/    # 该目录（模块/子包）存放异常类，例如 `CustomizedException`，完整的地址是：`com.example.project.exception.CustomizedException`
│   │   │       └── filter/       # 该目录（模块/子包）存放过滤器类，拦截器类，例如 `AuthFilter`，完整地址是：`com.example.project.filter.AuthFilter`
│   │   └── resources/  # 存放资源文件，例如：`application.yml`，`bootstrap.yml`，`logback.xml`
│   │           ├── application.yml  # 运行配置文件
│   │           ├── static/          # 存放静态资源，例如图片，css，js等
│   │           ├── templates/       # 存放Thymeleaf 模板， 例如：`index.html`
│   │           └── logback.xml      # 日志配置文件
│   └── test/            # 预留测试目录
│        └── java/       # 测试java代码存放目录
│             └── package-path/     # java中的测试主包路径，例如 com.example.project
├── target/              # 存放编译后的文件
├── pom.xml              # maven项目配置文件
└── README.md            # 项目说明
```

### 2. 如果你在开发python项目，开发过程中一定按照基本的python项目结构来组织
- 再加上我的格式要求，如下：
```
project-x/           # python项目根目录
├── utils/           # 公共包目录，存放公共的模块，至于其他业务逻辑的封装模块可以放到其他包下
├── package1/        # 业务逻辑包目录，存放具体的业务逻辑封装模块，包名字创建时定义
├── package2/        # 业务逻辑包目录，存放具体的业务逻辑封装模块，包名字创建时定义
├── main.py          # 项目入口文件
└── README.md        # 项目说明
```

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

