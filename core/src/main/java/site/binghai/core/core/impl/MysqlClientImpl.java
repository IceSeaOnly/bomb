package site.binghai.core.core.impl;

import lombok.Data;
import site.binghai.core.core.Client;
import site.binghai.core.core.Table;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class MysqlClientImpl implements Client {
    private Connection connection;
    private Map<String, Table> tables;

    public MysqlClientImpl(Connection connection) {
        this.connection = connection;
        this.tables = new HashMap<>();
        try {
            loadTable(connection).forEach(v -> tables.put(v.getTableName().getResult(), v));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<Table> loadTable(Connection connection) throws Exception {
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
        return tables;
    }


    @Override
    public List<String> getTableNames() throws Exception {
        return tables.values().stream().map(v -> v.getTableName().getResult()).collect(Collectors.toList());
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
