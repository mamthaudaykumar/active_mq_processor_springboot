package com.orgname.activemq_processor.util;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

public class XmlUtility {

  private static final Logger logger = LoggerFactory.getLogger(XmlUtility.class);
  private static final XmlMapper xmlMapper = new XmlMapper();

  public static String convertObjectToXml(Object obj) throws Exception {
    return xmlMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
  }

  /**
   * Validates if the input XML string is well-formed.
   *
   * @param xml the XML string to validate
   * @return true if valid, false if invalid
   */
  public static boolean validateXml(String xml) {
    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      factory.setNamespaceAware(true);
      DocumentBuilder builder = factory.newDocumentBuilder();
      builder.parse(new InputSource(new StringReader(xml))); // will throw if invalid
      return true; // well-formed XML
    } catch (Exception e) {
      logger.error("Invalid XML: {}", e.getMessage());
      return false;
    }
  }
}
