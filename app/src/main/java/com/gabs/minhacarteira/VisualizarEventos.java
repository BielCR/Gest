package com.gabs.minhacarteira;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import ferramentas.EventosDB;
import modelo.Evento;

public class VisualizarEventos extends AppCompatActivity {

    private TextView tituloTxt, totalTxt;
    private ListView listaEventosList;
    private Button novoBtn, cancelBtn;

    private ArrayList<Evento> eventos;
    private ItemListaEventos adapter;

    //Operacao = 0 - entrada
    //Operacao = 1 - saida
    private int operacao = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_eventos);
        tituloTxt = (TextView) findViewById(R.id.tituloTxt);
        totalTxt = (TextView) findViewById(R.id.totalTxt);
        listaEventosList = (ListView) findViewById(R.id.listaEventos);
        novoBtn = (Button) findViewById(R.id.novoBtn);
        cancelBtn = (Button) findViewById(R.id.cancelBtn);

        Intent intencao = getIntent();
        operacao = intencao.getIntExtra("acao", -1);

        ajustaOperacao();
        cadastrarEventos();
        carregaEventosLista();
    }

    private void cadastrarEventos() {
        novoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (operacao != -1) {
                    Intent trocaAct = new Intent(VisualizarEventos.this, CadastroEdicaoEvnt.class);

                    if (operacao == 0) {
                        trocaAct.putExtra("acao", 0);
                        startActivityForResult(trocaAct, 0);
                    } else {
                        trocaAct.putExtra("acao", 1);
                        startActivityForResult(trocaAct, 1);
                    }

                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void ajustaOperacao() {
        //busca no banco a respeito dos eventos existentes na lista

        if (operacao == 0) {
            tituloTxt.setText("Entradas");
        } else {
            if (operacao == 1) {
                tituloTxt.setText("Saídas");
            } else {
                //erro na configuracao da intent
                Toast.makeText(VisualizarEventos.this, "erro no parametro acao", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void carregaEventosLista() {
        eventos = new ArrayList<>();

        //busca de eventos no banco de dados
        //.add(new Evento("loja", null, 10.60, new Date(), new Date(), new Date()));
        //eventos.add(new Evento("bar", null, 100, new Date(), new Date(), new Date()));

        EventosDB db = new EventosDB(VisualizarEventos.this);
        eventos = db.buscaEvento(operacao, MainActivity.dataApp);


        adapter = new ItemListaEventos(getApplicationContext(), eventos);
        listaEventosList.setAdapter(adapter);

        listaEventosList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int indice, long id) {

                Evento eventoSelceionado = eventos.get(indice);
                Intent novoFluxo = new Intent(VisualizarEventos.this, CadastroEdicaoEvnt.class);

                if (operacao == 0) {
                    //edicao de entrada
                    novoFluxo.putExtra("acao", 2);
                } else {
                    //edicao de saida
                    novoFluxo.putExtra("acao", 3);
                }

                novoFluxo.putExtra("id", eventoSelceionado.getId()+"");

                //iniciando
                startActivityForResult(novoFluxo, operacao);

            }
        });


        double total = 0.0;
        for (Evento i : eventos) {
            total += i.getValor();
        }

        totalTxt.setText(String.format("%.2f", total));

    }

    protected void onActivityResult(int codReq, int codResult, Intent data) {
        super.onActivityResult(codReq, codResult, data);
        carregaEventosLista();
    }
}