package aucklanduni.ece.hc.repository.dao;

import java.sql.Connection;
import java.util.ArrayList;

import aucklanduni.ece.hc.repository.model.Account;
import aucklanduni.ece.hc.repository.model.Member;

public interface MemberDao extends BaseDao<Member>{
	public ArrayList<Account> GetMembers(Connection connection, long groupId) throws Exception;
	
	public void saveMember(Connection connection, long groupId, long accountId, String emailId, long roleId) throws Exception;
	
	//Ben 09/2014
	public void deleteMember(Connection connection, long groupId, long memberId) throws Exception;
	
	public String GetMemberRole(Connection connection, long accountId, long groupId) throws Exception;
	
	
	
	public int checkPatientCount(Connection connection, long groupId) throws Exception;
	
	public void deleteAllMember(Connection connection,long groupId) throws Exception;
	
}
