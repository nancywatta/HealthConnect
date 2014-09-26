package aucklanduni.ece.hc.service.impl;

import java.io.Serializable;
import java.sql.Connection;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aucklanduni.ece.hc.repository.dao.MemberDao;
import aucklanduni.ece.hc.repository.model.Database;
import aucklanduni.ece.hc.repository.model.Member;
import aucklanduni.ece.hc.service.MemberService;

@Service
public class MemberServiceImpl extends BaseServiceImpl<Member> implements
		MemberService {
	@Autowired
	private MemberDao memberDao;
	public boolean isMember(long accountId, long groupId) throws Exception{
		return memberDao.isMember(accountId,groupId);
	}
	public Member findByAccountIdAndGroupId(long accountId, long groupId)
			throws Exception {
		return memberDao.findByAccountIdAndGroupId(accountId,groupId);
	}
	
	
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
	
	

}
