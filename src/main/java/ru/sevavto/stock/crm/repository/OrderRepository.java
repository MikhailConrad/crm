package ru.sevavto.stock.crm.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.sevavto.stock.crm.model.entity.Order;
import ru.sevavto.stock.crm.model.entity.OrderStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long> {

    Optional<Order> findById(long id);

    List<Order> findAll();

    List<Order> findAllByOrganizationManager_OrganizationName(String organizationName);

    List<Order> findAllByStatus(OrderStatus status);

    Order save(Order order);

    List<Order> findAllByDateOfOrderBetween(LocalDate startDate, LocalDate endDate);
}
