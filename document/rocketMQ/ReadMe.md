https://www.apache.org/dyn/closer.cgi?path=rocketmq/4.3.1/rocketmq-all-4.3.1-bin-release.zip

系统环境变量配置

        变量名：ROCKETMQ_HOME

        变量值：MQ解压路径\MQ文件夹名
        
        
        
Cmd命令框执行进入至‘MQ文件夹\bin’下，然后执行‘start mqnamesrv.cmd’，启动NAMESERVER。成功后会弹出提示框，此框勿关闭。


  Cmd命令框执行进入至‘MQ文件夹\bin’下，然后执行‘start mqbroker.cmd -n 127.0.0.1:9876 autoCreateTopicEnable=true’，启动BROKER。成功后会弹出提示框，此框勿关闭。

假如弹出提示框提示‘错误: 找不到或无法加载主类 xxxxxx’。打开runbroker.cmd，然后将‘%CLASSPATH%’加上英文双引号。保存并重新执行start语句。


git clone https://github.com/apache/rocketmq-externals.git


vi rocketmq-externals\rocketmq-console\src\main\resources\application.properties

server.port=8787
rocketmq.config.namesrvAddr=127.0.0.1:9876



进入‘\rocketmq-externals\rocketmq-console’文件夹，执行‘mvn clean package -Dmaven.test.skip=true’

Cmd进入‘target’文件夹，执行‘java -jar rocketmq-console-ng-1.0.0.jar’，启动‘rocketmq-console-ng-1.0.0.jar’。


http://127.0.0.1:8787

