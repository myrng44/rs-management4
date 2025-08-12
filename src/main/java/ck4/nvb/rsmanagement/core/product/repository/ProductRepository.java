package ck4.nvb.rsmanagement.core.product.repository;


import ck4.nvb.rsmanagement.base.domain.repository.BaseFullAuditedRepository;
import ck4.nvb.rsmanagement.core.product.entity.ProductEntity;
import org.springframework.stereotype.Repository;
@Repository
public interface ProductRepository extends BaseFullAuditedRepository<ProductEntity, Long, Long> {

//
//    @Query("""
//    select new ck4.nvb.rsmanagement.core.product.dto.ProductDetailResponse (
//        p.id, p.name, c.name, s.name
//    )
//    from ProductEntity p
//    join CategoryEntity c on p.categoryId = c.id
//    JOIN SupplierEntity s on p.supplierId = s.id
//    where p.isDeleted = false
//""")
//    List<ProductResponseDTO> findAllProductDetails();
//    Page<ProductCreateDTO> findProductEntity(Pageable pageable);
}
