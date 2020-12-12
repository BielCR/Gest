package com.gabs.minhacarteira;

import android.content.Context;
import android.view.LayoutInflater;
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
    public View getView(int indice, @Nullable View convertView, @NonNull ViewGroup parent) {
        //return super.getView(indice, convertView, parent);
        Evento eventoAtual = eventos.get(indice);

        final View resultado;
        viewHolder novaView;

        //caso 1 e quando a view esta sendo criada pela primeira vez
        if (convertView == null) {
            novaView = new viewHolder();

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.intem_lista_eventos, parent, false);

            //Link dos atributos com XML
            novaView.dataTxt = (TextView) convertView.findViewById(R.id.dataListaTxt);
            novaView.fotoTxt = (TextView) convertView.findViewById(R.id.fotoListaTxt);
            novaView.nomeTxt = (TextView) convertView.findViewById(R.id.nomeListaTxt);
            novaView.valorTxt = (TextView) convertView.findViewById(R.id.valorListaTxt);
            novaView.repetirTxt = (TextView) convertView.findViewById(R.id.repeteListaTxt);

            resultado = convertView;
            convertView.setTag(novaView);

        }
    }

    private static class viewHolder {
        private TextView nomeTxt, valorTxt, dataTxt, repetirTxt, fotoTxt;
    }
}
