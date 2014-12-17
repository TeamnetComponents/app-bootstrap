package ro.teamnet.bootstrap.extend;

import flexjson.JSONDeserializer;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortHandlerMethodArgumentResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Clasa ce transpune parametrii de pe cerere intr-un obiect de tip {@link org.springframework.data.domain.Sort}
 * Extinde {@link org.springframework.data.web.SortHandlerMethodArgumentResolver}
 */
public class AppSortHandlerMethodArgumentResolver extends SortHandlerMethodArgumentResolver {

    public Sort resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        List<Sort.Order> orderList=new ArrayList<>();
        String sortStr=webRequest.getParameter(getSortParameter(parameter));
        if(sortStr==null)
            return null;
        Map<String,String>stringMap= new JSONDeserializer<Map<String,String>>().deserialize(sortStr,Map.class);
        for (String s : stringMap.keySet()) {
            orderList.add(new Sort.Order(stringMap.get(s).equalsIgnoreCase("ASC")? Sort.Direction.ASC : Sort.Direction.DESC, s));
        }
        return orderList.isEmpty()?null:new Sort(orderList);
    }

}
