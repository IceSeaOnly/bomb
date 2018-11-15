package site.binghai.core.def;

import site.binghai.framework.entity.Result;

@FunctionalInterface
public interface XCousumer {
    void accept(Result result) throws Exception;
}
