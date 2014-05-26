package de.htwg_konstanz.ebus.wholesaler.main;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import de.htwg_konstanz.ebus.framework.wholesaler.api.bo.BOCountry;
import de.htwg_konstanz.ebus.framework.wholesaler.api.bo.BOProduct;
import de.htwg_konstanz.ebus.framework.wholesaler.api.bo.BOPurchasePrice;
import de.htwg_konstanz.ebus.framework.wholesaler.api.bo.BOSalesPrice;
import de.htwg_konstanz.ebus.framework.wholesaler.api.bo.BOSupplier;
import de.htwg_konstanz.ebus.framework.wholesaler.api.boa.CountryBOA;
import de.htwg_konstanz.ebus.framework.wholesaler.api.boa.PriceBOA;
import de.htwg_konstanz.ebus.framework.wholesaler.api.boa.ProductBOA;
import de.htwg_konstanz.ebus.framework.wholesaler.api.boa.SupplierBOA;
import de.htwg_konstanz.ebus.wholesaler.exceptions.DocumentNotValidException;

public class Import {
	private final String CLASS_NAME = getClass().getName();
	private final Logger log = Logger.getLogger(CLASS_NAME);
	private final Handler handler = new ConsoleHandler();
	private BOSupplier supplier = null;

	public Import() {
		log.addHandler(handler);
	}

	/**
	 * Checks if the product is already existing according to this supplier in
	 * the database or not. If it exists already it gets updated. If not it gets
	 * created.
	 * 
	 * @param BOSupplier
	 *            Supplier of the product which is looked for
	 * @param String
	 *            Order number of the product
	 * @return BOProduct The product which is freshly created or was updated
	 */
	private BOProduct getProductfromStore(BOSupplier supplier,
			String orderNumber) {
		BOProduct searchedProduct = null;
		BOProduct existingProduct = ProductBOA.getInstance()
				.findByOrderNumberSupplier(orderNumber);

		if (existingProduct != null) {
			log.info("Product is already existing!");
			if (supplier.getSupplierNumber().equals(
					existingProduct.getSupplier().getSupplierNumber())) {
				log.info("Product is from the same supplier and will be updated!");
				existingProduct.setLongDescription(null);
				existingProduct.setLongDescriptionCustomer(null);
				searchedProduct = existingProduct;
			} else {
				log.info("Product with No. " + orderNumber
						+ " exists for another supplier!");
				// TODO Throw exception
			}
		} else {
			log.info("Product " + orderNumber
					+ " does not exist. Will be created for store!");
			searchedProduct = new BOProduct();
			searchedProduct.setOrderNumberSupplier(orderNumber);
			searchedProduct.setOrderNumberCustomer(orderNumber);
		}
		return searchedProduct;
	}

	/**
	 * Finds the supplier of the imported document and returns it.
	 * 
	 * @param org
	 *            .w3c.dom.Document The document which was parsed from the file
	 * @return BOSupplier Returns the Content of the Suppliertag which is the
	 *         name of the supplier
	 */
	private BOSupplier getSupplier(org.w3c.dom.Document document) {
		log.entering(CLASS_NAME, "getSupplier");

		NodeList suppliername = document.getElementsByTagName("SUPPLIER_NAME");
		String documentSupplierCompany = null;
		BOSupplier tempSupplier = null;

		if (suppliername.getLength() == 1) {
			documentSupplierCompany = suppliername.item(0).getTextContent();
		} else {
			log.warning("Unable to reach Supplier Company in Document!");
		}

		List<BOSupplier> suppliers = SupplierBOA.getInstance().findAll();
		for (BOSupplier boSupplier : suppliers) {
			if (documentSupplierCompany.equals(boSupplier.getCompanyname())) {
				tempSupplier = boSupplier;
			}
		}
		log.exiting(CLASS_NAME, "getSupplier");
		return tempSupplier;
	}

	/**
	 * Creates a W3C document from file and returns the parsed document.
	 * 
	 * @param InputStream
	 *            The file inputstream from which the document is parsed from
	 * @return org.w3c.dom.Document The created document from the file
	 *         inputstream
	 */
	private org.w3c.dom.Document createDocumentFromFile(InputStream file) {
		log.entering(CLASS_NAME, "createDocumentFromFile");
		org.w3c.dom.Document document = null;
		DocumentBuilder db;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		dbf.setIgnoringElementContentWhitespace(true);
		dbf.setValidating(false);

		try {
			db = dbf.newDocumentBuilder();
			document = db.parse(file);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		log.exiting(CLASS_NAME, "createDocumentFromFile");
		return document;
	}

	/**
	 * Checks if the created document is valid according to the catalogs XSD.
	 * 
	 * @param org
	 *            .w3c.dom.Document The document which is created from the
	 *            imported file
	 * @return boolean Returns if the document is validate to the BMEcat XSD
	 * @throws DocumentNotValidException
	 */
	private boolean validateDoc(org.w3c.dom.Document document)
			throws DocumentNotValidException {
		log.entering(CLASS_NAME, "validateDoc");
		boolean ret = false;
		SchemaFactory schemaFactory = SchemaFactory
				.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema bmeCatSchema = null;
		Validator validator = null;

		try {
			// TODO Link realtiv machen!
			bmeCatSchema = schemaFactory
					.newSchema(new File(
							"D:/EBUT/EBUT-Lab-Teil3-Tandem-02/Wholesaler/files/bmecat_new_catalog_1_2_simple_without_NS.xsd"));
			validator = bmeCatSchema.newValidator();
		} catch (SAXException e) {
			log.warning("No Schema found!");
			e.printStackTrace();
		}

		try {
			validator.validate(new DOMSource(document));
			ret = true;
		} catch (NullPointerException e) {
			log.warning("Source is null!");
			throw new DocumentNotValidException("Source is null!");
		} catch (SAXException e) {
			log.warning("Document is not valid!");
			throw new DocumentNotValidException("Document is not valid!");
		} catch (IOException e) {
			log.warning("Document is not valid!");
			throw new DocumentNotValidException("Document is not valid!");
		}
		log.exiting(CLASS_NAME, "validateDoc");
		return ret;
	}

	/**
	 * Gets the supplier AID from the currentNode if existing.
	 * 
	 * @param Node
	 *            Current structure node contraining the supplier AID
	 * @return String The supplier AID
	 */
	private String getSupplierAID(Node currentNode) {
		log.entering(CLASS_NAME, "getSupplierAID");
		String supplierAID = null;
		while (currentNode != null) {

			if (currentNode.getNodeName().equals(Constants.SUPPLIER_AID)) {
				supplierAID = currentNode.getFirstChild().getNodeValue();
				break;

			} else {
				currentNode = currentNode.getNextSibling();
			}
		}
		log.exiting(CLASS_NAME, "getSupplierAID");
		return supplierAID;
	}

	/**
	 * Parses a file and imports the files products into the store database.
	 * 
	 * @param InputStream
	 *            The file inputstream from which the products are imported
	 * @throws DocumentNotValidException
	 *             Throws this exception if the document created from the file
	 *             is not valid to the catalog XSD.
	 */
	public String uploadFile(InputStream file) throws DocumentNotValidException {
		log.entering(CLASS_NAME, "uploadFile");
		String ret = "";
		org.w3c.dom.Document document = this.createDocumentFromFile(file);

		if (document == null) {
			ret = "Your XML could not be parsed because it is not well-formed. Please check your XML!";
			log.warning(ret);
			throw new DocumentNotValidException(ret);
		} else {
			this.validateDoc(document);
			this.supplier = this.getSupplier(document);
			if (this.supplier != null) {
				this.importArticles(document);
				ret = "Upload or update successfull!";
			} else {
				ret = "Cannot Import Document, no such supplier in Database!";
				log.warning(ret);
				throw new DocumentNotValidException(ret);
			}
		}
		log.exiting(CLASS_NAME, "uploadFile");
		return ret;
	}

	/**
	 * Imports all the articles from the document into the database
	 * 
	 * @param org
	 *            .w3c.dom.Document The document which is created from the
	 *            imported file
	 */
	private void importArticles(org.w3c.dom.Document document) {
		log.entering(CLASS_NAME, "importArticles");

		BOProduct importedProduct;

		NodeList articles = document.getElementsByTagName(Constants.ARTICLE);
		HashSet<BOPurchasePrice> priceList = null;

		for (int i = 0; i < articles.getLength(); i++) {
			importedProduct = getProductfromStore(supplier,
					getSupplierAID(articles.item(i).getFirstChild()));
			importedProduct.setSupplier(supplier);
			Node importedProductContent = articles.item(i).getFirstChild();

			while (importedProductContent != null) {
				if (importedProductContent.getNodeName().equals(
						Constants.ARTICLE_DETAILS)) {
					setArticleDetails(importedProductContent, importedProduct);
				}
				if (importedProductContent.getNodeName().equals(
						Constants.ARTICLE_PRICE_DETAILS)) {
					priceList = getArticlePriceDetails(importedProductContent);
				}
				// There is no need to read ARTICLE_ORDER_DETAILS
				importedProductContent = importedProductContent
						.getNextSibling();
			}
			saveArticle(importedProduct, priceList);
		}
		log.exiting(CLASS_NAME, "importArticles");
	}

	/**
	 * Finds the articles purchase price of a node
	 * 
	 * @param Node
	 *            Node which contains the price of the article
	 * @return BOPurchasePrice The article price which was found for the product
	 */
	private BOPurchasePrice getArticlePrice(Node articlePrice) {
		log.entering(CLASS_NAME, "getArticlePrice");

		BOPurchasePrice price = new BOPurchasePrice();
		NamedNodeMap attributes = articlePrice.getAttributes();
		Node priceType = attributes.getNamedItem(Constants.PRICE_TYPE);
		price.setPricetype(priceType.getNodeValue());
		Node currentNode = articlePrice.getFirstChild();

		while (currentNode != null) {
			if (currentNode.getNodeName().equals(Constants.PRICE_AMOUNT)) {
				BigDecimal amount = new BigDecimal(currentNode.getFirstChild()
						.getNodeValue());
				price.setAmount(amount);
			}
			if (currentNode.getNodeName().equals(Constants.TAX)) {
				BigDecimal taxes = new BigDecimal(currentNode.getFirstChild()
						.getNodeValue());
				price.setTaxrate(taxes);
			}
			currentNode = currentNode.getNextSibling();
		}
		log.exiting(CLASS_NAME, "getArticlePrice");
		return price;
	}

	/**
	 * Finds all the countries the article has a price for.
	 * 
	 * @param Node
	 *            Node which contains the price information of the article
	 * @return HashSet<BOCountry> A list of countries
	 */
	private HashSet<BOCountry> getArticleCountries(Node articlePrice) {
		log.entering(CLASS_NAME, "getArticleCountries");

		Node currentNode = articlePrice.getFirstChild();
		HashSet<BOCountry> countries = new HashSet<BOCountry>();

		while (currentNode != null) {
			if (currentNode.getNodeName().equals(Constants.TERRITORY)) {
				BOCountry country = CountryBOA.getInstance().findCountry(
						currentNode.getFirstChild().getNodeValue());
				countries.add(country);
			}
			currentNode = currentNode.getNextSibling();
		}
		log.exiting(CLASS_NAME, "getArticleCountries");
		return countries;
	}

	/**
	 * Stores the article and all according information in the database.
	 * 
	 * @param BOProduct
	 *            The product which needs to be stored
	 * @param HashSet
	 *            <BOPurchasePrice> A list of all the purchase prices for the
	 *            product
	 */
	private void saveArticle(BOProduct product,
			HashSet<BOPurchasePrice> purchasePriceList) {
		log.entering(CLASS_NAME, "saveArticle");

		ProductBOA.getInstance().saveOrUpdate(product);
		PriceBOA price = PriceBOA.getInstance();
		BOSalesPrice salesPrice;

		for (BOPurchasePrice purchasePrice : purchasePriceList) {
			purchasePrice.setProduct(product);
			// Set default LowerboundScaledPrice
			purchasePrice.setLowerboundScaledprice(1);
			salesPrice = calcSalesPrice(purchasePrice);
			salesPrice.setProduct(product);
			price.saveOrUpdatePurchasePrice(purchasePrice);
			price.saveOrUpdate(salesPrice);
		}
		log.exiting(CLASS_NAME, "saveArticle");
	}

	/**
	 * Finds the article price details of the product
	 * 
	 * @param Node
	 *            Node with the current product content
	 * @return HashSet<BOPurchasePrice> A set of purchase prices with all the
	 *         price information
	 */
	private HashSet<BOPurchasePrice> getArticlePriceDetails(
			Node importedProductContent) {
		log.entering(CLASS_NAME, "getArticlePriceDetails");

		Node currentArticlePriceNode = importedProductContent.getFirstChild();
		HashSet<BOPurchasePrice> purchasePriceList = new HashSet<BOPurchasePrice>();

		while (currentArticlePriceNode != null) {
			if (currentArticlePriceNode.getNodeName().equals(
					Constants.ARTICLE_PRICE)) {
				BOPurchasePrice purchasePrice = getArticlePrice(currentArticlePriceNode);
				HashSet<BOCountry> countryList = getArticleCountries(currentArticlePriceNode);

				// One Price for every single Country
				for (BOCountry boCountry : countryList) {
					BOPurchasePrice purchasePriceForCountry = new BOPurchasePrice();
					purchasePriceForCountry.setPricetype(purchasePrice
							.getPricetype());
					purchasePriceForCountry
							.setAmount(purchasePrice.getAmount());
					purchasePriceForCountry.setTaxrate(purchasePrice
							.getTaxrate());
					purchasePriceForCountry.setCountry(boCountry);
					purchasePriceList.add(purchasePriceForCountry);
				}
			}
			currentArticlePriceNode = currentArticlePriceNode.getNextSibling();
		}
		log.exiting(CLASS_NAME, "getArticlePriceDetails");
		return purchasePriceList;
	}

	/**
	 * Sets all the article details of the product.
	 * 
	 * @param Node
	 *            Node with the current product content
	 * @param BOProduct
	 *            Product which the details is set for
	 */
	private void setArticleDetails(Node articleDetails,
			BOProduct importedProduct) {
		log.entering(CLASS_NAME, "setArticleDetails");

		Node currentNode = articleDetails.getFirstChild();

		while (currentNode != null) {
			if (currentNode.getNodeName().equals(Constants.DESCRIPTION_SHORT)) {
				importedProduct.setShortDescription(currentNode.getFirstChild()
						.getNodeValue());
				importedProduct.setShortDescriptionCustomer(currentNode
						.getFirstChild().getNodeValue());
			}
			if (currentNode.getNodeName().equalsIgnoreCase(
					Constants.DESCRIPTION_LONG)) {
				importedProduct.setLongDescription(currentNode.getFirstChild()
						.getNodeValue());
				importedProduct.setLongDescriptionCustomer(currentNode
						.getFirstChild().getNodeValue());
			}
			if (currentNode.getNodeName().equals(
					Constants.MANUFACTURER_TYPE_DESCR)) {
				importedProduct.setManufacturerTypeDescription(currentNode
						.getFirstChild().getNodeValue());
			}
			currentNode = currentNode.getNextSibling();
		}
		log.exiting(CLASS_NAME, "setArticleDetails");
	}

	/**
	 * Calculates the sales price of the product.
	 * 
	 * @param BOPurchasePrice
	 *            The purchase price of the product
	 * @return BOSalesPrice Returns the sales price object of the product
	 */
	private BOSalesPrice calcSalesPrice(BOPurchasePrice purchasePrice) {
		log.entering(CLASS_NAME, "calcSalesPrice");

		BOSalesPrice salesPrice = new BOSalesPrice();
		salesPrice.setAmount(purchasePrice.getAmount().multiply(
				Constants.SALESPRICE_MULTIPLIER));
		salesPrice.setCountry(purchasePrice.getCountry());
		salesPrice.setLowerboundScaledprice(purchasePrice
				.getLowerboundScaledprice());
		salesPrice.setPricetype(purchasePrice.getPricetype());
		salesPrice.setTaxrate(purchasePrice.getTaxrate());
		salesPrice.setProduct(purchasePrice.getProduct());

		log.exiting(CLASS_NAME, "calcSalesPrice");
		return salesPrice;
	}

}
