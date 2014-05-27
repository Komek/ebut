<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns="http://www.w3.org/1999/xhtml"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
	<xsl:output method="html" version="4.0" indent="yes"
		encoding="iso-8859-1" doctype-public="-//W3C//DTD XHTML 1.1//EN"
		doctype-system="http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd" />
	<!-- allgemeines Template -->
	<xsl:template match="/">
		<html>
			<head>
				<title>Product catalog</title>
			</head>
			<body>
				<h1>Product catalog</h1>
				<xsl:apply-templates select="Produktkatalog" />
			</body>
		</html>
	</xsl:template>
	<!-- Produktkatalog -->
	<xsl:template match="Produktkatalog">
		<p>
			Generated:
			<xsl:value-of select="@Generierungsdatum" />, Producer:
			<xsl:value-of select="@ErzeugerId" />,
			<xsl:value-of select="@Beschreibung" />
		</p>
		<ol>
			<xsl:apply-templates select="Artikel" />
		</ol>
	</xsl:template>
	<!-- Artikel -->
	<xsl:template match="Produktkatalog/Artikel">
		<li>
			<p>
				<label>Article No: </label>
				<xsl:value-of select="@Artikelnummer" />
				<br></br>
				<label>Description: </label>
				<xsl:value-of select="Kurzbeschreibung" />
				<br></br>
				<label>Long description: </label>
				<xsl:apply-templates select="Langbeschreibung" />
				<br></br>
				<xsl:apply-templates select="Nettopreise | Bruttopreise" />
			</p>
			<hr/>
		</li>
	</xsl:template>
	<!-- Langbeschreibung allgemein -->
	<xsl:template match="Langbeschreibung">
		<xsl:apply-templates />
	</xsl:template>
	<!-- Text Elemente in Langbeschreibung -->
	<xsl:template match="Langbeschreibung/text()">
		<xsl:copy>
			<xsl:copy-of select="." />
			<xsl:apply-templates />
		</xsl:copy>
	</xsl:template>
	<!-- <wichtig> Tags in Langbeschreibung -->
	<xsl:template match="Langbeschreibung//*">
		<em>
			<xsl:value-of select="." />
		</em>
	</xsl:template>
	<xsl:template match="Nettopreise | Bruttopreise">
		<label>Country: </label>
		<select name="country">
			<optgroup label="choose country">
				<xsl:apply-templates select="Preis" />
			</optgroup>
		</select>
	</xsl:template>
	<!-- Preis -->
	<xsl:template match="Preis">
		<option>
			<xsl:value-of select="Land" />
			, Tax:
			<xsl:value-of select="Steuersatz" />
			%, Price:
			<xsl:value-of select="Betrag" />
			<xsl:value-of select="@Waehrung" />
		</option>
	</xsl:template>
	<xsl:template match="sonstArtikelNr">
		<xsl:value-of select="@sonstArtikelnummerTyp" />
		:
		<xsl:value-of select="." />
	</xsl:template>
</xsl:stylesheet>
