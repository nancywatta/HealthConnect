package aucklanduni.ece.hc.service.impl;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aucklanduni.ece.hc.repository.dao.AccountDao;
import aucklanduni.ece.hc.repository.dao.GroupDao;
import aucklanduni.ece.hc.repository.dao.MemberDao;
import aucklanduni.ece.hc.repository.model.Account;
import aucklanduni.ece.hc.repository.model.Database;
import aucklanduni.ece.hc.repository.model.Dictionary;
import aucklanduni.ece.hc.repository.model.Group;
import aucklanduni.ece.hc.repository.model.Member;
import aucklanduni.ece.hc.service.DictionaryService;
import aucklanduni.ece.hc.service.GroupService;
import aucklanduni.ece.hc.service.NotifyService;
import aucklanduni.ece.hc.webservice.model.ValidationFailException;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Service
public class GroupServiceImpl extends BaseServiceImpl<Group> implements GroupService{
	@Autowired
	private DictionaryService dictionaryService;
	@Autowired
	private MemberDao memberDao;
	@Autowired
	private AccountDao accountDao;
	@Autowired
	private GroupDao groupDao;
	@Autowired
	private NotifyService notifyService;

	/**
	 * Function will get all members from the MEMBER table for the given groupId
	 * It will also return the role of each member in the given Group.
	 */
	public ArrayList<Account> GetMembers(long accountId,long groupId)throws Exception {
		ArrayList<Account> members = null;
		try {
			Database database= new Database();
			Connection connection = database.Get_Connection();
			members=memberDao.GetMembers(connection,groupId);
		}
		catch (Exception e) {
			throw e;
		}
		return members;
	}
	
	/**
	 * Function will save member details in MEMBER table for the given accountId
	 * and emailId. if the invited member does not exist in database, account will be registered
	 *  and emailId and default password will be saved in the ACCOUNT table.
	 */
	public  void inviteUser (long accountId, 
			long groupId, long roleId, String emailId) throws ValidationFailException, Exception {
		long accId;
		List<Account> accounts = new ArrayList<Account>();
		try {
			accounts = accountDao.findByHql(
					"from Account a "
							+ "WHERE "
							+ "a.email='" + emailId + "'");
			if(accounts == null || accounts.size() < 1) {
				Account memberAcc = new Account();
				memberAcc.setCreateDate(new Date());
				memberAcc.setEmail(emailId);
				memberAcc.setPassword("healthConnect");
				accountDao.add(memberAcc);
				accId = memberAcc.getId();
			}
			else{
				accId = accounts.get(0).getId();
			}

			// save member details in the MEMBER table
			Member newMember = new Member();
			newMember.setAccountId(accId);
			newMember.setCreateDate(new Date());
			newMember.setGroupId(groupId);
			newMember.setRoleId(roleId);

			saveNewMember(newMember);
			
			notifyService.notify(emailId,"You have been invited to a group","yy");
			
		}catch (ValidationFailException ve) {
			throw ve;
		}catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Function will save group details in GROUP_INFO table for the given accountId
	 * The service will also save the owner role in the MEMBER table.
	 */
	public void createNewGroup(long accountId, String groupName, long roleId, String members) 
			throws ValidationFailException,Exception {
		try {
			// save group in GROUP_INFO Table
			Group newGroup = new Group();
			newGroup.setGroupname(groupName);
			newGroup.setCreateDate(new Date());
			Account account = accountDao.findById(accountId);
			createGroup(newGroup, account, roleId);

			if(members !=null) {
				List<Account> accList = 
						new Gson().fromJson(members, new TypeToken<List<Account>>() {}.getType());

				for(Account  member : accList) {
					// Perform Member Validations
					inviteValidation(roleId, accountId, 
							newGroup.getId(), member.getRole().getId(), member.getEmail());

					List<Account> memberAccs = new ArrayList<Account>();
					memberAccs = accountDao.findByHql(
							"from Account a "
									+ "WHERE "
									+ "a.email='" + member.getEmail() + "'");
					long memberAccId;

					// if invited account does not exist, create the account with default password
					if(memberAccs.size() < 1) {
						Account memberAcc = new Account();
						memberAcc.setCreateDate(new Date());
						memberAcc.setEmail(member.getEmail());
						memberAcc.setPassword("healthConnect");
						accountDao.add(memberAcc);
						memberAccId = memberAcc.getId();
					}
					else {
						memberAccId = memberAccs.get(0).getId();
					}

					// save member details in the MEMBER table
					Member newMember = new Member();
					newMember.setAccountId(memberAccId);
					newMember.setCreateDate(new Date());
					newMember.setGroupId(newGroup.getId());
					newMember.setRoleId(member.getRole().getId());

					saveNewMember(newMember);
				}
			}
		}catch (ValidationFailException ve) {
			throw ve;
		}catch (Exception e) {
			throw e;
		}

	}

	/**
	 * Function will perform business validations.
	 * 1. Nurse can only invite Patient.
	 * 2. Support Member can neither create the group nor invite anyone to the group.
	 * 3. Patient can invite Nurse and Support Member to the group.
	 * 4. A group can have only one Patient.
	 */
	public  void inviteValidation (long ownerRoleId, long accountId, 
			long groupId, long roleId, String emailId) throws ValidationFailException, Exception {
		try {

			// Fetch Role of Owner of Group
			Dictionary ownerRole = dictionaryService.findById(new Long(ownerRoleId));

			String roleValue = ownerRole.getValue();

			// Support Member cannot invite any user in Group
			if(roleValue.compareTo("S")==0)
				throw new ValidationFailException("Support Member cannot Invite Member to Group");

			// Fetch Role of user being Invited
			Dictionary role = dictionaryService.findById(new Long(roleId));


			if(roleValue.compareTo("P")==0) {

				// Patient cannot invite any other patient in Group
				if(roleValue.compareTo(role.getValue())==0)
					throw new ValidationFailException("Only one patient allowed per group");
			} else if(roleValue.compareTo("N")==0) {

				// Nurse can only invite patient in Group
				if(role.getValue().compareTo("P")!=0)
					throw new ValidationFailException("Nurse can Invite only Patient to Group");

				// Check if Patient already exists in Group
				List<Member> members = new ArrayList<Member>();
				members = memberDao.findByHql("select m from Member m, Dictionary d "
						+ "WHERE "
						+ "m.roleId=d.id "
						+ "and m.groupId= " + groupId
						+ " and d.type = 'Role' "
						+ "and d.value = 'P' ");

				// If patient already exists, Nurse cannot invite any more patient
				if(members.size() >= 1)
					throw new ValidationFailException("Only one patient allowed per group");
			}

			// Check if the invited member is already registered user
			List<Account> accounts = new ArrayList<Account>();
			accounts = accountDao.findByHql(
					"from Account a "
							+ "WHERE "
							+ "a.email='" + emailId + "'");

			if(accounts.size() >=1) {

				// Check if the invited member is already member of the input Group
				List<Dictionary> roles = new ArrayList<Dictionary>();
				roles = dictionaryService.findByHql("select d from Dictionary d, Member m "
						+ "WHERE "
						+ "m.roleId=d.id "
						+ "and m.groupId= " + groupId
						+ " and m.accountId= " + accounts.get(0).getId());

				// throw error if invited member is already member of the input Group
				if(roles.size() >= 1)
					throw new ValidationFailException(emailId + " is already a member in this Group");
			}
		}
		catch (Exception e) {
			throw e;
		}

	}
	
	/**
	 * Function will perform business validations.
	 * 1. Patient can delete nurse and support member.
	 * 2. Patient cannot delete itself.
	 * 3. Nurse can delete the patient.
	 * 4. Nurse or Support Member can delete itself.
	 * 5. Nurse or Support Member cannot delete another nurse or support member.
	 */ 
	public  boolean deleteMemberValidation (long accountId,long groupId, long memberId)throws Exception {
		try {
			Database database= new Database();
			Connection connection = database.Get_Connection();
			
			String userRole = memberDao.GetMemberRole(connection, accountId, groupId);
			String memberRole = memberDao.GetMemberRole(connection, memberId, groupId);
			
			// Check if both member are valid
			if(userRole== "" || memberRole == "")
				throw new ValidationFailException("The user/member is invalid");
			
			// The current user is Patient
			if(userRole.compareTo("P")==0) {
				
				//Patient cannot delete itself.
				if(accountId == memberId)
					throw new ValidationFailException("Cannot delete the patient in the group, please delete the group");
				
				else return true;
			} 

			// The current user is Nurse
			else if(userRole.compareTo("N")==0) {
				
				//Nurse can delete itself.
				if(accountId == memberId)
					return true;
				//Nurse cannot delete support member.
				else if(memberRole.compareTo("S")==0)
					throw new ValidationFailException("Nurse cannot delete the support member");
				//Nurse cannot delete another nurse.
				else if(memberRole.compareTo("N")==0)
					throw new ValidationFailException("Nurse cannot delete another nurse");
				
				else return true;
			}
			
			// The current user is Support Member
			else if(userRole.compareTo("S")==0)
				
				//Support Member can only delete itself
				if(accountId == memberId)
					return true;
				else throw new ValidationFailException("Support Member can only delete itself");
			
			return false;
			
		}
		catch (Exception e) {
			throw e;
		}
	}
	
	
	
	/**
	 * Function will save a member into a specific group
	 */
	public void saveMember(long groupId, long accountId, String emailId, long roleId) throws Exception {
		try {
			Database database= new Database();
			Connection connection = database.Get_Connection();
			memberDao.saveMember(connection,groupId,accountId,emailId,roleId);
		}
		catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * Function will delete a specific member from a specific group
	 */
	public void deleteMember(long groupId, long memberId) throws Exception {
		try {
			Database database= new Database();
			Connection connection = database.Get_Connection();
			memberDao.deleteMember(connection,groupId,memberId);
		}
		catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Function will save group details in GROUP_INFO table
	 * it will also save the owner details in the MEMBER table
	 * for the given group.
	 */
	public void createGroup(Group group, Account account,long roleId) throws Exception {
		// create group
		groupDao.add(group);
		
		//add default memeber
		Member owner = new Member();
		owner.setAccountId(account.getId());
		owner.setGroupId(group.getId());
		owner.setRoleId(roleId);
		owner.setCreateDate(new Date());
		memberDao.add(owner);
	}
	
	/**
	 * Function will save member details in MEMBER table
	 */
	public void saveNewMember(Member member) throws Exception {
		memberDao.add(member);
	}
	
	public  void deleteGroupValidation (long accountId,long groupId) 
			throws ValidationFailException, Exception {
		try {
			Database database= new Database();
			Connection connection = database.Get_Connection();
			
			String userRole = memberDao.GetMemberRole(connection, accountId, groupId);
			if(userRole != ""){
				if(userRole.compareTo("S")==0){
					throw new ValidationFailException("Support Member cannot delete the Group");
				} else if(userRole.compareTo("N")==0){
					ArrayList<Account> members = null;
					members=memberDao.GetMembers(connection,groupId);
					if(members.size() > 1){
						throw new ValidationFailException("Nurse can only delete empty group!");
					} 
				}
			} else{
				throw new ValidationFailException("Invalid action");
			}
		} catch (Exception e) {
			throw e;
		} 
		
	}
	
	public void deleteGroup(long groupId) throws Exception {
		//delete group
		try {
			Database database= new Database();
			Connection connection = database.Get_Connection();
			groupDao.deleteGroup(connection, groupId);
		}
		catch (Exception e) {
			throw e;
		}
		
	}

	public void deleteAllMember(long groupId) throws Exception {
		//delete group
				try {
					Database database= new Database();
					Connection connection = database.Get_Connection();
					memberDao.deleteAllMember(connection,groupId);
				}
				catch (Exception e) {
					throw e;
				}
	}


}
