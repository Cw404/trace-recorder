# ![logo](./logo.png "trace-recorder logo")

Simple, Scalable, High-Powered. trace-recorder is a trace recorder, mainly in order to better record the source code flow,
can better access to the source code implementation of the basic process and the full process;
Records can be better managed with a scalable plug-in architecture.

### News
* TraceRecorder log info to dashboard show.
* TraceRecorder log info to echarts bar chart show.
* TraceRecorder log info to echarts relation chart show.

### Use
**Project depend on**

gradle
```gradle
implementation 'cn.xusc:trace-recorder:2.5.3'
```

maven
```maven
<dependency>
   <groupId>cn.xusc</groupId>
   <artifactId>trace-recorder</artifactId>
   <version>2.5.3</version>
</dependency>
```

### Sources Code Build
1、The JDK11 environment needs to be installed (openJDK is recommended)
> The following page is only [Linux/x64](https://jdk.java.net/java-se-ri/11)、[Windows/x64](https://jdk.java.net/java-se-ri/11) two versions，
> for more please visit [OracleJDK](https://www.oracle.com/java/technologies/downloads/#java11) official website to download

	openJDK11: https://jdk.java.net/java-se-ri/11

2、Cloning project

3、Dependency resolution

	gradle build

### Submit Pull requests
1、Execute task of spotless plugin（uniform code format）

	./gradlew spotlessApply

2、Submit your pr

### Author
* QQ    : 2506162335
* WeChat: m47705945
* email : 18573707104@163.com
* describe: Please contact the author if you have any questions

## Special thanks to JetBrains for supporting open source projects
<a href="https://jb.gg/OpenSourceSupport">
  <img src="https://user-images.githubusercontent.com/8643542/160519107-199319dc-e1cf-4079-94b7-01b6b8d23aa6.png" align="left" height="100" width="100"  alt="JetBrains">
</a>
