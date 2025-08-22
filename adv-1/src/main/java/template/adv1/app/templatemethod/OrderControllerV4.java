package template.adv1.app.templatemethod;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import template.adv1.trace.templatemethod.AbstractTemplate;
import template.adv1.trace.threadlocal.LogTrace;

@RestController
@RequiredArgsConstructor
public class OrderControllerV4 {
    private final OrderServiceV4 orderService;
    private final LogTrace trace;

    @GetMapping("/v4/request")
    public String request(String itemId) {
        AbstractTemplate<String> template = new AbstractTemplate<>(trace) {
            @Override
            protected String call() {
                orderService.orderItem(itemId);
                return "ok";
            }
        };

        return template.execute("OrderController.request()");
    }
}
