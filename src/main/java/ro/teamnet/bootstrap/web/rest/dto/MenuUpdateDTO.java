package ro.teamnet.bootstrap.web.rest.dto;

public class MenuUpdateDTO {
    public static enum PropertyTypes { SORT_NO };

    private Long id;
    private Long parentId;
    private String property;
    private Long newSortNo;
    private Long oldSortNo;

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

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public Long getNewSortNo() {
        return newSortNo;
    }

    public void setNewSortNo(Long newSortNo) {
        this.newSortNo = newSortNo;
    }

    public Long getOldSortNo() {
        return oldSortNo;
    }

    public void setOldSortNo(Long oldSortNo) {
        this.oldSortNo = oldSortNo;
    }

    @Override
    public String toString() {
        return "MenuUpdateDTO{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", property='" + property + '\'' +
                ", newSortNo=" + newSortNo +
                ", oldSortNo=" + oldSortNo +
                '}';
    }
}
