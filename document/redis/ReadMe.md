环境centos7 64位
https://redis.io/下载redis4.0.11

1、先安装gcc等软件、下载redis编译
 tar xzf redis-4.0.1.tar.gz
 cd redis-4.0.1
 make test
 #指定安装的位置
 make test PREFIX=/usr/local/redis install

 

2、修改配置文件
#端口根据对应的文件夹去配制端口 7000,7001,7002,7003,7004,7005 
port 7001
#根据本机所在的IP或hostname去配制 node1 node2 node3
bind 0.0.0.0
#redis后台运行
daemonize yes
#pidfile文件对应7000,7001,7002,7003,7004,7005
pidfile nodes_pid.pid
#开启集群 把注释#去掉
cluster-enabled yes
#集群的配置 配置文件首次启动自动生成 7000,7001,7002,7003,7004,7005
cluster-config-file nodes_config.conf
#请求超时 默认15秒，可自行设置
cluster-node-timeout 15000
#aof日志开启 有需要就开启，它会每次写操作都记录一条日志
appendonly yes

3、启动各个节点的redis 
   ./redis-server nodes.conf
   
#执行之后报错“ERROR:  Error installing redis:redis requires Ruby version >= 2.2.2.”，需要升级ruby
4、执行gem install redis命令
   4.1  yum install gem
   4.2  安装curl yum install curl
   4.3  安装RVM 执行报错中的gpg2 --recv-keys的命令。
        gpg2 --recv-keys 409B6B1796C275462A1703113804BB82D39DC0E3
        curl -L get.rvm.io | bash -s stable 

   4.4 source /usr/local/rvm/scripts/rvm

   #查看rvm库中已知的ruby版本
   4.5 rvm list known

   #安装一个ruby版本
   4.6 rvm install 2.3.3

   #使用一个ruby版本
   4.7 rvm use 2.3.3

   #卸载一个已知版本
   4.8 rvm remove 2.0.0
       ruby --version

   #再安装redis就可以了
   4.9 gem install redis

5、创建集群
    #ip地址不能使用127.0.0.1否则java客户端会连127.0.0.1的redis 该工具依赖Ruby环境和gem
    src/redis-trib.rb create --replicas 1 10.250.209.89:7000 10.250.209.89:7001 10.250.209.89:7002 10.250.209.89:7003 10.250.209.89:7004 10.250.209.89:7005 10.250.209.89:7006

6、部署结果验证
    使用客户端redis-cli二进制访问某个实例，执行set和get的测试。
    ./redis-cli -c -p 7000
    set foo bar
    get foo
    
#需要进入redis客户端
7、集群状态
    cluster info
    
8、节点状态
    cluster nodes

9、集群监控工具
   TreeNMS、RedisClusterManager
   
原理：
    redis4.0.11必须要6个节点以上，即3主3从，将2的14次方即16384个槽位分布到主节点上，每台主节点维护部分槽位，根据key的hash值对槽位数量取模存放的
相应的节点上，从节点负责对主节点做冗余备份

参考：
    https://blog.csdn.net/gw85047034/article/details/78689885
    https://blog.csdn.net/ljbmxsm/article/details/76598489