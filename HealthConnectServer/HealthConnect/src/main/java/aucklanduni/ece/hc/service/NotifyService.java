package aucklanduni.ece.hc.service;

import org.springframework.transaction.annotation.Transactional;



@Transactional
public interface NotifyService {
	
	
	
	public void notify(String emailId, String message, String type)throws Exception;
	

}
