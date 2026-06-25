package com.conectatarot.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminEspecialidadDTO {

    private Integer id;
    private String nombre;
    private String descripcion;
    private Boolean activa;
}
