# hui-base-springcloud

微服务项目骨架：集成 consul(register+config) zool feign(hystrix+ribbon) stream zipkin(sleuth)  dashboard(hystrix)
其它组件： 分布式事务用lcn中间件，集成netty(Rector  nio+eventLoop)框架

## 开发环境

- JDK1.8
- Maven
- Redis
- RabbitMQ
## 项目结构

├─hui-base-springcloud  
  ├─hui-base-springcloud-api-gateway  （网关中心）  
  ├─hui-base-springcloud-common （通用工具包）   
  ├─hui-base-springcloud-discovery （hystrix监控Dashbord）  
  ├─hui-base-springcloud-netty （netty相关包）  
  ├─hui-base-springcloud-order (feign hystrix ribbon stream consul-config consul lcn)  
  ├─hui-base-springcloud-product  (feign hystrix ribbon stream consul-config consul lcn netty)
  ├─hui-base-springcloud-tx-lcn （分布式事务管理器中心）  
  ├─zipkin.jar  （监控中心 sleuth+zipkin）
  └─consul （注册中心） 见下文安装方式
## 软件架构

1. 该项目是基于springcloud的微服务项目骨架
2. 除了tx-lcn外基本都采用了spring的框架，所有的package都可以从maven仓库下载
3. springcloud版本为Greenwich.RELEASE
4. 服务发现采用了Consul，私用consul提供的config server
5. 网关采用springcloiud-gateway
6. 分布式事务处理增加tx-lcn（同步分布式事务解决方案）

## 启动教程

### 0.初始化数据库

SQL/init_table.sql

### 1. 基础服务

|   service    | service-name | port |          Note           |
| :----------: | :----------: | :--: | :---------------------: |
| 关系型数据库 |    mysql     | 3306 | 开发的时候使用了5.6版本 |
| NOSQL数据库  |    redis     | 6379 |  供TX-LCN-MANAGER使用   |
|   消息队列   |   RabbitMQ   | 5672 |  提供给MQ和Stream组件   |

## 使用说明

1.consul
安装文档: https://learn.hashicorp.com/tutorials/consul/get-started-install?in=consul/getting-started
consul config
    在consul管理页上添加 k:v   config/{applicationName},{profiles.active}/data  :  {}    配置中的格式要和 spring.cloud.config.format的一致

2.TM分布式事务
- 分布式事务的服务也是一定要集成redis，并且和tx-manager要连接同一个redis服务。
- LCn TCX 模式时没有主键的表必须在 MysqlPrimaryKeysProvider 中配置主键

3.zipkin
    安装： curl -sSL https://zipkin.io/quickstart.sh | bash -s
    启动： java -jar zipkin.jar --server.port=28001
    访问： http://localhost:28001/

4.netty
    1. 建立tcp长连接 request.controller.tcp.RestTcpController.login()
    2. 调用tcp长连接 request.controller.tcp.RestTcpController
    3. 启动时执行    common.netty.config.TcpActionLoader   扫描长连接相关注解及接口 -> 启动netty服务 -> 与服务端建立连接
    4. 发送到接收指令调用链 
       MessageSendHandler -> TcpSendHandler -> MessageEncoder -> MessageDecoder -> MessageReceiverHandler -> TcpReceiveHandler 
    5. 本地环境搭建  
      - 可建立两个端，服务端不需操作，客户端可使用application-devclient.yml中的配置
      - 再打开TcpActionLoader中连接服务端的注解
      - 先启动服务端 ， 再启动客户端即可

中文文档
https://www.springcloud.cc/spring-cloud-greenwich.html