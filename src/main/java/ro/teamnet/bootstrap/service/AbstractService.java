package ro.teamnet.bootstrap.service;

import java.io.Serializable;
import java.util.List;


public interface AbstractService<T extends Serializable, ID extends Serializable> {

    public void save(T t);
    public List<T> findAll();
    public T findOne(ID id);
    public void delete(ID id);
}
