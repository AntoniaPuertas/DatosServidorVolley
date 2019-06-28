package com.example.datosservidorvolley;

import java.io.Serializable;

public class Villano implements Serializable {

    private String nombre;
    private String pelicula;
    private String poderes;


    public Villano(String nombre, String pelicula, String poderes) {
        this.nombre = nombre;
        this.pelicula = pelicula;
        this.poderes = poderes;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPelicula() {
        return pelicula;
    }

    public void setPelicula(String pelicula) {
        this.pelicula = pelicula;
    }

    public String getPoderes() {
        return poderes;
    }

    public void setPoderes(String poderes) {
        this.poderes = poderes;
    }

    @Override
    public String toString() {
        return "Villano{" +
                "nombre='" + nombre + '\'' +
                ", pelicula='" + pelicula + '\'' +
                ", poderes='" + poderes + '\'' +
                '}';
    }
}
