package com.example.adoptapet.model;

import lombok.Getter;

@Getter
public abstract class Mascota implements Adoptable, Comparable<Mascota> {

    protected int id;
    protected String nombre;
    protected int edad; //años
    protected String raza;
    protected String sexo; // Macho o Hembra
    protected String descripcion;
    protected boolean adoptada;
    protected String adopter;
    protected String imagen;

    public static String campoOrden = "nombre"; // puede ser "nombre" o "edad"
    

    public Mascota(int id, String nombre, int edad, String raza, String sexo, String descripcion, String imagen) {
        this.id = id;
        this.nombre = nombre;
        this.edad = edad;
        this.raza = raza;
        this.sexo = sexo;
        this.descripcion = descripcion;
        this.adoptada = false;
        this.imagen = imagen;
    }

    @Override
    public void adoptar(String adopterName) throws com.example.adoptapet.model.exceptions.AlreadyAdoptedException {
        if (this.adoptada) {
            throw new com.example.adoptapet.model.exceptions.AlreadyAdoptedException("La mascota ya fue adoptada");
        }
        this.adoptada = true;
        this.adopter = adopterName;
    }

    public String getTipo() {
        if (this instanceof Perro) {
            return "Perro";
        } else if (this instanceof Gato) {
            return "Gato";
        } else {
            return "Mascota";
        }
    }

    @Override
    public boolean isAdopted() {
        return adoptada;
    }

    // Ordena por el campo seleccionado
    @Override
    public int compareTo(Mascota o) {
        if ("edad".equals(campoOrden)) {
            return Integer.compare(this.edad, o.edad);
        } 
        // Por defecto
        return this.nombre.compareToIgnoreCase(o.nombre);
    }
}
