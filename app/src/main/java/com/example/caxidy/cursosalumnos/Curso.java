package com.example.caxidy.cursosalumnos;

public class Curso {
    int id;
    String nombre;

    public Curso(){
        id=0;
        nombre="";
    }

    public Curso(int id, String nombre){
        this.id=id;
        this.nombre=nombre;

    }

    public int getID(){return id;}

    public String getNombre(){return nombre;}
}
