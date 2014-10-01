package aucklanduni.ece.hc.service;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import aucklanduni.ece.hc.repository.model.Account;
import aucklanduni.ece.hc.repository.model.Member;

@Transactional
public interface MemberService extends BaseService<Member> {
	public boolean isMember(long accountId, long groupId) throws Exception;

	public Member findByAccountIdAndGroupId(long accountId, long groupId) throws Exception;
	
	public ArrayList getGroupIdOfNurse( long accountId, long roleId) throws Exception;
	
	public String getPatientName( long groupId) throws Exception;

	public List<Account> findAllMembersInGroup(long groupId)throws Exception;
	
}
