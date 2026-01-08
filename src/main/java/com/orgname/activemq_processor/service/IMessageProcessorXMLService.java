package com.orgname.activemq_processor.service;

public interface IMessageProcessorXMLService<T> {
  void sendMessage(T message, String correlationId);
}
