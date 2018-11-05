package com.jpa.optima.admin.controller;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import javax.xml.ws.Holder;

import org.bellatrix.services.ws.virtualaccount.CreateVAEventRequest;
import org.bellatrix.services.ws.virtualaccount.CreateVAEventResponse;
import org.bellatrix.services.ws.virtualaccount.DeleteVAEventRequest;
import org.bellatrix.services.ws.virtualaccount.LoadVAByMemberRequest;
import org.bellatrix.services.ws.virtualaccount.LoadVAByMemberResponse;
import org.bellatrix.services.ws.virtualaccount.LoadVAEventRequest;
import org.bellatrix.services.ws.virtualaccount.LoadVAEventResponse;
import org.bellatrix.services.ws.virtualaccount.VaDeleteRequest;
import org.bellatrix.services.ws.virtualaccount.VaRegisterRequest;
import org.bellatrix.services.ws.virtualaccount.VaRegisterResponse;
import org.bellatrix.services.ws.virtualaccount.VirtualAccount;
import org.bellatrix.services.ws.virtualaccount.VirtualAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jpa.optima.admin.model.VABilling;
import com.jpa.optima.admin.model.VAEvent;

@Component
public class VirtualAccountProcessor {

	@Autowired
	private ContextLoader contextLoader;

	public String loadVAEvent(String username, Integer currentPage, Integer pageSize) throws MalformedURLException {
		URL url = new URL(contextLoader.getHostWSUrl()+"virtualaccounts?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "VirtualAccountService");
		VirtualAccountService service = new VirtualAccountService(url, qName);
		VirtualAccount client = service.getVirtualAccountPort();

		org.bellatrix.services.ws.virtualaccount.Header headerVA = new org.bellatrix.services.ws.virtualaccount.Header();
		headerVA.setToken(contextLoader.getHeaderToken());
		Holder<org.bellatrix.services.ws.virtualaccount.Header> vaHeaderAuth = new Holder<org.bellatrix.services.ws.virtualaccount.Header>();
		vaHeaderAuth.value = headerVA;

		LoadVAEventRequest loadVAEventRequest = new LoadVAEventRequest();
		loadVAEventRequest.setUsername(username);
		loadVAEventRequest.setCurrentPage(currentPage);
		loadVAEventRequest.setPageSize(pageSize);
		LoadVAEventResponse loadVAEventResponse = client.loadVAEvent(vaHeaderAuth, loadVAEventRequest);
		Map<String, Object> trxMap = new HashMap<String, Object>();
		trxMap.put("data", loadVAEventResponse.getEvent());
		trxMap.put("recordsTotal", loadVAEventResponse.getTotalRecords());
		trxMap.put("recordsFiltered", loadVAEventResponse.getTotalRecords());
		return Utils.toJSON(trxMap);
	}

	public String loadVABilling(String username, Integer currentPage, Integer pageSize) throws MalformedURLException {
		URL url = new URL(contextLoader.getHostWSUrl()+"virtualaccounts?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "VirtualAccountService");
		VirtualAccountService service = new VirtualAccountService(url, qName);
		VirtualAccount client = service.getVirtualAccountPort();

		org.bellatrix.services.ws.virtualaccount.Header headerVA = new org.bellatrix.services.ws.virtualaccount.Header();
		headerVA.setToken(contextLoader.getHeaderToken());
		Holder<org.bellatrix.services.ws.virtualaccount.Header> vaHeaderAuth = new Holder<org.bellatrix.services.ws.virtualaccount.Header>();
		vaHeaderAuth.value = headerVA;

		LoadVAByMemberRequest loadVAByMemberRequest = new LoadVAByMemberRequest();
		loadVAByMemberRequest.setUsername(username);
		loadVAByMemberRequest.setCurrentPage(currentPage);
		loadVAByMemberRequest.setPageSize(pageSize);
		LoadVAByMemberResponse loadVAByMemberResponse = client.loadVAByMember(vaHeaderAuth, loadVAByMemberRequest);
		Map<String, Object> trxMap = new HashMap<String, Object>();
		trxMap.put("data", loadVAByMemberResponse.getVaRecord());
		trxMap.put("recordsTotal", loadVAByMemberResponse.getTotalRecords());
		trxMap.put("recordsFiltered", loadVAByMemberResponse.getTotalRecords());
		return Utils.toJSON(trxMap);
	}

	public CreateVAEventResponse createVAEvent(VAEvent event) throws Exception {
		URL url = new URL(contextLoader.getHostWSUrl()+"virtualaccounts?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "VirtualAccountService");
		VirtualAccountService service = new VirtualAccountService(url, qName);
		VirtualAccount client = service.getVirtualAccountPort();

		org.bellatrix.services.ws.virtualaccount.Header headerVA = new org.bellatrix.services.ws.virtualaccount.Header();
		headerVA.setToken(contextLoader.getHeaderToken());
		Holder<org.bellatrix.services.ws.virtualaccount.Header> vaHeaderAuth = new Holder<org.bellatrix.services.ws.virtualaccount.Header>();
		vaHeaderAuth.value = headerVA;

		CreateVAEventRequest createVAEventRequest = new CreateVAEventRequest();
		createVAEventRequest.setAmount(event.getAmount());
		createVAEventRequest.setDescription(event.getDescription());
		createVAEventRequest.setEventName(event.getEventName());

		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(event.getExpired());
		XMLGregorianCalendar xmldate = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);

		createVAEventRequest.setExpiredDateTime(xmldate);
		createVAEventRequest.setUsername(event.getUsername());

		CreateVAEventResponse createVAEventResponse = client.createVAEvent(vaHeaderAuth, createVAEventRequest);

		return createVAEventResponse;
	}

	public VaRegisterResponse createVABilling(VABilling billing) throws Exception {
		URL url = new URL(contextLoader.getHostWSUrl()+"virtualaccounts?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "VirtualAccountService");
		VirtualAccountService service = new VirtualAccountService(url, qName);
		VirtualAccount client = service.getVirtualAccountPort();

		org.bellatrix.services.ws.virtualaccount.Header headerVA = new org.bellatrix.services.ws.virtualaccount.Header();
		headerVA.setToken(contextLoader.getHeaderToken());
		Holder<org.bellatrix.services.ws.virtualaccount.Header> vaHeaderAuth = new Holder<org.bellatrix.services.ws.virtualaccount.Header>();
		vaHeaderAuth.value = headerVA;

		VaRegisterRequest vaRegisterRequest = new VaRegisterRequest();
		vaRegisterRequest.setAmount(billing.getAmount());
		vaRegisterRequest.setBankID(1);

		System.out.println("Expiry :  " + billing.isExpiry());

		if (billing.isExpiry()) {
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(billing.getExpired());
			XMLGregorianCalendar xmldate = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
			vaRegisterRequest.setExpiredDateTime(xmldate);
			vaRegisterRequest.setPersistent(false);
		} else {
			GregorianCalendar gc = new GregorianCalendar();
			XMLGregorianCalendar xmldate = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
			vaRegisterRequest.setPersistent(true);
			vaRegisterRequest.setExpiredDateTime(xmldate);
		}

		vaRegisterRequest.setName(billing.getName());
		vaRegisterRequest.setReferenceNumber(billing.getReferenceNo());
		vaRegisterRequest.setUsername(billing.getUsername());

		VaRegisterResponse vaRegisterResponse = client.registerVA(vaHeaderAuth, vaRegisterRequest);
		return vaRegisterResponse;
	}

	public void deleteVAEvent(String username, String ticketID) throws Exception {
		URL url = new URL(contextLoader.getHostWSUrl()+"virtualaccounts?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "VirtualAccountService");
		VirtualAccountService service = new VirtualAccountService(url, qName);
		VirtualAccount client = service.getVirtualAccountPort();

		org.bellatrix.services.ws.virtualaccount.Header headerVA = new org.bellatrix.services.ws.virtualaccount.Header();
		headerVA.setToken(contextLoader.getHeaderToken());
		Holder<org.bellatrix.services.ws.virtualaccount.Header> vaHeaderAuth = new Holder<org.bellatrix.services.ws.virtualaccount.Header>();
		vaHeaderAuth.value = headerVA;

		DeleteVAEventRequest deleteVAEventRequest = new DeleteVAEventRequest();
		deleteVAEventRequest.setUsername(username);
		deleteVAEventRequest.setTicketID(ticketID);
		client.deleteVAEvent(vaHeaderAuth, deleteVAEventRequest);

	}

	public void deleteVABilling(String username, String paymentCode) throws Exception {
		URL url = new URL(contextLoader.getHostWSUrl()+"virtualaccounts?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "VirtualAccountService");
		VirtualAccountService service = new VirtualAccountService(url, qName);
		VirtualAccount client = service.getVirtualAccountPort();

		org.bellatrix.services.ws.virtualaccount.Header headerVA = new org.bellatrix.services.ws.virtualaccount.Header();
		headerVA.setToken(contextLoader.getHeaderToken());
		Holder<org.bellatrix.services.ws.virtualaccount.Header> vaHeaderAuth = new Holder<org.bellatrix.services.ws.virtualaccount.Header>();
		vaHeaderAuth.value = headerVA;

		VaDeleteRequest deleteVARequest = new VaDeleteRequest();
		deleteVARequest.setUsername(username);
		deleteVARequest.setPaymentCode(paymentCode);
		client.deleteVA(vaHeaderAuth, deleteVARequest);
	}

}
