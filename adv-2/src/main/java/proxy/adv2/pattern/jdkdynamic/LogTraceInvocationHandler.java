package proxy.adv2.pattern.jdkdynamic;

import org.springframework.util.PatternMatchUtils;
import proxy.adv2.trace.TraceStatus;
import proxy.adv2.trace.logtrace.LogTrace;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class LogTraceInvocationHandler implements InvocationHandler {

    private final Object target;
    private final LogTrace logTrace;
    private final String[] patterns;

    public LogTraceInvocationHandler(Object target, LogTrace logTrace, String[] patterns) {
        this.target = target;
        this.logTrace = logTrace;
        this.patterns = patterns;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws
            Throwable {

        String methodName = method.getName();
        if (!PatternMatchUtils.simpleMatch(patterns, methodName)) {
            return method.invoke(target, args);
        }

        TraceStatus status = null;
        try {
            String message = method.getDeclaringClass().getSimpleName() + "." + method.getName() + "()";
            status = logTrace.begin(message);
            //로직 호출
            Object result = method.invoke(target, args);
            logTrace.end(status);
            return result; } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }
}
