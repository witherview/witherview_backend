package com.witherview.configuration;

import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapping;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.convert.*;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//@RequiredArgsConstructor
@Configuration
public class MongoDBConfig {
//    private final MongoDatabaseFactory mongoDatabaseFactory;
//    private final MongoMappingContext mongoMappingContext;
    @WritingConverter
    public class LocalDateTimeToStringConverter implements Converter<Instant, String> {
        @Override
        public String convert(Instant source) {
            return source.toString();
        }
    }
    @ReadingConverter
    public class StringToLocalDateTimeConverter implements Converter<String, Instant> {
        @Override
        public Instant convert(String source) {
            return Instant.parse(source);
        }
    }
    @Bean
    public MongoCustomConversions customConversions() {
        List<Converter<?, ?>> converterList =  new ArrayList<>();
        converterList.add(new LocalDateTimeToStringConverter());
        converterList.add(new StringToLocalDateTimeConverter());
        return new MongoCustomConversions(converterList);
    }

    @Bean
    @ConditionalOnMissingBean({MongoConverter.class})
    public MappingMongoConverter mappingMongoConverter(
            MongoDatabaseFactory mongoDatabaseFactory,
            MongoMappingContext mongoMappingContext
            ) {
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(mongoDatabaseFactory);
        MappingMongoConverter converter = new MappingMongoConverter(dbRefResolver, mongoMappingContext);

        // _class가 DB에 자동으로 삽입되는 로직 삭제
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
        converter.afterPropertiesSet();
        return converter;
    }
}
