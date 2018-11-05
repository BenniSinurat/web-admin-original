
package org.bellatrix.services.ws.access;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for changeCredentialRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="changeCredentialRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="accessTypeID" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="newCredential" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="oldCredential" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="username" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "changeCredentialRequest", propOrder = {
    "accessTypeID",
    "newCredential",
    "oldCredential",
    "username"
})
public class ChangeCredentialRequest {

    protected Integer accessTypeID;
    protected String newCredential;
    protected String oldCredential;
    protected String username;

    /**
     * Gets the value of the accessTypeID property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getAccessTypeID() {
        return accessTypeID;
    }

    /**
     * Sets the value of the accessTypeID property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setAccessTypeID(Integer value) {
        this.accessTypeID = value;
    }

    /**
     * Gets the value of the newCredential property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNewCredential() {
        return newCredential;
    }

    /**
     * Sets the value of the newCredential property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNewCredential(String value) {
        this.newCredential = value;
    }

    /**
     * Gets the value of the oldCredential property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOldCredential() {
        return oldCredential;
    }

    /**
     * Sets the value of the oldCredential property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOldCredential(String value) {
        this.oldCredential = value;
    }

    /**
     * Gets the value of the username property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the value of the username property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUsername(String value) {
        this.username = value;
    }

}
