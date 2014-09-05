package aucklanduni.ece.hc.service;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import aucklanduni.ece.hc.repository.model.Account;
import aucklanduni.ece.hc.repository.model.Group;
import aucklanduni.ece.hc.repository.model.Member;
@Transactional
public interface GroupService extends BaseService<Group>{
	
	public Map<String, ArrayList<Group>> GetGroups(long accountId)throws Exception;
	
	public ArrayList<Account> GetMembers(long accountId,long groupId)throws Exception;
	
	public  String inviteUserValidation (long accountId,long groupId, long roleId, String emailId)throws Exception;
	
	public void saveMember(long groupId, long accountId, String emailId, long roleId) throws Exception;
	
	public void createGroup(Group group, Account account,long roleId) throws Exception;
	
	public void saveNewMember(Member member) throws Exception;
}
