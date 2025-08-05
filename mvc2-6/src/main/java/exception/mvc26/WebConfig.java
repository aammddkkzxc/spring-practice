package exception.mvc26;

import exception.mvc26.resolver.MyHandlerExceptionResolver;
import exception.mvc26.resolver.UserHandlerExceptionResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
        exceptionResolvers.add(new MyHandlerExceptionResolver());
        exceptionResolvers.add(new UserHandlerExceptionResolver());
    }

}
