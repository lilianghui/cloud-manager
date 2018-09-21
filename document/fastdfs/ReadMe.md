1.搜索fastdfs镜像 
    docker search fastdfs
    
2.下载fastdfs镜像，这里选择mypjb/fastdfs
    docker pull mypjb/fastdfs
    
3.创建宿主机保存fastdfs文件目录
    mkdir /root/data/fastdfs
    
4.执行命令运行fastdfs容器(将下面的【192.168.1.40】替换成自己机器的ip即可)
    docker run --add-host fastdfs.net:212.64.26.25 --name fastdfs --net=host -e TRACKER_ENABLE=1 -e NGINX_PORT=81 -v /root/data/fastdfs:/storage/fastdfs -it mypjb/fastdfs

5.重启fastdfs容器
    docker restart fastdfs
    
6.开放81端口
    firewall-cmd --zone=public --add-port=81/tcp --permanent
    firewall-cmd --reload
