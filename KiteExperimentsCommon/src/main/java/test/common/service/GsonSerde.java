package test.common.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * @author s.smitienko
 */
public abstract class GsonSerde<T> implements Serde<T> {

    private Gson gson;


    protected GsonSerde() {
        this.gson = new GsonBuilder().setLenient().create();
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public void close() {

    }

    @Override
    public Serializer<T> serializer() {
        return new Serializer<T>() {
            @Override
            public void configure(Map<String, ?> configs, boolean isKey) {

            }

            @Override
            public byte[] serialize(String topic, T data) {
                return gson.toJson(data).getBytes();
            }

            @Override
            public void close() {

            }
        };

    }

    @Override
    public Deserializer<T> deserializer() {
        return new Deserializer<T>() {

            @Override
            public void configure(Map<String, ?> configs, boolean isKey) {
                //
            }

            @Override
            public T deserialize(String topic, byte[] data) {
                return gson.fromJson(new String(data), getType());
            }

            @Override
            public void close() {
            }
        };
    }

    public abstract Type getType();
}
