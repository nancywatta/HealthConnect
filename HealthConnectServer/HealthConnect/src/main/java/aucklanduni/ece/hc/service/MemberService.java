package aucklanduni.ece.hc.service;

import org.springframework.transaction.annotation.Transactional;

import aucklanduni.ece.hc.repository.model.Member;

@Transactional
public interface MemberService extends BaseService<Member> {
	public boolean isMember(long accountId, long groupId) throws Exception;

}
