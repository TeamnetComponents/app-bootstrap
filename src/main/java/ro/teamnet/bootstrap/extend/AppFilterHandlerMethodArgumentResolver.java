package ro.teamnet.bootstrap.extend;


import flexjson.JSONDeserializer;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class AppFilterHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String DEFAULT_PARAMETER = "filters";
    public static final String FILTER_TYPE = "_filter_type";
    public static final String FILTER_PREFIX="_filter_prefix";

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return false;
    }

        private String getFilterParameter() {
        return DEFAULT_PARAMETER;
    }

    public List<Filter> resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        List<Filter> filterList=new ArrayList<>();
        String filterStrs=webRequest.getParameter(getFilterParameter());

        if(filterStrs==null)
            return null;
        Map<String,?> stringMap= new JSONDeserializer<Map<String,?>>().deserialize(filterStrs,Map.class);
        for (String property : stringMap.keySet()) {
            if(property.endsWith(FILTER_TYPE))
                continue;
            if(property.endsWith(FILTER_PREFIX))
                continue;

            Object value=stringMap.get(property);
            String filterType= (String) stringMap.get(property+ FILTER_TYPE);
            String filterPrefix= (String) stringMap.get(property+ FILTER_PREFIX);
            if(value!=null&&value instanceof String){
                filterList.add(new Filter(property, (String) value, Filter.FilterType.getBy(filterType),filterPrefix));
            }else if(value!=null&&value instanceof List&&((List) value).size()>0){
                //noinspection unchecked
                filterList.add(new Filter(property, (List<String>) value, Filter.FilterType.IN,filterPrefix));
            }

        }
        return filterList;
    }


}
