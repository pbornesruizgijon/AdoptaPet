package com.example.adoptapet.model;

public class Gato extends Mascota {
    @SuppressWarnings("FieldMayBeFinal")
    private boolean castrado;

    public Gato(int id, String nombre, int edad, String raza, String sexo, String descripcion, boolean castrado, String imagen) {
        super(id, nombre, edad, raza, sexo, descripcion, imagen);
        this.castrado = castrado;
    }

    public boolean isCastrado() {
        return castrado;
    }
}