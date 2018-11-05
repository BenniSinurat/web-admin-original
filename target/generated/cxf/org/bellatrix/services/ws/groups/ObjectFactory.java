
package org.bellatrix.services.ws.groups;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.bellatrix.services.ws.groups package. 
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

    private final static QName _Exception_QNAME = new QName("http://services.bellatrix.org/", "Exception");
    private final static QName _LoadAllGroups_QNAME = new QName("http://services.bellatrix.org/", "loadAllGroups");
    private final static QName _LoadGroupsByIDResponse_QNAME = new QName("http://services.bellatrix.org/", "loadGroupsByIDResponse");
    private final static QName _HeaderAuth_QNAME = new QName("http://services.bellatrix.org/", "headerAuth");
    private final static QName _LoadAllGroupsResponse_QNAME = new QName("http://services.bellatrix.org/", "loadAllGroupsResponse");
    private final static QName _CreateGroups_QNAME = new QName("http://services.bellatrix.org/", "createGroups");
    private final static QName _LoadGroupsByID_QNAME = new QName("http://services.bellatrix.org/", "loadGroupsByID");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.bellatrix.services.ws.groups
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link LoadGroupsByIDResponse }
     * 
     */
    public LoadGroupsByIDResponse createLoadGroupsByIDResponse() {
        return new LoadGroupsByIDResponse();
    }

    /**
     * Create an instance of {@link Exception }
     * 
     */
    public Exception createException() {
        return new Exception();
    }

    /**
     * Create an instance of {@link LoadGroupsResponse }
     * 
     */
    public LoadGroupsResponse createLoadGroupsResponse() {
        return new LoadGroupsResponse();
    }

    /**
     * Create an instance of {@link ResponseStatus }
     * 
     */
    public ResponseStatus createResponseStatus() {
        return new ResponseStatus();
    }

    /**
     * Create an instance of {@link CreateGroupsRequest }
     * 
     */
    public CreateGroupsRequest createCreateGroupsRequest() {
        return new CreateGroupsRequest();
    }

    /**
     * Create an instance of {@link Groups }
     * 
     */
    public Groups createGroups() {
        return new Groups();
    }

    /**
     * Create an instance of {@link LoadGroupsRequest }
     * 
     */
    public LoadGroupsRequest createLoadGroupsRequest() {
        return new LoadGroupsRequest();
    }

    /**
     * Create an instance of {@link Header }
     * 
     */
    public Header createHeader() {
        return new Header();
    }

    /**
     * Create an instance of {@link LoadGroupsByIDRequest }
     * 
     */
    public LoadGroupsByIDRequest createLoadGroupsByIDRequest() {
        return new LoadGroupsByIDRequest();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Exception }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services.bellatrix.org/", name = "Exception")
    public JAXBElement<Exception> createException(Exception value) {
        return new JAXBElement<Exception>(_Exception_QNAME, Exception.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LoadGroupsRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services.bellatrix.org/", name = "loadAllGroups")
    public JAXBElement<LoadGroupsRequest> createLoadAllGroups(LoadGroupsRequest value) {
        return new JAXBElement<LoadGroupsRequest>(_LoadAllGroups_QNAME, LoadGroupsRequest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LoadGroupsByIDResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services.bellatrix.org/", name = "loadGroupsByIDResponse")
    public JAXBElement<LoadGroupsByIDResponse> createLoadGroupsByIDResponse(LoadGroupsByIDResponse value) {
        return new JAXBElement<LoadGroupsByIDResponse>(_LoadGroupsByIDResponse_QNAME, LoadGroupsByIDResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Header }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services.bellatrix.org/", name = "headerAuth")
    public JAXBElement<Header> createHeaderAuth(Header value) {
        return new JAXBElement<Header>(_HeaderAuth_QNAME, Header.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LoadGroupsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services.bellatrix.org/", name = "loadAllGroupsResponse")
    public JAXBElement<LoadGroupsResponse> createLoadAllGroupsResponse(LoadGroupsResponse value) {
        return new JAXBElement<LoadGroupsResponse>(_LoadAllGroupsResponse_QNAME, LoadGroupsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateGroupsRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services.bellatrix.org/", name = "createGroups")
    public JAXBElement<CreateGroupsRequest> createCreateGroups(CreateGroupsRequest value) {
        return new JAXBElement<CreateGroupsRequest>(_CreateGroups_QNAME, CreateGroupsRequest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LoadGroupsByIDRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services.bellatrix.org/", name = "loadGroupsByID")
    public JAXBElement<LoadGroupsByIDRequest> createLoadGroupsByID(LoadGroupsByIDRequest value) {
        return new JAXBElement<LoadGroupsByIDRequest>(_LoadGroupsByID_QNAME, LoadGroupsByIDRequest.class, null, value);
    }

}
