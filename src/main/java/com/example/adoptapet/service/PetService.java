package com.example.adoptapet.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.adoptapet.model.Adopter;
import com.example.adoptapet.model.MascotaEntity;
import com.example.adoptapet.model.exceptions.AlreadyAdoptedException;
import com.example.adoptapet.repository.AdopterRepository;
import com.example.adoptapet.repository.MascotaRepository;

@Service
public class PetService {

    private final MascotaRepository mascotaRepository;
    private final AdopterRepository adopterRepository;

    public PetService(MascotaRepository mascotaRepository,
                      AdopterRepository adopterRepository) {
        this.mascotaRepository = mascotaRepository;
        this.adopterRepository = adopterRepository;
    }

    // Devuelve todas las mascotas
    public List<MascotaEntity> todas() {
        return mascotaRepository.findAll();
    }

    // Busca mascota por id
    public Optional<MascotaEntity> findById(Long id) {
        return mascotaRepository.findById(id);
    }

    // Adopta una mascota si es posible
    public void adoptar(Long id, String adopterName) throws AlreadyAdoptedException {
    MascotaEntity mascota = mascotaRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Mascota no encontrada"));

    if (mascota.isAdoptada()) {
        throw new AlreadyAdoptedException("La mascota ya fue adoptada");
    }

    Optional<Adopter> opt = adopterRepository.findByNombre(adopterName);
    Adopter adopter;
    if (opt.isPresent()) {
        adopter = opt.get();
    } else {
        adopter = adopterRepository.save(new Adopter(adopterName, "", ""));
    }

    mascota.setAdopter(adopter);
    mascota.setAdoptada(true);
    mascotaRepository.save(mascota);
}


    // Número de mascotas adoptadas
    public int contarAdoptadas() {
        return (int) mascotaRepository.findAll()
                .stream()
                .filter(MascotaEntity::isAdoptada)
                .count();
    }

    public List<MascotaEntity> ordenarPorCampo(String campo) {
        if ("edad".equalsIgnoreCase(campo)) {
            return mascotaRepository.findAll(Sort.by("edad"));
        }
        return mascotaRepository.findAll(Sort.by("nombre"));
    }
}