package ro.teamnet.bootstrap.repository;

import org.springframework.stereotype.Repository;
import ro.teamnet.bootstrap.domain.SavedFileDetails;
import ro.teamnet.bootstrap.extend.AppRepository;

/**
 * Created by Radu.Hoaghe on 7/23/2015.
 */
@Repository
public interface SavedFileDetailsRepository extends AppRepository<SavedFileDetails, Long> {
}
