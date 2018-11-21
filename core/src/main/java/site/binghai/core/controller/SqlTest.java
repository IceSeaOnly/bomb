package site.binghai.core.controller;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import site.binghai.core.core.Client;
import site.binghai.core.core.Column;
import site.binghai.core.service.ConversationService;
import site.binghai.core.service.DataBaseConfigService;
import site.binghai.core.core.impl.ClientManager;
import site.binghai.core.entity.DataBaseConfig;
import site.binghai.framework.utils.BaseBean;

@Component
public class SqlTest extends BaseBean implements InitializingBean {
    @Autowired
    private DataBaseConfigService dataBaseConfigService;
    @Autowired
    private ClientManager clientManager;
    @Autowired
    private ConversationService conversationService;

    //@Override
    public void _afterPropertiesSet() throws Exception {
        logger.info("\n\n\n");
        DataBaseConfig cfg = dataBaseConfigService.findById(1L);
        Client client = clientManager.get(cfg);


        client.getTableNames().ifPresent(list -> {
            for (String tabelName : list) {
                client.getTable(tabelName).ifPresent(table -> {
                    logger.info("create sql for table {} is :{}", tabelName, table.showCreateSql());
                    table.getColumnNameList().ifPresent(clist -> {
                        for (Column column : clist) {
                            logger.info("column {} of table {} is {}", column.name(), tabelName, column);
                        }
                    });

                });
            }
        });

        logger.info("\n\n\n");
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
