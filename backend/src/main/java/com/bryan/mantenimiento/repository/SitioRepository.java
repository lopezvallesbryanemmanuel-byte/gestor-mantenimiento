package com.bryan.mantenimiento.repository;

import com.bryan.mantenimiento.entity.Sitio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SitioRepository extends JpaRepository<Sitio, Long> {
}