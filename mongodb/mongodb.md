MongoDB



`MongoDB`是基于分布式文件存储的数据库，由`C++`语言编写

解决什么问题(应用场景)?

怎么解决的(原理)?

具体实施(工程能力)?



现代应用特征: 高并发,海量数据 => 高性能读写,高可用,易扩展,MongoDB能满足三高要求.



具体场景:

社交: 微信,海量数据,如朋友圈,位置信息等

游戏: 海量数据,高效读写.如经验值,积分等

物流: 海量数据,实时更新,如快递位置等

物联网: 大数据

直播: 点赞互动等



**海量数据,操作频繁读写要求高,数据价值低,事务一致性要求不高**



查询较简单,数据模型待定(有很多扩展字段)



无模式: 没有具体的列(字段),使用类似JSON的BSON格式,是最接近RDB的NOSQLDB.



和RDB类比: 一个RDB是一个数据库,有多个表,每个表有很多行,一行很多列

MongoDB: 一个数据库,有很多集合collection,每个集合有很多文档document,一个文档有很多域field



因为是无模式的文档,所以叫document和collection很合理,其实就是文件数据库



支持index,不支持join,通过嵌入式文档来实现多个collection连接,性能比join要好.

支持primary key,无需用户指定,MongoDB自动设定_id字段为主键



BSON支持的特殊类型:

对象ID, 64位浮点数(默认), date, binary data, regular expression, code

NumberInt 和NumberLong可以将浮点型转换成整型



安装 scoop install mongodb

启动 mongod --dbpath=../data/db 默认开启**27017**端口,开发时可以这种方式

也可以使用配置文件方式, mongod.conf,启动mongod -f ../conf/mongd.conf (-f可以用--config)

```shell
storage:
	dbpath: d:\it\mongod\data\db
```

验证连接

```shell
mongo #连接本机
mongo --host=localhost --port=27017 #连接具体机器
show dbs #列出所有db
```

也可使用compass GUI管理器



关闭mongod: 进入mongo然后use admin, db.shutdownServer()即可.也可直接kill -2 pid (2sigint程序终止(interrupt)信号, 在用户键入INTR字符(通常是Ctrl-C)时发出，用于通知前台进程组终止进程)



基本操作

创建db: use db_name 如果有就切换,没有就创建.创建的db在内存里,当有数据时就会持久化到磁盘.show dbs显示的是磁盘里的数据库. db命令可以显示当前的使用的数据库,db.dropDatabase()可以删除数据库



admin:类似root数据库,用于管理

local: 不会被复制,只用于存储本机的一些集合

config: 集群设置时的分片设置等



集合

显示创建 db.createCollection(name)

隐式创建 通过直接创建文档从而创建集合

列出集合show collections

删除集合db.collection_name.drop()



文档

db.collection_name.insert() 插入单条,如果collection不存在,会首先创建collection.

db.collection_name.save()等同于insert.插入多条使用insertMany()

一般就关心document这个参数即可.还有2个参数writeConcern和order



查询使用db.collection_name.find()即可查询全部

find({key:value})可以设置查询条件

findOne则可以查询第一条

投影查询 db.collection_name.find({sex:”M”}, {id:1, name:1, _id:0})表示查询男性,只显示id和name域

```shell
db.users.insert({id:1,name:"ws",sex:"M"});
db.users.find()
db.users.find({id:1})
db.users.find({id:1},{id:1, name:1, _id:0})
 db.users.find({},{id:1, name:1, _id:0}) # 不带条件,如果要指定字段,需要传一个{}query
```

_id mongodb可以帮助我们生成,但是也可以自己指定

insertMany不会因为某一条文档插入失败而回滚,如果需要知道,使用try catch语法可以print(e)会汇报错误的那一条

```sql
try { db.users.insertMany([{id:3,name:"ws2",sex:"M"},{_id:"5e6ce32469cea028ff631bb7", id:4,namex:"wsx",sex:"M"}]) } catch(e) {print(e)}
BulkWriteError({
        "writeErrors" : [
                {
                        "index" : 1,
                        "code" : 11000,
                        "errmsg" : "E11000 duplicate key error collection: test.users index: _id_ dup key: { _id: \"5e6ce32469cea028ff631bb7\" }",
                        "op" : {
                                "_id" : "5e6ce32469cea028ff631bb7",
                                "id" : 4,
                                "namex" : "wsx",
                                "sex" : "M"
                        }
                }
        ],
        "writeConcernErrors" : [ ],
        "nInserted" : 1,
        "nUpserted" : 0,
        "nMatched" : 0,
        "nModified" : 0,
        "nRemoved" : 0,
        "upserted" : [ ]
})
```



如果批量插入的时候，中间有一个文档插入失败，那么前面的文档插入成功，而后面的文档则全部插入失败。



修改语法: db.collection_name.update(query, update, options)

覆盖式修改即全量覆盖 db.users.update({_id:”1”}, {name:”ws”})修改后只有name这个字段了

指定修改: db.users.update({username: "ws"}, {$set:{sex:"M"}})默认只修改第一条,如果修改所有匹配的文档,需要追加参数 {multi:true}

列值增长修改: $inc:{likenum:NumberInt(1)} 将likenum增长1



删除: db.collection_name.remove

全部删除 remove({})

指定删除 remove({userid: "1002"})



分页查询

统计: db.collection_name.count

count()统计所有, count({sex: "M"})按条件统计

翻页db.users.find().limit(2) 第一页2条 db.users.find().limit(2).skip(2) 第二页2条



排序 find().sort({userid: 1}) 1表示升序, -1表示降序



复杂查询

正则查询: find({name: /regexp/}) 完全兼容js的正则表达式语法

比较查询: find({age: {$gt: 18}}) 类似的还有 lt, gte, lte, ne

包含查询(in):  find({userid: {$in: ["1", "2"]}}) not in = nin

条件查询: $and 类似的or

```sql
db.users.find({$and: [ {likenum: {$gt:100}}, {likenum: {$lt: 1000}} ]}) -- likenum > 100 and likenum < 1000
```



索引

mysql B+tree mongo Btree

单字段索引 {userId : 1} userId 升序索引

复合索引 {userId :1, score: -1} userId升序, score降序 联合索引

其他索引: 地理空间索引, 文本索引, 哈希索引等



查看 db.collection_name.getIndexes() 默认索引有_id, v表示版本号, 默认索引名为字段名加下划线加排序方式

创建 db.collection_name.createIndex({userid:1}) 还可以指定选项比如unique,或者指定名字

移除 dropIndex("index_name") 或者 按照文档属性来删除 dropIndex({"userid:1"}),文本索引只能按照名字来删

dropIndexes()删除所有索引(_id这个内置的索引不会被删)

执行计划: db.users.find({name:”ws”}).explain(),也可以通过compass界面来看

collscan表示全集合扫描,没有用上索引;如果是fetch/ixscan表示用上了索引

涵盖查询covered query,表示查询的字段就在索引中,不需要再去文档里查找数据,直接从索引返回.效率更高

compass会显示coverd by index



java开发

spring-boot-starter-data-mongodb

spring.data.mongodb.uri=mongodb://localhost:27017/article单机连接方式

log显示Opened connection [connectionId{localValue:1, serverValue:2}] to localhost:27017即可

实体document的写法和jpa entity类似, dao集成mongorepository

复杂的使用mongoTemplate,以及query和update类



集群(副本集 Replica Sets)

主从复制有固定的主,副本集没有固定的主,通过选举产生

主节点primary: 负责写

次要节点Secondary: 冗余备份节点,负责读和选举,读需要配置才能起作用,是默认角色

仲裁节点Arbiter: 不保留任何数据,只负责选举投票,副本成员可以是仲裁节点



副本集的名称要一致

按照端口号不同,可以再一台机器上启动三个服务,主,副本,仲裁

使用配置文件的方式

启动主节点,初始话集群, rs.initiate(),启动后显示ok, 身份也从SECONDARY变成PRIMARY

rs.config()可以获取配置, rs.status()可以查看状态,主要关注members属性



rs.add(host, isarb)添加副本, rs.addArb(host)添加仲裁者

rs.slaveOk()设置副本可读,否则报错 not master and slaveOk=false

仲裁节点只有local数据库,表示只存储配置信息



选举原则: 主网路故障(hb默认10秒), 或者人工干预rs.stepDown(600)

获胜原则: 票数最高且超过一半, 票数相同看数据新旧(比较oplog)

票的质量看优先级, priority默认是1, 仲裁者的优先级是0

如果仲裁节点和主机点都挂了,剩下的副本不会升级为主,因为票数没有超过大多数

**<u>仲裁和副本都挂掉的话,主会降级为副本</u>**



集群连接: uri=mongodb://host1,host2,host3/dbname?connect=replicaSet&slaveOk=true&replicaSet=replicaName

slaveOk=true自动读写分离



分片sharding

三个组件: 分片副本集, 路由mongos,调度config servers

sharding.clusterRole: shardsvr或者configsvr

具体操作:

1. 先搭建普通分片1和2, 一主一从一仲裁,创建数据文件夹,添加配置文件,启动,初始化,添加副本和仲裁
2. 搭建配置服务,一主两从,主要是利用副本存储,不需要仲裁
3. 搭建路由服务,不需要data文件夹,只需要log,配置文件mongos.conf,配置sharding:configDB:name/h1:p1,h2:p2,h3:p3,使用mongos启动,添加分片sh.addShard(ip,port)一次添加多个shard,使用shardname/ip1:port1,h2:p2,h3:p3;使用sh.status查看
4. 开始分片功能,sh.enableShard(数据库);然后enableCollection(namespace, key),ns格式是db.collection,key有具体的分片策略如rs.enabledCollection(“article.comment”, {“nickname”:”hashed”})
5. 分片策略:hashed,范围策略.mongodb一个集合只能按照一个字段进行分片.范围策略默认放在一个分片上,数据量大了才分配,默认的chunksize是64m.可以通过配置修改use config, db.setting.save({_id:"chunksize", value:1}).按照范围查询的适合使用范围策略.一般还是使用hash策略,hash上来就分好片,范围则是后期根据数据再分
6. 第二个路由不用过多设置,因为第一个的信息都同步到config server里了
7. 连接就使用路由连接了,格式mongodb://r1:p1,r2:p2/dbname. springdata默认会有负载均衡策略,随机选一个



安全认证

role/resource(库,集合等)&action(CRUD等)设计,常见的有readWrite, dbAdmin, root等

use admin

db.createUser({user: “myroot”, pwd: “111”, roles: [{role: “root”, db: “admin”}]}) 创建admin root账户

db.system.users.find() 查找用户

db.dropUser(“username”)

db.changeUserPassword(“username”, “password”)

db.auth(“username”, “password”)

服务器开启认证:--auth,或者security: authentication:enabled

uri: mongodb://user:password@ip:port/dbname



副本集安全认证

app访问: mongodb://user:password@host1,host2,host3/dbname?connect=replicaSet&slaveOK=true&replicaSet=myrs

集群内部访问: key文件 openssl rand -base64 90 -out mogo.keyfile 集群内部使用同一份key文件

security:keyFile: filepath



分片安全认证

基本和副本集一样

一旦开启安全认证之后,必须使用localhost登录才能创建账户



GUI

官方的compass

idea的插件 mongo4idea https://github.com/dboissier/mongo4idea 或者考虑 DataGrip

Studio 3T https://studio3t.com/knowledge-base/categories/mongodb-tutorials/



Mongo+SRV方式连接

---

测试



存储引擎?

性能指标?

查询(aggregation pipe, text search, geospatial queries)

Embedded Documents

MongoDB and MySQL

MongoDB and Redis

事务?

更新磁盘频率?

不会,磁盘写操作默认是延迟执行的.写操作可能在两三秒(默认在60秒内)后到达磁盘，通过 `syncPeriodSecs` 启动参数，可以进行配置.例如,如果一秒内数据库收到一千个对一个对象递增的操作,仅刷新磁盘一次.

索引类型?

moveChunk失败了怎么办?

不需要，移动操作是一致(`consistent`)并且是确定性的(`deterministic`)。一次失败后，移动操作会不断重试,当完成后，数据只会出现在新的分片里(shard).更新操作会立即发生在旧的块（Chunk）上，然后更改才会在所有权转移前复制到新的分片上。



如果一个分片停止了，除非查询设置了 “`Partial`” 选项，否则查询会返回一个错误。如果一个分片响应很慢，`MongoDB` 会等待它的响应。



如果业务中存在大量复杂的事务逻辑操作，则不要用`MongoDB`数据库；在处理非结构化 / 半结构化的大数据使用`MongoDB`，操作的数据类型为动态时也使用`MongoDB`



文件结构?



MongoDB支持主键外键关系吗?



ObjectID组成?

一共有四部分组成:时间戳、客户端ID、客户进程ID、三个字节的增量计数器



MongoDB支持存储过程吗？



MongoDB采用的预分配空间的方式来防止文件碎片。所以文件很大



查看 Mongo 正在使用的链接

db._adminCommand(“connPoolStats”);