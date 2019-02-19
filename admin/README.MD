# Admin



##1.运行环境


jdk版本：  1.8 及以上

##2.程序安装
1).下载tar.gz包，例如：admin-1.0-assembly.tar.gz.

2).解压后目录一般为：  
```text
 admin-1.0   根目录
         -- config 配置目录
         -- lib 库目录
         -- script 脚本目录
         -- log 日志目录（不存时在程序会自动创建）
```
       
3).启动根目录下面的 start.cmd  或者 start.sh  启动程序。程序启动后，会在log目录写PID文件   
         
##3.程序概览

程序基于spring boot 2.0.4.RELEASE 开发，集成thymeleaf开发页面，使用maven管理依赖和打包。


##4.程序功能

