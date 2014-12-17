package ro.teamnet.bootstrap.extend;

import flexjson.JSONDeserializer;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Map;


public class AppLocaleHandlerMethodArgumentResolver  implements HandlerMethodArgumentResolver {

    private static final String DEFAULT_PARAMETER = "locale";

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return false;
    }

    private String getLocaleParameter() {
        return DEFAULT_PARAMETER;
    }

    public String resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

        String filterStrs=webRequest.getParameter(getLocaleParameter());
        if(filterStrs==null)
            return null;
        Map<String,String> stringMap= new JSONDeserializer<Map<String,String>>().deserialize(filterStrs,Map.class);
       return  stringMap.get(getLocaleParameter());
    }

}
