package site.binghai.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.binghai.core.core.Client;
import site.binghai.core.core.Table;
import site.binghai.core.service.ConnConfigService;
import site.binghai.core.core.impl.ClientManager;
import site.binghai.core.entity.ConnConfig;
import site.binghai.framework.controller.BaseController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/sql/test/")
public class SqlTest extends BaseController {
    @Autowired
    private ConnConfigService connConfigService;
    @Autowired
    private ClientManager clientManager;

    @GetMapping("readTables")
    public Object readTables(@RequestParam Long id, String table) {
        ConnConfig cfg = connConfigService.findById(id);
        Client client = clientManager.get(cfg);

        List<String> ret = new ArrayList<>();
        try {
            client.getTableNames().ifPresent(v -> ret.addAll(v));
            if (table == null) {
                return success(ret, null);
            }

            return client.getTable(table).orElseGet(null);
        } catch (Exception e) {
            return fail(e.getMessage());
        }
    }

    @GetMapping("tableCloumn")
    public Object tableCloumn(@RequestParam Long id, String table) {
        ConnConfig cfg = connConfigService.findById(id);
        Client client = clientManager.get(cfg);

        Table tab = client.getTable(table).orElse(ret -> {
            logger.error("table [{}] not found", table);
            return null;
        });
        if (tab == null) {
            return fail("no such table called " + table);
        }
        return success(tab.getColumnNameList().orElse(null), null);
    }
}
