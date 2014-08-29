package aucklanduni.ece.hc.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import aucklanduni.ece.hc.repository.model.Account;
import aucklanduni.ece.hc.repository.model.Group;

public interface GroupService extends BaseService<Group>{
	
	public Map<String, ArrayList<Group>> GetGroups(long accountId)throws Exception;
	
	public ArrayList<Account> GetMembers(long accountId,long groupId)throws Exception;
	
}
