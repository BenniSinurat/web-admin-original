package com.jpa.optima.admin.controller;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Holder;

import org.bellatrix.services.ws.access.ValidateCredentialResponse;
import org.bellatrix.services.ws.payments.AgentCashoutRequest;
import org.bellatrix.services.ws.payments.AgentCashoutResponse;
import org.bellatrix.services.ws.payments.ConfirmAgentCashoutRequest;
import org.bellatrix.services.ws.payments.ConfirmAgentCashoutResponse;
import org.bellatrix.services.ws.payments.ConfirmPaymentRequest;
import org.bellatrix.services.ws.payments.InquiryRequest;
import org.bellatrix.services.ws.payments.InquiryResponse;
import org.bellatrix.services.ws.payments.Payment;
import org.bellatrix.services.ws.payments.PaymentRequest;
import org.bellatrix.services.ws.payments.PaymentResponse;
import org.bellatrix.services.ws.payments.PaymentService;
import org.bellatrix.services.ws.payments.RequestPaymentConfirmationResponse;
import org.bellatrix.services.ws.payments.ResponseStatus;
import org.bellatrix.services.ws.payments.ReversalRequest;
import org.bellatrix.services.ws.payments.ReversalResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jpa.optima.admin.model.CashoutMember;
import com.jpa.optima.admin.model.TopupMember;
import com.jpa.optima.admin.model.TransferMember;

@Component
public class PaymentProcessor {

	@Autowired
	private ContextLoader contextLoader;
	@Autowired
	private AccessProcessor accessProcessor;

	public InquiryResponse transferInquiry(TransferMember transfer, String username) throws MalformedURLException {
		URL url = new URL(contextLoader.getHostWSUrl() + "payments?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "PaymentService");
		PaymentService service = new PaymentService(url, qName);
		Payment client = service.getPaymentPort();

		org.bellatrix.services.ws.payments.Header headerPayment = new org.bellatrix.services.ws.payments.Header();
		headerPayment.setToken(contextLoader.getHeaderToken());
		Holder<org.bellatrix.services.ws.payments.Header> paymentHeaderAuth = new Holder<org.bellatrix.services.ws.payments.Header>();
		paymentHeaderAuth.value = headerPayment;

		String str[] = transfer.getToMember().split("-");
		
		InquiryRequest inquiryRequest = new InquiryRequest();
		inquiryRequest.setFromMember(username);
		inquiryRequest.setToMember(str[0]);
		inquiryRequest.setAmount(BigDecimal.valueOf(transfer.getAmount()));
		inquiryRequest.setTransferTypeID(transfer.getTransferTypeID());

		InquiryResponse inquiryResponse = client.doInquiry(paymentHeaderAuth, inquiryRequest);

		return inquiryResponse;
	}

	public PaymentResponse transferPayment(String username, Integer amount, String toMember, String description,
			String credential, Integer transferTypeID) throws MalformedURLException {
		URL url = new URL(contextLoader.getHostWSUrl() + "payments?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "PaymentService");
		PaymentService service = new PaymentService(url, qName);
		Payment client = service.getPaymentPort();

		org.bellatrix.services.ws.payments.Header headerPayment = new org.bellatrix.services.ws.payments.Header();
		headerPayment.setToken(contextLoader.getHeaderToken());
		Holder<org.bellatrix.services.ws.payments.Header> paymentHeaderAuth = new Holder<org.bellatrix.services.ws.payments.Header>();
		paymentHeaderAuth.value = headerPayment;

		String str[] = toMember.split("-");
		System.out.println("Trf Type ID: "+transferTypeID);
		PaymentRequest paymentRequest = new PaymentRequest();
		paymentRequest.setFromMember(username);
		paymentRequest.setToMember(str[0]);
		paymentRequest.setAmount(BigDecimal.valueOf(amount));
		paymentRequest.setTransferTypeID(transferTypeID);
		paymentRequest.setDescription(description);
		paymentRequest.setTraceNumber(Utils.generateTraceNum());
		paymentRequest.setCredential(credential);
		paymentRequest.setAccessTypeID(contextLoader.getChangeCredentialTypeID());

		PaymentResponse paymentResponse = client.doPayment(paymentHeaderAuth, paymentRequest);

		return paymentResponse;
	}

	public RequestPaymentConfirmationResponse topupInquiry(TopupMember topup, String username)
			throws MalformedURLException {
		URL url = new URL(contextLoader.getHostWSUrl() + "payments?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "PaymentService");
		PaymentService service = new PaymentService(url, qName);
		Payment client = service.getPaymentPort();

		org.bellatrix.services.ws.payments.Header headerPayment = new org.bellatrix.services.ws.payments.Header();
		headerPayment.setToken(contextLoader.getHeaderToken());
		Holder<org.bellatrix.services.ws.payments.Header> paymentHeaderAuth = new Holder<org.bellatrix.services.ws.payments.Header>();
		paymentHeaderAuth.value = headerPayment;

		PaymentRequest paymentRequest = new PaymentRequest();
		paymentRequest.setAmount(BigDecimal.valueOf(topup.getAmount()));
		paymentRequest.setDescription(topup.getDescription());
		paymentRequest.setFromMember(username);
		paymentRequest.setToMember(topup.getToAccount());
		paymentRequest.setOriginator(username);
		paymentRequest.setTransferTypeID(topup.getTransferTypeID());
		paymentRequest.setTraceNumber(Utils.generateTraceNum());
		paymentRequest.setAccessTypeID(contextLoader.getChangeCredentialTypeID());
		paymentRequest.setCredential(topup.getCredential());

		RequestPaymentConfirmationResponse paymentResponse = client.requestPaymentConfirmation(paymentHeaderAuth,
				paymentRequest);

		return paymentResponse;
	}

	public PaymentResponse topupPayment(String otp, String requestID) throws MalformedURLException {
		URL url = new URL(contextLoader.getHostWSUrl() + "payments?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "PaymentService");
		PaymentService service = new PaymentService(url, qName);
		Payment client = service.getPaymentPort();

		org.bellatrix.services.ws.payments.Header headerPayment = new org.bellatrix.services.ws.payments.Header();
		headerPayment.setToken(contextLoader.getHeaderToken());
		Holder<org.bellatrix.services.ws.payments.Header> paymentHeaderAuth = new Holder<org.bellatrix.services.ws.payments.Header>();
		paymentHeaderAuth.value = headerPayment;

		ConfirmPaymentRequest paymentRequest = new ConfirmPaymentRequest();
		paymentRequest.setOtp(otp);
		paymentRequest.setRequestID(requestID);

		PaymentResponse paymentResponse = client.confirmPayment(paymentHeaderAuth, paymentRequest);

		return paymentResponse;
	}

	public RequestPaymentConfirmationResponse cashoutMemberInquiry(TopupMember cashout, String username)
			throws MalformedURLException {
		URL url = new URL(contextLoader.getHostWSUrl() + "payments?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "PaymentService");
		PaymentService service = new PaymentService(url, qName);
		Payment client = service.getPaymentPort();

		org.bellatrix.services.ws.payments.Header headerPayment = new org.bellatrix.services.ws.payments.Header();
		headerPayment.setToken(contextLoader.getHeaderToken());
		Holder<org.bellatrix.services.ws.payments.Header> paymentHeaderAuth = new Holder<org.bellatrix.services.ws.payments.Header>();
		paymentHeaderAuth.value = headerPayment;

		PaymentRequest paymentRequest = new PaymentRequest();
		paymentRequest.setAmount(BigDecimal.valueOf(cashout.getAmount()));
		paymentRequest.setDescription(cashout.getDescription());
		paymentRequest.setFromMember(cashout.getFromAccount());
		paymentRequest.setToMember(username);
		paymentRequest.setOriginator(username);
		paymentRequest.setTransferTypeID(contextLoader.getCashoutAgentTransferTypeID());
		paymentRequest.setTraceNumber(Utils.generateTraceNum());

		RequestPaymentConfirmationResponse paymentResponse = client.requestPaymentConfirmation(paymentHeaderAuth,
				paymentRequest);

		return paymentResponse;
	}

	public AgentCashoutResponse cashoutAgentInquiry(CashoutMember cashout, String username)
			throws MalformedURLException {
		URL url = new URL(contextLoader.getHostWSUrl() + "payments?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "PaymentService");
		PaymentService service = new PaymentService(url, qName);
		Payment client = service.getPaymentPort();

		org.bellatrix.services.ws.payments.Header headerPayment = new org.bellatrix.services.ws.payments.Header();
		headerPayment.setToken(contextLoader.getHeaderToken());
		Holder<org.bellatrix.services.ws.payments.Header> paymentHeaderAuth = new Holder<org.bellatrix.services.ws.payments.Header>();
		paymentHeaderAuth.value = headerPayment;

		AgentCashoutRequest agentCashoutReq = new AgentCashoutRequest();
		agentCashoutReq.setFromMember(cashout.getFromAccount());
		agentCashoutReq.setTransferTypeID(contextLoader.getCashoutAgentTransferTypeID());
		agentCashoutReq.setAmount(BigDecimal.valueOf(cashout.getAmount()));

		AgentCashoutResponse agentCashoutRes = client.agentCashoutConfirmation(paymentHeaderAuth, agentCashoutReq);

		return agentCashoutRes;
	}

	public ConfirmAgentCashoutResponse cashoutAgentPayment(CashoutMember cashout, String username)
			throws MalformedURLException {
		URL url = new URL(contextLoader.getHostWSUrl() + "payments?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "PaymentService");
		PaymentService service = new PaymentService(url, qName);
		Payment client = service.getPaymentPort();

		org.bellatrix.services.ws.payments.Header headerPayment = new org.bellatrix.services.ws.payments.Header();
		headerPayment.setToken(contextLoader.getHeaderTokenNonCredential());
		Holder<org.bellatrix.services.ws.payments.Header> paymentHeaderAuth = new Holder<org.bellatrix.services.ws.payments.Header>();
		paymentHeaderAuth.value = headerPayment;
		
		ValidateCredentialResponse vcRes = accessProcessor.validateCredential(username,
				cashout.getCredential());
		if(vcRes.getStatus().getMessage().equalsIgnoreCase("VALID")){
			ConfirmAgentCashoutRequest cACRequest = new ConfirmAgentCashoutRequest();
			cACRequest.setAmount(BigDecimal.valueOf(cashout.getAmount()));
			cACRequest.setDescription(cashout.getDescription());
			cACRequest.setFromMember(cashout.getFromAccount());
			cACRequest.setToMember(username);
			cACRequest.setOtp(cashout.getOtp());
			cACRequest.setTraceNumber(Utils.generateTraceNum());
			cACRequest.setCredential(cashout.getCredential());
			cACRequest.setAccessTypeID(contextLoader.getChangeCredentialTypeID());

			ConfirmAgentCashoutResponse cACResponse = client.confirmAgentCashout(paymentHeaderAuth, cACRequest);
			return cACResponse;
		}else if(vcRes.getStatus().getMessage().equalsIgnoreCase("BLOCKED")){
			ConfirmAgentCashoutResponse cACResponse = new ConfirmAgentCashoutResponse();
			ResponseStatus rps = new ResponseStatus();
			rps.setResponseCode(vcRes.getStatus().getResponseCode());
			rps.setDescription("Member Blocked");
			rps.setMessage(vcRes.getStatus().getMessage());
			cACResponse.setStatus(rps);
			return cACResponse;
		}else{
			ConfirmAgentCashoutResponse cACResponse = new ConfirmAgentCashoutResponse();
			ResponseStatus rps = new ResponseStatus();
			rps.setResponseCode(vcRes.getStatus().getResponseCode());
			rps.setDescription("Invalid Username/Password");
			rps.setMessage(vcRes.getStatus().getMessage());
			cACResponse.setStatus(rps);
			return cACResponse;
		}
	}

	public ReversalResponse reversePayment(String trxNumber) throws MalformedURLException {
		URL url = new URL(contextLoader.getHostWSUrl() + "payments?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "PaymentService");
		PaymentService service = new PaymentService(url, qName);
		Payment client = service.getPaymentPort();

		org.bellatrix.services.ws.payments.Header headerPayment = new org.bellatrix.services.ws.payments.Header();
		headerPayment.setToken(contextLoader.getHeaderToken());
		Holder<org.bellatrix.services.ws.payments.Header> paymentHeaderAuth = new Holder<org.bellatrix.services.ws.payments.Header>();
		paymentHeaderAuth.value = headerPayment;

		ReversalRequest reversalRes = new ReversalRequest();
		reversalRes.setTransactionNumber(trxNumber);

		ReversalResponse reversalRep = client.reversePayment(paymentHeaderAuth, reversalRes);

		return reversalRep;
	}
}
