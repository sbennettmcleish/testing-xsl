package com.thecuriousdev.xsl

import org.w3c.dom.Document
import org.xml.sax.InputSource
import org.xml.sax.SAXException

import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.ParserConfigurationException
import javax.xml.transform.*
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult
import javax.xml.transform.stream.StreamSource

class XslUtils {
    static String loadXmlFromFile(String filename) throws IOException, ParserConfigurationException, SAXException, TransformerException {
        //get loader and fetch input stream
        ClassLoader loader = Thread.currentThread().getContextClassLoader()
        InputStream xmlInputStream = loader.getResourceAsStream(filename)

        //set up a transformer
        TransformerFactory tf = TransformerFactory.newInstance()
        Transformer transformer = tf.newTransformer()
        //transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8") //do we need this?
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance()
        dbf.setNamespaceAware(true)

        //change input stream into a doc
        Document doc = dbf.newDocumentBuilder().parse(new InputSource(xmlInputStream))

        //transform doc into a string
        StringWriter sw = new StringWriter()
        StreamResult result = new StreamResult(sw)
        DOMSource source = new DOMSource(doc)
        transformer.transform(source, result)

        return sw.toString();
    }

    static Source loadXslFromFile(String filename) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader()
        Source xsl = new StreamSource(
                loader.getResourceAsStream(filename),
                loader.getResource(filename).toString())

        return xsl
    }

    static def String transformXmlWithXsl(String inputXml, Source xsl) {
        //setup transformer factory etc
        TransformerFactory tf = TransformerFactory.newInstance()
        Transformer transformer = tf.newTransformer(xsl)

        //setup doc factory etc
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance()
        dbf.setNamespaceAware(true)
        DocumentBuilder db = dbf.newDocumentBuilder()

        //setup input
        Document sourceDoc = db.parse(new InputSource(new StringReader(inputXml)))

        //transform!
        StreamResult result = new StreamResult(new StringWriter())
        transformer.transform(new DOMSource(sourceDoc), result)

        //return the transformed xml
        return result.getWriter().toString()
    }
}
