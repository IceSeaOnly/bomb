package site.binghai.core.core;

public interface Column {
    String name();

    boolean unique();

    boolean nullable();

    boolean insertable();

    boolean updatable();

    String columnDefinition();

    String table();

    int length();

    int precision();

    int scale();
}
