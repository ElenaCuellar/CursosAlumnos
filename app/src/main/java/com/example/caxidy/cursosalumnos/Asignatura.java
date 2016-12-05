package com.example.caxidy.cursosalumnos;

public class Asignatura {
    int id, idProfesor, idCurso;
    String nombre;

    public Asignatura(){
        id=0;
        nombre="";
        idProfesor=0;
        idCurso=0;
    }

    public Asignatura(int id, String nombre, int idProfesor, int idCurso){
        this.id=id;
        this.nombre=nombre;
        this.idProfesor=idProfesor;
        this.idCurso=idCurso;
    }

    public int getID(){return id;}

    public String getNombre(){return nombre;}

    public int getIdProfesor(){return idProfesor;}

    public int getIdCurso(){return idCurso;}
}
