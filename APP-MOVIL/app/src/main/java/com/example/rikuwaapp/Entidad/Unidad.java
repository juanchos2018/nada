package com.example.rikuwaapp.Entidad;

public class Unidad {
    String nombrePlaca;
    String nombrePlacatmp;
    String nombreUnidad;
    String fotoReferencial;
    String zonaUnidad;
    String estadoPersonas;
    Double latitud;
    Double longitud;
    String id_unidad;

    public  Unidad(){

    }

    public Unidad(String nombrePlaca, String nombrePlacatmp, String nombreUnidad, String fotoReferencial, String zonaUnidad, String estadoPersonas, Double latitud, Double longitud) {
        this.nombrePlaca = nombrePlaca;
        this.nombrePlacatmp = nombrePlacatmp;
        this.nombreUnidad = nombreUnidad;
        this.fotoReferencial = fotoReferencial;
        this.zonaUnidad = zonaUnidad;
        this.estadoPersonas = estadoPersonas;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public String getNombrePlacatmp() {
        return nombrePlacatmp;
    }

    public void setNombrePlacatmp(String nombrePlacatmp) {
        this.nombrePlacatmp = nombrePlacatmp;
    }

    public String getNombrePlaca() {
        return nombrePlaca;
    }

    public void setNombrePlaca(String nombrePlaca) {
        this.nombrePlaca = nombrePlaca;
    }

    public String getNombreUnidad() {
        return nombreUnidad;
    }

    public void setNombreUnidad(String nombreUnidad) {
        this.nombreUnidad = nombreUnidad;
    }

    public String getFotoReferencial() {
        return fotoReferencial;
    }

    public void setFotoReferencial(String fotoReferencial) {
        this.fotoReferencial = fotoReferencial;
    }

    public String getZonaUnidad() {
        return zonaUnidad;
    }

    public void setZonaUnidad(String zonaUnidad) {
        this.zonaUnidad = zonaUnidad;
    }

    public String getEstadoPersonas() {
        return estadoPersonas;
    }

    public void setEstadoPersonas(String estadoPersonas) {
        this.estadoPersonas = estadoPersonas;
    }

    public String getId_unidad() {
        return id_unidad;
    }

    public void setId_unidad(String id_unidad) {
        this.id_unidad = id_unidad;
    }
}
