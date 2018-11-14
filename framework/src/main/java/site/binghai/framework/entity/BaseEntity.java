package site.binghai.framework.entity;

import site.binghai.framework.utils.TimeTools;

import javax.persistence.MappedSuperclass;

/**
 * 具有支付状态的实体请继承 PayBizEntity
 * @see PayBizEntity
 * */

@MappedSuperclass
public abstract class BaseEntity {
    private Long created;
    private String createdTime;

    public BaseEntity() {
        created = TimeTools.currentTS();
        createdTime = TimeTools.format(created);
    }


    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public abstract Long getId();
}

