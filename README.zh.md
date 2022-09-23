# ![logo](./logo_zh.png "Trace-Recorder logo")

简单的， 可伸缩的， 高性能的。 Trace-Recorder 是一个跟踪记录仪，主要是为了更好的去进行源码流程的记录，
可以更好的获取源码执行的基础流程和全流程；通过可伸缩的插件体系可以更好的去管理记录。

### 使用
**项目依赖**

gradle
```gradle
implementation 'cn.xusc:trace-recorder:2.4'
```

maven
```maven
<dependency>
   <groupId>cn.xusc</groupId>
   <artifactId>trace-recorder</artifactId>
   <version>2.4</version>
</dependency>
```

### 源代码构建
1、需要安装jdk11环境（推荐openJDK环境）
> 下面网页只有 [Linux/x64](https://jdk.java.net/java-se-ri/11)、[Windows/x64](https://jdk.java.net/java-se-ri/11) 两个版本，
> 如需更多请前往[OracleJDK](https://www.oracle.com/java/technologies/downloads/#java11)官网下载

	openJDK11: https://jdk.java.net/java-se-ri/11

2、克隆项目

3、解决依赖

	gradle build

### 提交拉请求
1、执行spotless插件任务（统一代码格式）

	./gradlew spotlessApply

2、提交你的pr

### 作者
* QQ : 2506162335
* 微信: m47705945
* 邮箱: 18573707104@163.com
* 描述: 如果有任何问题请联系作者
