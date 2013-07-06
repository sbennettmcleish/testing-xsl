package com.thecuriousdev.xsl

import org.custommonkey.xmlunit.Difference
import org.w3c.dom.Document

import javax.xml.transform.Source
import org.custommonkey.xmlunit.DetailedDiff
import org.custommonkey.xmlunit.Diff
import org.custommonkey.xmlunit.XMLUnit

import static org.junit.Assert.assertTrue

class XslTestHelper {
    static void execute(Source xsl, String inputXml, String expectedXml) throws Exception {
        //important, makes the diffs ignore the whitespace between XML elements
        XMLUnit.setIgnoreWhitespace(true)

        String actualXml = XslUtils.transformXmlWithXsl(inputXml, xsl)
        Document expectedDocument = XMLUnit.buildDocument(XMLUnit.newControlParser(), new StringReader(expectedXml))
        Document actualDocument = XMLUnit.buildDocument(XMLUnit.newControlParser(), new StringReader(actualXml))

        Diff diff = new Diff(expectedDocument, actualDocument)
        DetailedDiff detailDiff = new DetailedDiff(diff)
        detailDiff.overrideElementQualifier(null)

        XslTestHelper.assertValid(detailDiff)
    }

    static void assertValid(DetailedDiff detailedDiff) {
        //use junit assertion to check the xsl output is the same as that expected
        assertTrue(
            XslTestHelper.printDifferences(detailedDiff.getAllDifferences()),
            (detailedDiff.getAllDifferences() != null
                && detailedDiff.getAllDifferences().size() == 0
                && detailedDiff.similar()
                && detailedDiff.identical()
            )
        )
    }

    private static String printDifferences(List<Difference> diffs) {
        //pull all diffs together to display in junit assertion fail
        def sb = new StringBuilder()
        diffs.each { d ->
            sb << "${d}\n"
        }

        return sb.toString()
    }
}
