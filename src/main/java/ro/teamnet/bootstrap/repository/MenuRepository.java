package ro.teamnet.bootstrap.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ro.teamnet.bootstrap.domain.Menu;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, String> {
    @Query("select m from Menu m where m.parentId = :parentId")
    List<Menu> getMenuByParentId(@Param(value = "parentId") Long parentId);
}
