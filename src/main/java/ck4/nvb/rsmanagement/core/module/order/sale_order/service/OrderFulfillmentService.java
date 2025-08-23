package ck4.nvb.rsmanagement.core.module.order.sale_order.service;

import ck4.nvb.rsmanagement.base.application.exception.AppException;
import ck4.nvb.rsmanagement.base.web.utils.SearchCriteria;
import ck4.nvb.rsmanagement.base.web.utils.SearchOperator;
import ck4.nvb.rsmanagement.core.module.order.sale_order.domain.SaleAllocation;
import ck4.nvb.rsmanagement.core.module.order.sale_order.domain.SaleAllocationRepository;
import ck4.nvb.rsmanagement.core.module.order.sale_order.service.dto.SaleAllocationDto;
import ck4.nvb.rsmanagement.core.module.stores.batch_stock.domain.BatchStock;
import ck4.nvb.rsmanagement.core.module.stores.batch_stock.domain.BatchStockRepository;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service("orderFullfillmentService")
public class OrderFulfillmentService {
    @Autowired
    private ISaleAllocationService saleAllocationService;

    @Autowired
    private SaleAllocationRepository saleAllocationRepository;

    @Autowired
    private BatchStockRepository batchStockRepository;

    /**
     * Pick items for a sale line - update picked quantities
     */
    @Transactional
    public void pickItemsForSaleLine(Long saleLineId, UserGetDto user) throws AppException {
        List<SaleAllocationDto> allocations = saleAllocationService.getAll(
                List.of(new SearchCriteria("saleLineId", SearchOperator.EQUALS, saleLineId.toString())));

        for (SaleAllocationDto allocation : allocations) {
            if (allocation.getQtyPicked() < allocation.getQtyAllocated()) {
                // Update picked quantity to match allocated quantity
                allocation.setQtyPicked(allocation.getQtyAllocated());
                saleAllocationService.update(allocation.getId(), allocation, user);

                log.info("Picked {} items from allocation {} for sale line {}",
                        allocation.getQtyAllocated(), allocation.getId(), saleLineId);
            }
        }
    }

    /**
     * Complete order fulfillment - finalize all allocations and update inventory
     */
    @Transactional
    public void completeOrderFulfillment(String orderId, UserGetDto user) throws AppException {
        // Get all allocations for this order
        List<SaleAllocation> orderAllocations = saleAllocationRepository
                .findAllocationsByOrderId(orderId);

        for (SaleAllocation allocation : orderAllocations) {
            if (allocation.getQtyPicked() > 0) {
                // Update batch stock - reduce total quantity and reserved quantity
                BatchStock batchStock = batchStockRepository.getReferenceById(allocation.getBatchStockId());

                // Reduce total quantity by picked amount
                batchStock.setQtyTotal(batchStock.getQtyTotal() - allocation.getQtyPicked());

                // Reduce reserved quantity by picked amount
                batchStock.setQtyReversed(batchStock.getQtyReversed() - allocation.getQtyPicked());

                // Update status if stock is depleted
                if (batchStock.getQtyTotal() <= 0) {
                    batchStock.setStatus("SOLD_OUT");
                    batchStock.setQtyAvailable(0);
                    batchStock.setQtyTotal(0);
                } else if (batchStock.getQtyReversed() == 0 && batchStock.getQtyAvailable() > 0) {
                    batchStock.setStatus("ACTIVE");
                }

                batchStock.setVersion(batchStock.getVersion() + 1);
                batchStock.setUpdatedTime(LocalDateTime.now());
                batchStock.setUpdaterID(user.getId());

                batchStockRepository.save(batchStock);

                log.info("Completed fulfillment for allocation {}. Updated batch stock {}",
                        allocation.getId(), batchStock.getId());
            }
        }
    }

    /**
     * Cancel order - release reserved inventory
     */
    @Transactional
    public void cancelOrder(String orderId, UserGetDto user) throws AppException {
        List<SaleAllocation> orderAllocations = saleAllocationRepository
                .findAllocationsByOrderId(orderId);

        for (SaleAllocation allocation : orderAllocations) {
            if (allocation.getQtyPicked() == 0) { // Only cancel unpicked allocations
                // Release reserved inventory back to available
                BatchStock batchStock = batchStockRepository.getReferenceById(allocation.getBatchStockId());

                batchStock.setQtyAvailable(batchStock.getQtyAvailable() + allocation.getQtyAllocated());
                batchStock.setQtyReversed(batchStock.getQtyReversed() - allocation.getQtyAllocated());

                if (batchStock.getQtyAvailable() > 0) {
                    batchStock.setStatus("ACTIVE");
                }

                batchStock.setVersion(batchStock.getVersion() + 1);
                batchStock.setUpdatedTime(LocalDateTime.now());
                batchStock.setUpdaterID(user.getId());

                batchStockRepository.save(batchStock);

                // Mark allocation as cancelled (you might want to add a status field)
                allocation.setDeleted(true);
                allocation.setUpdatedTime(LocalDateTime.now());
                allocation.setUpdaterID(user.getId());

                saleAllocationRepository.save(allocation);

                log.info("Cancelled allocation {} and released {} units back to inventory",
                        allocation.getId(), allocation.getQtyAllocated());
            }
        }
    }

    /**
     * Get inventory allocation details for an order
     */
    public List<SaleAllocationDto> getOrderAllocationDetails(String orderId) throws AppException {
        return saleAllocationService.getAll(
                List.of(new SearchCriteria("saleOrderId", SearchOperator.EQUALS, orderId)));
    }

    /**
     * Partial picking - pick specific quantities from allocations
     */
    @Transactional
    public void partialPick(Long allocationId, Integer qtyToPick, UserGetDto user) throws AppException {
        SaleAllocationDto allocation = saleAllocationService.get(allocationId);

        if (qtyToPick > (allocation.getQtyAllocated() - allocation.getQtyPicked())) {
            throw new AppException("Cannot pick more than allocated quantity");
        }

        allocation.setQtyPicked(allocation.getQtyPicked() + qtyToPick);
        saleAllocationService.update(allocationId, allocation, user);

        log.info("Partially picked {} items from allocation {}", qtyToPick, allocationId);
    }

    /**
     * Check if order can be fulfilled completely
     */
    public boolean canFulfillOrder(String orderId) {
        List<SaleAllocation> allocations = saleAllocationRepository.findAllocationsByOrderId(orderId);

        return allocations.stream()
                .allMatch(allocation -> allocation.getQtyPicked().equals(allocation.getQtyAllocated()));
    }

    /**
     * Get fulfillment status for an order
     */
    public OrderFulfillmentStatus getOrderFulfillmentStatus(String orderId) {
        List<SaleAllocation> allocations = saleAllocationRepository.findAllocationsByOrderId(orderId);

        if (allocations.isEmpty()) {
            return OrderFulfillmentStatus.NOT_ALLOCATED;
        }

        boolean fullyPicked = allocations.stream()
                .allMatch(allocation -> allocation.getQtyPicked().equals(allocation.getQtyAllocated()));

        boolean partiallyPicked = allocations.stream()
                .anyMatch(allocation -> allocation.getQtyPicked() > 0);

        if (fullyPicked) {
            return OrderFulfillmentStatus.FULLY_PICKED;
        } else if (partiallyPicked) {
            return OrderFulfillmentStatus.PARTIALLY_PICKED;
        } else {
            return OrderFulfillmentStatus.ALLOCATED_NOT_PICKED;
        }
    }

    /**
     * Enum for order fulfillment status
     */
    public enum OrderFulfillmentStatus {
        NOT_ALLOCATED,
        ALLOCATED_NOT_PICKED,
        PARTIALLY_PICKED,
        FULLY_PICKED
    }
}
