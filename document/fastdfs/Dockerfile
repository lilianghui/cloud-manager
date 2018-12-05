FROM alpine:3.6

MAINTAINER lilianghui <810653725@qq.com>

ENV HOME /root

# 配置启动脚本，在启动时中根据环境变量替换nginx端口、fastdfs端口
# 默认nginx端口
#ENV WEB_PORT 8888
# 默认fastdfs端口
ENV FDFS_PORT 22122

ENV IP 172.17.0.2


# 暴露端口。改为采用host网络，不需要单独暴露端口
EXPOSE 80 22122 23000 11411


#安装准备
RUN apk update \
    && apk add --no-cache  git gcc libc-dev make automake autoconf libtool pcre pcre-dev zlib zlib-dev openssl-dev wget vim --repository https://mirrors.tuna.tsinghua.edu.cn/alpine/v3.6/main

ADD db-18.1.25.tar.gz ${HOME}
RUN mkdir -p /var/local/fdfs/tracker /var/local/fdfs/storage /var/local/fdfs/fastdht

#下载fastdfs.libfastcommon.nginx插件源码
RUN cd ${HOME} \
    && git clone https://github.com/happyfish100/libfastcommon.git --depth 1 \
    && git clone https://github.com/happyfish100/fastdfs.git --depth 1 \
    && git clone https://github.com/happyfish100/fastdht.git --depth 1 \
    && git clone https://github.com/happyfish100/fastdfs-nginx-module.git --depth 1
#	&& wget http://nginx.org/download/nginx-1.15.4.tar.gz

# 安装libfastcommon
RUN cd ${HOME}/libfastcommon/ \
    && ./make.sh  \
    && ./make.sh install

	
#tracker.conf 改路径
#storage.conf 改路径 改tracker_server
#client.conf 改路径 改tracker_server

# 安装fastdfs
RUN cd ${HOME}/fastdfs/ \
    && ./make.sh \
    && ./make.sh install \
    && cp /etc/fdfs/tracker.conf.sample /etc/fdfs/tracker.conf \
    && cp /etc/fdfs/storage.conf.sample /etc/fdfs/storage.conf \
    && cp /etc/fdfs/client.conf.sample /etc/fdfs/client.conf \
    && cp ${HOME}/fastdfs/conf/http.conf /etc/fdfs/ \
    && cp ${HOME}/fastdfs/conf/mime.types /etc/fdfs/ \
    && sed -i "s|/home/yuqing/fastdfs|/var/local/fdfs/tracker|g" /etc/fdfs/tracker.conf \
    && sed -i "s|/home/yuqing/fastdfs|/var/local/fdfs/storage|g" /etc/fdfs/storage.conf \
    && sed -i "s|/home/yuqing/fastdfs|/var/local/fdfs/storage|g" /etc/fdfs/client.conf

	
# 安装Berkeley DB
RUN cd ${HOME}/db-18.1.25/build_unix/ \
	&& ../dist/configure --prefix=/usr/local/db-18.1.25 \
	&& make \
	&& make install


# 安装FastDHT
RUN cd ${HOME}/fastdht \
	&& sed -i "s|-D_GNU_SOURCE|-D_GNU_SOURCE -I/usr/local/db-18.1.25/include/ -L/usr/local/db-18.1.25/lib/|g" ./make.sh \
	&& ./make.sh \
	&& ./make.sh install \
	&& sed -i "s|/home/yuqing/fastdht|/var/local/fdfs/fastdht|g" /etc/fdht/fdht_client.conf \
	&& sed -i "s|/home/yuqing/fastdht|/var/local/fdfs/fastdht|g" /etc/fdht/fdhtd.conf \
    && sed -i "s|check_file_duplicate=0|check_file_duplicate=1|g" /etc/fdfs/storage.conf \
    && sed -i "s|keep_alive=0|keep_alive=1|g" /etc/fdfs/storage.conf \
    && sed -i "s|##include /home/yuqing/fastdht/conf/fdht_servers.conf|#include /etc/fdht/fdht_servers.conf|g" /etc/fdfs/storage.conf


RUN cp /usr/local/db-18.1.25/lib/libdb-18.1.so /usr/lib/

# 清理文件
RUN rm -rf ${HOME}/*
#RUN apk del gcc libc-dev make openssl-dev




# 创建启动脚本
RUN echo -e "\
local_ip=`ifconfig eth0 | grep inet | awk '{print \$2}'| awk -F: '{print \$2}'` \n\
if [ \"\$IP\" = \"\" ]; then \n\
    IP=local_ip\n\
fi \n\
#sed -i \"s/use_storage_id = false/use_storage_id = true/g\" /etc/fdfs/tracker.conf \n\
#sed -i \"s/id_type_in_filename = ip/id_type_in_filename = id/g\" /etc/fdfs/tracker.conf \n\
#sed -i \"s/use_storage_id = false/use_storage_id = true/g\" /etc/fdfs/client.conf \n\
#echo \"100001   group1  10.250.209.43\" > /etc/fdfs/storage_ids.conf  \n\
echo \"group_count = 1 \ngroup0 = \$IP:11411\" > /etc/fdht/fdht_servers.conf  \n\
sed -i \"s/^tracker_server=.*$/tracker_server=\$IP:\$FDFS_PORT/g\" /etc/fdfs/client.conf; \n\
sed -i \"s/^tracker_server=.*$/tracker_server=\$IP:\$FDFS_PORT/g\" /etc/fdfs/storage.conf; \n\
/etc/init.d/fdfs_trackerd start; \n\
/etc/init.d/fdfs_storaged start; \n\
fdhtd /etc/fdht/fdhtd.conf; \n\
tail -f /var/local/fdfs/tracker/logs/trackerd.log; \n\
">/start.sh \
&& chmod u+x /start.sh



ENTRYPOINT ["/bin/bash","/start.sh"]

#docker build -t llh/fastdfs .
##docker run -it -e IP=10.250.209.43 llh/fastdfs /bin/bash
##docker run -t -i -e IP=172.17.0.2 llh/fastdfs /bin/bash
#docker run -dti -e IP=10.250.209.43 -e FDFS_PORT=22122 -p 11411:11411 -p 23000:23000 -p 22122:22122 llh/fastdfs
#docker exec -it d48b21a7e439 /bin/sh
#docker rm -f $(docker ps -aq)
#netstat -unltp|grep fd