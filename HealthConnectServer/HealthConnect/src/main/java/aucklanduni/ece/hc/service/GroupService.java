package aucklanduni.ece.hc.service;

import java.util.ArrayList;

import org.springframework.transaction.annotation.Transactional;

import aucklanduni.ece.hc.repository.model.Account;
import aucklanduni.ece.hc.repository.model.Group;
import aucklanduni.ece.hc.repository.model.Member;
import aucklanduni.ece.hc.webservice.model.ValidationFailException;
@Transactional
public interface GroupService extends BaseService<Group>{
	
	public ArrayList<Account> GetMembers(long accountId,long groupId)throws Exception;
	
	public  void inviteUser (long accountId, 
			long groupId, long roleId, String emailId) throws ValidationFailException, Exception;
	
	public  void inviteValidation (long ownerRoleId, long accountId, 
			long groupId, long roleId, String emailId)throws ValidationFailException, Exception;
	
	public void createNewGroup(long accountId, String groupName, long roleId, String members) 
			throws ValidationFailException,Exception;
	
	//Ben 09/2014
	public  String deleteMemberValidation (long accountId,long groupId, long memberId)throws Exception;
	public void deleteMember(long groupId, long memberId) throws Exception;
	
	public void createGroup(Group group, Account account,long roleId) throws Exception;
	
	public void saveNewMember(Member member) throws Exception;
	
	public void deleteGroup(long groupId) throws Exception;
	 
}
