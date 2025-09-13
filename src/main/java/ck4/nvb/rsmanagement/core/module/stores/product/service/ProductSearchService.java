package ck4.nvb.rsmanagement.core.module.stores.product.service;

import ck4.nvb.rsmanagement.base.search.base.BaseSearchService;
import ck4.nvb.rsmanagement.core.module.stores.product.domain.ProductSearchDocument;
import ck4.nvb.rsmanagement.core.module.stores.product.domain.ProductSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("productSearchService")
public class ProductSearchService extends BaseSearchService<ProductSearchDocument, Long> {
    @Autowired
    private ProductSearchRepository productSearchRepository;

    @Override
    protected String[] getSearchFields() {
        return new String[]{"name", "sku", "desc"};
    }

    @Override
    protected String extractFieldValue(ProductSearchDocument document, String field) {
        return switch (field) {
            case "name" -> document.getName();
            case "sku" -> document.getSku();
            case "desc" -> document.getDesc();
            default -> "";
        };
    }
}
