package aucklanduni.ece.hc.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aucklanduni.ece.hc.repository.dao.MemberDao;
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

}
