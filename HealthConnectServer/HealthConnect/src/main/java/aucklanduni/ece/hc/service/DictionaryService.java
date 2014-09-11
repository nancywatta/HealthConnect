package aucklanduni.ece.hc.service;

import java.util.ArrayList;

import org.springframework.transaction.annotation.Transactional;

import aucklanduni.ece.hc.repository.model.Dictionary;

@Transactional
public interface DictionaryService  extends BaseService<Dictionary>{
	
	public ArrayList<Dictionary> GetSpecificRoles(long accountId, long groupId) throws Exception;

}
