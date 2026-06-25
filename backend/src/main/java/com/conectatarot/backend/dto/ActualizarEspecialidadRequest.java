package com.conectatarot.backend.dto;

import lombok.Data;

@Data
public class ActualizarEspecialidadRequest {

    private String nombre;
    private String descripcion;
    private Boolean activa;
}
