package template.adv1.app.callback;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import template.adv1.trace.callback.CallbackTraceTemplate;
import template.adv1.trace.threadlocal.LogTrace;

@RestController
public class OrderControllerV5 {
    private final OrderServiceV5 orderService;
    private final CallbackTraceTemplate template;

    public OrderControllerV5(OrderServiceV5 orderService, LogTrace trace) {
        this.orderService = orderService;
        this.template = new CallbackTraceTemplate(trace);
    }

    @GetMapping("/v5/request")
    public String request(String itemId) {
        return template.execute("OrderController.request()",
                () -> {
                    orderService.orderItem(itemId);
                    return "ok";
                });
    }
}
