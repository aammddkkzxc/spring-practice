package proxy.adv2.config.pureproxy.implement;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import proxy.adv2.app.pureproxy.implemetproxy.OrderControllerInterfaceProxy;
import proxy.adv2.app.pureproxy.implemetproxy.OrderRepositoryInterfaceProxy;
import proxy.adv2.app.pureproxy.implemetproxy.OrderServiceInterfaceProxy;
import proxy.adv2.app.v1.*;
import proxy.adv2.trace.logtrace.LogTrace;

@Configuration
public class InterfaceProxyConfig {

    @Bean
    public OrderControllerV1 orderController(LogTrace logTrace) {
        OrderControllerV1Impl controllerImpl = new OrderControllerV1Impl(orderService(logTrace));
        return new OrderControllerInterfaceProxy(controllerImpl, logTrace);
    }
    @Bean
    public OrderServiceV1 orderService(LogTrace logTrace) {
        OrderServiceV1Impl serviceImpl = new OrderServiceV1Impl(orderRepository(logTrace));
        return new OrderServiceInterfaceProxy(serviceImpl, logTrace);
    }

    @Bean
    public OrderRepositoryV1 orderRepository(LogTrace trace) {
        OrderRepositoryV1 orderRepository = new OrderRepositoryV1Impl();
        return new OrderRepositoryInterfaceProxy(orderRepository, trace);
    }

}
