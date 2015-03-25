package ro.teamnet.bootstrap.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ro.teamnet.bootstrap.domain.Menu;
import ro.teamnet.bootstrap.domain.Role;
import ro.teamnet.bootstrap.extend.AppRepository;

import java.util.List;

@Repository
public interface MenuRepository extends AppRepository<Menu, Long> {
    @Query("select m from Menu m where m.parentId = :parentId order by m.sortNo")
    List<Menu> getMenuByParentId(@Param(value = "parentId") Long parentId);

    @Modifying
    @Query("update Menu m set m.parentId = :parentId, m.sortNo = :sortNo where m.id = :id")
    void updateMenuPosition(
            @Param(value = "id") Long id,
            @Param(value = "parentId") Long parentId,
            @Param(value = "sortNo") Long sortNo
    );

    Menu getById(Long id);
}
