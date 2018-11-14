package site.binghai.core.core.impl;

import org.springframework.stereotype.Service;
import site.binghai.core.core.Client;
import site.binghai.core.core.impl.MysqlClient;
import site.binghai.core.drivers.MysqlDriverImpl;
import site.binghai.core.entity.ConnConfig;
import site.binghai.core.interfaces.Driver;
import site.binghai.framework.entity.OptResult;
import site.binghai.framework.service.AbastractMultiKVCacheService;
import site.binghai.core.enums.DriverType;

import java.sql.Connection;

@Service
public class ClientManager extends AbastractMultiKVCacheService<ConnConfig, Client> {

    @Override
    protected long setExpiredSecs() {
        return -1L;
    }

    @Override
    protected Client load(ConnConfig cfg) {
        DriverType type = DriverType.valueOf(cfg.getDriveType());
        OptResult<Connection> ret = null;
        Client client = null;

        switch (type) {
            case MYSQL:
                Driver driver = new MysqlDriverImpl();
                ret = driver.init(cfg);
                ret.assertOk();
                return new MysqlClient(ret.getResult());
            default:
                logger.error("no suitable client for this config:{}",cfg);
                throw new RuntimeException("no suitable client for this config!");
        }
    }
}
