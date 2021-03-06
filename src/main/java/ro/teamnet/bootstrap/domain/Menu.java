package ro.teamnet.bootstrap.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Entity used for application menu configuration.
 */
@Entity
@Table(name = "T_MENU")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Menu implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "name")
    private String name;

    @Column(name = "route")
    private String route;

    @Column(name = "css_class")
    private String cssClass;

    @Column(name = "sort_no")
    private Long sortNo;

    @Column(name = "active")
    private Long active;

    @OneToOne (fetch = FetchType.LAZY)
    @JoinColumn(name="module_id", unique= true, nullable=false, insertable=true, updatable=true)
    private Module module;

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

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public Long getSortNo() {
        return sortNo;
    }

    public void setSortNo(Long sortNo) {
        this.sortNo = sortNo;
    }

    public Long getActive() {
        return active;
    }

    public void setActive(Long active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Menu)) return false;

        Menu menu = (Menu) o;

        if (module != null ? !module.equals(menu.module) : menu.module != null) return false;
        if (cssClass != null ? !cssClass.equals(menu.cssClass) : menu.cssClass != null) return false;
        if (!id.equals(menu.id)) return false;
        if (name != null ? !name.equals(menu.name) : menu.name != null) return false;
        if (parentId != null ? !parentId.equals(menu.parentId) : menu.parentId != null) return false;
        if (route != null ? !route.equals(menu.route) : menu.route != null) return false;
        if (!sortNo.equals(menu.sortNo)) return false;
        if (!active.equals(menu.active)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (parentId != null ? parentId.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (route != null ? route.hashCode() : 0);
        result = 31 * result + (cssClass != null ? cssClass.hashCode() : 0);
        result = 31 * result + (module != null ? module.hashCode() : 0);
        result = 31 * result + (sortNo != null ? sortNo.hashCode() : 0);
        result = 31 * result + (active != null ? active.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", label='" + name + '\'' +
                ", route='" + route + '\'' +
                ", cssClass='" + cssClass + '\'' +
                ", authorities=" + module +
                ", sortNo=" + sortNo +
                ", active=" + active +
                '}';
    }
}
