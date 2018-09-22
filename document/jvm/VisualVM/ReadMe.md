JMX:
从jre\lib\management下复制jmxremote.access、jmxremote.password.template文件到tomcat\conf目录下并重命名jmxremote.password

cp $JAVA_HOME\jre\lib\management\jmxremote.access $CATALINA_HOME/conf/jmxremote.access
cp $JAVA_HOME\jre\lib\management\jmxremote.password.template $CATALINA_HOME/conf/jmxremote.password

jmxremote.password为用户文件(用户名密码): llh llh 
jmxremote.access为用户权限文件(用户名权限): llh readwrite 

MAVEN下载[groupId:org.apache.tomcat,artifactId:tomcat-catalina-jmx-remote]上传到tomcat/lib目录
tomcat/conf/server.xml添加以下监听:   (rmiRegistryPortPlatform为JMX暴露端口)
<Listener className="org.apache.catalina.mbeans.JmxRemoteLifecycleListener" rmiRegistryPortPlatform="10001" rmiServerPortPlatform="10002" />

tomcat/bin/catalina.sh添加以下命令:   (两个文件为刚配置的权限文件)
JAVA_OPTS="$JAVA_OPTS -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.password.file=../conf/jmxremote.password -Dcom.sun.management.jmxremote.access.file=../conf/jmxremote.access"

JSTATD:
    1、在java.policyf末尾添加
       vi $JAVA_HOME/jre/lib/security/java.policy 添加 permission java.security.AllPermission;
       
    2、启动jstatd(-p指定端口否则随机)
        cd $JAVA_HOME/bin
        ./jstatd -J-Djava.security.policy=all.policy -p 10003 &
        
        
本地jdk\bin目录下启动jvisualvm.exe  添加远程 jmx和jstatd


参考：
https://blog.csdn.net/autfish/article/details/51326340