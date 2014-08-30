package aucklanduni.ece.hc.service.impl;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aucklanduni.ece.hc.repository.dao.AccountDao;
import aucklanduni.ece.hc.repository.dao.MemberDao;
import aucklanduni.ece.hc.repository.dao.impl.GroupDaoImpl;
import aucklanduni.ece.hc.repository.model.Account;
import aucklanduni.ece.hc.repository.model.Database;
import aucklanduni.ece.hc.repository.model.Dictionary;
import aucklanduni.ece.hc.repository.model.Group;
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

}
