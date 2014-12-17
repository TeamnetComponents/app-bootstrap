package ro.teamnet.bootstrap.extend;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Iterator;
import java.util.List;

/**
 * Implementare de baza a {@link ro.teamnet.bootstrap.extend.AppPage}
 * @param <T>
 */
public class PageResource<T> implements AppPage<T>{

    /**
     * Numele parametrului ce indica numarul paginii
     */
    public static final String PAGE_PARAM="page";

    /**
     * Numele parametrului ce indica numarul de elemente de pe o pagina
     */
    public static final String SIZE_PARAM="size";

    /**
     * O pagina ce implementeaza {@link ro.teamnet.bootstrap.extend.AppPage}
     */
    private final AppPage<T> page;

    /**
     * Constructor al {@link ro.teamnet.bootstrap.extend.PageResource}
     * @param page o pagina de tip {@link ro.teamnet.bootstrap.extend.AppPage}
     */
    public PageResource(AppPage<T> page) {
        super();
        this.page = page;
    }

    /**
     * Creaza un obiect {@link org.springframework.web.servlet.support.ServletUriComponentsBuilder}
     * @return un obiect {@link org.springframework.web.servlet.support.ServletUriComponentsBuilder}
     */
    private ServletUriComponentsBuilder createBuilder() {
        return ServletUriComponentsBuilder.fromCurrentRequestUri();
    }


    @Override
    public List<Filter> getFilters() {
        return page.getFilters();
    }

    @Override
    public AppPageable getPageable() {
        return null;
    }

    @Override
    public List<Metadata> getMetadata() {
        return page.getMetadata();
    }

    @Override
    public int getNumber() {
        return page.getNumber();
    }

    @Override
    public int getSize() {
        return page.getSize();
    }

    @Override
    public int getTotalPages() {
        return page.getTotalPages();
    }

    @Override
    public int getNumberOfElements() {
        return page.getNumberOfElements();
    }

    @Override
    public long getTotalElements() {
        return page.getTotalElements();
    }

    @Override
    public Pageable nextPageable() {
        return page.nextPageable();
    }

    @Override
    public Pageable previousPageable() {
        return page.previousPageable();
    }

    @Override
    public Iterator<T> iterator() {
        return page.iterator();
    }

    @Override
    public List<T> getContent() {
        return page.getContent();
    }

    @Override
    public boolean hasContent() {
        return page.hasContent();
    }

    @Override
    public Sort getSort() {
        return page.getSort();
    }

    @Override
    public boolean isFirst() {
        return page.isFirst();
    }

    @Override
    public boolean isLast() {
        return page.isLast();
    }

    @Override
    public boolean hasNext() {
        return page.hasNext();
    }

    @Override
    public boolean hasPrevious() {
        return page.hasPrevious();
    }
}
