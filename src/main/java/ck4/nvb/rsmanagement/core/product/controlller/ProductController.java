package ck4.nvb.rsmanagement.core.product.controlller;

import ck4.nvb.rsmanagement.base.web.controller.AuditedCrudController;
import ck4.nvb.rsmanagement.core.auth.dto.UserDTO;
import ck4.nvb.rsmanagement.core.product.dto.ProductCreateDTO;
import ck4.nvb.rsmanagement.core.product.dto.ProductResponseDTO;
import ck4.nvb.rsmanagement.core.product.dto.ProductUpdateDTO;
import ck4.nvb.rsmanagement.core.product.entity.ProductEntity;
import ck4.nvb.rsmanagement.core.product.service.ProductServiceImpl;
import lombok.Getter;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
@Getter
public class ProductController extends AuditedCrudController<ProductResponseDTO, ProductEntity, Long, UserDTO, Long, ProductCreateDTO, ProductUpdateDTO> {

    private final ProductServiceImpl productService;

    @Override
    public UserDTO extractUser(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return null;
        }

        Object principal = auth.getPrincipal();

        return new ModelMapper().map(principal, UserDTO.class);
    }

    public ProductController(ProductServiceImpl productService) {
        super(productService);
        this.productService = productService;
    }

//    @GetMapping("/get/{id}")
//    public ProductResponseDTO getId(@PathVariable Long id) {
//        ProductEntity result = productRepository.findById(id)
//                .orElseThrow(() -> new ObjectNotFoundException("object not found"));
//        System.out.println(result);
//        return new ProductResponseDTO().builder()
//                .productId(result.getId())
//                .productName(result.getName())
//                .unitPrice(result.getUnitPrice())
//                .description(result.getDescription())
//                .categoryId(result.getCategoryId())
//                .supplierId(result.getSupplierId())
//                .build();
//    }


}