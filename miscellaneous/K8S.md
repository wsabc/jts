K8S Quick Start

环境准备

确认开启虚拟化
grep -E --color 'vmx|svm' /proc/cpuinfo

安装kubectl
cat <<EOF > /etc/yum.repos.d/kubernetes.repo
[kubernetes]
name=Kubernetes
baseurl=https://packages.cloud.google.com/yum/repos/kubernetes-el7-x86_64
enabled=1
gpgcheck=1
repo_gpgcheck=1
gpgkey=https://packages.cloud.google.com/yum/doc/yum-key.gpg https://packages.cloud.google.com/yum/doc/rpm-package-key.gpg
EOF

上面的无法安装,使用aliyun

cat <<EOF > /etc/yum.repos.d/kubernetes.repo
[kubernetes]
name=Kubernetes
baseurl=http://mirrors.aliyun.com/kubernetes/yum/repos/kubernetes-el7-x86_64
enabled=1
gpgcheck=0
repo_gpgcheck=0
gpgkey=http://mirrors.aliyun.com/kubernetes/yum/doc/yum-key.gpg
       http://mirrors.aliyun.com/kubernetes/yum/doc/rpm-package-key.gpg
EOF


yum install -y kubectl

安装minikube

curl -LO https://storage.googleapis.com/minikube/releases/latest/minikube-1.6.1.rpm \
 && sudo rpm -ivh minikube-1.6.1.rpm
If you are already running minikube from inside a VM, it is possible to skip the creation of an additional VM layer by using the none driver

确认安装
minikube start --vm-driver=none

minikube status

使用阿里云版
curl -Lo minikube https://github.com/kubernetes/minikube/releases/download/v1.5.2/minikube-linux-amd64 && chmod +x minikube && sudo mv minikube /usr/local/bin/

