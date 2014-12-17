package ro.teamnet.bootstrap.extend;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import javax.persistence.EntityManager;
import java.io.Serializable;


public class AppRepositoryFactoryBean <R extends JpaRepository<T, I>, T, I
        extends Serializable>extends JpaRepositoryFactoryBean<R, T, I> {
    protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {

        return new AppRepositoryFactory(entityManager);
    }
}
