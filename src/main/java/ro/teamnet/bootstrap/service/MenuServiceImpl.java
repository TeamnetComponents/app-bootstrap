package ro.teamnet.bootstrap.service;

import org.springframework.stereotype.Service;
import ro.teamnet.bootstrap.domain.Menu;
import ro.teamnet.bootstrap.repository.MenuRepository;
import ro.teamnet.bootstrap.web.rest.dto.MenuDTO;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Service
public class MenuServiceImpl implements MenuService {
    @Inject
    MenuRepository menuRepository;

    @Override
    public List<Menu> getMenuByParentId(Long parentId) {
        return menuRepository.getMenuByParentId(parentId);
    }

    @Override
    public List<MenuDTO> getMenuTreeByParentId(Long parentId) {
        return traverseMenus(menuRepository.getMenuByParentId(parentId));
    }

    @Override
    public void updateMenuPosition(Long id, Long parentId, Long sortNo) {
        menuRepository.updateMenuPosition(id, parentId, sortNo);
    }

    private List<MenuDTO> traverseMenus(List<Menu> rootMenus) {
        List<MenuDTO> resultMenus = new ArrayList<MenuDTO>();
        for(Menu menu: rootMenus) {
            List<Menu> menus = menuRepository.getMenuByParentId(menu.getId());
            if(menus.size() > 0) {
                resultMenus.add(new MenuDTO(menu, traverseMenus(menus)));
            } else {
                resultMenus.add(new MenuDTO(menu, new ArrayList<MenuDTO>()));
            }
        }

        return resultMenus;
    }
}
