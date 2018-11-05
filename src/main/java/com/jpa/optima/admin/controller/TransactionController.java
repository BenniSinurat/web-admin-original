package com.jpa.optima.admin.controller;

import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.bellatrix.services.ws.payments.InquiryResponse;
import org.bellatrix.services.ws.payments.PaymentResponse;
import org.bellatrix.services.ws.payments.RequestPaymentConfirmationResponse;
import org.bellatrix.services.ws.virtualaccount.CreateVAEventResponse;
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
import org.springframework.web.servlet.ModelAndView;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.jpa.optima.admin.model.AdminMenu;
import com.jpa.optima.admin.model.Group;
import com.jpa.optima.admin.model.Member;
import com.jpa.optima.admin.model.TopupMember;
import com.jpa.optima.admin.model.TransferMember;
import com.jpa.optima.admin.model.VAEvent;

@Controller
public class TransactionController {
	@Autowired
	private HazelcastInstance instance;
	@Autowired
	private MenuProcessor menuProcessor;
	@Autowired
	private MessageProcessor messageProcessor;
	@Autowired
	private ContextLoader contextLoader;
	@Autowired
	private MemberProcessor memberProcessor;
	@Autowired
	private PaymentProcessor paymentProcessor;
	@Autowired
	private GroupProcessor groupProcessor;

	@RequestMapping(value = "/topupAgent", method = RequestMethod.GET)
	public ModelAndView topupAgent(

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
		return new ModelAndView("topupAgent");
	}

	@RequestMapping(value = "/topupAgentInquiry", method = RequestMethod.POST)
	public ModelAndView topupAgentInquiry(@Valid @ModelAttribute("topupagent") TopupMember topup,

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

			topup.setTransferTypeID(contextLoader.getTopupDepositAgent());
			RequestPaymentConfirmationResponse rpcResponse = paymentProcessor.topupInquiry(topup, member.getUsername());

			if (rpcResponse.getStatus().getMessage().equalsIgnoreCase("PROCESSED")) {
				model.addAttribute(member);
				model.addAttribute("title", "Topup Agent");
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
				return new ModelAndView("topupAgentInquiry");
			} else {
				model.addAttribute(member);
				model.addAttribute("notification", "error");
				model.addAttribute("title", "Topup Agent");
				model.addAttribute("message", "Topup Agent Inquiry failed : " + rpcResponse.getStatus().getMessage());
				model.addAttribute("mainMenu", mainMenu);
				model.addAttribute("unreadMessage", unread);
				model.addAttribute("messageSummary", messageSummary);
				return new ModelAndView("topupAgent");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/topupAgentPayment", method = RequestMethod.POST)
	public ModelAndView topupAgentPayment(@Valid @ModelAttribute("topupagent") TopupMember topup,

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
				model.addAttribute("title", "Topup Agent");
				model.addAttribute("message", "Topup Agent has been successfully");
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
				return new ModelAndView("topupAgentPayment");
			} else {
				model.addAttribute(member);
				model.addAttribute("notification", "error");
				model.addAttribute("title", "Topup Agent");
				model.addAttribute("message",
						paymentResponse.getStatus().getDescription() + ": " + paymentResponse.getStatus().getMessage());
				model.addAttribute("mainMenu", mainMenu);
				model.addAttribute("unreadMessage", unread);
				model.addAttribute("messageSummary", messageSummary);
				return new ModelAndView("topupAgent");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/transferAgent", method = RequestMethod.GET)
	public ModelAndView transferAgent(

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
			return new ModelAndView("transferAgent");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/transferAgentInquiry", method = RequestMethod.POST)
	public ModelAndView transferAgentInquiry(@Valid @ModelAttribute("transferagent") TransferMember transfer,

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

			transfer.setTransferTypeID(contextLoader.getTransferToDepositAgent());
			transfer.setToMember(member.getUsername() + "-" + member.getName());
			InquiryResponse inquiryResponse = paymentProcessor.transferInquiry(transfer, member.getUsername());

			if (inquiryResponse.getStatus().getMessage().equalsIgnoreCase("PROCESSED")) {
				model.addAttribute(member);
				model.addAttribute("title", "Transfer To Agent");
				model.addAttribute("mainMenu", mainMenu);
				model.addAttribute("unreadMessage", unread);
				model.addAttribute("messageSummary", messageSummary);
				model.addAttribute("description", transfer.getDescription());
				model.addAttribute("fee", inquiryResponse.getTotalFees());
				model.addAttribute("amount", inquiryResponse.getTransactionAmount());
				model.addAttribute("totalAmount", inquiryResponse.getFinalAmount());
				model.addAttribute("toMember", inquiryResponse.getToMember().getUsername());
				model.addAttribute("status", inquiryResponse.getStatus().getMessage());
				return new ModelAndView("transferAgentInquiry");
			} else {
				model.addAttribute(member);
				model.addAttribute("notification", "error");
				model.addAttribute("title", "Transfer To Agent");
				model.addAttribute("message",
						"Transfer To Agent Inquiry failed : " + inquiryResponse.getStatus().getMessage());
				model.addAttribute("mainMenu", mainMenu);
				model.addAttribute("unreadMessage", unread);
				model.addAttribute("messageSummary", messageSummary);
				return new ModelAndView("transferAgent");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/transferAgentPayment", method = RequestMethod.POST)
	public ModelAndView transferAgentPayment(@Valid @ModelAttribute("transferagent") TransferMember transfer,

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
					contextLoader.getTransferToDepositAgent());

			if (paymentResponse.getStatus().getMessage().equalsIgnoreCase("PROCESSED")) {
				model.addAttribute(member);
				model.addAttribute("notification", "success");
				model.addAttribute("title", "Transfer To Agent");
				model.addAttribute("message", "Transfer to agent has been successfully");
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
				return new ModelAndView("transferAgentPayment");
			} else {
				model.addAttribute(member);
				model.addAttribute("notification", "error");
				model.addAttribute("title", "Transfer To Agent");
				model.addAttribute("message",
						paymentResponse.getStatus().getDescription() + ": " + paymentResponse.getStatus().getMessage());
				model.addAttribute("mainMenu", mainMenu);
				model.addAttribute("unreadMessage", unread);
				model.addAttribute("messageSummary", messageSummary);
				return new ModelAndView("transferAgent");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	// NON TRANSACTION
	@RequestMapping(value = "/group", method = RequestMethod.GET)
	public ModelAndView group(@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
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

			return new ModelAndView("group");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@ResponseBody
	@RequestMapping(value = "/groupData", method = RequestMethod.GET)
	public String groupData(@RequestParam(value = "username", required = false) String username,
			@RequestParam(value = "start") Integer start, @RequestParam(value = "length") Integer length)
			throws MalformedURLException {
		System.out.println("Group Data: "+start);
		String jsonData = groupProcessor.loadAllGroup(Integer.valueOf(start), Integer.valueOf(length));
		return jsonData;
	}

	@RequestMapping(value = "/detailGroup", method = RequestMethod.GET)
	public ModelAndView detailGroup(@Valid @RequestParam(value = "username") String username,
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
			Map<String, Object> groupDetails = groupProcessor.loadGroupsByID(ID);

			model.addAttribute(member);
			model.addAllAttributes(groupDetails);
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);

			return new ModelAndView("detailGroup");

		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/createGroup", method = RequestMethod.GET)
	public ModelAndView createGroup(Group group,
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
		model.addAttribute("group", new Group());
		model.addAttribute("mainMenu", mainMenu);
		model.addAttribute("unreadMessage", unread);
		model.addAttribute("messageSummary", messageSummary);
		return new ModelAndView("createGroup");
	}

	@RequestMapping(value = "/createGroupForm", method = RequestMethod.POST)
	public ModelAndView createGroupForm(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@Valid @ModelAttribute("group") Group group, BindingResult result, ModelMap model) {
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
			groupProcessor.createNewGroup(group);

			model.addAttribute(member);
			return new ModelAndView("redirect:/createGroupResult");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/createGroupResult", method = RequestMethod.GET)
	public ModelAndView createGroupResult(
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
			model.addAttribute("title", "Create Group");
			model.addAttribute("message", "Group has been created successfully");
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			return new ModelAndView("group");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	// NON TRANSACTION
	@RequestMapping(value = "/fee", method = RequestMethod.GET)
	public ModelAndView fee(@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
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

			return new ModelAndView("fee");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@ResponseBody
	@RequestMapping(value = "/feeData", method = RequestMethod.GET)
	public String feeData(@RequestParam(value = "username", required = false) String username,
			@RequestParam(value = "start") Integer start, @RequestParam(value = "length") Integer length)
			throws MalformedURLException {
		String jsonData = groupProcessor.loadAllGroup(Integer.valueOf(start), Integer.valueOf(length));
		return jsonData;
	}
}
