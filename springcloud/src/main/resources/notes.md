对于微服务架构而言，一个通用的配置中心是必不可少的
Netflix Archaius 是一个功能强大的配置管理库。它是一个可用于从许多不同来源收集配置属性的框架，提供对配置信息的快速及线程安全访问。
除此之外，Archaius允许属性在运行时动态更改，使系统无需重新启动应用程序即可获得这些变化。

和springcloud的configserver比较:
提供JMX MBean可以通过JConsole操作
动态,类型属性
开箱即用的组合配置(具有层级关系),自动覆盖同名属性(topmost slot wins)

默认使用classpath下的config.properties

自动获取新配置文件内容的默认周期有点长
手动调整: -Darchaius.fixedDelayPollingScheduler.delayMills=1000

指定配置文件路径,首先读取config.properties,然后读取additionalUrl,同名属性后面覆盖前面的
-Darchaius.configurationSource.additionalUrls=
  "classpath:other-dir/extra.properties,
  file:///home/user/other-extra.properties"

archaius.configurationSource.defaultFileName
archaius.fixedDelayPollingScheduler.initialDelayMills
archaius.fixedDelayPollingScheduler.delayMills default 1min(60000)