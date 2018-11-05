
package org.bellatrix.services.ws.menu;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for mainMenu complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="mainMenu">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="mainMenuName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="parentMenu" type="{http://services.bellatrix.org/}parentMenu" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "mainMenu", propOrder = {
    "id",
    "mainMenuName",
    "parentMenu"
})
public class MainMenu {

    protected Integer id;
    protected String mainMenuName;
    @XmlElement(nillable = true)
    protected List<ParentMenu> parentMenu;

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
     * Gets the value of the mainMenuName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMainMenuName() {
        return mainMenuName;
    }

    /**
     * Sets the value of the mainMenuName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMainMenuName(String value) {
        this.mainMenuName = value;
    }

    /**
     * Gets the value of the parentMenu property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the parentMenu property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getParentMenu().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ParentMenu }
     * 
     * 
     */
    public List<ParentMenu> getParentMenu() {
        if (parentMenu == null) {
            parentMenu = new ArrayList<ParentMenu>();
        }
        return this.parentMenu;
    }

}
