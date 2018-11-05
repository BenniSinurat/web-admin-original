
package org.bellatrix.services.ws.access;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for credentialStatusResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="credentialStatusResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="accessStatus" type="{http://services.bellatrix.org/}accessStatus" minOccurs="0"/>
 *         &lt;element name="status" type="{http://services.bellatrix.org/}responseStatus" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "credentialStatusResponse", propOrder = {
    "accessStatus",
    "status"
})
public class CredentialStatusResponse {

    protected AccessStatus accessStatus;
    protected ResponseStatus status;

    /**
     * Gets the value of the accessStatus property.
     * 
     * @return
     *     possible object is
     *     {@link AccessStatus }
     *     
     */
    public AccessStatus getAccessStatus() {
        return accessStatus;
    }

    /**
     * Sets the value of the accessStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link AccessStatus }
     *     
     */
    public void setAccessStatus(AccessStatus value) {
        this.accessStatus = value;
    }

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link ResponseStatus }
     *     
     */
    public ResponseStatus getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResponseStatus }
     *     
     */
    public void setStatus(ResponseStatus value) {
        this.status = value;
    }

}
