package ro.teamnet.bootstrap.extend;

/**
 * Adnotare ce indica daca o clasa va fi serializata ca si obiect JSON
 */
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@java.lang.annotation.Target({java.lang.annotation.ElementType.METHOD})
public @interface ToJson {
    /**
     * Proprietatile ce vor fi excluse de la serializare
     * @return un vector cu proprietatile ce sunt excluse
     */
    String[] excludeFields() default {"*.class"};

    /**
     * Proprietatile ce vor fi incluse la serializare
     * @return un vector cu prorietatile ce sunt incluse
     */
    String[] includeFields() default {};
}
