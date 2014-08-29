package aucklanduni.ece.hc.service.impl;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Map;

import org.springframework.stereotype.Service;

import aucklanduni.ece.hc.repository.dao.impl.GroupDaoImpl;
import aucklanduni.ece.hc.repository.dao.impl.MemberDaoImpl;
import aucklanduni.ece.hc.repository.model.Account;
import aucklanduni.ece.hc.repository.model.Database;
import aucklanduni.ece.hc.repository.model.Group;
import aucklanduni.ece.hc.service.GroupService;

@Service
public class GroupServiceImpl extends BaseServiceImpl<Group> implements GroupService{

	public Map<String, ArrayList<Group>> GetGroups(long accountId)throws Exception {
		
		Map<String, ArrayList<Group>> groups = null;
		try {
			Database database= new Database();
			Connection connection = database.Get_Connection();
			GroupDaoImpl groupDao= new GroupDaoImpl();
			groups=groupDao.GetGroups(connection,accountId);
		}
		catch (Exception e) {
			throw e;
		}
		return groups;
	}
	
	public ArrayList<Account> GetMembers(long accountId,long groupId)throws Exception {
		ArrayList<Account> members = null;
		try {
			Database database= new Database();
			Connection connection = database.Get_Connection();
			MemberDaoImpl memberDao= new MemberDaoImpl();
			members=memberDao.GetMembers(connection,groupId);
		}
		catch (Exception e) {
			throw e;
		}
		return members;
	}

}
