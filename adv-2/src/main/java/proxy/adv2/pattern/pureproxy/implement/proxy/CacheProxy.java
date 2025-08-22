package proxy.adv2.pattern.pureproxy.implement.proxy;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CacheProxy implements Subject{

    Subject subject;
    String cacheValue;

    public CacheProxy(Subject subject) {
        this.subject = subject;
    }

    @Override
    public String operation() {
        log.info("프록시 호출");
        if (cacheValue == null) {
            cacheValue = subject.operation();
        }
        return cacheValue;
    }
}
