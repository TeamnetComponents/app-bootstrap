package ro.teamnet.bootstrap.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import ro.teamnet.bootstrap.extend.AppRepository;

import java.io.Serializable;
import java.util.List;


public abstract class AbstractServiceImpl<T extends Serializable, ID extends Serializable> implements AbstractService<T, ID> {


         private final Logger log = LoggerFactory.getLogger(AbstractServiceImpl.class);

         private final AppRepository<T, ID> repository;

         public AbstractServiceImpl(AppRepository<T, ID> repository){
           this.repository=repository;
         }


        @Override
        @Transactional
        public void save(T t){
            log.debug("REST request to save : {}", t);
            repository.save(t) ;
        }



        @Override
        public List<T> findAll(){
            log.debug("REST request to get all records");
            return repository.findAll();
        }



        @Override
        public T findOne(ID id){
            log.debug("REST request to get : {}", id);
            return (T)repository.findOne(id);
        }


        @Override
        @Transactional
        public void delete(ID id){
            log.debug("REST request to delete : {}", id);
            repository.delete(id);
        }


}
