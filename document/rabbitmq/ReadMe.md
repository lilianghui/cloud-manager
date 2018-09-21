wget http://erlang.org/download/otp_src_19.3.tar.gz

yum -y install make gcc gcc-c++ kernel-devel m4 ncurses-devel openssl-devel unixODBC-devel
在Linux上安装ErLang环境时，如果出现  configure: error: No curses library functions found 提示 ，需要首先安装 ncurses-devel

./configure --prefix=/usr/erlang --without-javac 
make install
./bin/erl


export PATH=$PATH:/usr/erlang/lib/erlang/bin

wget https://www.rabbitmq.com/releases/rabbitmq-server/v3.6.8/rabbitmq-server-3.6.8-1.el7.noarch.rpm
rpm --import https://www.rabbitmq.com/rabbitmq-release-signing-key.asc
rpm -i --nodeps rabbitmq-server-3.6.8-1.el7.noarch.rpm


配置
创建/etc/rabbitmq/rabbitmq-env.conf

mkdir -p /usr/local/rabbitmq-server/data
mkdir -p /usr/local/rabbitmq-server/log

rabbitmq-env.conf:
RABBITMQ_MNESIA_BASE=/usr/local/rabbitmq-server/data
RABBITMQ_LOG_BASE=/usr/local/rabbitmq-server/log

cp /usr/share/doc/rabbitmq-server-3.6.8/rabbitmq.config.example /etc/rabbitmq
mv /etc/rabbitmq/rabbitmq.config.example /etc/rabbitmq/rabbitmq.config

RabbitMQ平时使用rabbitmq.config默认配置即可，此处修改下hipe_compile的属性为true就算完事了！

安装下RabbitMQ服务: rabbitmq-server install

启动RabbitMQ服务可以通过命令:rabbitmq-server start

添加rabbitmq到启动项可以通过命令：chkconfig rabbitmq-server on

还有就是开启管理界面可以通过命令：rabbitmq-plugins enable rabbitmq_management 

参考
https://www.cnblogs.com/ylsforever/p/6600925.html