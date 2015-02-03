package ro.teamnet.bootstrap.service;

import org.springframework.stereotype.Service;
import ro.teamnet.bootstrap.domain.Menu;
import ro.teamnet.bootstrap.repository.MenuRepository;

import javax.inject.Inject;
import java.util.List;

@Service
public class MenuServiceImpl implements MenuService {
    @Inject
    MenuRepository menuRepository;

    @Override
    public List<Menu> getMenuByParentId(Long parentId) {
        return menuRepository.getMenuByParentId(parentId);
    }
}
