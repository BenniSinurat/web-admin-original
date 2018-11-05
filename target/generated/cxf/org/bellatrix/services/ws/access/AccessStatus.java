
package org.bellatrix.services.ws.access;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for accessStatus complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="accessStatus">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="accessTypeID" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="blocked" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="expired" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="expiredDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "accessStatus", propOrder = {
    "accessTypeID",
    "blocked",
    "expired",
    "expiredDate"
})
public class AccessStatus {

    protected Integer accessTypeID;
    protected boolean blocked;
    protected boolean expired;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar expiredDate;

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
     * Gets the value of the blocked property.
     * 
     */
    public boolean isBlocked() {
        return blocked;
    }

    /**
     * Sets the value of the blocked property.
     * 
     */
    public void setBlocked(boolean value) {
        this.blocked = value;
    }

    /**
     * Gets the value of the expired property.
     * 
     */
    public boolean isExpired() {
        return expired;
    }

    /**
     * Sets the value of the expired property.
     * 
     */
    public void setExpired(boolean value) {
        this.expired = value;
    }

    /**
     * Gets the value of the expiredDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getExpiredDate() {
        return expiredDate;
    }

    /**
     * Sets the value of the expiredDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setExpiredDate(XMLGregorianCalendar value) {
        this.expiredDate = value;
    }

}
