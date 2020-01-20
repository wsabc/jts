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
定时任务常见方式:
crontab+sql
script(python)+sql
spring+timer
常见问题:
直接操作db,增加db压力,可能会引入过多的中间表
业务/代码不能重用
无法负载均衡
不能恢复失败的任务
异构系统,增加团队沟通成本
分布式场景存在任务竞争问题,可能会写死运行机器

较好的方案:
javabased, spring集成, 高可用, 负载均衡
参考: quartz jcrontab cron4j elastic-job等

quartz集群原理分析(https://tech.meituan.com/2014/08/31/mt-crm-quartz.html):
Spring通过提供org.springframework.scheduling.quartz下的封装类对Quartz支持
qz集群通过共享数据库来感知整个集群
<prop key="org.quartz.jobStore.isClustered">true</prop>
<prop key="org.quartz.jobStore.clusterCheckinInterval">15000</prop>
<property name="startupDelay" value="30" />应用启动完后在启动qz调度器
<property name="overwriteExistingJobs" value="true" />

集群中节点依赖于数据库来传播Scheduler实例的状态，你只能在使用JDBC JobStore时应用Quartz集群
org.quartz.jobStore.isClustered属性为true，通知Scheduler实例要它参与到一个集群当中
org.quartz.jobStore.clusterCheckinInterval属性定义了Scheduler实例检入到数据库中的频率(单位：毫秒)。
Scheduler检查是否其他的实例到了它们应当检入的时候未检入；这能指出一个失败的Scheduler实例，且当前 Scheduler会以此来接管任何执行失败并可恢复的Job。
通过检入操作，Scheduler 也会更新自身的状态记录。clusterChedkinInterval越小，Scheduler节点检查失败的Scheduler实例就越频繁。默认值是 15000 (即15 秒)。

前提: 分布式部署时需要保证各个节点的系统时间一致
核心表:
QRTZ_SCHEDULER_STATE	存储少量的有关Scheduler的状态信息，和别的Scheduler实例
QRTZ_LOCKS	存储程序的悲观锁的信息

Quartz线程模型
SimpleThreadPool创建了一定数量的WorkerThread实例来使得Job能够在线程中进行处理
QuartzScheduler被创建时创建一个QuartzSchedulerThread实例,Scheduler调度线程
QuartzScheduler调度线程不断获取trigger，触发trigger，释放trigger

select * from QD_QRTZ_LOCKS where sched_name = '' and lock_name = '' for update
当一个线程使用上述的SQL对表中的数据执行查询操作时，若查询结果中包含相关的行，数据库就对该行进行ROW LOCK；
若此时，另外一个线程使用相同的SQL对表的数据进行查询，由于查询出的数据行已经被数据库锁住了，
此时这个线程就只能等待，直到拥有该行锁的线程完成了相关的业务操作，执行了commit动作后，数据库才会释放了相关行的锁，这个线程才能继续执行。

具体执行过程:
node1: select...for update,nodata,nolock,insert,updateTrigger,commit/rollback,job execute
node2: select...for update,nodata,nolock,insert,updateTrigger,commit/rollback == 和node1竞争,两个至少有一个成功
node3: select...for update,havedata, lock,wait,selectTrigger=nodata,return

acquireNextTriggers
triggersFired
releaseAcquiredTrigger
每个方法都在自己的事务里
更新完trigger以后就run job
shell.initialize(qs);
qsRsrcs.getThreadPool().runInThread(shell) 

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
