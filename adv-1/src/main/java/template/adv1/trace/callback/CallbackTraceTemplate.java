package template.adv1.trace.callback;

import template.adv1.trace.TraceStatus;
import template.adv1.trace.threadlocal.LogTrace;

public class CallbackTraceTemplate {

    private final LogTrace trace;

    public CallbackTraceTemplate(LogTrace trace) {
        this.trace = trace;
    }

    public <T> T execute(String message, CallbackTrace<T> callback) {
        TraceStatus status = null;

        try {
            status = trace.begin(message);

            T result = callback.call();
            trace.end(status);
            return result;
        } catch (Exception e) {
            trace.exception(status, e);
            throw e;
        }
    }

}
