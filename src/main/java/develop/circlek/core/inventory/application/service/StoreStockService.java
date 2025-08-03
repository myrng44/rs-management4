package develop.circlek.core.inventory.application.service;

import develop.circlek.base.application.service.BaseService;
import develop.circlek.base.domain.repository.BaseRepository;
import develop.circlek.core.inventory.domain.entity.StoreStockEntity;
import develop.circlek.core.inventory.domain.repository.StoreStockRepository;
import develop.circlek.core.inventory.application.dto.StoreStockDTO;
import develop.circlek.base.application.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StoreStockService extends BaseService<StoreStockEntity, StoreStockDTO, Long> {
    
    private final StoreStockRepository storeStockRepository;
    
    @Override
    protected BaseRepository<StoreStockEntity, Long> getRepository() {
        return storeStockRepository;
    }
    
    @Override
    protected StoreStockDTO convertToDTO(StoreStockEntity entity) {
        return StoreStockDTO.builder()
                .productId(entity.getProductId())
                .productName(entity.getProduct() != null ? entity.getProduct().getName() : null)
                .productSku(entity.getProduct() != null ? entity.getProduct().getSku() : null)
                .storeId(entity.getStoreId())
                .storeName(entity.getStore() != null ? entity.getStore().getName() : null)
                .quantity(entity.getQuantity())
                .importId(entity.getImportId())
                .minQuantity(entity.getMinQuantity())
                .deleted(entity.getDeleted())
                .isLowStock(entity.getQuantity() <= entity.getMinQuantity())
                .build();
    }
    
    @Override
    protected StoreStockEntity convertToEntity(StoreStockDTO dto) {
        return StoreStockEntity.builder()
                .productId(dto.getProductId())
                .storeId(dto.getStoreId())
                .quantity(dto.getQuantity())
                .importId(dto.getImportId())
                .minQuantity(dto.getMinQuantity())
                .deleted(dto.getDeleted())
                .build();
    }
    
    @Override
    protected void updateEntityFromDTO(StoreStockEntity entity, StoreStockDTO dto) {
        entity.setQuantity(dto.getQuantity());
        entity.setMinQuantity(dto.getMinQuantity());
    }
    
    public List<StoreStockDTO> findByStore(Long storeId) {
        return storeStockRepository.findByStoreIdWithDetails(storeId).stream()
                .map(this::convertToDTO)
                .toList();
    }
    
    public List<StoreStockDTO> findByProduct(Long productId) {
        return storeStockRepository.findByProductIdAndDeletedFalse(productId).stream()
                .map(this::convertToDTO)
                .toList();
    }
    
    public List<StoreStockDTO> findLowStockItems() {
        return storeStockRepository.findLowStockItems().stream()
                .map(this::convertToDTO)
                .toList();
    }
    
    @Transactional
    public StoreStockDTO updateQuantity(Long storeId, Long productId, Integer newQuantity) {
        StoreStockEntity stock = storeStockRepository.findByStoreIdAndProductIdAndDeletedFalse(storeId, productId)
                .orElseThrow(() -> new NotFoundException("Stock not found for store " + storeId + " and product " + productId));
        
        stock.setQuantity(newQuantity);
        StoreStockEntity updatedStock = storeStockRepository.save(stock);
        return convertToDTO(updatedStock);
    }
    
    @Transactional
    public StoreStockDTO updateMinQuantity(Long storeId, Long productId, Integer minQuantity) {
        StoreStockEntity stock = storeStockRepository.findByStoreIdAndProductIdAndDeletedFalse(storeId, productId)
                .orElseThrow(() -> new NotFoundException("Stock not found for store " + storeId + " and product " + productId));
        
        stock.setMinQuantity(minQuantity);
        StoreStockEntity updatedStock = storeStockRepository.save(stock);
        return convertToDTO(updatedStock);
    }
}