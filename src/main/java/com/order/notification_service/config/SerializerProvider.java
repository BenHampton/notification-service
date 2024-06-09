package com.order.notification_service.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.cfg.CacheProvider;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class SerializerProvider extends DefaultSerializerProvider {

    public SerializerProvider() {
        super();
    }

    public SerializerProvider(SerializerProvider provider, SerializationConfig config, SerializerFactory factory) {
        super(provider, config, factory);
    }

    @Override
    public SerializerProvider createInstance(SerializationConfig config, SerializerFactory factory) {
        return new SerializerProvider(this, config, factory);
    }

    @Override
    public DefaultSerializerProvider withCaches(CacheProvider cacheProvider) {
        return null;
    }

    @Override
    public JsonSerializer<Object> findNullValueSerializer(BeanProperty property) throws JsonMappingException {

        if (property.getType().getRawClass().equals(String.class)) {

            return new StringStdSerializer();

        } else if (property.getType().getRawClass().equals(List.class)) {

            return new JsonSerializer<Object>() {
                @Override
                public void serialize(Object value, JsonGenerator gen, com.fasterxml.jackson.databind.SerializerProvider serializers) throws IOException {
                    gen.writeStartArray();
                    gen.writeEndArray();
                }
            };
        } else {
            return super.findNullValueSerializer(property);
        }
    }
}