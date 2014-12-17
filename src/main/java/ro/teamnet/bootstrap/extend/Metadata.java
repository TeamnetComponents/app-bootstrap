package ro.teamnet.bootstrap.extend;


import java.io.Serializable;

/**
 * Metadata reprezinta o serie de informatii a unui camp
 */
public final class Metadata implements Serializable {

    /**
     * Numele campului
     */
    private String field;

    /**
     * Numele campului afisat in interfata
     */
    private String displayName;

    /**
     * Tipul filtrului asociat campului
     */
    private String fieldFilterType;

    /**
     * Tipul campului
     */
    private String fieldType;

    /**
     * Proprietate ce indica daca este un camp ce poate fi redimensionat
     */
    private Boolean resizable = Boolean.TRUE;

    /**
     * Constructor al {@link ro.teamnet.bootstrap.extend.Metadata}
     * @param field campul caruia se ataseaza metadatele
     */
    public Metadata(String field) {
        this.field = field;
    }


    /**
     * Constructor al {@link ro.teamnet.bootstrap.extend.Metadata}
     */
    public Metadata() {
    }

    /**
     * Constructor al {@link ro.teamnet.bootstrap.extend.Metadata}
     * @param field campul caruia se ataseaza metadatele
     * @param fieldType tipul campului
     */
    public Metadata(String field, String fieldType) {
        this.field = field;

        this.fieldType = fieldType;
    }

    /**
     * Constructor al {@link ro.teamnet.bootstrap.extend.Metadata}
     * @param field field campul caruia se ataseaza metadatele
     * @param displayName numele campului afisat in interfata
     * @param fieldFilterType tipul filtrului asociat campului
     */
    public Metadata(String field, String displayName, String fieldFilterType) {
        this.field = field;
        this.displayName = displayName;
        this.fieldFilterType = fieldFilterType;
    }




    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Boolean getResizable() {
        return resizable;
    }

    public void setResizable(Boolean resizable) {
        this.resizable = resizable;
    }

    public String getFieldFilterType() {
        return fieldFilterType;
    }

    public void setFieldFilterType(String fieldFilterType) {
        this.fieldFilterType = fieldFilterType;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }
}
