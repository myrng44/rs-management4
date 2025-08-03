package develop.circlek.core.dashboard.web;

import develop.circlek.base.application.dto.ApiResponse;
import develop.circlek.core.dashboard.application.dto.DashboardSummaryDTO;
import develop.circlek.core.dashboard.application.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/summary")
    public ResponseEntity<ApiResponse<DashboardSummaryDTO>> getSummary() {
        DashboardSummaryDTO summary = dashboardService.getSummary();
        return ResponseEntity.ok(ApiResponse.success(summary));
    }
}