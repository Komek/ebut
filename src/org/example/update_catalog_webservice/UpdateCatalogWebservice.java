
package org.example.update_catalog_webservice;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.4-b01
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "update_catalog_webservice", targetNamespace = "http://www.example.org/update_catalog_webservice/", wsdlLocation = "file:/D:/workspace/ebut/WebContent/wsdl/updateCatalog.wsdl")
public class UpdateCatalogWebservice
    extends Service
{

    private final static URL UPDATECATALOGWEBSERVICE_WSDL_LOCATION;
    private final static WebServiceException UPDATECATALOGWEBSERVICE_EXCEPTION;
    private final static QName UPDATECATALOGWEBSERVICE_QNAME = new QName("http://www.example.org/update_catalog_webservice/", "update_catalog_webservice");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("file:/D:/workspace/ebut/WebContent/wsdl/updateCatalog.wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        UPDATECATALOGWEBSERVICE_WSDL_LOCATION = url;
        UPDATECATALOGWEBSERVICE_EXCEPTION = e;
    }

    public UpdateCatalogWebservice() {
        super(__getWsdlLocation(), UPDATECATALOGWEBSERVICE_QNAME);
    }

    public UpdateCatalogWebservice(WebServiceFeature... features) {
        super(__getWsdlLocation(), UPDATECATALOGWEBSERVICE_QNAME, features);
    }

    public UpdateCatalogWebservice(URL wsdlLocation) {
        super(wsdlLocation, UPDATECATALOGWEBSERVICE_QNAME);
    }

    public UpdateCatalogWebservice(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, UPDATECATALOGWEBSERVICE_QNAME, features);
    }

    public UpdateCatalogWebservice(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public UpdateCatalogWebservice(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns UpdateProductInterface
     */
    @WebEndpoint(name = "update_catalog_webserviceSOAP")
    public UpdateProductInterface getUpdateCatalogWebserviceSOAP() {
        return super.getPort(new QName("http://www.example.org/update_catalog_webservice/", "update_catalog_webserviceSOAP"), UpdateProductInterface.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns UpdateProductInterface
     */
    @WebEndpoint(name = "update_catalog_webserviceSOAP")
    public UpdateProductInterface getUpdateCatalogWebserviceSOAP(WebServiceFeature... features) {
        return super.getPort(new QName("http://www.example.org/update_catalog_webservice/", "update_catalog_webserviceSOAP"), UpdateProductInterface.class, features);
    }

    private static URL __getWsdlLocation() {
        if (UPDATECATALOGWEBSERVICE_EXCEPTION!= null) {
            throw UPDATECATALOGWEBSERVICE_EXCEPTION;
        }
        return UPDATECATALOGWEBSERVICE_WSDL_LOCATION;
    }

}
