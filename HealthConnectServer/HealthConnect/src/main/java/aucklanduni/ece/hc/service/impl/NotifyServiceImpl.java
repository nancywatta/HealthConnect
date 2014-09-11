package aucklanduni.ece.hc.service.impl;

import org.springframework.stereotype.Service;

import aucklanduni.ece.hc.service.NotifyService;
import aucklanduni.ece.hc.service.AccountService;
@Service
public class NotifyServiceImpl implements NotifyService{
	private AccountService accountservice;
	public void notify(long accountId, String message, String type)  throws Exception {
		String emailId = accountservice.getEmailByAccountId(accountId);
		System.out.println("notify with this email address: " + emailId);
		}
	public void notify(String emailId, String message, String type)  throws Exception {
		System.out.println("notify with this email address: " + emailId);
		}
}
