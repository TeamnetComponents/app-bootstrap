package ro.teamnet.bootstrap.service;

import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;


public interface AbstractService<T extends Serializable> {

    public T save(T t);
    public List<T> findAll();
    public T findOne(Long id);
    public void delete(Long id);
    public Page<T> findAllPaginatedAndSortedRawWithFilter(final int page, final int size, final String sortBy,
                                                          final String sortOrder, final String filterObject);

}
