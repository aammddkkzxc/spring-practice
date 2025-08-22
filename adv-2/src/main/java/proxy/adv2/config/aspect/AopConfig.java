package proxy.adv2.config.aspect;

import proxy.adv2.config.raw.AppV1Config;
import proxy.adv2.config.raw.AppV2Config;
import proxy.adv2.pattern.aspect.LogTraceAspect;
import proxy.adv2.trace.logtrace.LogTrace;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({AppV1Config.class, AppV2Config.class})
public class AopConfig {

    @Bean
    public LogTraceAspect logTraceAspect(LogTrace logTrace) {
        return new LogTraceAspect(logTrace);
    }

}
