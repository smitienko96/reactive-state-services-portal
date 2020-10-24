package test.common.service;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Фабрика мапперов Jackson для использования в возможных сценариях работы приложения
 *
 * @author s.smitienko
 */
public class ObjectMapperFactory {

    private ObjectMapperFactory() {
    }

    /**
     * Создает ObjectMapper для использования при манипуляции с объектами,
     * которые хранятся в локальном хранилище процессоров Kafka Streams
     *
     * @return
     */
    public static ObjectMapper createKafkaStateStoreObjectMapper() {
//        ObjectMapper objectMapper = new ObjectMapper(new SmileFactory());
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.enableDefaultTyping();

        return objectMapper;
    }

    /**
     * Создает ObjectMapper для использования в реактивных контроллерах
     * WebFlux.
     *
     * @return
     */
    public static ObjectMapper createWebFluxGenericObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        return objectMapper;
    }

}
