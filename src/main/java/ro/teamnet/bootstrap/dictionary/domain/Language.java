package ro.teamnet.bootstrap.dictionary.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * System-supported languages. Internationalized dictionary values may be defined for each of these languages.
 */
@Entity
@DiscriminatorValue(value = "D_LANGUAGES")
public class Language extends DictionaryElement {
}
