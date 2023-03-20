package ru.sevavto.stock.crm.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.sevavto.stock.crm.model.entity.OrderPosition;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderPositionRepository extends CrudRepository<OrderPosition, Long> {

    Optional<OrderPosition> findById(long id);

    List<OrderPosition> findAllByOrderId(long id);

    OrderPosition save(OrderPosition orderPosition);
}
