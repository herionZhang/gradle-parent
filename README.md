# IDEA 2020.2 +Gradle 6.6.1 + Spring Boot 2.3.4 创建多模块项目
gradle 多工程创建

##  环境介绍

### IDEA
我用的是2020.2
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200928125010686.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3poNDUyNjQ3NDU3,size_16,color_FFFFFF,t_70#pic_center)

### Gradle
-  安装参考 [Gradle安装配置](https://blog.csdn.net/zh452647457/article/details/108753607)
- 我这安装的是6.6.1

```
C:\Users\herion>gradle -v

------------------------------------------------------------
Gradle 6.6.1
------------------------------------------------------------

Build time:   2020-08-25 16:29:12 UTC
Revision:     f2d1fb54a951d8b11d25748e4711bec8d128d7e3

Kotlin:       1.3.72
Groovy:       2.5.12
Ant:          Apache Ant(TM) version 1.10.8 compiled on May 10 2020
JVM:          1.8.0_211 (Oracle Corporation 25.211-b12)
OS:           Windows 10 10.0 amd64
```

## 创建 gradle-parent
- New Project --> Spring Initalizr 选择jdk版本，我这里使用1.8
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200928101706286.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3poNDUyNjQ3NDU3,size_16,color_FFFFFF,t_70#pic_center)
-  Next--> 根据需求修改 Group、Artifact、version 、Type、name、package 等，选择所需依赖创建项目
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200928101938930.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3poNDUyNjQ3NDU3,size_16,color_FFFFFF,t_70#pic_center)
- 创建成功后删除src 目录

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200928102652409.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3poNDUyNjQ3NDU3,size_16,color_FFFFFF,t_70#pic_center)

## 创建子模块 gradle-demo
- 选中gradle-parent--> new -->Module
![在这里插入图片描述](https://img-blog.csdnimg.cn/2020092810292164.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3poNDUyNjQ3NDU3,size_16,color_FFFFFF,t_70#pic_center)
 - 创建子模块操作与创建gradle-parent 雷同，这里就不做复述了，创建好gradle-demo后在gradle-parent的settings.gradle 中引入模块依赖 include 'gradle-demo'
 ![在这里插入图片描述](https://img-blog.csdnimg.cn/20200928103451432.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3poNDUyNjQ3NDU3,size_16,color_FFFFFF,t_70#pic_center)
- 删除gradle-demo 中settings.gradle文件，否则不能喝gradle-parent建立依赖关系
 ![在这里插入图片描述](https://img-blog.csdnimg.cn/20200928103717188.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3poNDUyNjQ3NDU3,size_16,color_FFFFFF,t_70#pic_center)
- 定义gradle 自身所需资源

```
buildscript {
    ext {
        springBootVersion = '2.3.4.RELEASE'
        springBootManagementVersion = '1.0.8.RELEASE'
        springCloudVersion = 'Hoxton.SR6'
        REPOSITORY_HOME = "http://maven.aliyun.com"
    }
    repositories {
        maven { url '${REPOSITORY_HOME}/nexus/content/groups/public/' }
        mavenCentral()
        maven { url 'https://repo.spring.io/snapshot' }
        maven { url 'https://repo.spring.io/milestone' }
    }
    dependencies {
        classpath("org.springframework.boot:spring-boot-gradle-plugin:${springBootVersion}")
        classpath("io.spring.gradle:dependency-management-plugin:${springBootManagementVersion}")
    }
}
```

- 修改gradle-parent项目build.gradle 配置全项目通用配置

```
allprojects {
    apply plugin: 'java'
    apply plugin: 'idea'
    group = 'com.herion'
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}
```

- 子项目通用配置

```
subprojects {
    apply plugin: 'org.springframework.boot'
    apply plugin: 'io.spring.dependency-management'
    [compileJava, compileTestJava, javadoc]*.options*.encoding = 'UTF-8'
    //仓库
    repositories {
        maven { url '${REPOSITORY_HOME}/nexus/content/groups/public/' }
        mavenCentral()
        maven { url 'https://repo.spring.io/snapshot' }
        maven { url 'https://repo.spring.io/milestone' }
    }

    dependencies {
        implementation 'org.springframework.boot:spring-boot-starter'
        compileOnly 'org.projectlombok:lombok'
        developmentOnly 'org.springframework.boot:spring-boot-devtools'
        annotationProcessor 'org.springframework.boot:spring-boot-configuration-processor'
        annotationProcessor 'org.projectlombok:lombok'
        testImplementation('org.springframework.boot:spring-boot-starter-test') {
            exclude group: 'org.junit.vintage', module: 'junit-vintage-engine'
        }
    }

    dependencyManagement {
        imports { mavenBom("org.springframework.boot:spring-boot-dependencies:${springBootVersion}") }
        imports { mavenBom "org.springframework.cloud:spring-cloud-dependencies:${springCloudVersion}" }
    }

    test {
        useJUnitPlatform()
    }
}
```
- 发布插件配置

```

    /**
     * 发布插件
     */
    publishing {
        publications {
            mavenJava(MavenPublication) {
                from components.java
                versionMapping {
                    usage('java-api') {
                        fromResolutionOf('runtimeClasspath')
                    }
                    usage('java-runtime') {
                        fromResolutionResult()
                    }
                }
            }
        }
        // 发布仓库
        repositories {
            maven {
                // TODO 换成自己的私服地址
                def releasesRepoUrl = "http://my.repo.com/nexus/repository/maven-releases"
                def snapshotsRepoUrl = "http://my.repo..com/nexus/repository/maven-snapshots"
                url = version.endsWith('SNAPSHOT') ? snapshotsRepoUrl : releasesRepoUrl
                credentials {
                    username nexusUser
                    password nexusPassword
                }
            }
        }
    }

    configurations {
        [apiElements, runtimeElements].each {
            it.outgoing.artifacts.removeIf { it.buildDependencies.getDependencies(null).contains(jar) }
            it.outgoing.artifact(bootJar)
        }
    }

```
## 验证
## Gradle 查看所有项目

```
 gradle projects
> Task :projects
------------------------------------------------------------
Root project
------------------------------------------------------------
Root project 'gradle-parent'
+--- Project ':gradle-common'
+--- Project ':gradle-demo'
\--- Project ':gradle-demo2'

To see a list of the tasks of a project, run gradle <project-path>:tasks
For example, try running gradle :gradle-common:tasks

Deprecated Gradle features were used in this build, making it incompatible with Gradle 7.0.
Use '--warning-mode all' to show the individual deprecation warnings.
See https://docs.gradle.org/6.6.1/userguide/command_line_interface.html#sec:command_line_warnings

BUILD SUCCESSFUL in 4s
1 actionable task: 1 executed
```
### 编译项目

```
$ gradle build

Deprecated Gradle features were used in this build, making it incompatible with Gradle 7.0.
Use '--warning-mode all' to show the individual deprecation warnings.
See https://docs.gradle.org/6.6.1/userguide/command_line_interface.html#sec:command_line_warnings

BUILD SUCCESSFUL in 5s
12 actionable tasks: 12 up-to-date
```
 -  执行结果

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200929142705542.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3poNDUyNjQ3NDU3,size_16,color_FFFFFF,t_70#pic_center)


## 发布jar包到nexus
- 命令
```
$ gradle publishMavenJavaPublicationToMavenRepository
> Task :gradle-common:publishMavenJavaPublicationToMavenRepository
> Task :gradle-demo:publishMavenJavaPublicationToMavenRepository
> Task :gradle-demo2:publishMavenJavaPublicationToMavenRepository
Deprecated Gradle features were used in this build, making it incompatible with Gradle 7.0.
Use '--warning-mode all' to show the individual deprecation warnings.
See https://docs.gradle.org/6.6.1/userguide/command_line_interface.html#sec:command_line_warnings
BUILD SUCCESSFUL in 24s
16 actionable tasks: 13 executed, 3 up-to-date

```
- 执行结果
![在这里插入图片描述](https://img-blog.csdnimg.cn/2020092914281642.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3poNDUyNjQ3NDU3,size_16,color_FFFFFF,t_70#pic_center)
