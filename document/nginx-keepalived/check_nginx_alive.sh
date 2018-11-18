#!/bin/sh

PATH=/bin:/sbin:/usr/bin:/usr/sbin

A=`ps -C nginx --no-header |wc -l`

if [ $A -eq 0 ]
   then
     echo 'nginx server is died'
     killall keepalived
fi