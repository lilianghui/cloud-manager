Docker Deamon:

1.生成CA私钥ca-key.pem，使用该私钥对CA证书签名, ca-key.pem是一个临时文件，最后可以删除。
    mkdir ~/docker
    openssl genrsa -out ~/docker/ca-key.pem 4096
    
2.使用CA私钥生成自签名CA证书ca.pem。生成证书时，通过-days 365设置证书的有效期。单位为天，默认情况下为30天。
    openssl req -x509 -sha256 -batch -subj '/C=CN/ST=ShangHai/L=PuDong/O=llh Co.,Ltd/OU=Laboratory/CN=www.lilianghui.cn' -new -days 365 -key ~/docker/ca-key.pem -out ~/docker/ca.pem

    生成CA证书时，需要提供一些公司信息。
    
    C表示国家，中国为CN。
    
    ST表示省，比如Sichuan。
    
    L表示城市，比如Chengdu。
    
    O表示公司，比如Ghostcloud Co.,Ltd。
    
    OU表示部门名字，比如Laboratory。
    
    CN表示公司域名，比如www.ghostcloud.cn。
    
3.生成服务器私钥server-key.pem和CSR(Certificate Signing Request)server-csr.pem。CN为DockerDaemon。 server-csr.pem是一个临时文件，生成server-cert.pem以后，可以删除。
    openssl genrsa -out ~/docker/server-key.pem 4096
    openssl req -subj '/CN=DockerDaemon' -sha256 -new -key ~/docker/server-key.pem -out ~/docker/server-csr.pem
    
4.使用CA证书生成服务器证书server-cert.pem。TLS连接时，需要限制客户端的IP列表或者域名列表。只有在列表中的客户端才能通过客户端证书访问Docker Daemon。在本例中，只允许127.0.0.1和192.168.1.100的客户端访问。如果添加0.0.0.0，则所有客户端都可以通过证书访问Docker Daemon。allow.list是一个临时文件，生成server-cert.pem以后，可以删除。
    echo subjectAltName = IP:127.0.0.1,IP:0.0.0.0 > ~/docker/allow.list
    openssl x509 -req -days 365 -sha256 -in ~/docker/server-csr.pem -CA ~/docker/ca.pem -CAkey ~/docker/ca-key.pem -CAcreateserial -out ~/docker/server-cert.pem -extfile ~/docker/allow.list

5.生成客户端私钥client-key.pem和CSRclient-csr.pem。CN为DockerClient。 client-csr.pem是一个临时文件，生成client-cert.pem以后，可以删除。
    openssl genrsa -out ~/docker/client-key.pem 4096
    openssl req -subj '/CN=DockerClient' -new -key ~/docker/client-key.pem -out ~/docker/client-csr.pem

6.使用CA证书生成客户端证书client-cert.pem。需要加入extendedKeyUsage选项。
    echo extendedKeyUsage = clientAuth > ~/docker/options.list
    openssl x509 -req -days 365 -sha256 -in ~/docker/client-csr.pem -CA ~/docker/ca.pem -CAkey ~/docker/ca-key.pem -CAcreateserial -out ~/docker/client-cert.pem -extfile ~/docker/options.list
    
7.成功生成了需要的证书和秘钥，可以删除临时文件。
    rm -f ~/docker/server-csr.pem ~/docker/client-csr.pem ~/docker/allow.list ~/docker/options.list
    
8.为了保证证书和私钥的安全，需要修改文件的访问权限。
    chmod 0444 ~/docker/ca.pem ~/docker/server-cert.pem ~/docker/client-cert.pem
    chmod 0400 ~/docker/ca-key.pem ~/docker/server-key.pem ~/docker/client-key.pem
    
9.重启Docker Daemon，加入ca.pem、server-cert.pem和server-key.pem。-H=0.0.0.0:2376表示Docker Daemon监听在2376端口。
    dockerd  --tlsverify --tlscacert=/root/docker/ca.pem --tlscert=/root/docker/server-cert.pem --tlskey=/root/docker/server-key.pem -H=0.0.0.0:2376
    
10.在客户端，运行docker命令时，加入ca.pem、client-cert.pem和client-key.pem。本例中，只有127.0.0.1和192.168.1.100的客户端可以访问Docker Daemon。
    docker --tlsverify --tlscacert=~/docker/ca.pem --tlscert=~/docker/client-cert.pem --tlskey=~/docker/client-key.pem -H=tcp://127.0.0.1:2376 

Deamon后台默认启动修改：

vi /usr/lib/systemd/system/docker.service
ExecStart=/usr/bin/dockerd  --tlsverify --tlscacert=/root/docker/ca.pem --tlscert=/root/docker/server-cert.pem --tlskey=/root/docker/server-key.pem -H=0.0.0.0:2376 -H unix://var/run/docker.sock

systemctl daemon-reload
systemctl restart docker

curl https://212.64.26.25:2376/version --cert /root/docker/client-cert.pem --key /root/docker/client-key.pem -k


Docker私有仓库Registry:

1、拉取镜像registry(仓库)和registry-web(用于访问仓库的UI界面) docker编排
    yum install -y  docker-compose
    docker pull docker-registry-web
    docker pull registry

2、创建目录，用于存放配置文件和作为私有仓库的镜像存储目录
    mkdir -p /root/data/registry_dir/conf/registry   #存放仓库的配置信息
    mkdir -p /root/data/registry_dir/conf/registry-web #存放仓库UI界面的配置信息
    mkdir -p /root/data/registry_dir/registry  #存放仓库的镜像
    mkdir -p /root/data/registry_dir/db  #仓库的访问信息
    mkdir -p /root/data/registry_dir/conf/registry/certs  #存放仓库https secret

3、生成证书
    openssl req -new -newkey rsa:4096 -days 365 \
                    -subj "/CN=localhost" \
                    -nodes -x509  \
                    -keyout /root/data/registry_dir/conf/registry-web/auth.key \
                    -out /root/data/registry_dir/conf/registry/auth.cert

	openssl req -new -subj "/CN=localhost" -newkey rsa:4096 -nodes -sha256 -keyout /root/data/registry_dir/conf/registry/certs/domain.key -x509 -days 365 -out /root/data/registry_dir/conf/registry/certs/domain.crt

	cat /root/data/registry_dir/conf/registry/certs/domain.crt >> /etc/pki/tls/certs/ca-bundle.crt

4、创建yml配置文件 
    /root/data/registry_dir/conf/registry/config.yml
    /root/data/registry_dir/conf/registry/registry-web/config.yml
    /root/data/registry_dir/conf/docker-compose.yml

5、启动容器
    cd /root/data/registry_dir/conf/docker-compose.yml
    docker-compose up -d
    
    
    docker run -d -p 5000:5000 -v /data/registry:/var/lib/registry --name registry --restart=always registry


6、验证
    docker login localhost:5000 
    
    
Docker常用命令           (容器是镜像的实例)
    docker images       列出镜像
    docker rmi          删除镜像
    docker ps -a        列出容器
    docker rm           删除容器
    docker export cbe3cb7799ed > update.tar     将容器导出到文件
    docker import - update < update.tar         创建一个新容器从基于导出的文件
    docker save 9610cfc68e8d > update.tar       导出镜像
    docker load < update.tar                    导入镜像

参考：
https://blog.csdn.net/yuhaitao8922/article/details/72996993
https://yeasy.gitbooks.io/docker_practice/content/image/build.html