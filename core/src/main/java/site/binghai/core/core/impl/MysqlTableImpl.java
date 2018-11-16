package site.binghai.core.core.impl;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import site.binghai.core.core.BaseProcess;
import site.binghai.core.core.Column;
import site.binghai.core.core.Table;
import site.binghai.framework.entity.Result;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MysqlTableImpl extends BaseProcess implements Table {
    @JsonIgnore
    @JSONField(serialize=false)
    private Connection connection;
    private List<Column> columns;
    private Boolean inited;

    private String tableName;
    private String tableType;
    private String dataBaseName;
    private String tableRemark;

    public MysqlTableImpl(Connection connection, String tableName, String tableType,
                          String dataBaseName, String tableRemark) {
        this.columns = new ArrayList<>();
        this.connection = connection;
        this.tableName = tableName;
        this.tableType = tableType;
        this.dataBaseName = dataBaseName;
        this.tableRemark = tableRemark;
        this.inited = Boolean.FALSE;
        System.out.println(String.format("MysqlTableImpl for table %s created.", tableName));
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
    public Result<List<Column>> getColumnNameList() {
        return new Result(getColumns());
    }

    private synchronized void loadColumn() {
        if (inited) {
            return;
        }
        System.out.println(String.format("MysqlTableImpl for table %s loading columns.", tableName));
        Result<List<Column>> res = new Result();
        process(res, ret -> {
            List<Column> columns = new ArrayList<>();
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM " + tableName + " LIMIT 1");
            ResultSet set = stmt.executeQuery();
            ResultSetMetaData meta = set.getMetaData();
            //表列数
            int size = meta.getColumnCount();
            for (int i = 0; i < size; i++) {
                Column column = new MysqlColumnImpl(
                    meta.getColumnName(i + 1),
                    meta.getTableName(i + 1),
                    meta.getColumnTypeName(i + 1),
                    true,
                    meta.isNullable(i + 1) == 1,
                    true,
                    true,
                    -1,
                    meta.getPrecision(i + 1),
                    meta.getScale(i + 1)
                );
                columns.add(column);
            }
            ret.setResult(columns);
        });
        res.ifPresent(list -> columns.addAll(list));
        inited = Boolean.TRUE;
    }

    @Override
    public String getTableName() {
        return tableName;
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

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableType() {
        return tableType;
    }

    public void setTableType(String tableType) {
        this.tableType = tableType;
    }

    public String getDataBaseName() {
        return dataBaseName;
    }

    public void setDataBaseName(String dataBaseName) {
        this.dataBaseName = dataBaseName;
    }

    public String getTableRemark() {
        return tableRemark;
    }

    public void setTableRemark(String tableRemark) {
        this.tableRemark = tableRemark;
    }

    public List<Column> getColumns() {
        if (!inited) {
            loadColumn();
        }
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }
}
