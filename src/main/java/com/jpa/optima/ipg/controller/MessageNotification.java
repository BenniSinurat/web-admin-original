package com.jpa.optima.ipg.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageNotification {

	@Autowired
	private JmsTemplate jmsTemplate;

	public void sendNotification(String paymentCode, String amount, String expiredAt, String toEmail) {
		Map<String, String> message = new HashMap<String, String>();
		String body = "Thank you for using OPTIMA, this is your payment info :\r\n\r\nPayment Code    : " + paymentCode
				+ "\r\nAmount                : " + amount + "\r\nExpired                : " + expiredAt;
		message.put("body", body);
		message.put("from", "noreply@optima.co.id");
		message.put("to", toEmail);
		message.put("subject", "OPTIMA Payment Code");
		jmsTemplate.convertAndSend("notification.email", message);
	}

	public JmsTemplate getJmsTemplate() {
		return jmsTemplate;
	}

	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

}
