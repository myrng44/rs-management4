package develop.circlek.core.inventory.application.service;

import develop.circlek.base.application.service.BaseService;
import develop.circlek.base.domain.repository.BaseRepository;
import develop.circlek.core.inventory.domain.entity.ProductEntity;
import develop.circlek.core.inventory.domain.repository.ProductRepository;
import develop.circlek.core.inventory.domain.repository.CategoryRepository;
import develop.circlek.core.inventory.domain.repository.SupplierRepository;
import develop.circlek.core.inventory.application.dto.ProductDTO;
import develop.circlek.core.inventory.application.dto.request.CreateProductRequest;
import develop.circlek.base.application.exception.NotFoundException;
import develop.circlek.base.application.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService extends BaseService<ProductEntity, ProductDTO, Long> {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SupplierRepository supplierRepository;

    @Override
    protected BaseRepository<ProductEntity, Long> getRepository() {
        return productRepository;
    }

    @Override
    protected ProductDTO convertToDTO(ProductEntity entity) {
        return ProductDTO.builder()
                .id(entity.getId())
                .createAt(LocalDateTime.now())
                .createBy(getCurrentUserId())
                .updateAt(entity.getUpdateAt())
                .updateBy(entity.getUpdateBy())
                .name(entity.getName())
                .sku(entity.getSku())
                .description(entity.getDescription())
                .unitPrice(entity.getUnitPrice())
                .categoryId(entity.getCategoryId())
                .categoryName(entity.getCategory() != null ? entity.getCategory().getName() : null)
                .supplierId(entity.getSupplierId())
                .supplierName(entity.getSupplier() != null ? entity.getSupplier().getName() : null)
                .deleted(entity.getDeleted())
                .build();
    }

    @Override
    protected ProductEntity convertToEntity(ProductDTO dto) {
        return ProductEntity.builder()
                .name(dto.getName())
                .sku(dto.getSku())
                .description(dto.getDescription())
                .unitPrice(dto.getUnitPrice())
                .categoryId(dto.getCategoryId())
                .supplierId(dto.getSupplierId())
                .deleted(dto.getDeleted())
                .build();
    }

    @Override
    protected void updateEntityFromDTO(ProductEntity entity, ProductDTO dto) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setUnitPrice(dto.getUnitPrice());
        entity.setCategoryId(dto.getCategoryId());
        entity.setSupplierId(dto.getSupplierId());
    }

    @Transactional
    public ProductDTO createProduct(CreateProductRequest request) {
        if (productRepository.findBySkuAndDeletedFalse(request.getSku()).isPresent()) {
            throw new BusinessException("Product with SKU '" + request.getSku() + "' already exists");
        }

        if (!categoryRepository.existsById(request.getCategoryId())) {
            throw new NotFoundException("Category not found with id: " + request.getCategoryId());
        }

        if (!supplierRepository.existsById(request.getSupplierId())) {
            throw new NotFoundException("Supplier not found with id: " + request.getSupplierId());
        }

        ProductEntity product = ProductEntity.builder()
                .name(request.getName())
                .sku(request.getSku())
                .description(request.getDescription())
                .unitPrice(request.getUnitPrice())
                .categoryId(request.getCategoryId())
                .supplierId(request.getSupplierId())
                .deleted(false)
                .build();

        ProductEntity savedProduct = productRepository.save(product);
        return findByIdWithDetails(savedProduct.getId());
    }

    public ProductDTO findByIdWithDetails(Long id) {
        ProductEntity product = productRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new NotFoundException("Product not found with id: " + id));
        return convertToDTO(product);
    }

    public List<ProductDTO> findActiveProducts() {
        return productRepository.findByDeletedFalse().stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<ProductDTO> findByCategory(Long categoryId) {
        return productRepository.findByCategoryIdAndDeletedFalse(categoryId).stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<ProductDTO> findBySupplier(Long supplierId) {
        return productRepository.findBySupplierIdAndDeletedFalse(supplierId).stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<ProductDTO> searchByName(String name) {
        return productRepository.findByNameContainingAndDeletedFalse(name).stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Transactional
    public void softDelete(Long id) {
        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found with id: " + id));
        product.setDeleted(true);
        productRepository.save(product);
    }
}