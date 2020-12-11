package com.gabs.minhacarteira;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import ferramentas.EventosDB;

public class MainActivity extends AppCompatActivity {

    private TextView titulo, entrada, saida, saldo;
    private ImageButton entradaBtn, saidaBtn;
    private Button anteriorBtn, proximoBtn, novoBtn;

    private Calendar hoje, dataApp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Linkagem dos componentes java e xml
        //Text Views
        titulo = (TextView) findViewById(R.id.tituloMain);
        entrada = (TextView) findViewById(R.id.entradatxt);
        saida = (TextView) findViewById(R.id.saidaTxt);
        saldo = (TextView) findViewById(R.id.saldoTxt);

        //Image Buttons
        entradaBtn = (ImageButton) findViewById(R.id.entradaBtn);
        saidaBtn = (ImageButton) findViewById(R.id.saidaBtn);

        //Buttons
        anteriorBtn = (Button) findViewById(R.id.anteriorBtn);
        proximoBtn = (Button) findViewById(R.id.proximoBtn);
        novoBtn = (Button) findViewById(R.id.novoBtn);

        //responsavel por mostrar todos os eventos de botoes
        cadastroEventos();

        //calendars
        dataApp = Calendar.getInstance();
        hoje = Calendar.getInstance();


        //Mostrar a data no app
        mostraDataApp();

    }

    //cadastrar eventos
    private void cadastroEventos() {
        anteriorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                atualizaMes(-1);
            }
        });
        proximoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                atualizaMes(1);
            }
        });
        novoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventosDB db = new EventosDB(MainActivity.this);
                db.inserirEvento();

                Toast.makeText(MainActivity.this, db.getDatabaseName(), Toast.LENGTH_LONG).show();
            }
        });

        entradaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent trocaAct = new Intent(MainActivity.this, VizualizarEventos.class);
                trocaAct.putExtra("acao", 0);
                startActivity(trocaAct);
            }
        });

        saidaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent trocaAct = new Intent(MainActivity.this, VizualizarEventos.class);
                trocaAct.putExtra("acao", 1);
                startActivity(trocaAct);
            }
        });
    }

    //Mostrar a data do aplicativo
    private void mostraDataApp() {
        String nomeMes[] = {"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
                "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"};

        int mes = dataApp.get(Calendar.MONTH);
        int ano = dataApp.get(Calendar.YEAR);

        titulo.setText(nomeMes[mes] + "/" + ano);

    }

    //atualiza o mes
    private void atualizaMes(int ajuste) {

        dataApp.add(Calendar.MONTH, ajuste);

        //proximo mes não pode passar do mes atual
        if (ajuste > 0) {
            if (dataApp.after(hoje)) {
                dataApp.add(Calendar.MONTH, -1);
            }
        }
        //temos que realizar uma busca no banco de dados pa avaliar se exitem meses anteriores cadastrados
        mostraDataApp();
    }


}