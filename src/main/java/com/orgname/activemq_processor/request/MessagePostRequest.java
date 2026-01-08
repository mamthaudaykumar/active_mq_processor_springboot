package com.orgname.activemq_processor.request;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "MessageRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class MessagePostRequest {
  @XmlElement private String order;

  @XmlElement private String orderId;

  @XmlElement private String correlationId;
}
