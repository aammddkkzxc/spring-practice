package proxy.adv2.app.pureproxy.implemetproxy;

import lombok.RequiredArgsConstructor;
import proxy.adv2.app.v1.OrderServiceV1;
import proxy.adv2.trace.TraceStatus;
import proxy.adv2.trace.logtrace.LogTrace;

@RequiredArgsConstructor
public class OrderServiceInterfaceProxy implements OrderServiceV1 {

    private final OrderServiceV1 target;
    private final LogTrace logTrace;

    @Override
    public void orderItem(String itemId) {
        TraceStatus status = null;
        try {
            status = logTrace.begin("OrderService.orderItem()");
            //target 호출
            target.orderItem(itemId);
            logTrace.end(status);
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }
}