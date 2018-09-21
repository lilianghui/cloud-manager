cat  /etc/firewalld/zones/public.xml  防火墙列表
firewall-cmd --permanent --zone=public --add-port=9000/tcp  添加端口
firewall-cmd --reload 重新加载