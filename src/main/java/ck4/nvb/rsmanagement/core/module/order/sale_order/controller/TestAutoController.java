// package ck4.nvb.rsmanagement.core.module.order.sale_order.controller;
//
// import ck4.nvb.rsmanagement.core.module.order.sale_order.service.SaleOrderAutoGenerator;
// import java.time.LocalDate;
// import org.springframework.format.annotation.DateTimeFormat;
// import org.springframework.web.bind.annotation.*;
//
// @RestController
// @RequestMapping("/auto/test-orders")
// public class TestAutoController {
//
//  private final SaleOrderAutoGenerator generator;
//
//  public TestAutoController(SaleOrderAutoGenerator generator) {
//    this.generator = generator;
//  }
//
//  @GetMapping("/generate")
//  public String generate(
//      @RequestParam(value = "date", required = false) @DateTimeFormat(iso =
// DateTimeFormat.ISO.DATE)
//          LocalDate date,
//      @RequestParam(value = "count", required = false) Integer count) {
//    LocalDate target = (date == null) ? LocalDate.now().plusDays(1) : date;
//    int total = (count == null || count <= 0) ? this.getDefaultTotal() : count;
//
//    try {
//      generator.generateOrdersForDateWithTotal(target, total);
//      return "Triggered generateOrdersForDate for " + target + " with total=" + total;
//    } catch (NoSuchMethodError | UnsupportedOperationException ex) {
//      generator.generateOrdersForDate(target);
//      return "Triggered generateOrdersForDate for "
//          + target
//          + " with service-configured total (count param not applied).";
//    } catch (Exception ex) {
//      return "Failed to trigger generation: " + ex.getMessage();
//    }
//  }
//
//  private int getDefaultTotal() {
//    return 10;
//  }
// }
