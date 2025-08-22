package template.adv1.app.callback;

import template.adv1.trace.callback.CallbackTraceTemplate;
import template.adv1.trace.threadlocal.LogTrace;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepositoryV5 {
    private final CallbackTraceTemplate template;

    public OrderRepositoryV5(LogTrace trace) {
        this.template = new CallbackTraceTemplate(trace);
    }

    public void save(String itemId) {
        template.execute("OrderRepository.save()",
                () -> {
            if (itemId.equals("ex")) {
                throw new IllegalStateException("예외 발생!");
            }
            sleep(1000);
            return null;
        });
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}