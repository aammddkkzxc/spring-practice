package template.adv1.app.parameter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import template.adv1.trace.TraceId;
import template.adv1.trace.TraceStatus;
import template.adv1.trace.parameter.ParameterTrace;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryV2 {
    private final ParameterTrace trace;

    public void save(String itemId, TraceId traceId) {

        TraceStatus status = null;

        try {
            status = trace.beginSync(traceId, "OrderRepository.save()");

            //저장 로직
            if (itemId.equals("ex")) {
                throw new IllegalStateException("예외 발생!");
            }
            sleep(1000);

            trace.end(status);
        } catch (Exception e) {
            trace.exception(status, e);
            throw e;
        }
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}