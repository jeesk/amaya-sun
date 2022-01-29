# amaya-core-sun [![maven-central](https://img.shields.io/maven-central/v/io.github.amayaframework/core-sun?color=blue)](https://repo1.maven.org/maven2/io/github/amayaframework/core-sun/)

Amaya is a fairly lightweight web framework for Java, which guarantees speed, ease of creating plugins/addons, 
flexibility and ease of use. 

![Logo](https://github.com/amayaframework/amaya-core/raw/main/images/logo.png)

The main goals pursued during the creation were the avoidance of redundant abstractions, 
modularity, simplification of the creation of REST services and sufficient freedom for user configuration 
and modification. The server part is built on the basis of a repackaged sun server.

## Getting Started

The core of the Amaya framework, which is an independent dependency. I.e. it is enough to install only it to
create a simple server. The kernel is built in such a way as not to force users to install unnecessary dependencies.
In case you need any missing functionality, it will be enough to deliver the necessary dependency. The only possible
inconvenience is the need to specify classindex in dependencies, but this is the fee for the launch speed:
other similar libraries are incomparably slow.

To install it, you will need:
* any build of the JDK no older than version 8
* [classindex](https://github.com/atteo/classindex)
* some implementation of slf4j
* Maven/Gradle

## Installing

### Gradle dependency

```Groovy
dependencies {
    implementation group: 'org.atteo.classindex', name: 'classindex', version: '3.4'
    annotationProcessor group: 'org.atteo.classindex', name: 'classindex', version: '3.4'
    implementation group: 'com.github.romanqed', name: 'jutils', version: '1.2.7'
    implementation group: 'javax.servlet', name: 'javax.servlet-api', version: '4.0.1'
    implementation group: 'io.github.amayaframework', name: 'core-api', version: '1.0.2'
    implementation group: 'io.github.amayaframework', name: 'core-sun', version: 'LATEST'
}
```

### Maven dependency

```
<dependency>
    <groupId>org.atteo.classindex</groupId>
    <artifactId>classindex</artifactId>
    <version>3.4</version>
</dependency>

<dependency>
    <groupId>com.github.romanqed</groupId>
    <artifactId>jutils</artifactId>
    <version>1.2.7</version>
</dependency>

<dependency>
    <groupId>javax.servlet</groupId>
    <artifactId>javax.servlet-api</artifactId>
    <version>4.0.1</version>
</dependency>

<dependency>
    <groupId>io.github.amayaframework</groupId>
    <artifactId>core-api</artifactId>
    <version>1.0.2</version>
</dependency>

<dependency>
    <groupId>io.github.amayaframework</groupId>
    <artifactId>core-sun</artifactId>
    <version>LATEST</version>
</dependency>
```

## Usage example

### Server class
```Java
public class Server {
    public static void main(String[] args) throws IOException {
        AmayaServer server = new AmayaBuilder().
                bind(8080).
                build();
        server.start();
    }
}
```

### MyController class
```Java
@Endpoint("/my-end-point")
public class MyController extends AbstractController {
    @Get("/{count:int}")
    public HttpResponse get(HttpRequest request, @Path("count") Integer count) {
        String helloWorld = "Hello, world!";
        StringBuilder response = new StringBuilder();
        for (int i = 0; i < count; ++i) {
            response.append(helloWorld).append('\n');
        }
        return ok(response.toString());
    }
}
```

Let's look at the controller code in more detail.
* The Endpoint annotation instructs the framework to add a controller to the server at the specified path.
* Inheritance from AbstractController ensures that the contents of your controller will be properly processed.
* The Get annotation indicates to the framework that you want to register the corresponding method located along 
the passed sub-path relative to the controller path.
* The Path annotation shows the framework that you want to put the value of the path variable "count" 
in the specified argument.

There is nothing complicated or requiring a long study in this construction. However, the user is 
provided with a convenient tool that allows you to quickly and easily create an api without thinking 
about unnecessary things.

To learn more about the core capabilities, check [this](https://github.com/AmayaFramework/amaya-core-api)

## Built With

* [Gradle](https://gradle.org) - Dependency management
* [classindex](https://github.com/atteo/classindex) - Annotation scanning
* [cglib](https://github.com/cglib/cglib) - Method wrapping
* [slf4j](https://www.slf4j.org) - Logging facade
* [javax.servet](https://docs.oracle.com/javaee/7/api/javax/servlet/Servlet.html) - Servlets
* [java-utils](https://github.com/RomanQed/java-utils) - Pipelines and other stuff
* [amaya-core-api](https://github.com/AmayaFramework/amaya-core-api) - Basic framework utilities
* [sun-http-server](https://github.com/AmayaFramework/sun-http-server) - Server part
* [amaya-filters](https://github.com/AmayaFramework/amaya-filters) - Implementation of string and content filters

## Authors
* **RomanQed** - *Main work* - [RomanQed](https://github.com/RomanQed)
* **max0000402** - *Technical advices and ideas for features* - [max0000402](https://github.com/max0000402)

See also the list of [contributors](https://github.com/AmayaFramework/amaya-core/contributors) 
who participated in this project.

## License

This project is licensed under the GNU General Public License v3.0 - see the [LICENSE](LICENSE) file for details

## Acknowledgments

<p>Thanks to all the sun developers who participated in the creation of this server.</p>
<p>Thanks to max0000402 for good advices and great suggestions.</p>
<p>Thanks to IntelliJ IDEA creators - this is a really great IDE that saved me a couple of hundred hours.</p>
