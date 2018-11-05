package com.jpa.optima.ipg.controller;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Holder;

import org.bellatrix.services.ws.virtualaccount.LoadVAByIDRequest;
import org.bellatrix.services.ws.virtualaccount.LoadVAByIDResponse;
import org.bellatrix.services.ws.virtualaccount.LoadVAEventRequest;
import org.bellatrix.services.ws.virtualaccount.LoadVAEventResponse;
import org.bellatrix.services.ws.virtualaccount.VaRegisterRequest;
import org.bellatrix.services.ws.virtualaccount.VaRegisterResponse;
import org.bellatrix.services.ws.virtualaccount.VirtualAccount;
import org.bellatrix.services.ws.virtualaccount.VirtualAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PaymentPageProcessor {

	@Autowired
	private ContextLoader contextLoader;

	public LoadVAEventResponse loadVAEvent(String ticketID) throws Exception {
		URL url = new URL(contextLoader.getHostWSUrl()+"virtualaccounts?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "VirtualAccountService");
		VirtualAccountService service = new VirtualAccountService(url, qName);
		VirtualAccount client = service.getVirtualAccountPort();

		org.bellatrix.services.ws.virtualaccount.Header headerVA = new org.bellatrix.services.ws.virtualaccount.Header();
		headerVA.setToken(contextLoader.getHeaderToken());
		Holder<org.bellatrix.services.ws.virtualaccount.Header> vaHeaderAuth = new Holder<org.bellatrix.services.ws.virtualaccount.Header>();
		vaHeaderAuth.value = headerVA;

		LoadVAEventRequest loadVAEventRequest = new LoadVAEventRequest();
		loadVAEventRequest.setTicketID(ticketID);
		LoadVAEventResponse loadVAEventResponse = client.loadVAEventByID(vaHeaderAuth, loadVAEventRequest);
		return loadVAEventResponse;
	}

	public VaRegisterResponse registerVABilling(LoadVAEventResponse billing, String billName, String msisdn, String email) throws MalformedURLException {
		URL url = new URL(contextLoader.getHostWSUrl()+"virtualaccounts?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "VirtualAccountService");
		VirtualAccountService service = new VirtualAccountService(url, qName);
		VirtualAccount client = service.getVirtualAccountPort();

		org.bellatrix.services.ws.virtualaccount.Header headerVA = new org.bellatrix.services.ws.virtualaccount.Header();
		headerVA.setToken(contextLoader.getHeaderToken());
		Holder<org.bellatrix.services.ws.virtualaccount.Header> vaHeaderAuth = new Holder<org.bellatrix.services.ws.virtualaccount.Header>();
		vaHeaderAuth.value = headerVA;

		VaRegisterRequest vaRegisterRequest = new VaRegisterRequest();
		vaRegisterRequest.setAmount(billing.getEvent().get(0).getAmount());
		vaRegisterRequest.setBankID(1);
		vaRegisterRequest.setExpiredDateTime(billing.getEvent().get(0).getExpiredAt());
		vaRegisterRequest.setName(billName);
		vaRegisterRequest.setPersistent(false);
		vaRegisterRequest.setReferenceNumber(msisdn);
		vaRegisterRequest.setUsername(billing.getEvent().get(0).getUsername());
		vaRegisterRequest.setEventID(billing.getEvent().get(0).getTicketID());
		vaRegisterRequest.setEmail(email);
		vaRegisterRequest.setMinimumPayment(BigDecimal.ZERO);
		
		VaRegisterResponse vaRegisterResponse = client.registerVA(vaHeaderAuth, vaRegisterRequest);
		
		return vaRegisterResponse;
	}

	public LoadVAByIDResponse loadVAByID(String ticketID) throws MalformedURLException {
		URL url = new URL(contextLoader.getHostWSUrl()+"virtualaccounts?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "VirtualAccountService");
		VirtualAccountService service = new VirtualAccountService(url, qName);
		VirtualAccount client = service.getVirtualAccountPort();

		org.bellatrix.services.ws.virtualaccount.Header headerVA = new org.bellatrix.services.ws.virtualaccount.Header();
		headerVA.setToken(contextLoader.getHeaderToken());
		Holder<org.bellatrix.services.ws.virtualaccount.Header> vaHeaderAuth = new Holder<org.bellatrix.services.ws.virtualaccount.Header>();
		vaHeaderAuth.value = headerVA;

		LoadVAByIDRequest loadVAByIDRequest = new LoadVAByIDRequest();
		loadVAByIDRequest.setTicketID(ticketID);
		LoadVAByIDResponse loadVaByIDResponse = client.loadVAByID(vaHeaderAuth, loadVAByIDRequest);
		
		return loadVaByIDResponse;

	}
}
