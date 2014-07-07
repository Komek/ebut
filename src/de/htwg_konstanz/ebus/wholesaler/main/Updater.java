package de.htwg_konstanz.ebus.wholesaler.main;

import java.util.ArrayList;
import java.util.List;

import org.example.update_catalog_webservice.AuthenticationFaultMessage;
import org.example.update_catalog_webservice.ListOfProductsType;
import org.example.update_catalog_webservice.ListOfUnavaiableProductsType;
import org.example.update_catalog_webservice.ListOfUpdatedProductsType;
import org.example.update_catalog_webservice.ObjectFactory;
import org.example.update_catalog_webservice.SupplierProduct;
import org.example.update_catalog_webservice.UpdateCatalogWebservice;
import org.example.update_catalog_webservice.UpdateRequestType;
import org.example.update_catalog_webservice.UpdateResponseType;

import de.htwg_konstanz.ebus.framework.wholesaler.api.bo.BOProduct;
import de.htwg_konstanz.ebus.framework.wholesaler.api.boa.ProductBOA;
import de.htwg_konstanz.ebus.wholesaler.demo.util.ProductUtil;

public class Updater {

	private final ObjectFactory factory = new ObjectFactory();
	private final ProductUtil productUtil = new ProductUtil();

	public UpdateResponseType getUpdates(String supplierNumber) {
		UpdateResponseType response = factory.createUpdateResponseType();

		// Find all products for the given supplier
		List<BOProduct> products = ProductBOA.getInstance().findAll();
		List<BOProduct> productsOfSupplier = new ArrayList<>();
		for (BOProduct boProduct : products) {
			if (boProduct.getSupplier().getSupplierNumber()
					.equals(supplierNumber)) {
				productsOfSupplier.add(boProduct);
			}
		}
		ListOfProductsType listOfProducts = productUtil
				.getListOfProducts(productsOfSupplier);

		// Create Update Request
		UpdateRequestType updateRequest = factory.createUpdateRequestType();
		updateRequest.setListOfProducts(listOfProducts);

		// Do request
		try {

			response = new UpdateCatalogWebservice()
					.getUpdateCatalogWebserviceSOAP().updateCatalog(
							updateRequest);
		} catch (AuthenticationFaultMessage e) {
			e.printStackTrace();
		}

		return response;
	}

	public void importUpdates(ListOfUpdatedProductsType listOfUpdatedProducts,
			ListOfUnavaiableProductsType listOfUnavailableProducts) {
		for (SupplierProduct changedProduct : listOfUpdatedProducts
				.getChangedProducts()) {
			importChangedProducts(changedProduct);
		}

		for (SupplierProduct unavailableProduct : listOfUnavailableProducts
				.getSupplierProduct()) {
			ProductBOA.getInstance().delete(
					ProductBOA.getInstance().findByOrderNumberSupplier(
							unavailableProduct.getSupplierAID()));
		}

	}

	private void importChangedProducts(SupplierProduct inputSupplierProduct) {

		// Find boProduct
		BOProduct boProduct = ProductBOA.getInstance()
				.findByOrderNumberCustomer(
						inputSupplierProduct.getSupplierAID());

		// Write Updates
		boProduct.setOrderNumberSupplier(inputSupplierProduct.getSupplierAID());
		boProduct.setLongDescription(inputSupplierProduct.getLongDescription());
		boProduct.setShortDescription(inputSupplierProduct
				.getShortDescription());

	}
}
