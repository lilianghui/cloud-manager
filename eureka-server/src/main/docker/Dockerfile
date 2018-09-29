# 基于哪个镜像
FROM java
# 将本地文件夹挂载到当前容器
VOLUME /tmp
# 拷贝文件到容器
ADD eureka-server-0.0.1-SNAPSHOT.jar /app.jar
# 开放端口
EXPOSE 8761
# 配置容器启动后执行的命令
RUN /bin/cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]