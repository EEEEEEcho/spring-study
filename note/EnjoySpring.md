### Spring(一)

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

