package org.example;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.StringReader;

public class LoggingEntityResolver implements EntityResolver {

    @Override
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException {
        System.out.println("Attempting to resolve entity: " + systemId);
        return new InputSource(new StringReader(""));
    }
}