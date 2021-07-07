package com.witherview.chat.rabbitMQ;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    @Bean
    Queue chatQueue() {
        return new Queue("chat-queue", false);
    }

    @Bean
    Queue feedbackQueue() {
        return new Queue("feedback-queue", false);
    }

    @Bean
    TopicExchange topic() {
        return new TopicExchange("topic");
    }

    @Bean
    Binding bindingChat(Queue chatQueue, TopicExchange topic) {
        return BindingBuilder.bind(chatQueue).to(topic).with("chat.*");
    }

    @Bean
    Binding bindingFeedback(Queue feedbackQueue, TopicExchange topic) {
        return BindingBuilder.bind(feedbackQueue).to(topic).with("feedback.*");
    }

    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                  MessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }

    @Bean
    MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
