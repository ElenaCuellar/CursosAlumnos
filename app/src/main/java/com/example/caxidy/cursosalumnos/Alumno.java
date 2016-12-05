package com.example.caxidy.cursosalumnos;

public class Alumno {
    int id, idCurso;
    String nombre, apellido;

    public Alumno(){
        id=0;
        nombre="";
        apellido="";
        idCurso=0;
    }

    public Alumno(int id, String nombre, String apellido, int idCurso){
        this.id=id;
        this.nombre=nombre;
        this.apellido=apellido;
        this.idCurso=idCurso;
    }

    public int getID(){return id;}

    public String getNombre(){return nombre;}

    public String getApellido(){return apellido;}

    public int getIdCurso(){return idCurso;}
}
