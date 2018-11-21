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
        Conversation conversation = conversationMap.get(databaseCfgId);
        if (conversation == null) {
            instance(databaseCfgId);
        }
        return conversation;
    }

    private synchronized void instance(Long databaseCfgId) {
        if (conversationMap.get(databaseCfgId) != null) { return; }

        DataBaseConfig config = dataBaseConfigService.findById(databaseCfgId);
        if (config == null) {
            logger.error("no DataBaseConfig of id = {}", databaseCfgId);
            throw new RuntimeException("no DataBaseConfig of id = " + databaseCfgId);
        }

        Client client = clientManager.get(config);
        Map<String, TableMapperItem> tableMapperItemMap = buildTableMapperItemMap(client);
        Map<String, Map<String, ColumnMapperItem>> columnMapperItemMap = buildColumnMapperItemMap(client);
        Conversation conversation = new Conversation(client, config, tableMapperItemMap, columnMapperItemMap);
        conversationMap.put(databaseCfgId, conversation);
    }

    /**
     * 读取表中每个列的map配置
     */
    private Map<String, Map<String, ColumnMapperItem>> buildColumnMapperItemMap(Client client) {
        Map<String, Map<String, ColumnMapperItem>> map = new HashMap<>();

        client.getTables().getResult().forEach(table -> {
            Map<String, ColumnMapperItem> ret = new HashMap<>();
            Map<String, ColumnMapperItem> curTableMap = new HashMap<>();
            columnMapperItemService.findByDataBaseIdAndTableName(client.getDbId(), table.getTableName())
                .forEach(mapper -> curTableMap.put(mapper.getColumnName(), mapper));

            table.getColumnNameList().getResult().forEach(column -> {
                ColumnMapperItem item = curTableMap.get(column.name());
                if (item == null) {
                    item = new ColumnMapperItem();
                    item.setTableName(table.getTableName());
                    item.setDatabaseId(client.getDbId());
                    item.setColumnName(column.name());
                    item.setColumnAlias(column.name());
                    item = columnMapperItemService.save(item);
                }
                ret.put(column.name(), item);
                curTableMap.remove(column.name());
            });
            map.put(table.getTableName(), ret);

            if (curTableMap.size() > 0) {
                logger.warn("delete {} discarded column mapper.", curTableMap.size());
                curTableMap.forEach((k, v) -> columnMapperItemService.delete(v.getId()));
            }
        });
        return map;
    }

    /**
     * 读取每个表的map配置
     */
    private Map<String, TableMapperItem> buildTableMapperItemMap(Client client) {
        Map<String, TableMapperItem> map = new HashMap<>();
        List<String> tables = client.getTableNames().getResult();
        Map<String, TableMapperItem> tmp = new HashMap<>();
        tableMapperItemService.findByDatabaseId(client.getDbId()).forEach(v -> tmp.put(v.getTableName(), v));

        for (String table : tables) {
            TableMapperItem item = tmp.get(table);
            if (item == null) {
                item = new TableMapperItem();
                item.setTableName(table);
                item.setTableAlias(table);
                item.setDatabaseId(client.getDbId());
                item = tableMapperItemService.save(item);
            }
            map.put(table, item);
            tmp.remove(table);
        }

        if (tmp.size() > 0) {
            logger.warn("delete {} discarded table mapper.", tmp.size());
            tmp.forEach((k, v) -> {
                columnMapperItemService.clearByDatabaseIdAndTableName(client.getDbId(), v.getTableName());
                tableMapperItemService.delete(v.getId());
            });
        }
        return map;
    }
}
