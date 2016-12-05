package com.example.caxidy.cursosalumnos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BDCursos extends SQLiteOpenHelper {
    private static Curso curso;
    private static Alumno alumno;
    private static Profesor profesor;
    private static Asignatura asignatura;
    private static final int VERSION_BASEDATOS = 1;
    private static final String NOMBRE_BASEDATOS = "Instituto.db";
    private static final String NOMBRE_TABLA_CURSOS = "Cursos";
    private static final String NOMBRE_TABLA_ALUMNOS = "Alumnos";
    private static final String NOMBRE_TABLA_PROFESORES = "Profesores";
    private static final String NOMBRE_TABLA_ASIGNATURAS = "Asignaturas";
    private static final String insCursos = "CREATE TABLE Cursos (id INT PRIMARY KEY," +
            "nombre VARCHAR(50))";
    private static final String insAlumnos = "CREATE TABLE Alumnos (id INT PRIMARY KEY," +
            "nombre VARCHAR(50), apellidos VARCHAR(50), idCurso INT, FOREIGN KEY (idCurso) REFERENCES Cursos(id))";
    private static final String insProfesores = "CREATE TABLE Profesores (id INT PRIMARY KEY," +
            "nombre VARCHAR(50))";
    private static final String insAsignaturas = "CREATE TABLE Asignaturas (id INT PRIMARY KEY," +
            "nombre VARCHAR(50), idProfesor INT, idCurso INT, " +
            "FOREIGN KEY (idProfesor) REFERENCES Profesores(id), FOREIGN KEY (idCurso) REFERENCES Cursos(id))";

    public BDCursos(Context context) {
        super(context, NOMBRE_BASEDATOS,null,VERSION_BASEDATOS);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(insCursos);
        db.execSQL(insAlumnos);
        db.execSQL(insProfesores);
        db.execSQL(insAsignaturas);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+NOMBRE_TABLA_CURSOS);
        db.execSQL("DROP TABLE IF EXISTS "+NOMBRE_TABLA_ALUMNOS);
        db.execSQL("DROP TABLE IF EXISTS "+NOMBRE_TABLA_PROFESORES);
        db.execSQL("DROP TABLE IF EXISTS "+NOMBRE_TABLA_ASIGNATURAS);

        onCreate(db);
    }
}
