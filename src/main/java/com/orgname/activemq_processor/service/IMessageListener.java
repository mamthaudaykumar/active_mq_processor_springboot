package com.orgname.activemq_processor.service;

import jakarta.jms.Message;

public interface IMessageListener<T> {
  void receiveMessage(T message);
}
