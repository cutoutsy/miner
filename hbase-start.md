#Start Hadoop And Hbase Guide
###Start Hadoop
进入208，进入/usr/local/hadoop/，执行sbin/start-all.sh，这样成功启动hadoop
###Delete Files
首先hadoop fs -ls /hbase/WALs/\*查看一下
删除hadoop fs -rmr /hbase/WALs/\*
###Start Hbase
在208下，进入/usr/local/hbase/下，执行bin/start-hbase.sh
然后hbase shell测试一下，查看数据