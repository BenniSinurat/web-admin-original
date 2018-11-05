
package org.bellatrix.services.ws.menu;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.bellatrix.services.ws.menu package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _LoadMenuByGroupsResponse_QNAME = new QName("http://services.bellatrix.org/", "loadMenuByGroupsResponse");
    private final static QName _LoadMenuByGroups_QNAME = new QName("http://services.bellatrix.org/", "loadMenuByGroups");
    private final static QName _HeaderAuth_QNAME = new QName("http://services.bellatrix.org/", "headerAuth");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.bellatrix.services.ws.menu
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link LoadMenuByGroupsRequest }
     * 
     */
    public LoadMenuByGroupsRequest createLoadMenuByGroupsRequest() {
        return new LoadMenuByGroupsRequest();
    }

    /**
     * Create an instance of {@link ResponseStatus }
     * 
     */
    public ResponseStatus createResponseStatus() {
        return new ResponseStatus();
    }

    /**
     * Create an instance of {@link ParentMenu }
     * 
     */
    public ParentMenu createParentMenu() {
        return new ParentMenu();
    }

    /**
     * Create an instance of {@link LoadMenuByGroupsResponse }
     * 
     */
    public LoadMenuByGroupsResponse createLoadMenuByGroupsResponse() {
        return new LoadMenuByGroupsResponse();
    }

    /**
     * Create an instance of {@link ChildMenu }
     * 
     */
    public ChildMenu createChildMenu() {
        return new ChildMenu();
    }

    /**
     * Create an instance of {@link Header }
     * 
     */
    public Header createHeader() {
        return new Header();
    }

    /**
     * Create an instance of {@link MainMenu }
     * 
     */
    public MainMenu createMainMenu() {
        return new MainMenu();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LoadMenuByGroupsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services.bellatrix.org/", name = "loadMenuByGroupsResponse")
    public JAXBElement<LoadMenuByGroupsResponse> createLoadMenuByGroupsResponse(LoadMenuByGroupsResponse value) {
        return new JAXBElement<LoadMenuByGroupsResponse>(_LoadMenuByGroupsResponse_QNAME, LoadMenuByGroupsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LoadMenuByGroupsRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services.bellatrix.org/", name = "loadMenuByGroups")
    public JAXBElement<LoadMenuByGroupsRequest> createLoadMenuByGroups(LoadMenuByGroupsRequest value) {
        return new JAXBElement<LoadMenuByGroupsRequest>(_LoadMenuByGroups_QNAME, LoadMenuByGroupsRequest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Header }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services.bellatrix.org/", name = "headerAuth")
    public JAXBElement<Header> createHeaderAuth(Header value) {
        return new JAXBElement<Header>(_HeaderAuth_QNAME, Header.class, null, value);
    }

}
