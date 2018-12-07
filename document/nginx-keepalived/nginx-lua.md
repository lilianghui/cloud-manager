Nginx-Lua模块

    1 下载安装LuaJIT
        wget http://luajit.org/download/LuaJIT-2.0.5.tar.gz
        tar -xvf LuaJIT-2.0.5.tar.gz
        cd LuaJIT-2.0.5
        make && make install PREFIX=/usr/local/luajit
    
    2 下载nginx lua模块
        wget https://github.com/openresty/lua-nginx-module/archive/v0.10.14rc3.tar.gz
        tar -xvf v0.10.14rc3.tar.gz 
    
    3 下载 ngx_devel_kit (NDK) 
        wget https://github.com/simpl/ngx_devel_kit/archive/v0.3.1rc1.tar.gz
        tar -xvf v0.3.1rc1.tar.gz
    
    4 查看版本 nginx -V
         添加模块(不要make install)
            ./configure --prefix=/usr/local/nginx --with-http_stub_status_module --with-http_ssl_module --with-pcre=/root/docker/nginx/pcre-8.39 --add-module=/root/docker/nginx/fastdfs-nginx-module/src --add-module=/root/docker/ngx_devel_kit-0.3.1rc1 --add-module=/root/docker/lua-nginx-module-0.10.14rc3
            make
        
     5 换nginx二进制文件:
         停止nginx
         cp /usr/local/nginx/sbin/nginx /usr/local/nginx/sbin/nginx.bak
         cp ./objs/nginx /usr/local/nginx/sbin/