package ck4.nvb.rsmanagement.core.module.stores.product.service;

import ck4.nvb.rsmanagement.base.search.base.BaseHybridService;
import ck4.nvb.rsmanagement.base.search.base.BaseSearchService;
import ck4.nvb.rsmanagement.base.search.dto.SearchRequest;
import ck4.nvb.rsmanagement.base.search.sync.SyncEvent;
import ck4.nvb.rsmanagement.core.module.stores.product.domain.Product;
import ck4.nvb.rsmanagement.core.module.stores.product.domain.ProductRepository;
import ck4.nvb.rsmanagement.core.module.stores.product.domain.ProductSearchDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("productHybridService")
public class ProductHybridService extends BaseHybridService<Product, ProductSearchDocument, Long> {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductSearchService productSearchService;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    protected JpaRepository<Product, Long> getJpaRepository() {
        return productRepository;
    }

    @Override
    protected BaseSearchService<ProductSearchDocument, Long> getSearchService() {
        return productSearchService;
    }

    @Override
    protected Product updateEntity(Product existing, Product newData) {
        existing.setName(newData.getName());
        existing.setDescription(newData.getDescription());
        existing.setCategoryId(newData.getCategoryId());
        existing.setUnitPrice(newData.getUnitPrice());
        return existing;    }

    @Override
    protected List<Product> findEntitiesByIds(List<Long> longs) {
        return getJpaRepository().findAllById(longs);
    }

    @Override
    protected List<Product> structuredSearch(SearchRequest request) {
        return List.of();
    }

    @Override
    protected ProductSearchDocument convertToSearchDocument(Product entity) {
        ProductSearchDocument document = new ProductSearchDocument();
        document.fromEntity(entity);
        return document;
    }

    @Override
    public Product create(Product entity) {
        Product saved = super.create(entity);
        applicationEventPublisher.publishEvent(new SyncEvent.EntityCreated(this, saved, ProductSearchDocument.class));
        return saved;
    }

    @Override
    public Product update(Long aLong, Product entity) {
        Product updated =  super.update(aLong, entity);
        applicationEventPublisher.publishEvent(new SyncEvent.EntityUpdated(this, updated, ProductSearchDocument.class));
        return updated;
    }

    @Override
    public void delete(Long id) {
        super.delete(id);
        applicationEventPublisher.publishEvent(new SyncEvent.EntityDeleted(this, id, Product.class));
    }
}
