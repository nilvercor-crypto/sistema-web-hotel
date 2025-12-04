package com.acuario.hotel.controller;

import com.acuario.hotel.dto.DashboardStatsDTO;
import com.acuario.hotel.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "dashboard-controller", description = "Estadísticas y métricas del sistema")
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @Operation(
        summary = "Obtener estadísticas del dashboard",
        description = "Retorna estadísticas generales: reservas, ingresos, habitaciones, clientes, etc."
    )
    @ApiResponse(
        responseCode = "200",
        description = "Estadísticas obtenidas correctamente",
        content = @Content(schema = @Schema(implementation = DashboardStatsDTO.class))
    )
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'USUARIO')")
    @GetMapping("/estadisticas")
    public DashboardStatsDTO obtenerEstadisticas() {
        return dashboardService.obtenerEstadisticas();
    }
}

