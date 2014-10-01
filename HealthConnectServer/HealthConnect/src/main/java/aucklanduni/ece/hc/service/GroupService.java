package aucklanduni.ece.hc.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import aucklanduni.ece.hc.repository.model.Account;
import aucklanduni.ece.hc.repository.model.Group;
import aucklanduni.ece.hc.repository.model.Member;
import aucklanduni.ece.hc.webservice.model.ValidationFailException;
@Transactional
public interface GroupService extends BaseService<Group>{
	
	public ArrayList<Account> GetMembers(long accountId,long groupId)throws Exception;
	
	public List<Group> getGroupByName(String groupName)throws Exception;
	
	public List<Group> getGroupByAccId(long accountId)throws Exception;
	
	public  void inviteUser (long accountId, 
			long groupId, long roleId, String emailId) throws ValidationFailException, Exception;
	
	public  void inviteValidation (long ownerRoleId, long accountId, 
			long groupId, long roleId, String emailId)throws ValidationFailException, Exception;
	
	public void createNewGroup(long accountId, String groupName, long roleId, String members) 
			throws ValidationFailException,Exception;
	
	public  boolean deleteMemberValidation (long accountId,long groupId, long memberId)
			throws ValidationFailException, Exception;
	
	public void expireMember(long groupId, long memberId) throws ValidationFailException, Exception;
	
	public void createGroup(Group group, Account account,long roleId) throws Exception;
	
	public void saveNewMember(Member member) throws Exception;
	
	public  void deleteGroupValidation (long accountId,long groupId) 
			throws ValidationFailException, Exception;
	
	public void expireGroup(long groupId) throws Exception;

	public void expireAllMember(long groupId) throws Exception;
	
	public List<Member> getEffectiveMembers(long groupId) throws Exception;
	 
	public List<Group> findCommonGroup(long accountId, long memberId) throws Exception;
}
