package site.binghai.core.core;

import site.binghai.core.def.XCousumer;
import site.binghai.framework.entity.Result;

public class BaseProcess {
    protected Result process(Result result, XCousumer cousumer) {
        try {
            cousumer.accept(result);
            return result;
        } catch (Exception e) {
            result.setException(e);
        }
        return result;
    }
}
