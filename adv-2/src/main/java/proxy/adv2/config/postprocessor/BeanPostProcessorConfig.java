package proxy.adv2.config.postprocessor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.Advisor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import proxy.adv2.config.raw.AppV1Config;
import proxy.adv2.config.raw.AppV2Config;
import proxy.adv2.pattern.factory.LogTraceAdvice;
import proxy.adv2.pattern.postprocessor.PackageLogTraceProxyPostProcessor;
import proxy.adv2.trace.logtrace.LogTrace;

@Slf4j
@Configuration
@Import({AppV1Config.class, AppV2Config.class})
public class BeanPostProcessorConfig {

    @Bean
    public PackageLogTraceProxyPostProcessor packageLogTracePostProcessor(LogTrace logTrace) {
        return new PackageLogTraceProxyPostProcessor("proxy.adv2.app", getAdvisor(logTrace));
    }

    private Advisor getAdvisor(LogTrace logTrace) {

        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedNames("request*", "order*", "save*");

        LogTraceAdvice advice = new LogTraceAdvice(logTrace);

        return new DefaultPointcutAdvisor(pointcut, advice);
    }

}
