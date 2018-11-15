package site.binghai.core.core;

import site.binghai.framework.entity.Result;

import java.util.List;

public interface Client {
    Result<List<String>> getTableNames();

    Result<List<Table>> getTables();

    Result<Table> getTable(String tableName);
}
