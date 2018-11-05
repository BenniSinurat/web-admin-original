package org.bellatrix.services.ws.groups;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 2.7.15
 * 2018-11-02T13:53:10.263+07:00
 * Generated source version: 2.7.15
 * 
 */
@WebServiceClient(name = "GroupService", 
                  wsdlLocation = "http://149.129.214.53:8081/bellatrix/host/services/ws/groups?wsdl",
                  targetNamespace = "http://services.bellatrix.org/") 
public class GroupService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://services.bellatrix.org/", "GroupService");
    public final static QName GroupPort = new QName("http://services.bellatrix.org/", "GroupPort");
    static {
        URL url = null;
        try {
            url = new URL("http://149.129.214.53:8081/bellatrix/host/services/ws/groups?wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(GroupService.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "http://149.129.214.53:8081/bellatrix/host/services/ws/groups?wsdl");
        }
        WSDL_LOCATION = url;
    }

    public GroupService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public GroupService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public GroupService() {
        super(WSDL_LOCATION, SERVICE);
    }
    
    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public GroupService(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public GroupService(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public GroupService(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     *
     * @return
     *     returns Group
     */
    @WebEndpoint(name = "GroupPort")
    public Group getGroupPort() {
        return super.getPort(GroupPort, Group.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns Group
     */
    @WebEndpoint(name = "GroupPort")
    public Group getGroupPort(WebServiceFeature... features) {
        return super.getPort(GroupPort, Group.class, features);
    }

}
