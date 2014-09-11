package aucklanduni.ece.hc.service.impl;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aucklanduni.ece.hc.repository.dao.AccountDao;
import aucklanduni.ece.hc.repository.dao.GroupDao;
import aucklanduni.ece.hc.repository.dao.MemberDao;
import aucklanduni.ece.hc.repository.dao.impl.GroupDaoImpl;
import aucklanduni.ece.hc.repository.model.Account;
import aucklanduni.ece.hc.repository.model.Database;
import aucklanduni.ece.hc.repository.model.Dictionary;
import aucklanduni.ece.hc.repository.model.Group;
import aucklanduni.ece.hc.repository.model.Member;
import aucklanduni.ece.hc.service.DictionaryService;
import aucklanduni.ece.hc.service.GroupService;

@Service
public class GroupServiceImpl extends BaseServiceImpl<Group> implements GroupService{
	@Autowired
	private DictionaryService dictionaryService;
	@Autowired
	private MemberDao memberDao;
	@Autowired
	private AccountDao accountDao;
	@Autowired
	private GroupDao groupDao;

	public Map<String, ArrayList<Group>> GetGroups(long accountId)throws Exception {

		Map<String, ArrayList<Group>> groups = null;
		try {
			Database database= new Database();
			Connection connection = database.Get_Connection();
			GroupDaoImpl groupDao= new GroupDaoImpl();
			groups=groupDao.GetGroups(connection,accountId);
		}
		catch (Exception e) {
			throw e;
		}
		return groups;
	}

	public ArrayList<Account> GetMembers(long accountId,long groupId)throws Exception {
		ArrayList<Account> members = null;
		try {
			Database database= new Database();
			Connection connection = database.Get_Connection();
			members=memberDao.GetMembers(connection,groupId);
		}
		catch (Exception e) {
			throw e;
		}
		return members;
	}

	public  String inviteUserValidation (long accountId,long groupId, long roleId, String emailId)throws Exception {
		try {
			Database database= new Database();
			Connection connection = database.Get_Connection();
			String roleValue = memberDao.GetMemberRole(connection, accountId, groupId);
			if(roleValue.compareTo("S")==0)
				return "Action Not Allowed";

			Dictionary role = dictionaryService.findById(new Long(roleId));

			if(roleValue.compareTo("P")==0) {

				if(roleValue.compareTo(role.getValue())==0)
					return "Only one patient allowed per group";
			} else if(roleValue.compareTo("N")==0) {
				if(role.getValue().compareTo("P")!=0)
					return "Action Not Allowed";
				if(memberDao.checkPatientCount(connection,groupId) >= 1)
					return "Only one patient allowed per group";
			}
			
			Account account = accountDao.getAccountByEmail(connection, emailId);
			if(account!=null) {
				roleValue = "";
				roleValue = memberDao.GetMemberRole(connection, account.getId(), groupId);
				if(roleValue.compareTo("")!=0)
					return "Already a member in this Group";
			}
				
			return "Succes";
		}
		catch (Exception e) {
			throw e;
		}
	}
	
	
	//Ben 09/2014 TODO:maybe some problem like inviteValidation
	public  String deleteMemberValidation (long accountId,long groupId, long memberId)throws Exception {
		try {
			Database database= new Database();
			Connection connection = database.Get_Connection();
			
			String userRole = memberDao.GetMemberRole(connection, accountId, groupId);
			String memberRole = memberDao.GetMemberRole(connection, memberId, groupId);
			
			
			if(userRole.compareTo("P")==0) {
				if(userRole.compareTo(memberRole)==0)
					return "Cannot delete the patient in the group, please delete the group";
				else return "Succes";
			} 
			//delete the user himself
			else if (accountId == memberId)
				return "Succes";
			else if(userRole.compareTo("N")==0) {
				//nurse can delete member only there is no patient in the group
//				if(memberDao.checkPatientCount(connection,groupId) != 0)
//					return "Only the patient can delete the member";
				if(memberRole.compareTo("S")==0)
					return "Nurse cannot delete the support member";
//				else if(memberRole.compareTo("N")==0)
//					return "Nurse cannot delete another nurse";
				else return "Succes";
			}
			else if(userRole.compareTo("S")==0)
				return "Action Not Allowed";
			
			return "The user is invalid";
			
		}
		catch (Exception e) {
			throw e;
		}
	}
	
	
	public void saveMember(long groupId, long accountId, String emailId, long roleId) throws Exception {
		try {
			Database database= new Database();
			Connection connection = database.Get_Connection();
			memberDao.saveMember(connection,groupId,accountId,emailId,roleId);
		}
		catch (Exception e) {
			throw e;
		}
	}
	
	//Ben 09/2014
	public void deleteMember(long groupId, long memberId) throws Exception {
		try {
			Database database= new Database();
			Connection connection = database.Get_Connection();
			memberDao.deleteMember(connection,groupId,memberId);
		}
		catch (Exception e) {
			throw e;
		}
	}

	public void createGroup(Group group, Account account,long roleId) throws Exception {
		// create group
		groupDao.add(group);
		
		//add default memeber
		Member owner = new Member();
		owner.setAccountId(account.getId());
		owner.setGroupId(group.getId());
		owner.setRoleId(roleId);
		owner.setCreateDate(new Date());
		memberDao.add(owner);
	}
	
	public void saveNewMember(Member member) throws Exception {
		memberDao.add(member);
	}
	
	
	public String deleteGroup(long accountId, long groupId) throws Exception {
		
		try {
			Database database= new Database();
			Connection connection = database.Get_Connection();
			String roleValue = memberDao.GetMemberRole(connection, accountId, groupId);
			if(roleValue.compareTo("N")==0){
				ArrayList<Account> members = memberDao.GetMembers(connection,groupId);
				if(members == null){
					GroupDaoImpl groupDao= new GroupDaoImpl();
					groupDao.deleteGroup(connection, accountId, groupId);
					return "succeed!";
				} else {
					return "action not allowed!";
				}
			}
			if(roleValue.compareTo("S")==0){
				GroupDaoImpl groupDao= new GroupDaoImpl();
				groupDao.deleteGroup(connection, accountId, groupId);
				return "succeed!";
			}
		}
		catch (Exception e) {
			throw e;
		}
		return null;
		
		
	}
}
