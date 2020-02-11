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

注意: 消息队列里的消息是一次性消费,消费了就没有了.但如果手动ack,还有有的

confirm
refund
ack
延时消息
可靠性消息
事务
集群


管理后台 localhost:15672 界面自动刷新,可以管理 vhost, queue, exchange, user等


Windows安装使用scoop很方便
