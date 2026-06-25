package com.conectatarot.backend.service;

import com.conectatarot.backend.dto.ActualizarEspecialidadRequest;
import com.conectatarot.backend.dto.AdminEspecialidadDTO;
import com.conectatarot.backend.dto.CrearEspecialidadRequest;
import com.conectatarot.backend.dto.EspecialidadCatalogDTO;
import com.conectatarot.backend.entity.Especialidad;
import com.conectatarot.backend.repository.EspecialidadRepository;
import com.conectatarot.backend.repository.SesionRepository;
import com.conectatarot.backend.repository.TarotistaEspecialidadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EspecialidadService {

    private final EspecialidadRepository especialidadRepository;
    private final TarotistaEspecialidadRepository tarotistaEspecialidadRepository;
    private final SesionRepository sesionRepository;

    public List<EspecialidadCatalogDTO> listarActivas() {
        return especialidadRepository.findActivasOrderedByNombre()
                .stream()
                .map(this::convertirACatalogo)
                .toList();
    }

    public List<AdminEspecialidadDTO> listarTodas() {
        return especialidadRepository.findAllOrderedByNombre()
                .stream()
                .map(this::convertirAAdmin)
                .toList();
    }

    public AdminEspecialidadDTO crear(CrearEspecialidadRequest request) {
        if (request.getNombre() == null || request.getNombre().isBlank()) {
            throw new RuntimeException("El nombre es obligatorio");
        }

        String nombre = request.getNombre().trim();

        if (especialidadRepository.findByNombre(nombre).isPresent()) {
            throw new RuntimeException("Ya existe una especialidad con ese nombre");
        }

        Especialidad especialidad = Especialidad.builder()
                .nombre(nombre)
                .descripcion(request.getDescripcion())
                .activa(true)
                .build();

        return convertirAAdmin(especialidadRepository.save(especialidad));
    }

    public AdminEspecialidadDTO actualizar(Integer id, ActualizarEspecialidadRequest request) {
        Especialidad especialidad = especialidadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Especialidad no encontrada"));

        if (request.getNombre() != null && !request.getNombre().isBlank()) {
            String nombre = request.getNombre().trim();

            especialidadRepository.findByNombre(nombre)
                    .filter(existing -> !existing.getId().equals(id))
                    .ifPresent(existing -> {
                        throw new RuntimeException("Ya existe una especialidad con ese nombre");
                    });

            especialidad.setNombre(nombre);
        }

        if (request.getDescripcion() != null) {
            especialidad.setDescripcion(request.getDescripcion());
        }

        if (request.getActiva() != null) {
            especialidad.setActiva(request.getActiva());
        }

        return convertirAAdmin(especialidadRepository.save(especialidad));
    }

    public void eliminar(Integer id) {
        Especialidad especialidad = especialidadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Especialidad no encontrada"));

        if (tarotistaEspecialidadRepository.countByEspecialidad_Id(id) > 0) {
            throw new RuntimeException("No se puede eliminar: hay tarotistas asociados");
        }

        if (sesionRepository.existsByEspecialidad_Id(id)) {
            throw new RuntimeException("No se puede eliminar: hay sesiones asociadas");
        }

        especialidadRepository.delete(especialidad);
    }

    private EspecialidadCatalogDTO convertirACatalogo(Especialidad especialidad) {
        return EspecialidadCatalogDTO.builder()
                .id(especialidad.getId())
                .nombre(especialidad.getNombre())
                .build();
    }

    private AdminEspecialidadDTO convertirAAdmin(Especialidad especialidad) {
        return AdminEspecialidadDTO.builder()
                .id(especialidad.getId())
                .nombre(especialidad.getNombre())
                .descripcion(especialidad.getDescripcion())
                .activa(especialidad.getActiva() != null ? especialidad.getActiva() : true)
                .build();
    }
}
