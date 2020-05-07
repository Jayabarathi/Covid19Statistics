package com.covid19.stat.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.covid19.stat.model.District;
import com.covid19.stat.model.State;

@Component
@Repository
public class Covid19DaoImpl implements Covid19Dao{
	
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void saveJsonData(List<State> stateModelList) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		for(State state : stateModelList) {
			State stateObj = session.get(State.class, state.getStatecode());
			if(stateObj != null) {
				session.delete(stateObj);
			}
		}
		session.getTransaction().commit();
		session.beginTransaction();
		for(State state : stateModelList) {
			session.saveOrUpdate(state);
		}
		session.getTransaction().commit();
//		session.close();
	}

	@Override
	public List<District> getAllDistricts(String stateCode) {
		Session session = sessionFactory.getCurrentSession();
		List<District> districtList = session.createQuery("from District Where state_code = '" + stateCode + "' order by confirmed desc").list();
		return districtList;
	}

	@Override
	public List<State> getAllStateInfo() {
		Session session = sessionFactory.getCurrentSession();
		List<State> stateList = session.createQuery("from State").list();
		return stateList;
	}
}
