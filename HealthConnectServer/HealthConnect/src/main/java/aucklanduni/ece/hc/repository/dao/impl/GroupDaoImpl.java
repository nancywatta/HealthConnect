package aucklanduni.ece.hc.repository.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import aucklanduni.ece.hc.repository.dao.GroupDao;
import aucklanduni.ece.hc.repository.model.Group;

@Repository
public class GroupDaoImpl extends BaseDaoImpl<Group> implements GroupDao{
	
	public Map<String, ArrayList<Group>> GetGroups(Connection connection, long accountId) throws Exception
	{
		Map<String, ArrayList<Group>> groups = new HashMap<String, ArrayList<Group>>();
		ArrayList<Group> groupData = new ArrayList<Group>();
		try
		{
			PreparedStatement ps = connection.prepareStatement(
					"SELECT DISTINCT g.id, g.groupname "
					+ "FROM GROUP_INFO g "
					+ "INNER JOIN MEMBER m "
					+ "ON "
					+ "g.id=m.group_id "
					+ "and m.account_id= " + accountId);
			ResultSet rs = ps.executeQuery();
			while(rs.next())
			{
				Group groupObject = new Group();
				groupObject.setId(rs.getLong("id"));
				groupObject.setGroupname(rs.getString("groupname"));
				groupData.add(groupObject);
			}
			groups.put("groups", groupData);
			return groups;
		}
		catch(Exception e)
		{
			throw e;
		}
	}
	
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
