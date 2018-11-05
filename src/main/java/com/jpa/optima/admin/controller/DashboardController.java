package com.jpa.optima.admin.controller;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.xml.datatype.DatatypeConfigurationException;

import org.apache.commons.io.FilenameUtils;
import org.bellatrix.services.ws.access.CredentialStatusResponse;
import org.bellatrix.services.ws.access.ResetCredentialResponse;
import org.bellatrix.services.ws.access.ValidateCredentialResponse;
import org.bellatrix.services.ws.interbanks.BankAccountTransferResponse;
import org.bellatrix.services.ws.members.ConfirmKYCResponse;
import org.bellatrix.services.ws.members.Exception_Exception;
import org.bellatrix.services.ws.members.LoadMembersResponse;
import org.bellatrix.services.ws.payments.AgentCashoutResponse;
import org.bellatrix.services.ws.payments.ConfirmAgentCashoutResponse;
import org.bellatrix.services.ws.payments.InquiryResponse;
import org.bellatrix.services.ws.payments.PaymentResponse;
import org.bellatrix.services.ws.payments.RequestPaymentConfirmationResponse;
import org.bellatrix.services.ws.payments.ReversalResponse;
import org.bellatrix.services.ws.virtualaccount.CreateVAEventResponse;
import org.bellatrix.services.ws.virtualaccount.VaRegisterResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.jpa.optima.admin.model.AdminMenu;
import com.jpa.optima.admin.model.BankAccount;
import com.jpa.optima.admin.model.CashoutMember;
import com.jpa.optima.admin.model.ChangeCredential;
import com.jpa.optima.admin.model.Member;
import com.jpa.optima.admin.model.Pos;
import com.jpa.optima.admin.model.ReversePayment;
import com.jpa.optima.admin.model.SendMessage;
import com.jpa.optima.admin.model.TopupMember;
import com.jpa.optima.admin.model.TransferBank;
import com.jpa.optima.admin.model.TransferMember;
import com.jpa.optima.admin.model.UpgradeMember;
import com.jpa.optima.admin.model.UploadFiles;
import com.jpa.optima.admin.model.VABilling;
import com.jpa.optima.admin.model.VAEvent;

@Controller
public class DashboardController {

	@Autowired
	private HazelcastInstance instance;
	@Autowired
	private MenuProcessor menuProcessor;
	@Autowired
	private AccountProcessor accountProcessor;
	@Autowired
	private MessageProcessor messageProcessor;
	@Autowired
	private VirtualAccountProcessor virtualAccountProcessor;
	@Autowired
	private ContextLoader contextLoader;
	@Autowired
	private MemberProcessor memberProcessor;
	@Autowired
	private PaymentProcessor paymentProcessor;
	@Autowired
	private InterbankProcessor interbankProcessor;
	@Autowired
	private AccessProcessor accessProcessor;
	@Autowired
	private GroupProcessor groupProcessor;
	@Autowired
	private POSProcessor posProcessor;

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public ModelAndView login(@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			HttpServletResponse response, Model model) throws MalformedURLException {
		if (sessionID.equalsIgnoreCase("defaultCookieValue") || sessionID.equalsIgnoreCase(null)) {
			return new ModelAndView("redirect:/login");
		}
		IMap<String, Member> memberMap = instance.getMap("Member");
		Member member = memberMap.get(sessionID);
		if (member == null) {
			return new ModelAndView("redirect:/login");
		}
		AdminMenu adminMenu = menuProcessor.getMenuList(member.getGroupID());
		List<String> mainMenu = adminMenu.getMainMenu();
		String welcomeMenu = adminMenu.getWelcomeMenu();
		Integer unreadMessage = messageProcessor.getCountUnreadMessages(member.getUsername());
		String unread = unreadMessage != 0 ? String.valueOf(unreadMessage) : "";
		List<String> messageSummary = messageProcessor.loadUnreadMessages(member.getUsername());

		model.addAttribute(member);
		model.addAttribute("mainMenu", mainMenu);
		model.addAttribute("welcomeMenu", welcomeMenu);
		model.addAttribute("unreadMessage", unread);
		model.addAttribute("messageSummary", messageSummary);
		return new ModelAndView("index");
	}

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ModelAndView search(@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			HttpServletResponse response, Model model) throws MalformedURLException {
		if (sessionID.equalsIgnoreCase("defaultCookieValue") || sessionID.equalsIgnoreCase(null)) {
			return new ModelAndView("redirect:/login");
		}
		IMap<String, Member> memberMap = instance.getMap("Member");
		Member member = memberMap.get(sessionID);
		if (member == null) {
			return new ModelAndView("redirect:/login");
		}
		AdminMenu adminMenu = menuProcessor.getMenuList(member.getGroupID());
		List<String> mainMenu = adminMenu.getMainMenu();
		Integer unreadMessage = messageProcessor.getCountUnreadMessages(member.getUsername());
		String unread = unreadMessage != 0 ? String.valueOf(unreadMessage) : "";
		List<String> messageSummary = messageProcessor.loadUnreadMessages(member.getUsername());

		model.addAttribute(member);
		model.addAttribute("mainMenu", mainMenu);
		model.addAttribute("unreadMessage", unread);
		model.addAttribute("messageSummary", messageSummary);

		return new ModelAndView("search");
	}

	@RequestMapping(value = "/accountSelect", method = RequestMethod.GET)
	public ModelAndView accountSelect(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			HttpServletResponse response, Model model) throws MalformedURLException {
		if (sessionID.equalsIgnoreCase("defaultCookieValue") || sessionID.equalsIgnoreCase(null)) {
			return new ModelAndView("redirect:/login");
		}
		IMap<String, Member> memberMap = instance.getMap("Member");
		Member member = memberMap.get(sessionID);
		if (member == null) {
			return new ModelAndView("redirect:/login");
		}
		AdminMenu adminMenu = menuProcessor.getMenuList(member.getGroupID());
		List<String> mainMenu = adminMenu.getMainMenu();
		List<String> accounts = accountProcessor.getAccountFromGroupID(member.getGroupID(), member.getUsername());
		Integer unreadMessage = messageProcessor.getCountUnreadMessages(member.getUsername());
		String unread = unreadMessage != 0 ? String.valueOf(unreadMessage) : "";
		List<String> messageSummary = messageProcessor.loadUnreadMessages(member.getUsername());

		model.addAttribute(member);
		model.addAttribute("mainMenu", mainMenu);
		model.addAttribute("account", accounts);
		model.addAttribute("unreadMessage", unread);
		model.addAttribute("messageSummary", messageSummary);

		return new ModelAndView("accountSelect");
	}

	@RequestMapping(value = "/transactionHistory", method = RequestMethod.GET)
	public ModelAndView transactionHistory(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			HttpServletResponse response, @RequestParam(value = "GroupID") Integer groupID,
			@RequestParam(value = "username") String username, @RequestParam(value = "AccountID") String accountID,
			Model model) throws MalformedURLException {
		if (sessionID.equalsIgnoreCase("defaultCookieValue") || sessionID.equalsIgnoreCase(null)) {
			return new ModelAndView("redirect:/login");
		}
		IMap<String, Member> memberMap = instance.getMap("Member");
		Member member = memberMap.get(sessionID);
		if (member == null) {
			return new ModelAndView("redirect:/login");
		}
		AdminMenu adminMenu = menuProcessor.getMenuList(member.getGroupID());
		List<String> mainMenu = adminMenu.getMainMenu();
		Integer unreadMessage = messageProcessor.getCountUnreadMessages(member.getUsername());
		String unread = unreadMessage != 0 ? String.valueOf(unreadMessage) : "";
		List<String> messageSummary = messageProcessor.loadUnreadMessages(member.getUsername());
		Map<String, Object> accountDetails = accountProcessor.getAccountDetails(username, Integer.valueOf(accountID),
				groupID);
		Map<String, Object> getMemberDetails = memberProcessor.getMemberDetails(username);

		model.addAttribute(member);
		model.addAttribute("username", getMemberDetails.get("username"));
		model.addAttribute("groupID", getMemberDetails.get("groupID"));
		model.addAttribute("groupName", getMemberDetails.get("groupName"));
		model.addAttribute("name", getMemberDetails.get("name"));
		model.addAttribute("email", getMemberDetails.get("email"));
		model.addAttribute("accountID", accountID);
		model.addAttribute("accountName", accountDetails.get("accountName"));
		model.addAttribute("fromDate", Utils.GetDate("yyyy-MM-dd"));
		model.addAttribute("toDate", Utils.GetFutureDate("yyyy-MM-dd", 1));
		model.addAttribute("creditLimit", accountDetails.get("creditLimit"));
		model.addAttribute("upperCreditLimit", accountDetails.get("upperCreditLimit"));
		model.addAttribute("balance", accountDetails.get("balance"));
		model.addAttribute("reservedAmount", accountDetails.get("reservedAmount"));
		model.addAttribute("mainMenu", mainMenu);
		model.addAttribute("unreadMessage", unread);
		model.addAttribute("messageSummary", messageSummary);

		if (member.getGroupName().equalsIgnoreCase("ADMIN")) {
			return new ModelAndView("transactionHistoryAdmin");
		} else {
			return new ModelAndView("transactionHistory");
		}
	}

	@ResponseBody
	@RequestMapping(value = "/transactionHistoryData", method = RequestMethod.GET)
	public String transactionHistoryData(@RequestParam(value = "accountID") Integer accountID,
			@RequestParam(value = "start") Integer start, @RequestParam(value = "length") Integer length,
			@RequestParam(value = "username") String username, @RequestParam(value = "fromDate") String fromDate,
			@RequestParam(value = "toDate") String toDate,
			@RequestParam(value = "search[value]", required = false) String search) throws MalformedURLException {

		String jsonData;
		if (search == "") {
			jsonData = accountProcessor.getTransactionHistory(username, accountID, fromDate, toDate, start, length);
		} else {
			jsonData = accountProcessor.searchTransactionHistory(username, accountID, fromDate, toDate, start, length,
					search);
		}
		return jsonData;
	}

	@RequestMapping(value = "/reverseInquiry", method = RequestMethod.GET)
	public ModelAndView reverseInquiry(@Valid @RequestParam("traceNumber") String tracenumber,
			@RequestParam(value = "amount") String amount, @RequestParam(value = "toFromMember") String toFromMember,
			@RequestParam(value = "traceNumber") String traceNumber,
			@RequestParam("transactionDate") String transactionDate, @RequestParam(value = "remark") String remark,
			@RequestParam(value = "transactionNumber") String transactionNumber,
			@RequestParam(value = "description") String description,
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			HttpServletResponse response, Model model) {
		try {
			if (sessionID.equalsIgnoreCase("defaultCookieValue") || sessionID.equalsIgnoreCase(null)) {
				return new ModelAndView("redirect:/login");
			}
			IMap<String, Member> memberMap = instance.getMap("Member");
			Member member = memberMap.get(sessionID);
			if (member == null) {
				return new ModelAndView("redirect:/login");
			}
			AdminMenu adminMenu = menuProcessor.getMenuList(member.getGroupID());
			List<String> mainMenu = adminMenu.getMainMenu();
			Integer unreadMessage = messageProcessor.getCountUnreadMessages(member.getUsername());
			String unread = unreadMessage != 0 ? String.valueOf(unreadMessage) : "";
			List<String> messageSummary = messageProcessor.loadUnreadMessages(member.getUsername());

			if (member.getGroupName().equalsIgnoreCase("ADMIN")) {
				model.addAttribute(member);
				model.addAttribute("mainMenu", mainMenu);
				model.addAttribute("unreadMessage", unread);
				model.addAttribute("messageSummary", messageSummary);
				model.addAttribute("traceNumber", traceNumber);
				model.addAttribute("amount", amount);
				model.addAttribute("transactionDate", transactionDate);
				model.addAttribute("toFromMember", toFromMember);
				model.addAttribute("remark", remark);
				model.addAttribute("transactionNumber", transactionNumber);
				model.addAttribute("description", description);
				return new ModelAndView("reversePayment");
			} else {
				model.addAttribute(member);
				model.addAttribute("notification", "error");
				model.addAttribute("title", "Reversal");
				model.addAttribute("message", "UNAUTHORIZED_MEMBER_ACCESS : You don't have access to specified member");
				model.addAttribute("mainMenu", mainMenu);
				model.addAttribute("unreadMessage", unread);
				model.addAttribute("messageSummary", messageSummary);
				return new ModelAndView("member");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/reversePayment", method = RequestMethod.POST)
	public ModelAndView reversePayment(@Valid @ModelAttribute("reversepayment") ReversePayment reverse,
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID, Model model) {
		try {
			if (sessionID.equalsIgnoreCase("defaultCookieValue") || sessionID.equalsIgnoreCase(null)) {
				return new ModelAndView("redirect:/login");
			}
			IMap<String, Member> memberMap = instance.getMap("Member");
			Member member = memberMap.get(sessionID);
			if (member == null) {
				return new ModelAndView("redirect:/login");
			}
			AdminMenu adminMenu = menuProcessor.getMenuList(member.getGroupID());
			List<String> mainMenu = adminMenu.getMainMenu();
			Integer unreadMessage = messageProcessor.getCountUnreadMessages(member.getUsername());
			String unread = unreadMessage != 0 ? String.valueOf(unreadMessage) : "";
			List<String> messageSummary = messageProcessor.loadUnreadMessages(member.getUsername());
			ValidateCredentialResponse vcRes = accessProcessor.validateCredential(member.getUsername(),
					reverse.getCredential());

			if (vcRes.getStatus().getMessage().equalsIgnoreCase("VALID")) {
				ReversalResponse reversalRes = paymentProcessor.reversePayment(reverse.getTransactionNumber());
				if (reversalRes.getStatus().getMessage().equalsIgnoreCase("PROCESSED")) {
					model.addAttribute(member);
					model.addAttribute("notification", "success");
					model.addAttribute("title", "Reversal");
					model.addAttribute("message", "Reversal has been successfully");
					model.addAttribute("mainMenu", mainMenu);
					model.addAttribute("unreadMessage", unread);
					model.addAttribute("messageSummary", messageSummary);
					model.addAttribute("traceNumber", reverse.getTracenumber());
					model.addAttribute("amount", reverse.getAmount());
					model.addAttribute("transactionDate", reverse.getTransactionDate());
					model.addAttribute("toFromMember", reverse.getToFromMember());
					model.addAttribute("remark", reverse.getRemark());
					model.addAttribute("transactionNumber", reverse.getTransactionNumber());
					model.addAttribute("description", reverse.getDescription());
					return new ModelAndView("memberAdmin");
				} else {
					model.addAttribute(member);
					model.addAttribute("notification", "error");
					model.addAttribute("title", "Reversal");
					model.addAttribute("message",
							reversalRes.getStatus().getDescription() + ": " + reversalRes.getStatus().getMessage());
					model.addAttribute("mainMenu", mainMenu);
					model.addAttribute("unreadMessage", unread);
					model.addAttribute("messageSummary", messageSummary);
					return new ModelAndView("memberAdmin");
				}
			} else {
				model.addAttribute(member);
				model.addAttribute("notification", "error");
				model.addAttribute("title", "Reversal");
				model.addAttribute("message",
						vcRes.getStatus().getDescription() + ": " + vcRes.getStatus().getMessage());
				model.addAttribute("mainMenu", mainMenu);
				model.addAttribute("unreadMessage", unread);
				model.addAttribute("messageSummary", messageSummary);
				return new ModelAndView("memberAdmin");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/message", method = RequestMethod.GET)
	public ModelAndView message(@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@RequestParam(value = "MessageID", required = false) String messageID,
			@RequestParam(value = "Action", required = false) String action, Model model)
			throws NumberFormatException, MalformedURLException {
		if (sessionID.equalsIgnoreCase("defaultCookieValue") || sessionID.equalsIgnoreCase(null)) {
			return new ModelAndView("redirect:/login");
		}
		IMap<String, Member> memberMap = instance.getMap("Member");
		Member member = memberMap.get(sessionID);
		if (member == null) {
			return new ModelAndView("redirect:/login");
		}
		String messageContent = "<div class=\"col-sm-9 mail_view\"><div class=\"inbox-body\"><div class=\"mail_heading row\">"
				+ "<div class=\"col-md-8\"><div class=\"btn-group\"><button class=\"btn btn-sm btn-default\" type=\"button\" disabled data-placement=\"top\" data-toggle=\"tooltip\" data-original-title=\"Print\"><i class=\"fa fa-print\"></i></button><button class=\"btn btn-sm btn-default\" type=\"button\" disabled data-placement=\"top\" data-toggle=\"tooltip\" data-original-title=\"Trash\"><i class=\"fa fa-trash-o\"></i></button></div></div>"
				+ "<div class=\"col-md-4 text-right\"><p class=\"date\"></p></div><div class=\"col-md-12\"><h4></h4></div></div>"
				+ "<div class=\"sender-info\"><div class=\"row\"><div class=\"col-md-12\"></div></div></div><div class=\"view-mail\"></div><br/>"
				+ "<div class=\"btn-group\"><button class=\"btn btn-sm btn-default\" type=\"button\" disabled data-placement=\"top\" data-toggle=\"tooltip\" data-original-title=\"Print\"><i class=\"fa fa-print\"></i></button><button class=\"btn btn-sm btn-default\" type=\"button\" disabled data-placement=\"top\" data-toggle=\"tooltip\" data-original-title=\"Trash\"><i class=\"fa fa-trash-o\"></i></button></div></div></div>";

		if (messageID != null) {
			if (action != null && action.equalsIgnoreCase("delete")) {
				messageProcessor.deleteMessage(Integer.valueOf(messageID));
			} else {
				messageContent = messageProcessor.loadMessageByID(Integer.valueOf(messageID), sessionID);
			}
		}

		AdminMenu adminMenu = menuProcessor.getMenuList(member.getGroupID());
		List<String> mainMenu = adminMenu.getMainMenu();
		Integer unreadMessage = messageProcessor.getCountUnreadMessages(member.getUsername());
		String unread = unreadMessage != 0 ? String.valueOf(unreadMessage) : "";
		List<String> messageSummary = messageProcessor.loadUnreadMessages(member.getUsername());
		List<String> messageList = messageProcessor.loadMessages(member.getUsername(), sessionID);

		model.addAttribute(member);
		model.addAttribute("mainMenu", mainMenu);
		model.addAttribute("unreadMessage", unread);
		model.addAttribute("messageSummary", messageSummary);
		model.addAttribute("messageList", messageList);
		model.addAttribute("messageContent", messageContent);
		return new ModelAndView("message");
	}

	@RequestMapping(value = "/composeMessage", method = RequestMethod.GET)
	public ModelAndView composeMessage(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID, Model model)
			throws NumberFormatException, MalformedURLException {
		if (sessionID.equalsIgnoreCase("defaultCookieValue") || sessionID.equalsIgnoreCase(null)) {
			return new ModelAndView("redirect:/login");
		}
		IMap<String, Member> memberMap = instance.getMap("Member");
		Member member = memberMap.get(sessionID);
		if (member == null) {
			return new ModelAndView("redirect:/login");
		}
		AdminMenu adminMenu = menuProcessor.getMenuList(member.getGroupID());
		List<String> mainMenu = adminMenu.getMainMenu();
		Integer unreadMessage = messageProcessor.getCountUnreadMessages(member.getUsername());
		String unread = unreadMessage != 0 ? String.valueOf(unreadMessage) : "";
		List<String> messageSummary = messageProcessor.loadUnreadMessages(member.getUsername());
		List<String> messageList = messageProcessor.loadMessages(member.getUsername(), sessionID);

		model.addAttribute(member);
		model.addAttribute("mainMenu", mainMenu);
		model.addAttribute("unreadMessage", unread);
		model.addAttribute("messageSummary", messageSummary);
		model.addAttribute("messageList", messageList);
		return new ModelAndView("sendMessage");
	}

	@RequestMapping(value = "/sendMessage", method = RequestMethod.POST)
	public ModelAndView sendMessage(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@Valid @ModelAttribute("sendmessage") SendMessage sendMessage, Model model)
			throws NumberFormatException, MalformedURLException, org.bellatrix.services.ws.message.Exception_Exception {
		try {
			if (sessionID.equalsIgnoreCase("defaultCookieValue") || sessionID.equalsIgnoreCase(null)) {
				return new ModelAndView("redirect:/login");
			}
			IMap<String, Member> memberMap = instance.getMap("Member");
			Member member = memberMap.get(sessionID);
			if (member == null) {
				return new ModelAndView("redirect:/login");
			}

			AdminMenu adminMenu = menuProcessor.getMenuList(member.getGroupID());
			List<String> mainMenu = adminMenu.getMainMenu();
			Integer unreadMessage = messageProcessor.getCountUnreadMessages(member.getUsername());
			String unread = unreadMessage != 0 ? String.valueOf(unreadMessage) : "";
			List<String> messageSummary = messageProcessor.loadUnreadMessages(member.getUsername());
			List<String> messageList = messageProcessor.loadMessages(member.getUsername(), sessionID);

			LoadMembersResponse loadMembersRes = memberProcessor.loadMember(sendMessage.getToMember());
			if (loadMembersRes.getStatus().getMessage().equalsIgnoreCase("PROCESSED")) {
				messageProcessor.sendMessage(member.getUsername(), sendMessage.getToMember(), sendMessage.getSubject(),
						sendMessage.getBody());
				model.addAttribute(member);
				model.addAttribute("mainMenu", mainMenu);
				model.addAttribute("unreadMessage", unread);
				model.addAttribute("messageSummary", messageSummary);
				model.addAttribute("messageList", messageList);
				model.addAttribute("notification", "success");
				model.addAttribute("title", "Send Message");
				model.addAttribute("message", "Your message has been send");
				return new ModelAndView("message");
			} else {
				model.addAttribute(member);
				model.addAttribute("mainMenu", mainMenu);
				model.addAttribute("unreadMessage", unread);
				model.addAttribute("messageSummary", messageSummary);
				model.addAttribute("messageList", messageList);
				model.addAttribute("notification", "error");
				model.addAttribute("title", "Send Message");
				model.addAttribute("message", loadMembersRes.getStatus().getDescription());
				return new ModelAndView("message");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/virtualAccountEvent", method = RequestMethod.GET)
	public ModelAndView virtualAccountEvent(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID, Model model)
			throws MalformedURLException {
		if (sessionID.equalsIgnoreCase("defaultCookieValue") || sessionID.equalsIgnoreCase(null)) {
			return new ModelAndView("redirect:/login");
		}
		IMap<String, Member> memberMap = instance.getMap("Member");
		Member member = memberMap.get(sessionID);
		if (member == null) {
			return new ModelAndView("redirect:/login");
		}
		AdminMenu adminMenu = menuProcessor.getMenuList(member.getGroupID());
		List<String> mainMenu = adminMenu.getMainMenu();
		Integer unreadMessage = messageProcessor.getCountUnreadMessages(member.getUsername());
		String unread = unreadMessage != 0 ? String.valueOf(unreadMessage) : "";
		List<String> messageSummary = messageProcessor.loadUnreadMessages(member.getUsername());

		model.addAttribute(member);
		model.addAttribute("paymentPageURL", contextLoader.getVAEventURL());
		model.addAttribute("mainMenu", mainMenu);
		model.addAttribute("unreadMessage", unread);
		model.addAttribute("messageSummary", messageSummary);
		return new ModelAndView("virtualAccountEvent");
	}

	@ResponseBody
	@RequestMapping(value = "/virtualAccountEventData", method = RequestMethod.GET)
	public String virtualAccountEventData(@RequestParam(value = "username", required = false) String username,
			@RequestParam(value = "start") Integer start, @RequestParam(value = "length") Integer length)
			throws MalformedURLException {
		String jsonData = virtualAccountProcessor.loadVAEvent(username, Integer.valueOf(start),
				Integer.valueOf(length));
		return jsonData;
	}

	@RequestMapping(value = "/virtualAccountBilling", method = RequestMethod.GET)
	public ModelAndView virtualAccountBilling(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID, Model model)
			throws MalformedURLException {
		if (sessionID.equalsIgnoreCase("defaultCookieValue") || sessionID.equalsIgnoreCase(null)) {
			return new ModelAndView("redirect:/login");
		}
		IMap<String, Member> memberMap = instance.getMap("Member");
		Member member = memberMap.get(sessionID);
		if (member == null) {
			return new ModelAndView("redirect:/login");
		}
		AdminMenu adminMenu = menuProcessor.getMenuList(member.getGroupID());
		List<String> mainMenu = adminMenu.getMainMenu();
		Integer unreadMessage = messageProcessor.getCountUnreadMessages(member.getUsername());
		String unread = unreadMessage != 0 ? String.valueOf(unreadMessage) : "";
		List<String> messageSummary = messageProcessor.loadUnreadMessages(member.getUsername());

		model.addAttribute(member);
		model.addAttribute("mainMenu", mainMenu);
		model.addAttribute("unreadMessage", unread);
		model.addAttribute("messageSummary", messageSummary);
		return new ModelAndView("virtualAccountBilling");
	}

	@ResponseBody
	@RequestMapping(value = "/virtualAccountBillingData", method = RequestMethod.GET)
	public String virtualAccountBillingData(@RequestParam(value = "username", required = false) String username,
			@RequestParam(value = "start") Integer start, @RequestParam(value = "length") Integer length)
			throws MalformedURLException {
		String jsonData = virtualAccountProcessor.loadVABilling(username, Integer.valueOf(start),
				Integer.valueOf(length));
		return jsonData;
	}

	@RequestMapping(value = "/createVAEvent", method = RequestMethod.GET)
	public ModelAndView createVAEvent(VAEvent event,
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID, Model model)
			throws MalformedURLException {
		if (sessionID.equalsIgnoreCase("defaultCookieValue") || sessionID.equalsIgnoreCase(null)) {
			return new ModelAndView("redirect:/login");
		}
		IMap<String, Member> memberMap = instance.getMap("Member");
		Member member = memberMap.get(sessionID);
		if (member == null) {
			return new ModelAndView("redirect:/login");
		}
		AdminMenu adminMenu = menuProcessor.getMenuList(member.getGroupID());
		List<String> mainMenu = adminMenu.getMainMenu();
		Integer unreadMessage = messageProcessor.getCountUnreadMessages(member.getUsername());
		String unread = unreadMessage != 0 ? String.valueOf(unreadMessage) : "";
		List<String> messageSummary = messageProcessor.loadUnreadMessages(member.getUsername());

		model.addAttribute(member);
		model.addAttribute("vaevent", new VAEvent());
		model.addAttribute("mainMenu", mainMenu);
		model.addAttribute("unreadMessage", unread);
		model.addAttribute("messageSummary", messageSummary);
		return new ModelAndView("createVAEvent");
	}

	@RequestMapping(value = "/createVABilling", method = RequestMethod.GET)
	public ModelAndView createVABilling(VABilling billing,
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID, Model model)
			throws MalformedURLException {
		if (sessionID.equalsIgnoreCase("defaultCookieValue") || sessionID.equalsIgnoreCase(null)) {
			return new ModelAndView("redirect:/login");
		}
		IMap<String, Member> memberMap = instance.getMap("Member");
		Member member = memberMap.get(sessionID);
		if (member == null) {
			return new ModelAndView("redirect:/login");
		}
		AdminMenu adminMenu = menuProcessor.getMenuList(member.getGroupID());
		List<String> mainMenu = adminMenu.getMainMenu();
		Integer unreadMessage = messageProcessor.getCountUnreadMessages(member.getUsername());
		String unread = unreadMessage != 0 ? String.valueOf(unreadMessage) : "";
		List<String> messageSummary = messageProcessor.loadUnreadMessages(member.getUsername());

		model.addAttribute(member);
		model.addAttribute("vabilling", new VABilling());
		model.addAttribute("mainMenu", mainMenu);
		model.addAttribute("unreadMessage", unread);
		model.addAttribute("messageSummary", messageSummary);
		return new ModelAndView("createVABilling");
	}

	@RequestMapping(value = "/createVAEventForm", method = RequestMethod.POST)
	public ModelAndView createVAEventForm(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@Valid @ModelAttribute("vaevent") VAEvent event, BindingResult result, ModelMap model) {
		try {
			if (result.hasErrors()) {
				return new ModelAndView("page_500");
			}
			if (sessionID.equalsIgnoreCase("defaultCookieValue") || sessionID.equalsIgnoreCase(null)) {
				return new ModelAndView("redirect:/login");
			}
			IMap<String, Member> memberMap = instance.getMap("Member");
			Member member = memberMap.get(sessionID);
			if (member == null) {
				return new ModelAndView("redirect:/login");
			}
			CreateVAEventResponse createVAEventResponse = virtualAccountProcessor.createVAEvent(event);

			model.addAttribute(member);
			model.addAttribute("status", createVAEventResponse.getStatus().getMessage());
			return new ModelAndView("redirect:/createVAEventResult");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/createVAEventResult", method = RequestMethod.GET)
	public ModelAndView createVAEventResult(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@RequestParam(value = "status", required = false) String status, ModelMap model) {
		try {
			if (sessionID.equalsIgnoreCase("defaultCookieValue") || sessionID.equalsIgnoreCase(null)) {
				return new ModelAndView("redirect:/login");
			}
			IMap<String, Member> memberMap = instance.getMap("Member");
			Member member = memberMap.get(sessionID);
			if (member == null) {
				return new ModelAndView("redirect:/login");
			}
			AdminMenu adminMenu = menuProcessor.getMenuList(member.getGroupID());
			List<String> mainMenu = adminMenu.getMainMenu();
			Integer unreadMessage = messageProcessor.getCountUnreadMessages(member.getUsername());
			String unread = unreadMessage != 0 ? String.valueOf(unreadMessage) : "";
			List<String> messageSummary = messageProcessor.loadUnreadMessages(member.getUsername());

			if (status.equalsIgnoreCase("PROCESSED")) {
				model.addAttribute(member);
				model.addAttribute("paymentPageURL", contextLoader.getVAEventURL());
				model.addAttribute("notification", "success");
				model.addAttribute("title", "Create Payment Event");
				model.addAttribute("message", "Your event has been created successfully");
				model.addAttribute("mainMenu", mainMenu);
				model.addAttribute("unreadMessage", unread);
				model.addAttribute("messageSummary", messageSummary);
				return new ModelAndView("virtualAccountEvent");
			} else if (status.equalsIgnoreCase("BLOCKED")) {
				return new ModelAndView("redirect:/login");
			} else {
				model.addAttribute(member);
				model.addAttribute("paymentPageURL", contextLoader.getVAEventURL());
				model.addAttribute("notification", "error");
				model.addAttribute("title", "Create Payment Event");
				model.addAttribute("message", "Your event creation failed : " + status);
				model.addAttribute("mainMenu", mainMenu);
				model.addAttribute("unreadMessage", unread);
				model.addAttribute("messageSummary", messageSummary);
				return new ModelAndView("virtualAccountEvent");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/createVABillingForm", method = RequestMethod.POST)
	public ModelAndView createVABillingForm(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@Valid @ModelAttribute("vabilling") VABilling billing, BindingResult result, ModelMap model) {
		try {
			if (result.hasErrors()) {
				return new ModelAndView("page_500");
			}
			if (sessionID.equalsIgnoreCase("defaultCookieValue") || sessionID.equalsIgnoreCase(null)) {
				return new ModelAndView("redirect:/login");
			}
			IMap<String, Member> memberMap = instance.getMap("Member");
			Member member = memberMap.get(sessionID);
			if (member == null) {
				return new ModelAndView("redirect:/login");
			}
			VaRegisterResponse vaRegisterResponse = virtualAccountProcessor.createVABilling(billing);

			model.addAttribute(member);
			model.addAttribute("status", vaRegisterResponse.getStatus().getMessage());
			return new ModelAndView("redirect:/createVABillingResult", model);
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/createVABillingResult", method = RequestMethod.GET)
	public ModelAndView createVABillingResult(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@RequestParam(value = "status", required = false) String status, ModelMap model) {

		try {
			if (sessionID.equalsIgnoreCase("defaultCookieValue") || sessionID.equalsIgnoreCase(null)) {
				return new ModelAndView("redirect:/login");
			}
			IMap<String, Member> memberMap = instance.getMap("Member");
			Member member = memberMap.get(sessionID);
			if (member == null) {
				return new ModelAndView("redirect:/login");
			}
			AdminMenu adminMenu = menuProcessor.getMenuList(member.getGroupID());
			List<String> mainMenu = adminMenu.getMainMenu();
			Integer unreadMessage = messageProcessor.getCountUnreadMessages(member.getUsername());
			String unread = unreadMessage != 0 ? String.valueOf(unreadMessage) : "";
			List<String> messageSummary = messageProcessor.loadUnreadMessages(member.getUsername());

			if (status.equalsIgnoreCase("PROCESSED")) {
				model.addAttribute(member);
				model.addAttribute("notification", "success");
				model.addAttribute("title", "Create Payment Billing");
				model.addAttribute("message", "Your billing has been created successfully");
				model.addAttribute("mainMenu", mainMenu);
				model.addAttribute("unreadMessage", unread);
				model.addAttribute("messageSummary", messageSummary);
			} else {
				model.addAttribute(member);
				model.addAttribute("notification", "error");
				model.addAttribute("title", "Create Payment Billing");
				model.addAttribute("message", "Your billing creation failed : " + status);
				model.addAttribute("mainMenu", mainMenu);
				model.addAttribute("unreadMessage", unread);
				model.addAttribute("messageSummary", messageSummary);
			}
			return new ModelAndView("virtualAccountBilling");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/deleteVAEvent", method = RequestMethod.GET)
	public ModelAndView deleteVAEventForm(@RequestParam(value = "username", required = false) String username,
			@RequestParam(value = "ticketID", required = false) String ticketID,
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID, ModelMap model) {

		try {
			if (sessionID.equalsIgnoreCase("defaultCookieValue") || sessionID.equalsIgnoreCase(null)) {
				return new ModelAndView("redirect:/login");
			}
			IMap<String, Member> memberMap = instance.getMap("Member");
			Member member = memberMap.get(sessionID);
			if (member == null) {
				return new ModelAndView("redirect:/login");
			}
			AdminMenu adminMenu = menuProcessor.getMenuList(member.getGroupID());
			List<String> mainMenu = adminMenu.getMainMenu();
			Integer unreadMessage = messageProcessor.getCountUnreadMessages(member.getUsername());
			String unread = unreadMessage != 0 ? String.valueOf(unreadMessage) : "";
			List<String> messageSummary = messageProcessor.loadUnreadMessages(member.getUsername());
			virtualAccountProcessor.deleteVAEvent(username, ticketID);

			model.addAttribute(member);
			model.addAttribute("paymentPageURL", contextLoader.getVAEventURL());
			model.addAttribute("notification", "success");
			model.addAttribute("title", "Delete Payment Event");
			model.addAttribute("message", "Your event has been deleted successfully");
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			return new ModelAndView("virtualAccountEvent");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("virtualAccountEvent");
		}
	}

	@RequestMapping(value = "/deleteVABilling", method = RequestMethod.GET)
	public ModelAndView deleteVABilling(@RequestParam(value = "username", required = false) String username,
			@RequestParam(value = "paymentCode", required = false) String paymentCode,
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID, ModelMap model) {

		try {
			if (sessionID.equalsIgnoreCase("defaultCookieValue") || sessionID.equalsIgnoreCase(null)) {
				return new ModelAndView("redirect:/login");
			}
			IMap<String, Member> memberMap = instance.getMap("Member");
			Member member = memberMap.get(sessionID);
			if (member == null) {
				return new ModelAndView("redirect:/login");
			}
			virtualAccountProcessor.deleteVABilling(username, paymentCode);

			model.addAttribute(member);
			return new ModelAndView("redirect:/deleteVABillingResult", model);
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/deleteVABillingResult", method = RequestMethod.GET)
	public ModelAndView deleteVABillingResult(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID, ModelMap model) {
		try {
			if (sessionID.equalsIgnoreCase("defaultCookieValue") || sessionID.equalsIgnoreCase(null)) {
				return new ModelAndView("redirect:/login");
			}
			IMap<String, Member> memberMap = instance.getMap("Member");
			Member member = memberMap.get(sessionID);
			if (member == null) {
				return new ModelAndView("redirect:/login");
			}
			AdminMenu adminMenu = menuProcessor.getMenuList(member.getGroupID());
			List<String> mainMenu = adminMenu.getMainMenu();
			Integer unreadMessage = messageProcessor.getCountUnreadMessages(member.getUsername());
			String unread = unreadMessage != 0 ? String.valueOf(unreadMessage) : "";
			List<String> messageSummary = messageProcessor.loadUnreadMessages(member.getUsername());

			model.addAttribute(member);
			model.addAttribute("notification", "success");
			model.addAttribute("title", "Delete Payment Billing");
			model.addAttribute("message", "Your billing has been deleted successfully");
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			return new ModelAndView("virtualAccountBilling");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/setting", method = RequestMethod.GET)
	public ModelAndView setting(@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			Model model) throws MalformedURLException {
		try {
			if (sessionID.equalsIgnoreCase("defaultCookieValue") || sessionID.equalsIgnoreCase(null)) {
				return new ModelAndView("redirect:/login");
			}
			IMap<String, Member> memberMap = instance.getMap("Member");
			Member member = memberMap.get(sessionID);
			if (member == null) {
				return new ModelAndView("redirect:/login");
			}
			AdminMenu adminMenu = menuProcessor.getMenuList(member.getGroupID());
			List<String> mainMenu = adminMenu.getMainMenu();
			Integer unreadMessage = messageProcessor.getCountUnreadMessages(member.getUsername());
			String unread = unreadMessage != 0 ? String.valueOf(unreadMessage) : "";
			List<String> messageSummary = messageProcessor.loadUnreadMessages(member.getUsername());

			model.addAttribute(member);
			model.addAttribute("name", member.getName());
			model.addAttribute("email", member.getEmail());
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);

			return new ModelAndView("setting");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@ResponseBody
	@RequestMapping(value = "/accountTransferData", method = RequestMethod.GET)
	public String accountTransferData(@RequestParam(value = "start") Integer start,
			@RequestParam(value = "length") Integer length, @RequestParam(value = "username") String username) {
		System.out.println("Page: " + start);
		String accTransfer = interbankProcessor.getAccountTransferList(username, start, length);
		return accTransfer;
	}

	@RequestMapping(value = "/editProfile", method = RequestMethod.POST)
	public ModelAndView editProfile(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@Valid @ModelAttribute("editmember") UpgradeMember editmember, ModelMap model) {
		try {
			if (sessionID.equalsIgnoreCase("defaultCookieValue") || sessionID.equalsIgnoreCase(null)) {
				return new ModelAndView("redirect:/login");
			}
			IMap<String, Member> memberMap = instance.getMap("Member");
			Member member = memberMap.get(sessionID);
			if (member == null) {
				return new ModelAndView("redirect:/login");
			}
			AdminMenu adminMenu = menuProcessor.getMenuList(member.getGroupID());
			List<String> mainMenu = adminMenu.getMainMenu();
			Integer unreadMessage = messageProcessor.getCountUnreadMessages(member.getUsername());
			String unread = unreadMessage != 0 ? String.valueOf(unreadMessage) : "";
			List<String> messageSummary = messageProcessor.loadUnreadMessages(member.getUsername());
			memberProcessor.editProfile(editmember);

			model.addAttribute(member);
			model.addAttribute("email", editmember.getEmail());
			model.addAttribute("name", editmember.getName());
			model.addAttribute("notification", "success");
			model.addAttribute("title", "Edit Profile");
			model.addAttribute("message", "Edit profile has been successfully");
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			return new ModelAndView("setting");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/createBankAccount", method = RequestMethod.GET)
	public ModelAndView createBankAccount(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID, Model model)
			throws org.bellatrix.services.ws.interbanks.Exception_Exception {
		try {
			if (sessionID.equalsIgnoreCase("defaultCookieValue") || sessionID.equalsIgnoreCase(null)) {
				return new ModelAndView("redirect:/login");
			}
			IMap<String, Member> memberMap = instance.getMap("Member");
			Member member = memberMap.get(sessionID);
			if (member == null) {
				return new ModelAndView("redirect:/login");
			}
			AdminMenu adminMenu = menuProcessor.getMenuList(member.getGroupID());
			List<String> mainMenu = adminMenu.getMainMenu();
			Integer unreadMessage = messageProcessor.getCountUnreadMessages(member.getUsername());
			String unread = unreadMessage != 0 ? String.valueOf(unreadMessage) : "";
			List<String> messageSummary = messageProcessor.loadUnreadMessages(member.getUsername());
			List<String> listBank = interbankProcessor.loadBankTransfer(member.getUsername());

			model.addAttribute(member);
			// model.addAttribute("member", new Member());
			model.addAttribute("listBank", listBank);
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			return new ModelAndView("createBankAccount");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/createBankAccountForm", method = RequestMethod.POST)
	public ModelAndView createBankAccountForm(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@Valid @ModelAttribute("bankaccount") BankAccount bankaccount, BindingResult result, ModelMap model) {
		try {
			if (result.hasErrors()) {
				return new ModelAndView("page_500");
			}
			if (sessionID.equalsIgnoreCase("defaultCookieValue") || sessionID.equalsIgnoreCase(null)) {
				return new ModelAndView("redirect:/login");
			}
			IMap<String, Member> memberMap = instance.getMap("Member");
			Member member = memberMap.get(sessionID);
			if (member == null) {
				return new ModelAndView("redirect:/login");
			}
			interbankProcessor.createBankAccount(bankaccount, member.getUsername());

			model.addAttribute(member);
			model.addAttribute(bankaccount);
			return new ModelAndView("redirect:/createBankAccountResult", model);
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/createBankAccountResult", method = RequestMethod.GET)
	public ModelAndView createBankAccountResult(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID, ModelMap model) {
		try {
			if (sessionID.equalsIgnoreCase("defaultCookieValue") || sessionID.equalsIgnoreCase(null)) {
				return new ModelAndView("redirect:/login");
			}
			IMap<String, Member> memberMap = instance.getMap("Member");
			Member member = memberMap.get(sessionID);
			if (member == null) {
				return new ModelAndView("redirect:/login");
			}
			AdminMenu adminMenu = menuProcessor.getMenuList(member.getGroupID());
			List<String> mainMenu = adminMenu.getMainMenu();
			Integer unreadMessage = messageProcessor.getCountUnreadMessages(member.getUsername());
			String unread = unreadMessage != 0 ? String.valueOf(unreadMessage) : "";
			List<String> messageSummary = messageProcessor.loadUnreadMessages(member.getUsername());

			model.addAttribute(member);
			model.addAttribute("notification", "success");
			model.addAttribute("title", "Create Bank Account");
			model.addAttribute("message", "Bank Account has been created successfully");
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			return new ModelAndView("setting");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/detailBankAccount", method = RequestMethod.GET)
	public ModelAndView detailBankAccount(@Valid @ModelAttribute("bankaccount") BankAccount bankaccount,
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@RequestParam(value = "accountNumber") String accountNo, Model model)
			throws org.bellatrix.services.ws.interbanks.Exception_Exception {
		try {
			if (sessionID.equalsIgnoreCase("defaultCookieValue") || sessionID.equalsIgnoreCase(null)) {
				return new ModelAndView("redirect:/login");
			}
			IMap<String, Member> memberMap = instance.getMap("Member");
			Member member = memberMap.get(sessionID);
			if (member == null) {
				return new ModelAndView("redirect:/login");
			}
			AdminMenu adminMenu = menuProcessor.getMenuList(member.getGroupID());
			List<String> mainMenu = adminMenu.getMainMenu();
			Integer unreadMessage = messageProcessor.getCountUnreadMessages(member.getUsername());
			String unread = unreadMessage != 0 ? String.valueOf(unreadMessage) : "";
			List<String> messageSummary = messageProcessor.loadUnreadMessages(member.getUsername());
			Map<String, Object> bankAccountDetails = interbankProcessor.getBankAccountDetail(accountNo,
					member.getUsername());

			model.addAttribute(member);
			model.addAttribute(bankaccount);
			model.addAllAttributes(bankAccountDetails);
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			return new ModelAndView("detailBankAccount");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/transferBankAccount", method = RequestMethod.GET)
	public ModelAndView transferToBankAccount(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID, Model model) {
		try {
			if (sessionID.equalsIgnoreCase("defaultCookieValue") || sessionID.equalsIgnoreCase(null)) {
				return new ModelAndView("redirect:/login");
			}
			IMap<String, Member> memberMap = instance.getMap("Member");
			Member member = memberMap.get(sessionID);
			if (member == null) {
				return new ModelAndView("redirect:/login");
			}
			AdminMenu adminMenu = menuProcessor.getMenuList(member.getGroupID());
			List<String> mainMenu = adminMenu.getMainMenu();
			Integer unreadMessage = messageProcessor.getCountUnreadMessages(member.getUsername());
			String unread = unreadMessage != 0 ? String.valueOf(unreadMessage) : "";
			List<String> messageSummary = messageProcessor.loadUnreadMessages(member.getUsername());
			List<String> listBankTransfer = interbankProcessor.loadAccountTransfer(member.getUsername());

			model.addAttribute(member);
			model.addAttribute("listBankTransfer", listBankTransfer);
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			return new ModelAndView("transferBankAccount");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/transferBankAccountInquiry", method = RequestMethod.POST)
	public ModelAndView transferBankAccountInquiry(@Valid @ModelAttribute("transferbank") TransferBank transfer,
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID, ModelMap model) {
		try {
			if (sessionID.equalsIgnoreCase("defaultCookieValue") || sessionID.equalsIgnoreCase(null)) {
				return new ModelAndView("redirect:/login");
			}
			IMap<String, Member> memberMap = instance.getMap("Member");
			Member member = memberMap.get(sessionID);
			if (member == null) {
				return new ModelAndView("redirect:/login");
			}
			AdminMenu adminMenu = menuProcessor.getMenuList(member.getGroupID());
			List<String> mainMenu = adminMenu.getMainMenu();
			Integer unreadMessage = messageProcessor.getCountUnreadMessages(member.getUsername());
			String unread = unreadMessage != 0 ? String.valueOf(unreadMessage) : "";
			List<String> messageSummary = messageProcessor.loadUnreadMessages(member.getUsername());
			BankAccountTransferResponse bankAccTrfRes = interbankProcessor.transferBankAccountInquiry(transfer);

			if (bankAccTrfRes.getStatus().getMessage().equalsIgnoreCase("PROCESSED")) {
				String toAccount[] = transfer.getToAccount().split("-");
				model.addAttribute(member);
				model.addAttribute("mainMenu", mainMenu);
				model.addAttribute("unreadMessage", unread);
				model.addAttribute("message", bankAccTrfRes.getStatus().getDescription());
				model.addAttribute("messageSummary", messageSummary);
				model.addAttribute("description", transfer.getDescription());
				model.addAttribute("amount", transfer.getAmount());
				model.addAttribute("toAccountNo", toAccount[0]);
				model.addAttribute("toAccountName", toAccount[1]);
				return new ModelAndView("transferBankAccountInquiry");
			} else {
				model.addAttribute(member);
				model.addAttribute("notification", "error");
				model.addAttribute("title", "Transfer To Bank Account");
				model.addAttribute("message", bankAccTrfRes.getStatus().getDescription());
				model.addAttribute("mainMenu", mainMenu);
				model.addAttribute("unreadMessage", unread);
				model.addAttribute("messageSummary", messageSummary);
				return new ModelAndView("transferBankAccount");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/transferBankAccountPayment", method = RequestMethod.POST)
	public ModelAndView transferBankAccountPayment(@Valid @ModelAttribute("transferbankpayment") TransferBank transfer,
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID, Model model)
			throws org.bellatrix.services.ws.interbanks.Exception_Exception {
		try {
			if (sessionID.equalsIgnoreCase("defaultCookieValue") || sessionID.equalsIgnoreCase(null)) {
				return new ModelAndView("redirect:/login");
			}
			IMap<String, Member> memberMap = instance.getMap("Member");
			Member member = memberMap.get(sessionID);
			if (member == null) {
				return new ModelAndView("redirect:/login");
			}
			AdminMenu adminMenu = menuProcessor.getMenuList(member.getGroupID());
			List<String> mainMenu = adminMenu.getMainMenu();
			Integer unreadMessage = messageProcessor.getCountUnreadMessages(member.getUsername());
			String unread = unreadMessage != 0 ? String.valueOf(unreadMessage) : "";
			List<String> messageSummary = messageProcessor.loadUnreadMessages(member.getUsername());
			BankAccountTransferResponse accTransferResponse = interbankProcessor.transferBankAccountPayment(transfer);

			if (accTransferResponse.getStatus().getMessage().equalsIgnoreCase("REQUEST_RECEIVED")) {
				model.addAttribute(member);
				model.addAttribute("notification", "success");
				model.addAttribute("title", "Transfer To Bank Account");
				model.addAttribute("message", "Transfer to bank account has been successfully");
				model.addAttribute("mainMenu", mainMenu);
				model.addAttribute("unreadMessage", unread);
				model.addAttribute("messageSummary", messageSummary);
				model.addAttribute("description", transfer.getDescription());
				model.addAttribute("amount", transfer.getAmount());
				model.addAttribute("toAccountName", transfer.getToAccountName());
				model.addAttribute("toAccountNo", transfer.getToAccountNo());
				model.addAttribute("reffNumber", accTransferResponse.getReferenceNumber());
				model.addAttribute("status", accTransferResponse.getStatus().getMessage());
				model.addAttribute("trxNumber", accTransferResponse.getTransactionNumber());
				return new ModelAndView("transferBankAccountPayment");
			} else {
				model.addAttribute(member);
				model.addAttribute("notification", "error");
				model.addAttribute("title", "Transfer To Bank Account");
				model.addAttribute("message", accTransferResponse.getStatus().getDescription() + ":"
						+ accTransferResponse.getStatus().getMessage());
				model.addAttribute("mainMenu", mainMenu);
				model.addAttribute("unreadMessage", unread);
				model.addAttribute("messageSummary", messageSummary);
				return new ModelAndView("transferBankAccount");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/transferMember", method = RequestMethod.GET)
	public ModelAndView transferToMember(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID, Model model) {
		try {
			if (sessionID.equalsIgnoreCase("defaultCookieValue") || sessionID.equalsIgnoreCase(null)) {
				return new ModelAndView("redirect:/login");
			}
			IMap<String, Member> memberMap = instance.getMap("Member");
			Member member = memberMap.get(sessionID);
			if (member == null) {
				return new ModelAndView("redirect:/login");
			}
			AdminMenu adminMenu = menuProcessor.getMenuList(member.getGroupID());
			List<String> mainMenu = adminMenu.getMainMenu();
			Integer unreadMessage = messageProcessor.getCountUnreadMessages(member.getUsername());
			String unread = unreadMessage != 0 ? String.valueOf(unreadMessage) : "";
			List<String> messageSummary = messageProcessor.loadUnreadMessages(member.getUsername());
			List<String> listMember;
			if (member.getGroupName().equalsIgnoreCase("ADMIN")) {
				listMember = memberProcessor.getListAllMember(member.getUsername());
			} else {
				listMember = memberProcessor.getListMember(member.getUsername());
			}

			model.addAttribute(member);
			model.addAttribute("listMember", listMember);
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			return new ModelAndView("transferMember");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/transferMemberInquiry", method = RequestMethod.POST)
	public ModelAndView transferMemberInquiry(@Valid @ModelAttribute("transfermember") TransferMember transfer,
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID, ModelMap model) {
		try {
			if (sessionID.equalsIgnoreCase("defaultCookieValue") || sessionID.equalsIgnoreCase(null)) {
				return new ModelAndView("redirect:/login");
			}
			IMap<String, Member> memberMap = instance.getMap("Member");
			Member member = memberMap.get(sessionID);
			if (member == null) {
				return new ModelAndView("redirect:/login");
			}
			AdminMenu adminMenu = menuProcessor.getMenuList(member.getGroupID());
			List<String> mainMenu = adminMenu.getMainMenu();
			Integer unreadMessage = messageProcessor.getCountUnreadMessages(member.getUsername());
			String unread = unreadMessage != 0 ? String.valueOf(unreadMessage) : "";
			List<String> messageSummary = messageProcessor.loadUnreadMessages(member.getUsername());

			transfer.setTransferTypeID(contextLoader.getP2PTrfTypeID());
			InquiryResponse inquiryResponse = paymentProcessor.transferInquiry(transfer, member.getUsername());

			if (inquiryResponse.getStatus().getMessage().equalsIgnoreCase("PROCESSED")) {
				model.addAttribute(member);
				model.addAttribute("title", "Transfer To Member");
				model.addAttribute("mainMenu", mainMenu);
				model.addAttribute("unreadMessage", unread);
				model.addAttribute("messageSummary", messageSummary);
				model.addAttribute("description", transfer.getDescription());
				model.addAttribute("fee", inquiryResponse.getTotalFees());
				model.addAttribute("amount", inquiryResponse.getTransactionAmount());
				model.addAttribute("totalAmount", inquiryResponse.getFinalAmount());
				model.addAttribute("toMember", inquiryResponse.getToMember().getUsername());
				model.addAttribute("status", inquiryResponse.getStatus().getMessage());
				return new ModelAndView("transferMemberInquiry");
			} else {
				model.addAttribute(member);
				model.addAttribute("notification", "error");
				model.addAttribute("title", "Transfer To Member");
				model.addAttribute("message",
						"Transfer To Member Inquiry failed : " + inquiryResponse.getStatus().getMessage());
				model.addAttribute("mainMenu", mainMenu);
				model.addAttribute("unreadMessage", unread);
				model.addAttribute("messageSummary", messageSummary);
				return new ModelAndView("transferMember");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/transferMemberPayment", method = RequestMethod.POST)
	public ModelAndView transferMemberPayment(@Valid @ModelAttribute("transfermember") TransferMember transfer,
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID, Model model) {
		try {
			if (sessionID.equalsIgnoreCase("defaultCookieValue") || sessionID.equalsIgnoreCase(null)) {
				return new ModelAndView("redirect:/login");
			}
			IMap<String, Member> memberMap = instance.getMap("Member");
			Member member = memberMap.get(sessionID);
			if (member == null) {
				return new ModelAndView("redirect:/login");
			}
			AdminMenu adminMenu = menuProcessor.getMenuList(member.getGroupID());
			List<String> mainMenu = adminMenu.getMainMenu();
			Integer unreadMessage = messageProcessor.getCountUnreadMessages(member.getUsername());
			String unread = unreadMessage != 0 ? String.valueOf(unreadMessage) : "";
			List<String> messageSummary = messageProcessor.loadUnreadMessages(member.getUsername());
			PaymentResponse paymentResponse = paymentProcessor.transferPayment(member.getUsername(),
					transfer.getAmount(), transfer.getToMember(), transfer.getDescription(), transfer.getCredential(),
					contextLoader.getP2PTrfTypeID());

			if (paymentResponse.getStatus().getMessage().equalsIgnoreCase("PROCESSED")) {
				model.addAttribute(member);
				model.addAttribute("notification", "success");
				model.addAttribute("title", "Transfer To Member");
				model.addAttribute("message", "Transfer to member has been successfully");
				model.addAttribute("mainMenu", mainMenu);
				model.addAttribute("unreadMessage", unread);
				model.addAttribute("messageSummary", messageSummary);
				model.addAttribute("description", paymentResponse.getDescription());
				model.addAttribute("amount", paymentResponse.getAmount());
				model.addAttribute("toMember", transfer.getToMember());
				model.addAttribute("fee", paymentResponse.getTotalFees());
				model.addAttribute("totalAmount", paymentResponse.getFinalAmount());
				model.addAttribute("traceNumber", paymentResponse.getTraceNumber());
				model.addAttribute("transactionNumber", paymentResponse.getTransactionNumber());
				model.addAttribute("status", paymentResponse.getStatus().getMessage());
				return new ModelAndView("transferMemberPayment");
			} else {
				model.addAttribute(member);
				model.addAttribute("notification", "error");
				model.addAttribute("title", "Transfer To Member");
				model.addAttribute("message",
						paymentResponse.getStatus().getDescription() + ": " + paymentResponse.getStatus().getMessage());
				model.addAttribute("mainMenu", mainMenu);
				model.addAttribute("unreadMessage", unread);
				model.addAttribute("messageSummary", messageSummary);
				return new ModelAndView("transferMember");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/member", method = RequestMethod.GET)
	public ModelAndView member(@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			Model model) {
		try {
			if (sessionID.equalsIgnoreCase("defaultCookieValue") || sessionID.equalsIgnoreCase(null)) {
				return new ModelAndView("redirect:/login");
			}
			IMap<String, Member> memberMap = instance.getMap("Member");
			Member member = memberMap.get(sessionID);
			if (member == null) {
				return new ModelAndView("redirect:/login");
			}
			AdminMenu adminMenu = menuProcessor.getMenuList(member.getGroupID());
			List<String> mainMenu = adminMenu.getMainMenu();
			Integer unreadMessage = messageProcessor.getCountUnreadMessages(member.getUsername());
			String unread = unreadMessage != 0 ? String.valueOf(unreadMessage) : "";
			List<String> messageSummary = messageProcessor.loadUnreadMessages(member.getUsername());

			model.addAttribute(member);
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			if (member.getGroupName().equalsIgnoreCase("ADMIN")) {
				return new ModelAndView("memberAdmin");
			} else {
				return new ModelAndView("member");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@ResponseBody
	@RequestMapping(value = "/memberData", method = RequestMethod.GET)
	public String memberData(@RequestParam(value = "username", required = false) String username,
			@RequestParam(value = "start") Integer start, @RequestParam(value = "length") Integer length)
			throws MalformedURLException {
		String jsonData = memberProcessor.loadListMember(username, Integer.valueOf(start), Integer.valueOf(length));
		return jsonData;
	}

	@RequestMapping(value = "/createMember", method = RequestMethod.GET)
	public ModelAndView createMember(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID, Model model)
			throws MalformedURLException {
		if (sessionID.equalsIgnoreCase("defaultCookieValue") || sessionID.equalsIgnoreCase(null)) {
			return new ModelAndView("redirect:/login");
		}
		IMap<String, Member> memberMap = instance.getMap("Member");
		Member member = memberMap.get(sessionID);
		if (member == null) {
			return new ModelAndView("redirect:/login");
		}
		AdminMenu adminMenu = menuProcessor.getMenuList(member.getGroupID());
		List<String> mainMenu = adminMenu.getMainMenu();
		Integer unreadMessage = messageProcessor.getCountUnreadMessages(member.getUsername());
		String unread = unreadMessage != 0 ? String.valueOf(unreadMessage) : "";
		List<String> messageSummary = messageProcessor.loadUnreadMessages(member.getUsername());
		List<String> listGroup = groupProcessor.getListGroup();

		model.addAttribute(member);
		model.addAttribute("member", new Member());
		model.addAttribute("listGroup", listGroup);
		model.addAttribute("mainMenu", mainMenu);
		model.addAttribute("unreadMessage", unread);
		model.addAttribute("messageSummary", messageSummary);
		return new ModelAndView("createMember");
	}

	@RequestMapping(value = "/createMemberForm", method = RequestMethod.POST)
	public ModelAndView createMemberForm(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@Valid @ModelAttribute("newmember") Member members, BindingResult result, ModelMap model) {
		try {
			if (result.hasErrors()) {
				return new ModelAndView("page_500");
			}
			if (sessionID.equalsIgnoreCase("defaultCookieValue") || sessionID.equalsIgnoreCase(null)) {
				return new ModelAndView("redirect:/login");
			}
			IMap<String, Member> memberMap = instance.getMap("Member");
			Member member = memberMap.get(sessionID);
			if (member == null) {
				return new ModelAndView("redirect:/login");
			}
			if (members.getCredential().equalsIgnoreCase(members.getConfirmCredential())) {
				memberProcessor.createMember(members, member.getUsername());
				accessProcessor.createCredential(members, contextLoader.getChangeCredentialTypeID());
				accessProcessor.createCredential(members, 4);

				model.addAttribute(member);
				model.addAttribute(members);
				return new ModelAndView("redirect:/createMemberResult", model);
			} else {
				return new ModelAndView("page_500");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/createMemberResult", method = RequestMethod.GET)
	public ModelAndView createMemberResult(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID, ModelMap model) {
		try {
			if (sessionID.equalsIgnoreCase("defaultCookieValue") || sessionID.equalsIgnoreCase(null)) {
				return new ModelAndView("redirect:/login");
			}
			IMap<String, Member> memberMap = instance.getMap("Member");
			Member member = memberMap.get(sessionID);
			if (member == null) {
				return new ModelAndView("redirect:/login");
			}
			AdminMenu adminMenu = menuProcessor.getMenuList(member.getGroupID());
			List<String> mainMenu = adminMenu.getMainMenu();
			Integer unreadMessage = messageProcessor.getCountUnreadMessages(member.getUsername());
			String unread = unreadMessage != 0 ? String.valueOf(unreadMessage) : "";
			List<String> messageSummary = messageProcessor.loadUnreadMessages(member.getUsername());

			model.addAttribute(member);
			model.addAttribute("notification", "success");
			model.addAttribute("title", "Create Member");
			model.addAttribute("message", "Member has been created successfully");
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			return new ModelAndView("memberAdmin");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/detailMember", method = RequestMethod.GET)
	public ModelAndView detailMember(@Valid @ModelAttribute("member") Member members,
			@RequestParam(value = "username") String username,
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID, Model model) {
		try {
			if (sessionID.equalsIgnoreCase("defaultCookieValue") || sessionID.equalsIgnoreCase(null)) {
				return new ModelAndView("redirect:/login");
			}
			IMap<String, Member> memberMap = instance.getMap("Member");
			Member member = memberMap.get(sessionID);
			if (member == null) {
				return new ModelAndView("redirect:/login");
			}
			AdminMenu adminMenu = menuProcessor.getMenuList(member.getGroupID());
			List<String> mainMenu = adminMenu.getMainMenu();
			Integer unreadMessage = messageProcessor.getCountUnreadMessages(member.getUsername());
			String unread = unreadMessage != 0 ? String.valueOf(unreadMessage) : "";
			List<String> messageSummary = messageProcessor.loadUnreadMessages(member.getUsername());
			Map<String, Object> memberDetails = memberProcessor.searchMemberDetails(username, member.getID(),
					member.getGroupName());
			CredentialStatusResponse csRes = accessProcessor.credentialStatus(username);

			model.addAttribute(member);
			model.addAllAttributes(memberDetails);
			model.addAttribute("isBlocked", csRes.getAccessStatus().isBlocked());
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);

			if (memberDetails.get("status").equals("UNAUTHORIZED_MEMBER_ACCESS")
					&& member.getGroupName().equalsIgnoreCase("PARTNER")) {
				model.addAttribute("notification", "error");
				model.addAttribute("title", "UNAUTHORIZED_MEMBER_ACCESS");
				model.addAttribute("message", "You don't have access to specified member");
				return new ModelAndView("member");
			} else {
				if (member.getGroupName().equalsIgnoreCase("ADMIN")) {
					return new ModelAndView("detailMemberAdmin");
				} else {
					return new ModelAndView("detailMember");
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@ResponseBody
	@RequestMapping(value = "/accountData", method = RequestMethod.GET)
	public String accountData(@RequestParam(value = "username", required = false) String username,
			@RequestParam(value = "groupID", required = false) Integer groupID,
			@RequestParam(value = "start") Integer start, @RequestParam(value = "length") Integer length)
			throws MalformedURLException {
		String jsonData = accountProcessor.loadAccountMember(groupID, Integer.valueOf(start), Integer.valueOf(length));
		return jsonData;
	}

	@RequestMapping(value = "/editMember", method = RequestMethod.GET)
	public ModelAndView editMember(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@RequestParam(value = "username") String username, Model model) throws Exception_Exception, IOException {
		try {
			if (sessionID.equalsIgnoreCase("defaultCookieValue") || sessionID.equalsIgnoreCase(null)) {
				return new ModelAndView("redirect:/login");
			}
			IMap<String, Member> memberMap = instance.getMap("Member");
			Member member = memberMap.get(sessionID);
			if (member == null) {
				return new ModelAndView("redirect:/login");
			}
			AdminMenu adminMenu = menuProcessor.getMenuList(member.getGroupID());
			List<String> mainMenu = adminMenu.getMainMenu();
			Integer unreadMessage = messageProcessor.getCountUnreadMessages(member.getUsername());
			String unread = unreadMessage != 0 ? String.valueOf(unreadMessage) : "";
			List<String> messageSummary = messageProcessor.loadUnreadMessages(member.getUsername());
			Map<String, Object> memberDetails = memberProcessor.getMemberDetails(username);

			model.addAttribute(member);
			model.addAllAttributes(memberDetails);
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			return new ModelAndView("editMember");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/member", method = RequestMethod.POST, params = "confirm=edit")
	public ModelAndView editProfileMember(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@Valid @ModelAttribute("editmember") UpgradeMember editmember, ModelMap model) {
		try {
			if (sessionID.equalsIgnoreCase("defaultCookieValue") || sessionID.equalsIgnoreCase(null)) {
				return new ModelAndView("redirect:/login");
			}
			IMap<String, Member> memberMap = instance.getMap("Member");
			Member member = memberMap.get(sessionID);
			if (member == null) {
				return new ModelAndView("redirect:/login");
			}
			AdminMenu adminMenu = menuProcessor.getMenuList(member.getGroupID());
			List<String> mainMenu = adminMenu.getMainMenu();
			Integer unreadMessage = messageProcessor.getCountUnreadMessages(member.getUsername());
			String unread = unreadMessage != 0 ? String.valueOf(unreadMessage) : "";
			List<String> messageSummary = messageProcessor.loadUnreadMessages(member.getUsername());
			memberProcessor.editMember(editmember);

			model.addAttribute(member);
			model.addAttribute("notification", "success");
			model.addAttribute("title", "Edit Member");
			model.addAttribute("message", "Edit profile has been successfully");
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);

			return new ModelAndView("memberAdmin");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/changeCredential", method = RequestMethod.GET)
	public ModelAndView changeCredential(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID, Model model) {
		try {
			if (sessionID.equalsIgnoreCase("defaultCookieValue") || sessionID.equalsIgnoreCase(null)) {
				return new ModelAndView("redirect:/login");
			}
			IMap<String, Member> memberMap = instance.getMap("Member");
			Member member = memberMap.get(sessionID);
			if (member == null) {
				return new ModelAndView("redirect:/login");
			}
			AdminMenu adminMenu = menuProcessor.getMenuList(member.getGroupID());
			List<String> mainMenu = adminMenu.getMainMenu();
			Integer unreadMessage = messageProcessor.getCountUnreadMessages(member.getUsername());
			String unread = unreadMessage != 0 ? String.valueOf(unreadMessage) : "";
			List<String> messageSummary = messageProcessor.loadUnreadMessages(member.getUsername());

			model.addAttribute(member);
			model.addAttribute("username", member.getUsername());
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			return new ModelAndView("changeCredential");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/changeCredentialResult", method = RequestMethod.POST)
	public ModelAndView changeCredentialResult(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@Valid @ModelAttribute("changecredential") ChangeCredential changecredential, ModelMap model) {
		try {
			if (sessionID.equalsIgnoreCase("defaultCookieValue") || sessionID.equalsIgnoreCase(null)) {
				return new ModelAndView("redirect:/login");
			}
			IMap<String, Member> memberMap = instance.getMap("Member");
			Member member = memberMap.get(sessionID);
			if (member == null) {
				return new ModelAndView("redirect:/login");
			}
			AdminMenu adminMenu = menuProcessor.getMenuList(member.getGroupID());
			List<String> mainMenu = adminMenu.getMainMenu();
			String welcomeMenu = adminMenu.getWelcomeMenu();
			Integer unreadMessage = messageProcessor.getCountUnreadMessages(member.getUsername());
			String unread = unreadMessage != 0 ? String.valueOf(unreadMessage) : "";
			List<String> messageSummary = messageProcessor.loadUnreadMessages(changecredential.getMsisdn());
			accessProcessor.changeCredential(changecredential);

			model.addAttribute(member);
			model.addAttribute("notification", "success");
			model.addAttribute("title", "Change Credential");
			model.addAttribute("message", "Change credential has been changed successfully");
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			model.addAttribute("welcomeMenu", welcomeMenu);

			return new ModelAndView("index");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/topupMember", method = RequestMethod.GET)
	public ModelAndView topupMember(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID, Model model)
			throws MalformedURLException {
		if (sessionID.equalsIgnoreCase("defaultCookieValue") || sessionID.equalsIgnoreCase(null)) {
			return new ModelAndView("redirect:/login");
		}
		IMap<String, Member> memberMap = instance.getMap("Member");
		Member member = memberMap.get(sessionID);
		if (member == null) {
			return new ModelAndView("redirect:/login");
		}
		AdminMenu adminMenu = menuProcessor.getMenuList(member.getGroupID());
		List<String> mainMenu = adminMenu.getMainMenu();
		Integer unreadMessage = messageProcessor.getCountUnreadMessages(member.getUsername());
		String unread = unreadMessage != 0 ? String.valueOf(unreadMessage) : "";
		List<String> messageSummary = messageProcessor.loadUnreadMessages(member.getUsername());

		model.addAttribute(member);
		model.addAttribute("mainMenu", mainMenu);
		model.addAttribute("unreadMessage", unread);
		model.addAttribute("messageSummary", messageSummary);
		return new ModelAndView("topupMember");
	}

	@RequestMapping(value = "/topupMemberInquiry", method = RequestMethod.POST)
	public ModelAndView topupMemberInquiry(@Valid @ModelAttribute("topupmember") TopupMember topup,
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID, ModelMap model) {
		try {
			if (sessionID.equalsIgnoreCase("defaultCookieValue") || sessionID.equalsIgnoreCase(null)) {
				return new ModelAndView("redirect:/login");
			}
			IMap<String, Member> memberMap = instance.getMap("Member");
			Member member = memberMap.get(sessionID);
			if (member == null) {
				return new ModelAndView("redirect:/login");
			}
			AdminMenu adminMenu = menuProcessor.getMenuList(member.getGroupID());
			List<String> mainMenu = adminMenu.getMainMenu();
			Integer unreadMessage = messageProcessor.getCountUnreadMessages(member.getUsername());
			String unread = unreadMessage != 0 ? String.valueOf(unreadMessage) : "";
			List<String> messageSummary = messageProcessor.loadUnreadMessages(member.getUsername());

			topup.setTransferTypeID(contextLoader.getTopupAgentTransferTypeID());
			RequestPaymentConfirmationResponse rpcResponse = paymentProcessor.topupInquiry(topup, member.getUsername());

			if (rpcResponse.getStatus().getMessage().equalsIgnoreCase("PROCESSED")) {
				model.addAttribute(member);
				model.addAttribute("title", "Topup Member");
				model.addAttribute("mainMenu", mainMenu);
				model.addAttribute("unreadMessage", unread);
				model.addAttribute("messageSummary", messageSummary);
				model.addAttribute("description", topup.getDescription());
				model.addAttribute("fee", rpcResponse.getTotalFees());
				model.addAttribute("amount", rpcResponse.getTransactionAmount());
				model.addAttribute("totalAmount", rpcResponse.getFinalAmount());
				model.addAttribute("toAccount", rpcResponse.getToMember().getUsername());
				model.addAttribute("requestID", rpcResponse.getRequestID());
				model.addAttribute("status", rpcResponse.getStatus().getMessage());
				return new ModelAndView("topupMemberInquiry");
			} else {
				model.addAttribute(member);
				model.addAttribute("notification", "error");
				model.addAttribute("title", "Topup Member");
				model.addAttribute("message", "Topup Member Inquiry failed : " + rpcResponse.getStatus().getMessage());
				model.addAttribute("mainMenu", mainMenu);
				model.addAttribute("unreadMessage", unread);
				model.addAttribute("messageSummary", messageSummary);
				return new ModelAndView("topupMember");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/topupMemberPayment", method = RequestMethod.POST)
	public ModelAndView topupMemberPayment(@Valid @ModelAttribute("topupmember") TopupMember topup,
			@RequestParam(value = "otp") String otp, @RequestParam(value = "requestID") String requestID,
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID, Model model) {
		try {
			if (sessionID.equalsIgnoreCase("defaultCookieValue") || sessionID.equalsIgnoreCase(null)) {
				return new ModelAndView("redirect:/login");
			}
			IMap<String, Member> memberMap = instance.getMap("Member");
			Member member = memberMap.get(sessionID);
			if (member == null) {
				return new ModelAndView("redirect:/login");
			}
			AdminMenu adminMenu = menuProcessor.getMenuList(member.getGroupID());
			List<String> mainMenu = adminMenu.getMainMenu();
			Integer unreadMessage = messageProcessor.getCountUnreadMessages(member.getUsername());
			String unread = unreadMessage != 0 ? String.valueOf(unreadMessage) : "";
			List<String> messageSummary = messageProcessor.loadUnreadMessages(member.getUsername());
			PaymentResponse paymentResponse = paymentProcessor.topupPayment(otp, requestID);

			if (paymentResponse.getStatus().getMessage().equalsIgnoreCase("PROCESSED")) {
				model.addAttribute(member);
				model.addAttribute("notification", "success");
				model.addAttribute("title", "Topup Member");
				model.addAttribute("message", "Topup member has been successfully");
				model.addAttribute("mainMenu", mainMenu);
				model.addAttribute("unreadMessage", unread);
				model.addAttribute("messageSummary", messageSummary);
				model.addAttribute("description", paymentResponse.getDescription());
				model.addAttribute("amount", paymentResponse.getAmount());
				model.addAttribute("toAccount", topup.getToAccount());
				model.addAttribute("fee", paymentResponse.getTotalFees());
				model.addAttribute("totalAmount", paymentResponse.getFinalAmount());
				model.addAttribute("traceNumber", paymentResponse.getTraceNumber());
				model.addAttribute("transactionNumber", paymentResponse.getTransactionNumber());
				model.addAttribute("status", paymentResponse.getStatus().getMessage());
				return new ModelAndView("topupMemberPayment");
			} else {
				model.addAttribute(member);
				model.addAttribute("notification", "error");
				model.addAttribute("title", "Topup Member");
				model.addAttribute("message",
						paymentResponse.getStatus().getDescription() + ": " + paymentResponse.getStatus().getMessage());
				model.addAttribute("mainMenu", mainMenu);
				model.addAttribute("unreadMessage", unread);
				model.addAttribute("messageSummary", messageSummary);
				return new ModelAndView("topupMember");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/cashoutMember", method = RequestMethod.GET)
	public ModelAndView cashoutMember(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID, Model model)
			throws MalformedURLException {
		if (sessionID.equalsIgnoreCase("defaultCookieValue") || sessionID.equalsIgnoreCase(null)) {
			return new ModelAndView("redirect:/login");
		}
		IMap<String, Member> memberMap = instance.getMap("Member");
		Member member = memberMap.get(sessionID);
		if (member == null) {
			return new ModelAndView("redirect:/login");
		}
		AdminMenu adminMenu = menuProcessor.getMenuList(member.getGroupID());
		List<String> mainMenu = adminMenu.getMainMenu();
		Integer unreadMessage = messageProcessor.getCountUnreadMessages(member.getUsername());
		String unread = unreadMessage != 0 ? String.valueOf(unreadMessage) : "";
		List<String> messageSummary = messageProcessor.loadUnreadMessages(member.getUsername());

		model.addAttribute(member);
		model.addAttribute("mainMenu", mainMenu);
		model.addAttribute("unreadMessage", unread);
		model.addAttribute("messageSummary", messageSummary);
		return new ModelAndView("cashoutMember");
	}

	@RequestMapping(value = "/cashoutMemberInquiry", method = RequestMethod.POST)
	public ModelAndView cashoutMemberInquiry(@Valid @ModelAttribute("cashoutmember") CashoutMember cashout,
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID, ModelMap model) {
		try {
			if (sessionID.equalsIgnoreCase("defaultCookieValue") || sessionID.equalsIgnoreCase(null)) {
				return new ModelAndView("redirect:/login");
			}
			IMap<String, Member> memberMap = instance.getMap("Member");
			Member member = memberMap.get(sessionID);
			if (member == null) {
				return new ModelAndView("redirect:/login");
			}
			AdminMenu adminMenu = menuProcessor.getMenuList(member.getGroupID());
			List<String> mainMenu = adminMenu.getMainMenu();
			Integer unreadMessage = messageProcessor.getCountUnreadMessages(member.getUsername());
			String unread = unreadMessage != 0 ? String.valueOf(unreadMessage) : "";
			List<String> messageSummary = messageProcessor.loadUnreadMessages(member.getUsername());
			AgentCashoutResponse acRes = paymentProcessor.cashoutAgentInquiry(cashout, member.getUsername());

			if (acRes.getStatus().getMessage().equalsIgnoreCase("PROCESSED")) {
				model.addAttribute(member);
				model.addAttribute("mainMenu", mainMenu);
				model.addAttribute("unreadMessage", unread);
				model.addAttribute("messageSummary", messageSummary);
				model.addAttribute("description", cashout.getDescription());
				model.addAttribute("fee");
				model.addAttribute("amount", cashout.getAmount());
				model.addAttribute("totalAmount");
				model.addAttribute("otp", cashout.getOtp());
				model.addAttribute("fromAccount", cashout.getFromAccount());
				model.addAttribute("toAccount", member.getUsername());
				return new ModelAndView("cashoutMemberInquiry");
			} else {
				model.addAttribute(member);
				model.addAttribute("notification", "error");
				model.addAttribute("title", "Cashout Member");
				model.addAttribute("message", acRes.getStatus().getDescription());
				model.addAttribute("mainMenu", mainMenu);
				model.addAttribute("unreadMessage", unread);
				model.addAttribute("messageSummary", messageSummary);
				return new ModelAndView("cashoutMember");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/cashoutMemberPayment", method = RequestMethod.POST)
	public ModelAndView cashoutMemberPayment(@Valid @ModelAttribute("cashoutmember") CashoutMember cashout,
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID, Model model) {
		try {
			if (sessionID.equalsIgnoreCase("defaultCookieValue") || sessionID.equalsIgnoreCase(null)) {
				return new ModelAndView("redirect:/login");
			}
			IMap<String, Member> memberMap = instance.getMap("Member");
			Member member = memberMap.get(sessionID);
			if (member == null) {
				return new ModelAndView("redirect:/login");
			}
			AdminMenu adminMenu = menuProcessor.getMenuList(member.getGroupID());
			List<String> mainMenu = adminMenu.getMainMenu();
			Integer unreadMessage = messageProcessor.getCountUnreadMessages(member.getUsername());
			String unread = unreadMessage != 0 ? String.valueOf(unreadMessage) : "";
			List<String> messageSummary = messageProcessor.loadUnreadMessages(member.getUsername());
			ConfirmAgentCashoutResponse paymentResponse = paymentProcessor.cashoutAgentPayment(cashout,
					member.getUsername());

			if (paymentResponse.getStatus().getMessage().equalsIgnoreCase("PROCESSED")) {
				model.addAttribute(member);
				model.addAttribute("notification", "success");
				model.addAttribute("title", "Cashout Member");
				model.addAttribute("message", "Cashout member has been successfully");
				model.addAttribute("mainMenu", mainMenu);
				model.addAttribute("unreadMessage", unread);
				model.addAttribute("messageSummary", messageSummary);
				model.addAttribute("description", paymentResponse.getDescription());
				model.addAttribute("amount", paymentResponse.getAmount());
				model.addAttribute("fromAccount", cashout.getFromAccount());
				model.addAttribute("toAccount", member.getUsername());
				model.addAttribute("fee", paymentResponse.getTotalFees());
				model.addAttribute("totalAmount", paymentResponse.getFinalAmount());
				model.addAttribute("traceNumber", paymentResponse.getTraceNumber());
				model.addAttribute("transactionNumber", paymentResponse.getTransactionNumber());
				model.addAttribute("status", paymentResponse.getStatus().getMessage());
				return new ModelAndView("cashoutMemberPayment");
			} else {
				model.addAttribute(member);
				model.addAttribute("notification", "error");
				model.addAttribute("title", "Cashout Member");
				model.addAttribute("message",
						paymentResponse.getStatus().getDescription() + ": " + paymentResponse.getStatus().getMessage());
				model.addAttribute("mainMenu", mainMenu);
				model.addAttribute("unreadMessage", unread);
				model.addAttribute("messageSummary", messageSummary);
				return new ModelAndView("cashoutMember");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/memberKyc", method = RequestMethod.GET)
	public ModelAndView kyc(@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			Model model) throws MalformedURLException {
		if (sessionID.equalsIgnoreCase("defaultCookieValue") || sessionID.equalsIgnoreCase(null)) {
			return new ModelAndView("redirect:/login");
		}
		IMap<String, Member> memberMap = instance.getMap("Member");
		Member member = memberMap.get(sessionID);
		if (member == null) {
			return new ModelAndView("redirect:/login");
		}
		AdminMenu adminMenu = menuProcessor.getMenuList(member.getGroupID());
		List<String> mainMenu = adminMenu.getMainMenu();
		Integer unreadMessage = messageProcessor.getCountUnreadMessages(member.getUsername());
		String unread = unreadMessage != 0 ? String.valueOf(unreadMessage) : "";
		List<String> messageSummary = messageProcessor.loadUnreadMessages(member.getUsername());

		model.addAttribute(member);
		model.addAttribute("mainMenu", mainMenu);
		model.addAttribute("unreadMessage", unread);
		model.addAttribute("messageSummary", messageSummary);
		if (member.getGroupName().equalsIgnoreCase("ADMIN")) {
			return new ModelAndView("memberKycAdmin");
		} else {
			return new ModelAndView("memberKyc");
		}
	}

	@ResponseBody
	@RequestMapping(value = "/kycListData", method = RequestMethod.GET)
	public String kycListData(@RequestParam(value = "start") Integer start,
			@RequestParam(value = "length") Integer length, @RequestParam(value = "username") String username)
			throws Exception_Exception, MalformedURLException {
		String kyc = memberProcessor.loadKycRequest(start, length);
		return kyc;
	}

	@RequestMapping(value = "/searchMember", method = RequestMethod.GET)
	public ModelAndView searchMember(@Valid @ModelAttribute("searchmember") UpgradeMember search,
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID, Model model) {
		try {
			if (sessionID.equalsIgnoreCase("defaultCookieValue") || sessionID.equalsIgnoreCase(null)) {
				return new ModelAndView("redirect:/login");
			}
			IMap<String, Member> memberMap = instance.getMap("Member");
			Member member = memberMap.get(sessionID);
			if (member == null) {
				return new ModelAndView("redirect:/login");
			}
			AdminMenu adminMenu = menuProcessor.getMenuList(member.getGroupID());
			List<String> mainMenu = adminMenu.getMainMenu();
			Integer unreadMessage = messageProcessor.getCountUnreadMessages(member.getUsername());
			String unread = unreadMessage != 0 ? String.valueOf(unreadMessage) : "";
			List<String> messageSummary = messageProcessor.loadUnreadMessages(member.getUsername());
			Map<String, Object> memberDetails = memberProcessor.getMemberDetails(search.getMsisdn());

			model.addAttribute(member);
			model.addAllAttributes(memberDetails);
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			return new ModelAndView("searchMember");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/upgradeMember", method = RequestMethod.POST)
	public ModelAndView upgradeMember(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@Valid @ModelAttribute("uploadFiles") UploadFiles uploadFiles,
			@Valid @ModelAttribute("upgrademember") UpgradeMember upgradeMember, Model model)
			throws IllegalStateException, IOException, Exception_Exception, DatatypeConfigurationException {
		try {
			if (sessionID.equalsIgnoreCase("defaultCookieValue") || sessionID.equalsIgnoreCase(null)) {
				return new ModelAndView("redirect:/login");
			}
			IMap<String, Member> memberMap = instance.getMap("Member");
			Member member = memberMap.get(sessionID);
			if (member == null) {
				return new ModelAndView("redirect:/login");
			}
			AdminMenu adminMenu = menuProcessor.getMenuList(member.getGroupID());
			List<String> mainMenu = adminMenu.getMainMenu();
			Integer unreadMessage = messageProcessor.getCountUnreadMessages(member.getUsername());
			String unread = unreadMessage != 0 ? String.valueOf(unreadMessage) : "";
			List<String> messageSummary = messageProcessor.loadUnreadMessages(member.getUsername());

			List<MultipartFile> files = uploadFiles.getFiles();
			List<String> fileNames = new ArrayList<String>();
			int count = 0;
			if (null != files && files.size() > 0) {
				for (MultipartFile multipartFile : files) {
					count = count + 1;
					byte[] bytes = multipartFile.getBytes();
					String fileName = multipartFile.getOriginalFilename();
					if (!"".equalsIgnoreCase(fileName)) {
						fileNames.add(fileName);
						String rootPath = contextLoader.getPathFile();
						try {
							File serverFile = new File(rootPath + count + "-" + upgradeMember.getMsisdn() + "."
									+ FilenameUtils.getExtension(fileName));

							BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
							stream.write(bytes);
							stream.close();

							BufferedImage originalImage = ImageIO.read(serverFile);
							System.out.println("TYPE " + count + ": " + originalImage.getType());
							int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB
									: originalImage.getType();

							BufferedImage resizedImage = new BufferedImage(500, 500, type);
							Graphics2D g = resizedImage.createGraphics();
							g.drawImage(originalImage, 0, 0, 500, 500, null);
							g.dispose();
							ImageIO.write(resizedImage, "png", serverFile);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
			memberProcessor.requestKycMember(upgradeMember, uploadFiles);

			model.addAttribute(member);
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			model.addAttribute("notification", "success");
			model.addAttribute("title", "KYC Member");
			model.addAttribute("message", "KYC member requested has been successfully");

			if (member.getGroupName().equalsIgnoreCase("ADMIN")) {
				return new ModelAndView("memberKycAdmin");
			} else {
				return new ModelAndView("memberKyc");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/editKyc", method = RequestMethod.GET)
	public ModelAndView editKyc(@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@RequestParam(value = "id") int id, Model model) throws Exception_Exception, IOException {
		try {
			if (sessionID.equalsIgnoreCase("defaultCookieValue") || sessionID.equalsIgnoreCase(null)) {
				return new ModelAndView("redirect:/login");
			}
			IMap<String, Member> memberMap = instance.getMap("Member");
			Member member = memberMap.get(sessionID);
			if (member == null) {
				return new ModelAndView("redirect:/login");
			}
			AdminMenu adminMenu = menuProcessor.getMenuList(member.getGroupID());
			List<String> mainMenu = adminMenu.getMainMenu();
			Integer unreadMessage = messageProcessor.getCountUnreadMessages(member.getUsername());
			String unread = unreadMessage != 0 ? String.valueOf(unreadMessage) : "";
			List<String> messageSummary = messageProcessor.loadUnreadMessages(member.getUsername());
			Map<String, Object> memberDetails = memberProcessor.getMemberKycDetails(id);

			model.addAttribute(member);
			model.addAllAttributes(memberDetails);
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			return new ModelAndView("editKyc");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/kyc", method = RequestMethod.POST, params = "confirm=confirmkyc")
	public ModelAndView detailKyc(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@Valid @ModelAttribute("confirmkyc") UpgradeMember upgradeMember, Model model)
			throws Exception_Exception, IOException {
		if (sessionID.equalsIgnoreCase("defaultCookieValue") || sessionID.equalsIgnoreCase(null)) {
			return new ModelAndView("redirect:/login");
		}
		IMap<String, Member> memberMap = instance.getMap("Member");
		Member member = memberMap.get(sessionID);
		if (member == null) {
			return new ModelAndView("redirect:/login");
		}
		AdminMenu adminMenu = menuProcessor.getMenuList(member.getGroupID());
		List<String> mainMenu = adminMenu.getMainMenu();
		Integer unreadMessage = messageProcessor.getCountUnreadMessages(member.getUsername());
		String unread = unreadMessage != 0 ? String.valueOf(unreadMessage) : "";
		List<String> messageSummary = messageProcessor.loadUnreadMessages(member.getUsername());
		Map<String, Object> memberDetails = memberProcessor.getMemberKycDetails(upgradeMember.getId());

		model.addAttribute(member);
		model.addAllAttributes(memberDetails);
		model.addAttribute("mainMenu", mainMenu);
		model.addAttribute("unreadMessage", unread);
		model.addAttribute("messageSummary", messageSummary);
		if (member.getGroupName().equals("ADMIN")) {
			return new ModelAndView("detailKycAdmin");
		} else {
			return new ModelAndView("detailKyc");
		}
	}

	@RequestMapping(value = "/kyc", method = RequestMethod.POST, params = "confirm=approve")
	public ModelAndView approveKYC(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@Valid @ModelAttribute("confirmkyc") UpgradeMember upgradeMember, Model model)
			throws IllegalStateException, IOException, Exception_Exception {
		try {
			if (sessionID.equalsIgnoreCase("defaultCookieValue") || sessionID.equalsIgnoreCase(null)) {
				return new ModelAndView("redirect:/login");
			}
			IMap<String, Member> memberMap = instance.getMap("Member");
			Member member = memberMap.get(sessionID);
			if (member == null) {
				return new ModelAndView("redirect:/login");
			}
			AdminMenu adminMenu = menuProcessor.getMenuList(member.getGroupID());
			List<String> mainMenu = adminMenu.getMainMenu();
			Integer unreadMessage = messageProcessor.getCountUnreadMessages(member.getUsername());
			String unread = unreadMessage != 0 ? String.valueOf(unreadMessage) : "";
			List<String> messageSummary = messageProcessor.loadUnreadMessages(member.getUsername());
			ConfirmKYCResponse confirmKYCResponse = memberProcessor.approveKycRequest(upgradeMember.getId(),
					member.getUsername());

			if (confirmKYCResponse.getStatus().getMessage().equalsIgnoreCase("PROCESSED")) {
				model.addAttribute(member);
				model.addAttribute("mainMenu", mainMenu);
				model.addAttribute("unreadMessage", unread);
				model.addAttribute("messageSummary", messageSummary);
				model.addAttribute("notification", "success");
				model.addAttribute("title", "KYC Member");
				model.addAttribute("message", "KYC member confirmed has been successfully");
			} else {
				model.addAttribute(member);
				model.addAttribute("mainMenu", mainMenu);
				model.addAttribute("unreadMessage", unread);
				model.addAttribute("messageSummary", messageSummary);
				model.addAttribute("notification", "error");
				model.addAttribute("title", "KYC Member");
				model.addAttribute("message",
						"KYC member confirmed has been failed : " + confirmKYCResponse.getStatus().getMessage());
			}
			return new ModelAndView("memberKycAdmin");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/kyc", method = RequestMethod.POST, params = "confirm=reject")
	public ModelAndView rejectKYC(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@Valid @ModelAttribute("confirmkyc") UpgradeMember upgradeMember, Model model)
			throws IllegalStateException, IOException, Exception_Exception {
		try {
			if (sessionID.equalsIgnoreCase("defaultCookieValue") || sessionID.equalsIgnoreCase(null)) {
				return new ModelAndView("redirect:/login");
			}
			IMap<String, Member> memberMap = instance.getMap("Member");
			Member member = memberMap.get(sessionID);
			if (member == null) {
				return new ModelAndView("redirect:/login");
			}
			AdminMenu adminMenu = menuProcessor.getMenuList(member.getGroupID());
			List<String> mainMenu = adminMenu.getMainMenu();
			Integer unreadMessage = messageProcessor.getCountUnreadMessages(member.getUsername());
			String unread = unreadMessage != 0 ? String.valueOf(unreadMessage) : "";
			List<String> messageSummary = messageProcessor.loadUnreadMessages(member.getUsername());
			ConfirmKYCResponse confirmKYCResponse = memberProcessor.rejectKycRequest(upgradeMember.getId(),
					member.getUsername(), upgradeMember.getDescription());

			if (confirmKYCResponse.getStatus().getMessage().equalsIgnoreCase("PROCESSED")) {
				model.addAttribute(member);
				model.addAttribute("mainMenu", mainMenu);
				model.addAttribute("unreadMessage", unread);
				model.addAttribute("messageSummary", messageSummary);
				model.addAttribute("notification", "success");
				model.addAttribute("title", "KYC Member");
				model.addAttribute("message", "KYC member confirmed has been successfully");
			} else {
				model.addAttribute(member);
				model.addAttribute("mainMenu", mainMenu);
				model.addAttribute("unreadMessage", unread);
				model.addAttribute("messageSummary", messageSummary);
				model.addAttribute("notification", "error");
				model.addAttribute("title", "KYC Member");
				model.addAttribute("message",
						"KYC member confirmed has been failed : " + confirmKYCResponse.getStatus().getMessage());
			}
			return new ModelAndView("memberKycAdmin");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/kyc", method = RequestMethod.POST, params = "confirm=edit")
	public ModelAndView editMember(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@Valid @ModelAttribute("editmember") UpgradeMember editmember, ModelMap model) {
		try {
			if (sessionID.equalsIgnoreCase("defaultCookieValue") || sessionID.equalsIgnoreCase(null)) {
				return new ModelAndView("redirect:/login");
			}
			IMap<String, Member> memberMap = instance.getMap("Member");
			Member member = memberMap.get(sessionID);
			if (member == null) {
				return new ModelAndView("redirect:/login");
			}
			AdminMenu adminMenu = menuProcessor.getMenuList(member.getGroupID());
			List<String> mainMenu = adminMenu.getMainMenu();
			Integer unreadMessage = messageProcessor.getCountUnreadMessages(member.getUsername());
			String unread = unreadMessage != 0 ? String.valueOf(unreadMessage) : "";
			List<String> messageSummary = messageProcessor.loadUnreadMessages(member.getUsername());
			memberProcessor.editMember(editmember);

			model.addAttribute(member);
			model.addAttribute("notification", "success");
			model.addAttribute("title", "Edit Member");
			model.addAttribute("message", "Edit profile has been successfully");
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);

			if (editmember.getType().equalsIgnoreCase("member")) {
				return new ModelAndView("member");
			} else {
				return new ModelAndView("memberKycAdmin");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/accessMember", method = RequestMethod.POST, params = "access=unblockPin")
	public ModelAndView unblockCredential(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@Valid @ModelAttribute("accessmember") UpgradeMember accessmember, ModelMap model) {
		try {
			if (sessionID.equalsIgnoreCase("defaultCookieValue") || sessionID.equalsIgnoreCase(null)) {
				return new ModelAndView("redirect:/login");
			}
			IMap<String, Member> memberMap = instance.getMap("Member");
			Member member = memberMap.get(sessionID);
			if (member == null) {
				return new ModelAndView("redirect:/login");
			}
			AdminMenu adminMenu = menuProcessor.getMenuList(member.getGroupID());
			List<String> mainMenu = adminMenu.getMainMenu();
			Integer unreadMessage = messageProcessor.getCountUnreadMessages(member.getUsername());
			String unread = unreadMessage != 0 ? String.valueOf(unreadMessage) : "";
			List<String> messageSummary = messageProcessor.loadUnreadMessages(member.getUsername());
			accessProcessor.unblockCredential(accessmember);

			model.addAttribute(member);
			model.addAttribute("notification", "success");
			model.addAttribute("title", "Unblock PIN");
			model.addAttribute("message", "Unblock PIN has been successfully");
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);

			return new ModelAndView("memberAdmin");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/accessMember", method = RequestMethod.POST, params = "access=resetPin")
	public ModelAndView changeCredential(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@Valid @ModelAttribute("accessmember") UpgradeMember accessmember, ModelMap model) {
		try {
			if (sessionID.equalsIgnoreCase("defaultCookieValue") || sessionID.equalsIgnoreCase(null)) {
				return new ModelAndView("redirect:/login");
			}
			IMap<String, Member> memberMap = instance.getMap("Member");
			Member member = memberMap.get(sessionID);
			if (member == null) {
				return new ModelAndView("redirect:/login");
			}
			AdminMenu adminMenu = menuProcessor.getMenuList(member.getGroupID());
			List<String> mainMenu = adminMenu.getMainMenu();
			Integer unreadMessage = messageProcessor.getCountUnreadMessages(member.getUsername());
			String unread = unreadMessage != 0 ? String.valueOf(unreadMessage) : "";
			List<String> messageSummary = messageProcessor.loadUnreadMessages(member.getUsername());
			ResetCredentialResponse rcr = accessProcessor.resetCredential(accessmember);

			if (rcr.getStatus().getMessage().equalsIgnoreCase("PROCESSED")) {
				model.addAttribute(member);
				model.addAttribute("notification", "success");
				model.addAttribute("title", "Reset PIN");
				model.addAttribute("message", "Reset PIN has been successfully");
				model.addAttribute("mainMenu", mainMenu);
				model.addAttribute("unreadMessage", unread);
				model.addAttribute("messageSummary", messageSummary);
				return new ModelAndView("memberAdmin");
			} else {
				model.addAttribute(member);
				model.addAttribute("notification", "error");
				model.addAttribute("title", "RESET PIN");
				model.addAttribute("message", rcr.getStatus().getDescription());
				model.addAttribute("mainMenu", mainMenu);
				model.addAttribute("unreadMessage", unread);
				model.addAttribute("messageSummary", messageSummary);
				return new ModelAndView("memberAdmin");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/accessMember", method = RequestMethod.POST, params = "access=changeGroup")
	public ModelAndView changeGroup(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@Valid @ModelAttribute("accessmember") UpgradeMember accessmember, ModelMap model) {
		try {
			if (sessionID.equalsIgnoreCase("defaultCookieValue") || sessionID.equalsIgnoreCase(null)) {
				return new ModelAndView("redirect:/login");
			}
			IMap<String, Member> memberMap = instance.getMap("Member");
			Member member = memberMap.get(sessionID);
			if (member == null) {
				return new ModelAndView("redirect:/login");
			}
			AdminMenu adminMenu = menuProcessor.getMenuList(member.getGroupID());
			List<String> mainMenu = adminMenu.getMainMenu();
			Integer unreadMessage = messageProcessor.getCountUnreadMessages(member.getUsername());
			String unread = unreadMessage != 0 ? String.valueOf(unreadMessage) : "";
			List<String> messageSummary = messageProcessor.loadUnreadMessages(member.getUsername());
			Map<String, Object> memberDetails = memberProcessor.getMemberDetails(accessmember.getMsisdn());
			List<String> listGroup = groupProcessor.getListGroup();

			model.addAttribute(member);
			model.addAttribute("username", accessmember.getMsisdn());
			model.addAttribute("groupName", memberDetails.get("groupName"));
			model.addAttribute("name", memberDetails.get("name"));
			model.addAttribute("email", memberDetails.get("email"));
			model.addAttribute("listGroup", listGroup);
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			return new ModelAndView("changeGroup");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/changeGroup", method = RequestMethod.POST)
	public ModelAndView confirmChangeGroup(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@Valid @ModelAttribute("upgrademember") UpgradeMember accessmember, ModelMap model) {
		try {
			if (sessionID.equalsIgnoreCase("defaultCookieValue") || sessionID.equalsIgnoreCase(null)) {
				return new ModelAndView("redirect:/login");
			}
			IMap<String, Member> memberMap = instance.getMap("Member");
			Member member = memberMap.get(sessionID);
			if (member == null) {
				return new ModelAndView("redirect:/login");
			}
			AdminMenu adminMenu = menuProcessor.getMenuList(member.getGroupID());
			List<String> mainMenu = adminMenu.getMainMenu();
			Integer unreadMessage = messageProcessor.getCountUnreadMessages(member.getUsername());
			String unread = unreadMessage != 0 ? String.valueOf(unreadMessage) : "";
			List<String> messageSummary = messageProcessor.loadUnreadMessages(member.getUsername());
			memberProcessor.changeGroup(accessmember);

			model.addAttribute(member);
			model.addAttribute("notification", "success");
			model.addAttribute("title", "Change Group");
			model.addAttribute("message", "Change Group has been successfully");
			model.addAttribute("username", accessmember.getMsisdn());
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			return new ModelAndView("memberAdmin");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/accessMember", method = RequestMethod.POST, params = "access=changePin")
	public ModelAndView changePin(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@Valid @ModelAttribute("upgrademember") UpgradeMember accessmember, ModelMap model) {
		try {
			if (sessionID.equalsIgnoreCase("defaultCookieValue") || sessionID.equalsIgnoreCase(null)) {
				return new ModelAndView("redirect:/login");
			}
			IMap<String, Member> memberMap = instance.getMap("Member");
			Member member = memberMap.get(sessionID);
			if (member == null) {
				return new ModelAndView("redirect:/login");
			}
			AdminMenu adminMenu = menuProcessor.getMenuList(member.getGroupID());
			List<String> mainMenu = adminMenu.getMainMenu();
			Integer unreadMessage = messageProcessor.getCountUnreadMessages(member.getUsername());
			String unread = unreadMessage != 0 ? String.valueOf(unreadMessage) : "";
			List<String> messageSummary = messageProcessor.loadUnreadMessages(member.getUsername());

			model.addAttribute(member);
			model.addAttribute("username", accessmember.getMsisdn());
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			return new ModelAndView("changeCredential");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/pos", method = RequestMethod.GET)
	public ModelAndView pos(@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			Model model) {
		try {
			if (sessionID.equalsIgnoreCase("defaultCookieValue") || sessionID.equalsIgnoreCase(null)) {
				return new ModelAndView("redirect:/login");
			}
			IMap<String, Member> memberMap = instance.getMap("Member");
			Member member = memberMap.get(sessionID);
			if (member == null) {
				return new ModelAndView("redirect:/login");
			}
			AdminMenu adminMenu = menuProcessor.getMenuList(member.getGroupID());
			List<String> mainMenu = adminMenu.getMainMenu();
			Integer unreadMessage = messageProcessor.getCountUnreadMessages(member.getUsername());
			String unread = unreadMessage != 0 ? String.valueOf(unreadMessage) : "";
			List<String> messageSummary = messageProcessor.loadUnreadMessages(member.getUsername());

			model.addAttribute(member);
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			
			return new ModelAndView("pos");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@ResponseBody
	@RequestMapping(value = "/posData", method = RequestMethod.GET)
	public String posData(@RequestParam(value = "username", required = false) String username,
			@RequestParam(value = "start") Integer start, @RequestParam(value = "length") Integer length)
			throws MalformedURLException, org.bellatrix.services.ws.pos.Exception_Exception {
		String jsonData = posProcessor.loadPOSList(username, Integer.valueOf(start), Integer.valueOf(length));
		return jsonData;
	}


	@RequestMapping(value = "/createPos", method = RequestMethod.GET)
	public ModelAndView createPos(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID, Model model) {
		try {
			if (sessionID.equalsIgnoreCase("defaultCookieValue") || sessionID.equalsIgnoreCase(null)) {
				return new ModelAndView("redirect:/login");
			}
			IMap<String, Member> memberMap = instance.getMap("Member");
			Member member = memberMap.get(sessionID);
			if (member == null) {
				return new ModelAndView("redirect:/login");
			}
			AdminMenu adminMenu = menuProcessor.getMenuList(member.getGroupID());
			List<String> mainMenu = adminMenu.getMainMenu();
			Integer unreadMessage = messageProcessor.getCountUnreadMessages(member.getUsername());
			String unread = unreadMessage != 0 ? String.valueOf(unreadMessage) : "";
			List<String> messageSummary = messageProcessor.loadUnreadMessages(member.getUsername());

			model.addAttribute(member);
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			return new ModelAndView("createPOS");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/createPosForm", method = RequestMethod.POST)
	public ModelAndView createPosForm(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@Valid @ModelAttribute("newpos") Pos pos, BindingResult result, ModelMap model) {
		try {
			if (result.hasErrors()) {
				return new ModelAndView("page_500");
			}
			if (sessionID.equalsIgnoreCase("defaultCookieValue") || sessionID.equalsIgnoreCase(null)) {
				return new ModelAndView("redirect:/login");
			}
			IMap<String, Member> memberMap = instance.getMap("Member");
			Member member = memberMap.get(sessionID);
			if (member == null) {
				return new ModelAndView("redirect:/login");
			}
			posProcessor.registerPOS(pos, member.getUsername());

			model.addAttribute(member);
			return new ModelAndView("redirect:/createPosResult", model);

		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/createPosResult", method = RequestMethod.GET)
	public ModelAndView createPosResult(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID, ModelMap model) {
		try {
			if (sessionID.equalsIgnoreCase("defaultCookieValue") || sessionID.equalsIgnoreCase(null)) {
				return new ModelAndView("redirect:/login");
			}
			IMap<String, Member> memberMap = instance.getMap("Member");
			Member member = memberMap.get(sessionID);
			if (member == null) {
				return new ModelAndView("redirect:/login");
			}
			AdminMenu adminMenu = menuProcessor.getMenuList(member.getGroupID());
			List<String> mainMenu = adminMenu.getMainMenu();
			Integer unreadMessage = messageProcessor.getCountUnreadMessages(member.getUsername());
			String unread = unreadMessage != 0 ? String.valueOf(unreadMessage) : "";
			List<String> messageSummary = messageProcessor.loadUnreadMessages(member.getUsername());

			model.addAttribute(member);
			model.addAttribute("notification", "success");
			model.addAttribute("title", "Create POS");
			model.addAttribute("message", "Pos has been created successfully");
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			return new ModelAndView("pos");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/detailPos", method = RequestMethod.GET)
	public ModelAndView detailPos(@Valid @RequestParam(value = "username") String username,
			@RequestParam(value = "id") Integer ID,
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID, Model model) {
		try {
			if (sessionID.equalsIgnoreCase("defaultCookieValue") || sessionID.equalsIgnoreCase(null)) {
				return new ModelAndView("redirect:/login");
			}
			IMap<String, Member> memberMap = instance.getMap("Member");
			Member member = memberMap.get(sessionID);
			if (member == null) {
				return new ModelAndView("redirect:/login");
			}
			AdminMenu adminMenu = menuProcessor.getMenuList(member.getGroupID());
			List<String> mainMenu = adminMenu.getMainMenu();
			Integer unreadMessage = messageProcessor.getCountUnreadMessages(member.getUsername());
			String unread = unreadMessage != 0 ? String.valueOf(unreadMessage) : "";
			List<String> messageSummary = messageProcessor.loadUnreadMessages(member.getUsername());
			Map<String, Object> posDetails = posProcessor.detailPos(username, ID);

			model.addAttribute(member);
			model.addAllAttributes(posDetails);
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);

			return new ModelAndView("detailPOS");

		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/editPos", method = RequestMethod.GET)
	public ModelAndView editPos(@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@RequestParam(value = "id") Integer ID, @RequestParam(value = "username") String username, Model model)
			throws Exception_Exception, IOException {
		try {
			if (sessionID.equalsIgnoreCase("defaultCookieValue") || sessionID.equalsIgnoreCase(null)) {
				return new ModelAndView("redirect:/login");
			}
			IMap<String, Member> memberMap = instance.getMap("Member");
			Member member = memberMap.get(sessionID);
			if (member == null) {
				return new ModelAndView("redirect:/login");
			}
			AdminMenu adminMenu = menuProcessor.getMenuList(member.getGroupID());
			List<String> mainMenu = adminMenu.getMainMenu();
			Integer unreadMessage = messageProcessor.getCountUnreadMessages(member.getUsername());
			String unread = unreadMessage != 0 ? String.valueOf(unreadMessage) : "";
			List<String> messageSummary = messageProcessor.loadUnreadMessages(member.getUsername());
			Map<String, Object> posDetails = posProcessor.detailPos(username, ID);

			model.addAttribute(member);
			model.addAllAttributes(posDetails);
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			return new ModelAndView("editPOS");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/pos", method = RequestMethod.POST, params = "confirm=edit")
	public ModelAndView editPos(@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@Valid @ModelAttribute("editpos") Pos editpos, ModelMap model) {
		try {
			if (sessionID.equalsIgnoreCase("defaultCookieValue") || sessionID.equalsIgnoreCase(null)) {
				return new ModelAndView("redirect:/login");
			}
			IMap<String, Member> memberMap = instance.getMap("Member");
			Member member = memberMap.get(sessionID);
			if (member == null) {
				return new ModelAndView("redirect:/login");
			}
			AdminMenu adminMenu = menuProcessor.getMenuList(member.getGroupID());
			List<String> mainMenu = adminMenu.getMainMenu();
			Integer unreadMessage = messageProcessor.getCountUnreadMessages(member.getUsername());
			String unread = unreadMessage != 0 ? String.valueOf(unreadMessage) : "";
			List<String> messageSummary = messageProcessor.loadUnreadMessages(member.getUsername());
			posProcessor.updatePos(editpos, member.getUsername());

			model.addAttribute(member);
			model.addAttribute("notification", "success");
			model.addAttribute("title", "Edit POS");
			model.addAttribute("message", "Edit POS has been successfully");
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);

			return new ModelAndView("pos");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/deletePos", method = RequestMethod.GET)
	public ModelAndView deletePOS(@RequestParam(value = "username", required = false) String username,
			@RequestParam(value = "id", required = true) Integer ID,
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID, ModelMap model) {

		try {
			if (sessionID.equalsIgnoreCase("defaultCookieValue") || sessionID.equalsIgnoreCase(null)) {
				return new ModelAndView("redirect:/login");
			}
			IMap<String, Member> memberMap = instance.getMap("Member");
			Member member = memberMap.get(sessionID);
			if (member == null) {
				return new ModelAndView("redirect:/login");
			}
			AdminMenu adminMenu = menuProcessor.getMenuList(member.getGroupID());
			List<String> mainMenu = adminMenu.getMainMenu();
			Integer unreadMessage = messageProcessor.getCountUnreadMessages(member.getUsername());
			String unread = unreadMessage != 0 ? String.valueOf(unreadMessage) : "";
			List<String> messageSummary = messageProcessor.loadUnreadMessages(member.getUsername());
			posProcessor.deletePos(ID, member.getUsername());

			model.addAttribute(member);
			model.addAttribute("notification", "success");
			model.addAttribute("title", "Delete POS");
			model.addAttribute("message", "Your POS has been deleted successfully");
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			return new ModelAndView("pos");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

}