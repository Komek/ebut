<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns="http://www.w3.org/1999/xhtml"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:noNamespaceSchemaLocation="Schemata/Aufgabe1_XMLSchema.xsd">
	<xsl:output method="xhtml" version="1.0" indent="yes"
		encoding="UTF-8" doctype-public="-//W3C//DTD XHTML 1.1//EN"
		doctype-system="http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd" />
	<xsl:template match="/">
		<html>
			<head>
				<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
				<xsl:apply-templates select="BMECAT/HEADER"/>
			</head>
			<body>
				<h1>Product catalog</h1>
				<xsl:apply-templates select="T_NEW_CATALOG" />
			</body>
		</html>
	</xsl:template>
	<xsl:template match="BMECAT/HEADER">
		<title><xsl:value-of select="CATALOG_NAME"/></title>
	</xsl:template>
	<xsl:template match="T_NEW_CATALOG">
		<ol>
			<xsl:apply-templates select="ARTICLE"/>
		</ol>
	</xsl:template>
	<xsl:template match="ARTICLE">
		<li>
			<p><xsl:apply-templates select="ARTICLE_DETAILS"/></p>
			<p><xsl:apply-templates select="ARTICLE_PRICE_DETAILS"/></p>
		</li>
	</xsl:template>
	<xsl:template match="ARTICLE_DETAILS">
		<!-- em><xsl:value-of select=""/></em--><!-- TODO -->
	</xsl:template>
	<xsl:template match="ARTICLE_PRICE_DETAILS"></xsl:template>


</xsl:stylesheet>
