wget https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-6.4.0.tar.gz

elasticsearch用root用户无法启动  需要添加用户
useradd -m elasticsearch


#解压到指定目录
mkdir elasticsearch-6.4.0
tar -zxvf  elasticsearch-6.4.0.tar.gz -C /home/elasticsearch/elasticsearch-6.4.0

mkdir /home/elasticsearch/elasticsearch-6.4.0-9300/data

启动elasticsearch 服务 
./elasticsearch -d

验证 
在浏览器上输入 http://10.250.209.89:9202/_cluster/health?pretty

ERROR: [3] bootstrap checks failed
[1]: max file descriptors [4096] for elasticsearch process is too low, increase to at least [65536]
[2]: max number of threads [3802] for user [elasticsearch] is too low, increase to at least [4096]
[3]: max virtual memory areas vm.max_map_count [65530] is too low, increase to at least [262144]


elasticsearch安装时遇到的错误

解决办法：

#切换到root用户修改 在最后面追加下面内容 elasticsearch是用户名
vi /etc/security/limits.conf

elasticsearch hard nofile 65536
elasticsearch soft nofile 65536

elasticsearch soft nproc 4096
elasticsearch hard nproc 4096

elasticsearch hard memlock unlimited
elasticsearch soft memlock unlimited

#切换到root用户修改配置sysctl.conf
vi /etc/sysctl.conf

vm.max_map_count=655360

并执行命令：sysctl -p


安装ElasticSearch 插件plugin-head(集群监控操作)

安装node
wget https://npm.taobao.org/mirrors/node/latest-v4.x/node-v4.4.7-linux-x64.tar.gz
tar -zxvf node-v4.4.7-linux-x64.tar.gz


export NODE_HOME=/home/elasticsearch/node-v4.4.7-linux-x64
export PATH=$PATH:$NODE_HOME/bin/
export NODE_PATH=$NODE_HOME/lib/node_modules

执行 source /etc/profile 

wget  https://github.com/mobz/elasticsearch-head/archive/master.zip
unzip master.zip
cd elasticsearch-head-master/
npm install
npm run start

