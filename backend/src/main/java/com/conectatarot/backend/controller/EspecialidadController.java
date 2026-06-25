package com.conectatarot.backend.controller;

import com.conectatarot.backend.service.EspecialidadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/especialidades")
@RequiredArgsConstructor
public class EspecialidadController {

    private final EspecialidadService especialidadService;

    @GetMapping
    public ResponseEntity<?> listarActivas() {
        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "data", especialidadService.listarActivas()
                )
        );
    }
}
