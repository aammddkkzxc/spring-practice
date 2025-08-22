package proxy.adv2.app.pureproxy.extendproxy;

import proxy.adv2.app.v2.OrderControllerV2;
import proxy.adv2.trace.TraceStatus;
import proxy.adv2.trace.logtrace.LogTrace;

public class OrderControllerExtendProxy extends OrderControllerV2 {
    private final OrderControllerV2 target;
    private final LogTrace logTrace;

    public OrderControllerExtendProxy(OrderControllerV2 target, LogTrace
            logTrace) {
        super(null);
        this.target = target;
        this.logTrace = logTrace;
    }

    @Override
    public String request(String itemId) {
        TraceStatus status = null;
        try {
            status = logTrace.begin("OrderController.request()");
            //target 호출
            String result = target.request(itemId);
            logTrace.end(status);
            return result;
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }

    @Override
    public String noLog() {
        return target.noLog();
    }
}