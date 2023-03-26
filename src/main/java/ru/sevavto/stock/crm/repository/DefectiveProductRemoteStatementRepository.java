package ru.sevavto.stock.crm.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.sevavto.stock.crm.model.entity.DefectiveProductRemoteStatement;
import ru.sevavto.stock.crm.model.entity.DefectiveProductRemoteStatementStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface DefectiveProductRemoteStatementRepository extends CrudRepository<DefectiveProductRemoteStatement, Long> {

    Optional<DefectiveProductRemoteStatement> findById(long id);

    List<DefectiveProductRemoteStatement> findAll();
    List<DefectiveProductRemoteStatement> findAllByStatus(DefectiveProductRemoteStatementStatus status);

    DefectiveProductRemoteStatement save(DefectiveProductRemoteStatement updatedStatement);
}
