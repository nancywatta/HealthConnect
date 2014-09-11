package aucklanduni.ece.hc.repository.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.springframework.stereotype.Repository;

import aucklanduni.ece.hc.repository.dao.DictionaryDao;
import aucklanduni.ece.hc.repository.model.Dictionary;

@Repository
public class DictionaryDaoImpl  extends BaseDaoImpl<Dictionary> implements DictionaryDao{
	
	public ArrayList<Dictionary> findByValue(Connection connection, String roleValue) throws Exception
	{
		ArrayList<Dictionary> roleData = new ArrayList<Dictionary>();
		try
		{
			PreparedStatement ps = connection.prepareStatement(
					"SELECT d.id, d.value, d.name "
					+ "FROM DICTIONARY d "
					+ "WHERE d.type='Role' "
					+ "and d.value= '" + roleValue +"'");
			ResultSet rs = ps.executeQuery();
			while(rs.next())
			{
				Dictionary roleObject = new Dictionary();
				roleObject.setId(rs.getLong("id"));
				roleObject.setValue(rs.getString("value"));
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
