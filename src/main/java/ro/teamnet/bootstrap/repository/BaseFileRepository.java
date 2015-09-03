package ro.teamnet.bootstrap.repository;

import org.springframework.data.repository.NoRepositoryBean;
import ro.teamnet.bootstrap.extend.AppRepository;

import java.io.Serializable;

@NoRepositoryBean
public interface BaseFileRepository<T extends Serializable> extends AppRepository<T, Long> {
}
