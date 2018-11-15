package site.binghai.core.core.impl;

import lombok.Data;
import org.apache.commons.collections.CollectionUtils;
import site.binghai.core.core.Table;
import site.binghai.core.def.XCousumer;
import site.binghai.framework.entity.Result;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Data
public class MysqlTableImpl implements Table {
    private List<String> columnNameList;
    private Connection connection;

    private String tableName;
    private String tableType;
    private String dataBaseName;
    private String tableRemark;

    public MysqlTableImpl(Connection connection, String tableName, String tableType,
                          String dataBaseName, String tableRemark) {
        this.columnNameList = new ArrayList<>();
        this.connection = connection;
        this.tableName = tableName;
        this.tableType = tableType;
        this.dataBaseName = dataBaseName;
        this.tableRemark = tableRemark;
    }

    @Override
    public Result<String> showCreateSql() {
        StringBuilder sb = new StringBuilder();
        try {
            PreparedStatement stmt = connection.prepareStatement("SHOW CREATE TABLE " + tableName);
            ResultSet set = stmt.executeQuery();
            while (null != set && set.next()) {
                sb.append(set.getString(2));
            }
        } catch (Exception e) {
            return new Result<>(false, e, null);
        }
        return new Result<>(sb.toString());
    }

    @Override
    public Result<List<String>> getColumnNameList() {
        if (!CollectionUtils.isEmpty(columnNameList)) {
            return new Result<>(columnNameList);
        }

        //process(new Result(), ret -> {
        //    DatabaseMetaData dbMetaData = connection.getMetaData();
        //    ResultSet rs = dbMetaData.getTables(null, null, null, new String[] {"TABLE"});
        //})

        return null;
    }

    @Override
    public Result<String> getTableName() {
        return new Result(tableName);
    }

    @Override
    public Result<Long> getMaxId() {
        Long max = null;
        try {
            PreparedStatement stmt = connection.prepareStatement("select max(id) from " + tableName);
            ResultSet set = stmt.executeQuery();
            while (null != set && set.next()) {
                max = set.getLong(1);
            }
        } catch (Exception e) {
            return new Result<>(false, e, null);
        }
        return null;
    }

    @Override
    public Result<Object> executeRawSql(String sql) {
        //return process(new Result(), ret -> {
        //    PreparedStatement stmt = connection.prepareStatement("select max(id) from " + tableName);
        //    ResultSet set = stmt.executeQuery();
        //    while (null != set && set.next()) {
        //
        //    }
        //});
        return null;
    }

    @Override
    public Result<Long> count() {
        return null;
    }

    private Result process(Result result, XCousumer cousumer) {
        try {
            return cousumer.accept(result);
        } catch (Exception e) {
            result.setException(e);
        }
        return result;
    }
}
