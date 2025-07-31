package develop.circlek.core.inventory.application.service;

import develop.circlek.base.application.service.BaseService;
import develop.circlek.base.domain.repository.BaseRepository;
import develop.circlek.core.inventory.domain.entity.StockEntity;
import develop.circlek.core.inventory.domain.repository.StockRepository;
import develop.circlek.core.inventory.application.dto.StockDTO;
import develop.circlek.base.application.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockService extends BaseService<StockEntity, StockDTO, Long> {
    
    private final StockRepository stockRepository;
    
    @Override
    protected BaseRepository<StockEntity, Long> getRepository() {
        return stockRepository;
    }
    
    @Override
    protected StockDTO convertToDTO(StockEntity entity) {
        return StockDTO.builder()
                .name(entity.getName())
                .location(entity.getLocation())
                .deleted(entity.getDeleted())
                .build();
    }
    
    @Override
    protected StockEntity convertToEntity(StockDTO dto) {
        return StockEntity.builder()
                .name(dto.getName())
                .location(dto.getLocation())
                .deleted(dto.getDeleted())
                .build();
    }
    
    @Override
    protected void updateEntityFromDTO(StockEntity entity, StockDTO dto) {
        entity.setName(dto.getName());
        entity.setLocation(dto.getLocation());
    }
    
    public List<StockDTO> findActiveStocks() {
        return stockRepository.findByDeletedFalse().stream()
                .map(this::convertToDTO)
                .toList();
    }
    
    @Transactional
    public void softDelete(Long id) {
        StockEntity stock = stockRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Stock not found with id: " + id));
        stock.setDeleted(true);
        stockRepository.save(stock);
    }
}