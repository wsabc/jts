使用H2,因为H2和mysql语法相近,比HSQL和Derby要好

username sa and an empty password
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.url=jdbc:h2:file:/data/demo

spring.h2.console.enabled=true
http://localhost:8080/h2-console

@DataJpaTest provides some standard setup needed for testing the persistence layer
configuring H2, an in-memory database
setting Hibernate, Spring Data, and the DataSource
performing an @EntityScan
turning on SQL logging

Spring Boot provides @TestConfiguration annotation that can be used on classes in src/test/java to indicate that they should not be picked up by scanning.

In most of the cases, @WebMvcTest will be limited to bootstrap a single controller. It is used along with @MockBean to provide mock implementations for required dependencies.

The integration tests need to start up a container to execute the test cases.

WebEnvironment.MOCK here – so that the container will operate in a mock servlet environment.
The difference from the Controller layer unit tests is that here nothing is mocked.

使用TestSuite时候,为了保证所有的context一致,最好使用一个基类用于准备环境
@SpringBootTest By default, once loaded, the configured ApplicationContext is reused for each test. Thus, the setup cost is incurred only once per test suite, and subsequent test execution is much faster.

maven-surefire-plugin默认扫描下列文件
```
"**/Test*.java" - includes all of its subdirectories and all Java filenames that start with "Test".
"**/*Test.java"
"**/*Tests.java"
"**/*TestCase.java"
```

SpringBoot 2.2.X之后surefire默认使用5,如果要指定4,使用下列
				<dependencies>
					<dependency>
						<groupId>org.apache.maven.surefire</groupId>
						<artifactId>surefire-junit47</artifactId>
						<version>3.0.0-M4</version>
					</dependency>
				</dependencies>
				
				
----

使用cucumber之类的好处,就是对非开发人员友好,大家都能看懂case
没有指定glue,会在runner的package下扫描lambdaGlue

You're not allowed to extend classes that define Step Definitions or hooks.

java.nio.file.InvalidPathException: Illegal char <:> at index 9: classpath:csfeatures//cs1.feature

screenplay pattern
In the Screenplay Pattern, we call a user interacting with the system an Actor. Actors are at the heart of the Screenplay Pattern (see The Screenplay Pattern uses an actor-centric model). Each actor has a certain number of Abilities, such as the ability to browse the web or to query a restful web service. Actors can also perform Tasks such as adding an item to the Todo list. To achieve these tasks, they will typically need to interact with the application, such as by entering a value into a field or by clicking on a button. We call these interactions Actions. Actors can also ask Questions about the state of the application, such as by reading the value of a field on the screen or by querying a web service.

At the heart of the Screenplay Pattern, an actor performs a sequence of tasks. In Serenity, this mechanism is implemented in the Actor class using a variation of the Command Pattern, where the actor executes each task by invoking a special method called performAs() on the corresponding Task object (see The actor invokes the performAs() method on a sequence of tasks).
 
Tasks can be created using annotated fields or builders
@Steps annotation. In the following code snippet, Serenity will instantiate the openTheApplication field for you, This works well for very simple tasks or actions
Instrumented.instanceOf().withProperties() methods
In practice, this means that the performAs() method of a task typically executes other, lower level tasks or interacts with the application in some other way.

The @Step annotation on the performAs() method is used to provide information about how the task will appear in the test reports:

Serenity Screenplay uses Rest-Assured to interact with rest endpoints, and to query the responses. 

----
Cucumber-JVM does not support running a hook only once.
 @DynamicPropertySource

Idea launches Junit by JUnitStarter
Spring2默认使用Junit5

Junit原理:
编写TestCase, 组成TestSuite, 需要一个TestRunner调用JunitCore.runClasses

Idea执行过程: 参数指定junit4
JUnitStarter > IdeaTestRunner > JUnit4IdeaTestRunner#startRunnerWithArgs > JUnitCore > @RunWith or BlockJUnit4ClassRunner by default
Junit5似乎后面也是委托给Junit4执行

final Result result = runner.run(testRunner); // JUnitCore.run(testRunner)
=>
ParentRunner.run(RunNotifier)
Statement childrenInvoker(RunNotifier notifier) 被指定的Runner覆盖,返回自己的Statement
这个statement评估的过程就是执行PickleRunner.run的过程

Cucumber的children是FeatureRunner, FeatureRunner里的children是一堆steps对应的PickleRunner
执行子类的runChild,也就是PickleRunner.run

Runner runner = runnerSupplier.get(); 获得runner,cucumber启动的时候就实例化了runner,并找到了所有的glue
Backend#loadGlue > classFinder.scanForSubClassesInPackage(basePackageName, LambdaGlue.class)

buildBackendWorlds(); ==> 启动context, initialize all steps

testCase.run(bus); 执行

==============具体调用@SpringBootTest的路径

SerenityObjectFactory newInstance 实例化steps
Serenity.initializeWithNoStepListener(instance).throwExceptionsImmediately();
injectDependenciesInto(testCase);
SpringDependencyInjector
contextManager.prepareTestInstance(target);

https://github.com/serenity-bdd/serenity-cucumber-starter/blob/master/pom.xml

