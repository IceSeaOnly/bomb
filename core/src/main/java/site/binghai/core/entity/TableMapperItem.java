package site.binghai.core.entity;

import lombok.Data;
import site.binghai.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class TableMapperItem extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;
    private Long databaseId;
    private String tableName;
    private String tableAlias;
    private String remark;
}
