package ro.teamnet.bootstrap.service;

import ro.teamnet.bootstrap.domain.Menu;

import java.util.List;

public interface MenuService {
    List<Menu> getMenuByParentId(Long parentId);
}
