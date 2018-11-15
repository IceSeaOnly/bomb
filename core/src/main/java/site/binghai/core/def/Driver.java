package site.binghai.core.def;

import site.binghai.framework.entity.Result;
import site.binghai.core.entity.ConnConfig;

import java.sql.Connection;

public interface Driver {
    Result<Connection> init(ConnConfig connConfig);
    Result<Boolean> close(Connection connection);
}

