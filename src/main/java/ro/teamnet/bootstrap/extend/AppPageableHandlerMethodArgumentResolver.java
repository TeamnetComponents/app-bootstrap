package ro.teamnet.bootstrap.extend;

import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.lang.annotation.Annotation;
import java.util.List;


public class AppPageableHandlerMethodArgumentResolver extends PageableHandlerMethodArgumentResolver {


    private static final Pageable DEFAULT_PAGE_REQUEST = new AppPageRequest(0, 20);
    private static final String DEFAULT_PAGE_PARAMETER = "page";
    private static final String DEFAULT_SIZE_PARAMETER = "size";
    private static final String DEFAULT_PREFIX = "";
    private static final String DEFAULT_QUALIFIER_DELIMITER = "_";
    private static final int DEFAULT_MAX_PAGE_SIZE = 200000000;

    private Pageable fallbackPageable = DEFAULT_PAGE_REQUEST;

    private String pageParameterName = DEFAULT_PAGE_PARAMETER;
    private String sizeParameterName = DEFAULT_SIZE_PARAMETER;
    private String prefix = DEFAULT_PREFIX;
    private String qualifierDelimiter = DEFAULT_QUALIFIER_DELIMITER;
    private int maxPageSize = DEFAULT_MAX_PAGE_SIZE;
    private boolean oneIndexedParameters = false;


    private final AppSortHandlerMethodArgumentResolver sortResolver;
    private final AppFilterHandlerMethodArgumentResolver filtersResolver;
    private final AppLocaleHandlerMethodArgumentResolver localeResolver;

    public AppPageableHandlerMethodArgumentResolver() {
        this(null,null,null);
    }

    public AppPageableHandlerMethodArgumentResolver(AppSortHandlerMethodArgumentResolver sortResolver,
                                                    AppFilterHandlerMethodArgumentResolver filtersResolver,
                                                    AppLocaleHandlerMethodArgumentResolver localeResolver) {
        super(sortResolver);
        this.sortResolver = sortResolver;
        this.filtersResolver=filtersResolver;
        this.localeResolver=localeResolver;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return super.supportsParameter(parameter)|| AppPageable.class.equals(parameter.getParameterType());
    }

    @Override
    public Pageable resolveArgument(MethodParameter methodParameter, ModelAndViewContainer mavContainer,
                                    NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {



        Pageable defaultOrFallback = getDefaultFromAnnotationOrFallback(methodParameter);

        String pageString = webRequest.getParameter(getParameterNameToUse(pageParameterName, methodParameter));
        String pageSizeString = webRequest.getParameter(getParameterNameToUse(sizeParameterName, methodParameter));

        int page = StringUtils.hasText(pageString) ? Integer.parseInt(pageString) - (oneIndexedParameters ? 1 : 0)
                : defaultOrFallback.getPageNumber();
        int pageSize = StringUtils.hasText(pageSizeString) ? Integer.parseInt(pageSizeString) : defaultOrFallback
                .getPageSize();
        pageSize = pageSize > maxPageSize ? maxPageSize : pageSize;

        Sort sort = sortResolver.resolveArgument(methodParameter, mavContainer, webRequest, binderFactory);
        List<Filter> filters = filtersResolver.resolveArgument(methodParameter, mavContainer, webRequest, binderFactory);
        String locale=localeResolver.resolveArgument(methodParameter, mavContainer, webRequest, binderFactory);
        return new AppPageRequest(page, pageSize, sort == null ? defaultOrFallback.getSort() : sort, filters,locale);
    }

    private Pageable getDefaultFromAnnotationOrFallback(MethodParameter methodParameter) {

        if (methodParameter.hasParameterAnnotation(PageableDefault.class)) {
            @SuppressWarnings("deprecation") Pageable pageable = getDefaultPageRequestFrom(methodParameter
                    .getParameterAnnotation(PageableDefault.class));
            return new AppPageRequest(pageable.getPageNumber() - 1, pageable.getPageSize(), pageable.getSort());
        }

        if (methodParameter.hasParameterAnnotation(PageableDefault.class)) {
            return getDefaultPageRequestFrom(methodParameter.getParameterAnnotation(PageableDefault.class));
        }

        return fallbackPageable;
    }



    public static <T> T getSpecificPropertyOrDefaultFromValue(Annotation annotation, String property) {

        Object propertyDefaultValue = AnnotationUtils.getDefaultValue(annotation, property);
        Object propertyValue = AnnotationUtils.getValue(annotation, property);

        //noinspection unchecked
        return (T) (ObjectUtils.nullSafeEquals(propertyDefaultValue, propertyValue) ? AnnotationUtils.getValue(annotation)
                : propertyValue);
    }
    private static Pageable getDefaultPageRequestFrom(PageableDefault defaults) {

        Integer defaultPageNumber = defaults.page();
        Integer defaultPageSize = getSpecificPropertyOrDefaultFromValue(defaults, "size");

        if (defaults.sort().length == 0) {
            return new AppPageRequest(defaultPageNumber, defaultPageSize);
        }

        return new AppPageRequest(defaultPageNumber, defaultPageSize, defaults.direction(), defaults.sort());
    }
}
