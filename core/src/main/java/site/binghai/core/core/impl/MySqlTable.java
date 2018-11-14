package site.binghai.core.core.impl;

import site.binghai.core.core.Table;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MySqlTable implements Table {
    private String tableName;
    private Connection connection;

    public MySqlTable(String tableName, Connection connection) {
        this.tableName = tableName;
        this.connection = connection;
    }

    @Override
    public String showCreateSql() throws Exception {
        PreparedStatement stmt = connection.prepareStatement("SHOW CREATE TABLE " + tableName);
        ResultSet set = stmt.executeQuery();
        StringBuilder sb = new StringBuilder();
        while (null != set && set.next()) {
            sb.append(set.getString(2));
        }
        return sb.toString();
    }

    @Override
    public String getTableName() {
        return tableName;
    }
}
