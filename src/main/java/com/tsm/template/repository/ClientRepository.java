package com.tsm.template.repository;

import com.tsm.template.model.Client;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Transactional(propagation = Propagation.MANDATORY)
public interface ClientRepository extends Repository<Client, Integer>, IBaseRepository<Client, Integer> {

    Optional<Client> findByToken(final String token);

    Set<Client> findByStatus(final Client.ClientStatus status);

    Set<Client> findByIsAdmin(final Boolean admin);

}
