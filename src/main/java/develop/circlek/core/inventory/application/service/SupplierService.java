package develop.circlek.core.inventory.application.service;

import develop.circlek.base.application.service.BaseService;
import develop.circlek.base.domain.repository.BaseRepository;
import develop.circlek.core.inventory.application.dto.CategoryDTO;
import develop.circlek.core.inventory.application.dto.request.CreateCategoryRequest;
import develop.circlek.core.inventory.application.dto.request.CreateSupplierRequest;
import develop.circlek.core.inventory.domain.entity.CategoryEntity;
import develop.circlek.core.inventory.domain.entity.SupplierEntity;
import develop.circlek.core.inventory.domain.repository.SupplierRepository;
import develop.circlek.core.inventory.application.dto.SupplierDTO;
import develop.circlek.base.application.exception.NotFoundException;
import develop.circlek.base.application.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SupplierService extends BaseService<SupplierEntity, SupplierDTO, Long> {
    
    private final SupplierRepository supplierRepository;
    
    @Override
    protected BaseRepository<SupplierEntity, Long> getRepository() {
        return supplierRepository;
    }
    
    @Override
    protected SupplierDTO convertToDTO(SupplierEntity entity) {
        return SupplierDTO.builder()
                .id(entity.getId())
                .createAt(LocalDateTime.now())
                .createBy(getCurrentUserId())
                .name(entity.getName())
                .address(entity.getAddress())
                .contact(entity.getContact())
                .deleted(entity.getDeleted())
                .build();
    }
    
    @Override
    protected SupplierEntity convertToEntity(SupplierDTO dto) {
        return SupplierEntity.builder()
                .name(dto.getName())
                .address(dto.getAddress())
                .contact(dto.getContact())
                .deleted(dto.getDeleted())
                .build();
    }
    
    @Override
    protected void updateEntityFromDTO(SupplierEntity entity, SupplierDTO dto) {
        entity.setName(dto.getName());
        entity.setAddress(dto.getAddress());
        entity.setContact(dto.getContact());
    }
    
    public List<SupplierDTO> findActiveSuppliers() {
        return supplierRepository.findByDeletedFalse().stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Transactional
    public SupplierDTO createSupplier(CreateSupplierRequest request) {

        SupplierEntity category = SupplierEntity.builder()
                .name(request.getName())
                .address(request.getAddress())
                .contact(request.getContact())
                .deleted(false)
                .build();

        SupplierEntity savedCategory = supplierRepository.save(category);
        return convertToDTO(savedCategory);
    }

    @Transactional
    public void softDelete(Long id) {
        SupplierEntity supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Supplier not found with id: " + id));
        supplier.setDeleted(true);
        supplierRepository.save(supplier);
    }
}