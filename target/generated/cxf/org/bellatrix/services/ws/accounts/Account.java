package org.bellatrix.services.ws.accounts;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * This class was generated by Apache CXF 2.7.15
 * 2018-11-02T13:53:10.111+07:00
 * Generated source version: 2.7.15
 * 
 */
@WebService(targetNamespace = "http://services.bellatrix.org/", name = "Account")
@XmlSeeAlso({ObjectFactory.class})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface Account {

    @WebMethod(action = "loadAccountsByID")
    @WebResult(name = "loadAccountsByIDResponse", targetNamespace = "http://services.bellatrix.org/", partName = "loadAccountsByIDResponse")
    public LoadAccountsByIDResponse loadAccountsByID(
        @WebParam(partName = "headerAuth", mode = WebParam.Mode.INOUT, name = "headerAuth", targetNamespace = "http://services.bellatrix.org/", header = true)
        javax.xml.ws.Holder<Header> headerAuth,
        @WebParam(partName = "loadAccountsByID", name = "loadAccountsByID", targetNamespace = "http://services.bellatrix.org/")
        LoadAccountsByIDRequest loadAccountsByID
    );

    @WebMethod(action = "loadAccounts")
    @WebResult(name = "loadAccountsResponse", targetNamespace = "http://services.bellatrix.org/", partName = "loadAccountsResponse")
    public LoadAccountsResponse loadAccounts(
        @WebParam(partName = "headerAuth", mode = WebParam.Mode.INOUT, name = "headerAuth", targetNamespace = "http://services.bellatrix.org/", header = true)
        javax.xml.ws.Holder<Header> headerAuth,
        @WebParam(partName = "loadAccounts", name = "loadAccounts", targetNamespace = "http://services.bellatrix.org/")
        LoadAccountsRequest loadAccounts
    );

    @WebMethod(action = "updateAccount")
    public void updateAccount(
        @WebParam(partName = "headerAuth", mode = WebParam.Mode.INOUT, name = "headerAuth", targetNamespace = "http://services.bellatrix.org/", header = true)
        javax.xml.ws.Holder<Header> headerAuth,
        @WebParam(partName = "updateAccount", name = "updateAccount", targetNamespace = "http://services.bellatrix.org/")
        AccountsRequest updateAccount
    ) throws TransactionException_Exception;

    @WebMethod(action = "updateAccountPermission")
    public void updateAccountPermission(
        @WebParam(partName = "headerAuth", mode = WebParam.Mode.INOUT, name = "headerAuth", targetNamespace = "http://services.bellatrix.org/", header = true)
        javax.xml.ws.Holder<Header> headerAuth,
        @WebParam(partName = "updateAccountPermission", name = "updateAccountPermission", targetNamespace = "http://services.bellatrix.org/")
        AccountsPermissionRequest updateAccountPermission
    ) throws TransactionException_Exception;

    @WebMethod(action = "createAccount")
    public void createAccount(
        @WebParam(partName = "headerAuth", mode = WebParam.Mode.INOUT, name = "headerAuth", targetNamespace = "http://services.bellatrix.org/", header = true)
        javax.xml.ws.Holder<Header> headerAuth,
        @WebParam(partName = "createAccount", name = "createAccount", targetNamespace = "http://services.bellatrix.org/")
        AccountsRequest createAccount
    ) throws TransactionException_Exception;

    @WebMethod(action = "loadAccountsByGroups")
    @WebResult(name = "loadAccountsByGroupsResponse", targetNamespace = "http://services.bellatrix.org/", partName = "loadAccountsByGroupsResponse")
    public LoadAccountsByGroupsResponse loadAccountsByGroups(
        @WebParam(partName = "headerAuth", mode = WebParam.Mode.INOUT, name = "headerAuth", targetNamespace = "http://services.bellatrix.org/", header = true)
        javax.xml.ws.Holder<Header> headerAuth,
        @WebParam(partName = "loadAccountsByGroups", name = "loadAccountsByGroups", targetNamespace = "http://services.bellatrix.org/")
        LoadAccountsByGroupsRequest loadAccountsByGroups
    );

    @WebMethod(action = "createAccountPermission")
    public void createAccountPermission(
        @WebParam(partName = "headerAuth", mode = WebParam.Mode.INOUT, name = "headerAuth", targetNamespace = "http://services.bellatrix.org/", header = true)
        javax.xml.ws.Holder<Header> headerAuth,
        @WebParam(partName = "createAccountPermission", name = "createAccountPermission", targetNamespace = "http://services.bellatrix.org/")
        AccountsPermissionRequest createAccountPermission
    ) throws TransactionException_Exception;

    @WebMethod(action = "deleteAccountPermission")
    public void deleteAccountPermission(
        @WebParam(partName = "headerAuth", mode = WebParam.Mode.INOUT, name = "headerAuth", targetNamespace = "http://services.bellatrix.org/", header = true)
        javax.xml.ws.Holder<Header> headerAuth,
        @WebParam(partName = "deleteAccountPermission", name = "deleteAccountPermission", targetNamespace = "http://services.bellatrix.org/")
        AccountsPermissionRequest deleteAccountPermission
    ) throws TransactionException_Exception;

    @WebMethod(action = "loadTransactionHistory")
    @WebResult(name = "loadTransactionHistoryResponse", targetNamespace = "http://services.bellatrix.org/", partName = "loadTransactionHistoryResponse")
    public TransactionHistoryResponse loadTransactionHistory(
        @WebParam(partName = "headerAuth", mode = WebParam.Mode.INOUT, name = "headerAuth", targetNamespace = "http://services.bellatrix.org/", header = true)
        javax.xml.ws.Holder<Header> headerAuth,
        @WebParam(partName = "loadTransactionHistory", name = "loadTransactionHistory", targetNamespace = "http://services.bellatrix.org/")
        TransactionHistoryRequest loadTransactionHistory
    );

    @WebMethod(action = "loadBalanceInquiry")
    @WebResult(name = "loadBalanceInquiryResponse", targetNamespace = "http://services.bellatrix.org/", partName = "loadBalanceInquiryResponse")
    public BalanceInquiryResponse loadBalanceInquiry(
        @WebParam(partName = "headerAuth", mode = WebParam.Mode.INOUT, name = "headerAuth", targetNamespace = "http://services.bellatrix.org/", header = true)
        javax.xml.ws.Holder<Header> headerAuth,
        @WebParam(partName = "loadBalanceInquiry", name = "loadBalanceInquiry", targetNamespace = "http://services.bellatrix.org/")
        BalanceInquiryRequest loadBalanceInquiry
    );
}