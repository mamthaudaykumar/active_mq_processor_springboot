package com.orgname.activemq_processor.controller;

import com.orgname.activemq_processor.model.MessageRequest;
import com.orgname.activemq_processor.model.MessageResponse;
import com.orgname.activemq_processor.service.ActiveMqXmlMessageProcessorServiceImpl;
import com.orgname.activemq_processor.service.IMessageProcessorXMLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageProcessorController implements com.orgname.activemq_processor.api.MessagingApi {

  private final IMessageProcessorXMLService messageProcessorService;

  @Autowired
  public MessageProcessorController(
      ActiveMqXmlMessageProcessorServiceImpl activeMQMessageProcessorService) {
    this.messageProcessorService = activeMQMessageProcessorService;
  }

  public ResponseEntity<MessageResponse> sendMessage(MessageRequest messageRequest) {
    messageProcessorService.sendMessage(messageRequest, messageRequest.getCorrelationId());
    return ResponseEntity.accepted()
        .body(
            new MessageResponse()
                .message(
                    String.format(
                        "Message sent to queue with correlationId: {} !!!",
                        messageRequest.getCorrelationId())));
  }
}
