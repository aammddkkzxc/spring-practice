package template.adv1.trace.threadlocal;


import template.adv1.trace.TraceStatus;

public interface LogTrace {

    TraceStatus begin(String message);

    void end(TraceStatus status);

    void exception(TraceStatus status, Exception e);

}
