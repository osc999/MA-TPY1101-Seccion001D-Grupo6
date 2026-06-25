package com.conectatarot.backend.repository;

import com.conectatarot.backend.entity.Sesion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SesionRepository extends JpaRepository<Sesion, Integer> {

    boolean existsByTarotista_IdAndFecha(
            Integer tarotistaId,
            LocalDateTime fecha
    );

    List<Sesion> findByTarotista_Id(Integer tarotistaId);

    List<Sesion> findByUsuario_Email(String email);

    List<Sesion> findByTarotista_Usuario_EmailOrderByFechaAsc(String email);

    Page<Sesion> findByTarotista_Usuario_EmailOrderByFechaAsc(
            String email,
            Pageable pageable
    );

    Page<Sesion> findByTarotista_Usuario_EmailAndEstadoOrderByFechaAsc(
            String email,
            String estado,
            Pageable pageable
    );

    boolean existsByEspecialidad_Id(Integer especialidadId);
}