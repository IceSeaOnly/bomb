package site.binghai.core.drivers;

import org.springframework.stereotype.Component;
import site.binghai.core.def.Driver;
import site.binghai.framework.entity.Result;
import site.binghai.core.entity.DataBaseConfig;
import site.binghai.framework.utils.BaseBean;

import java.sql.*;

@Component
public class MysqlDriverImpl extends BaseBean implements Driver {
    private static final String BASE_URL = "jdbc:mysql://";
    private static final String DRIVER_NAME = "com.mysql.jdbc.Driver";

    private Connection _init_(DataBaseConfig config) throws Exception {
        Connection conn;
        String url = BASE_URL + config.getHost() + ":" + config.getPort() + "/" + config.getDbName();
        Class.forName(DRIVER_NAME);
        conn = DriverManager.getConnection(url, config.getUser(), config.getPass());
        if (!conn.isClosed()) {
            return conn;
        }
        throw new Exception("can't establish conn to " + url);
    }

    @Override
    public Result<Connection> init(DataBaseConfig cfg) {
        Result<Connection> ret = new Result();

        try {
            Connection conn = _init_(cfg);
            ret.setResult(conn);
        } catch (Exception e) {
            ret.setException(e);
            logger.error("mysql connection init failed! params:{}", cfg, e);
        }

        return ret;
    }

    @Override
    public Result close(Connection connection) {
        Result<Boolean> ret = new Result();
        try {
            connection.close();
        } catch (SQLException e) {
            ret.setException(e);
            logger.info("mysql connection close failed!", e);
        }
        return ret;
    }
}
