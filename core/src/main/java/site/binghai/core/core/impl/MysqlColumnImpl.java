package site.binghai.core.core.impl;

import lombok.Data;
import site.binghai.core.core.Column;


public class MysqlColumnImpl implements Column {
    private String name;
    private String tableName;
    private String columnDefinition;
    private boolean unique;
    private boolean unllable;
    private boolean insertable;
    private boolean updatable;
    private int length;
    private int precision;
    private int scale;

    @Override
    public String name() {
        return name;
    }

    @Override
    public boolean unique() {
        return unique;
    }

    @Override
    public boolean nullable() {
        return nullable();
    }

    @Override
    public boolean insertable() {
        return insertable;
    }

    @Override
    public boolean updatable() {
        return updatable;
    }

    @Override
    public String columnDefinition() {
        return columnDefinition;
    }

    @Override
    public String table() {
        return tableName;
    }

    @Override
    public int length() {
        return length;
    }

    @Override
    public int precision() {
        return precision;
    }

    @Override
    public int scale() {
        return scale;
    }
}
