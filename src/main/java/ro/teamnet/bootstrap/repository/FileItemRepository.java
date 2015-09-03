package ro.teamnet.bootstrap.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ro.teamnet.bootstrap.domain.FileItem;
import ro.teamnet.bootstrap.extend.AppRepository;

import java.util.List;

public interface FileItemRepository extends AppRepository<FileItem,Long> {

    @Query(value = "select c from FileItem as c where token=:tkn and c.fileMaster.group=:grp")
    public FileItem findByTokenAndGroup( @Param(value = "tkn") String token, @Param(value = "grp") String group);

    @Query(value="select c from FileItem as c where c.fileMaster.id=:id" )
    public List<FileItem> findByMasterId( @Param(value = "id") Long fileMasterId);
}
