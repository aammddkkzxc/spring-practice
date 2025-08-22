package template.adv1;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import template.adv1.trace.threadlocal.LogTrace;
import template.adv1.trace.threadlocal.ThreadLocalLogTrace;

@Component
public class LogTraceConfig {

    @Bean
    public LogTrace logTrace() {
//        return new FieldLogTrace();
        return new ThreadLocalLogTrace();
    }

}
