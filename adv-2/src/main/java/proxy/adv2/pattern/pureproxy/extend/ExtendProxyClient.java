package proxy.adv2.pattern.pureproxy.extend;

public class ExtendProxyClient {

    private ExtendLogic extendLogic;

    public ExtendProxyClient(ExtendLogic extendLogic) {
        this.extendLogic = extendLogic;
    }

    public void execute() {
        extendLogic.operation();
    }

}