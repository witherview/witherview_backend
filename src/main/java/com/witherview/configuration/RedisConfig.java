package com.witherview.configuration;

import com.witherview.chat.RedisSubscriber;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@RequiredArgsConstructor
@Configuration
public class RedisConfig {
    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(redisHost, redisPort);
    }

    @Bean
    public ChannelTopic studyRoomChannelTopic() {
        return new ChannelTopic("studyRoomChat");
    }

    @Bean
    public ChannelTopic feedBackChannelTopic() {
        return new ChannelTopic("feedBackChat");
    }

    // redis에 발행된 메시지 처리를 위한 리스너 설정
    @Bean
    public RedisMessageListenerContainer redisMessageListener(RedisConnectionFactory connectionFactory,
                                                              MessageListenerAdapter chatListenerAdapter,
                                                              MessageListenerAdapter feedBackListenerAdapter,
                                                              ChannelTopic studyRoomChannelTopic,
                                                              ChannelTopic feedBackChannelTopic) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(chatListenerAdapter, studyRoomChannelTopic);
        container.addMessageListener(feedBackListenerAdapter, feedBackChannelTopic);
        return container;
    }

    // 실제 메시지를 처리하는 subscriber 설정 추가
    @Bean
    public MessageListenerAdapter chatListenerAdapter(RedisSubscriber subscriber) {
        return new MessageListenerAdapter(subscriber, "sendChat");
    }

    @Bean
    public MessageListenerAdapter feedBackListenerAdapter(RedisSubscriber subscriber) {
        return new MessageListenerAdapter(subscriber, "sendFeedback");
    }

    @Bean
    public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String,Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());

        return redisTemplate;
    }
}
