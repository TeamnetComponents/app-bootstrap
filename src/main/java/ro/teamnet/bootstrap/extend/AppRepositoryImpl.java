package ro.teamnet.bootstrap.extend;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@NoRepositoryBean
public class AppRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements AppRepository<T, ID> {
    @SuppressWarnings("UnusedDeclaration")
    private EntityManager entityManager;

    public AppRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }

    // There are two constructors to choose from, either can be used.
    public AppRepositoryImpl(Class<T> domainClass, EntityManager entityManager) {
        super(domainClass, entityManager);

        // This is the recommended method for accessing inherited class dependencies.
        this.entityManager = entityManager;
    }

    @Override
    public Page<T> findAll(Pageable pag) {
        AppPageable pageable = (AppPageable) pag;
        Page<T> page;
        if (pageable.getFilters() != null && !pageable.getFilters().isEmpty()) {
            Specification<T> specification = createSpecification(pageable);
            page = super.findAll(specification, pageable);
        } else {
            page = super.findAll(pageable);

        }
        if (pageable instanceof PageRequest)
            return page;

        return new PageResource<T>(new AppPageImpl<T>(page != null ? page.getContent() : null,
            pageable, page != null ? page.getTotalElements() : 0,
            pageable.getFilters()));


    }

    @Override
    public AppPage<T> findAll(AppPageable appPageable) {
        List<Filter> filters = appPageable.getFilters() == null
            ? new ArrayList<Filter>()
            : appPageable.getFilters();

        Page<T> page = filters.isEmpty()
            ? super.findAll(appPageable)
            : super.findAll(createSpecification(appPageable), appPageable);

        List<T> content = new ArrayList<>();
        for (T item : page) {
            content.add((T) item);
        }

        return new AppPageImpl<T>(content, appPageable, page.getTotalElements(), filters);
    }

    private Specification<T> createSpecification(final AppPageable pageable) {

        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate ret = null;
                for (Filter filter : pageable.getFilters()) {
                    ret = cb.and(root.get(filter.getProperty()).in(convertStringToObject(root.get(filter.getProperty()).type(), filter)));
                }
                return ret;
            }

            private Collection<?> convertStringToObject(Expression<Class<?>> type, Filter filter) {
                Collection<Object> ret = new ArrayList<>();

                ret.add(filter.getValue());
                return ret;
            }
        };
    }


}
