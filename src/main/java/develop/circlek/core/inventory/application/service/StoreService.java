package develop.circlek.core.inventory.application.service;

import develop.circlek.base.application.service.BaseService;
import develop.circlek.base.domain.repository.BaseRepository;
import develop.circlek.core.inventory.application.dto.SupplierDTO;
import develop.circlek.core.inventory.application.dto.request.CreateStoreRequest;
import develop.circlek.core.inventory.application.dto.request.CreateSupplierRequest;
import develop.circlek.core.inventory.domain.entity.StoreEntity;
import develop.circlek.core.inventory.domain.entity.SupplierEntity;
import develop.circlek.core.inventory.domain.repository.StoreRepository;
import develop.circlek.core.inventory.application.dto.StoreDTO;
import develop.circlek.base.application.exception.NotFoundException;
import develop.circlek.base.application.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.Store;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StoreService extends BaseService<StoreEntity, StoreDTO, Long> {
    
    private final StoreRepository storeRepository;
    
    @Override
    protected BaseRepository<StoreEntity, Long> getRepository() {
        return storeRepository;
    }
    
    @Override
    protected StoreDTO convertToDTO(StoreEntity entity) {
        return StoreDTO.builder()
                .id(entity.getId())
                .createAt(LocalDateTime.now())
                .createBy(getCurrentUserId())
                .name(entity.getName())
                .address(entity.getAddress())
                .phone(entity.getPhone())
                .isActive(entity.getIsActive())
                .deleted(entity.getDeleted())
                .build();
    }
    
    @Override
    protected StoreEntity convertToEntity(StoreDTO dto) {
        return StoreEntity.builder()
                .name(dto.getName())
                .address(dto.getAddress())
                .phone(dto.getPhone())
                .isActive(dto.getIsActive())
                .deleted(dto.getDeleted())
                .build();
    }
    
    @Override
    protected void updateEntityFromDTO(StoreEntity entity, StoreDTO dto) {
        entity.setName(dto.getName());
        entity.setAddress(dto.getAddress());
        entity.setPhone(dto.getPhone());
        entity.setIsActive(dto.getIsActive());
    }

    @Transactional
    public StoreDTO createStore(CreateStoreRequest request) {

        StoreEntity store = StoreEntity.builder()
                .name(request.getName())
                .address(request.getAddress())
                .phone(request.getPhone())
                .deleted(false)
                .build();

        StoreEntity storeEntity = storeRepository.save(store);
        return convertToDTO(storeEntity);
    }


    public List<StoreDTO> findActiveStores() {
        return storeRepository.findByIsActiveTrueAndDeletedFalse().stream()
                .map(this::convertToDTO)
                .toList();
    }
    
    @Transactional
    public void softDelete(Long id) {
        StoreEntity store = storeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Store not found with id: " + id));
        store.setDeleted(true);
        storeRepository.save(store);
    }
    
    @Transactional
    public void deactivateStore(Long id) {
        StoreEntity store = storeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Store not found with id: " + id));
        store.setIsActive(false);
        storeRepository.save(store);
    }
    
    @Transactional
    public void activateStore(Long id) {
        StoreEntity store = storeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Store not found with id: " + id));
        store.setIsActive(true);
        storeRepository.save(store);
    }
}
