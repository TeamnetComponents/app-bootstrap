package ro.teamnet.bootstrap.extend;

import org.springframework.data.domain.PageImpl;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;


public class AppPageImpl<T> extends PageImpl<T> implements AppPage<T>, Serializable {

    private List<Filter> filters;
    private AppPageable pageable2;
    private List<Metadata> metadata;

    public AppPageImpl(List<T> content, AppPageable pageable, long total, List<Filter> filters) {
        super(content, pageable, total);
        this.pageable2=pageable;
        this.filters = filters;
    }

    public AppPageImpl(List<T> content) {
        super(content);
    }

    public AppPageImpl(List<T> content, AppPageable pageable,
                       long total, List<Filter> filters, List<Metadata> metadata) {
        super(content, pageable, total);
        this.filters = filters;
        this.pageable2 = pageable;
        this.metadata = metadata;
    }

    @Override
    public List<Filter> getFilters() {
        return filters;
    }

    @Override
    public AppPageable getPageable() {
        return pageable2;
    }

    @Override
    public List<Metadata> getMetadata() {
        return metadata;
    }


    @SuppressWarnings("unchecked")
    @Override
    public List<T> getContent() {
        try {
            Field field=PageImpl.class.getDeclaredField("content");field.setAccessible(true);
            return (List<T>) field.get(this);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return super.getContent();
        }

    }
}
