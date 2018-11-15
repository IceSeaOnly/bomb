package site.binghai.core.def;

import site.binghai.framework.entity.Result;

@FunctionalInterface
public interface XCousumer {
    Result accept(Result result) throws Exception;
}
