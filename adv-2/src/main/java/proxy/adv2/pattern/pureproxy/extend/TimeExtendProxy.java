package proxy.adv2.pattern.pureproxy.extend;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TimeExtendProxy extends ExtendLogic {

    private final ExtendLogic realLogic;

    public TimeExtendProxy(ExtendLogic realLogic) {
        this.realLogic = realLogic;
    }

    @Override
    public String operation() {
        log.info("TimeDecorator 실행");
        long startTime = System.currentTimeMillis();
        String result = realLogic.operation();
        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("TimeDecorator 종료 resultTime={}", resultTime);
        return result;
    }
}
