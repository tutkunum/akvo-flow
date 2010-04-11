package org.waterforpeople.mapping.helper;

import java.util.List;
import java.util.logging.Logger;

import org.waterforpeople.mapping.domain.TechnologyType;

import com.gallatinsystems.framework.dao.BaseDAO;

public class TechnologyTypeHelper {
	private static final Logger log = Logger.getLogger(TechnologyTypeHelper.class
			.getName());

	public List<TechnologyType> listTechnologyTypes(){
		BaseDAO<TechnologyType> baseDAO = new BaseDAO<TechnologyType>(TechnologyType.class);
		return baseDAO.list();
	}
	
	public TechnologyType save(TechnologyType techType){
		BaseDAO<TechnologyType> baseDAO = new BaseDAO<TechnologyType>(TechnologyType.class);
		return baseDAO.save(techType);
	}
	
	public void delete(TechnologyType techType){
		BaseDAO<TechnologyType> baseDAO = new BaseDAO<TechnologyType>(TechnologyType.class);
		baseDAO.delete(techType);
	}
	
	public TechnologyType getTechnologyType(Long id){
		BaseDAO<TechnologyType> baseDAO = new BaseDAO<TechnologyType>(TechnologyType.class);
		return baseDAO.getByKey(id);
	}
	
	public void deleteAll(){
		BaseDAO<TechnologyType> baseDAO = new BaseDAO<TechnologyType>(TechnologyType.class);
		for(TechnologyType techType: baseDAO.list()){
			baseDAO.delete(techType);
		}
	}
}
