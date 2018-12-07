设置tracker nginx 指向storage里的nginx 

upstream fdfs_group1 {
    server 10.250.209.43:8080 weight=1 max_fails=2 fail_timeout=30s;
}

server{
    location ~ /group[0-9]/M00 {
        if ($arg_attname ~ "^(.+)") {
            #设置下载
            add_header Content-Type application/x-download;
            #设置文件名
            add_header Content-Disposition "attachment;filename=$arg_attname";
        }
        proxy_pass http://fdfs_group1;
    }
}


--属性
method          = ngx.var.request_method    -- http://wiki.nginx.org/HttpCoreModule#.24request_method
schema          = ngx.var.schema            -- http://wiki.nginx.org/HttpCoreModule#.24scheme
host            = ngx.var.host              -- http://wiki.nginx.org/HttpCoreModule#.24host
hostname        = ngx.var.hostname          -- http://wiki.nginx.org/HttpCoreModule#.24hostname
uri             = ngx.var.request_uri       -- http://wiki.nginx.org/HttpCoreModule#.24request_uri
path            = ngx.var.uri               -- http://wiki.nginx.org/HttpCoreModule#.24uri
filename        = ngx.var.request_filename  -- http://wiki.nginx.org/HttpCoreModule#.24request_filename
query_string    = ngx.var.query_string      -- http://wiki.nginx.org/HttpCoreModule#.24query_string
user_agent      = ngx.var.http_user_agent   -- http://wiki.nginx.org/HttpCoreModule#.24http_HEADER
remote_addr     = ngx.var.remote_addr       -- http://wiki.nginx.org/HttpCoreModule#.24remote_addr
remote_port     = ngx.var.remote_port       -- http://wiki.nginx.org/HttpCoreModule#.24remote_port
remote_user     = ngx.var.remote_user       -- http://wiki.nginx.org/HttpCoreModule#.24remote_user
remote_passwd   = ngx.var.remote_passwd     -- http://wiki.nginx.org/HttpCoreModule#.24remote_passwd
content_type    = ngx.var.content_type      -- http://wiki.nginx.org/HttpCoreModule#.24content_type
content_length  = ngx.var.content_length    -- http://wiki.nginx.org/HttpCoreModule#.24content_length

headers         = ngx.req.get_headers()     -- http://wiki.nginx.org/HttpLuaModule#ngx.req.get_headers
uri_args        = ngx.req.get_uri_args()    -- http://wiki.nginx.org/HttpLuaModule#ngx.req.get_uri_args
post_args       = ngx.req.get_post_args()   -- http://wiki.nginx.org/HttpLuaModule#ngx.req.get_post_args
socket          = ngx.req.socket            -- http://wiki.nginx.org/HttpLuaModule#ngx.req.socket

--方法
request:read_body()                         -- http://wiki.nginx.org/HttpLuaModule#ngx.req.read_body
request:get_uri_arg(name, default)
request:get_post_arg(name, default)
request:get_arg(name, default)

request:get_cookie(key, decrypt)
request:rewrite(uri, jump)                  -- http://wiki.nginx.org/HttpLuaModule#ngx.req.set_uri
request:set_uri_args(args)                  -- http://wiki.nginx.org/HttpLuaModule#ngx.req.set_uri_args
 

 

response对象的属性和方法

--属性
headers         = ngx.header                -- http://wiki.nginx.org/HttpLuaModule#ngx.header.HEADER

--方法
response:set_cookie(key, value, encrypt, duration, path)
response:write(content)
response:writeln(content)
response:ltp(template,data)
response:redirect(url, status)              -- http://wiki.nginx.org/HttpLuaModule#ngx.redirect

response:finish()                           -- http://wiki.nginx.org/HttpLuaModule#ngx.eof
response:is_finished()
response:defer(func, ...)                   -- 在response返回后执行


https://blog.csdn.net/qq_26440803/article/details/83066132
http://www.cnblogs.com/ityouknow/p/8240976.html
https://blog.csdn.net/u012979009/article/details/55052318
https://blog.csdn.net/ForeverSunshine/article/details/51226061
http://www.ityouknow.com/fastdfs/2017/10/10/cluster-building-fastdfs.html