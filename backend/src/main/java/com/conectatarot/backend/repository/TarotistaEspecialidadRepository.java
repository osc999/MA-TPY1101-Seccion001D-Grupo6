package com.conectatarot.backend.repository;

import com.conectatarot.backend.entity.TarotistaEspecialidad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TarotistaEspecialidadRepository extends JpaRepository<TarotistaEspecialidad, Integer> {

    boolean existsByTarotista_IdAndEspecialidad_Id(
            Integer tarotistaId,
            Integer especialidadId
    );

    long countByTarotista_Id(Integer tarotistaId);

    Optional<TarotistaEspecialidad> findByTarotista_IdAndEspecialidad_Id(
            Integer tarotistaId,
            Integer especialidadId
    );

    List<TarotistaEspecialidad> findByTarotista_Id(Integer tarotistaId);

    long countByEspecialidad_Id(Integer especialidadId);
}