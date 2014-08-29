package aucklanduni.ece.hc.service;

import java.util.ArrayList;
import java.util.Map;

import aucklanduni.ece.hc.repository.model.Dictionary;

public interface DictionaryService  extends BaseService<Dictionary>{
	
	public ArrayList<Dictionary> GetRoles() throws Exception;
	
	public ArrayList<Dictionary> GetSpecificRoles(long accountId, long groupId) throws Exception;

}
