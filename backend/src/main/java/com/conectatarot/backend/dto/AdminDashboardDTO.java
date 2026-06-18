package com.conectatarot.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AdminDashboardDTO {

    private long usuarios;
    private long tarotistas;
    private long sesiones;
    private long pendientes;
}