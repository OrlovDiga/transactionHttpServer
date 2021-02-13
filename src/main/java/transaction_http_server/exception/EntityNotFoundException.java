package transaction_http_server.exception;

import static transaction_http_server.exception.util.EntityExceptionSupport.generateMessage;
import static transaction_http_server.exception.util.EntityExceptionSupport.toMap;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(Class clazz, String... searchParamsMap) {
        super(generateMessage(clazz.getSimpleName(),
                toMap(String.class, String.class, searchParamsMap), " not found by parameters"));
    }
}
