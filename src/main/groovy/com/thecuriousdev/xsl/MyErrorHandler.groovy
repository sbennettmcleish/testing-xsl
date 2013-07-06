package com.thecuriousdev.xsl

import org.xml.sax.ErrorHandler
import org.xml.sax.SAXException
import org.xml.sax.SAXParseException

class MyErrorHandler implements ErrorHandler {
    List<String> errorsList

    @Override
    void warning(SAXParseException exception) throws SAXException {
        //don't care
    }

    @Override
    void error(SAXParseException exception) throws SAXException {
        if (errorsList == null) {
            errorsList = new ArrayList<String>()
        }

        errorsList << "Line ${exception.getLineNumber()}: ${exception.getMessage()}"
    }

    @Override
    void fatalError(SAXParseException exception) throws SAXException {
        //don't care
    }

    protected List<String> getErrors() {
        if (errorsList == null) {
            errorsList = new ArrayList<String>()
        }

        return errorsList
    }
}
