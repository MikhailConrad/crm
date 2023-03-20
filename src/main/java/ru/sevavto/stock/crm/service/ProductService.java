package ru.sevavto.stock.crm.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sevavto.stock.crm.exception.NotFoundException;
import ru.sevavto.stock.crm.model.dto.UpsertProductRequest;
import ru.sevavto.stock.crm.model.dto.ProductResponse;
import ru.sevavto.stock.crm.util.mapper.ProductToDtoMapper;
import ru.sevavto.stock.crm.model.entity.ActualProductPrice;
import ru.sevavto.stock.crm.model.entity.PriceRecord;
import ru.sevavto.stock.crm.model.entity.Product;
import ru.sevavto.stock.crm.repository.ActualProductPriceRepository;
import ru.sevavto.stock.crm.repository.PriceRecordRepository;
import ru.sevavto.stock.crm.repository.ProductCategoryRepository;
import ru.sevavto.stock.crm.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final ActualProductPriceRepository actualProductPriceRepository;
    private final PriceRecordRepository priceRecordRepository;

    @Autowired
    public ProductService(ProductRepository productRepository,
                          ProductCategoryRepository productCategoryRepository,
                          ActualProductPriceRepository actualProductPriceRepository,
                          PriceRecordRepository priceRecordRepository) {
        this.productRepository = productRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.actualProductPriceRepository = actualProductPriceRepository;
        this.priceRecordRepository = priceRecordRepository;
    }

    public ProductResponse getProductById(long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Продукта с таким id не существует"));
        PriceRecord priceRecord = actualProductPriceRepository.findByProduct(product).getPriceRecord();
        BigDecimal price = priceRecord.getPrice();
        return ProductToDtoMapper.mapToDto(product, price);
    }

    public ProductResponse getProductByProductCode(String productCode) {
        Product product = productRepository.findByProductCode(productCode)
                .orElseThrow(() -> new NotFoundException("Продукта с таким id не существует"));
        PriceRecord priceRecord = actualProductPriceRepository.findByProduct(product).getPriceRecord();
        BigDecimal price = priceRecord.getPrice();
        return ProductToDtoMapper.mapToDto(product, price);
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();

        List<ProductResponse> productsWithPrices = products.stream()
                .map(product -> ProductToDtoMapper.mapToDto(
                        product,
                        actualProductPriceRepository.findByProduct(product).getPriceRecord().getPrice()
                ))
                .collect(Collectors.toList());
        return productsWithPrices;
    }

    public List<ProductResponse> getProductsByName(String name) {
        List<Product> products = productRepository.findAllByName(name);

        List<ProductResponse> productsWithPrices = products.stream()
                .map(product -> ProductToDtoMapper.mapToDto(
                        product,
                        actualProductPriceRepository.findByProduct(product).getPriceRecord().getPrice()
                        ))
                .collect(Collectors.toList());
        return productsWithPrices;
    }

    @Transactional
    public ProductResponse addProduct(UpsertProductRequest request) {
        if(productRepository.findByProductCode(request.getProductCode()).isPresent()) {
            throw new RuntimeException("Товар с таким productCode уже существует");
        }
        Product product = productRepository.save(
                Product.builder()
                    .productCode(request.getProductCode())
                    .name(request.getName())
                    .description(request.getDescription())
                    .productCategory(productCategoryRepository.findByName(request.getProductCategory())
                            .orElseThrow(() -> new NotFoundException("Категория товаров не найдена")))
                    .weight(request.getWeight())
                    .quantity(request.getQuantity())
                    .build()
        );
        PriceRecord priceRecord = priceRecordRepository.save(new PriceRecord(product, request.getPrice()));
        ActualProductPrice actualProductPrice = actualProductPriceRepository.save(new ActualProductPrice(product, priceRecord));
        return ProductToDtoMapper.mapToDto(product, actualProductPrice.getPriceRecord().getPrice());
    }

    @Transactional
    public ProductResponse updateProduct(UpsertProductRequest request) {
        Product product = productRepository.findByProductCode(request.getProductCode())
                .orElseThrow(() -> new NotFoundException("Продукта с таким productCode не существует"));

        product.setProductCode(request.getProductCode());
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setProductCategory(productCategoryRepository.findByName(request.getProductCategory())
                .orElseThrow(() -> new NotFoundException("Категория товаров не найдена")));
        product.setWeight(request.getWeight());
        product.setQuantity(request.getQuantity());
        productRepository.save(product);

        ActualProductPrice actualProductPrice = actualProductPriceRepository.findByProduct(product);
        if(!request.getPrice().equals(actualProductPrice.getPriceRecord().getPrice())) {
            PriceRecord priceRecord = priceRecordRepository.save(new PriceRecord(product, request.getPrice()));
            actualProductPrice = actualProductPriceRepository.save(new ActualProductPrice(product, priceRecord));
        }
        return ProductToDtoMapper.mapToDto(product, actualProductPrice.getPriceRecord().getPrice());
    }
}
