package site.binghai.core.entity;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import site.binghai.core.enums.DriverType;
import site.binghai.framework.entity.BaseEntity;
import site.binghai.framework.utils.MD5;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class ConnConfig extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;
    /**
     * @see DriverType
     */
    private String driveType;
    private String host;
    private String port;
    private String dbName;
    private String user;
    private String pass;
    private String extraCfg;
    private String coding;

    private String name;

    public String getCfgId() {
        return MD5.encryption(JSON.toJSONString(this));
    }
}
