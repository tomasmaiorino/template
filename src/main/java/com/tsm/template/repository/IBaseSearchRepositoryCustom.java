package com.tsm.template.repository;

import com.tsm.template.model.Message;
import com.tsm.template.model.SearchCriteria;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Objects;

/**
 * Created by tomas on 5/16/18.
 */

public interface IBaseSearchRepositoryCustom<I> {

    EntityManager getEntityManager();

    default List<I> search(List<SearchCriteria> params, Class<I> clazz) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<I> query = builder.createQuery(clazz);
        Root<Message> r = query.from(Message.class);

        Predicate predicate = builder.conjunction();

        for (SearchCriteria param : params) {
            if (param.getOperation().equalsIgnoreCase(">")) {
                predicate = builder.and(predicate,
                        builder.greaterThanOrEqualTo(r.get(param.getKey()),
                                param.getValue().toString()));
            } else if (param.getOperation().equalsIgnoreCase("<")) {
                predicate = builder.and(predicate,
                        builder.lessThanOrEqualTo(r.get(param.getKey()),
                                param.getValue().toString()));
            } else if (param.getOperation().equalsIgnoreCase(":")) {
                if (r.get(param.getKey()).getJavaType() == String.class) {
                    predicate = builder.and(predicate,
                            //builder.like(r.get(param.getKey()), "%" + param.getValue() + "%"));
                            builder.equal(r.get(param.getKey()),param.getValue()));
                } else if (!Objects.isNull(param.getConvertEnumClass())) {
                    predicate = builder.and(predicate,
                            builder.equal(r.get(param.getKey()),
                                    Enum.valueOf(param.getConvertEnumClass(),  param.getValue().toString())));
                } else {
                    predicate = builder.and(predicate,
                            builder.equal(r.get(param.getKey()), param.getValue()));
                }
            }
        }
        query.where(predicate);

        return getEntityManager().createQuery(query).getResultList();
    }
}
