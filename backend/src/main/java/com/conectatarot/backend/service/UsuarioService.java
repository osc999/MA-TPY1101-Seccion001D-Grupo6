package com.conectatarot.backend.service;

import com.conectatarot.backend.entity.Rol;
import com.conectatarot.backend.entity.Usuario;
import com.conectatarot.backend.entity.Tarotista;
import com.conectatarot.backend.repository.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final TarotistaRepository tarotistaRepository;
    private final SesionRepository sesionRepository;
    private final TarotistaEspecialidadRepository tarotistaEspecialidadRepository;
    private final DisponibilidadTarotistaRepository disponibilidadTarotistaRepository;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          RolRepository rolRepository,
                          BCryptPasswordEncoder passwordEncoder,
                          TarotistaRepository tarotistaRepository,
                          SesionRepository sesionRepository,
                          TarotistaEspecialidadRepository tarotistaEspecialidadRepository,
                          DisponibilidadTarotistaRepository disponibilidadTarotistaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
        this.tarotistaRepository = tarotistaRepository;
        this.sesionRepository = sesionRepository;
        this.tarotistaEspecialidadRepository = tarotistaEspecialidadRepository;
        this.disponibilidadTarotistaRepository = disponibilidadTarotistaRepository;
    }

    public Usuario registrarUsuario(Usuario usuario) {

        Optional<Usuario> existente = usuarioRepository.findByEmail(usuario.getEmail());

        if (existente.isPresent()) {
            throw new RuntimeException("El email ya está registrado");
        }

        Rol rolCliente = rolRepository.findByNombreRol("CLIENTE")
                .orElseThrow(() -> new RuntimeException("Rol CLIENTE no encontrado"));

        usuario.setRol(rolCliente);
        usuario.setActivo(true);
        usuario.setFechaRegistro(LocalDateTime.now());
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        return usuarioRepository.save(usuario);
    }
    public Usuario actualizarUsuario(Integer id, String nombre, String email) {
       Usuario usuario = usuarioRepository.findById(id)
          .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
       usuario.setNombre(nombre);
       usuario.setEmail(email);
       return usuarioRepository.save(usuario); 
    }

    @Transactional
    public void eliminarUsuario(Integer id, String password) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(password, usuario.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        // Si es un tarotista, eliminar sus dependencias
        tarotistaRepository.findAll().stream()
                .filter(t -> t.getUsuario().getIdUsuario().equals(id))
                .findFirst()
                .ifPresent(tarotista -> {
                    // 1. Eliminar sesiones donde es tarotista
                    sesionRepository.deleteAll(sesionRepository.findByTarotista_Id(tarotista.getId()));
                    // 2. Eliminar especialidades del tarotista
                    tarotistaEspecialidadRepository.deleteAll(tarotistaEspecialidadRepository.findByTarotista_Id(tarotista.getId()));
                    // 3. Eliminar disponibilidad (acceder a través de la relación Tarotista)
                    disponibilidadTarotistaRepository.deleteAll(disponibilidadTarotistaRepository.findAll().stream()
                            .filter(d -> d.getTarotista().getId().equals(tarotista.getId()))
                            .toList());
                    // 4. Eliminar el perfil de tarotista
                    tarotistaRepository.delete(tarotista);
                });

        // 5. Eliminar sesiones donde es cliente
        sesionRepository.deleteAll(sesionRepository.findByUsuario_Email(usuario.getEmail()));

        // 6. Finalmente eliminar el usuario
        usuarioRepository.delete(usuario);
    }
}

