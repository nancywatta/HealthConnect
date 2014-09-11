package aucklanduni.ece.hc.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aucklanduni.ece.hc.repository.dao.DictionaryDao;
import aucklanduni.ece.hc.repository.model.Dictionary;
import aucklanduni.ece.hc.service.DictionaryService;

@Service
public class DictionaryServiceImpl extends BaseServiceImpl<Dictionary> implements DictionaryService{

	@Autowired
	private DictionaryDao dictionaryDao;

	public ArrayList<Dictionary> GetSpecificRoles(long accountId, long groupId) throws Exception {
		List<Dictionary> roles = new ArrayList<Dictionary>();
		try {

			List<Dictionary> ownerRoles = new ArrayList<Dictionary>();
			ownerRoles = dictionaryDao.findByHql(
					"select d from Dictionary d, Member m "
							+ "WHERE "
							+ "m.roleId=d.id "
							+ "and m.groupId= " + groupId
							+ " and m.accountId= " + accountId);

			String roleValue = ownerRoles.get(0).getValue();

			if(roleValue.compareTo("S")==0)
				return null;
			else if(roleValue.compareTo("P")==0) {
				roles = dictionaryDao.findByHql(
						"from Dictionary "
								+ "WHERE type='Role' " 
								+ "and value!='P'");
			}
			else if(roleValue.compareTo("N")==0) {
				roles = dictionaryDao.findByHql(
						"from Dictionary "
								+ "WHERE type='Role' " 
								+ "and value='P'");
			}
			else {
				roles = dictionaryDao.findByHql(
						"from Dictionary "
								+ "WHERE type='Role' ");
			}
		}
		catch (Exception e) {
			throw e;
		}
		return (ArrayList<Dictionary>)roles;
	}

}
