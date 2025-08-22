package template.adv1.app.parameter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import template.adv1.trace.TraceId;
import template.adv1.trace.TraceStatus;
import template.adv1.trace.parameter.ParameterTrace;

@Service
@RequiredArgsConstructor
public class OrderServiceV2 {
    private final OrderRepositoryV2 orderRepository;
    private final ParameterTrace trace;

    public void orderItem(String itemId, TraceId traceId) {
        TraceStatus status = null;

        try {
            status = trace.beginSync(traceId, "OrderService.orderItem()");
            orderRepository.save(itemId, status.getTraceId());
            trace.end(status);

        } catch (Exception e) {
            trace.exception(status, e);
            throw e;
        }
    }
}