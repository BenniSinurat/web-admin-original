
package org.bellatrix.services.ws.interbanks;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.bellatrix.services.ws.interbanks package. 
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
    private final static QName _BankAccountTransferInquiry_QNAME = new QName("http://services.bellatrix.org/", "bankAccountTransferInquiry");
    private final static QName _RegisterAccountTransfer_QNAME = new QName("http://services.bellatrix.org/", "registerAccountTransfer");
    private final static QName _LoadBankTransferResponse_QNAME = new QName("http://services.bellatrix.org/", "loadBankTransferResponse");
    private final static QName _LoadAccountTransferResponse_QNAME = new QName("http://services.bellatrix.org/", "loadAccountTransferResponse");
    private final static QName _TopupReversalResponse_QNAME = new QName("http://services.bellatrix.org/", "topupReversalResponse");
    private final static QName _TopupInquiry_QNAME = new QName("http://services.bellatrix.org/", "topupInquiry");
    private final static QName _RemoveAccountTransfer_QNAME = new QName("http://services.bellatrix.org/", "removeAccountTransfer");
    private final static QName _BankAccountTransferPaymentResponse_QNAME = new QName("http://services.bellatrix.org/", "bankAccountTransferPaymentResponse");
    private final static QName _TopupRequest_QNAME = new QName("http://services.bellatrix.org/", "topupRequest");
    private final static QName _HeaderAuth_QNAME = new QName("http://services.bellatrix.org/", "headerAuth");
    private final static QName _LoadAccountTransfer_QNAME = new QName("http://services.bellatrix.org/", "loadAccountTransfer");
    private final static QName _TopupPayment_QNAME = new QName("http://services.bellatrix.org/", "topupPayment");
    private final static QName _BankAccountTransferInquiryResponse_QNAME = new QName("http://services.bellatrix.org/", "bankAccountTransferInquiryResponse");
    private final static QName _BankAccountTransferPayment_QNAME = new QName("http://services.bellatrix.org/", "bankAccountTransferPayment");
    private final static QName _LoadBankTransfer_QNAME = new QName("http://services.bellatrix.org/", "loadBankTransfer");
    private final static QName _TopupRequestResponse_QNAME = new QName("http://services.bellatrix.org/", "topupRequestResponse");
    private final static QName _BankAccountTransferPaymentConfirmation_QNAME = new QName("http://services.bellatrix.org/", "bankAccountTransferPaymentConfirmation");
    private final static QName _TopupReversal_QNAME = new QName("http://services.bellatrix.org/", "topupReversal");
    private final static QName _TopupPaymentResponse_QNAME = new QName("http://services.bellatrix.org/", "topupPaymentResponse");
    private final static QName _TopupInquiryResponse_QNAME = new QName("http://services.bellatrix.org/", "topupInquiryResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.bellatrix.services.ws.interbanks
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link TopupResponse }
     * 
     */
    public TopupResponse createTopupResponse() {
        return new TopupResponse();
    }

    /**
     * Create an instance of {@link TopupRequest }
     * 
     */
    public TopupRequest createTopupRequest() {
        return new TopupRequest();
    }

    /**
     * Create an instance of {@link LoadAccountTransferRequest }
     * 
     */
    public LoadAccountTransferRequest createLoadAccountTransferRequest() {
        return new LoadAccountTransferRequest();
    }

    /**
     * Create an instance of {@link LoadAccountTransferResponse }
     * 
     */
    public LoadAccountTransferResponse createLoadAccountTransferResponse() {
        return new LoadAccountTransferResponse();
    }

    /**
     * Create an instance of {@link TopupParamRequest }
     * 
     */
    public TopupParamRequest createTopupParamRequest() {
        return new TopupParamRequest();
    }

    /**
     * Create an instance of {@link LoadBankTransferResponse }
     * 
     */
    public LoadBankTransferResponse createLoadBankTransferResponse() {
        return new LoadBankTransferResponse();
    }

    /**
     * Create an instance of {@link ResponseStatus }
     * 
     */
    public ResponseStatus createResponseStatus() {
        return new ResponseStatus();
    }

    /**
     * Create an instance of {@link AccountTransfer }
     * 
     */
    public AccountTransfer createAccountTransfer() {
        return new AccountTransfer();
    }

    /**
     * Create an instance of {@link Header }
     * 
     */
    public Header createHeader() {
        return new Header();
    }

    /**
     * Create an instance of {@link LoadBankTransferRequest }
     * 
     */
    public LoadBankTransferRequest createLoadBankTransferRequest() {
        return new LoadBankTransferRequest();
    }

    /**
     * Create an instance of {@link BankAccountTransferResponse }
     * 
     */
    public BankAccountTransferResponse createBankAccountTransferResponse() {
        return new BankAccountTransferResponse();
    }

    /**
     * Create an instance of {@link BankView }
     * 
     */
    public BankView createBankView() {
        return new BankView();
    }

    /**
     * Create an instance of {@link BankAccountTransferRequest }
     * 
     */
    public BankAccountTransferRequest createBankAccountTransferRequest() {
        return new BankAccountTransferRequest();
    }

    /**
     * Create an instance of {@link Exception }
     * 
     */
    public Exception createException() {
        return new Exception();
    }

    /**
     * Create an instance of {@link MemberView }
     * 
     */
    public MemberView createMemberView() {
        return new MemberView();
    }

    /**
     * Create an instance of {@link RegisterAccountTransferRequest }
     * 
     */
    public RegisterAccountTransferRequest createRegisterAccountTransferRequest() {
        return new RegisterAccountTransferRequest();
    }

    /**
     * Create an instance of {@link BankAccountTransferPaymentConfirmation }
     * 
     */
    public BankAccountTransferPaymentConfirmation createBankAccountTransferPaymentConfirmation() {
        return new BankAccountTransferPaymentConfirmation();
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
     * Create an instance of {@link JAXBElement }{@code <}{@link BankAccountTransferRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services.bellatrix.org/", name = "bankAccountTransferInquiry")
    public JAXBElement<BankAccountTransferRequest> createBankAccountTransferInquiry(BankAccountTransferRequest value) {
        return new JAXBElement<BankAccountTransferRequest>(_BankAccountTransferInquiry_QNAME, BankAccountTransferRequest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RegisterAccountTransferRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services.bellatrix.org/", name = "registerAccountTransfer")
    public JAXBElement<RegisterAccountTransferRequest> createRegisterAccountTransfer(RegisterAccountTransferRequest value) {
        return new JAXBElement<RegisterAccountTransferRequest>(_RegisterAccountTransfer_QNAME, RegisterAccountTransferRequest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LoadBankTransferResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services.bellatrix.org/", name = "loadBankTransferResponse")
    public JAXBElement<LoadBankTransferResponse> createLoadBankTransferResponse(LoadBankTransferResponse value) {
        return new JAXBElement<LoadBankTransferResponse>(_LoadBankTransferResponse_QNAME, LoadBankTransferResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LoadAccountTransferResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services.bellatrix.org/", name = "loadAccountTransferResponse")
    public JAXBElement<LoadAccountTransferResponse> createLoadAccountTransferResponse(LoadAccountTransferResponse value) {
        return new JAXBElement<LoadAccountTransferResponse>(_LoadAccountTransferResponse_QNAME, LoadAccountTransferResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TopupResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services.bellatrix.org/", name = "topupReversalResponse")
    public JAXBElement<TopupResponse> createTopupReversalResponse(TopupResponse value) {
        return new JAXBElement<TopupResponse>(_TopupReversalResponse_QNAME, TopupResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TopupParamRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services.bellatrix.org/", name = "topupInquiry")
    public JAXBElement<TopupParamRequest> createTopupInquiry(TopupParamRequest value) {
        return new JAXBElement<TopupParamRequest>(_TopupInquiry_QNAME, TopupParamRequest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LoadAccountTransferRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services.bellatrix.org/", name = "removeAccountTransfer")
    public JAXBElement<LoadAccountTransferRequest> createRemoveAccountTransfer(LoadAccountTransferRequest value) {
        return new JAXBElement<LoadAccountTransferRequest>(_RemoveAccountTransfer_QNAME, LoadAccountTransferRequest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BankAccountTransferResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services.bellatrix.org/", name = "bankAccountTransferPaymentResponse")
    public JAXBElement<BankAccountTransferResponse> createBankAccountTransferPaymentResponse(BankAccountTransferResponse value) {
        return new JAXBElement<BankAccountTransferResponse>(_BankAccountTransferPaymentResponse_QNAME, BankAccountTransferResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TopupRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services.bellatrix.org/", name = "topupRequest")
    public JAXBElement<TopupRequest> createTopupRequest(TopupRequest value) {
        return new JAXBElement<TopupRequest>(_TopupRequest_QNAME, TopupRequest.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link LoadAccountTransferRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services.bellatrix.org/", name = "loadAccountTransfer")
    public JAXBElement<LoadAccountTransferRequest> createLoadAccountTransfer(LoadAccountTransferRequest value) {
        return new JAXBElement<LoadAccountTransferRequest>(_LoadAccountTransfer_QNAME, LoadAccountTransferRequest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TopupParamRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services.bellatrix.org/", name = "topupPayment")
    public JAXBElement<TopupParamRequest> createTopupPayment(TopupParamRequest value) {
        return new JAXBElement<TopupParamRequest>(_TopupPayment_QNAME, TopupParamRequest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BankAccountTransferResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services.bellatrix.org/", name = "bankAccountTransferInquiryResponse")
    public JAXBElement<BankAccountTransferResponse> createBankAccountTransferInquiryResponse(BankAccountTransferResponse value) {
        return new JAXBElement<BankAccountTransferResponse>(_BankAccountTransferInquiryResponse_QNAME, BankAccountTransferResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BankAccountTransferRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services.bellatrix.org/", name = "bankAccountTransferPayment")
    public JAXBElement<BankAccountTransferRequest> createBankAccountTransferPayment(BankAccountTransferRequest value) {
        return new JAXBElement<BankAccountTransferRequest>(_BankAccountTransferPayment_QNAME, BankAccountTransferRequest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LoadBankTransferRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services.bellatrix.org/", name = "loadBankTransfer")
    public JAXBElement<LoadBankTransferRequest> createLoadBankTransfer(LoadBankTransferRequest value) {
        return new JAXBElement<LoadBankTransferRequest>(_LoadBankTransfer_QNAME, LoadBankTransferRequest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TopupResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services.bellatrix.org/", name = "topupRequestResponse")
    public JAXBElement<TopupResponse> createTopupRequestResponse(TopupResponse value) {
        return new JAXBElement<TopupResponse>(_TopupRequestResponse_QNAME, TopupResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BankAccountTransferPaymentConfirmation }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services.bellatrix.org/", name = "bankAccountTransferPaymentConfirmation")
    public JAXBElement<BankAccountTransferPaymentConfirmation> createBankAccountTransferPaymentConfirmation(BankAccountTransferPaymentConfirmation value) {
        return new JAXBElement<BankAccountTransferPaymentConfirmation>(_BankAccountTransferPaymentConfirmation_QNAME, BankAccountTransferPaymentConfirmation.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TopupParamRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services.bellatrix.org/", name = "topupReversal")
    public JAXBElement<TopupParamRequest> createTopupReversal(TopupParamRequest value) {
        return new JAXBElement<TopupParamRequest>(_TopupReversal_QNAME, TopupParamRequest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TopupResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services.bellatrix.org/", name = "topupPaymentResponse")
    public JAXBElement<TopupResponse> createTopupPaymentResponse(TopupResponse value) {
        return new JAXBElement<TopupResponse>(_TopupPaymentResponse_QNAME, TopupResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TopupResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services.bellatrix.org/", name = "topupInquiryResponse")
    public JAXBElement<TopupResponse> createTopupInquiryResponse(TopupResponse value) {
        return new JAXBElement<TopupResponse>(_TopupInquiryResponse_QNAME, TopupResponse.class, null, value);
    }

}
