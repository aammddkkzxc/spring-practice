package servlet.mvc11.web.frontcontroller.v3;

import servlet.mvc11.web.frontcontroller.ModelView;

import java.util.Map;

public interface ControllerV3 {

    ModelView process(Map<String, String> paramMap);

}
