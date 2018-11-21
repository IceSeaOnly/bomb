package site.binghai.core.def;

import site.binghai.framework.entity.Result;
import site.binghai.core.entity.DataBaseConfig;

import java.sql.Connection;

public interface Driver {
    Result<Connection> init(DataBaseConfig dataBaseConfig);
    Result<Boolean> close(Connection connection);
}

