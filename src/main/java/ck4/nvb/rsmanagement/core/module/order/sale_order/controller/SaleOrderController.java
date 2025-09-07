package ck4.nvb.rsmanagement.core.module.order.sale_order.controller;

import ck4.nvb.rsmanagement.base.application.dto.FilterInput;
import ck4.nvb.rsmanagement.base.web.controller.AuditedCrudController;
import ck4.nvb.rsmanagement.base.web.controller.api.ApiResponse;
import ck4.nvb.rsmanagement.base.web.controller.api.PageResponse;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/${rs.api.main.baseUrl}/orders")
@CrossOrigin(origins = "http://localhost:5173")
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

    if (principal instanceof UserRoleDto) {
      UserRoleDto userRoleDto = (UserRoleDto) principal;
      UserGetDto userGetDto = new UserGetDto();
      userGetDto.setId(userRoleDto.getUserId());
      userGetDto.setStoreId(userRoleDto.getStoreId());
      userGetDto.setUserName(userRoleDto.getUserName());
      return userGetDto;
    }


    if (principal instanceof UserGetDto) {
      return (UserGetDto) principal;
    }
    return null;
  }

  @GetMapping("/most")
  public ResponseEntity<ApiResponse<List<ProductGetDto.WithSales>>> getMostSoldProductsLastDay(
      Authentication auth, @RequestParam int days, @RequestParam int noProducts) {
    UserGetDto user = extractUser(auth);
    List<ProductGetDto.WithSales> products = saleLineService.getMostSoldProductsLastDay(days, noProducts);
    return ResponseEntity.ok(ApiResponse.success(products));
  }

  @PostMapping
  @Override
  public ResponseEntity<ApiResponse<SaleOrderGetFullDto>> create(
      Authentication auth, @RequestBody SaleOrderCreateDto entity) {
    UserGetDto user = extractUser(auth);
    SaleOrderGetFullDto output = getService().create(entity, user);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(
            ApiResponse.<SaleOrderGetFullDto>builder()
                .code(201)
                .message("Create order success.")
                .data(output)
                .build());
  }


  @DeleteMapping("/{orderId}")
  @Override
  public ResponseEntity<ApiResponse<Void>> delete(Authentication auth, @PathVariable String orderId) {
    return super.delete(auth, orderId);
  }

  @GetMapping
  @Override
  public ResponseEntity<ApiResponse<PageResponse<SaleOrderGetFullDto>>> getList(
          Authentication auth,
          @RequestParam(required = false, name = "query") List<String> query,
          @RequestParam(required = false, name = "sort") String sort,
          @RequestParam(required = false, name = "offset", defaultValue = "0") int offset,
          @RequestParam(required = false, name = "limit", defaultValue = "20") int limit) {
    return super.getList(auth, query, sort, offset, limit);
  }

  @GetMapping("/{orderId}")
  @Override
  public ResponseEntity<ApiResponse<SaleOrderGetFullDto>> getById(
          Authentication auth, @PathVariable String orderId) {
    return super.getById(auth, orderId);
  }

  @Override
  public ResponseEntity<ApiResponse<PageResponse<SaleOrderGetFullDto>>> getList(
          Authentication auth, FilterInput request) {
    return super.getList(auth, request);
  }

  @GetMapping("/{orderId}/fulfillment/status")
  public ResponseEntity<ApiResponse<OrderFulfillmentStatusDto>> getFulfillmentStatus(
      Authentication auth, @PathVariable String orderId) {

    OrderFulfillmentService.OrderFulfillmentStatus status =
        fulfillmentService.getOrderFulfillmentStatus(orderId);

    OrderFulfillmentStatusDto statusDto = new OrderFulfillmentStatusDto();
    statusDto.setOrderId(orderId);
    statusDto.setStatus(status);
    statusDto.setCanFulfill(fulfillmentService.canFulfillOrder(orderId));

    return ResponseEntity.ok(ApiResponse.success(statusDto));
  }

  @GetMapping("/{orderId}/allocations")
  public ResponseEntity<ApiResponse<List<SaleAllocationDto>>> getOrderAllocations(
      Authentication auth, @PathVariable String orderId) {

    List<SaleAllocationDto> allocations = fulfillmentService.getOrderAllocationDetails(orderId);
    return ResponseEntity.ok(ApiResponse.success(allocations));
  }

  @PostMapping("/{orderId}/fulfillment/pick")
  public ResponseEntity<ApiResponse<String>> pickOrderItems(
      Authentication auth, @PathVariable String orderId) {

    UserGetDto user = extractUser(auth);

    try {
      List<SaleLineGetDto> saleLines =
          saleLineService.getAll(
              List.of(
                  new ck4.nvb.rsmanagement.base.web.utils.SearchCriteria(
                      "saleOrderId",
                      ck4.nvb.rsmanagement.base.web.utils.SearchOperator.EQUALS,
                      orderId)));

      for (SaleLineGetDto saleLine : saleLines) {
        fulfillmentService.pickItemsForSaleLine(saleLine.getId(), user);
      }

      return ResponseEntity.ok(
          ApiResponse.<String>builder()
              .code(200)
              .message("All items for order " + orderId + " have been picked.")
              .data("Order items picked successfully")
              .build());

    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(ApiResponse.error(500, "Failed to pick order items: " + e.getMessage()));
    }
  }

  @PostMapping("/lines/{saleLineId}/pick")
  public ResponseEntity<ApiResponse<String>> pickSaleLineItems(
      Authentication auth,
      @PathVariable Long saleLineId,
      @RequestBody PickItemsRequestDto request) {

    UserGetDto user = extractUser(auth);

    try {
      if (request.getQtyToPick() != null) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(
                ApiResponse.error(
                    400, "Partial picking not yet implemented. Use full pick instead."));
      } else {
        fulfillmentService.pickItemsForSaleLine(saleLineId, user);
        return ResponseEntity.ok(
            ApiResponse.<String>builder()
                .code(200)
                .message("All items for sale line " + saleLineId + " have been picked.")
                .data("Sale line items picked successfully")
                .build());
      }
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(ApiResponse.error(500, "Failed to pick sale line items: " + e.getMessage()));
    }
  }

  @PostMapping("/{orderId}/fulfillment/complete")
  public ResponseEntity<ApiResponse<String>> completeOrderFulfillment(
      Authentication auth, @PathVariable String orderId) {

    UserGetDto user = extractUser(auth);

    try {
      if (!fulfillmentService.canFulfillOrder(orderId)) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(
                ApiResponse.error(
                    400, "Order cannot be completed. Some items are not yet picked."));
      }

      fulfillmentService.completeOrderFulfillment(orderId, user);
      return ResponseEntity.ok(
          ApiResponse.<String>builder()
              .code(200)
              .message("Order " + orderId + " has been completed and inventory updated.")
              .data("Order fulfillment completed")
              .build());

    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(ApiResponse.error(500, "Failed to complete order fulfillment: " + e.getMessage()));
    }
  }

  @PostMapping("/{orderId}/fulfillment/cancel")
  public ResponseEntity<ApiResponse<String>> cancelOrder(
      Authentication auth, @PathVariable String orderId) {

    UserGetDto user = extractUser(auth);

    try {
      fulfillmentService.cancelOrder(orderId, user);
      return ResponseEntity.ok(
          ApiResponse.<String>builder()
              .code(200)
              .message("Order " + orderId + " has been cancelled and inventory released.")
              .data("Order cancelled successfully")
              .build());

    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(ApiResponse.error(500, "Failed to cancel order: " + e.getMessage()));
    }
  }

  @PostMapping("/allocations/{allocationId}/pick")
  public ResponseEntity<ApiResponse<String>> partialPickFromAllocation(
      Authentication auth,
      @PathVariable Long allocationId,
      @RequestBody PartialPickRequestDto request) {

    UserGetDto user = extractUser(auth);

    try {
      fulfillmentService.partialPick(allocationId, request.getQtyToPick(), user);
      return ResponseEntity.ok(
          ApiResponse.<String>builder()
              .code(200)
              .message(
                  "Picked " + request.getQtyToPick() + " items from allocation " + allocationId)
              .data("Partial pick completed")
              .build());

    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(ApiResponse.error(500, "Failed to partial pick: " + e.getMessage()));
    }
  }
}
