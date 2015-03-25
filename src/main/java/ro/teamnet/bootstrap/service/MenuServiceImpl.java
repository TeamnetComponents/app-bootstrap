package ro.teamnet.bootstrap.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.teamnet.bootstrap.domain.Menu;
import ro.teamnet.bootstrap.repository.MenuRepository;
import ro.teamnet.bootstrap.web.rest.dto.MenuDTO;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class MenuServiceImpl extends AbstractServiceImpl<Menu, Long> implements MenuService {
    private final MenuRepository menuRepository;
    private final ModuleService moduleService;

    @Inject
    public MenuServiceImpl(MenuRepository menuRepository, ModuleService moduleService) {
        super(menuRepository);

        this.menuRepository = menuRepository;
        this.moduleService = moduleService;
    }

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

    @Override
    public Menu save(Menu menu) {
        if(menu.getId() == null) {
            // add security module when creating new menu only
            moduleService.save(menu.getModule());
        }

        return menuRepository.save(menu);
    }
}
