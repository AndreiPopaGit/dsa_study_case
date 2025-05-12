package org.datasource.jpa;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service @ApplicationScope
public class JPADataSourceConnector {
	@PersistenceContext
	private EntityManager em;

	public EntityManager getEntityManager(){
		return em;
	}
	
}