package ru.sevavto.stock.crm.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.sevavto.stock.crm.model.entity.PriceRecord;
import ru.sevavto.stock.crm.model.entity.Product;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PriceRecordRepository extends CrudRepository<PriceRecord, Long> {

    <Optional>PriceRecord findById(long id);

    List<PriceRecord> findAllByProductId(long productId);

    PriceRecord save(PriceRecord priceRecord);

    List<PriceRecord> findAllByProductAndDateOfPriceUpdateBetween(Product product, LocalDate startDate, LocalDate endDate);
}
