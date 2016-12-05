/*-Localizar todos los String
-Navigation Drawer con ListView: en el drawer, cuando pulsas un item d asignatura sale un dialog en el que pone
el curso en el que se da y el profesor que la imparte.*/

/*Actividades finales 2 y 3: uso de ExpandableListView y Navigation Drawer que contenga un ListView + uso de base de datos */

package com.example.caxidy.cursosalumnos;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ExpandableListView listaExpandable;
    ExpandableListAdapter adaptadorExpandable;
    List<String> listaCabeceras;
    HashMap<String,List<String>> listaHijos;
    BDCursos bd;

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
                Toast.makeText(getApplicationContext(), "Alumno: " + listaHijos.get(listaCabeceras.get(groupPosition)).get(childPosition),
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        //Listener para cuando se expande un grupo:
        listaExpandable.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(), "El grupo "+listaCabeceras.get(groupPosition) + " se ha abierto",
                        Toast.LENGTH_SHORT).show();
            }
        });
        //Listener al contraer un grupo:
        listaExpandable.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(), "El grupo "+listaCabeceras.get(groupPosition) + " se ha cerrado",
                        Toast.LENGTH_SHORT).show();
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
                    Toast.makeText(getApplicationContext(), "Error al cargar alumnos del grupo", Toast.LENGTH_SHORT).show();
            }
        }
        else
            Toast.makeText(getApplicationContext(), "Error al cargar los cursos", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getApplicationContext(),"Cursos agregados correctamente",Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getApplicationContext(),"Error al agregar cursos",Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getApplicationContext(),"Alumnos agregados correctamente",Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getApplicationContext(),"Error al agregar a los alumnos",Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Opciones del menu
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        //Opciones del drawer
        int id = item.getItemId();

        if (id == R.id.nav_camera) {

        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
