package site.binghai.core.core;

import site.binghai.framework.entity.Result;

import java.util.List;

public interface Table {
    Result<String> showCreateSql();

    Result<List<Column>> getColumnNameList();

    String getTableName();

    Result<Long> getMaxId();

    Result<Object> executeRawSql(String sql);

    Result<Long> count();
}
