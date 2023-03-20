package ru.sevavto.stock.crm.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.sevavto.stock.crm.model.entity.ProductCategory;

import java.util.Optional;

@Repository
public interface ProductCategoryRepository extends CrudRepository<ProductCategory, Long> {

    Optional<ProductCategory> findById(long id);

    Optional<ProductCategory> findByName(String name);

    ProductCategory save(ProductCategory product);
}
