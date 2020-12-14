package com.gabs.minhacarteira;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

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
        ViewHolder novaView;

        //caso 1 e quando a view esta sendo criada pela primeira vez
        if (convertView == null) {
            novaView = new ViewHolder();

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

        }else{
            //caso 2 é quando a view está sendo modificada
            novaView = (ViewHolder) convertView.getTag();
            resultado = convertView;
        }
        //setando todos os resultados
        novaView.nomeTxt.setText(eventoAtual.getNome());
        novaView.valorTxt.setText(eventoAtual.getValor()+"");
        novaView.fotoTxt.setText(eventoAtual.getCaminhoFoto() == null ? "Não" : "Sim");
        SimpleDateFormat formataData = new SimpleDateFormat("dd/MM/yyyy");
        novaView.dataTxt.setText(formataData.format(eventoAtual.getOcorreu()));

            //evento repete
        Calendar data1 = Calendar.getInstance();
        data1.setTime(eventoAtual.getOcorreu());
        Calendar data2 = Calendar.getInstance();
        data2.setTime(eventoAtual.getValida());
        novaView.repetirTxt.setText(data1.get(Calendar.MONTH) != data2.get(Calendar.MONTH) ? "Sim" : "Não");

        return resultado;
    }

    private static class ViewHolder {
        private TextView nomeTxt, valorTxt, dataTxt, repetirTxt, fotoTxt;
    }
}
