package com.conectatarot.backend.repository;

import com.conectatarot.backend.entity.Especialidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EspecialidadRepository extends JpaRepository<Especialidad, Integer> {

    Optional<Especialidad> findByNombre(String nombre);

    @Query("SELECT e FROM Especialidad e WHERE e.activa = true ORDER BY e.nombre ASC")
    List<Especialidad> findActivasOrderedByNombre();

    @Query("SELECT e FROM Especialidad e ORDER BY e.nombre ASC")
    List<Especialidad> findAllOrderedByNombre();
}
