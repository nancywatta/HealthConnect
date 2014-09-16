package aucklanduni.ece.hc.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aucklanduni.ece.hc.controller.RoleController;
import aucklanduni.ece.hc.service.NotifyService;
import aucklanduni.ece.hc.service.AccountService;
@Service
public class NotifyServiceImpl implements NotifyService{

	Logger log = Logger.getLogger(NotifyServiceImpl.class);
	@Autowired
	private AccountService accountservice;
	public void notify(long accountId, String message, String type)  throws Exception {
		String emailId = accountservice.getEmailByAccountId(accountId);
		log.debug("notify with this email address: " + emailId);
		}
	public void notify(String emailId, String message, String type)  throws Exception {
		log.debug("notify with this email address: " + emailId);
		}
}
