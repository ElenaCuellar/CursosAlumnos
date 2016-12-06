/*Actividades finales 2 y 3: uso de ExpandableListView y Navigation Drawer que contenga un ListView + uso de base de datos
 * La aplicacion consta de un ExpandableListView de los cursos del ciclo, con sus alumnos y de un Navigation Drawer que
  * contiene un ListView con las asignaturas de los cursos. Si se selecciona una asignatura sale un Dialog con informacion.*/

package com.example.caxidy.cursosalumnos;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ExpandableListView listaExpandable;
    ExpandableListAdapter adaptadorExpandable;
    List<String> listaCabeceras;
    HashMap<String,List<String>> listaHijos;
    BDCursos bd;
    ListView listaAsignaturas;
    AdaptadorAsignaturas adpAsig;
    ArrayList<Asignatura> arrayAsig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bd = new BDCursos(this);

        //Obtener el ExpandableListView y llenarlo de datos
        listaExpandable = (ExpandableListView) findViewById(R.id.listExpandable);
        mostrarDatos();
        //Concretar el adaptador
        adaptadorExpandable = new ExpandableListAdapter(this,listaCabeceras,listaHijos);
        listaExpandable.setAdapter(adaptadorExpandable);

        //Listener de la lista Expandable (al pulsar en un hijo):
        listaExpandable.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Toast.makeText(getApplicationContext(), getString(R.string.al1) + listaHijos.get(listaCabeceras.get(groupPosition)).get(childPosition),
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        //Listener para cuando se expande un grupo:
        listaExpandable.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(), getString(R.string.gr1)+listaCabeceras.get(groupPosition) + getString(R.string.gr2),
                        Toast.LENGTH_SHORT).show();
            }
        });
        //Listener al contraer un grupo:
        listaExpandable.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(), getString(R.string.gr1)+listaCabeceras.get(groupPosition) + getString(R.string.gr3),
                        Toast.LENGTH_SHORT).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //Lista del Drawer: llenar los datos y configurar el adaptador
        listaAsignaturas = (ListView) findViewById(android.R.id.list);
        arrayAsig = new ArrayList<>();

        if(!bd.hayProfesores()){
            llenarBDProfesores();
        }

        if(!bd.hayAsignaturas()){
            llenarBDAsignaturas();
        }

        arrayAsig = new ArrayList<>();
        arrayAsig = bd.listarAsignaturas();
        if(arrayAsig != null) {
            adpAsig = new AdaptadorAsignaturas(this, arrayAsig);
            adpAsig.notifyDataSetChanged();
            listaAsignaturas.setAdapter(adpAsig);
            Toast.makeText(getApplicationContext(),getString(R.string.asigCargada),Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(getApplicationContext(),getString(R.string.asigCargadaErr),Toast.LENGTH_SHORT).show();

        //Listener del ListView de asignaturas
        listaAsignaturas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView adapter, View view, int position, long arg)
            {
                Asignatura asig = (Asignatura) listaAsignaturas.getAdapter().getItem(position);

                //Sacamos la informacion de la asignatura en un Dialog
                mostrarDialog(asig);
            }
        });
    }

    public void mostrarDialog(Asignatura asig){

        AlertDialog.Builder alertDialogBu = new AlertDialog.Builder(this);
        alertDialogBu.setTitle(asig.getNombre());
        alertDialogBu.setMessage(getString(R.string.asig1)+asig.getNombre()+"\n"+getString(R.string.curso1)+bd.mostrarCursoAsig(asig.getID())+"\n"+
                getString(R.string.prof1)+ bd.mostrarProfesorAsig(asig.getID()));
        alertDialogBu.setIcon(R.mipmap.ic_launcher);

        alertDialogBu.setPositiveButton(getString(R.string.aceptar), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {}
        });
        AlertDialog alertDialog = alertDialogBu.create();
        alertDialog.show();
    }

    public void mostrarDatos(){
        List<String> grupoAl;
        ArrayList<Alumno> listaAl = new ArrayList<>();
        listaCabeceras = new ArrayList<>();
        listaHijos = new HashMap<String, List<String>>();

        //Comprobar si las BD de Cursos o Alumnos estan vacias para añadir los cursos y alumnos necesarios...
        if(!bd.hayCursos()){
            llenarBDCursos();
        }

        if(!bd.hayAlumnos()){
            llenarBDAlumnos();
        }

        //Añadir cursos a las cabeceras y alumnos a las listas hijas
        ArrayList<Curso> listaCursos = bd.listarCursos();
        if(listaCursos!=null){
            for(int i=0;i<listaCursos.size();i++) {
                listaCabeceras.add(listaCursos.get(i).getNombre());
                if(listaAl !=null && listaAl.size()>0)
                    listaAl.clear();
                listaAl = bd.listarAlumnos(listaCursos.get(i).getID());
                if(listaAl !=null) {
                    grupoAl = new ArrayList<>();
                    for (int j = 0; j < listaAl.size(); j++)
                        grupoAl.add(listaAl.get(j).getNombre()+" "+listaAl.get(j).getApellido());
                    listaHijos.put(listaCabeceras.get(i), grupoAl);
                }
                else
                    Toast.makeText(getApplicationContext(), getString(R.string.cargAl), Toast.LENGTH_SHORT).show();
            }
        }
        else
            Toast.makeText(getApplicationContext(), getString(R.string.cargCr), Toast.LENGTH_SHORT).show();
    }

    public void llenarBDCursos(){
        long[] indices = new long[4];

        Curso c1 = new Curso(1,"Primero de DAW");
        indices[0]= bd.insertarCurso(c1);
        Curso c2 = new Curso(2,"Segundo de DAW");
        indices[1]= bd.insertarCurso(c2);
        Curso c3 = new Curso(3,"Primero de DAM");
        indices[2]= bd.insertarCurso(c3);
        Curso c4 = new Curso(4,"Segundo de DAM");
        indices[3]= bd.insertarCurso(c4);

        boolean correcto=true;
        for(int i=0;i<indices.length;i++)
            if(indices[i]==-1)
                correcto=false;
        if(correcto)
            Toast.makeText(getApplicationContext(),getString(R.string.agrCr),Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getApplicationContext(),getString(R.string.agrCrErr),Toast.LENGTH_SHORT).show();
    }

    public void llenarBDAlumnos(){
        long[] indices = new long[19];

        Alumno a1 = new Alumno(1,"Pablo","Rodriguez Salas",1);
        indices[0]=bd.insertarAlumno(a1);
        Alumno a2 = new Alumno(2,"Nerea","Arbol Romero",1);
        indices[1]=bd.insertarAlumno(a2);
        Alumno a3 = new Alumno(3,"Manuel","Sanchez Bermudez",1);
        indices[2]=bd.insertarAlumno(a3);
        Alumno a4 = new Alumno(4,"Aitor","Mesa Lopez",1);
        indices[3]=bd.insertarAlumno(a4);

        Alumno a5 = new Alumno(5,"Ana","Lopera Portero",2);
        indices[4]=bd.insertarAlumno(a5);
        Alumno a6 = new Alumno(6,"Pedro","Calvente Puertas",2);
        indices[5]= bd.insertarAlumno(a6);
        Alumno a7 = new Alumno(7,"Alberto","Quesada Viruez",2);
        indices[6]= bd.insertarAlumno(a7);
        Alumno a8 = new Alumno(8,"Gonzalo","Garcia Grima",2);
        indices[7]= bd.insertarAlumno(a8);
        Alumno a9 = new Alumno(9,"Maria","Piedra Sampedro",2);
        indices[8]= bd.insertarAlumno(a9);

        Alumno a10 = new Alumno(10,"Mario","Arenas Sandoval",3);
        indices[9]= bd.insertarAlumno(a10);
        Alumno a11 = new Alumno(11,"Laura","Manzano Llorente",3);
        indices[10]= bd.insertarAlumno(a11);
        Alumno a12 = new Alumno(12,"Jose Antonio","Ortega Araque",3);
        indices[11]= bd.insertarAlumno(a12);
        Alumno a13 = new Alumno(13,"Sheila","Martinez Soga",3);
        indices[12]= bd.insertarAlumno(a13);
        Alumno a14 = new Alumno(14,"Salvador","Pinos Quesada",3);
        indices[13]= bd.insertarAlumno(a14);

        Alumno a15 = new Alumno(15,"Francisco","Pozo Arroyo",4);
        indices[14]= bd.insertarAlumno(a15);
        Alumno a16 = new Alumno(16,"Gloria","Sabinas Ortega",4);
        indices[15]= bd.insertarAlumno(a16);
        Alumno a17 = new Alumno(17,"Estefania","Fuentes Labrada",4);
        indices[16]= bd.insertarAlumno(a17);
        Alumno a18 = new Alumno(18,"Jose Enrique","Cuellar Arenas",4);
        indices[17]= bd.insertarAlumno(a18);
        Alumno a19 = new Alumno(19,"Guillermo","Luque Otero",4);
        indices[18]= bd.insertarAlumno(a19);

        boolean correcto=true;
        for(int i=0;i<indices.length;i++)
            if(indices[i]==-1)
                correcto=false;
        if(correcto)
            Toast.makeText(getApplicationContext(),getString(R.string.agrAl),Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getApplicationContext(),getString(R.string.agrAlErr),Toast.LENGTH_SHORT).show();
    }

    public void llenarBDProfesores(){
        long[] indices = new long[9];

        Profesor p1 = new Profesor(1,"Paco Lopez");
        indices[0]=bd.insertarProfesor(p1);
        Profesor p2 = new Profesor(2,"Isabel Parejo");
        indices[0]=bd.insertarProfesor(p2);
        Profesor p3 = new Profesor(3,"Mario Caminos");
        indices[0]=bd.insertarProfesor(p3);
        Profesor p4 = new Profesor(4,"Santiago Ancho");
        indices[0]=bd.insertarProfesor(p4);
        Profesor p5 = new Profesor(5,"Ignacio Cabrero");
        indices[0]=bd.insertarProfesor(p5);
        Profesor p6 = new Profesor(6,"Elena Santos");
        indices[0]=bd.insertarProfesor(p6);
        Profesor p7 = new Profesor(7,"Marilo Casablanca");
        indices[0]=bd.insertarProfesor(p7);
        Profesor p8 = new Profesor(8,"Amanda Molas");
        indices[0]=bd.insertarProfesor(p8);
        Profesor p9 = new Profesor(9,"Francisco Amarillo");
        indices[0]=bd.insertarProfesor(p9);

        boolean correcto=true;
        for(int i=0;i<indices.length;i++)
            if(indices[i]==-1)
                correcto=false;
        if(correcto)
            Toast.makeText(getApplicationContext(),getString(R.string.agrPr),Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getApplicationContext(),getString(R.string.agrPrErr),Toast.LENGTH_SHORT).show();
    }

    public void llenarBDAsignaturas(){
        long[] indices = new long[20];

        Asignatura a1 = new Asignatura(1,"Sistemas Informaticos DAW",3,1);
        indices[0]=bd.insertarAsignatura(a1);
        Asignatura a2 = new Asignatura(2,"Sistemas Informaticos DAM",3,3);
        indices[0]=bd.insertarAsignatura(a2);
        Asignatura a3 = new Asignatura(3,"Entornos de desarrollo DAW",1,1);
        indices[0]=bd.insertarAsignatura(a3);
        Asignatura a4 = new Asignatura(4,"Entornos de desarrollo DAM",1,3);
        indices[0]=bd.insertarAsignatura(a4);
        Asignatura a5 = new Asignatura(5,"Programacion Web",5,1);
        indices[0]=bd.insertarAsignatura(a5);
        Asignatura a6 = new Asignatura(6,"Programacion",8,3);
        indices[0]=bd.insertarAsignatura(a6);
        Asignatura a7 = new Asignatura(7,"Bases de datos DAW",2,1);
        indices[0]=bd.insertarAsignatura(a7);
        Asignatura a8 = new Asignatura(8,"Bases de datos DAM",4,3);
        indices[0]=bd.insertarAsignatura(a8);
        Asignatura a9 = new Asignatura(9,"Lenguajes de marcas DAW",9,1);
        indices[0]=bd.insertarAsignatura(a9);
        Asignatura a10 = new Asignatura(10,"Lenguajes de marcas DAM",7,3);
        indices[0]=bd.insertarAsignatura(a10);
        Asignatura a11 = new Asignatura(11,"Programacion en dispositivos moviles",6,4);
        indices[0]=bd.insertarAsignatura(a11);
        Asignatura a12 = new Asignatura(12,"Desarrollo de interfaces",7,4);
        indices[0]=bd.insertarAsignatura(a12);
        Asignatura a13 = new Asignatura(13,"Programacion concurrente",8,4);
        indices[0]=bd.insertarAsignatura(a13);
        Asignatura a14 = new Asignatura(14,"Acceso a datos",6,4);
        indices[0]=bd.insertarAsignatura(a14);
        Asignatura a15 = new Asignatura(15,"Sistemas de gestion empresarial",4,4);
        indices[0]=bd.insertarAsignatura(a15);
        Asignatura a16 = new Asignatura(16,"Programacion web II",2,2);
        indices[0]=bd.insertarAsignatura(a16);
        Asignatura a17 = new Asignatura(17,"Desarrollo de interfaces web",5,2);
        indices[0]=bd.insertarAsignatura(a17);
        Asignatura a18 = new Asignatura(18,"Programacion para servidores web",9,2);
        indices[0]=bd.insertarAsignatura(a18);
        Asignatura a19 = new Asignatura(19,"Acceso a datos DAW",5,2);
        indices[0]=bd.insertarAsignatura(a19);
        Asignatura a20 = new Asignatura(20,"Sistemas de gestion empresarial DAW",9,2);
        indices[0]=bd.insertarAsignatura(a20);

        boolean correcto=true;
        for(int i=0;i<indices.length;i++)
            if(indices[i]==-1)
                correcto=false;
        if(correcto)
            Toast.makeText(getApplicationContext(),getString(R.string.agrAs),Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getApplicationContext(),getString(R.string.agrAsErr),Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onRestart () {
        super.onRestart();
        Toast.makeText(this, getString(R.string.asigRCargada), Toast.LENGTH_SHORT).show();
        adpAsig = null;
        adpAsig = new AdaptadorAsignaturas(this,arrayAsig);
        adpAsig.notifyDataSetChanged();
        listaAsignaturas.setAdapter(adpAsig);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
