package aucklanduni.ece.hc.service;
import org.springframework.transaction.annotation.Transactional;

import aucklanduni.ece.hc.repository.model.ApnUser;

@Transactional
public interface ApnUserService extends BaseService<ApnUser>{
	public int executMySql() throws Exception;
	public int executMyHql() throws Exception;
}
//@Service
//public class ApnUserService {
//	@Autowired
//	private BaseDao<ApnUser> baseDao;
//	
//	@Transactional
//	public List<ApnUser> findAll() throws Exception {
//		return baseDao.findAll();
//	}
//}
