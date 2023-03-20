package ru.sevavto.stock.crm.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.sevavto.stock.crm.model.entity.CrmUser;

import java.util.Optional;

@Repository
public interface CrmUserRepository extends CrudRepository<CrmUser, Long> {

    Optional<CrmUser> findByEmail(String email);
}
