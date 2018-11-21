package site.binghai.core.service;

import org.springframework.stereotype.Service;
import site.binghai.core.entity.TableMapperItem;
import site.binghai.framework.service.BaseService;

import java.util.List;

@Service
public class TableMapperItemService extends BaseService<TableMapperItem> {

    public List<TableMapperItem> findByDatabaseId(Long dbId){
        TableMapperItem exp = new TableMapperItem();
        exp.setDatabaseId(dbId);
        return query(exp);
    }

    public TableMapperItem findByDatabaseIdAndTableName(Long dbId,String tableName){
        TableMapperItem exp = new TableMapperItem();
        exp.setDatabaseId(dbId);
        exp.setTableName(tableName);
        TableMapperItem ret = queryOne(exp);
        return ret;
    }
}
