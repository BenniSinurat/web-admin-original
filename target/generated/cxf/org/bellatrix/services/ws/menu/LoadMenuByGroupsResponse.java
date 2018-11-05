
package org.bellatrix.services.ws.menu;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for loadMenuByGroupsResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="loadMenuByGroupsResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="mainMenu" type="{http://services.bellatrix.org/}mainMenu" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="repsonse" type="{http://services.bellatrix.org/}responseStatus" minOccurs="0"/>
 *         &lt;element name="welcomeMenu" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "loadMenuByGroupsResponse", propOrder = {
    "mainMenu",
    "repsonse",
    "welcomeMenu"
})
public class LoadMenuByGroupsResponse {

    @XmlElement(nillable = true)
    protected List<MainMenu> mainMenu;
    protected ResponseStatus repsonse;
    protected String welcomeMenu;

    /**
     * Gets the value of the mainMenu property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the mainMenu property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMainMenu().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MainMenu }
     * 
     * 
     */
    public List<MainMenu> getMainMenu() {
        if (mainMenu == null) {
            mainMenu = new ArrayList<MainMenu>();
        }
        return this.mainMenu;
    }

    /**
     * Gets the value of the repsonse property.
     * 
     * @return
     *     possible object is
     *     {@link ResponseStatus }
     *     
     */
    public ResponseStatus getRepsonse() {
        return repsonse;
    }

    /**
     * Sets the value of the repsonse property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResponseStatus }
     *     
     */
    public void setRepsonse(ResponseStatus value) {
        this.repsonse = value;
    }

    /**
     * Gets the value of the welcomeMenu property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWelcomeMenu() {
        return welcomeMenu;
    }

    /**
     * Sets the value of the welcomeMenu property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWelcomeMenu(String value) {
        this.welcomeMenu = value;
    }

}
