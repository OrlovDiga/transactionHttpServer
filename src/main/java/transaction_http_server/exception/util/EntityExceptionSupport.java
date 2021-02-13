package transaction_http_server.exception.util;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public final class EntityExceptionSupport {

    public static String generateMessage(String entity, Map<String, String> searchParams, String msg) {
        return StringUtils.capitalize(entity) +
                msg +
                searchParams;
    }

    public static <K, V> Map<K, V> toMap(
            Class<K> keyType, Class<V> valueType, Object... entries) {
        if (entries.length % 2 == 1)
            throw new IllegalArgumentException("Невалидная запись параметров. Подсказка: должно быть 2n агументов для параметров поиска");
        return IntStream.range(0, entries.length / 2).map(i -> i * 2)
                .collect(HashMap::new,
                        (m, i) -> m.put(keyType.cast(entries[i]), valueType.cast(entries[i + 1])),
                        Map::putAll);
    }
}
