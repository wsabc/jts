-------------------------
Windows 10 Hyper-V 安装 CentOS 7

1. 开启Hyper-v
2. 安装虚拟机(默认Generation 1, Default Switch)
3. 启动虚拟机,无法访问网络
4. 在hyper-v里新建一个Internal网络交换机如ICS
5. 在主机网络连接里使用网络连接共享机制,设置物理网卡或者无线网卡代理ICS网卡访问
6. 虚拟机可以访问网络,虚拟网卡ip为192.168.137.1
7. 设置虚拟机ip /etc/sysconfig/network-scritps/ifcfg-eth0修改内容如下:

BOOTPROTO=static
ONBOOT=yes
GATEWAY=192.168.137.1
IPADDR=192.168.137.8
NETMASK=255.255.255.0
DNS1=10.164.12.130 # same as your host

8. 重启电脑后,虚拟机又无法上网,因为windows10的bug,使用下列脚本修复:

```shell
Set-ExecutionPolicy RemoteSigned
$NetShare = New-Object -ComObject HNetCfg.HNetShare
$wlan = $null
$ethernet = $null

foreach ($int in $NetShare.EnumEveryConnection) {
  $props = $NetShare.NetConnectionProps.Invoke($int)
  if ($props.Name -eq "WLAN") {
    $wlan = $int;
  }
  if ($props.Name -eq "vEthernet (sunwind-dev)") {
    $ethernet = $int;
  }
}

$wlanConfig = $NetShare.INetSharingConfigurationForINetConnection.Invoke($wlan);
$ethernetConfig = $NetShare.INetSharingConfigurationForINetConnection.Invoke($ethernet);

$wlanConfig.DisableSharing();
$ethernetConfig.DisableSharing();

$wlanConfig.EnableSharing(0);
$ethernetConfig.EnableSharing(1);
```

具体步骤参考[老衲说](https://laona.dev/post/win10-hyperv-nat-network-tips/)

-------------------------

CentOS系统更换软件安装源

第一步：备份你的原镜像文件，以免出错后可以恢复。

mv /etc/yum.repos.d/CentOS-Base.repo /etc/yum.repos.d/CentOS-Base.repo.backup

第二步：下载新的CentOS-Base.repo 到/etc/yum.repos.d/

wget -O /etc/yum.repos.d/CentOS-Base.repo http://mirrors.aliyun.com/repo/Centos-7.repo
或者
curl -O http://mirrors.aliyun.com/repo/Centos-7.repo

更改CentOS-Media.repo使其为不生效：
enabled=0

第三步：运行yum makecache生成缓存
yum clean all
yum makecache

-------------------------

安装ifconfig

yum search ifconfig
yum install net-tools.x86_64

-------------------------

开启sshd

yum list installed | grep openssh-server
yum install openssh-server
vi /etc/ssh/sshd_config

Port 22
ListenAddress 0.0.0.0
ListenAddress ::

PermitRootLogin yes

PasswordAuthentication yes

sudo service sshd start

ps -e | grep sshd
netstat -an | grep 22

systemctl enable sshd.service
systemctl list-unit-files | grep sshd

----------------------------

piix4_smbus: SMBus base address uninitialized - upgrade BIOS or use force_addr=0xaddr
缺少组件smbus,但是不影响工作,可以使用以下命令关闭

lsmod | grep i2c_piix4
vi /etc/modprobe.d/blacklist.conf
blacklist i2c_piix4
reboot

----------------------------

安装docker

设置仓库
yum install -y yum-utils device-mapper-persistent-data lvm2
yum-config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo

安装 Docker Engine-Community
yum install docker-ce docker-ce-cli containerd.io

使用阿里云安装源                                                                                                                                                                                         
yum-config-manager --add-repo http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo                                                            
rpm --import http://mirrors.aliyun.com/docker-ce/linux/centos/gpg

yum makecache fast                                                                                                                                                                                
yum -y install docker-ce

添加aliyun镜像
/etc/docker/daemon.json

{
  "registry-mirrors": ["https://v2ltjwbg.mirror.aliyuncs.com"]
}

systemctl daemon-reload
systemctl restart docker

启动 Docker
systemctl start docker

systemctl enable docker.service

--------------------------

CentOS7官方源默认已经包含bash-completion，直接安装
yum -y install bash-completion
退出当前shell，重新登陆shell，即可生效

--------------------------

安装git
yum -y install git

--------------------------

CentOs安装oh my zsh

安装zsh包
yum -y install zsh
切换默认shell为zsh
chsh -s /bin/zsh
重启服务器让修改的配置生效
reboot
安装on my zsh
sh -c "$(curl -fsSL https://raw.githubusercontent.com/robbyrussell/oh-my-zsh/master/tools/install.sh)"
或者
sh -c "$(wget https://raw.githubusercontent.com/robbyrussell/oh-my-zsh/master/tools/install.sh -O -)"
修改oh my zsh配置

安装插件
git clone https://github.com/zsh-users/zsh-syntax-highlighting.git

git clone https://github.com/zsh-users/zsh-autosuggestions

plugins=(
  git osx extract z zsh-autosuggestions zsh-syntax-highlighting # 添加的插件
)

source .zshrc

--------------------------

设置dns

在CentOS 7下，手工设置 /etc/resolv.conf 里的DNS，过了一会，发现被系统重新覆盖或者清除了。和CentOS 6下的设置DNS方法不同，有几种方式： 1、使用全新的命令行工具 nmcli 来设置

显示当前网络连接
nmcli connection show
NAME UUID                                 TYPE           DEVICE
eno1 5fb06bd0-0bb0-7ffb-45f1-d6edd65f3e03 802-3-ethernet eno1

修改当前网络连接对应的DNS服务器，这里的网络连接可以用名称或者UUID来标识
nmcli con mod eno1 ipv4.dns "114.114.114.114 8.8.8.8"

将dns配置生效
nmcli con up eno1

--------------------------