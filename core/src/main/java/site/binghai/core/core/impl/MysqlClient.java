package site.binghai.core.core.impl;

import site.binghai.core.core.Client;
import site.binghai.core.core.Table;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MysqlClient implements Client {
    private Connection connection;
    private Map<String, Table> tables;

    public MysqlClient(Connection connection) {
        this.connection = connection;
        this.tables = new HashMap<>();
        try {
            loadTable(connection).forEach(v -> tables.put(v.getTableName(), v));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<Table> loadTable(Connection connection) throws Exception {
        List<Table> tables = new ArrayList<>();
        for (String tableName : getTableNames()) {
            tables.add(new MySqlTable(tableName, connection));
        }
        return tables;
    }

    @Override
    public List<String> getTableNames() throws Exception {
        List<String> tables = new ArrayList<>();
        PreparedStatement stmt = connection.prepareStatement("show tables");
        ResultSet set = stmt.executeQuery();
        while (null != set && set.next()) {
            tables.add(set.getString(1));
        }
        return tables;
    }

    @Override
    public List<Table> getTables() throws Exception {
        return new ArrayList<>(tables.values());
    }

    @Override
    public Table getTable(String tableName) {
        return tables.get(tableName);
    }
}
