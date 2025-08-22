package proxy.adv2.app.pureproxy.extendproxy;


import proxy.adv2.app.v2.OrderServiceV2;
import proxy.adv2.trace.TraceStatus;
import proxy.adv2.trace.logtrace.LogTrace;

public class OrderServiceExtendProxy extends OrderServiceV2 {
    private final OrderServiceV2 target;
    private final LogTrace logTrace;

    public OrderServiceExtendProxy(OrderServiceV2 target, LogTrace logTrace) {
        super(null);
        this.target = target;
        this.logTrace = logTrace;
    }

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