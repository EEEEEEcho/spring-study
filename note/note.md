## 1.IOC容器

### 1.1 对IOC容器和Beans的简介

- org.springframework.beans和org.springframework.context构成了IOC实现的基础
- BeanFactory是能够管理各种类型对象的一个接口，ApplicationContext是它的子接口
- 简而言之，BeanFactory提供了配置框架和基本功能，而ApplicationContext则增加了更多的企业特定功能。ApplicationContext是BeanFactory的一个完整的超集

### 1.2 容器概览

- ApplicationContext接口代表了IOC容器，是它对对象进行的实例化、配置和整合
- 配制方法是通过读取一些元数据，这些元数据可以由xml、注解和Java配置文件进行配置。

