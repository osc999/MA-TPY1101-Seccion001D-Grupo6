package com.conectatarot.backend.controller;

import com.conectatarot.backend.dto.AdminDashboardDTO;
import com.conectatarot.backend.entity.Tarotista;
import com.conectatarot.backend.entity.Usuario;
import com.conectatarot.backend.repository.SesionRepository;
import com.conectatarot.backend.repository.TarotistaRepository;
import com.conectatarot.backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UsuarioRepository usuarioRepository;
    private final TarotistaRepository tarotistaRepository;
    private final SesionRepository sesionRepository;

    @GetMapping("/dashboard")
    public ResponseEntity<?> dashboard() {

        AdminDashboardDTO dto =
                new AdminDashboardDTO(
                        usuarioRepository.count(),
                        tarotistaRepository.count(),
                        sesionRepository.count(),
                        tarotistaRepository.countByEstadoIgnoreCase("PENDIENTE")
                );

        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "data", dto
                )
        );
    }


    @GetMapping("/usuarios")
    public ResponseEntity<?> usuarios() {

        List<Usuario> usuarios = usuarioRepository.findAll();

        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "data", usuarios
                )
        );
    }

    @GetMapping("/tarotistas/pendientes")
    public ResponseEntity<?> pendientes() {

        List<Tarotista> pendientes =
                tarotistaRepository.findByEstadoIgnoreCase("PENDIENTE");

        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "data", pendientes
                )
        );
    }

    @PutMapping("/tarotistas/{id}/aprobar")
    public ResponseEntity<?> aprobar(
            @PathVariable Integer id
    ) {

        Tarotista tarotista =
                tarotistaRepository.findById(id)
                        .orElseThrow();

        tarotista.setEstado("APROBADO");

        tarotistaRepository.save(tarotista);

        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "message", "Tarotista aprobado"
                )
        );
    }

    @PutMapping("/tarotistas/{id}/rechazar")
    public ResponseEntity<?> rechazar(
            @PathVariable Integer id
    ) {

        Tarotista tarotista =
                tarotistaRepository.findById(id)
                        .orElseThrow();

        tarotista.setEstado("RECHAZADO");

        tarotistaRepository.save(tarotista);

        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "message", "Tarotista rechazado"
                )
        );
    }

}