wget https://artifacts.elastic.co/downloads/logstash/logstash-6.4.0.tar.gz
#解压到指定目录
tar -zxvf  logstash-6.4.0.tar.gz -C /Data/apps
vi logstash-6.4.0/config/logstash.conf
./bin/logstash -f ./config/logstash.conf --debug

logstash-springboot.conf
input {
    tcp {
        port => 4560   //从本地的4560端口取日志。这里笔者将Logstash部署在了虚拟机192.168.226.132上，所以取的是本地地址。
        codec => json_lines  //需要安装logstash-codec-json_lines插件
    }
}
output{
  elasticsearch { hosts => ["localhost:9200"] }  //输出到ElasticSearch
  stdout { codec => rubydebug }  //若不需要在控制台中输出，此行可以删除
}

# Sample Logstash configuration for creating a simple
# Beats -> Logstash -> Elasticsearch pipeline.

input {
  tcp {
    port => 4560
  }
}

output {
  elasticsearch {
    hosts => ["http://localhost:9200"]
    index => "%{[@metadata][beat]}-%{[@metadata][version]}-%{+YYYY.MM.dd}"
    #user => "elastic"
    #password => "changeme"
  }
}
