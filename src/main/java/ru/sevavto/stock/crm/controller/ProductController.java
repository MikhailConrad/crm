package ru.sevavto.stock.crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.sevavto.stock.crm.model.dto.UpsertProductRequest;
import ru.sevavto.stock.crm.model.dto.ProductResponse;
import ru.sevavto.stock.crm.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {
    private ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('product:read')")
    public List<ProductResponse> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('product:read')")
    public ProductResponse getProductById(@PathVariable long id) {
        return productService.getProductById(id);
    }

    @GetMapping(params = "name")
    @PreAuthorize("hasAuthority('product:read')")
    public List<ProductResponse> getProductsByName(@RequestParam(value = "name") String name) {
        return productService.getProductsByName(name);
    }

    @GetMapping(params = "productCode")
    @PreAuthorize("hasAuthority('product:read')")
    public ProductResponse getProductsByProductCode(@RequestParam(value = "productCode") String productCode) {
        return productService.getProductByProductCode(productCode);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('product:upsert')")
    public ProductResponse addProduct(@RequestBody UpsertProductRequest request) {
        return productService.addProduct(request);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('product:upsert')")
    public ProductResponse updateProduct(@RequestBody UpsertProductRequest request) {
        return productService.updateProduct(request);
    }
}
