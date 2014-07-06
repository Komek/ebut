package de.htwg_konstanz.ebus.wholesaler.action;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.example.update_catalog_webservice.UpdateResponseType;

import de.htwg_konstanz.ebus.framework.wholesaler.api.security.Security;
import de.htwg_konstanz.ebus.wholesaler.demo.IAction;
import de.htwg_konstanz.ebus.wholesaler.demo.LoginBean;
import de.htwg_konstanz.ebus.wholesaler.demo.util.Constants;
import de.htwg_konstanz.ebus.wholesaler.main.Updater;

public class UpdateAction implements IAction {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, ArrayList<String> errorList) {
		String result;

		// get the login bean from the session
		LoginBean loginBean = (LoginBean) request.getSession(true)
				.getAttribute(Constants.PARAM_LOGIN_BEAN);

		// ensure that the user is logged in
		if (loginBean != null && loginBean.isLoggedIn()) {

			if (Security.getInstance().isUserAllowed(loginBean.getUser(),
					Security.RESOURCE_ALL, Security.ACTION_READ)) {

				String supplierNumber = (String) request
						.getParameter(Constants.PARAM_SUPPLIER);

				UpdateResponseType updateResponse = new Updater()
						.getUpdates(supplierNumber);

				// Fill Session
				request.getSession(true).setAttribute(
						"listOfUpdatedProducts",
						updateResponse.getListOfUpdatedProducts()
								.getChangedProducts());
				request.getSession(true).setAttribute(
						"listOfUnavailableProducts",
						updateResponse.getListOfUnavaiableProducts()
								.getSupplierProduct());
				request.getSession(true).setAttribute(
						"listOfUpdatedProductsObject",
						updateResponse.getListOfUpdatedProducts());
				request.getSession(true).setAttribute(
						"listOfUnavailableProductsObject",
						updateResponse.getListOfUnavaiableProducts());

				result = "updater_overview.jsp";
			} else {
				// authorization failed -> show error message
				errorList.add("You are not allowed to perform this action!");

				// redirect to the welcome page
				result = "welcome.jsp";
			}
		} else {
			// redirect to the login page
			result = "login.jsp";
		}
		return result;
	}

	@Override
	public boolean accepts(String actionName) {
		return actionName.equalsIgnoreCase("showUpdates");
	}

}
