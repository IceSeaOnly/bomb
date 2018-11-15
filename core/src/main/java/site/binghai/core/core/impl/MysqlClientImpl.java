package site.binghai.core.core.impl;

import site.binghai.core.core.BaseProcess;
import site.binghai.core.core.Client;
import site.binghai.core.core.Table;
import site.binghai.framework.entity.Result;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MysqlClientImpl extends BaseProcess implements Client {
    private Connection connection;
    private Map<String, Table> tables;
    private Boolean initedTable;

    public MysqlClientImpl(Connection connection) {
        this.connection = connection;
        this.tables = new HashMap<>();
        this.initedTable = Boolean.FALSE;
    }

    private synchronized void loadTable() {
        if (initedTable) {
            return;
        }

        Result<List<Table>> ret = new Result();

        process(ret, rt -> {
            List<Table> tables = new ArrayList<>();
            DatabaseMetaData dbMetaData = connection.getMetaData();
            ResultSet rs = dbMetaData.getTables(null, null, null, new String[] {"TABLE"});
            while (rs.next()) {
                String tableName = rs.getString("TABLE_NAME");
                String tableType = rs.getString("TABLE_TYPE");
                String tableCat = rs.getString("TABLE_CAT");
                String remarks = rs.getString("REMARKS");

                Table table = new MysqlTableImpl(connection, tableName, tableType, tableCat, remarks);
                tables.add(table);
            }
            ret.setResult(tables);
        });

        ret.ifPresent(list -> list.forEach(v -> tables.put(v.getTableName(), v)));
        this.initedTable = Boolean.TRUE;
    }

    @Override
    public Result<List<String>> getTableNames() {
        Result<List<String>> result = new Result<>();
        process(result, ret -> {
            List<String> list = getTables().getResult().stream().map(v -> v.getTableName()).collect(Collectors.toList());
            ret.setResult(list);
        });
        return result;
    }

    private Map<String, Table> getTableMap() {
        if (!initedTable) {
            loadTable();
        }
        return tables;
    }

    @Override
    public Result<List<Table>> getTables() {
        List<Table> list = new ArrayList(getTableMap().values());
        return new Result(list);
    }

    @Override
    public Result<Table> getTable(String tableName) {
        if (getTableMap().get(tableName) == null) {
            return new Result(false, "no such table called " + tableName, null, null);
        }
        return new Result<>(getTableMap().get(tableName));
    }
}
