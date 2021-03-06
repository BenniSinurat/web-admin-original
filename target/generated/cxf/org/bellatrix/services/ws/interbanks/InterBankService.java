package org.bellatrix.services.ws.interbanks;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 2.7.15
 * 2018-11-02T13:53:10.966+07:00
 * Generated source version: 2.7.15
 * 
 */
@WebServiceClient(name = "InterBankService", 
                  wsdlLocation = "http://149.129.214.53:8081/bellatrix/host/services/ws/interbanks?wsdl",
                  targetNamespace = "http://services.bellatrix.org/") 
public class InterBankService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://services.bellatrix.org/", "InterBankService");
    public final static QName InterBankPort = new QName("http://services.bellatrix.org/", "InterBankPort");
    static {
        URL url = null;
        try {
            url = new URL("http://149.129.214.53:8081/bellatrix/host/services/ws/interbanks?wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(InterBankService.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "http://149.129.214.53:8081/bellatrix/host/services/ws/interbanks?wsdl");
        }
        WSDL_LOCATION = url;
    }

    public InterBankService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public InterBankService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public InterBankService() {
        super(WSDL_LOCATION, SERVICE);
    }
    
    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public InterBankService(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public InterBankService(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public InterBankService(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     *
     * @return
     *     returns InterBank
     */
    @WebEndpoint(name = "InterBankPort")
    public InterBank getInterBankPort() {
        return super.getPort(InterBankPort, InterBank.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns InterBank
     */
    @WebEndpoint(name = "InterBankPort")
    public InterBank getInterBankPort(WebServiceFeature... features) {
        return super.getPort(InterBankPort, InterBank.class, features);
    }

}
