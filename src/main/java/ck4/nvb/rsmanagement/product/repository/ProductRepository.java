package ck4.nvb.rsmanagement.product.repository;


import ck4.nvb.rsmanagement.product.dto.ProductDetailResponse;
import ck4.nvb.rsmanagement.product.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
//    @Query("""
//        SELECT new ck4.nvb.rsmanagement.product.dto.ProductDetailResponse (
//            p.name, c.name, s.name
//        )
//        FROM ProductEntity p
//        JOIN CategoryEntity c on p.categoryId = c.id
//        JOIN SupplierEntity s on p.supplierId = s.id
//        where p.isDeleted = false
//""")
//    List<ProductDetailResponse> findAllProductDetails();

    Optional<ProductEntity> findProductEntityById(Long id);

    @Query("""
    select new ck4.nvb.rsmanagement.product.dto.ProductDetailResponse (
        p.id, p.name, c.name, s.name
    )
    from ProductEntity p
    join CategoryEntity c on p.categoryId = c.id
    JOIN SupplierEntity s on p.supplierId = s.id
    where p.isDeleted = false
""")
    List<ProductDetailResponse> findAllProductDetails();
}
