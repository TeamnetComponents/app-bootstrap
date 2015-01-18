package ro.teamnet.bootstrap.dictionary.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.joda.deser.LocalDateDeserializer;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;
import ro.teamnet.bootstrap.domain.util.CustomLocalDateSerializer;

import javax.persistence.*;

/**
 * A value associated to a dictionary element. In case of i18n, multiple values may be associated to the same element, one for each language.
 */
@Entity
@Table(name = "T_DICTIONARY_VALUE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DictionaryValue {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "value")
    private String value;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @Column(name = "activation_date", nullable = false)
    private LocalDate activationDate;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @Column(name = "inactivation_date", nullable = false)
    private LocalDate inactivationDate;

    @ManyToOne
    @JoinColumn(name = "dictionary_element_id", nullable = false)
    private DictionaryElement dictionaryElement;

    @ManyToOne
    @JoinColumn(name = "language_id", nullable = true)
    private DictionaryElement language;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public LocalDate getActivationDate() {
        return activationDate;
    }

    public void setActivationDate(LocalDate activationDate) {
        this.activationDate = activationDate;
    }

    public LocalDate getInactivationDate() {
        return inactivationDate;
    }

    public void setInactivationDate(LocalDate inactivationDate) {
        this.inactivationDate = inactivationDate;
    }

    public DictionaryElement getDictionaryElement() {
        return dictionaryElement;
    }

    public void setDictionaryElement(DictionaryElement dictionaryElement) {
        this.dictionaryElement = dictionaryElement;
    }

    public DictionaryElement getLanguage() {
        return language;
    }

    public void setLanguage(DictionaryElement language) {
        this.language = language;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DictionaryValue that = (DictionaryValue) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
