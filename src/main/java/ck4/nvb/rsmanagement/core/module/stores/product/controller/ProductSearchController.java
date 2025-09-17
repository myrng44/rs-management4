package ck4.nvb.rsmanagement.core.module.stores.product.controller;

import ck4.nvb.rsmanagement.base.search.base.BaseHybridService;
import ck4.nvb.rsmanagement.base.search.base.BaseSearchController;
import ck4.nvb.rsmanagement.base.search.dto.SearchRequest;
import ck4.nvb.rsmanagement.core.module.stores.product.domain.Product;
import ck4.nvb.rsmanagement.core.module.stores.product.service.ProductHybridService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/${rs.api.main.baseUrl}/products")
public class ProductSearchController extends BaseSearchController<Product, Long> {
  @Autowired private ProductHybridService productHybridService;

  @Override
  protected BaseHybridService<Product, ?, Long> getHybridService() {
    return productHybridService;
  }

  @Override
  public ResponseEntity<List<Product>> search(String q) {
    return super.search(q);
  }

  @Override
  public ResponseEntity<List<Product>> advancedSearch(SearchRequest request) {
    return super.advancedSearch(request);
  }

  @GetMapping("search/suggestions")
  @Override
  public ResponseEntity<List<String>> suggestions(String prefix, String field) {
    return super.suggestions(prefix, field);
  }

  @GetMapping("/search/fuzzy")
  @Override
  public ResponseEntity<List<Product>> fuzzySearch(String q) {
    return super.fuzzySearch(q);
  }
}
