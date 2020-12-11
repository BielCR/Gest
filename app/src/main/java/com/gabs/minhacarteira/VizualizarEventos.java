package com.gabs.minhacarteira;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class    VizualizarEventos extends AppCompatActivity {


    private TextView tituloTxt, totalTxt;
    private ListView listaEventosList;
    private Button novoBtn, cancelBtn;

    //Operacao = 0 - entrada
    //Operacao = 1 - saida
    private int operacao = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vizualizar_eventos);
        tituloTxt = (TextView) findViewById(R.id.tituloTxt);
        totalTxt = (TextView) findViewById(R.id.totalTxt);
        listaEventosList = (ListView) findViewById(R.id.listaEventos);
        novoBtn = (Button) findViewById(R.id.novoBtn);
        cancelBtn = (Button) findViewById(R.id.cancelBtn);
    }
}