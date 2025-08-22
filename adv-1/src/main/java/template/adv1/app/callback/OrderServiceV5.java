package template.adv1.app.callback;

import template.adv1.trace.callback.CallbackTraceTemplate;
import template.adv1.trace.threadlocal.LogTrace;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceV5 {
    private final OrderRepositoryV5 orderRepository;
    private final CallbackTraceTemplate template;

    public OrderServiceV5(OrderRepositoryV5 orderRepository, LogTrace trace) {
        this.orderRepository = orderRepository;
        this.template = new CallbackTraceTemplate(trace);
    }

    public void orderItem(String itemId) {
        template.execute("OrderService.orderItem()", () -> {
            orderRepository.save(itemId);
            return null;
        });
    }
}