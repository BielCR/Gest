package com.gabs.minhacarteira;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;

import org.w3c.dom.Text;

public class CadastroEdicaoEvnt extends AppCompatActivity {


    private TextView titulo, data;
    private EditText nome, valor;
    private CheckBox repeteBtn;
    private ImageView foto;
    private Button fotoBtn, salvarBtn, cancelarBtn;


    /*
    0 - Cadastra entrada
    1 - Cadastra saida
    2 - Edita entrada
    3 - Edita saida
     */
    private  int acao = -1;
    private void cadastrarEventos(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_edicao_evnt);

        titulo = (TextView) findViewById(R.id.tituloCadastroTxt);
        data = (TextView) findViewById(R.id.dataCadastroTxt);
        nome = (EditText) findViewById(R.id.nomeCadastroTxt);
        valor = (EditText) findViewById(R.id.valorCadastroTxt);
        repeteBtn = (CheckBox) findViewById(R.id.repeteCadastroCheck);
        foto = (ImageView) findViewById(R.id.fotoCadastroImg);
        fotoBtn = (Button) findViewById(R.id.fotoCadastroBtn);
        salvarBtn = (Button) findViewById(R.id.salvarCadastroBtn);
        cancelarBtn = (Button) findViewById(R.id.cancelarCadastroBtn);

        Intent intencao = getIntent();
        acao = intencao.getIntExtra("acao",-1);
    }
}