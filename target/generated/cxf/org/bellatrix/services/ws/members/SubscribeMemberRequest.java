
package org.bellatrix.services.ws.members;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for subscribeMemberRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="subscribeMemberRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="externalMemberFields" type="{http://services.bellatrix.org/}externalMemberFields" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "subscribeMemberRequest", propOrder = {
    "externalMemberFields"
})
public class SubscribeMemberRequest {

    protected ExternalMemberFields externalMemberFields;

    /**
     * Gets the value of the externalMemberFields property.
     * 
     * @return
     *     possible object is
     *     {@link ExternalMemberFields }
     *     
     */
    public ExternalMemberFields getExternalMemberFields() {
        return externalMemberFields;
    }

    /**
     * Sets the value of the externalMemberFields property.
     * 
     * @param value
     *     allowed object is
     *     {@link ExternalMemberFields }
     *     
     */
    public void setExternalMemberFields(ExternalMemberFields value) {
        this.externalMemberFields = value;
    }

}
