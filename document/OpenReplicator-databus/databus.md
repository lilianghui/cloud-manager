http://127.0.0.1:8080/group1/M00/00/00/CvrRK1wGlreAKrgBAAAPCemPKqY648.txt


http://10.250.209.43:22122/group1/M00/00/00/CvrRK1wGlreAKrgBAAAPCemPKqY648.txt
 


https://blog.csdn.net/qq_26440803/article/details/83066132
http://www.cnblogs.com/ityouknow/p/8240976.html
https://blog.csdn.net/u012979009/article/details/55052318
https://blog.csdn.net/ForeverSunshine/article/details/51226061
http://www.ityouknow.com/fastdfs/2017/10/10/cluster-building-fastdfs.html

https://www.cnblogs.com/jietang/p/5615681.html

springboot jwt
https://www.jianshu.com/p/0b1131be7ace

databus
https://aoyouzi.iteye.com/blog/2398751
https://sq.163yun.com/blog/article/173554725500456960
gradle -Dopen_source=true assemble -x javadocJar

SHOW VARIABLES LIKE 'sql_log_bin';
SHOW VARIABLES LIKE 'binlog_format';
SHOW VARIABLES LIKE 'binlog_checksum';
RESET MASTER;

show master status;

SET sql_log_bin = 1;
SET globle binlog_format = 'ROW';
SET GLOBAL binlog_checksum = NONE;


log-bin=mysql-bin
binlog-format=Row


CHANGE MASTER TO master_log_file = 1mysql-bin.0035841, master_log_pos = 02 # 先指到下一个 binlog 档, pos 指 0 或 4, 都是一样意思.


cd /root/databus/databus/build/databus2-example-relay-pkg/distributions/databus2-example-relay-pkg-2.0.0/
 ./bin/create_person.sh
 ./bin/start-example-relay.sh or_person -Y ./conf/sources-or-person.json
 
 
cd /root/databus/databus/build/databus2-example-client-pkg/distributions/databus2-example-client-pkg-2.0.0/
 ./bin/start-example-client.sh person

tail -f /root/databus/databus/build/databus2-example-relay-pkg/distributions/databus2-example-relay-pkg-2.0.0/logs/relay.log
tail -f /root/databus/databus/build/databus2-example-client-pkg/distributions/databus2-example-client-pkg-2.0.0/logs/client.log



curl  http://localhost:11115/sources
curl http://localhost:11115/relayStats/outbound/http/clients
curl -s http://localhost:11115/containerStats/inbound/events/total?pretty |  grep -m1 numDataEvents