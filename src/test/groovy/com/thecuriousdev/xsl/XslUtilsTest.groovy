package com.thecuriousdev.xsl

import org.junit.Test

import static org.junit.Assert.*

class XslUtilsTest {

    @Test
    def void testCountryTransformWithXMLUnit() {
        XslTestHelper.execute(
                XslUtils.loadXslFromFile("xsl/TransformCountries.xsl"),
                XslUtils.loadXmlFromFile("xml/countrylist.xml"),
                XslUtils.loadXmlFromFile("xml/countrylistExpected.xml")
        )
    }

    @Test
    def void testCountryTransformWithCountOfOccurrences() {
        String result = XslUtils.transformXmlWithXsl(
                XslUtils.loadXmlFromFile("xml/countrylist.xml"),
                XslUtils.loadXslFromFile("xsl/TransformCountries.xsl"))
        assertNotNull(result)
        println result

        //check that the transform has produced the 2 expected Country elements
        assertEquals(2, XslResultUtils.countOccurrencesWithXpath(result, "//Country"))

        //check that the transform has produced the 2 expected Capital elements
        assertEquals(2, XslResultUtils.countOccurrencesWithXpath(result, "//Country/Capital"))
    }

    @Test
    def void testCountryTransformWithElementValue() {
        String result = XslUtils.transformXmlWithXsl(
                XslUtils.loadXmlFromFile("xml/countrylist.xml"),
                XslUtils.loadXslFromFile("xsl/TransformCountries.xsl"))
        assertNotNull(result)
        println result

        //check that one of the expected Capital elements contains 'Canberra'
        assertTrue(result.contains("<Capital>Canberra</Capital>"))  // <-- old way
        assertTrue(XslResultUtils.checkElementValueExists(result, "//Country/Capital", "Canberra")) // <-- better way

        //check that we're getting a negative when we should be
        assertFalse(XslResultUtils.checkElementValueExists(result, "//Capital", "Sydney"))
    }

    @Test
    def void testCountryTransform() {
        String result = XslUtils.transformXmlWithXsl(
                XslUtils.loadXmlFromFile("xml/countrylist.xml"),
                XslUtils.loadXslFromFile("xsl/TransformCountries.xsl"))
        assertNotNull(result)
        println result

        //check that the transform has produced the 2 expected Capital elements
        assertEquals(2, XslResultUtils.countOccurrencesWithXpath(result, "//Country/Capital"))

        //check that one of the expected Capital elements contains 'Canberra'
        assertTrue(result.contains("<Capital>Canberra</Capital>"))  // <-- old way
        assertTrue(XslResultUtils.checkElementValueExists(result, "//Country/Capital", "Canberra")) // <-- better way

        //check that the number of capital elements matches the number of country elements
        assertEquals(XslResultUtils.countOccurrencesWithXpath(result, "//Country"),
                XslResultUtils.countOccurrencesWithXpath(result, "//Capital"))

        //check that a particular country contains correct capital
        assertTrue(XslResultUtils.checkElementValueExists(result, "//Country[Name='Australia']/Capital", "Canberra"))
        //alternate more 'xpathy' way
        assertEquals(1, XslResultUtils.countOccurrencesWithXpath(result,
                "/Countries/Country[Name='Australia' and Capital='Canberra']"))

        //check countries are ordered as expected
        assertEquals(1, XslResultUtils.countOccurrencesWithXpath(result, "/Countries/Country[1][starts-with(Name, 'Antigua')]"))
        assertEquals(1, XslResultUtils.countOccurrencesWithXpath(result, "//Country[2][Name='Australia']"))

        //check we only get the exact two TLDs as expected
        assertEquals(0, XslResultUtils.countOccurrencesWithXpath(result, "//Country[TLD!='.au' and TLD!='.ag']"))
        assertEquals(2, XslResultUtils.countOccurrencesWithXpath(result, "//Country[TLD='.au' or TLD='.ag']"))
    }

    @Test
    def void testCountryTransformSchemaValidateSuccess() {
        String schemaLocation = "xsd/Countries.xsd"
        String result = XslUtils.transformXmlWithXsl(
                XslUtils.loadXmlFromFile("xml/countrylist.xml"),
                XslUtils.loadXslFromFile("xsl/TransformCountries.xsl")
        )

        assertNotNull(result)
        assertEquals(0, XslResultUtils.validateXmlWithSchema(result, schemaLocation).size())
    }

    @Test
    def void testCountryTransformSchemaValidateFail() {
        String schemaLocation = "xsd/Countries.xsd"
        String result = XslUtils.transformXmlWithXsl(
                XslUtils.loadXmlFromFile("xml/countrylist.xml"),
                XslUtils.loadXslFromFile("xsl/TransformCountriesBadFormat.xsl")
        )

        assertNotNull(result)
        assertEquals(2, XslResultUtils.validateXmlWithSchema(result, schemaLocation).size())
    }
}