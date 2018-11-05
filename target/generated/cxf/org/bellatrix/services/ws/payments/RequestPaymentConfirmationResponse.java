
package org.bellatrix.services.ws.payments;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for requestPaymentConfirmationResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="requestPaymentConfirmationResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="finalAmount" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="fromMember" type="{http://services.bellatrix.org/}memberView" minOccurs="0"/>
 *         &lt;element name="requestID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="status" type="{http://services.bellatrix.org/}responseStatus" minOccurs="0"/>
 *         &lt;element name="toMember" type="{http://services.bellatrix.org/}memberView" minOccurs="0"/>
 *         &lt;element name="totalFees" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="transactionAmount" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="transferType" type="{http://services.bellatrix.org/}transferTypeFields" minOccurs="0"/>
 *         &lt;element name="twoFactorAuthentication" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "requestPaymentConfirmationResponse", propOrder = {
    "finalAmount",
    "fromMember",
    "requestID",
    "status",
    "toMember",
    "totalFees",
    "transactionAmount",
    "transferType",
    "twoFactorAuthentication"
})
public class RequestPaymentConfirmationResponse {

    protected BigDecimal finalAmount;
    protected MemberView fromMember;
    protected String requestID;
    protected ResponseStatus status;
    protected MemberView toMember;
    protected BigDecimal totalFees;
    protected BigDecimal transactionAmount;
    protected TransferTypeFields transferType;
    protected boolean twoFactorAuthentication;

    /**
     * Gets the value of the finalAmount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getFinalAmount() {
        return finalAmount;
    }

    /**
     * Sets the value of the finalAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setFinalAmount(BigDecimal value) {
        this.finalAmount = value;
    }

    /**
     * Gets the value of the fromMember property.
     * 
     * @return
     *     possible object is
     *     {@link MemberView }
     *     
     */
    public MemberView getFromMember() {
        return fromMember;
    }

    /**
     * Sets the value of the fromMember property.
     * 
     * @param value
     *     allowed object is
     *     {@link MemberView }
     *     
     */
    public void setFromMember(MemberView value) {
        this.fromMember = value;
    }

    /**
     * Gets the value of the requestID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRequestID() {
        return requestID;
    }

    /**
     * Sets the value of the requestID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRequestID(String value) {
        this.requestID = value;
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

    /**
     * Gets the value of the toMember property.
     * 
     * @return
     *     possible object is
     *     {@link MemberView }
     *     
     */
    public MemberView getToMember() {
        return toMember;
    }

    /**
     * Sets the value of the toMember property.
     * 
     * @param value
     *     allowed object is
     *     {@link MemberView }
     *     
     */
    public void setToMember(MemberView value) {
        this.toMember = value;
    }

    /**
     * Gets the value of the totalFees property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTotalFees() {
        return totalFees;
    }

    /**
     * Sets the value of the totalFees property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTotalFees(BigDecimal value) {
        this.totalFees = value;
    }

    /**
     * Gets the value of the transactionAmount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }

    /**
     * Sets the value of the transactionAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTransactionAmount(BigDecimal value) {
        this.transactionAmount = value;
    }

    /**
     * Gets the value of the transferType property.
     * 
     * @return
     *     possible object is
     *     {@link TransferTypeFields }
     *     
     */
    public TransferTypeFields getTransferType() {
        return transferType;
    }

    /**
     * Sets the value of the transferType property.
     * 
     * @param value
     *     allowed object is
     *     {@link TransferTypeFields }
     *     
     */
    public void setTransferType(TransferTypeFields value) {
        this.transferType = value;
    }

    /**
     * Gets the value of the twoFactorAuthentication property.
     * 
     */
    public boolean isTwoFactorAuthentication() {
        return twoFactorAuthentication;
    }

    /**
     * Sets the value of the twoFactorAuthentication property.
     * 
     */
    public void setTwoFactorAuthentication(boolean value) {
        this.twoFactorAuthentication = value;
    }

}
