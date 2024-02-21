# caychen-boot-canal

## 1、准备工作

### 1.1、 Mysql版本

当前的 Canal 支持源端 MySQL 版本包括 5.1.x , 5.5.x , 5.6.x , 5.7.x , 8.0.x

#### 1.2、 对于自建 MySQL , 需要先开启 Binlog 写入功能，配置 binlog-format 为 ROW 模式，my.cnf 中配置如下

```text
[mysqld]
log-bin=mysql-bin # 开启 binlog
binlog-format=ROW # 选择 ROW 模式
server_id=1 # 配置 MySQL replaction 需要定义，不要和 canal 的 slaveId 重复
```

> 注意：针对阿里云 RDS for MySQL , 默认打开了 binlog , 并且账号默认具有 binlog dump 权限 , 不需要任何权限或者 binlog
> 设置,可以直接跳过这一步

### 1.3、 授权 Canal 链接 MySQL 账号具有作为 MySQL slave 的权限, 如果已有账户可直接 grant。

```sql
-- 创建canal用户
CREATE USER canal IDENTIFIED BY '密码';  

-- 授权
GRANT SELECT, REPLICATION SLAVE, REPLICATION CLIENT ON *.* TO 'canal'@'%';
-- GRANT ALL PRIVILEGES ON *.* TO 'canal'@'%' ;

-- 刷新权限
FLUSH PRIVILEGES;
```

如果用Mysql8的，因为Mysql8使用的密码加密方式跟Mysql5的形式不一样，如果直接用上述的形式，创建的密码加密形式为`caching_sha2_password`,
会导致canal连不上Mysql服务器，所以Mysql8创建canal用户的时候，需要使用如下sql：

```sql
ALTER USER 'canal'@'%' IDENTIFIED WITH mysql_native_password BY '密码';
```

## 2、启动Canal服务器

### 2.1、下载并解压Canal

可以直接从Canal的 [Github官方地址](https://github.com/alibaba/canal) 进行下载，并进行解压。

### 2.2、修改Canal的conf配置

默认Canal有个默认的实例example，所以它会有个实例的文件夹example，而每个实例里有个instance.properties配置文件。
如果需要自定义实例，可以新建一个实例文件夹，将example文件夹下的instance.properties拷贝到自定义的文件夹里即可。

此处本人新建了一个新的实例caychen，并修改instance.propertise配置文件。

主要修改有几处，如下：

```properties
# 如果canal服务器和Mysql服务器不在同一台机器上的话，此处需要修改成Mysql的实际ip
canal.instance.master.address=127.0.0.1:3306
canal.instance.master.journal.name=
canal.instance.master.position=
canal.instance.master.timestamp=
canal.instance.master.gtid=

# 授权的canal用户的账号和密码
canal.instance.dbUsername=canal
canal.instance.dbPassword=授权canal用户的密码
canal.instance.connectionCharset = UTF-8
```

其他默认即可，如果需要复杂的功能，可以自行设置。

### 2.3、启动Canal

进入bin目录，执行如下命令：

```text
./startup.sh
```

* 启动之后，会在bin目录下生成一个canal.pid文件，记录canal服务器的线程id。
* 进入logs目录，会生成每个实例文件夹，进入自定义的实例文件夹，会有一个实例名.log的日志文件。

### 2.4、注意点

startup.sh默认使用的GC是低版本的CMS，如果虚拟机上安装的是其他高版本的jdk，会启动canal失败。
本人的虚拟机上安装的是jdk11，所以修改了startup.sh启动脚本，使用的GC是G1。

```text
JAVA_OPTS="-server -Xms1024m -Xmx1024m -Xmn512m -XX:SurvivorRatio=2 -XX:PermSize=96m -XX:MaxPermSize=256m -Xss256k -XX:-UseAdaptiveSizePolicy -XX:MaxTenuringThreshold=15 -XX:+DisableExplicitGC -XX:+UseG1GC  -XX:+HeapDumpOnOutOfMemoryError"
```

修改完后启动Canal即可。

## 3、Canal客户端编码

### 3.1、此处使用的SpringBoot-2.3.12.RELEASE版本。

```yaml
canal:
  server-address: 单机版的Canal服务器地址
  instance: 实例名称
  filter: canal-study.t_user #订阅库表
  batchSize: 1000 # 每次拉取数量，默认1000
  port: 11111 # canal监听端口，默认11111
  interval: 2000 # canal监听时间间隔，默认2s
```

### 3.2、监听Mysql数据库表变化

实现`AbstractCanalDataHandler`抽象类，里面的方法都是默认函数，只需要重写需要的方法即可。
可以实现Redis、MQ等中间件的同步。
代码略。