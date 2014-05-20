//package de.htwg_konstanz.ebus.wholesaler.main;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
//
//import java.io.File;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.w3c.dom.NodeList;
//
//import de.htwg_konstanz.ebus.framework.wholesaler.api.bo.BOProduct;
//import de.htwg_konstanz.ebus.framework.wholesaler.api.bo.BOSupplier;
//import de.htwg_konstanz.ebus.framework.wholesaler.api.boa.ProductBOA;
//
//public class JImport {
//
//	File file;
//	Import importer;
//
//	@Before
//	public void setup() {
//		file = new File(
//				"D:/EBUT/EBUT-Lab-Teil3-Tandem-02/Programm/files/sample_articles_kn_media_store.xml");
//		importer = new Import();
//	}
//
//	@Test
//	public void testCreateDocumentFromFile() {
//		assertNotNull("File ist null", file);
//		org.w3c.dom.Document doc = importer.createDocumentFromFile(file);
//
//		assertNotNull("doc ist null", doc);
//
//		NodeList liste = doc.getElementsByTagName("SUPPLIER_NAME");
//		assertEquals(liste.getLength(), 1);
//		assertEquals("KN MEDIA STORE", liste.item(0).getTextContent());
//	}
//
//	@Test
//	public void testValidateDoc() {
//		org.w3c.dom.Document doc = importer.createDocumentFromFile(file);
//		assertEquals(true, importer.validateDoc(doc));
//	}
//
//	// @Test
//	// public void testGetProductfromStore() {
//	// org.w3c.dom.Document doc = importer.createDocumentFromFile(file);
//	// BOSupplier supplier = importer.getSupplier(doc);
//	// assertNotNull(supplier);
//	// assertEquals("KN MEDIA STORE", supplier.getCompanyname());
//	//
//	// BOProduct testProduct = importer.getProductfromStore(supplier, "A1111");
//	// assertNotNull(testProduct);
//	// System.err.println(testProduct.getShortDescriptionCustomer());
//	// assertEquals("Tablet PC", testProduct.getShortDescription());
//	//
//	// }
//
//	// @Test
//	// public void testImportArticles() {
//	// fail("Not yet implemented");
//	// }
//
//	// @Test
//	// public void testCalcSalesPrice() {
//	// BOProduct existingProduct = ProductBOA.getInstance()
//	// .findByOrderNumberSupplier("mer-penbook-1");
//	// BOPurchasePrice salesPrice = new BOPurchasePrice();
//	//
//	// org.w3c.dom.Document doc = importer.createDocumentFromFile(file);
//	// BigDecimal expectedGrossPrice = new BigDecimal("5.88");
//	//
//	// assertEquals(1, existingProduct.getPurchasePrices().size());
//	//
//	// assertEquals(expectedGrossPrice, existingProduct.getPurchasePrices()
//	// .get(0).getAmount());
//	// salesPrice.setAmount(expectedGrossPrice);
//	// BOSalesPrice newSalesPrice = importer.calcSalesPrice(salesPrice);
//	// assertEquals(new BigDecimal(8.82), newSalesPrice.getAmount());
//	// }
//
//	@Test
//	public void testSaveArticle() {
//
//	}
//
//	@Test
//	public void testGetArticleCountries() {
//
//	}
//
//	@Test
//	public void testGetArticlePrice() {
//
//	}
//
//	@Test
//	public void testGetArticlePriceDetails() {
//		BOProduct existingProduct = ProductBOA.getInstance()
//				.findByOrderNumberSupplier("mer-penbook-1");
//
//		assertEquals("HTWG-Pen Book", existingProduct.getShortDescription());
//	}
//
//	@Test
//	public void testgetSupplierAID() {
//		org.w3c.dom.Document doc = importer.createDocumentFromFile(file);
//		NodeList liste = doc.getElementsByTagName("SUPPLIER_AID");
//		assertEquals(4, liste.getLength());
//		assertEquals("A1111", importer.getSupplierAID(liste.item(0)));
//		assertEquals("A2222", importer.getSupplierAID(liste.item(1)));
//		assertEquals("A3333", importer.getSupplierAID(liste.item(2)));
//		assertEquals("A4444", importer.getSupplierAID(liste.item(3)));
//	}
//
//	@Test
//	public void testGetSupplier() {
//		org.w3c.dom.Document doc = importer.createDocumentFromFile(file);
//		BOSupplier supplier = importer.getSupplier(doc);
//		assertNotNull(supplier);
//		assertEquals("KN MEDIA STORE", supplier.getCompanyname());
//	}
//
// }
