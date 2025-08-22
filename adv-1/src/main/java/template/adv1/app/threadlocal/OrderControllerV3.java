package template.adv1.app.threadlocal;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import template.adv1.trace.TraceStatus;
import template.adv1.trace.threadlocal.LogTrace;

@RestController
@RequiredArgsConstructor
public class OrderControllerV3 {
    private final OrderServiceV3 orderService;
    private final LogTrace trace;

    @GetMapping("/v3/request")
    public String request(String itemId) {
        TraceStatus status = null;

        try {
            status = trace.begin("OrderController.request()");

            orderService.orderItem(itemId);
            trace.end(status);
            return "ok";
        } catch (IllegalArgumentException e) {
            trace.exception(status, e);
            throw e;
        }
    }
}
