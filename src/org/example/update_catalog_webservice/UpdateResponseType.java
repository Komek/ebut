
package org.example.update_catalog_webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for updateResponseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="updateResponseType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="updateDate" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="listOfUpdatedProducts" type="{http://www.example.org/update_catalog_webservice/}listOfUpdatedProductsType"/>
 *         &lt;element name="listOfUnavaiableProducts" type="{http://www.example.org/update_catalog_webservice/}listOfUnavaiableProductsType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "updateResponseType", propOrder = {
    "updateDate",
    "listOfUpdatedProducts",
    "listOfUnavaiableProducts"
})
public class UpdateResponseType {

    @XmlElement(required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar updateDate;
    @XmlElement(required = true)
    protected ListOfUpdatedProductsType listOfUpdatedProducts;
    @XmlElement(required = true)
    protected ListOfUnavaiableProductsType listOfUnavaiableProducts;

    /**
     * Gets the value of the updateDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getUpdateDate() {
        return updateDate;
    }

    /**
     * Sets the value of the updateDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setUpdateDate(XMLGregorianCalendar value) {
        this.updateDate = value;
    }

    /**
     * Gets the value of the listOfUpdatedProducts property.
     * 
     * @return
     *     possible object is
     *     {@link ListOfUpdatedProductsType }
     *     
     */
    public ListOfUpdatedProductsType getListOfUpdatedProducts() {
        return listOfUpdatedProducts;
    }

    /**
     * Sets the value of the listOfUpdatedProducts property.
     * 
     * @param value
     *     allowed object is
     *     {@link ListOfUpdatedProductsType }
     *     
     */
    public void setListOfUpdatedProducts(ListOfUpdatedProductsType value) {
        this.listOfUpdatedProducts = value;
    }

    /**
     * Gets the value of the listOfUnavaiableProducts property.
     * 
     * @return
     *     possible object is
     *     {@link ListOfUnavaiableProductsType }
     *     
     */
    public ListOfUnavaiableProductsType getListOfUnavaiableProducts() {
        return listOfUnavaiableProducts;
    }

    /**
     * Sets the value of the listOfUnavaiableProducts property.
     * 
     * @param value
     *     allowed object is
     *     {@link ListOfUnavaiableProductsType }
     *     
     */
    public void setListOfUnavaiableProducts(ListOfUnavaiableProductsType value) {
        this.listOfUnavaiableProducts = value;
    }

}
