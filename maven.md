maven各种配置

Maven中为我们集成了软件配置管理的（SCM：Software Configuration Management）功能
常见操作: mvn scm:checkin -Dmessage="<commit_log_here>"
<scm>scm:git:git://github.com/path_to_repository</scm>
<developerConnection>scm:git:[fetch=]http://mywebserver.org/path_to_repository[push=]ssh://username@otherserver:8898/~/repopath.git</developerConnection>

connection节点requires read access for Maven to be able to find the source code (for example, an update), 
developerConnection配置requires a connection that will give write access.

maven-surefire-plugin
maven里执行测试用例的插件，不显示配置就会用默认配置。这个插件的surefire:test命令会默认绑定maven执行的test阶段

需要设定文件的编码格式（如果不设定，将会以系统的默认编码进行处理）与JDK版本版本变量
<properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <java.version>1.8</java.version>
</properties>

provided:编译的时候需要,实际部署的时候不需要,因为容器会提供,如servlet.jar
runtime:编译的时候不需要,实际运行的时候需要,如jpa和hibernate的关系

maven-shade-plugin shade阴影
生成一个 uber-jar(fatjar)以 -shaded.jar 为后缀的 jar 包，它包含所有的依赖 jar 包
maven-shade-plugin 将 goal shade:shade 绑定到 phase package 上
Resource Transformers
有些jar会含有一些资源文件如properties,而且同名,为了避免覆盖,需要将文件合并到一起.AppendingTransformer就是做这个事情的.
类似的还有ManifestResourceTransformer用来设置Manifest项目

maven-assembly-plugin assembly装配 集合
Assembly 插件的主要作用是，允许用户将项目输出与它的依赖项、模块、站点文档、和其他文件一起组装成一个可分发的归档文件
就是定制化打包
需要指定一个Assembly描述符文件,通常在resource/assembly下。
该文件指定了打包格式，包含的文件/过滤的文件等信息，可以同时指定多个描述符文件，打包成不同的格式
在execution中指定对应的描述文件,如configuration>description>src/main/resources/assembly/xx.xml
绑定在package阶段,一般都是和profile结合使用,定义<env.devMode>dev</env.devMode><skipAssemblyDEV>false</skipAssemblyDEV>等