
package org.bellatrix.services.ws.members;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for memberKYCRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="memberKYCRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="address" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="dateOfBirth" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="groupID" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="idCardNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="imagePath1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="imagePath2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="imagePath3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="motherMaidenName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nationality" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="placeOfBirth" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="sex" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="username" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="work" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "memberKYCRequest", propOrder = {
    "address",
    "dateOfBirth",
    "groupID",
    "idCardNo",
    "imagePath1",
    "imagePath2",
    "imagePath3",
    "motherMaidenName",
    "nationality",
    "placeOfBirth",
    "sex",
    "username",
    "work"
})
public class MemberKYCRequest {

    protected String address;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dateOfBirth;
    protected Integer groupID;
    protected String idCardNo;
    protected String imagePath1;
    protected String imagePath2;
    protected String imagePath3;
    protected String motherMaidenName;
    protected String nationality;
    protected String placeOfBirth;
    protected String sex;
    protected String username;
    protected String work;

    /**
     * Gets the value of the address property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the value of the address property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddress(String value) {
        this.address = value;
    }

    /**
     * Gets the value of the dateOfBirth property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Sets the value of the dateOfBirth property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateOfBirth(XMLGregorianCalendar value) {
        this.dateOfBirth = value;
    }

    /**
     * Gets the value of the groupID property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getGroupID() {
        return groupID;
    }

    /**
     * Sets the value of the groupID property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setGroupID(Integer value) {
        this.groupID = value;
    }

    /**
     * Gets the value of the idCardNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdCardNo() {
        return idCardNo;
    }

    /**
     * Sets the value of the idCardNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdCardNo(String value) {
        this.idCardNo = value;
    }

    /**
     * Gets the value of the imagePath1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getImagePath1() {
        return imagePath1;
    }

    /**
     * Sets the value of the imagePath1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setImagePath1(String value) {
        this.imagePath1 = value;
    }

    /**
     * Gets the value of the imagePath2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getImagePath2() {
        return imagePath2;
    }

    /**
     * Sets the value of the imagePath2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setImagePath2(String value) {
        this.imagePath2 = value;
    }

    /**
     * Gets the value of the imagePath3 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getImagePath3() {
        return imagePath3;
    }

    /**
     * Sets the value of the imagePath3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setImagePath3(String value) {
        this.imagePath3 = value;
    }

    /**
     * Gets the value of the motherMaidenName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMotherMaidenName() {
        return motherMaidenName;
    }

    /**
     * Sets the value of the motherMaidenName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMotherMaidenName(String value) {
        this.motherMaidenName = value;
    }

    /**
     * Gets the value of the nationality property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNationality() {
        return nationality;
    }

    /**
     * Sets the value of the nationality property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNationality(String value) {
        this.nationality = value;
    }

    /**
     * Gets the value of the placeOfBirth property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    /**
     * Sets the value of the placeOfBirth property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPlaceOfBirth(String value) {
        this.placeOfBirth = value;
    }

    /**
     * Gets the value of the sex property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSex() {
        return sex;
    }

    /**
     * Sets the value of the sex property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSex(String value) {
        this.sex = value;
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

    /**
     * Gets the value of the work property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWork() {
        return work;
    }

    /**
     * Sets the value of the work property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWork(String value) {
        this.work = value;
    }

}
