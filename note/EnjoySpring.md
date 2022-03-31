Spring(一)

#### Spring体系结构

![image-20220322143709745](EnjoySpring.assets/image-20220322143709745.png)

#### XML装配bean

```xml
<bean id="person" class="com.echo.enjoy.chapter1.pojo.Person">
    <property name="name" value="echo"/>
    <property name="age" value="18" />
</bean>
```

#### 配置类装配bean

```java
@Configuration	//告诉Spring这是一个配置类
public class MainConfig {
    //给容器中注册一个bean，类型为返回值类型
    @Bean
    public Person person(){
        return new Person("james",20);
    }
}
```

```java
public static void useAnnotation(){
    ApplicationContext context = new AnnotationConfigApplicationContext(MainConfig.class);
    //用配置文件配置的bean，bean的id使用的是方法的名字，比如说。
    //如果方法名字为person，那么这个bean的id就是person，如果是getPerson
    //那么这个bean的id就是getPerson
    Person person = context.getBean("person", Person.class);
    System.out.println(person);
    //获取bean的名称
    String[] namesForBean = context.getBeanNamesForType(Person.class);
    System.out.println(Arrays.toString(namesForBean));
}
```

```bash
Person{name='james', age=20}
[person]
```

#### 包扫描@ComponentScan

```java
@Configuration
//扫描com.echo.enjoy.chapter1包下的所有被声明为bean的组件
@ComponentScan(value = "com.echo.enjoy.chapter1")
public class MainConfig2 {
    @Bean
    public Person person(){
        return new Person("james",20);
    }
}
```

测试

```java
@Test
public void test01(){
    ApplicationContext context = new AnnotationConfigApplicationContext(MainConfig2.class);
    //获取容器中所有bean的名字
    String[] names = context.getBeanDefinitionNames();
    for (String name: names){
        System.out.println(name);
    }
}
```

```bash
org.springframework.context.annotation.internalConfigurationAnnotationProcessor
org.springframework.context.annotation.internalAutowiredAnnotationProcessor
org.springframework.context.event.internalEventListenerProcessor
org.springframework.context.event.internalEventListenerFactory
mainConfig2
mainConfig
orderController
orderDao
orderService
person
```

概述

```java
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Repeatable(ComponentScans.class)
public @interface ComponentScan {
    /*.....*/
	//定制要扫面的组件
    ComponentScan.Filter[] includeFilters() default {};
	//定制要排除的组件
    ComponentScan.Filter[] excludeFilters() default {};
    
    /*...*/
}
```

自定义扫描规则

```java
@ComponentScan(
    value = "com.echo.enjoy.chapter1", //要扫描的包
    includeFilters = {
        @ComponentScan.Filter(type = FilterType.ANNOTATION,classes = {Controller.class})
	},//声明为按注解过滤，扫描时只扫描被Controller注解标注的bean
	useDefaultFilters = false	//关闭默认的过滤规则
)
```

```bash
org.springframework.context.annotation.internalConfigurationAnnotationProcessor
org.springframework.context.annotation.internalAutowiredAnnotationProcessor
org.springframework.context.event.internalEventListenerProcessor
org.springframework.context.event.internalEventListenerFactory
mainConfig2
orderController
person
```

自定义扫描方式，通过实现TypeFilter接口来进行自定义扫描规则的定制

```java
public class MyTypeFilter implements TypeFilter {
    /**
     *
     * @param metadataReader 读取到当前正在扫描的类信息
     * @param metadataReaderFactory 从该工厂可以获取到其他任何类信息
     * @return 是否过滤
     * @throws IOException
     */
    @Override
    public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
        //获取当前扫描到的注解的元数据
        AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();
        //获取当前扫描到的类的元数据
        ClassMetadata classMetadata = metadataReader.getClassMetadata();
        //获取当前类的资源(其实就是类的路径)
        Resource resource = metadataReader.getResource();
        //获取当前扫描到的类的名字
        String className = classMetadata.getClassName();
        System.out.println("当前扫描的类是: " + className);
        return false;
    }
}
```

```java
@ComponentScan(
        value = "com.echo.enjoy.chapter1",
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.CUSTOM, classes = {MyTypeFilter.class})
        },
        useDefaultFilters = false
)
```

运行测试

```java
@Test
public void test01(){
    ApplicationContext context = new AnnotationConfigApplicationContext(MainConfig2.class);
    //获取容器中所有bean的名字
    String[] names = context.getBeanDefinitionNames();
    for (String name: names){
        System.out.println(name);
    }
}
```

```bash
当前扫描的类是: com.echo.enjoy.chapter1.Demo1
当前扫描的类是: com.echo.enjoy.chapter1.filter.MyTypeFilter
当前扫描的类是: com.echo.enjoy.chapter1.config.MainConfig
当前扫描的类是: com.echo.enjoy.chapter1.controller.OrderController
当前扫描的类是: com.echo.enjoy.chapter1.dao.OrderDao
当前扫描的类是: com.echo.enjoy.chapter1.pojo.Person
当前扫描的类是: com.echo.enjoy.chapter1.service.OrderService
org.springframework.context.annotation.internalConfigurationAnnotationProcessor
org.springframework.context.annotation.internalAutowiredAnnotationProcessor
org.springframework.context.event.internalEventListenerProcessor
org.springframework.context.event.internalEventListenerFactory
mainConfig2
person
```

可以看到，只有Spring内置的bean，配置文件bean，以及我们在配置文件中手动注入的bean，被装载到了容器中，那些需要进行扫描装载的@Controller、@Service、@Repository都没有被扫描进来，**因为我们的方法返回的是false**

```java
@Override
public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
    //获取当前扫描到的注解的元数据
    AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();
    //获取当前扫描到的类的元数据
    ClassMetadata classMetadata = metadataReader.getClassMetadata();
    //获取当前类的资源(其实就是类的路径)
    Resource resource = metadataReader.getResource();
    //获取当前扫描到的类的名字
    String className = classMetadata.getClassName();
    System.out.println("当前扫描的类是: " + className);
    //如果类的全限定类名中包含echo,那么扫面进去
    if (className.contains("echo")){
        return true;
    }
    return false;
}
```

```bash
当前扫描的类是: com.echo.enjoy.chapter1.Demo1
当前扫描的类是: com.echo.enjoy.chapter1.config.MainConfig
当前扫描的类是: com.echo.enjoy.chapter1.controller.OrderController
当前扫描的类是: com.echo.enjoy.chapter1.dao.OrderDao
当前扫描的类是: com.echo.enjoy.chapter1.filter.MyTypeFilter
当前扫描的类是: com.echo.enjoy.chapter1.pojo.Person
当前扫描的类是: com.echo.enjoy.chapter1.service.OrderService
org.springframework.context.annotation.internalConfigurationAnnotationProcessor
org.springframework.context.annotation.internalAutowiredAnnotationProcessor
org.springframework.context.event.internalEventListenerProcessor
org.springframework.context.event.internalEventListenerFactory
mainConfig2
demo1
mainConfig
orderController
orderDao
myTypeFilter
person
orderService
```

#### bean的作用域@Scope

Spring中的bean默认是单例的

作用域为Singleton(单例)时：**对象会随着容器的创建而创建**。以后每次获取都是从容器中拿(Map)的同一个bean

```java
@Bean
public Person person() {
    return new Person("james", 20);
}
```

```java
@Test
public void test02(){
    ApplicationContext context = new AnnotationConfigApplicationContext(MainConfig2.class);

    Person person1 = context.getBean("person", Person.class);
    Person person2 = context.getBean("person", Person.class);
    System.out.println(person1 == person2);
}
```

```bash
true
```

作用域为Prototype(多实例)时：**对象是懒加载的**，只有在需要对象时，才会进行对象的创建

```java
@Bean
@Scope(value = "prototype")
public Person person() {
    return new Person("james", 20);
}
```

```java
@Test
public void test02(){
    ApplicationContext context = new AnnotationConfigApplicationContext(MainConfig2.class);

    Person person1 = context.getBean("person", Person.class);
    Person person2 = context.getBean("person", Person.class);
    System.out.println(person1 == person2);
}
```

```bash
false
```

#### @Lazy懒加载

主要针对单实例bean，容器启动时不创建对象。当且仅当第一次获取(bean)的时候才被创建初始化。

```java
@Bean
public Person person() {
    System.out.println("给容器中添加person");
    return new Person("james", 20);
}
```

```java
@Test
public void test02(){
    ApplicationContext context = new AnnotationConfigApplicationContext(MainConfig2.class);
    System.out.println("IOC容器创建完成");
    Person person1 = context.getBean("person", Person.class);
    System.out.println(person1);
}
```

```bash
给容器中添加person
IOC容器创建完成
Person{name='james', age=20}
```

加上懒加载

```java
@Bean
@Lazy
public Person person() {
    System.out.println("给容器中添加person");
    return new Person("james", 20);
}
```

```java
@Test
public void test02(){
    ApplicationContext context = new AnnotationConfigApplicationContext(MainConfig2.class);
    System.out.println("IOC容器创建完成");
    Person person1 = context.getBean("person", Person.class);
    System.out.println(person1);
}
```

```bash
IOC容器创建完成
给容器中添加person
Person{name='james', age=20}
```

### Spring(二)

#### @Conditional条件注入

首先说一下FactoryBean和BeanFactory的区别：

FactoryBean：可以将Java实例Bean通过FactoryBean注入到容器中

BeanFactory: 可以从容器中获取实例化后的bean

**运行时，如果是windows操作系统，让echo注入容器，如果是linux操作系统让jane注入容器**

1.创建配置类

```java
@Configuration
public class MainConfig1 {

    @Bean("echo")
    //加一个bean注入容器时的条件
    @Conditional(WindowsCondition.class)
    public Person getEcho(){
        System.out.println("给容器中添加echo......");
        return new Person("echo",27);
    }

    @Bean("jane")
    //加一个bean注入容器时的条件
    @Conditional(LinuxCondition.class)
    public Person getJane(){
        System.out.println("给容器中添加jane......");
        return new Person("jane",28);
    }

    @Bean("lucky")
    public Person getLucky(){
        System.out.println("给容器中添加lucky......");
        return new Person("lucky",29);
    }
}
```

2.实现Condition接口创建不同的条件

```java
public class WindowsCondition implements Condition {
    /**
     * @param conditionContext 判断条件可以使用的上下文环境
     * @param annotatedTypeMetadata 注解的元数据信息
     * @return 是否符合条件
     */
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        //能获取到IOC容器正在使用的beanFactory
        ConfigurableListableBeanFactory beanFactory = conditionContext.getBeanFactory();
        //获取当前的环境变量，包括操作系统类型
        Environment environment = conditionContext.getEnvironment();
        //获取当前环境的操作系统
        String os = environment.getProperty("os.name");
        if (os.contains("Windows")){
            return true;
        }
        return false;
    }
}
```

```java
public class LinuxCondition implements Condition {
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        //能获取到IOC容器正在使用的beanFactory
        ConfigurableListableBeanFactory beanFactory = conditionContext.getBeanFactory();
        //获取当前的环境变量，包括操作系统类型
        Environment environment = conditionContext.getEnvironment();
        //获取当前环境的操作系统
        String os = environment.getProperty("os.name");
        if (os.contains("linux")){
            return true;
        }
        return false;
    }
}
```

3.测试

```java
@Test
public void test01(){
    ApplicationContext context = new AnnotationConfigApplicationContext(MainConfig1.class);
    //运行时，如果是windows操作系统，让echo注入容器，如果是linux操作系统让jane注入容器
    //
    System.out.println("容器初始化完成");
}
```

```bash
给容器中添加echo......
给容器中添加lucky......
容器初始化完成
```

#### @Import注册bean

```java
/**
     * 给容器中注册组件(Bean)的方式
     * 1.@Bean：适用于导入第三方的类或包的组件，例如Person为第三方的类，需要我们在
     * IOC容器中使用时，需要使用该注解
     * 2.包扫描(@ComponentScan) + 组件的标注注解(@Controller,@Service,@Repository,@Component)
     * 一般是针对我们自己写的类，
     * 3.@Import:能够快速给容器导入一个Bean，
     *    3.1 @Import(value = {Dog.class, Cat.class})value中的值是要注入到容器中的bean的class，bean的ID为类的全限定类名
     *    3.2 ImportSelector:是一个接口，返回需要导入到容器的组件的全类名数组
     *    3.3 ImportBeanDefinitionRegister: 可以手动添加组件到IOC容器，所有Bean的注册可以使用BeanDefinitionRegistry
     */
```

**直接使用@Import注解**

```java
public class Cat {
}
```

```java
public class Dog {
}
```

```java
@Configuration
@Import(value = {Dog.class, Cat.class})
public class MainConfig2 {
}
```

```java
@Test
public void test02(){
    ApplicationContext context = new AnnotationConfigApplicationContext(MainConfig2.class);
    System.out.println("容器初始化完成");
    //看看狗的bean是否注入到了容器中
    String[] names = context.getBeanDefinitionNames();
    for (String name : names){
        System.out.println(name);
    }
}
```

```bash
容器初始化完成
org.springframework.context.annotation.internalConfigurationAnnotationProcessor
org.springframework.context.annotation.internalAutowiredAnnotationProcessor
org.springframework.context.event.internalEventListenerProcessor
org.springframework.context.event.internalEventListenerFactory
mainConfig2
com.echo.enjoy.chapter2.pojo.Dog   //狗
com.echo.enjoy.chapter2.pojo.Cat	//猫
```

**使用ImportSelector**

创建两个新的类

```java
public class Fish {
}
```

```java
public class Cow {
}
```

创建一个ImportSelector并实现selectImports()方法，注意，该方法不可以返回null，否则会报错

```java
public class EchoImportSelector implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {
        return new String[]{"com.echo.enjoy.chapter2.pojo.Fish","com.echo.enjoy.chapter2.pojo.Cow"};
    }
}
```

```java
@Import(value = {Dog.class, Cat.class,EchoImportSelector.class})
```

测试

```java
@Test
public void test02(){
    ApplicationContext context = new AnnotationConfigApplicationContext(MainConfig2.class);
    System.out.println("容器初始化完成");
    //看看狗的bean是否注入到了容器中
    String[] names = context.getBeanDefinitionNames();
    for (String name : names){
        System.out.println(name);
    }
}
```

```bash
容器初始化完成
org.springframework.context.annotation.internalConfigurationAnnotationProcessor
org.springframework.context.annotation.internalAutowiredAnnotationProcessor
org.springframework.context.event.internalEventListenerProcessor
org.springframework.context.event.internalEventListenerFactory
mainConfig2
com.echo.enjoy.chapter2.pojo.Dog
com.echo.enjoy.chapter2.pojo.Cat
com.echo.enjoy.chapter2.pojo.Fish
com.echo.enjoy.chapter2.pojo.Cow
```

**使用ImportBeanDefinitionRegister**

```java
public class Car {
}
```

```java
public class Robot {
}
```

实现ImportBeanDefinitionRegistrar接口，重写registerBeanDefinitions()方法

```java
public class EchoBeanDefinitionRegister implements ImportBeanDefinitionRegistrar {
    /**
     * 自定义注入bean
     * @param importingClassMetadata 当前类的注解元数据
     * @param registry 把所有需要添加到容器中的bean加入
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        //ImportBeanDefinitionRegistrar.super.registerBeanDefinitions(importingClassMetadata, registry);
        //如果容器中包含Fish和Cow那么就注入Robot和Car
        boolean fish = registry.containsBeanDefinition("com.echo.enjoy.chapter2.pojo.Fish");
        boolean cow = registry.containsBeanDefinition("com.echo.enjoy.chapter2.pojo.Cow");
        if (fish && cow){
            //对于我们要注册的bean，要将其进行包装
            RootBeanDefinition car = new RootBeanDefinition(Car.class);
            RootBeanDefinition robot = new RootBeanDefinition(Robot.class);
            registry.registerBeanDefinition("robot",robot);
            registry.registerBeanDefinition("car",car);
        }
    }
}
```

将上面的类加入到Configuration的@Import注解中

```java
@Configuration
@Import(value = {Dog.class, Cat.class,EchoImportSelector.class,EchoBeanDefinitionRegister.class})
public class MainConfig2 {

}
```

测试运行

```java
@Test
public void test02(){
    ApplicationContext context = new AnnotationConfigApplicationContext(MainConfig2.class);
    System.out.println("容器初始化完成");
    //看看狗的bean是否注入到了容器中
    String[] names = context.getBeanDefinitionNames();
    for (String name : names){
        System.out.println(name);
    }
}
```

```bash
容器初始化完成
org.springframework.context.annotation.internalConfigurationAnnotationProcessor
org.springframework.context.annotation.internalAutowiredAnnotationProcessor
org.springframework.context.event.internalEventListenerProcessor
org.springframework.context.event.internalEventListenerFactory
mainConfig2
com.echo.enjoy.chapter2.pojo.Dog
com.echo.enjoy.chapter2.pojo.Cat
com.echo.enjoy.chapter2.pojo.Fish
com.echo.enjoy.chapter2.pojo.Cow
robot		//注入的
car			//注入的
```

#### 使用FacotryBean注入

```java
public class SpiderMan {
}
```

```java
public class EchoFactoryBean implements FactoryBean<SpiderMan> {
    @Override
    public SpiderMan getObject() throws Exception {
        return new SpiderMan();
    }

    @Override
    public Class<?> getObjectType() {
        return SpiderMan.class;
    }

    @Override
    public boolean isSingleton() {
        return FactoryBean.super.isSingleton();
    }
}
```

```java
@Configuration
@Import(value = {Dog.class, Cat.class,EchoImportSelector.class,EchoBeanDefinitionRegister.class})
public class MainConfig2 {

    //将自定义的工厂注入到容器中，然后容器会解析这个FactoryBean中对bean的定义，将FactoryBean中定义的bean注入到容器中
    @Bean
    public EchoFactoryBean echoFactoryBean(){
        return new EchoFactoryBean();
    }
}
```

```java
@Test
public void test03(){
    ApplicationContext context = new AnnotationConfigApplicationContext(MainConfig2.class);
    System.out.println("容器初始化完成");
    Object bean = context.getBean("echoFactoryBean");
    System.out.println(bean);
}
```

接下来会出现一个比较诡异的结果，我们将这个FactoryBean也注入到了容器中，这个FactoryBean会将在其中定义的SpiderMan注入到容器中，但是如果我们以context.getBean("echoFactoryBean")的方式来获取bean的话，获取的并不是这个FactoryBean而是它注入的SpiderMan的bean;这可以通过跟源码来发现。

```bash
com.echo.enjoy.chapter2.pojo.SpiderMan@126253fd
```

但是如果以这样的方式

```java
@Test
public void test03(){
    ApplicationContext context = new AnnotationConfigApplicationContext(MainConfig2.class);
    System.out.println("容器初始化完成");
    //        Object bean = context.getBean("echoFactoryBean");
    Object bean = context.getBean("&echoFactoryBean");	//加一个&
    System.out.println(bean);
}
```

那么就会正常拿到echoBeanFactoryBean

```bash
com.echo.enjoy.chapter2.config.EchoFactoryBean@126253fd
```

### Spring(三) bean的生命周期

先看一个简单的例子

#### 1.最朴素的生命周期(定义了init和destroy方法)

![image-20220323153433660](EnjoySpring.assets/image-20220323153433660.png)

**单实例**

```java
public class Bike {
    public Bike(){
        System.out.println("Bike constructor...");
    }
    public void init(){
        System.out.println("Bike init....");
    }
    public void destroy(){
        System.out.println("Bike destroy....");
    }
}
```

```java
@Bean(initMethod = "init",destroyMethod = "destroy")
public Bike bike(){
    return new Bike();
}
```

```java
@Test
public void test01(){
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig1.class);
    System.out.println("IOC容器创建完成");
    //关掉容器
    context.close();
    System.out.println("IOC容器被关闭");
}
```

```java
Bike constructor...
Bike init....
IOC容器创建完成
Bike destroy....
IOC容器被关闭
```

对于Singleton的bean可以正常调用初始化和销毁的方法



*扩展*

*IOC容器关闭，调用close方法时*

```java
// Destroy all cached singletons in the context's BeanFactory.
destroyBeans();

// Close the state of this context itself.
closeBeanFactory();

// Let subclasses do some final clean-up if they wish...
onClose();

```

*调用destroyBeans()*

```java
this.containedBeanMap.clear();		//把装bean的map清空
this.dependentBeanMap.clear();
this.dependenciesForBeanMap.clear();
```

*增强bean的核心代码*

```java
Object wrappedBean = bean;
//BeanPostProcessorsBeforeInitialization
if (mbd == null || !mbd.isSynthetic()) {
    wrappedBean = applyBeanPostProcessorsBeforeInitialization(wrappedBean, beanName);
}
//init
try {
    //@Bean(initMethod="init") 增强的是init方法？
    invokeInitMethods(beanName, wrappedBean, mbd);
}
catch (Throwable ex) {
    throw new BeanCreationException(
        (mbd != null ? mbd.getResourceDescription() : null),
        beanName, "Invocation of init method failed", ex);
}
//BeanPostProcessorsAfterInitialization
if (mbd == null || !mbd.isSynthetic()) {
    wrappedBean = applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);
}

return wrappedBean;
```

**多实例**

对于多实例的bean，容器只负责初始化，但不会管理bean，容器关闭时不会调用销毁方法.

```java
@Scope("prototype")
@Bean(initMethod = "init",destroyMethod = "destroy")
public Bike bike(){
	return new Bike();
}
```

```bash
IOC容器创建完成
IOC容器被关闭
```

#### InitializingBean接口和DisposableBean接口

![image-20220323154230696](EnjoySpring.assets/image-20220323154230696.png)

```java
public interface InitializingBean {

	/**
	 * Invoked by the containing {@code BeanFactory} after it has set all bean properties
	 * and satisfied {@link BeanFactoryAware}, {@code ApplicationContextAware} etc.
	 * <p>This method allows the bean instance to perform validation of its overall
	 * configuration and final initialization when all bean properties have been set.
	 * @throws Exception in the event of misconfiguration (such as failure to set an
	 * essential property) or if initialization fails for any other reason
	 */
    //bean的属性设值之后调用
	void afterPropertiesSet() throws Exception;

}
```

```java
public interface DisposableBean {

	/**
	 * Invoked by the containing {@code BeanFactory} on destruction of a bean.
	 * @throws Exception in case of shutdown errors. Exceptions will get logged
	 * but not rethrown to allow other beans to release their resources as well.
	 */
    //在bean销毁时，由BeanFactory调用此方法
	void destroy() throws Exception;

}
```

定义一个pojo

```java
@Component
public class Train implements InitializingBean, DisposableBean {
    public Train(){
        System.out.println("Train constructor...");
    }

    @Override
    //在bean销毁时，调用此方法
    public void destroy() throws Exception {
        System.out.println("Train DisposableBean destroy()...");
    }

    @Override
    //bean的属性设值,初始化完成之后调用
    public void afterPropertiesSet() throws Exception {
        System.out.println("Train InitializingBean afterPropertiesSet()...");
    }
}
```

```java
@Test
public void test02(){
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig1.class);
    System.out.println("IOC容器创建完成");
    //关掉容器
    context.close();
    System.out.println("IOC容器被关闭");
}
```

```bash
Train constructor...
Train InitializingBean afterPropertiesSet()...
IOC容器创建完成
Train DisposableBean destroy()...
IOC容器被关闭
```

#### @PostConstruct注解和@PreDestroy注解

![image-20220323155124249](EnjoySpring.assets/image-20220323155124249.png)

```java
@Component
public class Jeep {
    public Jeep(){
        System.out.println("Jeep Constructor ......");
    }
    @PostConstruct
    public void doInit(){
        System.out.println("Jeep doInit() ......");
    }
    @PreDestroy
    public void doDestroy(){
        System.out.println("Jeep doDestroy() ......");
    }
}
```

```java
@Test
public void test02(){
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig1.class);
    System.out.println("IOC容器创建完成");
    //关掉容器
    context.close();
    System.out.println("IOC容器被关闭");
}
```

```bash
Jeep Constructor ......
Jeep doInit() ......
IOC容器创建完成
Jeep doDestroy() ......
IOC容器被关闭
```

#### BeanPostProcessor

![image-20220323160341144](EnjoySpring.assets/image-20220323160341144.png)

```java
@Component
public class EkkoBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        //return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
        System.out.println("EkkoBeanPostProcessor postProcessBeforeInitialization() 处理了" + beanName);
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        //return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
        System.out.println("EkkoBeanPostProcessor postProcessAfterInitialization() 处理了" + beanName);
        return bean;
    }
}
```

```java
@Test
public void test02(){
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig1.class);
    System.out.println("IOC容器创建完成");
    //关掉容器
    context.close();
    System.out.println("IOC容器被关闭");
}
```

```bash
EkkoBeanPostProcessor postProcessBeforeInitialization() 处理了mainConfig1
EkkoBeanPostProcessor postProcessAfterInitialization() 处理了mainConfig1
Jeep Constructor ......
EkkoBeanPostProcessor postProcessBeforeInitialization() 处理了jeep
Jeep doInit() ......
EkkoBeanPostProcessor postProcessAfterInitialization() 处理了jeep
IOC容器创建完成
Jeep doDestroy() ......
IOC容器被关闭
```

#### 一个整体的生命周期流程

```java
public class Loser implements InitializingBean, DisposableBean {
    public static int count = 0;
    private String name;
    public Loser(String name){
        count++;
        System.out.println("这是构造方法: " + " count 的值为: " + count);
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void customInit(){
        count++;
        System.out.println("这是我自定义的init方法,在该方法里的name属性值为： " + this.name + " count 的值为: " + count);
    }

    public void customDestroy(){
        count++;
        System.out.println("这是我自定义的destroy方法,在该方法里的name属性值为：  " + this.name + " count 的值为: " + count);

    }

    @Override
    public void destroy() throws Exception {
        count++;
        System.out.println("这是DisposableBean的destroy方法,在该方法里的name属性值为：  " + this.name + " count 的值为: " + count);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        count++;
        System.out.println("这是InitializingBean的afterPropertiesSet方法,在该方法里的name属性值为： " + this.name  + " count 的值为: " + count);
    }

    @PostConstruct
    public void customPostConstruct(){
        count++;
        System.out.println("这是我自定义的由@PostConstruct标记的方法,在该方法里的name属性值为：  " + this.name  + " count 的值为: " + count);
    }

    @PreDestroy
    public void customPreDestroy(){
        count++;
        System.out.println("这是我自定义的由@PreDestroy标记的方法,在该方法里的name属性值为：  " + this.name  + " count 的值为: " + count);
    }
}

```

```java
@Component
public class EkkoBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        //return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
        if (beanName.equals("loser")){
            Loser loser = (Loser) bean;
            Loser.count ++;
            System.out.println("EkkoBeanPostProcessor postProcessBeforeInitialization() 处理了 " + beanName  +
                     " name属性值为：" + loser.getName() + " count 的值为: " + Loser.count);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        //return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
        if (beanName.equals("loser")){
            Loser loser = (Loser) bean;
            Loser.count ++;
            System.out.println("EkkoBeanPostProcessor postProcessAfterInitialization() 处理了 " + beanName  +
                    " name属性值为：" + loser.getName() + " count 的值为: " + Loser.count);
        }
        return bean;
    }
}

```

```java
@Configuration
public class MainConfig1 {
    @Bean
    public EkkoBeanPostProcessor ekkoBeanPostProcessor(){
        return new EkkoBeanPostProcessor();
    }

    @Bean(initMethod = "customInit",destroyMethod = "customDestroy")
    public Loser loser(){
        return new Loser("Ekko");
    }
}
```

```java
@Test
public void test02(){
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig1.class);
    System.out.println("IOC容器创建完成");
    //关掉容器
    context.close();
    System.out.println("IOC容器被关闭");
}
```

```bash
这是构造方法:  count 的值为: 1
EkkoBeanPostProcessor postProcessBeforeInitialization() 处理了 loser name属性值为：Ekko count 的值为: 2
这是我自定义的由@PostConstruct标记的方法,在该方法里的name属性值为：  Ekko count 的值为: 3
这是InitializingBean的afterPropertiesSet方法,在该方法里的name属性值为： Ekko count 的值为: 4
这是我自定义的init方法,在该方法里的name属性值为： Ekko count 的值为: 5
EkkoBeanPostProcessor postProcessAfterInitialization() 处理了 loser name属性值为：Ekko count 的值为: 6
IOC容器创建完成
这是我自定义的由@PreDestroy标记的方法,在该方法里的name属性值为：  Ekko count 的值为: 7
这是DisposableBean的destroy方法,在该方法里的name属性值为：  Ekko count 的值为: 8
这是我自定义的destroy方法,在该方法里的name属性值为：  Ekko count 的值为: 9
IOC容器被关闭
```

与经典流程图一样

![v2-baaf7d50702f6d0935820b9415ff364c_r](EnjoySpring.assets/v2-baaf7d50702f6d0935820b9415ff364c_r.jpg)

### Spring(四) BeanPostProcessor

#### 引子

@Autowired注解的实现其实就是一个BeanPostProcessor.

```java
public class AutowiredAnnotationBeanPostProcessor extends InstantiationAwareBeanPostProcessorAdapter
		implements MergedBeanDefinitionPostProcessor, PriorityOrdered, BeanFactoryAware {
}
```

![image-20220324164949644](EnjoySpring.assets/image-20220324164949644.png)

其实这些BeanPostProcessor也是一种注入到容器中的bean，只不过它们用来管理和控制我们创建的业务bean。



将bean交给processor处理的核心方法其实是：**applyBeanPostProcessorsBeforeInitialization()**

```java
wrappedBean = applyBeanPostProcessorsBeforeInitialization(wrappedBean, beanName);
```

Spring在该方法中的逻辑是在完成bean的创建和属性赋值之后，依次遍历所有的BeanPostProcessor然后进行相应的逻辑处理

```java
@Override
public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName)
    throws BeansException {
	//将当前的bean赋值给result
    Object result = existingBean;
    //遍历所有的Spring内置的和我们自定义的BeanPostProcessor
    for (BeanPostProcessor processor : getBeanPostProcessors()) {
        //如果bean需要当前的processor处理，那么使用当前processor对该bean进行处理。
        //例如，如果当前bean有@Autowire注解，在AutowiredAnnotationBeanPostProcessor被遍历到的时候，使用AutowiredAnnotationBeanPostProcessor进行处理。
        Object current = processor.postProcessBeforeInitialization(result, beanName);
        if (current == null) {
            return result;
        }
        result = current;
    }
    return result;
}
```

#### 一个@Async的例子

```java
@Async	//这个注解并不是SpringBoot中的注解，而是Spring的原生注解!!
public void methodA() throws Exception {
    System.out.println("methodA()...");
}
```

使用该注解之后，该方法会被AsyncAnnotationBeanPostProcessor进行拦截，然后开线程对方法进行执行。

```java
public class AsyncAnnotationBeanPostProcessor extends AbstractBeanFactoryAwareAdvisingPostProcessor {

	/**
	 * The default name of the {@link TaskExecutor} bean to pick up: "taskExecutor".
	 * <p>Note that the initial lookup happens by type; this is just the fallback
	 * in case of multiple executor beans found in the context.
	 * @since 4.2
	 * @see AnnotationAsyncExecutionInterceptor#DEFAULT_TASK_EXECUTOR_BEAN_NAME
	 */
	public static final String DEFAULT_TASK_EXECUTOR_BEAN_NAME =
			AnnotationAsyncExecutionInterceptor.DEFAULT_TASK_EXECUTOR_BEAN_NAME;


	protected final Log logger = LogFactory.getLog(getClass());

	@Nullable
	private Supplier<Executor> executor;

	@Nullable
	private Supplier<AsyncUncaughtExceptionHandler> exceptionHandler;

	@Nullable
	private Class<? extends Annotation> asyncAnnotationType;



	public AsyncAnnotationBeanPostProcessor() {
		setBeforeExistingAdvisors(true);
	}


	/**
	 * Configure this post-processor with the given executor and exception handler suppliers,
	 * applying the corresponding default if a supplier is not resolvable.
	 * @since 5.1
	 */
	public void configure(
			@Nullable Supplier<Executor> executor, @Nullable Supplier<AsyncUncaughtExceptionHandler> exceptionHandler) {

		this.executor = executor;
		this.exceptionHandler = exceptionHandler;
	}

	/**
	 * Set the {@link Executor} to use when invoking methods asynchronously.
	 * <p>If not specified, default executor resolution will apply: searching for a
	 * unique {@link TaskExecutor} bean in the context, or for an {@link Executor}
	 * bean named "taskExecutor" otherwise. If neither of the two is resolvable,
	 * a local default executor will be created within the interceptor.
	 * @see AnnotationAsyncExecutionInterceptor#getDefaultExecutor(BeanFactory)
	 * @see #DEFAULT_TASK_EXECUTOR_BEAN_NAME
	 */
	public void setExecutor(Executor executor) {
		this.executor = SingletonSupplier.of(executor);
	}

	/**
	 * Set the {@link AsyncUncaughtExceptionHandler} to use to handle uncaught
	 * exceptions thrown by asynchronous method executions.
	 * @since 4.1
	 */
	public void setExceptionHandler(AsyncUncaughtExceptionHandler exceptionHandler) {
		this.exceptionHandler = SingletonSupplier.of(exceptionHandler);
	}

	/**
	 * Set the 'async' annotation type to be detected at either class or method
	 * level. By default, both the {@link Async} annotation and the EJB 3.1
	 * {@code javax.ejb.Asynchronous} annotation will be detected.
	 * <p>This setter property exists so that developers can provide their own
	 * (non-Spring-specific) annotation type to indicate that a method (or all
	 * methods of a given class) should be invoked asynchronously.
	 * @param asyncAnnotationType the desired annotation type
	 */
	public void setAsyncAnnotationType(Class<? extends Annotation> asyncAnnotationType) {
		Assert.notNull(asyncAnnotationType, "'asyncAnnotationType' must not be null");
		this.asyncAnnotationType = asyncAnnotationType;
	}


	@Override
	public void setBeanFactory(BeanFactory beanFactory) {
		super.setBeanFactory(beanFactory);

		AsyncAnnotationAdvisor advisor = new AsyncAnnotationAdvisor(this.executor, this.exceptionHandler);
		if (this.asyncAnnotationType != null) {
			advisor.setAsyncAnnotationType(this.asyncAnnotationType);
		}
		advisor.setBeanFactory(beanFactory);
		this.advisor = advisor;
	}

}
```

#### Aware

```java
public class Hero implements ApplicationContextAware {

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("感知到了容器:" + applicationContext);
    }
}
```

```java
@Configuration
public class MainConfig1 {
    @Bean
    public Hero hero(){
        return new Hero();
    }
}
```

```java
@Test
public void test01(){
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig1.class);
    context.close();
}
```

```bash
感知到了容器:org.springframework.context.annotation.AnnotationConfigApplicationContext@69b794e2, started on Thu Mar 24 17:28:01 CST 2022
```

其实，处理实现的感知接口，也是由Spring的一个BeanPostProcessor来实现的。



Spring底层对BeanPostProcessor的使用，包括bean的赋值，注入其他组件，生命周期注解功能

#### @Value注解

使用@Value进行初始化赋值，赋值的类型包括：1.基本字符串，2.SpringEL表达式 3.读取配置文件

```java
public class Bird {
    //使用@Value进行初始化赋值，赋值的类型包括：1.基本字符串，2.SpringEL表达式 3.读取配置文件
    @Value("James")
    private String name;
    @Value("#{20-2}")   //SpringEL表达式
    private Integer age;
    @Value("${bird.color}")
    private String color;


    public Bird(){

    }

    public Bird(String name, Integer age,String color) {
        this.name = name;
        this.age = age;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "Bird{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", color='" + color + '\'' +
                '}';
    }
}
```

```java
@Configuration
//当容器启动时，会将这个配置加载到Environment中
@PropertySource(value = "classpath:/application.properties")
public class MainConfig1 {
    @Bean
    public Hero hero(){
        return new Hero();
    }

    @Bean
    public Bird bird(){
        return new Bird();
    }
}
```

```java
@Test
public void test01(){
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig1.class);
    //从容器中获取所有bean
    String[] beanDefinitionNames = context.getBeanDefinitionNames();
    for (String name: beanDefinitionNames){
        System.out.println(name);
    }
    //验证是否将bird.color属性加载到了环境变量中
    ConfigurableEnvironment environment = context.getEnvironment();
    System.out.println("environment : " + environment.getProperty("bird.color"));

    Bird bird = context.getBean("bird", Bird.class);
    System.out.println(bird);
    context.close();
}
```

```bash
感知到了容器:org.springframework.context.annotation.AnnotationConfigApplicationContext@69b794e2, started on Thu Mar 24 22:18:12 CST 2022
org.springframework.context.annotation.internalConfigurationAnnotationProcessor
org.springframework.context.annotation.internalAutowiredAnnotationProcessor
org.springframework.context.annotation.internalCommonAnnotationProcessor
org.springframework.context.event.internalEventListenerProcessor
org.springframework.context.event.internalEventListenerFactory
mainConfig1
hero
bird
environment : red
Bird{name='James', age=18, color='red'}
```

#### @Autowired

DAO

```java
@Repository
public class TestDao {
}
```

Service

```java
@Service
public class TestService {
    @Autowired
    private TestDao testDao;

    public void printDao(){
        System.out.println(testDao);
    }
}
```

Controller

```java
@Controller
public class TestController {
    @Autowired
    private TestService testService;
}
```

```java
@Configuration
@ComponentScan(value =
        {"com.echo.enjoy.chapter4.controller",
        "com.echo.enjoy.chapter4.service",
        "com.echo.enjoy.chapter4.dao"}
)
public class MainConfig1 {
}

```



```java
@Test
public void test02(){
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig1.class);
    //        String[] beanDefinitionNames = context.getBeanDefinitionNames();
    //        Arrays.stream(beanDefinitionNames).forEach(System.out::println);
    TestService testService = context.getBean("testService", TestService.class);
    TestDao testDao = context.getBean("testDao", TestDao.class);
    //测试容器中的TestDao和注入到TestService中TestDao是否相同
    testService.printDao();
    System.out.println(testDao);
    context.close();
}
```

```bash
com.echo.enjoy.chapter4.dao.TestDao@6e2aa843
com.echo.enjoy.chapter4.dao.TestDao@6e2aa843
```

可以看到使用@AutoWired注入的bean和直接注入到容器中的bean是一个。

**补充**

```java
@Autowired
private UserDao userDao;
```

使用上述方式，Spring在注入时，会先根据bean的名称，这里是userDao去容器中寻找，如果找到名称为userDao的bean则直接注入。如果找不到bean名称味userDao的bean，则会按照类型，去容器中寻找是否有com.echo.xxyy.dao.UserDao类型的bean，如果有的话，直接进行注入。



@Autowired可以加在很多地方

```java
@Component
public class Sun{
    private Moon moon;
    
    @Autowired	//在设置属性的方法上添加
    public void setMoon(Moon moon){
        this.moon = moon;
    }
    
    public Moon getMoon(){
        return this.moon;
    }
}
```

```java
@Component
public class Sun{
    private Moon moon;
    
    @Autowired	//在构造方法上添加
    public Sun(Moon moon){
        this.moon = moon;
    }
    
    public void setMoon(Moon moon){
        this.moon = moon;
    }
    
    public Moon getMoon(){
        return this.moon;
    }
}
```

```java
@Component
public class Sun{
    private Moon moon;
    
    //在参数上添加
    public Sun(@Autowired Moon moon){
        this.moon = moon;
    }
    
    public void setMoon(Moon moon){
        this.moon = moon;
    }
    
    public Moon getMoon(){
        return this.moon;
    }
}
```



#### @Qualifier

如果向容器中注入两次相同的beanid的bean。那么只会存在一个，毕竟容器是个字典，而且优先级是@Bean高于@Autowired

```java
@Service
public class TestService {
    @Autowired  //通过包扫面和@Autowired注入了一次
    private TestDao testDao;

    public void printDao(){
        System.out.println(testDao);
    }
}
```

在配置中又注入一次

```java
@Bean("testDao")
public TestDao testDao(){
    return new TestDao();
}
```

最后容器中只有一份

```java
@Test
public void test02(){
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig1.class);
    String[] beanDefinitionNames = context.getBeanDefinitionNames();
    Arrays.stream(beanDefinitionNames).forEach(System.out::println);

    TestService testService = context.getBean("testService", TestService.class);
    testService.printDao();
    TestDao testDao = context.getBean("testDao", TestDao.class);
    System.out.println(testDao);
    //        context.close();
}
```

```bash
org.springframework.context.annotation.internalConfigurationAnnotationProcessor
org.springframework.context.annotation.internalAutowiredAnnotationProcessor
org.springframework.context.annotation.internalCommonAnnotationProcessor
org.springframework.context.event.internalEventListenerProcessor
org.springframework.context.event.internalEventListenerFactory
mainConfig1
testController
testService
testDao
com.echo.enjoy.chapter4.dao.TestDao@5852c06f
com.echo.enjoy.chapter4.dao.TestDao@5852c06f
```

但是如果有不同的beanid的相同的bean注入，比如：

```java
@Bean("testDao2")
public TestDao testDao(){
    return new TestDao();
}
```

使用@Autowired注入时，注入的是testDao。这里又注入了一个testDao2。所以，使用时可以使用@Qualifier来进行指定bean的注入

```java
@Autowired
@Qualifier("testDao2")
private TestDao testDao;
```

#### @Primary

看这种情况

```java
public interface UserDao {
    void printUrl();
}
```

```java
public class UserDaoImpl1 implements UserDao{
    private String url = "1";

    @Override
    public void printUrl() {
        System.out.println(this.url);
    }
}
```

```java
public class UserDaoImpl2 implements UserDao{
    private String url = "2";
    @Override
    public void printUrl() {
        System.out.println(this.url);
    }
}
```

```java
@Bean
public UserDao userDaoImpl1(){
    return new UserDaoImpl1();
}
@Bean
public UserDao userDaoImpl2(){
    return new UserDaoImpl2();
}
```



 ```java
 @Service
 public class TestService {
     @Autowired
     private UserDao userDao;
 
     public void invokeUserDaoPrint(){
         userDao.printUrl();
     }
 }
 ```

```java
@Test
public void test02(){
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig1.class);
    TestService testService = context.getBean("testService", TestService.class);
    testService.invokeUserDaoPrint();
}
```

如果这时候进行测试，会直接报错，因为TestService中的UserDao具体是哪一个容器并不知道。

```bash
available: expected single matching bean but found 2: userDaoImpl1,userDaoImpl2
```

所以，解决方案是使用@Primary来告诉容器，优先要注入的是哪一个

```java
@Bean
@Primary
public UserDao userDaoImpl1(){
    return new UserDaoImpl1();
}
@Bean
public UserDao userDaoImpl2(){
    return new UserDaoImpl2();
}
```

```bash
1		//注入UserDaoImpl1成功
```

或者也可以使用@Qualifier来进行注入

```java
@Autowired
@Qualifier("userDaoImpl2")
private UserDao userDao;
```

```bash
2
```

#### @Resource

@Resource也可以将bean注入到容器中。@Resource是JSR250规范中的注解。

@Resource与@Autowired的异同

同：效果一样，都可以进行bean的装配

异:   @Resource不支持@Primary  

​		@Autowired可以使用@Autowired(required=false),进行这种设置后，如果@Autowired在容器中没有找到相应的bean，就不会进行注入操作，并不会报错。但是@Resource没有这种选项，只会发生报错。

补充：

1、@Resource是JDK原生的注解，@Autowired是Spring2.5 引入的注解

2、@Resource有两个属性name和type。Spring将@Resource注解的name属性解析为bean的名字，而type属性则解析为bean的类型。所以如果使用name属性，则使用byName的自动注入策略，而使用type属性时则使用byType自动注入策略。如果既不指定name也不指定type属性，这时将通过反射机制使用byName自动注入策略。

@Autowired只根据type进行注入，不会去匹配name。如果涉及到type无法辨别注入对象时，那需要依赖@Qualifier或@Primary注解一起来修饰。

#### @Inject

需要pom中额外引入javax.inject,和Autowired功能类似，支持@Primary,但是不支持required=false,该注解不依赖于Spring。可以注入到其他容器中。

### Spring(五) AOP的基本使用

![image-20220327222018648](EnjoySpring.assets/image-20220327222018648.png)

#### 自动装配：Aware注入Spring组件原理

![image-20220327225445740](EnjoySpring.assets/image-20220327225445740.png)

各种感知器都是实现了Aware接口。

`aware`,翻译过来是知道的，已感知的，意识到的，所以这些接口从字面意思应该是能感知到所有`Aware`前面的含义。比如ApplicationContextAware就是可以感知到ApplicationContext。

所以`BeanNameAware`接口是为了让**自身`Bean`能够感知到**，**获取到自身在Spring容器中的id属性**。

同理，其他的`Aware`接口也是为了能够感知到自身的一些属性。
 比如实现了`ApplicationContextAware`接口的类，能够获取到`ApplicationContext`，实现了`BeanFactoryAware`接口的类，能够获取到`BeanFactory`对象。

```java
@Component
public class Light implements ApplicationContextAware, BeanNameAware, EmbeddedValueResolverAware {
    private ApplicationContext context;
    
    @Override
    public void setBeanName(String name) {
        //感知bean自身的名字
        System.out.println("当前bean的名字为: " + name);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        //感知到bean自身所在的容器
        this.context = applicationContext;
        System.out.println("传入的IOC容器为:" + this.context);
    }

    @Override
    public void setEmbeddedValueResolver(StringValueResolver resolver) {
        //感知到字符值解析器
        String result = resolver.resolveStringValue("你好${os.name},计算#{3*8}");
        System.out.println("解析的字符串为: " + result);
    }
}
```

![image-20220328163819126](EnjoySpring.assets/image-20220328163819126.png)

#### SpringAOP

AOP:面向切面编程[底层就是动态代理]

指程序在运行期间动态的将某段代码切入到指定方法位置进行运行的编程方式。

AOP通知方法：

![image-20220328164002053](EnjoySpring.assets/image-20220328164002053.png)

引入依赖

```xml
<!-- https://mvnrepository.com/artifact/org.springframework/spring-aspects -->
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-aspects</artifactId>
    <version>5.2.9.RELEASE</version>
</dependency>
```

需要被切入的业务逻辑方法

```java
@Component("calculator")
public class Calculator {
    //假设这是业务逻辑方法
    public int div(int i,int j){
        return i / j;
    }
}

```

定义切面，然后编写切入点方法

```java
/**
 * 日志切面类的方法需要动态感知到业务逻辑方法的运行，
 * 通知方法：通知方法是对方法运行到的地点进行通知。也即拦截方法运行的过程
 *      前置通知 @Before：在我们执行目标业务逻辑方法之前执行。
 *      后置通知 @After：在我们执行目标业务逻辑方法运行结束之后执行，不管有无异常
 *      返回通知 @AfterReturning：在我们目标业务逻辑方法正常返回值之后执行
 *      异常通知 @AfterThrowing：在我们目标业务逻辑方法出现异常后运行
 *      环绕通知 @Around：动态代理。我们定义一个方法，然后在这个方法里随意写代码进行增强，然后手动执行
 *      业务逻辑方法。
 */
@Aspect
@Component
public class LogAspect {

//    @Before("execution (public int com.echo.enjoy.chapter5.aop.*(..))")
//    在执行com.echo.enjoy.chapter5.aop包的所有类的所有方法之前切入，方法的参数类型和个数无限制，返回值是int类型
//    @Before("execution (public int com.echo.enjoy.chapter5.aop.Calculator.*(..))")
//    在执行com.echo.enjoy.chapter5.aop.Calculator类下的所有方法之前切入，方法的参数类型和个数无限制，返回值是int类型
    @Before("execution (public int com.echo.enjoy.chapter5.aop.Calculator.div(int,int))")
    //在执行com.echo.enjoy.chapter5.aop.Calculator.div的方法前切入，方法的参数是两个int类型，返回值是int类型，这种是最具体的写法
    public void logStart(){
        System.out.println("业务逻辑方法开始运行...");
    }

    @After("execution (public int com.echo.enjoy.chapter5.aop.Calculator.div(int,int))")
    public void logEnd(){
        System.out.println("业务逻辑方法运行结束...");
    }

    @AfterReturning("execution (public int com.echo.enjoy.chapter5.aop.Calculator.div(int,int))")
    public void logReturning(){
        System.out.println("业务逻辑方法正常返回...");
    }

    @AfterThrowing("execution (public int com.echo.enjoy.chapter5.aop.Calculator.div(int,int))")
    public void logException(){
        System.out.println("业务逻辑方法抛出异常...");
    }

    @Around("execution (public int com.echo.enjoy.chapter5.aop.Calculator.div(int,int))")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("环绕通知：执行目标方法之前");
        Object obj = joinPoint.proceed();   //通过反射机制调用业务逻辑方法。我们决定什么时候区调用业务逻辑方法
        System.out.println("环绕通知：执行目标方法之后");
        return obj;
    }
}
```

**可以将切入点抽象出来**

```java
@Aspect
@Component
public class LogAspect {

    //将拦截的方法路径抽取出来，进行解耦
    @Pointcut("execution (public int com.echo.enjoy.chapter5.aop.Calculator.div(int,int))")
    public void pointCut(){}

//    @Before("execution (public int com.echo.enjoy.chapter5.aop.*(..))")
//    在执行com.echo.enjoy.chapter5.aop包的所有类的所有方法之前切入，方法的参数类型和个数无限制，返回值是int类型
//    @Before("execution (public int com.echo.enjoy.chapter5.aop.Calculator.*(..))")
//    在执行com.echo.enjoy.chapter5.aop.Calculator类下的所有方法之前切入，方法的参数类型和个数无限制，返回值是int类型
//    @Before("execution (public int com.echo.enjoy.chapter5.aop.Calculator.div(int,int))")
    //在执行com.echo.enjoy.chapter5.aop.Calculator.div的方法前切入，方法的参数是两个int类型，返回值是int类型，这种是最具体的写法
    @Before("pointCut()")
    public void logStart(){
        System.out.println("业务逻辑方法开始运行...");
    }

    @After("pointCut()")
    public void logEnd(){
        System.out.println("业务逻辑方法运行结束...");
    }

    @AfterReturning("pointCut()")
    public void logReturning(){
        System.out.println("业务逻辑方法正常返回...");
    }

    @AfterThrowing("pointCut()")
    public void logException(){
        System.out.println("业务逻辑方法抛出异常...");
    }
    @Around("pointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("环绕通知：执行目标方法之前");
        Object obj = joinPoint.proceed();   //通过反射机制调用业务逻辑方法。
        System.out.println("环绕通知：执行目标方法之后");
        return obj;
    }
}
```

配置类，开启切面支持

```java
@Configuration
@ComponentScan(value = "com.echo.enjoy.chapter5")
@EnableAspectJAutoProxy //开启切面，原生态的Spring需要手动开启，SpringBoot中已经封装好了
public class Config {
}
```

测试

```java
@Test
public void test02(){
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
    Calculator calculator = context.getBean("calculator", Calculator.class);
    int div = calculator.div(4, 2);
    System.out.println(div);
}
```

结果

```bash
环绕通知：执行目标方法之前
业务逻辑方法开始运行...
业务逻辑方法正常返回...
业务逻辑方法运行结束...
环绕通知：执行目标方法之后
2
```

异常

```java
@Test
public void test02(){
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
    Calculator calculator = context.getBean("calculator", Calculator.class);
    int div = calculator.div(4, 0);
    System.out.println(div);
}
```

```bash
环绕通知：执行目标方法之前
业务逻辑方法开始运行...
业务逻辑方法抛出异常...
业务逻辑方法运行结束...

java.lang.ArithmeticException: / by zero
```

将异常通知进行改造，让业务逻辑方法中抛出的异常对象被异常通知拿到

```java
@AfterThrowing(value = "pointCut()",throwing = "exception")
public void logException(Exception exception){
    System.out.println("业务逻辑方法抛出异常...: " + exception);
}
```

![image-20220328223231067](EnjoySpring.assets/image-20220328223231067.png)

```bash
环绕通知：执行目标方法之前
业务逻辑方法开始运行...
业务逻辑方法抛出异常...: java.lang.ArithmeticException: / by zero
业务逻辑方法运行结束...
```

**通过JoinPoint来获取切入点方法的元信息**

![image-20220328223958310](EnjoySpring.assets/image-20220328223958310.png)

```java
@Aspect
@Component
public class LogAspect {

    //将拦截的方法路径抽取出来，进行解耦
    @Pointcut("execution (public int com.echo.enjoy.chapter5.aop.Calculator.div(int,int))")
    public void pointCut(){}

    @Before("pointCut()")
    public void logStart(JoinPoint joinPoint){
        System.out.println();
        System.out.println("**************前置通知开始************");
        System.out.println("业务逻辑方法"+joinPoint.getSignature().getName()+
                " 开始运行之前传入的参数是:" + Arrays.asList(joinPoint.getArgs()));
        System.out.println("**************前置通知结束************");
        System.out.println();
    }

    @After("pointCut()")
    public void logEnd(JoinPoint joinPoint){

        System.out.println();
        System.out.println("**************后置通知开始************");
        System.out.println("业务逻辑方法"+joinPoint.getSignature().getName()+"运行结束...");
        System.out.println("**************后置通知结束************");
        System.out.println();

    }

    @AfterReturning(value = "pointCut()",returning = "value")
    public void logReturning(JoinPoint joinPoint,int value){
        System.out.println();
        System.out.println("**************返回通知开始************");
        System.out.println("业务逻辑方法"+joinPoint.getSignature().getName()+"的返回值是：" + value);
        System.out.println("**************返回通知结束************");
        System.out.println();

    }

    @AfterThrowing(value = "pointCut()",throwing = "exception")
    public void logException(JoinPoint joinPoint,Exception exception){
        System.out.println();
        System.out.println("**************异常通知开始************");
        System.out.println("业务逻辑方法"+joinPoint.getSignature().getName() + "抛出的异常是" + exception);
        System.out.println("**************异常通知结束************");
        System.out.println();
    }

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println();
        System.out.println("------------环绕通知：执行目标方法" + joinPoint.getSignature().getName() + "之前------------");
        Object obj = joinPoint.proceed();   //通过反射机制调用业务逻辑方法。
        System.out.println("------------环绕通知：执行目标方法" + joinPoint.getSignature().getName() + "之后------------");
        System.out.println();
        return obj;
    }
}
```

```bash
------------环绕通知：执行目标方法div之前------------

**************前置通知开始************
业务逻辑方法div 开始运行之前传入的参数是:[4, 2]
**************前置通知结束************


**************返回通知开始************
业务逻辑方法div的返回值是：2
**************返回通知结束************


**************后置通知开始************
业务逻辑方法div运行结束...
**************后置通知结束************

------------环绕通知：执行目标方法div之后------------

2
```



使用@EnableAspectJAutoProxy，

```java
@Import(AspectJAutoProxyRegistrar.class)
public @interface EnableAspectJAutoProxy {
    /../
}
```

会Import，将AspectJAutoProxyRegistrar这个bean注入到容器中，然后会发生很多连锁反应，最后将一个beanid为：org.springframework.aop.config.internalAutoProxyCreator，然后类型为AnnotationAwareAspectJAutoProxyCreator.class的bean注入到容器中。然后这个AnnotationAwareAspectJAutoProxyCreator贯穿了AOP的所有功能
