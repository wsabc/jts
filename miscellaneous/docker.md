docker desktop for windows
信任私有镜像中心:
1. 添加insecure-registries

"insecure-registries": [
    "x.cn"
]

2. 添加crt到windows certmgr中Trusted CA

如果是客户端证书, 放到 C:\ProgramData\Docker\certs.d\ 私有镜像中心带端口号 \ ca.crt

