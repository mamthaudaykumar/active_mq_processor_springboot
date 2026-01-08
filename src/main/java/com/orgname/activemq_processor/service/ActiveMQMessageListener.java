package com.orgname.activemq_processor.service;

import com.orgname.activemq_processor.util.XmlUtility;
import jakarta.jms.TextMessage;
import jakarta.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class ActiveMQMessageListener<T> implements IMessageListener<T> {

  private static final Logger logger = LoggerFactory.getLogger(ActiveMQMessageListener.class);

  @Value("${app.messaging.queue-name}")
  private String queueName;

  /**
   * Listens to messages from the configured queue. ActiveMQ will automatically deliver messages to
   * this method asynchronously.
   */
  @JmsListener(
      id = "activeMQMessageListener",
      destination = "${app.messaging.queue-name}",
      containerFactory = "jmsListenerContainerFactory")
  @Override
  public void receiveMessage(T message) {
    try {
      if (message instanceof TextMessage textMessage) {
        // Extract XML payload
        String payload = textMessage.getText();

        // Extract correlation ID
        String correlationId = textMessage.getJMSCorrelationID();

        // Validate XML
        if (!XmlUtility.validateXml(payload)) {
          logger.warn("Received invalid XML | correlationId: {}", correlationId);
          // Optionally: discard or send to DLQ
          throw new ValidationException("Message payload invalid");
        }

        // Process valid XML
        logger.info("Received valid XML | correlationId: {} | payload: {}", correlationId, payload);

      } else {
        logger.warn("Received unknown message type: {}", message.getClass());
      }
    } catch (Exception e) {
      logger.error("Error processing message", e);
      throw new RuntimeException(e); // for JMS redelivery
    }
  }
}
