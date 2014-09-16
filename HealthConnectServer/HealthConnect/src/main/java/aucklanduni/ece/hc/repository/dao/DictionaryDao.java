package aucklanduni.ece.hc.repository.dao;

import aucklanduni.ece.hc.repository.model.Dictionary;

public interface DictionaryDao  extends BaseDao<Dictionary> {

	Dictionary findRoleByAccountIdAndGroupId(long accountId, long groupId) throws Exception;

}
