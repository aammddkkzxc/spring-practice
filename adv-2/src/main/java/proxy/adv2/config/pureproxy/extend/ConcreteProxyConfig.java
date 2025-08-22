package proxy.adv2.config.pureproxy.extend;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import proxy.adv2.app.pureproxy.extendproxy.OrderControllerExtendProxy;
import proxy.adv2.app.pureproxy.extendproxy.OrderRepositoryExtendProxy;
import proxy.adv2.app.pureproxy.extendproxy.OrderServiceExtendProxy;
import proxy.adv2.app.v2.OrderControllerV2;
import proxy.adv2.app.v2.OrderRepositoryV2;
import proxy.adv2.app.v2.OrderServiceV2;
import proxy.adv2.trace.logtrace.LogTrace;

@Configuration
public class ConcreteProxyConfig {
    @Bean
    public OrderControllerV2 orderControllerV2(LogTrace logTrace) {
        OrderControllerV2 controllerImpl = new OrderControllerV2(orderServiceV2(logTrace));
        return new OrderControllerExtendProxy(controllerImpl, logTrace);
    }

    @Bean
    public OrderServiceV2 orderServiceV2(LogTrace logTrace) {
        OrderServiceV2 serviceImpl = new OrderServiceV2(orderRepositoryV2(logTrace));
        return new OrderServiceExtendProxy(serviceImpl, logTrace);
    }

    @Bean
    public OrderRepositoryV2 orderRepositoryV2(LogTrace logTrace) {
        OrderRepositoryV2 repositoryImpl = new OrderRepositoryV2();
        return new OrderRepositoryExtendProxy(repositoryImpl, logTrace);
    }
}