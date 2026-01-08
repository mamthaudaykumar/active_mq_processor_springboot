package com.orgname.activemq_processor.service;

import com.orgname.activemq_processor.exception.MessageSendException;
import com.orgname.activemq_processor.util.XmlUtility;
import jakarta.jms.TextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class ActiveMqXmlMessageProcessorServiceImpl<T> implements IMessageProcessorXMLService<T> {

  private static final Logger logger =
      LoggerFactory.getLogger(ActiveMqXmlMessageProcessorServiceImpl.class);

  private final JmsTemplate jmsTemplate;

  // Inject the queue name from application.yml
  @Value("${app.messaging.queue-name}")
  private String queueName;

  @Autowired
  ActiveMqXmlMessageProcessorServiceImpl(JmsTemplate jmsTemplate) {

    this.jmsTemplate = jmsTemplate;
  }

  /**
   * Sends a message to the configured ActiveMQ queue.
   *
   * @param message XML message body
   * @param correlationId unique identifier for traceability
   * @throws MessageSendException if sending fails
   */
  @Override
  public void sendMessage(T message, String correlationId) throws MessageSendException {
    try {
      logger.info("Sending message to queue: {} with correlationId: {}", queueName, correlationId);

      // Convert object to XML
      String xmlMessage = XmlUtility.convertObjectToXml(message);

      jmsTemplate.send(
          queueName,
          session -> {
            // Send the XML string, not the object
            TextMessage textMessage = session.createTextMessage(xmlMessage);
            textMessage.setJMSCorrelationID(correlationId);
            return textMessage;
          });

      logger.info(
          "Message successfully sent to queue [{}] with correlationId [{}]",
          queueName,
          correlationId);

    } catch (Exception e) {
      logger.error("Failed to send message to queue [{}]: {}", queueName, e.getMessage(), e);
      throw new MessageSendException("Failed to send message to ActiveMQ queue: " + e.getMessage());
    }
  }
}
