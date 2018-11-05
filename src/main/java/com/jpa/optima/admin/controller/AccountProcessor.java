package com.jpa.optima.admin.controller;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.xml.namespace.QName;
import javax.xml.ws.Holder;

import org.bellatrix.services.ws.accounts.Account;
import org.bellatrix.services.ws.accounts.AccountService;
import org.bellatrix.services.ws.accounts.BalanceInquiryRequest;
import org.bellatrix.services.ws.accounts.BalanceInquiryResponse;
import org.bellatrix.services.ws.accounts.LoadAccountsByGroupsRequest;
import org.bellatrix.services.ws.accounts.LoadAccountsByGroupsResponse;
import org.bellatrix.services.ws.accounts.LoadAccountsByIDRequest;
import org.bellatrix.services.ws.accounts.LoadAccountsByIDResponse;
import org.bellatrix.services.ws.accounts.TransactionHistoryRequest;
import org.bellatrix.services.ws.accounts.TransactionHistoryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

@Component
public class AccountProcessor {
	@Autowired
	private ContextLoader contextLoader;
	@Autowired
	private HazelcastInstance instance;

	public List<String> getAccountFromGroupID(Integer groupID, String username)
			throws MalformedURLException {
		URL url = new URL(contextLoader.getHostWSUrl() + "accounts?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "AccountService");
		AccountService as = new AccountService(url, qName);
		Account client = as.getAccountPort();

		org.bellatrix.services.ws.accounts.Header headerAccount = new org.bellatrix.services.ws.accounts.Header();
		headerAccount.setToken(contextLoader.getHeaderToken());
		Holder<org.bellatrix.services.ws.accounts.Header> accountHeaderAuth = new Holder<org.bellatrix.services.ws.accounts.Header>();
		accountHeaderAuth.value = headerAccount;

		LoadAccountsByGroupsRequest lbg = new LoadAccountsByGroupsRequest();
		lbg.setGroupID(groupID);

		LoadAccountsByGroupsResponse response = client.loadAccountsByGroups(accountHeaderAuth, lbg);
		List<String> accountList = new LinkedList<String>();
		if (response.getStatus().getMessage().equalsIgnoreCase("PROCESSED")) {
			for (int i = 0; i < response.getAccounts().size(); i++) {
				String composeAccount = "<div class=\"col-md-4\"><p><h2>"
						+ response.getAccounts().get(i).getName().trim() + "</h2></p></div>";
				composeAccount += "<div class=\"col-md-6\"><p>" + response.getAccounts().get(i).getDescription().trim()
						+ "</p></div>";
				composeAccount += "<div class=\"col-md-2\"><p><button type=\"submit\" class=\"btn btn-success\" onclick=\"window.location='/admin/transactionHistory?GroupID="
						+ groupID + "&username=" + username + "&AccountID=" + response.getAccounts().get(i).getId()
						+ "'\">View</button></p></div>";
				accountList.add(composeAccount);
			}
			return accountList;
		} else {
			return null;
		}
	}

	public String getTransactionHistory(String username, Integer accountID, String fromDate, String toDate,
			Integer currentPage, Integer pageSize) throws MalformedURLException {
		IMap<String, List<String>> trxHistoryMap = instance.getMap("TransactionHistory");
		URL url = new URL(contextLoader.getHostWSUrl() + "accounts?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "AccountService");
		AccountService as = new AccountService(url, qName);
		Account client = as.getAccountPort();

		org.bellatrix.services.ws.accounts.Header headerAccount = new org.bellatrix.services.ws.accounts.Header();
		headerAccount.setToken(contextLoader.getHeaderTokenNonCredential());
		Holder<org.bellatrix.services.ws.accounts.Header> accountHeaderAuth = new Holder<org.bellatrix.services.ws.accounts.Header>();
		accountHeaderAuth.value = headerAccount;

		TransactionHistoryRequest trxReq = new TransactionHistoryRequest();
		trxReq.setUsername(username);
		trxReq.setAccountID(accountID);
		trxReq.setCurrentPage(currentPage);
		trxReq.setPageSize(pageSize);
		trxReq.setFromDate(fromDate);
		trxReq.setToDate(toDate);

		TransactionHistoryResponse response = client.loadTransactionHistory(accountHeaderAuth, trxReq);

		List<Map<String, Object>> trxList = new LinkedList<Map<String, Object>>();
		for (int i = 0; i < response.getTransfers().size(); i++) {
			Map<String, Object> trxContent = new HashMap<String, Object>();
			trxContent.put("transactionDate", Utils
					.formatDate(response.getTransfers().get(i).getTransactionDate().toGregorianCalendar().getTime()));

			String fromSelf = response.getTransfers().get(i).getFromMember().getUsername().equalsIgnoreCase(username)
					? "" : response.getTransfers().get(i).getFromMember().getName();
			String toSelf = response.getTransfers().get(i).getToMember().getUsername().equalsIgnoreCase(username) ? ""
					: response.getTransfers().get(i).getToMember().getName();
			
			if((fromSelf.equalsIgnoreCase("")) && (toSelf.equalsIgnoreCase(""))){
				trxContent.put("toFromMember", response.getTransfers().get(i).getFromMember().getName());
			}else{
				trxContent.put("toFromMember", fromSelf + toSelf);
			}
			trxContent.put("amount", Utils.formatAmount(response.getTransfers().get(i).getAmount().abs()));

			String transferTypeName = response.getTransfers().get(i).getTransferType() == null
					? response.getTransfers().get(i).getDescription()
					: response.getTransfers().get(i).getTransferType().getName();
			trxContent.put("transferType", transferTypeName);

			trxContent.put("transactionNumber", response.getTransfers().get(i).getTransactionNumber());
			trxContent.put("traceNumber", response.getTransfers().get(i).getTraceNumber());
			trxContent.put("description", response.getTransfers().get(i).getDescription());
			String remark = response.getTransfers().get(i).getAmount().compareTo(BigDecimal.ZERO) > 0 ? "C" : "D";
			trxContent.put("remark", remark);
			trxList.add(trxContent);
		}

		List<String> jsonList = convertList(trxList, s -> Utils.toJSON(s));
		trxHistoryMap.put(username, jsonList);

		Map<String, Object> trxMap = new HashMap<String, Object>();
		trxMap.put("data", trxList);
		trxMap.put("recordsTotal", response.getTotalRecords() != null ? response.getTotalRecords() : 0);
		trxMap.put("recordsFiltered", response.getDisplayRecords() != null ? response.getDisplayRecords() : 0);

		String jsontrxHistory = Utils.toJSON(trxMap);
		return jsontrxHistory;

	}

	public String searchTransactionHistory(String username, Integer accountID, String fromDate, String toDate,
			Integer start, Integer length, String searchValue) throws MalformedURLException {
		IMap<String, List<String>> trxHistoryMap = instance.getMap("TransactionHistory");
		List<String> cacheList = trxHistoryMap.get(username);
		Pattern pattern = Pattern.compile(".(:\")*" + searchValue);

		StringBuilder bufferList = new StringBuilder();
		Integer matchList = 0;
		if (cacheList != null) {
			bufferList.append("{\"data\":[");

			List<String> matches = cacheList.stream().filter(pattern.asPredicate()).collect(Collectors.toList());
			if (matches != null) {
				bufferList.append(matches.stream().map(Object::toString).collect(Collectors.joining(",")));
				matchList = matches.size();
			}
			bufferList.append("]");
			bufferList.append(",\"recordsTotal\":\"" + matchList + "\"");
			bufferList.append(",\"recordsFiltered\":\"" + matchList + "\"");
			bufferList.append("}");
		} else {
			this.getTransactionHistory(username, accountID, fromDate, toDate, start, length);
			cacheList = trxHistoryMap.get(username);
			bufferList.append("{\"data\":[");

			List<String> matches = cacheList.stream().filter(pattern.asPredicate()).collect(Collectors.toList());
			if (matches != null) {
				bufferList.append(matches.stream().map(Object::toString).collect(Collectors.joining(",")));
				matchList = matches.size();
			}
			bufferList.append("]");
			bufferList.append(",\"recordsTotal\":\"" + matchList + "\"");
			bufferList.append(",\"recordsFiltered\":\"" + matchList + "\"");
			bufferList.append("}");
		}

		return bufferList.toString();
	}

	public Map<String, Object> getAccountDetails(String username, Integer accountID, Integer groupID)
			throws MalformedURLException {
		URL url = new URL(contextLoader.getHostWSUrl() + "accounts?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "AccountService");
		AccountService as = new AccountService(url, qName);
		Account client = as.getAccountPort();
		org.bellatrix.services.ws.accounts.Header headerAccount = new org.bellatrix.services.ws.accounts.Header();
		headerAccount.setToken(contextLoader.getHeaderTokenNonCredential());
		Holder<org.bellatrix.services.ws.accounts.Header> accountHeaderAuth = new Holder<org.bellatrix.services.ws.accounts.Header>();
		accountHeaderAuth.value = headerAccount;

		LoadAccountsByIDRequest loadAccountsByID = new LoadAccountsByIDRequest();
		loadAccountsByID.setId(accountID);
		loadAccountsByID.setGroupID(groupID);

		LoadAccountsByIDResponse loadAccountsByIDResponse = client.loadAccountsByID(accountHeaderAuth,
				loadAccountsByID);

		BalanceInquiryRequest balanceInquiryRequest = new BalanceInquiryRequest();
		balanceInquiryRequest.setUsername(username);
		balanceInquiryRequest.setAccountID(accountID);

		BalanceInquiryResponse balanceInquiryResponse = client.loadBalanceInquiry(accountHeaderAuth,
				balanceInquiryRequest);

		Map<String, Object> accDetailsContentMap = new HashMap<String, Object>();
		accDetailsContentMap.put("accountName", loadAccountsByIDResponse.getAccount().getName());
		accDetailsContentMap.put("creditLimit",
				Utils.formatAmount(loadAccountsByIDResponse.getAccount().getCreditLimit()));
		accDetailsContentMap.put("upperCreditLimit",
				Utils.formatAmount(loadAccountsByIDResponse.getAccount().getUpperCreditLimit()));
		accDetailsContentMap.put("balance", balanceInquiryResponse.getFormattedBalance());
		accDetailsContentMap.put("reservedAmount", balanceInquiryResponse.getFormattedReservedAmount());

		return accDetailsContentMap;
	}

	public static <T, U> List<U> convertList(List<T> from, Function<T, U> func) {
		return from.stream().map(func).collect(Collectors.toList());
	}

	public String loadAccountMember(Integer groupID, Integer pageSize, Integer currentPage)
			throws MalformedURLException {
		URL url = new URL(contextLoader.getHostWSUrl() + "accounts?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "AccountService");
		AccountService as = new AccountService(url, qName);
		Account client = as.getAccountPort();

		org.bellatrix.services.ws.accounts.Header headerAccount = new org.bellatrix.services.ws.accounts.Header();
		headerAccount.setToken(contextLoader.getHeaderToken());
		Holder<org.bellatrix.services.ws.accounts.Header> accountHeaderAuth = new Holder<org.bellatrix.services.ws.accounts.Header>();
		accountHeaderAuth.value = headerAccount;

		LoadAccountsByGroupsRequest lbg = new LoadAccountsByGroupsRequest();
		lbg.setGroupID(groupID);
		LoadAccountsByGroupsResponse response = client.loadAccountsByGroups(accountHeaderAuth, lbg);

		Map<String, Object> trxMap = new HashMap<String, Object>();
		trxMap.put("groupID", groupID);
		trxMap.put("data", response.getAccounts());
		trxMap.put("recordsTotal", response.getAccounts().size());
		trxMap.put("recordsFiltered", response.getAccounts().size());
		return Utils.toJSON(trxMap);
	}

}
