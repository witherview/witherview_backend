package com.witherview.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import org.springframework.security.messaging.access.intercept.ChannelSecurityInterceptor;
import org.springframework.security.messaging.access.intercept.MessageSecurityMetadataSource;

//@Configuration
//@Order(Ordered.HIGHEST_PRECEDENCE + 99)
//public class WebSocketAuthorizationConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {
//
//    @Override
//    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
//        messages.anyMessage().authenticated();
//    }
//
//    @Override
//    protected boolean sameOriginDisabled() {
//        return true;
//    }
//}
