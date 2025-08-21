package ck4.nvb.rsmanagement.core.module.dashboard;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/${rs.api.main.baseUrl}/summary")
@Getter
@RequiredArgsConstructor
public class DashboardController {
  private final DashboardServiceImpl dashboardService;

  @GetMapping
  public ResponseEntity<DashboardSummaryDto> getDashboardSummary() {
    return ResponseEntity.ok(getDashboardService().getDashboardSummary());
  }
}
