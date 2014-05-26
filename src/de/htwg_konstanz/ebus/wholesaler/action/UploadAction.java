package de.htwg_konstanz.ebus.wholesaler.action;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import de.htwg_konstanz.ebus.framework.wholesaler.api.security.Security;
import de.htwg_konstanz.ebus.wholesaler.demo.ControllerServlet;
import de.htwg_konstanz.ebus.wholesaler.demo.IAction;
import de.htwg_konstanz.ebus.wholesaler.demo.LoginBean;
import de.htwg_konstanz.ebus.wholesaler.demo.util.Constants;
import de.htwg_konstanz.ebus.wholesaler.exceptions.DocumentNotValidException;
import de.htwg_konstanz.ebus.wholesaler.main.Import;

/**
 * The ProductListAction loads all available products from the database.
 * <p>
 * After loading, the action puts all products into an List-Object and makes
 * them available for the corresponding view (JSP-Page) via the HTTPSession.
 * 
 * @author stregens
 */
public class UploadAction implements IAction {

	/**
	 * The execute method is automatically called by the dispatching sequence of
	 * the {@link ControllerServlet}.
	 * 
	 * @param request
	 *            the HttpServletRequest-Object provided by the servlet engine
	 * @param response
	 *            the HttpServletResponse-Object provided by the servlet engine
	 * @param errorList
	 *            a Stringlist for possible error messages occured in the
	 *            corresponding action
	 * @return the redirection URL
	 */
	public String execute(HttpServletRequest request,
			HttpServletResponse response, ArrayList<String> errorList) {
		// get the login bean from the session
		LoginBean loginBean = (LoginBean) request.getSession(true)
				.getAttribute(Constants.PARAM_LOGIN_BEAN);

		// ensure that the user is logged in
		if (loginBean != null && loginBean.isLoggedIn()) {
			// ensure that the user is allowed to execute this action
			// (authorization)
			// at this time the authorization is not fully implemented.
			// -> use the "Security.RESOURCE_ALL" constant which includes all
			// resources.
			if (Security.getInstance().isUserAllowed(loginBean.getUser(),
					Security.RESOURCE_ALL, Security.ACTION_READ)) {
				DiskFileItemFactory factory = new DiskFileItemFactory();
				String nextPage = "welcome.jsp";

				// Configure a repository
				ServletContext servletContext = request.getSession()
						.getServletContext();
				File repository = (File) servletContext
						.getAttribute("javax.servlet.context.tempdir");
				factory.setRepository(repository);

				// Create a new file upload handler
				ServletFileUpload upload = new ServletFileUpload(factory);

				// Parse the request

				try {
					List<FileItem> items = upload.parseRequest(request);
					for (FileItem item : items) {
						if (item.isFormField() == false) {
							// Process form file field (input type="file").
							InputStream filecontent = item.getInputStream();
							try {
								// Call Importfunction
								errorList.add(new Import()
										.uploadFile(filecontent));
								filecontent.close();
							} catch (DocumentNotValidException e) {
								errorList.add(e.getMessage());
								e.printStackTrace();
								nextPage = "upload.jsp";
							}
						}
					}
				} catch (FileUploadException e) {
					errorList
							.add("Your upload failed, please contact your admin :)");
					e.printStackTrace();
					nextPage = "upload.jsp";
				} catch (IOException e) {
					errorList
							.add("Your upload failed, please contact your admin :)");
					e.printStackTrace();
					nextPage = "upload.jsp";
				}
				return nextPage;
			} else {
				// authorization failed -> show error message
				errorList.add("Your are not allowed to perform this action");
				// redirect to the welcome page
				return "welcome.jsp";
			}
		} else
			// redirect to the login page
			return "login.jsp";
	}

	/**
	 * Each action itself decides if it is responsible to process the
	 * corrensponding request or not. This means that the
	 * {@link ControllerServlet} will ask each action by calling this method if
	 * it is able to process the incoming action request, or not.
	 * 
	 * @param actionName
	 *            the name of the incoming action which should be processed
	 * @return true if the action is responsible, else false
	 */
	public boolean accepts(String actionName) {
		return actionName.equalsIgnoreCase(Constants.ACTION_UPLOAD_CATALOG);
	}
}
