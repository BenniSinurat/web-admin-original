package com.jpa.optima.ipg.controller;

import javax.validation.Valid;

import org.bellatrix.services.ws.virtualaccount.LoadVAByIDResponse;
import org.bellatrix.services.ws.virtualaccount.LoadVAEventResponse;
import org.bellatrix.services.ws.virtualaccount.VaRegisterResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.jpa.optima.ipg.model.Transfer;

@Controller
public class IPGController {

	@Autowired
	private PaymentPageProcessor paymentPageProcessor;

	@RequestMapping(value = "/paymentPage", method = RequestMethod.GET)
	public String login(@RequestParam(value = "ticketID", required = false) String ticketID, Model model) {
		if (ticketID == null) {
			return "page_404";
		}
		
		try {
			LoadVAEventResponse loadVAEventResponse = paymentPageProcessor.loadVAEvent(ticketID);

			if (loadVAEventResponse.getEvent().size() == 0) {
				return "page_404";
			}

			model.addAttribute("transfer", new Transfer());
			model.addAttribute("eventName", loadVAEventResponse.getEvent().get(0).getEventName());
			model.addAttribute("description", loadVAEventResponse.getEvent().get(0).getDescription());
			model.addAttribute("amount", loadVAEventResponse.getEvent().get(0).getFormattedAmount());
			model.addAttribute("ticketID", loadVAEventResponse.getEvent().get(0).getTicketID());
			return "paymentPage";
		} catch (Exception ex) {
			return "page_500";
		}

	}

	@RequestMapping(value = "/submitTransferForm", method = RequestMethod.POST)
	public String submitTransferForm(@Valid @ModelAttribute("transfer") Transfer transfer, BindingResult result,
			ModelMap model) {
		try {
			if (result.hasErrors()) {
				return "page_500";
			}

			LoadVAEventResponse loadVAEventResponse = paymentPageProcessor.loadVAEvent(transfer.getTicketID());
			if (loadVAEventResponse.getEvent().size() == 0) {
				return "page_404";
			}

			VaRegisterResponse vaRegisterResponse = paymentPageProcessor.registerVABilling(loadVAEventResponse,
					transfer.getName(), transfer.getMsisdn(), transfer.getEmail());

			System.out.println("TICKET ID: "+vaRegisterResponse.getTicketID());
			model.addAttribute("ticketID", transfer.getTicketID());
			model.addAttribute("regID", vaRegisterResponse.getTicketID());
			return "redirect:/transferPayment";

		} catch (Exception ex) {
			ex.printStackTrace();
			return "page_500";
		}

	}

	@RequestMapping(value = "/transferPayment", method = RequestMethod.GET)
	public String transferFormRedirection(ModelMap model,
			@RequestParam(value = "ticketID", required = false) String ticketID,
			@RequestParam(value = "regID", required = false) String regID) {
		try {

			LoadVAEventResponse loadVAEventResponse = paymentPageProcessor.loadVAEvent(ticketID);
			if (loadVAEventResponse.getEvent().size() == 0) {
				return "page_404";
			}

			LoadVAByIDResponse loadVAByIDResponse = paymentPageProcessor.loadVAByID(regID);
			if (loadVAEventResponse.getEvent().size() == 0) {
				return "page_404";
			}
			
			model.addAttribute("paymentCode", loadVAByIDResponse.getVaRecord().get(0).getId());
			model.addAttribute("eventName", loadVAEventResponse.getEvent().get(0).getEventName());
			model.addAttribute("description", loadVAEventResponse.getEvent().get(0).getDescription());
			model.addAttribute("amount", loadVAEventResponse.getEvent().get(0).getFormattedAmount());
			model.addAttribute("expiredAt", loadVAEventResponse.getEvent().get(0).getFormattedExpiredAt());

			return "transferPayment";

		} catch (Exception ex) {
			ex.printStackTrace();
			return "page_500";
		}

	}
}
