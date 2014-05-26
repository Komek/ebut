package de.htwg_konstanz.ebus.wholesaler.main;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import de.htwg_konstanz.ebus.framework.wholesaler.api.bo.BOProduct;
import de.htwg_konstanz.ebus.framework.wholesaler.api.bo.BOSalesPrice;
import de.htwg_konstanz.ebus.framework.wholesaler.api.boa.ProductBOA;
import de.htwg_konstanz.ebus.wholesaler.demo.util.Constants.ExportFormat;

/**
 * Exporter Product catalog
 * 
 * @author Stephen & Marco
 * 
 */
public class Exporter {
	private final DocumentBuilderFactory factory = DocumentBuilderFactory
			.newInstance();

	private final ProductBOA productBOA = ProductBOA.getInstance();
	private final TransformerFactory transormfactory = TransformerFactory
			.newInstance();
	private Transformer transformerBMECat;
	private Transformer transformerXHTML;
	private final Properties properties;
	private List<BOProduct> boProducts;
	private ExportFormat format;

	/**
	 * initialize transformer properties, transformers and product list
	 * 
	 * @param shortDescription
	 *            - search String, returns all products if String is empty
	 * @param format
	 *            - format of export file: BMECat or XHTML
	 */
	public Exporter(String shortDescription, ExportFormat format) {
		this.format = format;
		properties = new Properties();
		initProperties();
		if (shortDescription.equals("") || shortDescription.equals(null)) {
			boProducts = productBOA.findAll();
		} else {
			boProducts = productBOA.findByCriteriaLike(
					Constants.SEARCHSTRING_DESCRIPTION_SHORT, "%"
							+ shortDescription + "%");
		}

		try {
			switch (format) {
			case bmecat:
				transformerBMECat = transormfactory
						.newTransformer(new StreamSource(
								Constants.PATH_TO_BMECAT_PK));
				break;
			case xhtml:
				transformerBMECat = transormfactory
						.newTransformer(new StreamSource(
								Constants.PATH_TO_BMECAT_PK));
				transformerXHTML = transormfactory
						.newTransformer(new StreamSource(
								Constants.PATH_TO_PK_XHTML));
				transformerXHTML.setOutputProperties(properties);
				break;
			default:
				break;
			}

			transformerBMECat.setOutputProperties(properties);
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * returns product catalog as BMECat or as XHTML
	 * 
	 * formation to xhtml wont work
	 * 
	 * @return File
	 */
	public File export() throws SAXException, IOException,
			ParserConfigurationException {
		Document doc = createBMEcatDoc();
		File result = null;

		try {
			switch (format) {
			case bmecat:
				result = createBMEcatFile(doc);
				break;
			case xhtml:
				File pk = getDocumentAsProduktkatalog(doc);
				result = getPK_HTML(factory.newDocumentBuilder().parse(pk));
				break;
			default:
				break;
			}

		} catch (TransformerException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * properties for transformers
	 */
	private void initProperties() {
		/* Transformer Properties */

		properties.setProperty(OutputKeys.INDENT, "yes");
		properties.setProperty(OutputKeys.ENCODING, "iso-8859-1");
		properties
				.setProperty("{http://xml.apache.org/xslt}indent-amount", "2");
	}

	// private File createXHTMLcatFile(Document doc) throws TransformerException
	// {
	// DOMSource source = new DOMSource(doc);
	// File file = new File(Constants.BMECAT_FILENAME);
	// StreamResult result = new StreamResult(file);
	// transformerXHTML.transform(source, result);
	// return file;
	//
	// }

	/**
	 * returns product catalog as BMECat
	 * 
	 * @param document
	 * @return File
	 * @throws TransformerException
	 */
	private File createBMEcatFile(Document document)
			throws TransformerException {
		DOMSource source = new DOMSource(document);
		File file = new File(Constants.BMECAT_FILENAME);
		StreamResult result = new StreamResult(file);
		transformerBMECat.transform(source, result);
		return file;
	}

	/**
	 * product catalog 'Produktkatalog'
	 * 
	 * @param document
	 * @return File
	 * @throws TransformerException
	 */
	private File getDocumentAsProduktkatalog(Document document)
			throws TransformerException {
		DOMSource source = new DOMSource(document);
		File file = new File(Constants.PK_FILENAME);
		StreamResult result = new StreamResult(file);
		transformerBMECat.transform(source, result);
		return file;
	}

	/**
	 * return product catalog as HTML
	 * 
	 * @param document
	 * @return File
	 * @throws TransformerException
	 */
	private File getPK_HTML(Document document) throws TransformerException {
		DOMSource source = new DOMSource(document);
		File file = new File(Constants.XHTML_FILENAME);
		StreamResult result = new StreamResult(file);
		transformerXHTML.transform(source, result);
		return file;
	}

	/**
	 * get DOM
	 * 
	 * @return document
	 */
	private Document createBMEcatDoc() {
		DocumentBuilder builder;
		Document document = null;

		try {
			builder = factory.newDocumentBuilder();
			document = builder.newDocument();

			// create root element with attributes
			Element rootElement = document
					.createElement(Constants.ELEMENT_BMECAT);
			document.appendChild(rootElement);
			rootElement.setAttribute("version", "1.2");
			rootElement.setAttribute("xmlns:xsi",
					"http://www.w3.org/2001/XMLSchema-instance");

			// create BMECAT HEADER
			rootElement.appendChild(createHeader(document));
			rootElement.appendChild(createBody(document));

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}

		return document;

	}

	/**
	 * BmeCat Header elements
	 * 
	 * @param document
	 * @return Node
	 */
	private Node createHeader(Document document) {
		// Preconditions.checkNotNull(document);

		String lang = "deu";
		String version = "1.0";
		String name = "EBUT Catalog 2014";
		String id = "Tandem2";

		Element header = document.createElement(Constants.ELEMENT_HEADER);
		Element catalog = document.createElement(Constants.ELEMENT_CATALOG);
		header.appendChild(catalog);

		Element language = document.createElement(Constants.ELEMENT_LANGUAGE);
		language.appendChild(document.createTextNode(lang));
		Element catalogId = document
				.createElement(Constants.ELEMENT_CATALOG_ID);
		catalogId.appendChild(document.createTextNode(id));
		Element catalogVersion = document
				.createElement(Constants.ELEMENT_CATALOG_VERSION);
		catalogVersion.appendChild(document.createTextNode(version));
		Element catalogName = document
				.createElement(Constants.ELEMENT_CATALOG_NAME);
		catalogName.appendChild(document.createTextNode(name));

		catalog.appendChild(language);
		catalog.appendChild(catalogId);
		catalog.appendChild(catalogVersion);
		catalog.appendChild(catalogName);

		Element supplier = document.createElement(Constants.ELEMENT_SUPPLIER);
		Element supplierName = document.createElement(Constants.SUPPLIER_NAME);
		supplierName.appendChild(document.createTextNode("Tandem2SuperShop"));
		supplier.appendChild(supplierName);
		header.appendChild(supplier);

		return header;
	}

	/**
	 * BmeCat body elements
	 * 
	 * @param document
	 * @return Node
	 */
	private Node createBody(Document document) {
		Element tNewCatalog = document
				.createElement(Constants.ELEMENT_TNEWCATALOG);
		if (boProducts.isEmpty()) {
			System.err.println("--- no articles found ---");
		} else {
			for (BOProduct boProduct : boProducts) {
				Element articleElement = document
						.createElement(Constants.ELEMENT_ARTICLE);
				Element supplierElement = document
						.createElement(Constants.ELEMENT_SUPPLIER_AID);
				String supplierNumber = boProduct.getOrderNumberCustomer();
				supplierElement.appendChild(document
						.createTextNode(supplierNumber));

				Node articleDetailsElement = createArticleDetails(document,
						boProduct);

				Node articleOrderElement = createArticleOrderDetails(document,
						boProduct);

				Node articlePriceDetails = createArticlePriceDetails(document,
						boProduct);

				articleElement.appendChild(supplierElement);
				articleElement.appendChild(articleDetailsElement);
				articleElement.appendChild(articleOrderElement);
				articleElement.appendChild(articlePriceDetails);

				tNewCatalog.appendChild(articleElement);
			}
		}
		return tNewCatalog;
	}

	/**
	 * BMECat article details elements
	 * 
	 * @param document
	 * @param boProduct
	 * @return Node
	 */
	private Node createArticleDetails(Document document, BOProduct boProduct) {
		String shortDescriptionCustomer = boProduct
				.getShortDescriptionCustomer();
		String longDescriptionCustomer = boProduct.getLongDescriptionCustomer();

		Element articleDetailsElement = document
				.createElement(Constants.ARTICLE_DETAILS);
		Element descriptionShortElement = document
				.createElement(Constants.DESCRIPTION_SHORT);
		descriptionShortElement.appendChild(document
				.createTextNode(shortDescriptionCustomer));
		Element descriptionLongElement = document
				.createElement(Constants.DESCRIPTION_LONG);
		descriptionLongElement.appendChild(document
				.createTextNode(longDescriptionCustomer));

		articleDetailsElement.appendChild(descriptionShortElement);
		articleDetailsElement.appendChild(descriptionLongElement);

		return articleDetailsElement;
	}

	/**
	 * create and get price details per article
	 * 
	 * @param document
	 * @param boProduct
	 * @return Node
	 */
	private Node createArticlePriceDetails(Document document,
			BOProduct boProduct) {
		List<BOSalesPrice> salesPrices = boProduct.getSalesPrices();

		Element articlePriceDetailsElement = document
				.createElement(Constants.ARTICLE_PRICE_DETAILS);
		for (BOSalesPrice boSalesPrice : salesPrices) {
			Element articlePriceElement = document
					.createElement(Constants.ARTICLE_PRICE);
			articlePriceElement.setAttribute("price_type",
					boSalesPrice.getPricetype());
			Element priceAmountElement = document
					.createElement(Constants.PRICE_AMOUNT);
			priceAmountElement.appendChild(document.createTextNode(boSalesPrice
					.getAmount().toString()));
			Element priceCurrencyElement = document
					.createElement(Constants.PRICE_CURRENCY);
			priceCurrencyElement.appendChild(document
					.createTextNode(boSalesPrice.getCountry().getCurrency()
							.getCode()));
			Element taxElement = document.createElement(Constants.TAX);
			taxElement.appendChild(document.createTextNode(boSalesPrice
					.getTaxrate().toString()));

			Element territoryElement = document
					.createElement(Constants.TERRITORY);
			territoryElement.appendChild(document.createTextNode(boSalesPrice
					.getCountry().getIsocode()));

			articlePriceElement.appendChild(priceAmountElement);
			articlePriceElement.appendChild(priceCurrencyElement);
			articlePriceElement.appendChild(taxElement);
			articlePriceElement.appendChild(territoryElement);

			articlePriceDetailsElement.appendChild(articlePriceElement);
		}

		return articlePriceDetailsElement;
	}

	/**
	 * article order details product
	 * 
	 * @param document
	 * @param boProduct
	 * @return Node
	 */
	private Node createArticleOrderDetails(Document document,
			BOProduct boProduct) {
		Element articleOrderDetailsElement = document
				.createElement(Constants.ARTICLE_ORDER_DETAILS);
		Element orderUnitElement = document.createElement(Constants.ORDER_UNIT);
		Element contentUnitElement = document
				.createElement(Constants.CONTENT_UNIT);
		Element contentUnitPerOderUnitElement = document
				.createElement(Constants.NO_CU_PER_OU);

		orderUnitElement.appendChild(document.createTextNode("C62"));
		contentUnitElement.appendChild(document.createTextNode("04"));
		contentUnitPerOderUnitElement.appendChild(document.createTextNode("1"));

		articleOrderDetailsElement.appendChild(orderUnitElement);
		articleOrderDetailsElement.appendChild(contentUnitElement);
		articleOrderDetailsElement.appendChild(contentUnitPerOderUnitElement);

		return articleOrderDetailsElement;
	}
}
