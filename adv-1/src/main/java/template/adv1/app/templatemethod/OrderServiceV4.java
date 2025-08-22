package template.adv1.app.templatemethod;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import template.adv1.trace.templatemethod.AbstractTemplate;
import template.adv1.trace.threadlocal.LogTrace;

@Service
@RequiredArgsConstructor
public class OrderServiceV4 {
    private final OrderRepositoryV4 orderRepository;
    private final LogTrace trace;

    public void orderItem(String itemId) {
        AbstractTemplate<Void> template = new AbstractTemplate<>(trace) {

            @Override
            protected Void call() {
                orderRepository.save(itemId);
                return null;
            }
        };

        template.execute("OrderService.orderItem()");
    }
}