package de.htwg_konstanz.ebus.wholesaler.action;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.htwg_konstanz.ebus.wholesaler.demo.IAction;
import de.htwg_konstanz.ebus.wholesaler.demo.util.Constants;

public class Download_Dialog_Action implements IAction {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, ArrayList<String> errorList) {

		return "downloadCatalog.jsp";
	}

	@Override
	public boolean accepts(String actionName) {
		return actionName
				.equalsIgnoreCase(Constants.ACTION_SHOW_DOWNLOAD_CATALOG);
	}

}
