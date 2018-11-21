package site.binghai.core.service;

import org.springframework.stereotype.Service;
import site.binghai.core.entity.ColumnMapperItem;
import site.binghai.framework.service.BaseService;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ColumnMapperItemService extends BaseService<ColumnMapperItem> {

    public List<ColumnMapperItem> findByDataBaseIdAndTableName(Long dbId, String tableName) {
        ColumnMapperItem exp = new ColumnMapperItem();
        exp.setDatabaseId(dbId);
        exp.setTableName(tableName);
        return query(exp);
    }

    @Transactional
    public void clearByDatabaseIdAndTableName(Long dbId, String tableName) {
        findByDataBaseIdAndTableName(dbId, tableName)
            .forEach(v -> {
                delete(v.getId());
                logger.warn("column {} of table {} has been cleaned.", v.getColumnName(), tableName);
            });
    }
}
