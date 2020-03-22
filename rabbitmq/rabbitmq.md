RabbitMQ

rabbitmq由pivotal公司开发,基于erlang.

产生背景/原因
解决什么问题
作用

进程间通信(IPC)方式一般有: RPC, HTTP
生产者 --MQ-- 消费者方式: RocketMQ, Kafka, ActiveMQ等

异步,解耦,消息缓冲(流量洪峰),分发

AMQP 应用层协议

组件设计: 生产者Producer, 队列Queue, 消费者Consumer
Queue里分发机制: 交换器Exchange
MQ服务器也叫broker server,里面主要的部分就是Exchange和Queue

交换器有四种:
DirectExchange: 必须routingkey完全相等,默认
TopicExchange: 按照routingKey过滤,基于通配符*(匹配一个) #(匹配0或多个),单词分隔符是点,x.y.z是三个词
FanoutExchange: 广播到所有的queue
HeadersExchange: 根据headers匹配,忽略routingkey

RabbitMQ收发过程:
1. 对于独立进程,第一步肯定是获取连接,使用工厂获取连接
2. 从连接中开辟一条通道
3. 在通道上进行收发操作

和spring结合
使用spring-boot-start-amqp

1. 定义连接串属性等
2. 设置RabbitTemplate Bean
3. 定义Exchange,名字
4. 定义Queue, 名字,参数,是否持久化等
5. 绑定Exchange和Queue,得到一个binding.如BindingBuilder.bind(q).to(ex).with(routingKey)
6. 使用template.convertAndSend
7. 定义一个Component用来消费,使用@RabbitListener(queues=name)和@RabbitHandler消费

注意: 消息队列里的消息是一次性消费,消费了就没有了.但如果手动ack,还有的




管理后台 localhost:15672 界面自动刷新,可以管理 vhost, queue, exchange, user等

Windows安装使用scoop很方便

---

异步: 提高吞吐量,缩短业务处理时间

解耦: 防止连带效应,雪崩,比如依赖的服务失败导致当前服务失败

流量削峰: 秒杀等,在mq处过滤

日志处理: 大数据等(所有记录写入文件,然后收集到消息中间件如kafka,然后流计算实时计算,然后展示大屏幕;或者归档离线计算,导入mysql,出报表)



大流量不写数据库(有行锁),一般都写到文件里

日志收集: flume td-agent logstash等



安装: scoop安装即可

启动控制台: rabbitmq-plugins enable rabbitmq_management

http://localhost:15672

默认用户 guest/guest 超级管理员

启动: rabbitmq-server start/stop/restart



vhost相当于数据库,一般以/开头,如/comment



端口: amqp 5672 clustering 25672 http 15672



连接mq的底层代码类似数据库连接,连接工厂 - 设置host port vhost user password等 - 获取连接 - 开辟channel - 关联queue - 发送

从管理界面可以get messages,注意ackmode,防止消息被消费了,requeue的消息会有redelivered标签



简单队列: Producer和Consumer一一对应,queuename固定,耦合性高.不能一个P对应多个C; basicPublish和basicConsume对应



工作队列:

生产者一般只负责发送,效率很高;生产者一般跟业务关联,消费速度会慢一些.因此实际上都是1个生产者对应多个消费者

代码上没有什么区别,只是mq处理不一样,对于同一个q的消费者,默认使用轮询的方式(round-robin)分发消息.

如果想根据消费者的能力派发,需要使用channel.basicQos(1)告诉mq直到消费者ack之后才进行下一次分发.注意需要消费者手动应答.



autoAck: 自动确认,一旦分发就从内存删除,如果消费者故障,那么容易产生数据丢失.默认false.防止消费者异常导致消息丢失.

为了防止mq异常导致消息丢失,应该持久化消息: durable=true. 注意一旦创建了q,之后就不能修改durable属性了.会报错.

可以通过管理界面删除queue



订阅模式

之前的简单队列,工作队列都是一个消息只能被一个消费者消费,不能被多个消费者重复消费.订阅模式解决这个问题

一个p对应多个c,每个c都有自己的q,p发送消息到x,x分发到不同的q.

场景: 用户通知,有2个方式,邮件和短信,用户账户改变时,发送一条消息到mq,然后多个c分别发送mail和sms



消息发送到exchange,如果没有绑定queue,那么消息就丢失了.mq里只有queue有能力存储消息.

注意: 发送者面向Exchange发送消息,因此没有queue;消费者任何时候连上来都需要一个全新的完整的queue,而不是像之前的共享queue的那种机制大家只能消费queue里的一部分消息.因此使用临时queue,由server决定.

```java
String queueName = channel.queueDeclare().getQueue();
channel.queueBind(queueName, "exchange-name", "");
```

com.rabbitmq.client.AlreadyClosedException: channel is already closed due to clean channel shutdown

- Trying to publish a message to an exchange that doesn't exist
- Trying to publish a message with the immediate flag set that doesn't have a queue with an active consumer set

本案例发生的原因是使用了try-resources语句,无需显式关闭了--关闭一个已经关闭的channel就报错了



路由模式

之前的没有exchange的模式,都是匿名模式,exchange有几种:fanout direct topic header等

direct exchange: routingKey一致就匹配,缺点也是精确匹配,有的业务场景,需要模糊匹配,比如一个物品的所有操作记录,就不能一个个列举出来



主题模式 topic exchange

按照routingKey匹配, #一个或者多个, *单个

注意所有的routingKey匹配,都是指消费者和生产者之间的routingKey对应,生产者都是以一个具体的key发送,但是消费者可能是以一个具体的key(direct),也可能是以一个模式(topic)来匹配.这个和消息的内容无关.

**还有就是有exchange的发送,如果没有建立起来消费者,也就是说没有queue与这个exchange对应,那么生产者发送给exchange发送的消息都会丢失**



消息确认机制 - 事务确认

消息从producer发送到mq,producer怎么确认消息到达了mq??默认是不知道的,通过以下2种方式解决



AMQP事务机制

txSelect 设置当前的channel为事务模式

txCommit提交 txRollback回滚

这种方式和数据库很像,需要多次IO,会影响系统的吞吐量



生产者confirm模式

原理: 设置成confirm模式之后,mq服务器会对每一条消息生成一个唯一id,并将此id返回给生产者,或者在消息持久化之后返回给生产者.从而确认

注意: 一个老的queue是不能临时修改成confirm模式的,必须是一个新的queue

编程: 串行 waitForConfirms 等待反馈; 异步addConfirmListener

异步的话,需要自己维护一个unack的列表,用来处理业务逻辑,比如重发之类的,可以使用SortedSet



Spring集成

setPublisherReturns

set

basicRecover

RabbitTemplate.convertAndSend



问题

refund

return

延时消息
可靠性消息

集群

RPC