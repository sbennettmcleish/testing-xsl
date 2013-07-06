<?xml version='1.0' encoding='utf-8'?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output encoding="UTF-8" indent="yes" method="xml"/>

    <xsl:template match="/document">
        <xsl:element name="Countries">
            <xsl:for-each select="row">
                <xsl:if test="starts-with(Col1,'A')
                    and Col3='Independent State'
                    and Col8='Dollar'
                    and starts-with(Col13,'.a')">
                    <xsl:element name="Country">
                        <xsl:element name="Name">
                            <xsl:value-of select="Col1"/>
                        </xsl:element>
                    </xsl:element>
                </xsl:if>
            </xsl:for-each>
        </xsl:element>
    </xsl:template>
</xsl:stylesheet>