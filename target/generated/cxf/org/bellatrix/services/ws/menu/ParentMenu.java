
package org.bellatrix.services.ws.menu;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for parentMenu complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="parentMenu">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="childMenu" type="{http://services.bellatrix.org/}childMenu" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="icon" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="mainMenuid" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="parentMenuName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="sequenceNo" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "parentMenu", propOrder = {
    "childMenu",
    "icon",
    "id",
    "mainMenuid",
    "parentMenuName",
    "sequenceNo"
})
public class ParentMenu {

    @XmlElement(nillable = true)
    protected List<ChildMenu> childMenu;
    protected String icon;
    protected Integer id;
    protected Integer mainMenuid;
    protected String parentMenuName;
    protected Integer sequenceNo;

    /**
     * Gets the value of the childMenu property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the childMenu property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getChildMenu().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ChildMenu }
     * 
     * 
     */
    public List<ChildMenu> getChildMenu() {
        if (childMenu == null) {
            childMenu = new ArrayList<ChildMenu>();
        }
        return this.childMenu;
    }

    /**
     * Gets the value of the icon property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIcon() {
        return icon;
    }

    /**
     * Sets the value of the icon property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIcon(String value) {
        this.icon = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setId(Integer value) {
        this.id = value;
    }

    /**
     * Gets the value of the mainMenuid property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMainMenuid() {
        return mainMenuid;
    }

    /**
     * Sets the value of the mainMenuid property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMainMenuid(Integer value) {
        this.mainMenuid = value;
    }

    /**
     * Gets the value of the parentMenuName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParentMenuName() {
        return parentMenuName;
    }

    /**
     * Sets the value of the parentMenuName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParentMenuName(String value) {
        this.parentMenuName = value;
    }

    /**
     * Gets the value of the sequenceNo property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getSequenceNo() {
        return sequenceNo;
    }

    /**
     * Sets the value of the sequenceNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setSequenceNo(Integer value) {
        this.sequenceNo = value;
    }

}
