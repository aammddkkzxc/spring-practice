package proxy.adv2.config.jdkdynamic;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import proxy.adv2.app.v1.*;
import proxy.adv2.pattern.jdkdynamic.LogTraceInvocationHandler;
import proxy.adv2.trace.logtrace.LogTrace;

import java.lang.reflect.Proxy;

@Configuration
public class DynamicProxyConfig {
    private static final String[] PATTERNS = {"request*", "order*", "save*"};

    @Bean
    public OrderControllerV1 orderControllerV1(LogTrace logTrace) {
        OrderControllerV1 orderController = new OrderControllerV1Impl(orderServiceV1(logTrace));
        OrderControllerV1 proxy = (OrderControllerV1)
                Proxy.newProxyInstance(OrderControllerV1.class.getClassLoader(),
                        new Class[]{OrderControllerV1.class},
                        new LogTraceInvocationHandler(orderController, logTrace, PATTERNS)
                );
        return proxy;
    }

    @Bean
    public OrderServiceV1 orderServiceV1(LogTrace logTrace) {
        OrderServiceV1 orderService = new
                OrderServiceV1Impl(orderRepositoryV1(logTrace));
        OrderServiceV1 proxy = (OrderServiceV1)
                Proxy.newProxyInstance(OrderServiceV1.class.getClassLoader(),
                        new Class[]{OrderServiceV1.class},
                        new LogTraceInvocationHandler(orderService, logTrace, PATTERNS)
                );
        return proxy;
    }

    @Bean
    public OrderRepositoryV1 orderRepositoryV1(LogTrace logTrace) {
        OrderRepositoryV1 orderRepository = new OrderRepositoryV1Impl();
        OrderRepositoryV1 proxy = (OrderRepositoryV1)
                Proxy.newProxyInstance(OrderRepositoryV1.class.getClassLoader(),
                        new Class[]{OrderRepositoryV1.class},
                        new LogTraceInvocationHandler(orderRepository, logTrace, PATTERNS)
                );
        return proxy;
    }
}