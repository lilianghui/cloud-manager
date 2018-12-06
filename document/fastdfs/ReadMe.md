nginx-fastdfs

git clone https://github.com/happyfish100/fastdfs-nginx-module.git
cp fastdfs-nginx-module/src/mod_fastdfs.conf /etc/fdfs


cp https://github.com/happyfish100/fastdfs/blob/master/conf/http.conf   /etc/fdfs
cp https://github.com/happyfish100/fastdfs/blob/master/conf/mime.types   /etc/fdfs

#默认图片
sed -i "s|^http.anti_steal.token_check_fail.*$|http.anti_steal.token_check_fail=/etc/fdfs/default.jpg|g" /etc/fdfs/http.conf

sed -i "s|^store_path0.*$|store_path0=/var/local/fdfs/storage|g" /etc/fdfs/mod_fastdfs.conf
sed -i "s|^url_have_group_name =.*$|url_have_group_name = true|g" /etc/fdfs/mod_fastdfs.conf
sed -i "s|^tracker_server=.*$|tracker_server = 10.250.209.43:22122|g" /etc/fdfs/mod_fastdfs.conf

vim /usr/local/nginx/conf/nginx.conf

events {
    worker_connections  1024;
}
http {
    include       mime.types;
    default_type  application/octet-stream;
    server {
        listen 8888;
        server_name localhost;
        location ~ /group[0-9]/M00 {
            ngx_fastdfs_module;
        }
    }
}




设置tracker nginx 指向storage里的nginx 

upstream fdfs_group1 {
    server 10.250.209.43:8080 weight=1 max_fails=2 fail_timeout=30s;
}

server{
    location /group1/M00 {
      proxy_pass http://fdfs_group1;
    }
}





https://blog.csdn.net/qq_26440803/article/details/83066132
http://www.cnblogs.com/ityouknow/p/8240976.html
https://blog.csdn.net/u012979009/article/details/55052318
https://blog.csdn.net/ForeverSunshine/article/details/51226061
http://www.ityouknow.com/fastdfs/2017/10/10/cluster-building-fastdfs.html