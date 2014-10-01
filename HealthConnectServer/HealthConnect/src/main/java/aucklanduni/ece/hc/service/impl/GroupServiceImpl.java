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
import aucklanduni.ece.hc.service.AccountService;
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
	private AccountService accountService;
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
	 * Function will get return group details based on groupName.
	 */
	public List<Group> getGroupByName(String groupName)throws Exception {
		try {
			List<Group> groups = new ArrayList<Group>();
			groups = groupDao.findByHql("from Group WHERE groupname='" + groupName + "'");
			return groups;
		}
		catch (Exception e) {
			throw e;
		}
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
			accounts = accountService.getAccountbyEmail(emailId);
					
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
			newMember.setIsActive("Y");

			saveNewMember(newMember);
			
			//update column updated_date of GROUP_INFO table
			Group group = groupDao.findById(groupId);
			group.setUpdatedDate(new Date());
			groupDao.update(group);
			
			notifyService.notify(emailId,"You have been invited to a group","email");
			
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
					memberAccs = accountService.getAccountbyEmail(member.getEmail());
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
					newMember.setIsActive("Y");

					saveNewMember(newMember);
					
					notifyService.notify(member.getEmail(), "You have been invited to the group", "email");
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
				long time = System.currentTimeMillis();
				java.sql.Timestamp timestamp = new java.sql.Timestamp(time);
				List<Member> members = new ArrayList<Member>();
				members = memberDao.findByHql("select m from Member m, Dictionary d "
						+ "WHERE "
						+ "m.roleId=d.id "
						+ "and m.groupId= " + groupId
						+ " and d.type = 'Role' "
						+ "and d.value = 'P' "
						+ "and ( m.expirationDate IS NULL "
						+ "or m.expirationDate > '" 
						+ timestamp + "')");

				// If patient already exists, Nurse cannot invite any more patient
				if(members.size() >= 1)
					throw new ValidationFailException("Only one patient allowed per group");
			}

			// Check if the invited member is already registered user
			List<Account> accounts = new ArrayList<Account>();
			accounts = accountService.getAccountbyEmail(emailId); 

			if(accounts.size() >=1) {

				// Check if the invited member is already member of the input Group
				List<Dictionary> roles = new ArrayList<Dictionary>();
				roles = dictionaryService.getRolesByGroupIdAccId(accounts.get(0).getId(),groupId );

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
	 * Function will get return all groups of input accountId.
	 */
	public List<Group> getGroupByAccId(long accountId)throws Exception {
		List<Group> groupList = new ArrayList<Group>();
		try {
			groupList = groupDao.findByHql("select distinct g from Group g, "
					+ "Member m "
					+ "WHERE "
					+ "g.id=m.groupId "
					+ "and m.accountId= " + accountId);
			return groupList;
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
	public  boolean deleteMemberValidation (long accountId,long groupId, long memberId) 
			throws ValidationFailException, Exception {
		try {
			Database database= new Database();
			Connection connection = database.Get_Connection();
			
			List<Dictionary> roles = new ArrayList<Dictionary>();
			roles = dictionaryService.getRolesByGroupIdAccId(accountId, groupId);

			if(roles == null || roles.size() < 1) {
				throw new ValidationFailException("Invalid Inputs");
			}
			
			List<Dictionary> memberRoles = new ArrayList<Dictionary>();
			memberRoles = dictionaryService.getRolesByGroupIdAccId(memberId, groupId);

			if(memberRoles == null || memberRoles.size() < 1) {
				throw new ValidationFailException("Invalid Inputs");
			}
			
			String userRole = roles.get(0).getValue();
			String memberRole = memberRoles.get(0).getValue();
			
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
				if(accountId == memberId) {
					return true;
				}
				//Nurse can delete patient only when there is not any support member in the group
				else if(memberRole.compareTo("P")==0) {
					if(memberDao.checkSupportMemberCount(connection, groupId) != 0)
						throw new ValidationFailException("Nurse can delete patient only when there is not any support member in the group");
					else {
						List<Member> nurses = new ArrayList<Member>();
						nurses = memberDao.findByHql("select m from Member m, Dictionary d WHERE "
								+ "m.groupId=" + groupId
								+ "and m.isActive='Y' "
								+ "and m.roleId=d.id "
								+ "and d.value='N' "
								+ "and d.type='Role'");
						if(nurses.size() > 1)
							throw new ValidationFailException("Nurse can delete patient only when there is no other member in the group");
						else
							return true;
					}
				}
				//Nurse cannot delete support member.
				else if(memberRole.compareTo("S")==0)
					throw new ValidationFailException("Nurse cannot delete the support member");
				//Nurse cannot delete another nurse.
				else if(memberRole.compareTo("N")==0) {
					throw new ValidationFailException("Nurse cannot delete another nurse");
				}
				
				else return true;
			}
			
			// The current user is Support Member
			else if(userRole.compareTo("S")==0) {
				
				//Support Member can only delete itself
				if(accountId == memberId)
					return true;
				else throw new ValidationFailException("Support Member can only delete itself");
			}
			
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
	 * Function will expire a specific member from a specific group
	 */
	public void expireMember(long groupId, long memberId) throws ValidationFailException, Exception {
		try {
			
			List<Member> members = new ArrayList<Member>();
			members = memberDao.findByHql("from Member m WHERE "
					+ "m.accountId=" + memberId
					+ " and m.groupId=" + groupId
					+ " and m.isActive='Y'");
			
			if(members == null || members.size() < 1)
				throw new ValidationFailException("Invalid Input");
			
			//expire the member entry in MEMBER table and set isActive as N
			members.get(0).setIsActive("N");
			members.get(0).setExpirationDate(new Date());
			memberDao.update(members.get(0));
			
			// update column updated_date of GROUP_INFO table 
			Group group = groupDao.findById(groupId);
			group.setUpdatedDate(new Date());
			groupDao.update(group);
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
		
		//add default member
		Member owner = new Member();
		owner.setAccountId(account.getId());
		owner.setGroupId(group.getId());
		owner.setRoleId(roleId);
		owner.setCreateDate(new Date());
		owner.setIsActive("Y");
		memberDao.add(owner);
	}
	
	/**
	 * Function will save member details in MEMBER table
	 */
	public void saveNewMember(Member member) throws Exception {
		memberDao.add(member);
	}
	
	/**
	 * Function will perform business validations.
	 * 1. Patient can delete the group whether it is empty or not.
	 * 2. Nurse can delete the group only when it only has the nurse himself/herself.
	 * 3. Support member cannot delete a group.
	 */
	public  void deleteGroupValidation (long accountId,long groupId) 
			throws ValidationFailException, Exception {
		try {

			List<Dictionary> roles = new ArrayList<Dictionary>();
			roles = dictionaryService.getRolesByGroupIdAccId(accountId,groupId );

			if(roles == null || roles.size() < 1) {
				throw new ValidationFailException("Invalid action");
			}
			else {

				String userRole = roles.get(0).getValue();
				
				// Support Member cannot delete group
				if(userRole.compareTo("S")==0){
					throw new ValidationFailException("Support Member cannot delete the Group");
				} // Nurse should be able to delete only empty group 
				else if(userRole.compareTo("N")==0){
					List<Member> members = null;
					// count the no of effective members in the group
					members=getEffectiveMembers(groupId);
					if(members.size() > 1){
						throw new ValidationFailException("Nurse can only delete empty group!");
					} 
				}
			} 
		} catch (Exception e) {
			throw e;
		} 

	}
	
	/**
	 * Function will get all active members of the group
	 */
	public List<Member> getEffectiveMembers(long groupId) throws Exception {
		try {
			List<Member> members = new ArrayList<Member>();
			members = memberDao.findByHql("from Member m WHERE "
					+ "m.groupId=" + groupId
					+ " and m.isActive='Y'");
			return members;

		}catch (Exception e) {
			throw e;
		} 
	}

	/**
	 * Function will delete group details in GROUP_INFO table
	 */
	public void expireGroup(long groupId) throws Exception {
		//expire group
		try {
			
			Group group = groupDao.findById(groupId);
			group.setExpirationDate(new Date());
			groupDao.update(group);
		}
		catch (Exception e) {
			throw e;
		}
		
	}

	/**
	 * Function will expire all members in the specific group from member table.
	 */
	public void expireAllMember(long groupId) throws Exception {
		//delete group
		try {
			List<Member> members = null;
			// get all effective members
			members=getEffectiveMembers(groupId);
			
			if(!(members == null || members.size() < 1)) {
				// Expire all the members and set them as inactive in MEMBER table
				for(Member member: members) {
					member.setIsActive("N");
					member.setExpirationDate(new Date());
					memberDao.update(member);
				}
			}
		}
		catch (Exception e) {
			throw e;
		}
	}

	public List<Group> findCommonGroup(long accountId, long memberId) throws Exception {
		try {
			List<Group> groups = new ArrayList<Group>();
			
			groups = groupDao.findByHql("select g from Group g, Member m WHERE "
					+ "g.id=m.groupId "
					+ "and m.accountId = " + accountId 
					+ " and m.groupId in ( "
					+ "select m1.groupId from Member m1 WHERE "
					+ "m1.accountId=" + memberId
					+ ")");
			
			return groups;
		}
		catch (Exception e) {
			throw e;
		}
	}

}
