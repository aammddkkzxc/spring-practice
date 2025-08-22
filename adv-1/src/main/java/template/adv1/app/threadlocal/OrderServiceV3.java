package template.adv1.app.threadlocal;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import template.adv1.trace.TraceStatus;
import template.adv1.trace.threadlocal.LogTrace;

@Service
@RequiredArgsConstructor
public class OrderServiceV3 {
    private final OrderRepositoryV3 orderRepository;
    private final LogTrace trace;

    public void orderItem(String itemId) {
        TraceStatus status = null;

        try {
            status = trace.begin("OrderService.orderItem()");
            orderRepository.save(itemId);
            trace.end(status);

        } catch (Exception e) {
            trace.exception(status, e);
            throw e;
        }
    }
}