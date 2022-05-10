# Trace-Recorder<sup><sup>&nbsp;它是可伸缩的。</sup></sup>

简单的， 可伸缩的。 Trace-Recorder 是一个跟踪记录仪，主要是为了更好的去进行源码流程的记录，
可以更好的获取源码执行的基础流程和全流程；通过可伸缩的插件体系可以更好的去管理记录。

### 使用
1、需要安装jdk11环境（推荐openJDK环境）
> 下面网页只有 [Linux/x64](https://jdk.java.net/java-se-ri/11)、[Windows/x64](https://jdk.java.net/java-se-ri/11) 两个版本，
> 如需更多请前往[OracleJDK](https://www.oracle.com/java/technologies/downloads/#java11)官网下载
    
    openJDK11: https://jdk.java.net/java-se-ri/11

2、克隆项目

3、解决依赖

    gradle build

4、安装依赖到本地maven仓库

    gradle publishToMavenLocal    

5、项目依赖
```gradle
implementation 'cn.xusc:trace-recorder:1.0'
```

```maven
<dependency>
    <groupId>cn.xusc</groupId>
    <artifactId>trace-recorder</artifactId>
    <version>1.0</version>
</dependency>
```

### 作者
* QQ : 2506162335
* 微信: m47705945
* 邮箱: 18573707104@163.com
* 描述: 如果有任何问题请联系作者