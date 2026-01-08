package com.orgname.activemq_processor.controller;

import com.orgname.activemq_processor.api.AdminApi;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.config.JmsListenerEndpointRegistry;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController implements AdminApi {

  private final JmsListenerEndpointRegistry registry;

  public AdminController(JmsListenerEndpointRegistry registry) {
    this.registry = registry;
  }

  public ResponseEntity<String> pauseListener() {

    var container = registry.getListenerContainer("activeMQMessageListener");
    if (container != null && container.isRunning()) {
      container.stop();
      return ResponseEntity.ok("Listener stopped successfully");
    }
    return ResponseEntity.ok("Listener already stopped or not found");
  }

  public ResponseEntity<String> resumeListener() {
    var container = registry.getListenerContainer("activeMQMessageListener");
    if (container != null && !container.isRunning()) {
      container.start();
      return ResponseEntity.ok("Listener started successfully");
    }
    return ResponseEntity.ok("Listener already running or not found");
  }

  public ResponseEntity<String> listenerStatus() {
    var container = registry.getListenerContainer("activeMQMessageListener");
    if (container != null) {
      return ResponseEntity.ok("Listener running: " + container.isRunning());
    }
    return ResponseEntity.ok("Listener not found");
  }
}
