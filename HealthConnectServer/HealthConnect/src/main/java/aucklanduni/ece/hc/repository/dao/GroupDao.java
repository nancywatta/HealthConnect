package aucklanduni.ece.hc.repository.dao;

import java.sql.Connection;

import aucklanduni.ece.hc.repository.model.Group;

public interface GroupDao extends BaseDao<Group>{
	
	public void deleteGroup(Connection connection, long groupId) throws Exception;
}
