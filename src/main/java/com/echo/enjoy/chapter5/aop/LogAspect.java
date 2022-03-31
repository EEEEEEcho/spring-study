package com.echo.enjoy.chapter5.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;

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
    public void logStart(JoinPoint joinPoint){
        System.out.println();
        System.out.println("**************前置通知开始************");
        System.out.println("业务逻辑方法"+joinPoint.getSignature().getName()+
                " 开始运行之前传入的参数是:" + Arrays.asList(joinPoint.getArgs()));
        System.out.println("**************前置通知结束************");
        System.out.println();
    }

//    @After("execution (public int com.echo.enjoy.chapter5.aop.Calculator.div(int,int))")
    @After("pointCut()")
    public void logEnd(JoinPoint joinPoint){

        System.out.println();
        System.out.println("**************后置通知开始************");
        System.out.println("业务逻辑方法"+joinPoint.getSignature().getName()+"运行结束...");
        System.out.println("**************后置通知结束************");
        System.out.println();

    }

//    @AfterReturning("execution (public int com.echo.enjoy.chapter5.aop.Calculator.div(int,int))")
    @AfterReturning(value = "pointCut()",returning = "value")
    public void logReturning(JoinPoint joinPoint,int value){
        System.out.println();
        System.out.println("**************返回通知开始************");
        System.out.println("业务逻辑方法"+joinPoint.getSignature().getName()+"的返回值是：" + value);
        System.out.println("**************返回通知结束************");
        System.out.println();

    }

//    @AfterThrowing("execution (public int com.echo.enjoy.chapter5.aop.Calculator.div(int,int))")
    @AfterThrowing(value = "pointCut()",throwing = "exception")
    public void logException(JoinPoint joinPoint,Exception exception){
        System.out.println();
        System.out.println("**************异常通知开始************");
        System.out.println("业务逻辑方法"+joinPoint.getSignature().getName() + "抛出的异常是" + exception);
        System.out.println("**************异常通知结束************");
        System.out.println();
    }

//    @Around("execution (public int com.echo.enjoy.chapter5.aop.Calculator.div(int,int))")
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
