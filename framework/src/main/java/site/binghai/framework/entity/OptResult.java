package site.binghai.framework.entity;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.omg.SendingContext.RunTime;

import java.util.function.Consumer;

@Data
public class OptResult<T> {
    private Boolean succeed;
    private String message;
    private Throwable exception;
    private T result;

    public OptResult() {
        succeed = Boolean.TRUE;
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
            throw new RuntimeException("no object in the OptResult!");
        }
        return result;
    }

    public void ifPresent(Consumer<T> consumer) {
        if (result != null && succeed) {
            consumer.accept(result);
        }
    }

    public void assertOk() {
        if(!succeed || exception != null){
            throw new RuntimeException("not ok for OptResult");
        }
    }
}
