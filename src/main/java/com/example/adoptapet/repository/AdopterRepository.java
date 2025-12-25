package com.example.adoptapet.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.adoptapet.model.Adopter;

public interface AdopterRepository extends JpaRepository<Adopter, Long> {
    Optional<Adopter> findByNombre(String nombre);
}
