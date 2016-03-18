## 平台部署及测试项目运行
本平台代码托管在coding上，私有项目，项目成员可见
1、Clone平台代码到220:/opt/build/下,并切换到dev分支
```shell
git clone https://git.coding.net/cutoutsy/miner.git
git checkout dev
```

2、修改平台配置文件路径，并查看或修改平台配置文件
在miner-utils/src/main/java/miner/utils/PlatformParas.java文件里面，修改为：
public static ReadConfigUtil readConfigUtil= new ReadConfigUtil("/opt/build/platform_cluster_.properties", true);
查看/opt/build/下的platform_cluster_.properties是否正确，或者根据自己的需要修改。

3、编译项目，在项目根目录下，即/opt/build/miner/
```shell
mvn install
```
出现BUILD SUCCESS即可进入下一步

4、开启日志服务器
在/opt/build/miner/miner-spider/target/下执行nohup java -jar miner-spider-1.0-SNAPSHOT.jar &，并查看nohup.out中输出是否正常
正常输出：log server start...

5、开启代理更新程序
拷贝/opt/build/miner/log4j2/文件夹到/opt/build/miner/miner-proxy/target/下
运行程序，在/opt/build/miner/miner-proxy/target/下执行nohup java -jar miner-proxy-1.0-SNAPSHOT.jar &，并查看nohup.out中输出是否正常
进入211中，连接redis数据库，查看proxy_pool中的代理是否更新。

6、修改测试项目在redis里面保存的状态
测试项目为1-1
查看是否存在beijing4s1或者beijing4s2,且里面的值和beijing4s的值一样；
修改project_cronstate里1-1为3
修改project_state里1-1为die_

## 关于平台一些UI地址
在网关机上进行端口转发，端口不再是默认端口。这种方式需要在网关机/etc/rc.local下进行配置或开启
storm ui: http://wwww.datafish.wicp.net:1025
hadoop ui: http://wwww.datafish.wicp.net:1027
hbase ui: http://wwww.datafish.wicp.net:1027


## 关于在redis里面的几个数据库的说明
proxy_pool: 整个集群的代理池

project_cronstate: 项目的运行设置的状态

project_state: 保存项目的运行状态（die: 没有运行，running: 正在运行）


## 关于项目配置里面的几个数据库的简介：
workspace:
> * wid： workspaceid，项目的整个工作空间id
> * name： 工作空间名称
> * description： 关于此工作空间的描述

project:
> * wid: 所属的工作空间Id
> * pid: 项目ID
> * name: 项目名字
> * description： 项目描述
> * datasource: 项目启动连接redis里面的数据库名称
> * schedule: 项目的运行策略
> * precondition: 项目运行的前提条件

task:
> * wid: 任务所属的工作空间ID
> * pid: 任务所属的项目ID
> * tid: 任务ID
> * name: 任务名称
> * description: 任务描述
> * urlpattern: URL配置
> * urlgenerate: 是否生成URL
> * isloop: 是否参与循环

regex:
> * wid: 任务所属的工作空间ID
> * pid: 任务所属的项目ID
> * tid: 任务ID
> * tagname: 标签名称
> * path: 网页路径

data:
> * wid: 任务所属的工作空间ID
> * pid: 任务所属的项目ID
> * tid: 任务ID
> * dataid: 数据ID
> * description: 数据描述
> * property: 数据的属性
> * rowKey: Hbase表里面的行键
> * foreignKey: Hbase表里面的外键
> * foreignValue: Hbase表里的外键值
> * link: Hbase里面与之有关系的表
> * processWay: 数据处理方式（S：表示存储进数据库，l：表示进入循环）
> * lcondition: 进入循环的任务id





## 模块

####miner-parse：网页解析模块
####miner-store：数据存储模块
####miner-spider：爬虫公共模块
####miner-topo：storm topology模块


###任务定时执行采用开源的Quartz实现
Quartz使用类似于Linux下的Cron表达式定义时间规则，Cron表达式由6或7个由空格分隔的时间字段组成，如下：

 位置  | 时间域名 |  允许值     |允许的特殊字符
 :---  |:---      | :---        |:---- 
 1     | 秒       |   0-59      |, - * /        
 2     | 分钟     |   0-59      |, - * /        
 3     | 小时     |   0-23      |, - * /        
 4     | 日期     |   1-31      |, - * ? / L W C
 5     | 月份     |   1-12      |, - * /        
 6     | 星期     |   1-7       |, - * ? / L W #
 7     | 年(可选) |空值1970-2099|, - * /        
 
Cron 触发器利用一系列特殊字符，如下所示：
> * 反斜线（/）字符表示增量值。例如，在秒字段中“5/15”代表从第 5 秒开始，每 15 秒一次
> * 问号（?）字符和字母L字符只有在月内日期和周内日期字段中可用。问号表示这个字段不包含具体值。所以，如果指定月内日期，可以在周内日期字段中插入“?”，表示周内日期值无关紧要。字母 L 字符是 last的缩写。放在月内日期字段中，表示安排在当月最后一天执行。在周内日期字段中，如果“L”单独存在，就等于“7”，否则代表当月内周内日期的最后一个实例。所以“0L”表示安排在当月的最后一个星期日执行。
> * 在月内日期字段中的字母（W）字符把执行安排在最靠近指定值的工作日。把“1W”放在月内 日期字段中，表示把执行安排在当月的第一个工作日内。
> * 井号（#）字符为给定月份指定具体的工作日实例。把“MON#2”放在周内日期字段中，表示把任务安排在当月的第二个星期一。
> * 星号（*）字符是通配字符，表示该字段可以接受任何可能的值