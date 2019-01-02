package com.example.android.miwok.activities;

public class ItemMiwok {
    String palabra;
    String descripcion;
    int imagenRecurso;
    int audioRecurso;

    public ItemMiwok(String palabra, String descripcion, int imagenRecurso, int audioRecurso) {
        this.palabra = palabra;
        this.descripcion = descripcion;
        this.imagenRecurso = imagenRecurso;
        this.audioRecurso = audioRecurso;
    }

    public String getPalabra() {
        return palabra;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getImagenRecurso() {
        return imagenRecurso;
    }

    public int getAudioRecurso() {
        return audioRecurso;
    }
}
