package com.example.adoptapet.model;

public class Perro extends Mascota {
    @SuppressWarnings("FieldMayBeFinal")
    private String tamano;

    public Perro(int id, String nombre, int edad, String raza, String sexo, String descripcion, String tamano, String imagen) {
        super(id, nombre, edad, raza, sexo, descripcion, imagen);
        this.tamano = tamano;
    }

    public String getTamano() {
        return tamano;
    }
}