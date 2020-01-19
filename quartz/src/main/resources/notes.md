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

components:
Scheduler, Job, JobDetail, Trigger, Builders, JobStore

Scheduler:
Scheduler can't start until re-initialize once shutdown
Trigger can fire only if the scheduler is running(note even pause it not allowed)

job & trigger decoupling:
For some jobs misfired, we can add trigger to let it up
For some jobs, there may have multiple triggers, avoid to copy in this case

job & jobDetail design - job properties:
Field XXX in job and if they are also in JobDataMap, then setXXX in job can be called by JobFactory

JobExecutionContext can get JobDataMap from jobDetail/trigger, also can getMergedJobDataMap from both(override)

@PersistJobDataAfterExecution @DisallowConcurrentExecution both are JobDetail restrictions but annotated in Job class

Non Durability jobs without triggers will be deleted from scheduler, its lifecycle is determined by trigger
RequestsRecovery can make durable jobs run(recovery) if the scheduler occurs an error(or hard shutdown) and restart

Default priority is 5 and only concurrent triggers need compare priority

Scheduler checks all misfired triggers and handle them according to their policy(smart policy)

If interval is 0 then the trigger will run the job concurrently(repeat count or scheduler thread count)
TriggerBuilder will generate random name for trigger, and make it effective now if not set startAt(xxx)

Job Stores:
RAMJobStore无法履行作业和triggers上的非易失性设置

JDBC JobStore
要使用JDBCJobStore，必须首先创建一组数据库表以供Quartz使用
检索和更新触发triggers的时间通常将小于10毫秒

确定应用程序需要哪种类型的事务:如果您不需要将调度命令绑定到其他事务,那么可以通过使用JobStoreTX作为JobStore来管理事,这是最常见的选择
如果您需要Quartz与其他事务一起工作,那么您应该使用JobStoreCMT,在这种情况下,Quartz将让应用程序服务器容器管理事务

JobStoreTX
一种方法是让Quartz创建和管理DataSource本身:通过提供数据库的所有连接信息
另一种方法是让Quartz使用由Quartz正在运行的应用程序服务器管理的DataSource,通过提供JDBCJobStore DataSource的JNDI名称

如果您的计划程序正忙(即几乎总是执行与线程池大小相同的job数量)那么您应该将DataSource中的连接数设置为线程池+ 2的大小

TerracottaJobStore
提供了一种缩放和鲁棒性的手段,而不使用数据库,它的性能比通过JDBCJobStore使用数据库要好得多,约一个数量级更好,但比RAMJobStore要慢
TerracottaJobStore可以运行群集或非群集,并且在任一情况下,为应用程序重新启动之间持续的作业数据提供存储介质
org.quartz.jobStore.class = org.terracotta.quartz.TerracottaJobStore
org.quartz.jobStore.tcConfigUrl = localhost:9510

如果要捕获关于triggers启动和jobs执行的额外信息,使用
org.quartz.plugin.triggHistory.class = org.quartz.plugins.history.LoggingTriggerHistoryPlugin

cluster

QuartzDesk
QuartzDesk是一个Java Quartz调度器管理和监控的图形化工具,旨在为使用Quartz的Java开发者提供查询和监控的功能
3 components: web, agent, api
using: db(mysql), web container(tomcat)
1. enable JMX on Quartz JVM
2. add connection on QuartzDesk to #1
Quartz提供Mbean,QuartzDesk调用这些Mbean.

quartzdesk-api-x.y.z.jar in Quartz Application used for what?

Spring & Quartz

using spring to provider scheduler/job/jobdetail/trigger bean
using @QuartzDataSource to provider a different datasource
