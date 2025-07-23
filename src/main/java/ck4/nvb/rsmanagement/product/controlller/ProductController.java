package ck4.nvb.rsmanagement.product.controlller;

import ck4.nvb.rsmanagement.product.dto.ProductDetailRequest;
import ck4.nvb.rsmanagement.product.dto.ProductDetailResponse;
import ck4.nvb.rsmanagement.product.entity.ProductEntity;
import ck4.nvb.rsmanagement.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping("/products")
    public List<ProductDetailResponse> getAllProducts() {
        return productService.getAllProducts();
    }

    @PostMapping("/product/create")
    public ProductDetailResponse createProduct(@RequestBody ProductDetailRequest request) {
        return productService.createProduct(request);
    }

    @DeleteMapping("product/delete/{id}")
    public ProductEntity deleteProduct(@PathVariable Long id) {
       ProductEntity product = productService.findProductById(id);
       product.setDeleted(true);
       productService.save(product);
       return product;
    }
}
