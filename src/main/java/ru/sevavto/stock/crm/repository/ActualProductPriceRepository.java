package ru.sevavto.stock.crm.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.sevavto.stock.crm.model.entity.ActualProductPrice;
import ru.sevavto.stock.crm.model.entity.Product;

import java.util.Optional;

@Repository
public interface ActualProductPriceRepository extends CrudRepository<ActualProductPrice, Long> {

    Optional<ActualProductPrice> findById(long id);

    Optional<ActualProductPrice> findByProductId(long productId);

    ActualProductPrice findByProduct(Product product);

    ActualProductPrice save(ActualProductPrice actualProductPrice);
}
