package de.htwg_konstanz.ebus.wholesaler.demo.util;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;

import org.example.update_catalog_webservice.ListOfProductsType;
import org.example.update_catalog_webservice.ObjectFactory;
import org.example.update_catalog_webservice.PriceType;
import org.example.update_catalog_webservice.SupplierProduct;

import com.google.common.base.Preconditions;

import de.htwg_konstanz.ebus.framework.wholesaler.api.bo.BOProduct;
import de.htwg_konstanz.ebus.framework.wholesaler.api.bo.BOPurchasePrice;
import de.htwg_konstanz.ebus.framework.wholesaler.api.boa.ProductBOA;

public class ProductUtil {

	private final ObjectFactory factory = new ObjectFactory();
	private final ProductBOA productBOA = ProductBOA.getInstance();

	// private final CountryBOA countryBOA = CountryBOA.getInstance();

	public ListOfProductsType getListOfProducts(List<BOProduct> inputProductList) {
		ListOfProductsType listOfProducts = factory.createListOfProductsType();

		for (BOProduct boProduct : inputProductList) {
			listOfProducts.getSupplierProduct().add(
					getSupplierProduct(boProduct));
		}

		return listOfProducts;
	}

	public SupplierProduct getSupplierProduct(BOProduct boProduct) {
		SupplierProduct supplierProduct = factory.createSupplierProduct();
		supplierProduct.setSupplierAID(boProduct.getOrderNumberSupplier());
		supplierProduct.setShortDescription(boProduct.getShortDescription());
		supplierProduct.setLongDescription(boProduct.getLongDescription());

		supplierProduct.getPrice().addAll(getPriceList(boProduct));

		return supplierProduct;
	}

	private HashSet<PriceType> getPriceList(BOProduct boProduct) {
		HashSet<PriceType> priceList = new HashSet<PriceType>();
		for (BOPurchasePrice boPurchasePrice : boProduct.getPurchasePrices()) {
			PriceType price = factory.createPriceType();
			price.setAmount(boPurchasePrice.getAmount());
			price.setCountryISOCode(boPurchasePrice.getCountry().getIsocode());
			price.setCurrency(boPurchasePrice.getCountry().getCurrency()
					.getCode());
			Integer lowerbound = boPurchasePrice.getLowerboundScaledprice();
			BigInteger biggi = BigInteger.valueOf(lowerbound.intValue());
			price.setLowerBound(biggi);
			price.setPricetype(boPurchasePrice.getPricetype());
			price.setTax(boPurchasePrice.getTaxrate());

			priceList.add(price);
		}

		return priceList;
	}

	public boolean doesProductExist(SupplierProduct supplierProduct) {
		Preconditions.checkNotNull(supplierProduct);
		boolean existsing;
		String supplierAID = supplierProduct.getSupplierAID();
		BOProduct boProduct = productBOA.findByOrderNumberSupplier(supplierAID);
		if (boProduct == null) {
			existsing = false;
		} else {
			existsing = true;
		}
		return existsing;
	}

	// TODO Kommentar ggf. löschen!
	public boolean hasSupplierProductChanged(SupplierProduct supplierProduct) {
		Preconditions.checkNotNull(supplierProduct);
		boolean hasProductChanged = false;
		if (doesProductExist(supplierProduct)) {
			String supplierAID = supplierProduct.getSupplierAID();
			BOProduct boProduct = productBOA
					.findByOrderNumberSupplier(supplierAID);
			boolean hasLongDescriptionChanged = false;
			hasLongDescriptionChanged = supplierProduct.getLongDescription()
					.equals(boProduct.getLongDescription());

			boolean hasShortDescriptionChanged = false;
			hasShortDescriptionChanged = supplierProduct.getShortDescription()
					.equals(boProduct.getShortDescription());
			// boolean hasPriceChanged = checkPrices(supplierProduct,
			// boProduct);

			if (hasLongDescriptionChanged || hasShortDescriptionChanged) {
				// || hasPriceChanged) {
				hasProductChanged = true;
			}
		}

		return hasProductChanged;
	}

	// public Boolean checkPrices(SupplierProduct supplierProduct,
	// BOProduct boProduct) {
	// Preconditions.checkNotNull(supplierProduct);
	// Preconditions.checkNotNull(boProduct);
	//
	// Boolean hasPriceChanged = false;
	// for (PriceType supplierPrice : supplierProduct.getPrice()) {
	// String countryISOCode = supplierPrice.getCountryISOCode();
	// BOPurchasePrice boPurchasePrice = boProduct
	// .getPurchasePrice(countryBOA.findCountry(countryISOCode));
	// boolean hasAmountChanged = supplierPrice.getAmount().equals(
	// boPurchasePrice.getAmount()) == false;
	// boolean hasTaxChanged = supplierPrice.getTax().equals(
	// boPurchasePrice.getTaxrate()) == false;
	// boolean hasCurrencyChanged = supplierPrice.getCurrency().equals(
	// boPurchasePrice.getCountry().getCurrency().getCode()) == false;
	//
	// if (hasAmountChanged || hasTaxChanged || hasCurrencyChanged) {
	// hasPriceChanged = true;
	// }
	// }
	//
	// return hasPriceChanged;
	// }
}
