
package org.example.update_catalog_webservice;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.4-b01
 * Generated source version: 2.2
 * 
 */
@WebService(name = "updateProductInterface", targetNamespace = "http://www.example.org/update_catalog_webservice/")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
@XmlSeeAlso({
    ObjectFactory.class
})
public interface UpdateProductInterface {


    /**
     * 
     * @param updateRequestPart
     * @return
     *     returns org.example.update_catalog_webservice.UpdateResponseType
     * @throws AuthenticationFaultMessage
     */
    @WebMethod(action = "http://www.example.org/update_catalog_webservice/updateCatalog")
    @WebResult(name = "updateResponse", targetNamespace = "http://www.example.org/update_catalog_webservice/", partName = "updateResponsePart")
    public UpdateResponseType updateCatalog(
        @WebParam(name = "updateRequest", targetNamespace = "http://www.example.org/update_catalog_webservice/", partName = "updateRequestPart")
        UpdateRequestType updateRequestPart)
        throws AuthenticationFaultMessage
    ;

}