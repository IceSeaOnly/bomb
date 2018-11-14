package site.binghai.core.drivers;

import org.springframework.stereotype.Component;
import site.binghai.core.interfaces.Driver;
import site.binghai.framework.entity.OptResult;
import site.binghai.core.entity.ConnConfig;
import site.binghai.framework.utils.BaseBean;

import java.sql.*;

@Component
public class MysqlDriverImpl extends BaseBean implements Driver {
    private static final String BASE_URL = "jdbc:mysql://";
    private static final String DRIVER_NAME = "com.mysql.jdbc.Driver";

    private Connection _init_(ConnConfig config) throws Exception {
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
    public OptResult<Connection> init(ConnConfig cfg) {
        OptResult<Connection> ret = new OptResult();

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
    public OptResult close(Connection connection) {
        OptResult<Boolean> ret = new OptResult();
        try {
            connection.close();
        } catch (SQLException e) {
            ret.setException(e);
            logger.info("mysql connection close failed!", e);
        }
        return ret;
    }
}
