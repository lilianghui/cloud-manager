下载 keepalived-2.0.10.tar.gz nginx-1.15.6.tar.gz pcre-8.39.zip
nginx           http://nginx.org/en/download.html
keepalived      http://www.keepalived.org/software/

KeepAlived:
    192.168.1.11
    192.168.1.12
Nginx:
    192.168.1.13    
    192.168.1.14
        
先安装KeepAlived和Nginx依赖:
    yum install openssl openssl-devel gcc gcc-c++ autoconf automake libtool pcre-devel zlib-devel -y

tar -xvf keepalived-2.0.10.tar.gz
tar -xvf nginx-1.15.6.tar.gz

scp -r  /home/hadoop/keepalived-2.0.10 hadoop@llh002:/home/hadoop/
scp -r  /home/hadoop/keepalived-2.0.10 hadoop@llh003:/home/hadoop/
scp -r  /home/hadoop/keepalived-2.0.10 hadoop@llh004:/home/hadoop/

scp -r  /home/hadoop/nginx-1.15.6 hadoop@llh002:/home/hadoop/
scp -r  /home/hadoop/nginx-1.15.6 hadoop@llh003:/home/hadoop/
scp -r  /home/hadoop/nginx-1.15.6 hadoop@llh004:/home/hadoop/

KeepAlived安装:
./configure --prefix=/usr/local/keepalived --sysconf=/etc
make && make install

ln -s /usr/local/keepalived/sbin/keepalived /sbin/  --建立软链接
cp /home/hadoop/keepalived-2.0.10/keepalived/etc/init.d/keepalived /etc/init.d

chmod +x /etc/init.d/keepalived

chkconfig --add keepalived  -- 添加到系统服务
chkconfig keepalived on    -- 检测是否添加成功

vi /etc/keepalived/keepalived.conf
主用keepalived-master.conf
从用keepalived-backup.conf

service keepalived start|stop|restart

修改KeepAlived日志
vi /etc/sysconfig/keepalived(修改)
    KEEPALIVED_OPTIONS="-D -d -S 0"
vi /etc/rsyslog.conf(添加)
  local0.*  /var/log/keepalived.log
重启服务:
    service rsyslog restart
    service keepalived restart
查看日志
    tail -f /var/log/keepalived.log


Nginx安装:
wget http://nginx.org/download/nginx-1.15.4.tar.gz

下载pcre  ftp://ftp.csx.cam.ac.uk/pub/software/programming/pcre/
或 wget https://ftp.pcre.org/pub/pcre/pcre-8.39.tar.gz
unzip pcre-8.39.zip
scp -r  /home/hadoop/pcre-8.39 hadoop@llh003:/home/hadoop/
scp -r  /home/hadoop/pcre-8.39 hadoop@llh004:/home/hadoop/

#以下步骤非必须  安装nginx可指定pcre路径联编
cd pcre-8.39
./configure && make && make install

cd nginx-1.15.6
./configure --prefix=/usr/local/nginx --with-http_stub_status_module --with-http_ssl_module --with-pcre=/home/hadoop/pcre-8.39
make && make install

配置文件    /usr/local/nginx/conf
启动        /usr/local/nginx/sbin/nginx
停止/重启   /usr/local/nginx/sbin/nginx -s stop(quit、reload)

错误:
     'aclocal-1.15' is missing on your system.    方法:  autoreconf -ivf 
     
     
https://blog.csdn.net/vr7jj/article/details/80438663
https://blog.csdn.net/arackethis/article/details/42222905
