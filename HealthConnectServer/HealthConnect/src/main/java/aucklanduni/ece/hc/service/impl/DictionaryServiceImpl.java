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

	/**
	 * For the given groupId and accountId, the function will return 
	 * specific roles based on business validation. 
	 * 1. For Nurse, only Patient Role will be returned.
	 * 2. For Support Member, no roles will be returned.
	 * 3. For Patient, only Nurse and Support Member role will be returned.
	 */
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

			// Support Member cannot invite anyone, thus no role will be returned
			if(roleValue.compareTo("S")==0)
				return null;
			/* For Patient, all roles except Patient will be returned, since 
			since a group can have only one patient */
			else if(roleValue.compareTo("P")==0) {
				roles = dictionaryDao.findByHql(
						"from Dictionary "
								+ "WHERE type='Role' " 
								+ "and value!='P'");
			}
			// For Nurse, only Patient role will be returned
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

	public Dictionary findRoleByAccountIdAndGroupId(long accountId, long groupId)
			throws Exception {
		
		return dictionaryDao.findRoleByAccountIdAndGroupId(accountId, groupId);
	}

}
