package proxy.adv2.pattern.pureproxy.extend;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExtendLogic {
    public String operation() {
        log.info("ExtendLogic 실행");
        return "data";
    }
}