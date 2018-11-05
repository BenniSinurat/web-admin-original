package com.jpa.optima.admin.controller;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.ws.Holder;

import org.bellatrix.services.ws.interbanks.BankAccountTransferRequest;
import org.bellatrix.services.ws.interbanks.BankAccountTransferResponse;
import org.bellatrix.services.ws.interbanks.Exception_Exception;
import org.bellatrix.services.ws.interbanks.InterBank;
import org.bellatrix.services.ws.interbanks.InterBankService;
import org.bellatrix.services.ws.interbanks.LoadAccountTransferRequest;
import org.bellatrix.services.ws.interbanks.LoadAccountTransferResponse;
import org.bellatrix.services.ws.interbanks.LoadBankTransferRequest;
import org.bellatrix.services.ws.interbanks.LoadBankTransferResponse;
import org.bellatrix.services.ws.interbanks.RegisterAccountTransferRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jpa.optima.admin.model.BankAccount;
import com.jpa.optima.admin.model.TransferBank;

@Component
public class InterbankProcessor {
	@Autowired
	private ContextLoader contextLoader;

	public String getAccountTransferList(String username, Integer currentPage, Integer pageSize) {
		try {
			URL url = new URL(contextLoader.getHostWSUrl()+"interbanks?wsdl");
			QName qName = new QName(contextLoader.getHostWSPort(), "InterBankService");
			InterBankService interBankService = new InterBankService(url, qName);
			InterBank client = interBankService.getInterBankPort();

			org.bellatrix.services.ws.interbanks.Header headerInterbanks = new org.bellatrix.services.ws.interbanks.Header();
			headerInterbanks.setToken(contextLoader.getHeaderToken());
			Holder<org.bellatrix.services.ws.interbanks.Header> interbanksHeaderAuth = new Holder<org.bellatrix.services.ws.interbanks.Header>();
			interbanksHeaderAuth.value = headerInterbanks;

			LoadAccountTransferRequest loadAccountTransferRequest = new LoadAccountTransferRequest();
			loadAccountTransferRequest.setUsername(username);
			loadAccountTransferRequest.setCurrentPage(currentPage);
			loadAccountTransferRequest.setPageSize(pageSize);

			LoadAccountTransferResponse loadAccountTransferResponse = client.loadAccountTransfer(interbanksHeaderAuth,
					loadAccountTransferRequest);

			Map<String, Object> trxMap = new HashMap<String, Object>();
			trxMap.put("data", loadAccountTransferResponse.getAccountTransfer());
			trxMap.put("recordsTotal", loadAccountTransferResponse.getAccountTransfer().size());
			trxMap.put("recordsFiltered", loadAccountTransferResponse.getAccountTransfer().size());

			String jsonAccTransfer = Utils.toJSON(trxMap);

			return jsonAccTransfer;
		} catch (Exception ex) {
			return null;
		}
	}

	public List<String> loadAccountTransfer(String username) {
		try {
			URL url = new URL(contextLoader.getHostWSUrl()+"interbanks?wsdl");
			QName qName = new QName(contextLoader.getHostWSPort(), "InterBankService");
			InterBankService interBankService = new InterBankService(url, qName);
			InterBank client = interBankService.getInterBankPort();
			org.bellatrix.services.ws.interbanks.Header headerInterbanks = new org.bellatrix.services.ws.interbanks.Header();

			headerInterbanks.setToken(contextLoader.getHeaderToken());
			Holder<org.bellatrix.services.ws.interbanks.Header> interbanksHeaderAuth = new Holder<org.bellatrix.services.ws.interbanks.Header>();
			interbanksHeaderAuth.value = headerInterbanks;

			LoadAccountTransferRequest loadAccountTransferRequest = new LoadAccountTransferRequest();
			loadAccountTransferRequest.setUsername(username);
			loadAccountTransferRequest.setCurrentPage(0);
			loadAccountTransferRequest.setPageSize(100);

			LoadAccountTransferResponse loadAccTransferResponse = client.loadAccountTransfer(interbanksHeaderAuth,
					loadAccountTransferRequest);

			List<String> accTransferList = new LinkedList<String>();
			if (loadAccTransferResponse.getAccountTransfer().size() > 0) {
				for (int i = 0; i < loadAccTransferResponse.getAccountTransfer().size(); i++) {
					String accTransfer = loadAccTransferResponse.getAccountTransfer().get(i).getAccountNo() + "-"
							+ loadAccTransferResponse.getAccountTransfer().get(i).getAccountName();
					accTransferList.add(accTransfer);
				}
			}

			return accTransferList;
		} catch (Exception ex) {
			return null;
		}
	}

	/*public BankAccountTransferConfirmationResponse transferBankAccount(TransferBank transfer)
			throws Exception_Exception {
		InterBankService interBankService = new InterBankService();
		InterBank client = interBankService.getInterBankPort();
		org.bellatrix.services.ws.interbanks.Header headerInterbanks = new org.bellatrix.services.ws.interbanks.Header();

		headerInterbanks.setToken(contextLoader.getHeaderToken());
		Holder<org.bellatrix.services.ws.interbanks.Header> interbanksHeaderAuth = new Holder<org.bellatrix.services.ws.interbanks.Header>();
		interbanksHeaderAuth.value = headerInterbanks;

		String toAccount[] = transfer.getToAccount().split("-");

		BankAccountTransferConfirmationRequest bankAccTrfConfRequest = new BankAccountTransferConfirmationRequest();
		bankAccTrfConfRequest.setAccessTypeID(contextLoader.getBankAccountTransferTypeID());
		bankAccTrfConfRequest.setAccountNumber(toAccount[0]);
		bankAccTrfConfRequest.setAmount(BigDecimal.valueOf(transfer.getAmount()));
		bankAccTrfConfRequest.setDescription(transfer.getDescription());
		bankAccTrfConfRequest.setTraceNumber(Utils.generateTraceNum());
		bankAccTrfConfRequest.setUsername(transfer.getUsername());
		bankAccTrfConfRequest.setCredential(transfer.getCredential());

		BankAccountTransferConfirmationResponse bankAccTrfConfResponse = client
				.bankAccountTransferRequestConfirmation(interbanksHeaderAuth, bankAccTrfConfRequest);

		return bankAccTrfConfResponse;
	}*/

	public List<String> loadBankTransfer(String username) throws Exception_Exception, MalformedURLException {
		URL url = new URL(contextLoader.getHostWSUrl()+"interbanks?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "InterBankService");
		InterBankService interBankService = new InterBankService(url, qName);
		InterBank client = interBankService.getInterBankPort();

		org.bellatrix.services.ws.interbanks.Header headerInterBanks = new org.bellatrix.services.ws.interbanks.Header();
		headerInterBanks.setToken(contextLoader.getHeaderToken());
		Holder<org.bellatrix.services.ws.interbanks.Header> interbanksHeaderAuth = new Holder<org.bellatrix.services.ws.interbanks.Header>();
		interbanksHeaderAuth.value = headerInterBanks;

		LoadBankTransferRequest loadBankRequest = new LoadBankTransferRequest();
		loadBankRequest.setCurrentPage(0);
		loadBankRequest.setPageSize(100);
		loadBankRequest.setUsername(username);

		LoadBankTransferResponse loadBankResponse = client.loadBankTransfer(interbanksHeaderAuth, loadBankRequest);

		List<String> trxList = new LinkedList<String>();
		if (loadBankResponse.getBankDetails().size() > 0) {
			for (int i = 0; i < loadBankResponse.getBankDetails().size(); i++) {
				String bankList = loadBankResponse.getBankDetails().get(i).getBankCode() + "-"
						+ loadBankResponse.getBankDetails().get(i).getBankName();
				trxList.add(bankList);
			}
		}

		return trxList;
	}

	public void createBankAccount(BankAccount bankAccount, String username) throws Exception_Exception, MalformedURLException {
		URL url = new URL(contextLoader.getHostWSUrl()+"interbanks?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "InterBankService");
		InterBankService interBankService = new InterBankService(url, qName);
		InterBank client = interBankService.getInterBankPort();

		org.bellatrix.services.ws.interbanks.Header headerInterBanks = new org.bellatrix.services.ws.interbanks.Header();
		headerInterBanks.setToken(contextLoader.getHeaderToken());
		Holder<org.bellatrix.services.ws.interbanks.Header> interBankHeaderAuth = new Holder<org.bellatrix.services.ws.interbanks.Header>();
		interBankHeaderAuth.value = headerInterBanks;

		String[] bankCode = bankAccount.getBankTransfer().split("-");

		LoadBankTransferRequest loadBankRequest = new LoadBankTransferRequest();
		loadBankRequest.setCurrentPage(0);
		loadBankRequest.setPageSize(100);
		loadBankRequest.setBankID(Integer.parseInt(bankCode[0]));
		loadBankRequest.setUsername(username);

		LoadBankTransferResponse loadBankResponse = client.loadBankTransfer(interBankHeaderAuth, loadBankRequest);

		RegisterAccountTransferRequest registerAccTrfRequest = new RegisterAccountTransferRequest();
		registerAccTrfRequest.setAccountName(bankAccount.getAccountName());
		registerAccTrfRequest.setAccountNo(bankAccount.getAccountNumber());
		registerAccTrfRequest.setDescription(bankAccount.getDescription());
		registerAccTrfRequest.setBankID(loadBankResponse.getBankDetails().get(0).getBankId());
		registerAccTrfRequest.setUsername(username);

		client.registerAccountTransfer(interBankHeaderAuth, registerAccTrfRequest);
	}

	public Map<String, Object> getBankAccountDetail(String bankAccount, String username)
			throws Exception_Exception, MalformedURLException {
		URL url = new URL(contextLoader.getHostWSUrl()+"interbanks?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "InterBankService");
		InterBankService interBankService = new InterBankService(url, qName);
		InterBank client = interBankService.getInterBankPort();

		org.bellatrix.services.ws.interbanks.Header headerInterBanks = new org.bellatrix.services.ws.interbanks.Header();
		headerInterBanks.setToken(contextLoader.getHeaderToken());
		Holder<org.bellatrix.services.ws.interbanks.Header> interBankHeaderAuth = new Holder<org.bellatrix.services.ws.interbanks.Header>();
		interBankHeaderAuth.value = headerInterBanks;

		LoadAccountTransferRequest loadAccountTransferRequest = new LoadAccountTransferRequest();
		loadAccountTransferRequest.setUsername(username);
		loadAccountTransferRequest.setCurrentPage(0);
		loadAccountTransferRequest.setPageSize(100);
		loadAccountTransferRequest.setAccountNo(bankAccount);

		LoadAccountTransferResponse loadAccTransferResponse = client.loadAccountTransfer(interBankHeaderAuth,
				loadAccountTransferRequest);

		Map<String, Object> bankAccountDetails = new HashMap<String, Object>();
		bankAccountDetails.put("accountName", loadAccTransferResponse.getAccountTransfer().get(0).getAccountName());
		bankAccountDetails.put("accountNumber", loadAccTransferResponse.getAccountTransfer().get(0).getAccountNo());
		bankAccountDetails.put("bankCode", loadAccTransferResponse.getAccountTransfer().get(0).getBankCode());
		bankAccountDetails.put("bankName", loadAccTransferResponse.getAccountTransfer().get(0).getBankName());
		bankAccountDetails.put("description", loadAccTransferResponse.getAccountTransfer().get(0).getDescription());
		bankAccountDetails.put("createdDate", Utils.formatDate(
				loadAccTransferResponse.getAccountTransfer().get(0).getCreatedDate().toGregorianCalendar().getTime()));

		return bankAccountDetails;
	}

	public BankAccountTransferResponse transferBankAccountInquiry(TransferBank transfer) throws Exception_Exception, MalformedURLException {
		URL url = new URL(contextLoader.getHostWSUrl()+"interbanks?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "InterBankService");
		InterBankService interBankService = new InterBankService(url, qName);
		InterBank client = interBankService.getInterBankPort();

		org.bellatrix.services.ws.interbanks.Header headerInterBank = new org.bellatrix.services.ws.interbanks.Header();
		headerInterBank.setToken(contextLoader.getHeaderTokenNonCredential());
		Holder<org.bellatrix.services.ws.interbanks.Header> interBankHeaderAuth = new Holder<org.bellatrix.services.ws.interbanks.Header>();
		interBankHeaderAuth.value = headerInterBank;

		String toAccount[] = transfer.getToAccount().split("-");
		
		BankAccountTransferRequest bankAccTransferReq = new BankAccountTransferRequest();
		bankAccTransferReq.setAccountName(toAccount[1]);
		bankAccTransferReq.setAccountNumber(toAccount[0]);
		bankAccTransferReq.setAmount(BigDecimal.valueOf(transfer.getAmount()));
		bankAccTransferReq.setDescription(transfer.getDescription());
		bankAccTransferReq.setTraceNumber(Utils.generateTraceNum());
		bankAccTransferReq.setUsername(transfer.getUsername());
		bankAccTransferReq.setBankID(1);

		BankAccountTransferResponse bankAccTransferRes = client.bankAccountTransferInquiry(interBankHeaderAuth,
				bankAccTransferReq);

		return bankAccTransferRes;
	}
	
	public BankAccountTransferResponse transferBankAccountPayment(TransferBank transfer)
			throws Exception_Exception, MalformedURLException {
		URL url = new URL(contextLoader.getHostWSUrl()+"interbanks?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "InterBankService");
		InterBankService interBankService = new InterBankService(url, qName);
		InterBank client = interBankService.getInterBankPort();
		org.bellatrix.services.ws.interbanks.Header headerInterbanks = new org.bellatrix.services.ws.interbanks.Header();

		headerInterbanks.setToken(contextLoader.getHeaderToken());
		Holder<org.bellatrix.services.ws.interbanks.Header> interbanksHeaderAuth = new Holder<org.bellatrix.services.ws.interbanks.Header>();
		interbanksHeaderAuth.value = headerInterbanks;

		BankAccountTransferRequest bankAccTransferReq = new BankAccountTransferRequest();
		bankAccTransferReq.setAccessTypeID(contextLoader.getChangeCredentialTypeID());
		bankAccTransferReq.setAccountName(transfer.getToAccountName());
		bankAccTransferReq.setAccountNumber(transfer.getToAccountNo());
		bankAccTransferReq.setAmount(BigDecimal.valueOf(transfer.getAmount()));
		bankAccTransferReq.setCredential(transfer.getCredential());
		bankAccTransferReq.setDescription(transfer.getDescription());
		bankAccTransferReq.setTraceNumber(Utils.generateTraceNum());
		bankAccTransferReq.setUsername(transfer.getUsername());
		bankAccTransferReq.setBankID(1);
		bankAccTransferReq.setOtp(transfer.getOtp());
		
		BankAccountTransferResponse bankAccTransferRes = client
				.bankAccountTransferPayment(interbanksHeaderAuth, bankAccTransferReq);

		return bankAccTransferRes;
	}
}
