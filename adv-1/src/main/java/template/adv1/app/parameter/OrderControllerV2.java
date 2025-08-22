package template.adv1.app.parameter;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import template.adv1.trace.TraceStatus;
import template.adv1.trace.parameter.ParameterTrace;

@RestController
@RequiredArgsConstructor
public class OrderControllerV2 {
    private final OrderServiceV2 orderService;
    private final ParameterTrace trace;

    @GetMapping("/v2/request")
    public String request(String itemId) {
        TraceStatus status = null;

        try {
            status = trace.begin("OrderController.request()");

            orderService.orderItem(itemId, status.getTraceId());
            trace.end(status);
            return "ok";
        } catch (IllegalArgumentException e) {
            trace.exception(status, e);
            throw e;
        }
    }
}
