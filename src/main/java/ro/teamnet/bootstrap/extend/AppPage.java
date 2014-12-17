package ro.teamnet.bootstrap.extend;

import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Extindere a tipului {@link org.springframework.data.domain.Page} pentru a decora pagina cu
 * lista de filtre, lista de metadate si {@link ro.teamnet.bootstrap.extend.AppPageable}
 * @param <T>
 */
public interface AppPage<T> extends Page<T> {

    /**
     * Metoda ce returneaza filtrele
     * @return filtrele
     */
    public List<Filter> getFilters();

    /**
     * Metoda ce returneaza un obiect de tip {@link ro.teamnet.bootstrap.extend.AppPageable}
     * aferent cererii
     * @return obiect de tip{@link ro.teamnet.bootstrap.extend.AppPageable} aferent cererii
     */
    public AppPageable getPageable();

    /**
     * Metoda ce returneaza metadatele{@link ro.teamnet.bootstrap.extend.Metadata}
     * @return metadatele
     */
    public List<Metadata> getMetadata();

}

