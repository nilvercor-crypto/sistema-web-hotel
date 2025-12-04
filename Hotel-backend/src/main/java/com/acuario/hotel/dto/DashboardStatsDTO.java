package com.acuario.hotel.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class DashboardStatsDTO {
    private Long totalReservasHoy;
    private Long totalReservasMes;
    private Long totalReservasAnio;
    private BigDecimal ingresosTotales;
    private BigDecimal ingresosMes;
    private BigDecimal ingresosAnio;
    private Long habitacionesOcupadas;
    private Long habitacionesDisponibles;
    private Long habitacionesMantenimiento;
    private Long reservasPendientes;
    private Long reservasConfirmadas;
    private Long reservasCanceladas;
    private Long reservasFinalizadas;
    private Long totalClientes;
    private Long totalHabitaciones;
    private Long totalServicios;

    public DashboardStatsDTO() {
    }

    public Long getTotalReservasHoy() {
        return totalReservasHoy;
    }

    public void setTotalReservasHoy(Long totalReservasHoy) {
        this.totalReservasHoy = totalReservasHoy;
    }

    public Long getTotalReservasMes() {
        return totalReservasMes;
    }

    public void setTotalReservasMes(Long totalReservasMes) {
        this.totalReservasMes = totalReservasMes;
    }

    public Long getTotalReservasAnio() {
        return totalReservasAnio;
    }

    public void setTotalReservasAnio(Long totalReservasAnio) {
        this.totalReservasAnio = totalReservasAnio;
    }

    public BigDecimal getIngresosTotales() {
        return ingresosTotales;
    }

    public void setIngresosTotales(BigDecimal ingresosTotales) {
        this.ingresosTotales = ingresosTotales;
    }

    public BigDecimal getIngresosMes() {
        return ingresosMes;
    }

    public void setIngresosMes(BigDecimal ingresosMes) {
        this.ingresosMes = ingresosMes;
    }

    public BigDecimal getIngresosAnio() {
        return ingresosAnio;
    }

    public void setIngresosAnio(BigDecimal ingresosAnio) {
        this.ingresosAnio = ingresosAnio;
    }

    public Long getHabitacionesOcupadas() {
        return habitacionesOcupadas;
    }

    public void setHabitacionesOcupadas(Long habitacionesOcupadas) {
        this.habitacionesOcupadas = habitacionesOcupadas;
    }

    public Long getHabitacionesDisponibles() {
        return habitacionesDisponibles;
    }

    public void setHabitacionesDisponibles(Long habitacionesDisponibles) {
        this.habitacionesDisponibles = habitacionesDisponibles;
    }

    public Long getHabitacionesMantenimiento() {
        return habitacionesMantenimiento;
    }

    public void setHabitacionesMantenimiento(Long habitacionesMantenimiento) {
        this.habitacionesMantenimiento = habitacionesMantenimiento;
    }

    public Long getReservasPendientes() {
        return reservasPendientes;
    }

    public void setReservasPendientes(Long reservasPendientes) {
        this.reservasPendientes = reservasPendientes;
    }

    public Long getReservasConfirmadas() {
        return reservasConfirmadas;
    }

    public void setReservasConfirmadas(Long reservasConfirmadas) {
        this.reservasConfirmadas = reservasConfirmadas;
    }

    public Long getReservasCanceladas() {
        return reservasCanceladas;
    }

    public void setReservasCanceladas(Long reservasCanceladas) {
        this.reservasCanceladas = reservasCanceladas;
    }

    public Long getReservasFinalizadas() {
        return reservasFinalizadas;
    }

    public void setReservasFinalizadas(Long reservasFinalizadas) {
        this.reservasFinalizadas = reservasFinalizadas;
    }

    public Long getTotalClientes() {
        return totalClientes;
    }

    public void setTotalClientes(Long totalClientes) {
        this.totalClientes = totalClientes;
    }

    public Long getTotalHabitaciones() {
        return totalHabitaciones;
    }

    public void setTotalHabitaciones(Long totalHabitaciones) {
        this.totalHabitaciones = totalHabitaciones;
    }

    public Long getTotalServicios() {
        return totalServicios;
    }

    public void setTotalServicios(Long totalServicios) {
        this.totalServicios = totalServicios;
    }
}

