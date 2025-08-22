package proxy.adv2.trace.logtrace;


import proxy.adv2.trace.TraceStatus;

public interface LogTrace {

    TraceStatus begin(String message);
    void end(TraceStatus status);
    void exception(TraceStatus status, Exception e);
}
