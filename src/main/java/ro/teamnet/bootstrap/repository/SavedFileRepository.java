package ro.teamnet.bootstrap.repository;

import org.springframework.stereotype.Repository;
import ro.teamnet.bootstrap.domain.SavedFile;
import ro.teamnet.bootstrap.extend.AppRepository;

/**
 * Created by Radu.Hoaghe on 7/23/2015.
 */
@Repository
public interface SavedFileRepository extends AppRepository<SavedFile, Long> {
}
