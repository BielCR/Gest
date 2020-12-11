package com.gabs.minhacarteira;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class VisualizarEventos extends AppCompatActivity {


    private DatePickerDialog calendarioUser;
    private TextView tituloTxt, totalTxt;
    private ListView listaEventosList;
    private Button novoBtn, cancelBtn;

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
    }

    private void cadastrarEventos() {
        novoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Ajuste de qual operacao executar
                //adicionar put extra
                if (operacao != -1) {
                    Intent trocaAct = new Intent(VisualizarEventos.this, CadastroEdicaoEvnt.class);

                    if (operacao == 0) {
                        trocaAct.putExtra("acao", 0);
                    } else {
                        trocaAct.putExtra("acao", 1);
                    }
                    startActivity(trocaAct);
                }
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
}