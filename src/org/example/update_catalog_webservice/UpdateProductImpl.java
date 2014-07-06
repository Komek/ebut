package org.example.update_catalog_webservice;

import javax.jws.WebService;

import de.htwg_konstanz.ebus.framework.wholesaler.api.boa.ProductBOA;
import de.htwg_konstanz.ebus.wholesaler.demo.util.ProductUtil;

@WebService(endpointInterface = "de.htwg_konstanz.ebus.wholesaler.ws.updateProduct.UpdateCatalogWebservice", targetNamespace = "http://www.example.org/update_catalog_webservice/")
public class UpdateProductImpl implements UpdateProductInterface {
	private final ObjectFactory factory = new ObjectFactory();
	private final ProductUtil productUtil = new ProductUtil();
	private final ProductBOA productBOA = ProductBOA.getInstance();

	@Override
	public UpdateResponseType updateCatalog(UpdateRequestType parameters)
			throws AuthenticationFaultMessage {
		UpdateResponseType response = factory.createUpdateResponseType();
		ListOfProductsType listOfProducts = parameters.getListOfProducts();

		ListOfUnavaiableProductsType listOfUnavailableProducts = factory
				.createListOfUnavaiableProductsType();
		ListOfUpdatedProductsType listOfUpdatedProducts = factory
				.createListOfUpdatedProductsType();

		for (SupplierProduct supplierProduct : listOfProducts
				.getSupplierProduct()) {
			if (productUtil.doesProductExist(supplierProduct)) {
				if (productUtil.hasSupplierProductChanged(supplierProduct)) {
					String supplierAID = supplierProduct.getSupplierAID();

					listOfUpdatedProducts.getChangedProducts().add(
							productUtil.getSupplierProduct(productBOA
									.findByOrderNumberSupplier(supplierAID)));
				}
			} else {
				listOfUnavailableProducts.getSupplierProduct().add(
						supplierProduct);
			}
		}

		response.setListOfUnavaiableProducts(listOfUnavailableProducts);
		response.setListOfUpdatedProducts(listOfUpdatedProducts);

		return response;
	}
}
