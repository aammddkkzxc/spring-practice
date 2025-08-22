package proxy.adv2.app.pureproxy.extendproxy;


import proxy.adv2.app.v2.OrderRepositoryV2;
import proxy.adv2.trace.TraceStatus;
import proxy.adv2.trace.logtrace.LogTrace;

public class OrderRepositoryExtendProxy extends OrderRepositoryV2 {
    private final OrderRepositoryV2 target;
    private final LogTrace logTrace;

    public OrderRepositoryExtendProxy(OrderRepositoryV2 target, LogTrace
            logTrace) {
        this.target = target;
        this.logTrace = logTrace;
    }

    @Override
    public void save(String itemId) {
        TraceStatus status = null;
        try {
            status = logTrace.begin("OrderRepository.save()");
            //target 호출
            target.save(itemId);
            logTrace.end(status);
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }
}