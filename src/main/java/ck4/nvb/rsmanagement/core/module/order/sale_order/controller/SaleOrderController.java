package ck4.nvb.rsmanagement.core.module.order.sale_order.controller;

import ck4.nvb.rsmanagement.base.web.controller.AuditedCrudController;
import ck4.nvb.rsmanagement.base.web.controller.api.response.APIListResponse;
import ck4.nvb.rsmanagement.base.web.controller.api.response.APIResponse;
import ck4.nvb.rsmanagement.base.web.controller.api.response.APIResponseBuilder;
import ck4.nvb.rsmanagement.base.web.error.ErrorCode;
import ck4.nvb.rsmanagement.core.module.order.sale_order.domain.SaleOrder;
import ck4.nvb.rsmanagement.core.module.order.sale_order.service.ISaleLineService;
import ck4.nvb.rsmanagement.core.module.order.sale_order.service.ISaleOrderService;
import ck4.nvb.rsmanagement.core.module.order.sale_order.service.OrderFulfillmentService;
import ck4.nvb.rsmanagement.core.module.order.sale_order.service.dto.*;
import ck4.nvb.rsmanagement.core.module.stores.product.service.dto.ProductGetDto;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;
import ck4.nvb.rsmanagement.core.module.users.userrole.service.dto.UserRoleDto;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/${rs.api.main.baseUrl}/orders")
public class SaleOrderController
    extends AuditedCrudController<
        SaleOrderGetFullDto,
        SaleOrder,
        String,
        UserGetDto,
        Long,
        SaleOrderCreateDto,
        SaleOrderUpdateDto> {

  @Autowired private ISaleLineService saleLineService;
  @Autowired private OrderFulfillmentService fulfillmentService;
  @Autowired private ModelMapper modelMapper;

  public SaleOrderController(ISaleOrderService service) {
    super(service);
  }

  @Override
  public UserGetDto extractUser(Authentication auth) {
    if (auth == null || !auth.isAuthenticated()) {
      return null;
    }

    Object principal = auth.getPrincipal();
    if (principal instanceof UserGetDto) {
      return (UserGetDto) principal;
    }

    if (principal instanceof UserRoleDto) {
      UserRoleDto userRoleDto = (UserRoleDto) principal;
      UserGetDto userGetDto = new UserGetDto();
      userGetDto.setId(userRoleDto.getUserId());
      userGetDto.setUserName(userRoleDto.getUserName());
      return userGetDto;
    }
    return null;
  }

  @GetMapping("/most")
  public List<ProductGetDto> getMostSoldProductsLastDay(
      Authentication auth, @RequestParam int days, @RequestParam int noProducts) {
    UserGetDto user = extractUser(auth);

    return saleLineService.getMostSoldProductsLastDay(days, noProducts);
  }

  @PostMapping
  @Override
  public APIResponse<SaleOrderGetFullDto> create(Authentication auth, SaleOrderCreateDto entity) {
    UserGetDto user = extractUser(auth);

    SaleOrderGetFullDto output = getService().create(entity, user);

    return APIResponseBuilder.success(output, "create order success.");
  }

  // ===== FULFILLMENT ENDPOINTS =====

  /**
   * Get fulfillment status of an order
   * GET /orders/{orderId}/fulfillment/status
   */
  @GetMapping("/{orderId}/fulfillment/status")
  public APIResponse<OrderFulfillmentStatusDto> getFulfillmentStatus(
          Authentication auth,
          @PathVariable String orderId) {

    OrderFulfillmentService.OrderFulfillmentStatus status =
            fulfillmentService.getOrderFulfillmentStatus(orderId);

    OrderFulfillmentStatusDto statusDto = new OrderFulfillmentStatusDto();
    statusDto.setOrderId(orderId);
    statusDto.setStatus(status);
    statusDto.setCanFulfill(fulfillmentService.canFulfillOrder(orderId));

    return APIResponseBuilder.ok(statusDto);
  }

  /**
   * Get detailed allocation information for an order
   * GET /orders/{orderId}/allocations
   */
  @GetMapping("/{orderId}/allocations")
  public APIListResponse<List<SaleAllocationDto>> getOrderAllocations(
          Authentication auth,
          @PathVariable String orderId) {

    List<SaleAllocationDto> allocations = fulfillmentService.getOrderAllocationDetails(orderId);
    return APIResponseBuilder.successList(allocations, 0, 10, allocations.size(), "allocations");
  }

  /**
   * Pick all items for an order
   * POST /orders/{orderId}/fulfillment/pick
   */
  @PostMapping("/{orderId}/fulfillment/pick")
  public APIResponse<String> pickOrderItems(
          Authentication auth,
          @PathVariable String orderId) {

    UserGetDto user = extractUser(auth);

    // Get all sale lines for this order and pick them
    try {
      // This is a simplified approach - pick all lines at once
      // In reality, you might want to pick line by line
      List<SaleLineGetDto> saleLines = saleLineService.getAll(
              List.of(new ck4.nvb.rsmanagement.base.web.utils.SearchCriteria(
                      "saleOrderId",
                      ck4.nvb.rsmanagement.base.web.utils.SearchOperator.EQUALS,
                      orderId)));

      for (SaleLineGetDto saleLine : saleLines) {
        fulfillmentService.pickItemsForSaleLine(saleLine.getId(), user);
      }

      return APIResponseBuilder.success("Order items picked successfully",
              "All items for order " + orderId + " have been picked.");

    } catch (Exception e) {
      return APIResponseBuilder.error(ErrorCode.INTERNAL_SERVER_ERROR,"Failed to pick order items: " + e.getMessage());
    }
  }

  /**
   * Pick specific quantity from a sale line
   * POST /orders/lines/{saleLineId}/pick
   */
  @PostMapping("/lines/{saleLineId}/pick")
  public APIResponse<String> pickSaleLineItems(
          Authentication auth,
          @PathVariable Long saleLineId,
          @RequestBody PickItemsRequestDto request) {

    UserGetDto user = extractUser(auth);

    try {
      if (request.getQtyToPick() != null) {
        // Partial pick - this would require more complex logic to determine which allocation to pick from
        return APIResponseBuilder.error(ErrorCode.INTERNAL_SERVER_ERROR,"Partial picking not yet implemented. Use full pick instead.");
      } else {
        // Full pick
        fulfillmentService.pickItemsForSaleLine(saleLineId, user);
        return APIResponseBuilder.success("Sale line items picked successfully",
                "All items for sale line " + saleLineId + " have been picked.");
      }
    } catch (Exception e) {
      return APIResponseBuilder.error(ErrorCode.INTERNAL_SERVER_ERROR,"Failed to pick sale line items: " + e.getMessage());
    }
  }

  /**
   * Complete order fulfillment (finalize and ship)
   * POST /orders/{orderId}/fulfillment/complete
   */
  @PostMapping("/{orderId}/fulfillment/complete")
  public APIResponse<String> completeOrderFulfillment(
          Authentication auth,
          @PathVariable String orderId) {

    UserGetDto user = extractUser(auth);

    try {
      // Check if order is ready to be completed
      if (!fulfillmentService.canFulfillOrder(orderId)) {
        return APIResponseBuilder.error(ErrorCode.INTERNAL_SERVER_ERROR,"Order cannot be completed. Some items are not yet picked.");
      }

      fulfillmentService.completeOrderFulfillment(orderId, user);
      return APIResponseBuilder.success("Order fulfillment completed",
              "Order " + orderId + " has been completed and inventory updated.");

    } catch (Exception e) {
      return APIResponseBuilder.error(ErrorCode.INTERNAL_SERVER_ERROR,"Failed to complete order fulfillment: " + e.getMessage());
    }
  }

  /**
   * Cancel an order (release allocated inventory)
   * POST /orders/{orderId}/fulfillment/cancel
   */
  @PostMapping("/{orderId}/fulfillment/cancel")
  public APIResponse<String> cancelOrder(
          Authentication auth,
          @PathVariable String orderId) {

    UserGetDto user = extractUser(auth);

    try {
      fulfillmentService.cancelOrder(orderId, user);
      return APIResponseBuilder.success("Order cancelled successfully",
              "Order " + orderId + " has been cancelled and inventory released.");

    } catch (Exception e) {
      return APIResponseBuilder.error(ErrorCode.INTERNAL_SERVER_ERROR,"Failed to cancel order: " + e.getMessage());
    }
  }

  /**
   * Partial pick from specific allocation
   * POST /allocations/{allocationId}/pick
   */
  @PostMapping("/allocations/{allocationId}/pick")
  public APIResponse<String> partialPickFromAllocation(
          Authentication auth,
          @PathVariable Long allocationId,
          @RequestBody PartialPickRequestDto request) {

    UserGetDto user = extractUser(auth);

    try {
      fulfillmentService.partialPick(allocationId, request.getQtyToPick(), user);
      return APIResponseBuilder.success("Partial pick completed",
              "Picked " + request.getQtyToPick() + " items from allocation " + allocationId);

    } catch (Exception e) {
      return APIResponseBuilder.error(ErrorCode.INTERNAL_SERVER_ERROR,"Failed to partial pick: " + e.getMessage());
    }
  }
}
