package com.villanos_toni.datosservidorvolley;

import java.io.Serializable;

public class Villano implements Serializable {

    private int id;
    private String nombre;
    private String pelicula;
    private String poderes;
    private String imagen;


    public Villano(int id, String nombre, String pelicula, String poderes, String imagen) {
        this.id = id;
        this.nombre = nombre;
        this.pelicula = pelicula;
        this.poderes = poderes;
        this.imagen = imagen;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    @Override
    public String toString() {
        return "Villano{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", pelicula='" + pelicula + '\'' +
                ", poderes='" + poderes + '\'' +
                ", imagen='" + imagen + '\'' +
                '}';
    }
}
