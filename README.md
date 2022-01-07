# amaya-core [![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.amayaframework/core/badge.svg?style=flat)](https://repo1.maven.org/maven2/io/github/amayaframework/core/)

Amaya is a fairly lightweight web framework for Java, which guarantees speed, ease of creating plugins/addons, 
flexibility and ease of use. The main goals pursued during the creation were the avoidance of redundant abstractions, 
modularity, simplification of the creation of REST services and sufficient freedom for user configuration 
and modification. The server part is built on the basis of a repackaged sun server.

## Getting Started

The core of the Amaya framework, which is an independent dependency. I.e. it is enough to install only it to
create a simple server. The kernel is built in such a way as not to force users to install unnecessary dependencies.
In case you need any missing functionality, it will be enough to deliver the necessary dependency. The only possible
inconvenience is the need to specify classindex in dependencies, but this is the fee for the launch speed:
other similar libraries are incomparably slow.

To install it, you will need:
* any version of the JDK no older than version 8
* [classindex](https://github.com/atteo/classindex)
* Maven/Gradle

## Installing

### Gradle dependency

```Groovy
dependencies {
    implementation group: 'org.atteo.classindex', name: 'classindex', version: '3.4'
    annotationProcessor group: 'org.atteo.classindex', name: 'classindex', version: '3.4'
    implementation group: 'io.github.amayaframework', name: 'core', version: 'LATEST'
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
    <groupId>io.github.amayaframework</groupId>
    <artifactId>core</artifactId>
    <version>LATEST</version>
</dependency>
```

## Usage example

The code below will start the server associated with the address localhost:8000.
The server will contain only one controller with the GET method, which will return 
the string "Hello, world!" repeated as many times as specified in the path parameter.

### Server class
```Java
public class Server {
    public static void main(String[] args) throws IOException {
        AmayaServer server = new AmayaBuilder().
                bind(new InetSocketAddress(8000)).
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

## Pipeline concept

To process incoming http requests, the Amaya framework uses a simple and flexible concept of actions performed 
sequentially on some data, called Pipeline.

This is a simplified diagram of what a typical pipeline data lifecycle looks like.

![Pipelines_1](https://github.com/amayaframework/amaya-core/raw/main/images/pipelines_1.png)

The core of the framework provides a basic set of actions for the pipeline, which allows 
processing incoming requests in accordance with the declared functionality.

There are 4 actions in total, and all their names are described in the ProcessStage enum.
In this order they are executed:
<p>Input actions:</p>

* FindRouteAction (receives: HttpExchange, returns RequestData)
* ParseRequestAction (receives: RequestData, returns Pair<HttpRequest, Route>)
* InvokeControllerAction (receives: Pair<HttpRequest, Route>, returns HttpResponse)

<p>Output actions:</p>

* CheckResponseAction (receives: PipelineResult, returns HttpResponse)

Thanks to this separation, almost any necessary functionality can be added by simply inserting 
the necessary actions between existing ones.

To change the corresponding pipeline handlers, the framework provides a simple tool - 
a list of consumers who accept the handler to be changed as an argument. 
Set once, this list will be executed for each handler being added.

## Filter concept

The idea of the filter is very simple - it is some handler that converts the received data set according 
to internal rules and then returns them. In case of any discrepancy, an exception is thrown. 
If there is no exception, filtering is considered successful.

In general, filters in the framework are used to convert path parameters and inject 
the required values into method arguments.

### String filters
This kind of filters is applied to the path parameters and converts them from the raw string type to the required one.

For example, built-in integer filter:
```Java
@NamedFilter("int")
public class IntegerFilter implements StringFilter {
    @Override
    public Object transform(String source) {
        return Integer.parseInt(source);
    }
}
```

### Content filters
This kind of filters is applied to the data prepared for injection into the argument, trying to 
extract data defined by some string from them.
For example, path filter, which will get the right one with a specific name from the entire map of path variables.

For example, built-in path filter:
```Java
@NamedFilter("path")
public class PathFilter implements ContentFilter {
    @Override
    @SuppressWarnings("unchecked")
    public Object transform(Object source, String name) {
        try {
            return ((Map<String, Object>) source).get(name);
        } catch (Exception e) {
            return null;
        }
    }
}
```

Note: If you want to create your own filters,
you need to connect the necessary [dependency](https://github.com/AmayaFramework/amaya-filters).

## Creating plugins
At the moment, taking into account all the above, we get an easy-to-modify and modular system. 
And the plugin in general will be a set of pipeline filters and actions combined into a pipeline configurator.

Also, all the necessary information for this is contained in javadocs.

### "Filter" plugins
This type of plugins simply implies a set of filter classes packaged in a library. 
All you need to create such a plugin is:

1) Fill in your build file correctly
<p>For gradle, it will be like:</p>

```Groovy
dependencies {
    implementation group: 'org.atteo.classindex', name: 'classindex', version: '3.4'
    annotationProcessor group: 'org.atteo.classindex', name: 'classindex', version: '3.4'
    implementation group: 'io.github.amayaframework', name: 'filters', version: 'LATEST'
}
```

2) Create the filters you want
<p>For example, this</p>

```Java
@NamedFilter("hello-filter")
public class IntegerFilter implements StringFilter {
    @Override
    public Object transform(String source) {
        if (source.startsWith("Hello")) {
            return "Hi there!";
        }
        return source;
    }
}
```
3) Build and publish your library to any convenient repository
4) Connect your library to an Amaya Framework-based project
5) Enjoy your beautiful filters!

### "Pipeline" plugins
This type of plug-ins may contain a set of pipeline actions that add some functionality.
All you need to create such a plugin is:
1) Connect the necessary dependencies
<p>For gradle: </p>

```Groovy
dependencies {
    implementation group: 'com.github.romanqed', name: 'jutils', version: 'LATEST'
    implementation group: 'io.github.amayaframework', name: 'http-server', version: 'LATEST'
    implementation group: 'io.github.amayaframework', name: 'core', version: 'LATEST'
}
```
2) Create a stage enum that lists all your actions and in the future will help other 
plugin authors easily find the names of your actions

```Java
public enum MyStage {
    MY_STAGE_1,
    MY_STAGE_2
}
```
3) Create the necessary pipeline actions
<p>For example, so:</p>

```Java
public class MyStage1Action extends PipelineAction<Pair<HttpRequest, Route>, Pair<HttpRequest, Route>> {
    public MyStage1Action() {
        super(MyStage.MY_STAGE_1.name());
    }

    @Override
    public Pair<HttpRequest, Route> apply(Pair<HttpRequest, Route> pair) {
        HttpRequest request = pair.getKey();
        request.setBody(new MyBeautifulDataFormat(request.getBody()));
        return pair;
    }
}
```

```Java
public class MyStage2Action extends PipelineAction<HttpResponse, HttpResponse> {
    public MyStage2Action() {
        super(MyStage.MY_STAGE_2.name());
    }

    @Override
    public HttpResponse apply(HttpResponse response) {
        response.setBody(((MyBeautifulDataFormat) response.getBody()).toString());
        return response;
    }
}
```

4) Create a pipeline configurator that collects the fruits of your labors into a single whole
<p>Example code:</p>

```Java
public class MyPipelineConfigurator implements Consumer<PipelineHandler> {
    @Override
    public void accept(PipelineHandler handler) {
        Pipeline input = handler.input();
        Pipeline output = handler.output();
        input.insertAfter(
                ProcessStage.PARSE_REQUEST.name(),
                MyStage.MY_STAGE_1.name(),
                new MyStage1Action()
        );
        output.insertAfter(
                ProcessStage.CHECK_RESPONSE.name(),
                MyStage.MY_STAGE_2.name(),
                new MyStage2Action()
        );
    }
}
```

3) Build and publish your library to any convenient repository
4) Connect your library to an Amaya Framework-based project
5) Add your configurator to the framework server

```Java
public class Server {
    public static void main(String[] args) throws IOException {
        AmayaServer server = new AmayaBuilder().
                addConfigurator(new MyPipelineConfigurator()).
                build();
        server.start();
    }
}
```

6) Enjoy your beautiful data format!

In the same way, you can change not only the body and any content included in the request and response, 
but also change their types directly, creating your own classes inherited from the base ones.

### "Common" plugins
This type of plugins simply implies the presence of mixed content inside, which was described above.

## Configuring the framework
In addition to plugins, you also have the ability to configure the framework and the sun server via singleton configs.

### Amaya config
The Amaya config allows you to configure a fairly small number of parameters, but they all strongly affect the behavior 
of the framework.

#### Route packer
(In enum: ROUTE_PACKER)
Specifies the wrapper that will be used to wrap the found methods inside the controller. 
It affects the speed of method invocation and allows you to organize the injection of values into arguments. 
Can be overridden by a custom class. Basic interface for it named Packer.

#### Router
(In enum: ROUTER)
Specifies the router class to be used in the controllers. Affects the speed of route search and includes 
support for processing route parameters. Can be overridden by a custom class. Basic interface for it named Router.

#### Charset
(In enum: CHARSET)
Specifies the encoding that will be used when processing the request and response.

#### Backlog
(In enum: BACKLOG)
Specifies the value of the backlog parameter to be passed to the sun server.

## Results

As a result, we get a fully customizable framework, similar to an easily shared constructor. 
And I hope that all the necessary functionality that is missing at the moment will be gradually 
implemented by the community in the form of free plug-in libraries. In addition, do not be 
surprised by the absence of some familiar things like cookie storage: the framework was developed 
with an eye to the ideas of the REST philosophy and hopes to remain in this form. Of course, 
the author might have forgotten to invent or implement some things, like the same cookie parser, 
so he is waiting for your issues.

## Built With

* [Gradle](https://gradle.org) - Dependency Management
* [classindex](https://github.com/atteo/classindex) - Annotation scanning
* [cglib](https://github.com/cglib/cglib) - Method wrapping
* [java-utils](https://github.com/RomanQed/java-utils) - Pipelines and other stuff
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
