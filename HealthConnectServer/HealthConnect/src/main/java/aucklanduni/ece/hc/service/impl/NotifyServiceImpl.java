package aucklanduni.ece.hc.service.impl;

import org.springframework.stereotype.Service;

import aucklanduni.ece.hc.service.NotifyService;
@Service
public class NotifyServiceImpl implements NotifyService{

	@Override
	public void notify(String emailId, String message, String type)
			throws Exception {
		System.out.println("notify with this email address: " + emailId);
		
	}

	
		
	
}
