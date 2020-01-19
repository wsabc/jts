cron expression:
format: seconds minutes hours dayOfMonth month dayOfWeek year(optional)
example: 0 0 12 ? * WED = 12pm on every WED
each field contains range/list: 8-17 = 8am to 17pm; or JAN,OCT = only Jan and Oct
wildcard: * = all values for this field; ? = no specific values in and only in dayOfMonth/dayOfWeek field
increment: start/increment; 5/15 = start at 5 and step by 15(20, 35, 50...)
keywords:
L = last value in and only in dayOfMonth/dayOfWeek; L may indicate 28/2 or 31/1; or FRIL = last FRI in this month
W = close working day to specific day
\# = nth of the working day of this month; FRI#3 = 3rd FRI in this month

lib dependencies:
org.quartz-scheduler:quartz
org.quartz-scheduler:quartz-jobs
org.quartzdesk:quartzdesk-api

config:
quartz.properties

org.quartz.jobStore.class = org.quartz.simpl.RAMJobStore


components:
lots of XXBuilder
job & trigger decoupling
job & jobDetail design - job properties

jobDetail DataMap serialize



如果重复间隔为0，trigger将会以重复次数并发执行(或者以scheduler可以处理的近似并发数)。
TriggerBuilder会为trigger生成一个随机的名称；如果没有调用startAt(..)方法，则默认使用当前时间，即trigger立即生效


每次运行应用程序时，都需要重新注册该调度程序

Job Stores
RAMJobStore无法履行作业和triggers上的“非易失性”设置

JDBC JobStore
要使用JDBCJobStore，必须首先创建一组数据库表以供Quartz使用
检索和更新触发triggers的时间通常将小于10毫秒。


JobStoreTX
确定应用程序需要哪种类型的事务。如果您不需要将调度命令（例如添加和删除triggers）绑定到其他事务，那么可以通过使用JobStoreTX作为JobStore 来管理事务（这是最常见的选择）。
如果您需要Quartz与其他事务（即J2EE应用程序服务器）一起工作，那么您应该使用JobStoreCMT - 在这种情况下，Quartz将让应用程序服务器容器管理事务。
一种方法是让Quartz创建和管理DataSource本身 - 通过提供数据库的所有连接信息。另一种方法是让Quartz使用由Quartz正在运行的应用程序服务器管理的DataSource，通过提供JDBCJobStore DataSource的JNDI名称

如果您的计划程序正忙（即几乎总是执行与线程池大小相同的job数量），那么您应该将DataSource中的连接数设置为线程池+ 2的大小。



TerracottaJobStore提供了一种缩放和鲁棒性的手段，而不使用数据库
TerracottaJobStore可以运行群集或非群集，并且在任一情况下，为应用程序重新启动之间持续的作业数据提供存储介质，因为数据存储在Terracotta服务器中。它的性能比通过JDBCJobStore使用数据库要好得多（约一个数量级更好），但比RAMJobStore要慢。
org.quartz.jobStore.class = org.terracotta.quartz.TerracottaJobStore
org.quartz.jobStore.tcConfigUrl = localhost:9510


如果要捕获关于triggers启动和jobs执行的额外信息

cluster


