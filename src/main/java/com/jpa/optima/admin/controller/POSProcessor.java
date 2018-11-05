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

import org.bellatrix.services.ws.pos.DeletePOSRequest;
import org.bellatrix.services.ws.pos.Exception_Exception;
import org.bellatrix.services.ws.pos.LoadTerminalByIDRequest;
import org.bellatrix.services.ws.pos.LoadTerminalByUsernameRequest;
import org.bellatrix.services.ws.pos.Pos;
import org.bellatrix.services.ws.pos.PosService;
import org.bellatrix.services.ws.pos.RegisterPOSRequest;
import org.bellatrix.services.ws.pos.TerminalInquiryResponse;
import org.bellatrix.services.ws.pos.UpdatePOSRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class POSProcessor {
	@Autowired
	private ContextLoader contextLoader;

	public String loadPOSList(String username, Integer currentPage, Integer pageSize)
			throws MalformedURLException, Exception_Exception {
		URL url = new URL(contextLoader.getHostWSUrl() + "pos?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "PosService");
		PosService service = new PosService(url, qName);
		Pos client = service.getPosPort();
		
		org.bellatrix.services.ws.pos.Header headerPos = new org.bellatrix.services.ws.pos.Header();
		headerPos.setToken(contextLoader.getHeaderToken());
		Holder<org.bellatrix.services.ws.pos.Header> posHeaderAuth = new Holder<org.bellatrix.services.ws.pos.Header>();
		posHeaderAuth.value = headerPos;

		LoadTerminalByUsernameRequest loadTerminalByUsernameReq = new LoadTerminalByUsernameRequest();
		loadTerminalByUsernameReq.setUsername(username);
		loadTerminalByUsernameReq.setCurrentPage(currentPage);
		loadTerminalByUsernameReq.setPageSize(pageSize);

		TerminalInquiryResponse loadTerminalByUsernameRes = client.loadTerminalByUsername(posHeaderAuth,
				loadTerminalByUsernameReq);
		List<Map<String, Object>> trxList = new LinkedList<Map<String, Object>>();
		for (int i = 0; i < loadTerminalByUsernameRes.getTerminal().size(); i++) {
			Map<String, Object> trxContent = new HashMap<String, Object>();
			trxContent.put("id", loadTerminalByUsernameRes.getTerminal().get(i).getId());
			trxContent.put("username", loadTerminalByUsernameRes.getToMember().getUsername());
			trxContent.put("address", loadTerminalByUsernameRes.getTerminal().get(i).getAddress());
			trxContent.put("city", loadTerminalByUsernameRes.getTerminal().get(i).getCity());
			trxContent.put("postalCode", loadTerminalByUsernameRes.getTerminal().get(i).getPostalCode());
			trxContent.put("amount", loadTerminalByUsernameRes.getTerminal().get(i).getAmount());
			trxList.add(trxContent);
		}

		Map<String, Object> trxMap = new HashMap<String, Object>();
		trxMap.put("data", trxList);
		trxMap.put("recordsTotal", trxList.size());
		trxMap.put("recordsFiltered", trxList.size());

		return Utils.toJSON(trxMap);
	}

	public void registerPOS(com.jpa.optima.admin.model.Pos pos, String username)
			throws MalformedURLException, Exception_Exception {
		URL url = new URL(contextLoader.getHostWSUrl() + "pos?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "PosService");
		PosService service = new PosService(url, qName);
		Pos client = service.getPosPort();

		org.bellatrix.services.ws.pos.Header headerPos = new org.bellatrix.services.ws.pos.Header();
		headerPos.setToken(contextLoader.getHeaderToken());
		Holder<org.bellatrix.services.ws.pos.Header> posHeaderAuth = new Holder<org.bellatrix.services.ws.pos.Header>();
		posHeaderAuth.value = headerPos;

		RegisterPOSRequest registerPosReq = new RegisterPOSRequest();
		registerPosReq.setAddress(pos.getAddress());
		registerPosReq.setCity(pos.getCity());
		registerPosReq.setEmail(pos.getEmail());
		registerPosReq.setMsisdn(pos.getMsisdn());
		registerPosReq.setName(pos.getName());
		registerPosReq.setPic(pos.getPic());
		registerPosReq.setPostalCode(pos.getPostalCode());
		registerPosReq.setTransferTypeID(contextLoader.getPosTransferTypeId());
		registerPosReq.setUsername(username);
		System.out.println("Payment: " + pos.getPayment());
		if (pos.getPayment().equalsIgnoreCase("fixedAmount")) {
			registerPosReq.setFixedAmount(true);
			registerPosReq.setOpenPayment(false);
			registerPosReq.setAmount(BigDecimal.valueOf(pos.getAmount()));
		} else {
			registerPosReq.setFixedAmount(false);
			registerPosReq.setOpenPayment(true);
			registerPosReq.setAmount(BigDecimal.valueOf(0));
		}

		client.registerPOS(posHeaderAuth, registerPosReq);
	}

	public void deletePos(Integer terminalID, String username) throws MalformedURLException, Exception_Exception {
		URL url = new URL(contextLoader.getHostWSUrl() + "pos?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "PosService");
		PosService service = new PosService(url, qName);
		Pos client = service.getPosPort();

		org.bellatrix.services.ws.pos.Header headerPos = new org.bellatrix.services.ws.pos.Header();
		headerPos.setToken(contextLoader.getHeaderToken());
		Holder<org.bellatrix.services.ws.pos.Header> posHeaderAuth = new Holder<org.bellatrix.services.ws.pos.Header>();
		posHeaderAuth.value = headerPos;

		DeletePOSRequest deletePosReq = new DeletePOSRequest();
		deletePosReq.setTerminalID(terminalID);
		deletePosReq.setUsername(username);

		client.deletePOS(posHeaderAuth, deletePosReq);
	}

	public void updatePos(com.jpa.optima.admin.model.Pos pos, String username)
			throws MalformedURLException, Exception_Exception {
		URL url = new URL(contextLoader.getHostWSUrl() + "pos?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "PosService");
		PosService service = new PosService(url, qName);
		Pos client = service.getPosPort();

		org.bellatrix.services.ws.pos.Header headerPos = new org.bellatrix.services.ws.pos.Header();
		headerPos.setToken(contextLoader.getHeaderToken());
		Holder<org.bellatrix.services.ws.pos.Header> posHeaderAuth = new Holder<org.bellatrix.services.ws.pos.Header>();
		posHeaderAuth.value = headerPos;

		UpdatePOSRequest updatePosReq = new UpdatePOSRequest();
		updatePosReq.setTerminalID(pos.getId());
		updatePosReq.setAddress(pos.getAddress());
		updatePosReq.setCity(pos.getCity());
		updatePosReq.setEmail(pos.getEmail());
		updatePosReq.setMsisdn(pos.getMsisdn());
		updatePosReq.setName(pos.getName());
		updatePosReq.setPic(pos.getPic());
		updatePosReq.setPostalCode(pos.getPostalCode());
		updatePosReq.setTransferTypeID(contextLoader.getPosTransferTypeId());
		updatePosReq.setUsername(username);
		System.out.println("Payment: " + pos.getPayment());
		if (pos.getPayment().equalsIgnoreCase("fixedAmount")) {
			updatePosReq.setFixedAmount(true);
			updatePosReq.setOpenPayment(false);
			updatePosReq.setAmount(BigDecimal.valueOf(pos.getAmount()));
		} else {
			updatePosReq.setFixedAmount(false);
			updatePosReq.setOpenPayment(true);
			updatePosReq.setAmount(BigDecimal.valueOf(0));
		}

		client.updatePOS(posHeaderAuth, updatePosReq);
	}

	public Map<String, Object> detailPos(String username, Integer Id)
			throws MalformedURLException, Exception_Exception {
		URL url = new URL(contextLoader.getHostWSUrl() + "pos?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "PosService");
		PosService service = new PosService(url, qName);
		Pos client = service.getPosPort();

		org.bellatrix.services.ws.pos.Header headerPos = new org.bellatrix.services.ws.pos.Header();
		headerPos.setToken(contextLoader.getHeaderToken());
		Holder<org.bellatrix.services.ws.pos.Header> posHeaderAuth = new Holder<org.bellatrix.services.ws.pos.Header>();
		posHeaderAuth.value = headerPos;

		LoadTerminalByIDRequest loadTerminalByIDReq = new LoadTerminalByIDRequest();
		loadTerminalByIDReq.setTerminalID(Id);
		loadTerminalByIDReq.setToMember(username);

		TerminalInquiryResponse res = client.loadTerminalByID(posHeaderAuth, loadTerminalByIDReq);

		Map<String, Object> posDetailMap = new HashMap<String, Object>();
		posDetailMap.put("address", res.getTerminal().get(0).getAddress());
		posDetailMap.put("city", res.getTerminal().get(0).getCity());
		posDetailMap.put("id", res.getTerminal().get(0).getId());
		posDetailMap.put("name", res.getTerminal().get(0).getName());
		posDetailMap.put("postalCode", res.getTerminal().get(0).getPostalCode());
		posDetailMap.put("pic", res.getTerminal().get(0).getPic());
		posDetailMap.put("msisdn", res.getTerminal().get(0).getMsisdn());
		posDetailMap.put("email", res.getTerminal().get(0).getEmail());
		posDetailMap.put("fixedAmount", res.getTerminal().get(0).isFixedAmount());
		posDetailMap.put("openPayment", res.getTerminal().get(0).isOpenPayment());
		if (res.getTerminal().get(0).getAmount() == null) {
			posDetailMap.put("Amount", BigDecimal.valueOf(0));
		} else {
			posDetailMap.put("Amount", res.getTerminal().get(0).getAmount());
		}

		return posDetailMap;
	}

}
