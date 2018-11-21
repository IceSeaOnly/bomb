package site.binghai.core.core;

import lombok.Data;
import site.binghai.core.core.impl.ClientManager;
import site.binghai.core.entity.ColumnMapperItem;
import site.binghai.core.entity.DataBaseConfig;
import site.binghai.core.entity.TableMapperItem;

import java.util.Map;

/**
 * 会话层，负责映射转换工作
 * */
@Data
public class Conversation {
    private Client client;
    private DataBaseConfig dataBaseConfig;
    private Map<String, TableMapperItem> tableMapperItemMap;
    private Map<String, Map<String,ColumnMapperItem>> columnMapperItemMap;

    public Conversation(Client client, DataBaseConfig dataBaseConfig,
                        Map<String, TableMapperItem> tableMapperItemMap,
                        Map<String, Map<String,ColumnMapperItem>> columnMapperItemMap) {
        this.client = client;
        this.dataBaseConfig = dataBaseConfig;
        this.tableMapperItemMap = tableMapperItemMap;
        this.columnMapperItemMap = columnMapperItemMap;
    }
}
