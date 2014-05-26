package de.htwg_konstanz.ebus.wholesaler.action;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.htwg_konstanz.ebus.wholesaler.demo.IAction;
import de.htwg_konstanz.ebus.wholesaler.demo.LoginBean;
import de.htwg_konstanz.ebus.wholesaler.demo.util.Constants;
import de.htwg_konstanz.ebus.wholesaler.demo.util.Constants.ExportFormat;
import de.htwg_konstanz.ebus.wholesaler.main.Exporter;

public class DownloadAction implements IAction {

	private final Logger log = Logger.getLogger(getClass().getName());

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, ArrayList<String> errorList) {
		String nextPage = "downloadCatalog.jsp";

		// get the login bean from the session
		LoginBean loginBean = (LoginBean) request.getSession().getAttribute(
				Constants.PARAM_LOGIN_BEAN);
		// ensure that the user is logged in
		if (loginBean.isLoggedIn() && loginBean != null) {
			// ensure that the user is allowed to execute this action
			// (authorization)
			// at this time the authorization is not fully implemented.
			// -> use the "Security.RESOURCE_ALL" constant which includes all
			// resources.
			String shortDescription = request
					.getParameter(Constants.PARAM_SEARCH_STRING);

			ExportFormat fileType = Constants.ExportFormat
					.getExportFormat(request
							.getParameter(Constants.PARAM_FILE_TYPE));

			System.err
					.println(fileType
							+ "-------------------------------------------------------------------------");
			Exporter exporter = new Exporter(shortDescription, fileType);
			File exportFile = exporter.export();

			request.getSession(true).setAttribute("exportfile", exportFile);

		} else {
			errorList.add("Please log in first!");
			nextPage = "login.jsp";
		}
		return nextPage;
	}

	@Override
	public boolean accepts(String actionName) {
		return actionName.equalsIgnoreCase(Constants.ACTION_DOWNLOAD_CATALOG);
	}

}
