package com.jpa.optima.admin.controller;

import org.bellatrix.services.ws.access.Access;
import org.bellatrix.services.ws.access.AccessService;
import org.bellatrix.services.ws.access.ChangeCredentialRequest;
import org.bellatrix.services.ws.access.CreateCredentialRequest;
import org.bellatrix.services.ws.access.CredentialStatusRequest;
import org.bellatrix.services.ws.access.CredentialStatusResponse;
import org.bellatrix.services.ws.access.Exception_Exception;
import org.bellatrix.services.ws.access.ResetCredentialRequest;
import org.bellatrix.services.ws.access.ResetCredentialResponse;
import org.bellatrix.services.ws.access.UnblockCredentialRequest;
import org.bellatrix.services.ws.access.ValidateCredentialRequest;
import org.bellatrix.services.ws.access.ValidateCredentialResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jpa.optima.admin.model.ChangeCredential;
import com.jpa.optima.admin.model.Member;
import com.jpa.optima.admin.model.UpgradeMember;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Holder;

@Component
public class AccessProcessor {
	@Autowired
	private ContextLoader contextLoader;

	public void changeCredential(ChangeCredential changeCredential) throws MalformedURLException {
		try {
			URL url = new URL(contextLoader.getHostWSUrl()+"access?wsdl");
			QName qName = new QName(contextLoader.getHostWSPort(), "AccessService");
			AccessService service = new AccessService(url, qName);
			Access client = service.getAccessPort();

			org.bellatrix.services.ws.access.Header headerAccess = new org.bellatrix.services.ws.access.Header();
			headerAccess.setToken(contextLoader.getHeaderToken());
			Holder<org.bellatrix.services.ws.access.Header> accessHeaderAuth = new Holder<org.bellatrix.services.ws.access.Header>();
			accessHeaderAuth.value = headerAccess;

			ChangeCredentialRequest chgCredential = new ChangeCredentialRequest();
			chgCredential.setAccessTypeID(contextLoader.getChangeCredentialTypeID());
			chgCredential.setUsername(changeCredential.getMsisdn());
			chgCredential.setOldCredential(changeCredential.getOldCredential());
			chgCredential.setNewCredential(changeCredential.getNewCredential());

			client.changeCredential(accessHeaderAuth, chgCredential);

		} catch (Exception_Exception e) {
			e.printStackTrace();
		}
	}

	public void unblockCredential(UpgradeMember upgrade) throws Exception_Exception, MalformedURLException {
		URL url = new URL(contextLoader.getHostWSUrl()+"access?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "AccessService");
		AccessService service = new AccessService(url, qName);
		Access client = service.getAccessPort();

		org.bellatrix.services.ws.access.Header headerAccess = new org.bellatrix.services.ws.access.Header();
		headerAccess.setToken(contextLoader.getHeaderToken());
		Holder<org.bellatrix.services.ws.access.Header> accessHeaderAuth = new Holder<org.bellatrix.services.ws.access.Header>();
		accessHeaderAuth.value = headerAccess;

		UnblockCredentialRequest unblockCredentialReq = new UnblockCredentialRequest();
		unblockCredentialReq.setAccessTypeID(contextLoader.getChangeCredentialTypeID());
		unblockCredentialReq.setUsername(upgrade.getMsisdn());

		client.unblockCredential(accessHeaderAuth, unblockCredentialReq);
	}

	public ResetCredentialResponse resetCredential(UpgradeMember upgrade) throws Exception_Exception, MalformedURLException {
		URL url = new URL(contextLoader.getHostWSUrl()+"access?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "AccessService");
		AccessService service = new AccessService(url, qName);
		Access client = service.getAccessPort();

		org.bellatrix.services.ws.access.Header headerAccess = new org.bellatrix.services.ws.access.Header();
		headerAccess.setToken(contextLoader.getHeaderToken());
		Holder<org.bellatrix.services.ws.access.Header> accessHeaderAuth = new Holder<org.bellatrix.services.ws.access.Header>();
		accessHeaderAuth.value = headerAccess;

		ResetCredentialRequest rcr = new ResetCredentialRequest();
		rcr.setAccessTypeID(contextLoader.getChangeCredentialTypeID());
		rcr.setUsername(upgrade.getMsisdn());
		rcr.setEmail(upgrade.getEmail());

		ResetCredentialResponse rcres = client.resetCredential(accessHeaderAuth, rcr);

		return rcres;
	}

	public CredentialStatusResponse credentialStatus(String username) throws MalformedURLException {
		URL url = new URL(contextLoader.getHostWSUrl()+"access?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "AccessService");
		AccessService service = new AccessService(url, qName);
		Access client = service.getAccessPort();

		org.bellatrix.services.ws.access.Header headerAccess = new org.bellatrix.services.ws.access.Header();
		headerAccess.setToken(contextLoader.getHeaderToken());
		Holder<org.bellatrix.services.ws.access.Header> accessHeaderAuth = new Holder<org.bellatrix.services.ws.access.Header>();
		accessHeaderAuth.value = headerAccess;

		CredentialStatusRequest csr = new CredentialStatusRequest();
		csr.setUsername(username);
		csr.setAccessTypeID(contextLoader.getChangeCredentialTypeID());

		CredentialStatusResponse csres = client.credentialStatus(accessHeaderAuth, csr);

		return csres;
	}

	public void createCredential(Member createCredential, Integer accessTypeID) throws MalformedURLException {
		try {
			URL url = new URL(contextLoader.getHostWSUrl()+"access?wsdl");
			QName qName = new QName(contextLoader.getHostWSPort(), "AccessService");
			AccessService service = new AccessService(url, qName);
			Access client = service.getAccessPort();

			org.bellatrix.services.ws.access.Header headerAccess = new org.bellatrix.services.ws.access.Header();
			headerAccess.setToken(contextLoader.getHeaderToken());
			Holder<org.bellatrix.services.ws.access.Header> accessHeaderAuth = new Holder<org.bellatrix.services.ws.access.Header>();
			accessHeaderAuth.value = headerAccess;

			CreateCredentialRequest ccRes = new CreateCredentialRequest();
			ccRes.setAccessTypeID(accessTypeID);
			ccRes.setUsername(createCredential.getUsername().replace(",", ""));
			ccRes.setCredential(createCredential.getCredential());

			client.createCredential(accessHeaderAuth, ccRes);
		} catch (Exception_Exception e) {
			e.printStackTrace();
		}
	}

	public ValidateCredentialResponse validateCredential(String username, String credential) throws MalformedURLException {
		URL url = new URL(contextLoader.getHostWSUrl()+"access?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "AccessService");
		AccessService service = new AccessService(url, qName);
		Access client = service.getAccessPort();

		org.bellatrix.services.ws.access.Header headerAccess = new org.bellatrix.services.ws.access.Header();
		headerAccess.setToken(contextLoader.getHeaderToken());
		Holder<org.bellatrix.services.ws.access.Header> accessHeaderAuth = new Holder<org.bellatrix.services.ws.access.Header>();
		accessHeaderAuth.value = headerAccess;

		ValidateCredentialRequest validateCredentialReq = new ValidateCredentialRequest();
		validateCredentialReq.setAccessTypeID(contextLoader.getChangeCredentialTypeID());
		validateCredentialReq.setCredential(credential);
		validateCredentialReq.setUsername(username);

		ValidateCredentialResponse validateCredentialRes = client.validateCredential(accessHeaderAuth,
				validateCredentialReq);

		return validateCredentialRes;
	}
}
