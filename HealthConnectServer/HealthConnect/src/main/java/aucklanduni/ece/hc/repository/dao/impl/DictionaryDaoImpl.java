package aucklanduni.ece.hc.repository.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import aucklanduni.ece.hc.repository.dao.DictionaryDao;
import aucklanduni.ece.hc.repository.model.Dictionary;

@Repository
public class DictionaryDaoImpl  extends BaseDaoImpl<Dictionary> implements DictionaryDao{
	
	public ArrayList<Dictionary> GetRoles(Connection connection) throws Exception
	{
		ArrayList<Dictionary> roleData = new ArrayList<Dictionary>();
		try
		{
			PreparedStatement ps = connection.prepareStatement(
					"SELECT id, name "
					+ "FROM DICTIONARY "
					+ "WHERE type='1' ");
			ResultSet rs = ps.executeQuery();
			while(rs.next())
			{
				Dictionary roleObject = new Dictionary();
				roleObject.setId(rs.getLong("id"));
				roleObject.setName(rs.getString("name"));
				roleData.add(roleObject);
			}
			
			return roleData;
		}
		catch(Exception e)
		{
			throw e;
		}
	}
	
	public ArrayList<Dictionary> findByName(Connection connection, String roleName) throws Exception
	{
		ArrayList<Dictionary> roleData = new ArrayList<Dictionary>();
		try
		{
			PreparedStatement ps = connection.prepareStatement(
					"SELECT d.id, d.name "
					+ "FROM DICTIONARY d "
					+ "WHERE d.type='1' "
					+ "and d.name= '" + roleName +"'");
			ResultSet rs = ps.executeQuery();
			while(rs.next())
			{
				Dictionary roleObject = new Dictionary();
				roleObject.setId(rs.getLong("id"));
				roleObject.setName(rs.getString("name"));
				roleData.add(roleObject);
			}
			
			return roleData;
		}
		catch(Exception e)
		{
			throw e;
		}
	}
	
}
