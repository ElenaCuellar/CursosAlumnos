package com.example.caxidy.cursosalumnos;

public class Profesor {
    int id;
    String nombre;

    public Profesor(){
        id=0;
        nombre="";
    }

    public Profesor(int id, String nombre){
        this.id=id;
        this.nombre=nombre;

    }

    public int getID(){return id;}

    public String getNombre(){return nombre;}
}
