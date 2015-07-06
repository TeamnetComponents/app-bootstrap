package ro.teamnet.bootstrap.web.rest.dto;

import ro.teamnet.bootstrap.domain.Menu;
import ro.teamnet.bootstrap.domain.Module;

import java.util.ArrayList;
import java.util.List;

/**
 * A menu DTO.
 */
public class MenuDTO {
    private Long id;
    private Long parentId;
    private String name;
    private String route;
    private String cssClass;
    private Long sortNo;
    private Boolean active;
    private List<MenuDTO> items = new ArrayList<MenuDTO>();
    private Module module;

    public MenuDTO(Menu menu) {
        fetchMenuData(menu);
    }

    public MenuDTO(Menu menu, List<MenuDTO> items) {
        fetchMenuData(menu);
        this.items = items;
    }

    public void fetchMenuData(Menu menu) {
        this.id = menu.getId();
        this.parentId = menu.getParentId();
        this.name = menu.getName();
        this.route = menu.getRoute();
        this.cssClass = menu.getCssClass();
        this.sortNo = menu.getSortNo();
        this.active = menu.getActive() == 1;
        this.module = menu.getModule();
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getParentId() {
        return parentId;
    }
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getRoute() {
        return route;
    }
    public void setRoute(String route) {
        this.route = route;
    }
    public String getCssClass() {
        return cssClass;
    }
    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }
    public Long getSortNo() {
        return sortNo;
    }
    public void setSortNo(Long sortNo) {
        this.sortNo = sortNo;
    }
    public Boolean getActive() {
        return active;
    }
    public void setActive(Boolean active) {
        this.active = active;
    }
    public List<MenuDTO> getItems() {
        return items;
    }
    public void setItems(List<MenuDTO> items) {
        this.items = items;
    }
    public Module getModule() {
        return module;
    }
    public void setModule(Module module) {
        this.module = module;
    }
}
