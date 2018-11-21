package site.binghai.core.entity;

import lombok.Data;
import site.binghai.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class ColumnMapperItem extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;
    private Long databaseId;
    private String tableName;
    private String columnName;
    private String columnAlias;
    private String remark;
}
