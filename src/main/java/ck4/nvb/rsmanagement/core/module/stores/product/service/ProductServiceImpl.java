package ck4.nvb.rsmanagement.core.module.stores.product.service;

import ck4.nvb.rsmanagement.base.application.service.FullAuditedCrudServiceImpl;
import ck4.nvb.rsmanagement.base.web.utils.SearchOperator;
import ck4.nvb.rsmanagement.core.module.stores.product.domain.Product;
import ck4.nvb.rsmanagement.core.module.stores.product.domain.ProductRepository;
import ck4.nvb.rsmanagement.core.module.stores.product.service.dto.ProductGetDto;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service("productService")
public class ProductServiceImpl extends FullAuditedCrudServiceImpl<ProductGetDto, Product, Long, UserGetDto, Long> {

    protected ProductServiceImpl(ProductRepository repository) {
        super(repository, Product.class);
    }

    @Override
    public ProductGetDto mapToEntityDto(Product entity) {
        ProductGetDto dto = new ProductGetDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setSku(entity.getSku());
        dto.setCategoryId(entity.getCategoryId());
        dto.setUnitPrice(entity.getUnitPrice());
        dto.setSupplierId(entity.getSupplierId());

//        return new ModelMapper().map(entity, ProductGetDto.class);
        return dto;
    }

    @Override
    public Map<String, List<SearchOperator>> getSearchableKeys() {
        Map<String, List<SearchOperator>> keys = super.getSearchableKeys();
        keys.put("sku", List.of(SearchOperator.CONTAINS, SearchOperator.EQUALS));
        return keys;
    }

    @Override
    public Set<String> getSortableKeys() {
        Set<String> keys = super.getSortableKeys();
        keys.add("sku");
        keys.add("name");
        keys.add("price");

        return keys;
    }


}
