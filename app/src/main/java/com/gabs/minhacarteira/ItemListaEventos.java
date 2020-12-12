package com.gabs.minhacarteira;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import modelo.Evento;

//classe que defune comportamento e informacoes de cada um dos itens da lista
public class ItemListaEventos extends ArrayAdapter<Evento> {

    private Context contextoPai;
    private ArrayList<Evento> eventos;

    public ItemListaEventos(Context contexto, ArrayList<Evento> dados) {
        super(contexto, R.layout.intem_lista_eventos, dados);

        this.contextoPai = contexto;
        this.eventos = dados;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }

    private static class viewHolder() {
        private TextView nomeTxt, valorTxt, dataTxt, repetirTxt, fotoTxt;
    }
}
