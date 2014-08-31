package aucklanduni.ece.hc.service.impl;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Map;

import org.springframework.stereotype.Service;

import aucklanduni.ece.hc.repository.dao.impl.DictionaryDaoImpl;
import aucklanduni.ece.hc.repository.dao.impl.MemberDaoImpl;
import aucklanduni.ece.hc.repository.model.Database;
import aucklanduni.ece.hc.repository.model.Dictionary;
import aucklanduni.ece.hc.service.DictionaryService;

@Service
public class DictionaryServiceImpl extends BaseServiceImpl<Dictionary> implements DictionaryService{

	public ArrayList<Dictionary> GetRoles()throws Exception {
		ArrayList<Dictionary> roles = null;
		try {
			Database database= new Database();
			Connection connection = database.Get_Connection();
			DictionaryDaoImpl roleDao= new DictionaryDaoImpl();
			roles=roleDao.GetRoles(connection);
		}
		catch (Exception e) {
			throw e;
		}
		return roles;
	}
	
	public ArrayList<Dictionary> GetSpecificRoles(long accountId, long groupId) throws Exception {
		ArrayList<Dictionary> roles = new ArrayList<Dictionary>();
		try {
			Database database= new Database();
			Connection connection = database.Get_Connection();
			MemberDaoImpl memberDao= new MemberDaoImpl();
			String roleValue =memberDao.GetMemberRole(connection, accountId, groupId);
			System.out.println(roleValue);
			if(roleValue.compareTo("S")==0)
				return null;
			else if(roleValue.compareTo("P")==0) {
				roles = GetRoles();
				for(int i=0; i<roles.size(); i++) {
					Dictionary role = roles.get(i);
					if(role.getValue().compareTo(roleValue)==0)
						roles.remove(i);
				}
			}
			else if(roleValue.compareTo("N")==0) {
				DictionaryDaoImpl roleDao= new DictionaryDaoImpl();
				roles = roleDao.findByValue(connection,"P");
			}
			else {
				return GetRoles();
			}
		}
		catch (Exception e) {
			throw e;
		}
		return roles;
	}
	
}
