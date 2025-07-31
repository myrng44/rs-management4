package develop.circlek.base.web;

import develop.circlek.base.application.dto.ApiResponse;
import develop.circlek.base.application.dto.BaseDTO;
import develop.circlek.base.application.service.BaseService;
import develop.circlek.base.domain.entity.BaseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.io.Serializable;
import java.util.List;

public abstract class BaseController<T extends BaseEntity<ID>, D extends BaseDTO, ID extends Serializable> {
    
    protected abstract BaseService<T, D, ID> getService();
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<D>> getById(@PathVariable ID id) {
        try {
            D dto = getService().findById(id);
            return ResponseEntity.ok(ApiResponse.success(dto));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        }
    }
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<D>>> getAll() {
        try {
            List<D> dtos = getService().findAll();
            return ResponseEntity.ok(ApiResponse.success(dtos));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(500, "Internal server error"));
        }
    }
    
    @GetMapping("/page")
    public ResponseEntity<ApiResponse<Page<D>>> getAll(Pageable pageable) {
        try {
            Page<D> dtos = getService().findAll(pageable);
            return ResponseEntity.ok(ApiResponse.success(dtos));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(500, "Internal server error"));
        }
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<D>> create(@Valid @RequestBody D dto) {
        try {
            D createdDto = getService().save(dto);
            return ResponseEntity.ok(ApiResponse.success(createdDto));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<D>> update(@PathVariable ID id, @Valid @RequestBody D dto) {
        try {
            D updatedDto = getService().update(id, dto);
            return ResponseEntity.ok(ApiResponse.success(updatedDto));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable ID id) {
        try {
            getService().deleteById(id);
            return ResponseEntity.ok(ApiResponse.success("Deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        }
    }
}