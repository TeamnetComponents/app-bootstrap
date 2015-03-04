package ro.teamnet.bootstrap.service;

import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;


public interface AbstractService<T extends Serializable, ID extends Serializable> {

    T save(T t);
    List<T> findAll();
    T findOne(ID id);
    void delete(ID id);
    Page<T> findAllPaginatedAndSortedRawWithFilter(final int page, final int size, final String sortBy,
                                                          final String sortOrder, final String filterObject);
}
