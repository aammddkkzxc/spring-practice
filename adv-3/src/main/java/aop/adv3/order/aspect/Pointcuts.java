package aop.adv3.order.aspect;

import org.aspectj.lang.annotation.Pointcut;

public class Pointcuts {

    @Pointcut("execution(* aop.adv3.order..*(..))")
    public void allOrder() {
    }

    @Pointcut("execution(* *..*Service.*(..))")
    public void allService() {
    }

    @Pointcut("allOrder() && allService()")
    public void orderAndService() {
    }

}
