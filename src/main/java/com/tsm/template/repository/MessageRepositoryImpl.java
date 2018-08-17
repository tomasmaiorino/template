package com.tsm.template.repository;


import com.tsm.template.model.Message;
import com.tsm.template.model.SearchCriteria;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by tomas on 5/15/18.
 */
public class MessageRepositoryImpl implements IBaseSearchRepositoryCustom<Message> {

	@PersistenceContext
	private EntityManager entityManager;

	public List<Message> search(List<SearchCriteria> params) {
		return search(params, Message.class);
	}

	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}
}
