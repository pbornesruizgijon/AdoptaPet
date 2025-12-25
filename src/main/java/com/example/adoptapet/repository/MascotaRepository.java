package com.example.adoptapet.repository;

import com.example.adoptapet.model.MascotaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MascotaRepository extends JpaRepository<MascotaEntity, Long> {
}
