package site.binghai.framework.entity;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.function.Consumer;

@Data
public class Result<T> {
    private Boolean succeed;
    private String message;
    private Throwable exception;
    private T result;

    public Result() {
        this.succeed = Boolean.TRUE;
    }

    public Result(T data) {
        this.succeed = Boolean.TRUE;
        this.result = data;
    }

    public Result(Boolean succeed, Throwable exception, T result) {
        this(succeed, exception.getMessage(), exception, result);
    }

    public Result(Boolean succeed, String message, Throwable exception, T result) {
        this.succeed = succeed;
        this.message = message;
        this.exception = exception;
        this.result = result;
    }

    public void setException(Throwable exception) {
        this.exception = exception;
        this.succeed = Boolean.FALSE;
        if (StringUtils.isBlank(message)) {
            message = exception.getMessage();
        }
    }

    public T getResult() {
        if (result == null) {
            throw new RuntimeException("no object in the Result!");
        }
        return result;
    }

    public void ifPresent(Consumer<T> consumer) {
        if (result != null && succeed) {
            consumer.accept(result);
        }
    }

    public void assertOk() {
        if (!succeed || exception != null) {
            throw new RuntimeException("not ok for Result");
        }
    }
}
