package aucklanduni.ece.hc.repository.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.springframework.stereotype.Repository;

import aucklanduni.ece.hc.repository.dao.GroupDao;
import aucklanduni.ece.hc.repository.model.Group;

@Repository
public class GroupDaoImpl extends BaseDaoImpl<Group> implements GroupDao{
	
	public void deleteGroup(Connection connection, long groupId) throws Exception {
		try
		{
			PreparedStatement ps = connection.prepareStatement(
					"DELETE "
							+ "FROM group_info"
							+ " WHERE "
							+ "GROUP_ID = ?" );
			
			ps.setLong(1, groupId);
			ps.executeUpdate();
		}
		catch(Exception e)
		{
			throw e;
		}
	}
}
