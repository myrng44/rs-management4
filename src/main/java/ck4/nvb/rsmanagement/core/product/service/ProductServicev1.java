//package ck4.nvb.rsmanagement.core.product.service;
//
//
//import ck4.nvb.rsmanagement.core.product.dto.ProductCreateDTO;
//import ck4.nvb.rsmanagement.core.product.dto.ProductResponseDTO;
//import ck4.nvb.rsmanagement.core.product.entity.CategoryEntity;
//import ck4.nvb.rsmanagement.core.product.entity.ProductEntity;
//import ck4.nvb.rsmanagement.core.product.entity.SupplierEntity;
//import ck4.nvb.rsmanagement.core.product.repository.CategoryRepository;
//import ck4.nvb.rsmanagement.core.product.repository.ProductRepository;
//import ck4.nvb.rsmanagement.core.product.repository.SupplierRepository;
//import org.hibernate.query.sqm.UnknownEntityException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class ProductService {
//
//    @Autowired
//    ProductRepository productRepository;
//
//    @Autowired
//    CategoryRepository categoryRepository;
//
//    @Autowired
//    SupplierRepository supplierRepository;
//
//    public List<ProductResponseDTO> getAllProducts() {
//        return productRepository.findAllProductDetails();
//    }
//
//    public ProductResponseDTO createProduct2(ProductCreateDTO request) {
//        ProductEntity newProduct = convertToProductEntity(request);
//        newProduct = productRepository.save(newProduct);
//        return convertToProductResponse(newProduct);
//    }
//
//    public ProductResponseDTO convertToProductResponse(ProductEntity newProduct) {
//        return new ProductResponseDTO().builder()
//                .productName(newProduct.getName())
//                .productId(newProduct.getId())
//                .build();
//    }
//
//    private ProductEntity convertToProductEntity(ProductCreateDTO request) {
//        return new ProductEntity().builder()
//                .name(request.getProductName())
//                .build();
//    }
//
//    public ProductResponseDTO createProduct(ProductCreateDTO request) {
//        // tìm hoặc tạo category
//        CategoryEntity category = categoryRepository.findByName(request.getCategoryName())
//                .orElseGet(() -> {
//                    CategoryEntity newCategory = new CategoryEntity();
//                    newCategory.setName(request.getCategoryName());
//                    return categoryRepository.save(newCategory);
//                });
//       SupplierEntity supplier = supplierRepository.findByName(request.getSupplierName())
//               .orElseGet(() -> {
//                   SupplierEntity newSupplier = new SupplierEntity();
//                   newSupplier.setName(request.getSupplierName());
//                   return supplierRepository.save(newSupplier);
//               });
//       ProductEntity product = new ProductEntity().builder()
//               .name(request.getProductName())
//               .categoryId(category.getId())
//               .supplierId(supplier.getId())
//               .build();
//
//       productRepository.save(product);
//
//       CategoryEntity savedCategory = categoryRepository.findById(product.getCategoryId()).orElseThrow(() -> new RuntimeException("Category not found"));
//       SupplierEntity savedSupperlier = supplierRepository.findById(product.getSupplierId()).orElseThrow(() -> new RuntimeException("Supperlier not found"));
//
//       return ProductResponseDTO.builder()
//               .productName(product.getName())
//               .categoryName(savedCategory.getName())
//               .supplierName(savedSupperlier.getName())
//               .build();
//    }
//
//    public ProductEntity findProductById(Long id) {
//        ProductEntity product =  productRepository.findProductEntityById(id)
//                .orElseThrow(() -> new UnknownEntityException("not found entity"));
//        return product;
//    }
//
//    public ProductEntity save(ProductEntity product) {
//       return productRepository.save(product);
//    }
//}
