#!/bin/sh
for((i=0;i<=6;i++));
do
rm -rf /root/redis-4.0.11-cluster-700$i/nodes_config.conf
echo "删除	/root/redis-4.0.11-cluster-700$i/nodes_config.conf"
rm -rf /root/redis-4.0.11-cluster-700$i/appendonly.aof
echo "删除	/root/redis-4.0.11-cluster-700$i/appendonly.aof"
nohup /root/redis-4.0.11-cluster-700$i/redis-server /root/redis-4.0.11-cluster-700$i/nodes.conf &
echo "启动	/root/redis-4.0.11-cluster-700$i/redis-server /root/redis-4.0.11-cluster-700$i/nodes.conf"
done