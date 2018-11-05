
package org.bellatrix.services.ws.accounts;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for balanceInquiryResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="balanceInquiryResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="account" type="{http://services.bellatrix.org/}accountView" minOccurs="0"/>
 *         &lt;element name="balance" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="formattedBalance" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="formattedReservedAmount" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="member" type="{http://services.bellatrix.org/}memberView" minOccurs="0"/>
 *         &lt;element name="reservedAmount" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
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
@XmlType(name = "balanceInquiryResponse", propOrder = {
    "account",
    "balance",
    "formattedBalance",
    "formattedReservedAmount",
    "member",
    "reservedAmount",
    "status"
})
public class BalanceInquiryResponse {

    protected AccountView account;
    protected BigDecimal balance;
    protected String formattedBalance;
    protected String formattedReservedAmount;
    protected MemberView member;
    protected BigDecimal reservedAmount;
    protected ResponseStatus status;

    /**
     * Gets the value of the account property.
     * 
     * @return
     *     possible object is
     *     {@link AccountView }
     *     
     */
    public AccountView getAccount() {
        return account;
    }

    /**
     * Sets the value of the account property.
     * 
     * @param value
     *     allowed object is
     *     {@link AccountView }
     *     
     */
    public void setAccount(AccountView value) {
        this.account = value;
    }

    /**
     * Gets the value of the balance property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getBalance() {
        return balance;
    }

    /**
     * Sets the value of the balance property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setBalance(BigDecimal value) {
        this.balance = value;
    }

    /**
     * Gets the value of the formattedBalance property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFormattedBalance() {
        return formattedBalance;
    }

    /**
     * Sets the value of the formattedBalance property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFormattedBalance(String value) {
        this.formattedBalance = value;
    }

    /**
     * Gets the value of the formattedReservedAmount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFormattedReservedAmount() {
        return formattedReservedAmount;
    }

    /**
     * Sets the value of the formattedReservedAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFormattedReservedAmount(String value) {
        this.formattedReservedAmount = value;
    }

    /**
     * Gets the value of the member property.
     * 
     * @return
     *     possible object is
     *     {@link MemberView }
     *     
     */
    public MemberView getMember() {
        return member;
    }

    /**
     * Sets the value of the member property.
     * 
     * @param value
     *     allowed object is
     *     {@link MemberView }
     *     
     */
    public void setMember(MemberView value) {
        this.member = value;
    }

    /**
     * Gets the value of the reservedAmount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getReservedAmount() {
        return reservedAmount;
    }

    /**
     * Sets the value of the reservedAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setReservedAmount(BigDecimal value) {
        this.reservedAmount = value;
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
