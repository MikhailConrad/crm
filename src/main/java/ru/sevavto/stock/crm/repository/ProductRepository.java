package ru.sevavto.stock.crm.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.sevavto.stock.crm.model.entity.Product;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {

    Optional<Product> findById(long id);

    Optional<Product> findByProductCode(String productCode);

    List<Product> findAllByName(String name);

    List<Product> findAll();

    Product save(Product product);
}
