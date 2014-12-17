package ro.teamnet.bootstrap.extend;

import java.io.Serializable;
import java.util.List;

/**
 * Contine informatii despre proprietatea si valorile dupa care se va produce filtrarea asupra listei de rezultate.
 */
public final class Filter implements Serializable {

    /**
     * Numele proprietatii dupa care se efectueaza filtrarea
     */
    private String property;
    /**
     * Valoarea dupa care se filtreaza
     */
    private  String value;
    /**
     * Valorile dupa care se filtreaza
     */
    private  List<String> values;
    /**
     * Tipul filtrului
     */
    private FilterType type;
    /**
     * Prefixul numelui proprietatii
     */
    String prefix;


    /**
     * Tipurile de filtre disponibile.
     */
    public static enum FilterType {
        EQUAL("="),
        LIKE("like"),
        IN("in")
        ;
        private String opp;

        FilterType(String opp) {
            this.opp = opp;
        }

        public String getOpp() {
            return opp;
        }

        public static FilterType getBy(String type) {
            if (type == null) {
                return EQUAL;
            }
            for (FilterType filterType : values()) {
                if (filterType.name().toLowerCase().equals(type)) {
                    return filterType;
                }
            }

            return EQUAL;
        }
    }

    /**
     * Constructor al {@link ro.mediu.tn.bo.fluxuri.extend.Filter}
     * @param property numele proprietatii
     * @param value valoarea dupa care se filtreaza proprietatea respectiva
     */
    public Filter(String property, String value) {
        this.property = property;
        this.value = value;
    }

    /**
     * Constructor al {@link ro.mediu.tn.bo.fluxuri.extend.Filter}
     * @param property numele proprietatii
     * @param value valoarea dupa care se filtreaza proprietatea respectiva
     * @param type tipul filtrului
     */
    public Filter(String property, String value, FilterType type) {
        this.property = property;
        this.value = value;
        this.type = type;
    }

    /**
     * Constructor al {@link ro.mediu.tn.bo.fluxuri.extend.Filter}
     * @param property numele proprietatii
     * @param values valorile dupa care se filtreaza proprietatea respectiva
     * @param type tipul filtrului
     */
    public Filter(String property, List<String> values, FilterType type) {
        this.property = property;
        this.values = values;
        this.type = type;
    }

    /**
     * Constructor al {@link ro.mediu.tn.bo.fluxuri.extend.Filter}
     * @param property numele proprietatii
     * @param values valorile dupa care se filtreaza proprietatea respectiva
     */
    public Filter(String property, List<String> values) {
        this.property = property;
        this.values = values;
    }

    /**
     * Constructor al {@link ro.mediu.tn.bo.fluxuri.extend.Filter}
     * @param property numele proprietatii
     * @param value valoarea dupa care se filtreaza proprietatea respectiva
     * @param type tipul filtrului
     * @param prefix prefix al numelui proprietatii
     */
    public Filter(String property, String value, FilterType type, String prefix) {
        this.property = property;
        this.value = value;
        this.type = type;
        this.prefix = prefix;
    }

    /**
     * Constructor al {@link ro.mediu.tn.bo.fluxuri.extend.Filter}
     * @param property numele proprietatii
     * @param values valorile dupa care se filtreaza proprietatea respectiva
     * @param type tipul filtrului
     * @param prefix prefix al numelui proprietatii
     */
    public Filter(String property, List<String> values, FilterType type, String prefix) {
        this.property = property;
        this.values = values;
        this.type = type;
        this.prefix = prefix;
    }

    public List<String> getValues() {
        return values;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public FilterType getType() {
        return type;
    }

    public void setType(FilterType type) {
        this.type = type;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public Filter() {
        throw new RuntimeException("Possible violation code!!!");
    }

    public String getProperty() {
        return property;
    }

    public String getValue() {
        return value;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Filter filter = (Filter) o;

        if (prefix != null ? !prefix.equals(filter.prefix) : filter.prefix != null) return false;
        if (!property.equals(filter.property)) return false;
        if (type != filter.type) return false;
        if (value != null ? !value.equals(filter.value) : filter.value != null) return false;
        if (values != null ? !values.equals(filter.values) : filter.values != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = property.hashCode();
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (values != null ? values.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (prefix != null ? prefix.hashCode() : 0);
        return result;
    }
}
