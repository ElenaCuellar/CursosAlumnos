package com.example.caxidy.cursosalumnos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

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

    //SELECT:

    public boolean hayCursos(){
        SQLiteDatabase db = getReadableDatabase();
        if (db != null) {
            String[] campos = {"COUNT(id)"};
            Cursor c = db.query("Cursos", campos,null, null, null, null, null, null);
            if (c.moveToFirst())
                if(c.getInt(0)>0)
                    return true;
                else
                    return false;
            c.close();
        }
        db.close();
        return false;
    }

    public boolean hayAlumnos(){
        SQLiteDatabase db = getReadableDatabase();
        if (db != null) {
            String[] campos = {"COUNT(id)"};
            Cursor c = db.query("Alumnos", campos,null, null, null, null, null, null);
            if (c.moveToFirst())
                if(c.getInt(0)>0)
                    return true;
                else
                    return false;
            c.close();
        }
        db.close();
        return false;
    }

    public boolean hayProfesores(){
        SQLiteDatabase db = getReadableDatabase();
        if (db != null) {
            String[] campos = {"COUNT(id)"};
            Cursor c = db.query("Profesores", campos,null, null, null, null, null, null);
            if (c.moveToFirst())
                if(c.getInt(0)>0)
                    return true;
                else
                    return false;
            c.close();
        }
        db.close();
        return false;
    }

    public boolean hayAsignaturas(){
        SQLiteDatabase db = getReadableDatabase();
        if (db != null) {
            String[] campos = {"COUNT(id)"};
            Cursor c = db.query("Asignaturas", campos,null, null, null, null, null, null);
            if (c.moveToFirst())
                if(c.getInt(0)>0)
                    return true;
                else
                    return false;
            c.close();
        }
        db.close();
        return false;
    }

    public ArrayList<Curso> listarCursos(){
        ArrayList<Curso> arrayCursos = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        if (db != null) {
            String[] campos = {"id", "nombre"};
            Cursor c = db.query("Cursos", campos,null, null, null, null, null, null);
            if (c.moveToFirst())
                do {
                    curso = new Curso(c.getInt(0), c.getString(1));
                    arrayCursos.add(curso);
                }while(c.moveToNext());
            c.close();
        }
        db.close();
        if(arrayCursos.size()>0)
            return arrayCursos;
        else
            return null;
    }

    public ArrayList<Alumno> listarAlumnos(int idCurso){
        ArrayList<Alumno> arrayAlumnos = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        if (db != null) {
            String[] campos = {"id", "nombre", "apellidos", "idCurso"};
            Cursor c = db.query("Alumnos", campos,"idCurso="+idCurso, null, null, null, null, null);
            if (c.moveToFirst())
                do {
                    alumno = new Alumno(c.getInt(0), c.getString(1),c.getString(2),c.getInt(3));
                    arrayAlumnos.add(alumno);
                }while(c.moveToNext());
            c.close();
        }
        db.close();
        if(arrayAlumnos.size()>0)
            return arrayAlumnos;
        else
            return null;
    }

    //INSERT

    public long insertarCurso(Curso curso){
        long numReg = -1;
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            ContentValues valores = new ContentValues();
            valores.put("id", curso.getID());
            valores.put("nombre", curso.getNombre());
            numReg = db.insert("Cursos", null, valores);
        }
        db.close();
        return numReg;
    }

    public long insertarAlumno(Alumno alumno){
        long numReg = -1;
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            ContentValues valores = new ContentValues();
            valores.put("id", alumno.getID());
            valores.put("nombre", alumno.getNombre());
            valores.put("apellidos",alumno.getApellido());
            valores.put("idCurso",alumno.getIdCurso());
            numReg = db.insert("Alumnos", null, valores);
        }
        db.close();
        return numReg;
    }

    public long insertarProfesor(Profesor profesor){
        long numReg = -1;
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            ContentValues valores = new ContentValues();
            valores.put("id", profesor.getID());
            valores.put("nombre", profesor.getNombre());
            numReg = db.insert("Profesores", null, valores);
        }
        db.close();
        return numReg;
    }

    public long insertarAsignatura(Asignatura asignatura){
        long numReg = -1;
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            ContentValues valores = new ContentValues();
            valores.put("id", asignatura.getID());
            valores.put("nombre", asignatura.getNombre());
            valores.put("idProfesor",asignatura.getIdProfesor());
            valores.put("idCurso",asignatura.getIdCurso());
            numReg = db.insert("Asignaturas", null, valores);
        }
        db.close();
        return numReg;
    }
}
