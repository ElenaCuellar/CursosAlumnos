package com.example.caxidy.cursosalumnos;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AdaptadorAsignaturas extends BaseAdapter {
    private ArrayList<Asignatura> lista;
    private final Activity actividad;
    public AdaptadorAsignaturas(Activity a, ArrayList<Asignatura> v){
        super();
        this.lista = v;
        this.actividad = a;
    }

    @Override
    public int getCount() {
        return lista.size();
    }
    @Override
    public Object getItem(int arg0) {
        return lista.get(arg0);
    }
    @Override
    public long getItemId(int arg0) {
        return lista.get(arg0).getID();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater ly = actividad.getLayoutInflater();
        View view = ly.inflate(R.layout.asignatura, null, true);

        TextView tAsig= (TextView) view.findViewById(R.id.itemAsig);
        tAsig.setText(lista.get(position).getNombre());

        return view;
    }
}
