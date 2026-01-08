package com.orgname.activemq_processor.config;

import jakarta.jms.Queue;
import jakarta.jms.Session;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;

@Configuration
public class ActiveMQConfig {

  @Value("${app.messaging.queue-name}")
  private String queueName;

  @Value("${spring.activemq.broker-url}")
  private String brokerUrl;

  @Value("${spring.activemq.user}")
  private String username;

  @Value("${spring.activemq.password}")
  private String password;

  @Bean
  public Queue messageQueue() {
    // uses property value
    return new ActiveMQQueue(queueName);
  }

  @Bean
  public ActiveMQConnectionFactory connectionFactory() {
    ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
    factory.setBrokerURL(brokerUrl);
    factory.setUserName(username);
    factory.setPassword(password);
    return factory;
  }

  @Bean
  public CachingConnectionFactory cachingConnectionFactory(ActiveMQConnectionFactory factory) {
    return new CachingConnectionFactory(factory);
  }

  @Bean
  public JmsTemplate jmsTemplate(CachingConnectionFactory factory, Queue queue) {
    JmsTemplate jmsTemplate = new JmsTemplate(factory);
    jmsTemplate.setDefaultDestination(queue);
    jmsTemplate.setPubSubDomain(false);
    return jmsTemplate;
  }

  @Bean
  public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(
      CachingConnectionFactory connectionFactory) {
    DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
    factory.setConnectionFactory(connectionFactory);
    factory.setConcurrency("1-3"); // adjust threads
    factory.setPubSubDomain(false); // false = queue, true = topic
    factory.setSessionAcknowledgeMode(Session.AUTO_ACKNOWLEDGE);
    return factory;
  }
}
