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
		final String sourceMethod = "execute";
		String nextPage = "downloadCatalog.jsp";

		log.entering(getClass().getName(), sourceMethod);

		LoginBean loginBean = (LoginBean) request.getSession().getAttribute(
				Constants.PARAM_LOGIN_BEAN);
		if (loginBean.isLoggedIn()) {

			String shortDescription = request
					.getParameter(Constants.PARAM_SEARCH_STRING);

			ExportFormat fileType = Constants.ExportFormat
					.getExportFormat(request
							.getParameter(Constants.PARAM_FILE_TYPE));

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
