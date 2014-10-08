package aucklanduni.ece.hc.service.impl;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aucklanduni.ece.hc.repository.dao.MemberDao;
import aucklanduni.ece.hc.repository.model.Account;
import aucklanduni.ece.hc.repository.model.Database;
import aucklanduni.ece.hc.repository.model.Member;
import aucklanduni.ece.hc.service.MemberService;

@Service
public class MemberServiceImpl extends BaseServiceImpl<Member> implements
		MemberService {
	@Autowired
	private MemberDao memberDao;
	
	/**
	 * @Title: isMember
	 * @Description: Function will check if the input accountId is
	 * member of the given groupId.
	 * 
	 * @param accountId
	 * @param groupId
	 * @return boolean
	 * @throws Exception
	 */
	public boolean isMember(long accountId, long groupId) throws Exception{
		return memberDao.isMember(accountId,groupId);
	}
	
	/**
	 * @Title: findByAccountIdAndGroupId
	 * @Description: Function will return member details
	 * based on given groupId and accountId.
	 * 
	 * @param accountId
	 * @param groupId
	 * @return Member
	 * @throws Exception
	 */
	public Member findByAccountIdAndGroupId(long accountId, long groupId)
			throws Exception {
		return memberDao.findByAccountIdAndGroupId(accountId,groupId);
	}
	
	/**
	 * @Title: getGroupIdOfNurse
	 * @Description: Function will return all groups in which the input accountId
	 * has the role of Nurse.
	 * 
	 * @param accountId
	 * @param roleId
	 * @return ArrayList<Long>
	 * @throws Exception
	 */
	public ArrayList getGroupIdOfNurse(long accountId, long roleId) throws Exception {
		
		try {
			Database database= new Database();
			Connection connection = database.Get_Connection();

			
			return memberDao.getGroupIdOfNurse(connection, accountId, roleId);
		}
		catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * @Title: getPatientName
	 * @Description: Function will userName of the patient within the
	 * the given groupId.
	 * 
	 * @param groupId
	 * @return String
	 * @throws Exception
	 */
	public String getPatientName(long groupId) throws Exception {
		try {
			Database database= new Database();
			Connection connection = database.Get_Connection();

			
			return memberDao.getPatientName(connection, groupId);
		}
		catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * @Title: findAllMembersInGroup
	 * @Description: Function will return account details from ACCOUNT table
	 * of all members of the given groupId.
	 * 
	 * @param groupId
	 * @return List<Account>
	 * @throws Exception
	 */
	public List<Account> findAllMembersInGroup(long groupId) throws Exception {
		return memberDao.findAllMembersInGroup(groupId);
	}
}
