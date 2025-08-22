package proxy.adv2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import proxy.adv2.config.aspect.AopConfig;
import proxy.adv2.config.auto.AutoProxyConfig;
import proxy.adv2.config.factory.ProxyFactoryConfigV1;
import proxy.adv2.config.postprocessor.BeanPostProcessorConfig;
import proxy.adv2.trace.logtrace.LogTrace;
import proxy.adv2.trace.logtrace.ThreadLocalLogTrace;

//@Import({AppV1Config.class, AppV2Config.class})
//@Import(InterfaceProxyConfig.class)
//@Import(ConcreteProxyConfig.class)
//@Import(DynamicProxyConfig.class)
//@Import(ProxyFactoryConfigV1.class)
//@Import(BeanPostProcessorConfig.class)
//@Import(AutoProxyConfig.class)
@Import(AopConfig.class)
@SpringBootApplication(scanBasePackages = "proxy.adv2.app.v3")
public class Adv2Application {

    public static void main(String[] args) {
        SpringApplication.run(Adv2Application.class, args);
    }

    @Bean
    public LogTrace logTrace() {
        return new ThreadLocalLogTrace();
    }

}
