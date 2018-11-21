package site.binghai.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.binghai.core.core.Client;
import site.binghai.core.core.Conversation;
import site.binghai.core.core.impl.ClientManager;
import site.binghai.core.entity.ColumnMapperItem;
import site.binghai.core.entity.DataBaseConfig;
import site.binghai.core.entity.TableMapperItem;
import site.binghai.framework.service.BaseService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ConversationService extends BaseService {
    private Map<Long, Conversation> conversationMap;

    @Autowired
    private DataBaseConfigService dataBaseConfigService;
    @Autowired
    private ClientManager clientManager;
    @Autowired
    private TableMapperItemService tableMapperItemService;
    @Autowired
    private ColumnMapperItemService columnMapperItemService;

    public ConversationService() {
        conversationMap = new ConcurrentHashMap<>();
    }

    public Conversation getConversation(Long databaseCfgId) {
        DataBaseConfig config = dataBaseConfigService.findById(databaseCfgId);
        if (config == null) {
            throw new RuntimeException("no DataBaseConfig of id = " + databaseCfgId);
        }

        Client client = clientManager.get(config);
        Map<String, TableMapperItem> tableMapperItemMap = buildTableMapperItemMap(client);
        Map<String, Map<String, ColumnMapperItem>> columnMapperItemMap = buildColumnMapperItemMap(client,
            tableMapperItemMap);
        Conversation conversation = new Conversation(client, config, tableMapperItemMap, columnMapperItemMap);
        return conversation;
    }

    /**
     * 读取表中每个列的map配置
     * */
    private Map<String, Map<String, ColumnMapperItem>> buildColumnMapperItemMap(Client client,
                                                                                Map<String, TableMapperItem> tableMapperItemMap) {
        Map<String, Map<String, ColumnMapperItem>> map = new HashMap<>();

        tableMapperItemMap.forEach((tableName, tableMapper) -> {
            Map<String, ColumnMapperItem> curTableMap = new HashMap<>();
            columnMapperItemService.findByDataBaseIdAndTableName(client.getDbId(), tableName)
                .forEach(mapper -> curTableMap.put(mapper.getColumnName(), mapper));
            map.put(tableName, curTableMap);
        });
        return map;
    }

    /**
     * 读取每个表的map配置
     * */
    private Map<String, TableMapperItem> buildTableMapperItemMap(Client client) {
        Map<String, TableMapperItem> map = new HashMap<>();
        List<String> tables = client.getTableNames().getResult();
        for (String table : tables) {
            TableMapperItem item = tableMapperItemService.findByDatabaseIdAndTableName(client.getDbId(), table);
            if (item == null) {
                item = new TableMapperItem();
                item.setTableName(table);
                item.setTableAlias(table);
                item.setDatabaseId(client.getDbId());
                item = tableMapperItemService.save(item);
            }
            map.put(table, item);
        }
        return map;
    }
}
