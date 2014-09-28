package aucklanduni.ece.hc.repository.dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import aucklanduni.ece.hc.repository.model.Account;
import aucklanduni.ece.hc.repository.model.Member;

public interface MemberDao extends BaseDao<Member>{
	public ArrayList<Account> GetMembers(Connection connection, long groupId) throws Exception;
	
	public void saveMember(Connection connection, long groupId, long accountId, String emailId, long roleId) throws Exception;
	
	public void deleteMember(Connection connection, long groupId, long memberId) throws Exception;
	
	public String GetMemberRole(Connection connection, long accountId, long groupId) throws Exception;
	
	public int checkPatientCount(Connection connection, long groupId) throws Exception;

	public int checkSupportMemberCount(Connection connection, long groupId) throws Exception;
	
	public int checkMemberCount(Connection connection, long groupId) throws Exception;
	
	public void deleteAllMember(Connection connection,long groupId) throws Exception;

	public boolean isMember(long accountId, long groupId) throws Exception;

	public Member findByAccountIdAndGroupId(long accountId, long groupId) throws Exception;
	
	public String getPatientName(Connection connection, long groupId) throws Exception;
	
	public ArrayList getGroupIdOfNurse(Connection connection, long accountId, long roleId) throws Exception;

	public List<Account> findAllMembersInGroup(long groupId) throws Exception;
}
