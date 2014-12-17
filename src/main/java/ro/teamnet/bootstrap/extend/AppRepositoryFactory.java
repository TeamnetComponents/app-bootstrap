package ro.teamnet.bootstrap.extend;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.core.GenericTypeResolver;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.QueryDslJpaRepository;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.NamedQueries;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.*;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.repository.query.RepositoryQuery;
import org.springframework.data.repository.util.ClassUtils;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.springframework.util.ReflectionUtils.makeAccessible;


public class AppRepositoryFactory<T, I extends Serializable> extends JpaRepositoryFactory {
    private EntityManager entityManager;

    private List<RepositoryProxyPostProcessor> postProcessors;
    private ClassLoader classLoader1 = org.springframework.util.ClassUtils.getDefaultClassLoader();
    private QueryLookupStrategy.Key queryLookupStrategyKey;
    private NamedQueries namedQueries = PropertiesBasedNamedQueries.EMPTY;
    private List<QueryCreationListener<?>> queryPostProcessors = new ArrayList<QueryCreationListener<?>>();
    private ClassLoader classLoader = org.springframework.util.ClassUtils.getDefaultClassLoader();





    /**
     * Creates a new {@link org.springframework.data.jpa.repository.support.JpaRepositoryFactory}.
     *
     * @param entityManager must not be {@literal null}
     */


    public AppRepositoryFactory(EntityManager entityManager) {

        super(entityManager);
        this.entityManager=entityManager;


    }



    public void setQueryLookupStrategyKey(QueryLookupStrategy.Key key) {
        this.queryLookupStrategyKey = key;
    }

    public void setNamedQueries(NamedQueries namedQueries) {
        this.namedQueries = namedQueries == null ? PropertiesBasedNamedQueries.EMPTY : namedQueries;
    }


    public void addQueryCreationListener(QueryCreationListener<?> listener) {

        Assert.notNull(listener);
        this.queryPostProcessors.add(listener);
    }


    protected Object getTargetRepository(RepositoryMetadata metadata) {

        //noinspection unchecked
        return new AppRepositoryImpl<T, I>((Class<T>) metadata.getDomainType(), entityManager);
    }


    @SuppressWarnings("unchecked")
    protected <T, ID extends Serializable> SimpleJpaRepository<?, ?> getTargetRepository(
            RepositoryMetadata metadata, EntityManager entityManager) {
        Class<?> repositoryInterface = metadata.getRepositoryInterface();
        JpaEntityInformation<?, Serializable> entityInformation =
                getEntityInformation(metadata.getDomainType());
        if (isQueryDslExecutor(repositoryInterface)) {
            return new QueryDslJpaRepository(entityInformation, entityManager);
        } else {
            return new AppRepositoryImpl<>(entityInformation, entityManager); //custom implementation
        }
    }

   public RepositoryMetadata getRepositoryMetadata(Class<?> repositoryInterface) {
        return Repository.class.isAssignableFrom(repositoryInterface) ? new DefaultRepositoryMetadata(repositoryInterface)
                : new AnnotationRepositoryMetadata(repositoryInterface);
    }

    protected void validate(RepositoryInformation repositoryInformation, Object customImplementation) {

        if (null == customImplementation && repositoryInformation.hasCustomMethod()) {

            throw new IllegalArgumentException(String.format(
                    "You have custom methods in %s but not provided a custom implementation!",
                    repositoryInformation.getRepositoryInterface()));
        }

        validate(repositoryInformation);
    }



    public void addRepositoryProxyPostProcessor(RepositoryProxyPostProcessor processor) {

        Assert.notNull(processor);
        if( this.postProcessors==null)
            this.postProcessors=new ArrayList<>();
        this.postProcessors.add(processor);
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader1 = classLoader == null ? org.springframework.util.ClassUtils.getDefaultClassLoader() : classLoader;
    }

    public class AppQueryExecutorMethodInterceptor extends RepositoryFactorySupport.QueryExecutorMethodInterceptor {
        private final Map<Method, RepositoryQuery> queries = new ConcurrentHashMap<Method, RepositoryQuery>();

        private final Object customImplementation;
        private final RepositoryInformation repositoryInformation;
        private final Object target;

        public AppQueryExecutorMethodInterceptor(RepositoryInformation repositoryInformation, Object customImplementation, Object target) {
            super(repositoryInformation, customImplementation, target);

            this.repositoryInformation = repositoryInformation;
            this.customImplementation = customImplementation;
            this.target = target;

            QueryLookupStrategy lookupStrategy = getQueryLookupStrategy(queryLookupStrategyKey);
            Iterable<Method> queryMethods = repositoryInformation.getQueryMethods();

            if (lookupStrategy == null) {

                if (queryMethods.iterator().hasNext()) {
                    throw new IllegalStateException("You have defined query method in the repository but "
                            + "you don't have no query lookup strategy defined. The "
                            + "infrastructure apparently does not support query methods!");
                }

                return;
            }

            for (Method method : queryMethods) {
                RepositoryQuery query = lookupStrategy.resolveQuery(method, repositoryInformation, namedQueries);
                invokeListeners(query);
                queries.put(method, query);
            }
        }

        @SuppressWarnings({ "rawtypes", "unchecked" })
        private void invokeListeners(RepositoryQuery query) {

            for (QueryCreationListener listener : queryPostProcessors) {
                Class<?> typeArgument = GenericTypeResolver.resolveTypeArgument(listener.getClass(),
                        QueryCreationListener.class);
                if (typeArgument != null && typeArgument.isAssignableFrom(query.getClass())) {
                    listener.onCreation(query);
                }
            }
        }


        private boolean isCustomMethodInvocation(MethodInvocation invocation) {

            return null != customImplementation && repositoryInformation.isCustomMethod(invocation.getMethod());

        }


        private boolean hasQueryFor(Method method) {

            return queries.containsKey(method);
        }

        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            Method method = invocation.getMethod();

            if (isCustomMethodInvocation(invocation)) {
                Method actualMethod = repositoryInformation.getTargetClassMethod(method);
                makeAccessible(actualMethod);
                return executeMethodOn(customImplementation, actualMethod, invocation.getArguments());
            }

            if (hasQueryFor(method)) {
                if(method.isAnnotationPresent(ToJson.class)){
                   /* Object t=queries.get(method).execute(invocation.getArguments());

                    ToJson toJson=method.getAnnotation(ToJson.class);

                    JSONSerializer serializer= new JSONSerializer().exclude(toJson.excludeFields());

                    if(toJson.includeFields()!=null&&toJson.includeFields().length>0)
                        serializer.include(toJson.includeFields());


                    String defaultLocale;
                    defaultLocale=invocation.getArguments()[0] instanceof AppPageRequest?
                            ((AppPageRequest)invocation.getArguments()[0]).locale():"dd-MM-yyyy";

                    serializer.transform(dateTransformer(defaultLocale), Date.class);
//                    serializer.transform(dateTransformer(defaultLocale), GregorianCalendar.class);
                    String json= serializer.serialize(t);
                    if(t instanceof List && ((List)t).get(0)!=null&&((List)t).get(0) instanceof BaseDomain){
                        ((BaseDomain)((List)t).get(0)).setJson(json);
                    }

                    return t;*/
                }else{
                    return queries.get(method).execute(invocation.getArguments()) ;
                }




            }

            // Lookup actual method as it might be redeclared in the interface
            // and we have to use the repository instance nevertheless
            Method actualMethod = repositoryInformation.getTargetClassMethod(method);
            return executeMethodOn(target, actualMethod, invocation.getArguments());
        }




        private Object executeMethodOn(Object target, Method method, Object[] parameters) throws Throwable {

            try {
                return method.invoke(target, parameters);
            } catch (Exception e) {
                ClassUtils.unwrapReflectionException(e);
            }

            throw new IllegalStateException("Should not occur!");
        }
    }

    public <T> T getRepository(Class<T> repositoryInterface, Object customImplementation) {

        RepositoryMetadata metadata = getRepositoryMetadata(repositoryInterface);
        Class<?> customImplementationClass = null == customImplementation ? null : customImplementation.getClass();
        RepositoryInformation information = getRepositoryInformation(metadata, customImplementationClass);

        validate(information, customImplementation);

        Object target = getTargetRepository(information);

        // Create proxy
        ProxyFactory result = new ProxyFactory();
        result.setTarget(target);
        result.setInterfaces(new Class[] { repositoryInterface, Repository.class });

        for (RepositoryProxyPostProcessor processor : postProcessors) {
            processor.postProcess(result, information);
        }

        result.addAdvice(new QueryExecutorMethodInterceptor(information, customImplementation, target));

        return (T) result.getProxy(classLoader);
    }


    protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {

        if (isQueryDslExecutor(metadata.getRepositoryInterface())) {
            return QueryDslJpaRepository.class;
        }

        return AppRepository.class;
    }

    /**
     * Returns whether the given repository interface requires a QueryDsl
     * specific implementation to be chosen.
     *
     * @param repositoryInterface
     * @return
     */
    private boolean isQueryDslExecutor(Class<?> repositoryInterface) {
        return QueryDslPredicateExecutor.class
                .isAssignableFrom(repositoryInterface);
    }
}
