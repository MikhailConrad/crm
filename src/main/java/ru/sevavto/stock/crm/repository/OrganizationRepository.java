package ru.sevavto.stock.crm.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.sevavto.stock.crm.model.entity.Organization;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrganizationRepository extends CrudRepository<Organization, Long> {
    Optional<Organization> findByName(String name);

    List<Organization> findAll();

    Organization save(Organization organization);
}
