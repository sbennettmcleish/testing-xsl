package com.thecuriousdev.xsl

import org.junit.Test

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertTrue

class XslResultUtilsTest {

    @Test
    public void testCountOccurrenceWithXpath() {
        String xml = XslUtils.loadXmlFromFile("xml/countOccurrences.xml")

        assertEquals(2, XslResultUtils.countOccurrencesWithXpath(xml, "//Country/TLD"))
    }

    @Test
    public void testCheckElementValueExists() {
        String xml = XslUtils.loadXmlFromFile("xml/checkElementValueExists.xml")

        assertTrue(XslResultUtils.checkElementValueExists(xml, "//Country/Name", "Australia"))
    }
}
