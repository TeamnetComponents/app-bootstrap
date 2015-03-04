package ro.teamnet.bootstrap.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import ro.teamnet.bootstrap.extend.AppPageRequest;
import ro.teamnet.bootstrap.extend.AppRepository;
import ro.teamnet.bootstrap.extend.Filter;
import ro.teamnet.bootstrap.extend.Filters;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;


public abstract class AbstractServiceImpl<T extends Serializable, ID extends Serializable> implements AbstractService<T, ID> {


    private final Logger log = LoggerFactory.getLogger(AbstractServiceImpl.class);

    private final AppRepository<T, ID> repository;

    public AbstractServiceImpl(AppRepository<T, ID> repository) {
        this.repository = repository;
    }

    /**
     * Retrieves the specific repository, for implementations of this interface.
     *
     * @return The underlying repository.
     */
    protected AppRepository<T, ID> getRepository() {
        return this.repository;
    }

    @Override
    @Transactional
    public T save(T t) {
        log.debug("REST request to save : {}", t);
        return repository.save(t);
    }


    @Override
    public List<T> findAll() {
        log.debug("REST request to get all records");
        return repository.findAll();
    }


    @Override
    public T findOne(ID id) {
        log.debug("REST request to get : {}", id);
        return (T) repository.findOne(id);
    }


    @Override
    @Transactional
    public void delete(ID id) {
        log.debug("REST request to delete : {}", id);
        repository.delete(id);
    }

    protected final Sort constructSort(final String sortBy, final String sortOrder) {
        Sort sortInfo = null;
        if (sortBy != null) {
            sortInfo = new Sort(Sort.Direction.fromString(sortOrder), sortBy);
        }
        return sortInfo;
    }

    @Override
    public Page<T> findAllPaginatedAndSortedRawWithFilter(int page, int size, String sortBy, String sortOrder, String filterObject) {
        final Sort sort = constructSort(sortBy, sortOrder);
        ObjectMapper objectMapper = new ObjectMapper();
        Filters filters = new Filters();
        if (filterObject != null) {
            try {
                filterObject = "[" + filterObject + "]";
                filters.addFilters(
                    Collection.class.<Filter>cast(
                        objectMapper.readValue(filterObject, objectMapper.getTypeFactory().constructCollectionType(List.class, Filter.class))));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        AppPageRequest appPageRequest = new AppPageRequest(page, size, sort, filters);
        return repository.findAll(appPageRequest);
    }


}
