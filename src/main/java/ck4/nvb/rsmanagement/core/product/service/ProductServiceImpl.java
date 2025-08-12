package ck4.nvb.rsmanagement.core.product.service;

import ck4.nvb.rsmanagement.base.application.service.FullAuditedCrudServiceImpl;

import ck4.nvb.rsmanagement.base.domain.repository.BaseFullAuditedRepository;
import ck4.nvb.rsmanagement.core.auth.dto.UserDTO;
import ck4.nvb.rsmanagement.core.product.dto.ProductResponseDTO;
import ck4.nvb.rsmanagement.core.product.entity.ProductEntity;
import ck4.nvb.rsmanagement.core.product.repository.ProductRepository;
import org.springframework.stereotype.Service;


@Service
public class ProductServiceImpl extends FullAuditedCrudServiceImpl<
        ProductResponseDTO,
        ProductEntity,
        Long,
        UserDTO,
        Long
        > {
    protected ProductServiceImpl(ProductRepository repository) {
        super(repository, ProductEntity.class);
    }

    @Override
    public ProductResponseDTO mapToEntityDto(ProductEntity entity) {
        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setSku(entity.getSku());
        dto.setCategoryId(entity.getCategoryId());
        dto.setUnitPrice(entity.getUnitPrice());
        dto.setSupplierId(entity.getSupplierId());
        return dto;
    }

    @Override
    public BaseFullAuditedRepository<ProductEntity, Long, Long> getRepository() {
        return (ProductRepository) super.getRepository();
    }


}
