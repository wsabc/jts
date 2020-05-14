docker desktop for windows
信任私有镜像中心:
1. 添加insecure-registries

"insecure-registries": [
    "x.cn"
]

2. 添加crt到windows certmgr中Trusted CA

如果是客户端证书, 放到 C:\ProgramData\Docker\certs.d\ 私有镜像中心带端口号 \ ca.crt

docker常用命令就2类: image管理和container管理

#### Docker基础

##### 配置

1. 配置aliyun镜像加速器

##### 概念

1. 镜像(类), 容器(实例)

##### 命令

1. 运行示例

```shell
> docker run ubuntu:15.10 /bin/echo "Hello world"
# Docker 以 ubuntu15.10 镜像创建一个新容器, 然后在容器里执行 bin/echo "Hello world", 然后输出结果
```

```shell
> docker run -i -t ubuntu:15.10 /bin/bash
# 在新容器内指定一个伪终端通过标准输入STDIN进行交互
> exit
# 退出容器
```
2. 镜像管理

```shell
> docker images
# 列出本地主机上的镜像

> docker search httpd
# 从 Docker Hub 网站搜索镜像

> docker pull httpd
# 安装镜像

> docker tag 860c279d2fec runoob/centos:dev
# 为镜像添加一个新的标签,本例dev,860c279d2fec是src img

> docker rmi httpd
# 删除镜像

> docker build -t runoob/centos:6.7 .
# 创建一个新的镜像, -t指定名字, .指定Dockfile的路径

> docker push username/ubuntu:18.04
# 将自己的镜像推送到 Docker Hub
```
> FROM    centos:6.7
> MAINTAINER      Fisher "fisher@sudops.com"
>
> RUN     /bin/echo 'root:123456' |chpasswd
> RUN     useradd runoob
> RUN     /bin/echo 'runoob:123456' |chpasswd
> RUN     /bin/echo -e "LANG=\"en_US.UTF-8\"" >/etc/default/local
> EXPOSE  22
> EXPOSE  80
> CMD     /usr/sbin/sshd -D
>
> Dockfile示例,每一个指令的前缀都必须是大写.

3. 容器管理

```shell
> docker ps
# 查看容器, -l参数查询最后一次创建的容器

> docker stop cid
# 停止容器

> docker logs cid
# -f参数有类似tail -f作用,显示容器日志
```

```shell
> docker run -d ubuntu:15.10 /bin/sh -c "while true; do echo hello world; sleep 1; done"
> 2b1b7a428627c51ab8810d541d759f072b4fc75487eed05812646b8534a2fe63
# -d指定容器运行模式,返回容器id

> docker attach/exec cid
# 进入正在后台运行的容器,推荐使用exec,因为attach会导致容器退出

> docker top wizardly_chandrasekhar
# 查看容器内部运行的进程

> docker inspect wizardly_chandrasekhar
# 查看 Docker 的底层信息,它会返回一个 JSON 文件记录着 Docker 容器的配置和状态信息
```

```shell
> docker rm -f cid
# 删除容器

> docker container prune
# 清理所有处于终止状态的容器

> docker commit -m="has update" -a="runoob" e218edb10161 runoob/ubuntu:v2
# -m描述 -a作者 e...1容器id r...2要创建的镜像名
# 提交容器副本

> docker run -d -P --name runoob training/webapp python app.py
# --name 命名容器
```

4. 网络端口管理

```shell
> docker run -d -P training/webapp python app.py
# -P是容器内部端口随机映射到主机的高端口
# -p是容器内部端口绑定到指定的主机端口
# 默认都是绑定 tcp 端口,如果要绑定 UDP 端口,可以在端口后面加上 /udp

> docker port bf08b7f2cd89
# 查看指定 ID 或者名字的容器某个确定端口映射到宿主机的端口号

```

```shell
> docker network create -d bridge test-net
# 创建一个docker网络, -d指定网路类型,有bridge,overlay

> docker run -itd --name test1 --network test-net ubuntu /bin/bash
# 运行一个容器并加入指定网络
# 如有多个容器之间需要互联,推荐使用docker compose
```

```shell
> docker run -it --rm ubuntu  cat etc/resolv.conf
# 输出容器的 DNS 信息

> docker run -it --rm -h host_ubuntu --dns=114.114.114.114 --dns-search=test.com ubuntu
# 给指定的容器设置 DNS
# -h HOSTNAME,设定容器的主机名,它会被写到容器内的 /etc/hostname 和 /etc/hosts
# --dns=IP_ADDRESS,添加 DNS 服务器到容器的 /etc/resolv.conf 中,让容器用这个服务器来解析所有不在 /etc/hosts 中的主机名
# --dns-search=DOMAIN,设定容器的搜索域,在搜索一个名为 host 的主机时,DNS 不仅搜索 host,还会搜索 host.example.com
```

> 在宿主机的 /etc/docker/daemon.json 文件中增加以下内容来设置全部容器的 DNS:
> {
>   "dns" : [
>     "114.114.114.114",
>     "8.8.8.8"
>   ]
> }
> 注意重启 docker 才能生效.

5. Dockerfile

6. Docker Compose

   Compose 是用于定义和运行多容器 Docker 应用程序的工具,可以使用 YML 文件来配置应用程序需要的所有服务,使用一个命令就可以从 YML 文件配置中创建并启动所有服务.

   * 使用 Dockerfile 定义应用程序的环境
   * 使用 docker-compose.yml 定义构成应用程序的服务,这样它们可以在隔离环境中一起运行
   * 最后执行 docker-compose up 命令来启动并运行整个应用程序

7. Docker Machine

   Docker Machine 是一种可以在虚拟主机上安装 Docker 的工具,并可以使用 docker-machine 命令来管理主机;也可以集中管理所有的 docker 主机如快速的给 100 台服务器安装上 docker

   Docker Machine 也可以集中管理所有的 docker 主机，比如快速的给 100 台服务器安装上 docker。

8. Docker Swarm

   Docker Swarm 是 Docker 的集群管理工具,它将 Docker 主机池转变为单个虚拟 Docker 主机. swarm 集群由管理节点manager和工作节点work node构成:**mananger**负责整个集群的管理工作包括集群配置,服务管理等所有跟集群有关的工作;**node**主要负责运行相应的服务来执行任务.

9. 例子

   [Nginx容器教程](https://www.ruanyifeng.com/blog/2018/02/nginx-docker.html)

   
