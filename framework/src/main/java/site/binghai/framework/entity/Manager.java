package site.binghai.framework.entity;

import lombok.Data;
import site.binghai.framework.interfaces.SessionPersistent;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class Manager extends BaseEntity implements SessionPersistent {
    @Id
    @GeneratedValue
    private Long id;
    private String userName;
    private String passWord;
    private String nickName;
    private Boolean forbidden;

    @Override
    public String sessionTag() {
        return "_SYS_MANAGER_";
    }
}
