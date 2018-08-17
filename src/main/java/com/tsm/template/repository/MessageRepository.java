package com.tsm.template.repository;

import com.tsm.template.model.Client;
import com.tsm.template.model.Message;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Transactional(propagation = Propagation.MANDATORY)
public interface MessageRepository extends CrudRepository<Message, Long>,
        IBaseRepository<Message, Long>, IBaseSearchRepositoryCustom<Message> {

    Optional<Message> findByIdAndClient(final Long id, final Client client);

    Set<Message> findByClientAndCreatedBetween(final Client client, final LocalDateTime initialDate,
                                               final LocalDateTime finalDate);
}
