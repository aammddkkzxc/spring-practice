package proxy.adv2.pattern.postprocessor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanPostProcessor {

    @Bean(name = "beanA")
    public A a() {
        return new A();
    }

    @Bean
    public AToBPostProcessor helloPostProcessor() {
        return new AToBPostProcessor();
    }

    @Slf4j
    static class A {
        public void helloA() {
            log.info("hello A");
        }
    }

    @Slf4j
    static class B {
        public void helloB() {
            log.info("hello B");
        }
    }

    @Slf4j
    static class AToBPostProcessor implements org.springframework.beans.factory.config.BeanPostProcessor {
        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
            log.info("beanName={} bean={}", beanName, bean);

            if (bean instanceof A) {
                return new B();
            }

            return bean;
        }
    }

}


