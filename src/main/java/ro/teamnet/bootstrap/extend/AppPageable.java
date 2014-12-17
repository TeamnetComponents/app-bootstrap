package ro.teamnet.bootstrap.extend;

import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.List;

public interface AppPageable extends Pageable,Serializable {
    public List<Filter> getFilters();
    public String locale();
}
