package org.example;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.example.jackson_beans.JUnitReport;
import org.junit.Test;

import java.io.InputStream;

public class JacksonXMLReaderTest {

    private static final String XEE_ATTACK_FILE = "attack_files/XXE.xml";
    private static final String COERCIVE_ATTACK_FILENAME = "attack_files/coercive-attack.xml";
    private static final String BILLION_LAUGHS_ATTACK_FILENAME = "attack_files/billion-laughs.xml";
    private static final String SCHEMA_POISONING_ATTACK_FILENAME = "attack_files/schema-poisoning.xml";
    private static final String XML_INCLUSION_ATTACK_FILENAME = "attack_files/XML-inclusion.xml";
    private static final String HUGE_PAYLOAD_ATTACK_FILENAME = "attack_files/huge-payload.xml";
    private static final String VALID_JUNIT_XML_FILENAME = "testSuites.xml";
    private static final String INVALID_NUMBER = "attack_files/invalid-number.xml";

    @Test(expected = JsonParseException.class)
    public void shouldThrowExceptionAfterCoerciveAttack() throws Exception {
        InputStream fileSteam = ResourceUtil.readXMLFileFromResources(COERCIVE_ATTACK_FILENAME);
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.readValue(fileSteam, JUnitReport.class);
    }

    @Test(expected = JsonParseException.class)
    public void shouldThrowExceptionAfterXXEAttack() throws Exception {
        InputStream fileSteam = ResourceUtil.readXMLFileFromResources(XEE_ATTACK_FILE);
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.readValue(fileSteam, JUnitReport.class);
    }

    @Test(expected = JsonParseException.class)
    public void shouldThrowExceptionAfterBillionLaughsAttack() throws Exception {
        InputStream fileSteam = ResourceUtil.readXMLFileFromResources(BILLION_LAUGHS_ATTACK_FILENAME);
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.readValue(fileSteam, JUnitReport.class);
    }

    @Test()
    public void shouldThrowExceptionAfterSchemaPoisoningAttack() throws Exception {
        InputStream fileSteam = ResourceUtil.readXMLFileFromResources(SCHEMA_POISONING_ATTACK_FILENAME);
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.readValue(fileSteam, JUnitReport.class);
    }

    @Test()
    public void shouldThrowExceptionAfterXMLInclusionAttack() throws Exception {
        InputStream fileSteam = ResourceUtil.readXMLFileFromResources(XML_INCLUSION_ATTACK_FILENAME);
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.readValue(fileSteam, JUnitReport.class);
    }

    @Test()
    public void shouldThrowExceptionAfterHugePayloadAttack() throws Exception {
        InputStream fileSteam = ResourceUtil.readXMLFileFromResources(HUGE_PAYLOAD_ATTACK_FILENAME);
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.readValue(fileSteam, JUnitReport.class);
    }

    @Test
    public void shouldNotThrowExceptionAfterValidXML() throws Exception {
        InputStream fileSteam = ResourceUtil.readXMLFileFromResources(VALID_JUNIT_XML_FILENAME);
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.readValue(fileSteam, JUnitReport.class);
    }

    @Test(expected = JsonParseException.class)
    public void shouldThrowExceptionAfterInvalidNumber() throws Exception {
        InputStream fileSteam = ResourceUtil.readXMLFileFromResources(INVALID_NUMBER);
        XmlMapper xmlMapper = new XmlMapper();
        JUnitReport report = xmlMapper.readValue(fileSteam, JUnitReport.class);
    }
}