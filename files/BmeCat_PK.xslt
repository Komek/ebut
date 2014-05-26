<?xml version="1.0" encoding="UTF-8"?>
<?altova_samplexml articles_bmecat_new_catalog_1_2_simple_V0.96.xml?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:noNamespaceSchemaLocation="Schemata/Aufgabe1_XMLSchema.xsd">
	<xsl:output method="xml" version="1.0" encoding="UTF-8"
		indent="yes" />
	<!-- Default Template -->
	<xsl:template match="/">
		<Produktkatalog	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
			xsi:noNamespaceSchemaLocation="Schemata/Aufgabe1_XMLSchema.xsd">
			<xsl:attribute name="Generierungsdatum">2013-12-12</xsl:attribute>
			<xsl:attribute name="Beschreibung">
				<xsl:value-of select="BMECAT/HEADER/CATALOG/CATALOG_NAME" />
			</xsl:attribute>
			<xsl:attribute name="ErzeugerId">Hans Peter</xsl:attribute>
			<xsl:attribute name="Generierungsdatum">2013-12-12</xsl:attribute>
			<xsl:apply-templates select="BMECAT/T_NEW_CATALOG/ARTICLE" />
		</Produktkatalog>
	</xsl:template>
	<xsl:template match="BMECAT/T_NEW_CATALOG/ARTICLE">
		<Artikel Artikelnummer="{SUPPLIER_AID}">
			<Kurzbeschreibung>
				<xsl:value-of select="ARTICLE_DETAILS/DESCRIPTION_SHORT" />
			</Kurzbeschreibung>
			<Langbeschreibung>
				<xsl:value-of select="ARTICLE_DETAILS/DESCRIPTION_LONG" />
			</Langbeschreibung>
			<!--Das Vereinfachte BMEcat erlaubt nur Nettopreise -->
			<Nettopreise>
				<xsl:apply-templates select="ARTICLE_PRICE_DETAILS" />
			</Nettopreise>
			<xsl:if test="ARTICLE_REFERENCE/@type='accessories'">
				<Zubehoerartikel Artikelnummer="{ARTICLE_REFERENCE/ART_ID_TO}" />
			</xsl:if>
			<xsl:if test="ARTICLE_DETAILS/EAN">
				<sonstArtikelNr sonstArtikelnummerTyp="EAN">
					<xsl:value-of select="ARTICLE_DETAILS/EAN" />
				</sonstArtikelNr>
			</xsl:if>
		</Artikel>
	</xsl:template>
	<xsl:template match="ARTICLE_PRICE_DETAILS">
		<xsl:apply-templates select="ARTICLE_PRICE" />
	</xsl:template>
	<xsl:template match="ARTICLE_PRICE">
		<!--Im PK ist pro Preis nur ein Land erlaubt -->
		<xsl:for-each select="TERRITORY">
			<Preis Waehrung="{../PRICE_CURRENCY}">
				<xsl:choose>
					<xsl:when test="../../../ARTICLE_ORDER_DETAILS/ORDER_UNIT/text()='PK'">
						<xsl:choose>
							<xsl:when
								test="../../../ARTICLE_ORDER_DETAILS/CONTENT_UNIT/text()='C62'">
								<Betrag>
									<xsl:value-of
										select="../PRICE_AMOUNT/text() div ../../../ARTICLE_ORDER_DETAILS/NO_CU_PER_OU/text()" />
								</Betrag>
							</xsl:when>
							<xsl:otherwise>
								<xsl:comment>
									Achtung! Produkteinheit konnte für diesen Preis nicht ermittelt
									werden
								</xsl:comment>
								<Betrag>
									<xsl:value-of select="PRICE_AMOUNT" />
								</Betrag>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:when>
					<xsl:when test="../../../ARTICLE_ORDER_DETAILS/ORDER_UNIT/text()='C62'">
						<Betrag>
							<xsl:value-of select="../PRICE_AMOUNT" />
						</Betrag>
					</xsl:when>
					<xsl:otherwise>
						<xsl:comment>
							Achtung! Produkteinheit konnte für diesen Preis nicht ermittelt
							werden
						</xsl:comment>
						<Betrag>
							<xsl:value-of select="../PRICE_AMOUNT" />
						</Betrag>
					</xsl:otherwise>
				</xsl:choose>
				<Land>
					<xsl:value-of select="text()" />
				</Land>
				<Steuersatz>
					<!--Im PK wird der Steuersatz in Prozent angegeben -->
					<xsl:value-of select="../TAX/text() * 100" />
				</Steuersatz>
			</Preis>
		</xsl:for-each>
	</xsl:template>
</xsl:stylesheet>
