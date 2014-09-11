package aucklanduni.ece.hc.repository.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.springframework.stereotype.Repository;

import aucklanduni.ece.hc.repository.dao.GroupDao;
import aucklanduni.ece.hc.repository.model.Group;

@Repository
public class GroupDaoImpl extends BaseDaoImpl<Group> implements GroupDao{
	
	public void deleteGroup(Connection connection, long accountId, long groupId) throws Exception {
		try{
			//delete members in that group
			PreparedStatement ps1 = connection.prepareStatement(
					"DELETE  FROM member "
					+ "WHERE account_id =" + accountId 
					+ "AND group_id = " + groupId );
			ResultSet rs1 = ps1.executeQuery();
			//delete the group
			PreparedStatement ps2 = connection.prepareStatement(
					"DELETE FROM group_info"
					+ "WHERE id = " + groupId);
			ResultSet rs2 = ps2.executeQuery();
		}
		catch(Exception e)
		{
			throw e;
		}
	}
}
