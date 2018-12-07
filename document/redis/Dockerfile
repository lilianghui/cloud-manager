FROM centos

MAINTAINER lilianghui <810653725@qq.com>

ENV HOME /root
ENV PORT 6379
ENV NEW_HOST ''
ENV EXIST_HOST ''
ENV MASTER true
ENV GPG2_KEY_SERVER keyserver.ubuntu.com


#安装准备
RUN  yum install -y gcc libc-dev make automake autoconf libtool pcre pcre-dev zlib zlib-dev openssl-dev wget vim gem curl

ADD ruby-2.5.3.tar.gz ${HOME}

RUN cd ${HOME} \
    && wget http://download.redis.io/releases/redis-5.0.2.tar.gz
#    && wget https://cache.ruby-lang.org/pub/ruby/2.5/ruby-2.5.3.tar.gz

RUN cd ${HOME}/ruby-2.5.3\
    && yum remove -y ruby \
    && ./configure \
    && make && make install

RUN cd ${HOME} \
    && tar -zxvf redis-5.0.2.tar.gz \
    && cd redis-5.0.2 \
    && make && make install PREFIX=/usr/local/redis \
    && mkdir -p $HOME/redis \
#    && gpg2 --keyserver $GPG2_KEY_SERVER --recv-keys 409B6B1796C275462A1703113804BB82D39DC0E3 \
#    && curl -L get.rvm.io | bash -s stable \
#    && source /usr/local/rvm/scripts/rvm \
#    && /usr/local/rvm/scripts/rvm remove 2.0.0\
#    && /usr/local/rvm/scripts/rvm install 2.3.3\
#    && /usr/local/rvm/scripts/rvm use 2.3.3\
#    && gem install redis \
    && cp ${HOME}/redis-5.0.2/*/mkreleasehdr.sh ${HOME}/redis-5.0.2/*/redis-trib.rb  ${HOME}/redis-5.0.2/*/redis-benchmark ${HOME}/redis-5.0.2/*/redis-check-aof ${HOME}/redis-5.0.2/*/redis-check-rdb ${HOME}/redis-5.0.2/*/redis-cli ${HOME}/redis-5.0.2/redis.conf ${HOME}/redis-5.0.2/*/redis-server $HOME/redis


RUN echo -e "\
#端口根据对应的文件夹去配制端口 7000,7001,7002,7003,7004,7005 \n\
port 6379 \n\
#根据本机所在的IP或hostname去配制 node1 node2 node3 \n\
bind 0.0.0.0 \n\
#redis后台运行 \n\
daemonize yes \n\
#pidfile文件对应7000,7001,7002,7003,7004,7005 \n\
pidfile nodes_pid.pid \n\
#开启集群 把注释#去掉 \n\
cluster-enabled yes \n\
#集群的配置 配置文件首次启动自动生成 7000,7001,7002,7003,7004,7005 \n\
cluster-config-file nodes_config.conf \n\
#请求超时 默认15秒，可自行设置 \n\
cluster-node-timeout 15000 \n\
#aof日志开启 有需要就开启，它会每次写操作都记录一条日志 \n\
appendonly yes \n\
#日志等级\n\
loglevel notice \n\
#日志保存路 \n\
logfile /redis.log\n\
">$HOME/redis/nodes.conf
#

# 创建启动脚本
RUN echo -e "\
    sed -i \"s/^port.*$/port \$PORT/g\" \$HOME/redis/nodes.conf; \n\
    \$HOME/redis/redis-server \$HOME/redis/nodes.conf \n\
    if [ \"\$NEW_HOST\" != \"\" && \"\$EXIST_HOST\" != \"\" ]; then \n\
       \$HOME/redis/redis-trib.rb add-node \$NEW_HOST \$EXIST_HOST\n\
    fi \n\
    if [ \"\$NEW_HOST\" != \"\" && \"\$MASTER\" = \"true\" ]; then \n\
        \$HOME/redis/redis-trib.rb reshard \$NEW_HOST \n\
    fi \n\
    tail -f \$HOME/redis-5.0.2/redis.conf\n\
">/start.sh && chmod u+x /start.sh

ENTRYPOINT ["/bin/bash","/start.sh"]

#docker build -t llh/redis .
#docker run -dti --net=host --rm=false --restart=always -e PORT=6379 llh/redis
