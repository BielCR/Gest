package com.gabs.minhacarteira;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

import ferramentas.EventosDB;
import modelo.Evento;

public class MainActivity extends AppCompatActivity {

    static Calendar dataApp;
    private TextView titulo, entrada, saida, saldo;
    private ImageButton entradaBtn, saidaBtn;
    private Button anteriorBtn, proximoBtn, novoBtn;
    private Calendar hoje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Linkagem dos componentes java e xml
        //Text Views
        titulo = findViewById(R.id.tituloMain);
        entrada = findViewById(R.id.entradatxt);
        saida = findViewById(R.id.saidaTxt);
        saldo = findViewById(R.id.saldoTxt);

        //Image Buttons
        entradaBtn = findViewById(R.id.entradaBtn);
        saidaBtn = findViewById(R.id.saidaBtn);

        //Buttons
        anteriorBtn = findViewById(R.id.anteriorBtn);
        proximoBtn = findViewById(R.id.proximoBtn);
        novoBtn = findViewById(R.id.novoBtn);

        //responsavel por mostrar todos os eventos de botoes
        cadastroEventos();

        //calendars
        dataApp = Calendar.getInstance();
        hoje = Calendar.getInstance();


        //Mostrar a data no app
        mostraDataApp();

        //atualizando os valores na tela inicial
        atualizaValores();

        //verificando e configurando as permicoes do usuario
        configPer();

    }

    private void configPer(){
        if(ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
        || ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        || ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        }
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
                //EventosDB db = new EventosDB(MainActivity.this);
                //db.inserirEvento();

                //Toast.makeText(MainActivity.this, db.getDatabaseName(), Toast.LENGTH_LONG).show();
            }
        });

        entradaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent trocaAct = new Intent(MainActivity.this, VisualizarEventos.class);
                trocaAct.putExtra("acao", 0);
                //iniciamos a activitie passada como parametro
                startActivityForResult(trocaAct, 0);
            }
        });

        saidaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent trocaAct = new Intent(MainActivity.this, VisualizarEventos.class);
                trocaAct.putExtra("acao", 1);
                //iniciamos a activitie passada como parametro
                startActivityForResult(trocaAct, 1);
            }
        });
    }

    //Mostrar a data do aplicativo
    private void mostraDataApp() {
        String[] nomeMes = {"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
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
        atualizaValores();
    }

    private void atualizaValores() {
        //buscando entradas e saidas no banco de dados
        EventosDB db = new EventosDB(MainActivity.this);

        //armazenando informações em Arrays
        ArrayList<Evento> saidas = db.buscaEvento(1, dataApp);
        ArrayList<Evento> entradas = db.buscaEvento(0, dataApp);

        double entradasTotal = 0.0, saidasTotal = 0.0;

        //somando valores de entrada e saida no laco de repeticao
        for (Evento i : entradas) {
            entradasTotal += i.getValor();
        }
        for (Evento i : saidas) {
            saidasTotal += i.getValor();
        }

        //exibindo na aplicacao os valores totais
        double totalSaldo = entradasTotal - saidasTotal;
        entrada.setText(String.format("%.2f", entradasTotal));
        saida.setText(String.format("%.2f", saidasTotal));
        saldo.setText(String.format("%.2f", totalSaldo));

    }

    protected void onActivityResult(int codReq, int codResult, Intent data) {
        super.onActivityResult(codReq, codResult, data);
        atualizaValores();
    }

}