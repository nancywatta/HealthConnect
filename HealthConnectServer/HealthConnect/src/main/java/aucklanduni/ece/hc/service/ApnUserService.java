package aucklanduni.ece.hc.service;
import org.springframework.transaction.annotation.Transactional;

import aucklanduni.ece.hc.repository.model.ApnUser;
/**
 * 
* @ClassName: ApnUserService 
* @Description: This is a demo showing what a service is alike
* Note that use annotation @Transactional as all the methods inside 
* follow transaction rules
* @author Zhao Yuan
* @date 2014年9月15日 下午9:08:07 
*
 */
@Transactional
public interface ApnUserService extends BaseService<ApnUser>{
	public int executMySql() throws Exception;
	public int executMyHql() throws Exception;
}