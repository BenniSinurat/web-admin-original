package com.jpa.optima.admin.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.stereotype.Component;

@Component
@PropertySource("/WEB-INF/app.properties")
public class ContextLoader {

	@Value("${core.ws.header.token}")
	private String headerToken;
	@Value("${web.credential.type.id}")
	private Integer webCredentialTypeID;
	@Value("${va.event.url}")
	private String VAEventURL;
	@Value("${default.va.bank.id}")
	private String DefaultVABankID;
	@Value("${member.default.group.id}")
	private Integer DefaultGroupID;
	@Value("${p2p.transfer.type.id}")
	private Integer P2PTrfTypeID;
	@Value("${change.credential.type.id}")
	private Integer ChangeCredentialTypeID;
	@Value("${bank.account.transfer.type.id}")
	private Integer BankAccountTransferTypeID;
	@Value("${topup.agent.type.id}")
	private Integer TopupAgentTransferTypeID;
	@Value("${cashout.agent.type.id}")
	private Integer CashoutAgentTransferTypeID;
	@Value("${path.file}")
	private String PathFile;
	@Value("${url.file}")
	private String URLFile;
	@Value("${registered.group.id}")
	private Integer RegisteredGroupID;
	@Value("${merchant.default.group.id}")
	private Integer DefaultMerchantGroupID;
	@Value("${core.ws.header.token.non.credential}")
	private String headerTokenNonCredential;
	@Value("${host.ws.url}")
	private String HostWSUrl;
	@Value("${host.ws.port}")
	private String HostWSPort;
	@Value("${secret.key}")
	private String SecreKey;
	@Value("${site.key}")
	private String SiteKey;
	@Value("${topup.deposit.agent.type.id}")
	private Integer TopupDepositAgent;
	@Value("${transfer.to.deposit.agent.type.id}")
	private Integer TransferToDepositAgent;
	@Value("${pos.type.id}")
	private Integer PosTransferTypeId;

	public String getHeaderToken() {
		return headerToken;
	}

	public void setHeaderToken(String headerToken) {
		this.headerToken = headerToken;
	}

	public Integer getWebCredentialTypeID() {
		return webCredentialTypeID;
	}

	public void setWebCredentialTypeID(Integer webCredentialTypeID) {
		this.webCredentialTypeID = webCredentialTypeID;
	}

	public String getVAEventURL() {
		return VAEventURL;
	}

	public void setVAEventURL(String vAEventURL) {
		VAEventURL = vAEventURL;
	}

	@Bean
	public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	public String getDefaultVABankID() {
		return DefaultVABankID;
	}

	public void setDefaultVABankID(String defaultVABankID) {
		DefaultVABankID = defaultVABankID;
	}

	public Integer getDefaultGroupID() {
		return DefaultGroupID;
	}

	public void setDefaultGroupID(Integer defaultGroupID) {
		DefaultGroupID = defaultGroupID;
	}

	public Integer getP2PTrfTypeID() {
		return P2PTrfTypeID;
	}

	public void setP2PTrfTypeID(Integer p2pTrfTypeID) {
		P2PTrfTypeID = p2pTrfTypeID;
	}

	public Integer getChangeCredentialTypeID() {
		return ChangeCredentialTypeID;
	}

	public void setChangeCredentialTypeID(Integer changeCredentialTypeID) {
		ChangeCredentialTypeID = changeCredentialTypeID;
	}

	public Integer getBankAccountTransferTypeID() {
		return BankAccountTransferTypeID;
	}

	public void setBankAccountTransferTypeID(Integer bankAccountTransferTypeID) {
		BankAccountTransferTypeID = bankAccountTransferTypeID;
	}

	public Integer getTopupAgentTransferTypeID() {
		return TopupAgentTransferTypeID;
	}

	public void setTopupAgentTransferTypeID(Integer topupAgentTransferTypeID) {
		TopupAgentTransferTypeID = topupAgentTransferTypeID;
	}

	public Integer getCashoutAgentTransferTypeID() {
		return CashoutAgentTransferTypeID;
	}

	public void setCashoutAgentTransferTypeID(Integer cashoutAgentTransferTypeID) {
		CashoutAgentTransferTypeID = cashoutAgentTransferTypeID;
	}

	public String getPathFile() {
		return PathFile;
	}

	public void setPathFile(String pathFile) {
		PathFile = pathFile;
	}

	public String getURLFile() {
		return URLFile;
	}

	public void setURLFile(String uRLFile) {
		URLFile = uRLFile;
	}

	public Integer getRegisteredGroupID() {
		return RegisteredGroupID;
	}

	public void setRegisteredGroupID(Integer registeredGroupID) {
		RegisteredGroupID = registeredGroupID;
	}

	public Integer getDefaultMerchantGroupID() {
		return DefaultMerchantGroupID;
	}

	public String getHeaderTokenNonCredential() {
		return headerTokenNonCredential;
	}

	public void setHeaderTokenNonCredential(String headerTokenNonCredential) {
		this.headerTokenNonCredential = headerTokenNonCredential;
	}

	public String getHostWSUrl() {
		return HostWSUrl;
	}

	public void setHostWSUrl(String hostWSUrl) {
		HostWSUrl = hostWSUrl;
	}

	public String getHostWSPort() {
		return HostWSPort;
	}

	public void setHostWSPort(String hostWSPort) {
		HostWSPort = hostWSPort;
	}

	public String getSecreKey() {
		return SecreKey;
	}

	public void setSecreKey(String secreKey) {
		SecreKey = secreKey;
	}

	public String getSiteKey() {
		return SiteKey;
	}

	public void setSiteKey(String siteKey) {
		SiteKey = siteKey;
	}

	public Integer getTopupDepositAgent() {
		return TopupDepositAgent;
	}

	public void setTopupDepositAgent(Integer topupDepositAgent) {
		TopupDepositAgent = topupDepositAgent;
	}

	public Integer getTransferToDepositAgent() {
		return TransferToDepositAgent;
	}

	public void setTransferToDepositAgent(Integer transferToDepositAgent) {
		TransferToDepositAgent = transferToDepositAgent;
	}

	public Integer getPosTransferTypeId() {
		return PosTransferTypeId;
	}

	public void setPosTransferTypeId(Integer posTransferTypeId) {
		PosTransferTypeId = posTransferTypeId;
	}

}
