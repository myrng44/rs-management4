package ck4.nvb.rsmanagement.product.service;


import ck4.nvb.rsmanagement.product.dto.ProductDetailRequest;
import ck4.nvb.rsmanagement.product.dto.ProductDetailResponse;
import ck4.nvb.rsmanagement.product.entity.CategoryEntity;
import ck4.nvb.rsmanagement.product.entity.ProductEntity;
import ck4.nvb.rsmanagement.product.entity.SupplierEntity;
import ck4.nvb.rsmanagement.product.repository.CategoryRepository;
import ck4.nvb.rsmanagement.product.repository.ProductRepository;
import ck4.nvb.rsmanagement.product.repository.SupplierRepository;
import org.hibernate.query.sqm.UnknownEntityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    SupplierRepository supplierRepository;

    public List<ProductDetailResponse> getAllProducts() {
        return productRepository.findAllProductDetails();
    }

    public ProductDetailResponse createProduct2(ProductDetailRequest request) {
        ProductEntity newProduct = convertToProductEntity(request);
        newProduct = productRepository.save(newProduct);
        return convertToProductResponse(newProduct);
    }

    public ProductDetailResponse convertToProductResponse(ProductEntity newProduct) {
        return new ProductDetailResponse().builder()
                .productName(newProduct.getName())
                .productId(newProduct.getId())
                .build();
    }

    private ProductEntity convertToProductEntity(ProductDetailRequest request) {
        return new ProductEntity().builder()
                .name(request.getProductName())
                .build();
    }

    public ProductDetailResponse createProduct(ProductDetailRequest request) {
        // tìm hoặc tạo category
        CategoryEntity category = categoryRepository.findByName(request.getCategoryName())
                .orElseGet(() -> {
                    CategoryEntity newCategory = new CategoryEntity();
                    newCategory.setName(request.getCategoryName());
                    return categoryRepository.save(newCategory);
                });
       SupplierEntity supplier = supplierRepository.findByName(request.getSupplierName())
               .orElseGet(() -> {
                   SupplierEntity newSupplier = new SupplierEntity(); // ✅ dùng biến cục bộ mới
                   newSupplier.setName(request.getSupplierName());
                   return supplierRepository.save(newSupplier);
               });
       ProductEntity product = new ProductEntity().builder()
               .name(request.getProductName())
               .categoryId(category.getId())
               .supplierId(supplier.getId())
               .build();

       productRepository.save(product);

       CategoryEntity savedCategory = categoryRepository.findById(product.getCategoryId()).orElseThrow(() -> new RuntimeException("Category not found"));
       SupplierEntity savedSupperlier = supplierRepository.findById(product.getSupplierId()).orElseThrow(() -> new RuntimeException("Supperlier not found"));

       return ProductDetailResponse.builder()
               .productName(product.getName())
               .categoryName(savedCategory.getName())
               .supplierName(savedSupperlier.getName())
               .build();
    }

    public ProductEntity findProductById(Long id) {
        ProductEntity product =  productRepository.findProductEntityById(id)
                .orElseThrow(() -> new UnknownEntityException("not found entity"));
        return product;
    }

    public ProductEntity save(ProductEntity product) {
       return productRepository.save(product);
    }
}
