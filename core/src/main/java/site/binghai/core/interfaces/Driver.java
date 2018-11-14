package site.binghai.core.interfaces;

import site.binghai.framework.entity.OptResult;
import site.binghai.core.entity.ConnConfig;

import java.sql.Connection;

public interface Driver {
    OptResult<Connection> init(ConnConfig connConfig);
    OptResult<Boolean> close(Connection connection);
}

