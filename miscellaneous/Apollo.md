Apollo 配置中心 由携程Ctrip维护, 具体见github

现有生产配置: 通过k8s的configmap

目前环境配置: DEV SIT UAT PREP(pre-production) PERF(performance) PROD

结构: 有一个全局的all-in-one的portal,可以管理所有的环境,也可以进行环境之间的操作,比如SIT复制到UAT

具体有admin-service服务于portal, config-service服务于client(也就是集成apollo的app)

概念: department 相当于team, project(app)相当于repository, namespace相当于具体的profile或者properties

namespace分为public(可在app间共享)和private

**理解**: 配置哪个项目(repository)的哪个环境(properties)

权限: 很多角色,比如有project owner, admin, editor, releaser等,一个人不是能editor,同时又是releaser

conf-module和apollo的关系: 并存, k8s能处理好

敏感信息: 以前都是配置在jenkins映射到k8s的pod系统环境变量,以后要迁移到k8s的secret里,不能放到apollo里

目前apollo不能处理二进制配置文件,还需要放到conf里

**apollo的优先级比conf的要高**

具体结合SpringBoot: 在resource/META-INF/app.properties里配置app.id和enable apollo

使用@EnableApolloConfig("demo"), 默认加载application命名空间.多个命名空间写在前面的优先级高

运行参数里要加 -Dapollo.meta=configuration_url

本地debug: 抓取配置到本地cache之后,使用-Denv=local启动即可,此时本地修改属性没有自动加载

配置中心的改动会在秒级同步到client端

<u>apollo上可能可以配置日志级别以动态调整日志输出,待验证.</u> SpringActuator里可以动态调整日志

--完--

