package com.thecuriousdev.xsl

import org.w3c.dom.Document
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import org.xml.sax.InputSource
import org.xml.sax.SAXException

import javax.xml.XMLConstants
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.stream.StreamSource
import javax.xml.validation.Schema
import javax.xml.validation.SchemaFactory
import javax.xml.validation.Validator
import javax.xml.xpath.XPath
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathExpression
import javax.xml.xpath.XPathFactory

/**
 * A utility class of various helper methods to make unit testing easier.
 */
class XslResultUtils {

    public static Integer countOccurrencesWithXpath(String xmlDocument, String xpath) {
        try {
            Document resultDoc = loadDocumentFromString(xmlDocument)

            // run XPATH on provided XML
            XPath theXpath = XPathFactory.newInstance().newXPath()
            String xpathExp = "count(" + xpath + ")"
            XPathExpression expr = theXpath.compile(xpathExp)
            Number count = (Number) expr.evaluate(resultDoc, XPathConstants.NUMBER)
            println(xpathExp + " = " + count.intValue())

            return count.intValue()
        } catch (Exception e) {
            println(e.getMessage())
        }

        return -1
    }

    public static boolean checkElementValueExists(String xmlDocument, String xpath, String expectedVal) {
        try {
            Document resultDoc = loadDocumentFromString(xmlDocument)
            boolean found = false

            // run XPATH on provided XML
            XPath theXpath = XPathFactory.newInstance().newXPath()
            String xpathExp = xpath.startsWith("//") ? "${xpath}" : "//${xpath}"
            XPathExpression expr = theXpath.compile(xpathExp)
            NodeList nodeSet = (NodeList)expr.evaluate(resultDoc, XPathConstants.NODESET)

            // loop through nodeSet and check that an expected value exists
            nodeSet.each { Node node ->
                if (expectedVal.equals(node.getTextContent())) {
                    found = true
                }
            }

            return found
        } catch (Exception e) {
            println(e.getMessage())
            return false
        }
    }

    public static List<String> validateXmlWithSchema(String xmlDocument, String schemaPath) {
        StreamSource sourceXml = new StreamSource(new StringReader(xmlDocument))
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
        Schema schema = sf.newSchema(XslUtils.loadXslFromFile(schemaPath))
        Validator validator = schema.newValidator()
        MyErrorHandler errorHandler = new MyErrorHandler()

        try {
            validator.setErrorHandler(errorHandler)
            validator.validate(sourceXml);
        } catch (SAXException e) {
            e.printStackTrace()
        }

        List<String> errorsList = errorHandler.getErrors()
        errorsList.each { println it }

        return errorsList
    }

    private static Document loadDocumentFromString(String xmlDocument) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance()
        dbf.setNamespaceAware(true)
        Document resultDoc = dbf.newDocumentBuilder().parse(new InputSource(new StringReader(xmlDocument)))

        return resultDoc
    }
}
