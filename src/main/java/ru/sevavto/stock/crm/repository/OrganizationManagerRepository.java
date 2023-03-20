package ru.sevavto.stock.crm.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.sevavto.stock.crm.model.entity.OrganizationManager;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrganizationManagerRepository extends CrudRepository<OrganizationManager, Long> {

    Optional<OrganizationManager> findById(long id);

    List<OrganizationManager> findByOrganizationName(String organizationName);
}
