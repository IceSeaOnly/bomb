package site.binghai.core.core;

import java.util.List;

public interface Client {
    List<String> getTableNames() throws Exception;
    List<Table> getTables() throws Exception;
    Table getTable(String tableName);
}
