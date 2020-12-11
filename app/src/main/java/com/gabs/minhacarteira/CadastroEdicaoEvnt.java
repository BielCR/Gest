package com.gabs.minhacarteira;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.SimpleTimeZone;

import modelo.Evento;

public class CadastroEdicaoEvnt extends AppCompatActivity {

    private DatePickerDialog calendarioUser;

    private TextView titulo, data;
    private EditText nome, valor;
    private CheckBox repeteBtn;
    private ImageView foto;
    private Button fotoBtn, salvarBtn, cancelarBtn;
    private Calendar calendarioTemp;


    /*
    0 - Cadastra entrada
    1 - Cadastra saida
    2 - Edita entrada
    3 - Edita saida
     */
    private int acao = -1;

    private void cadastrarEventos() {
    //Configuracao do datePicker
        calendarioTemp = Calendar.getInstance();
        calendarioUser = new DatePickerDialog(CadastroEdicaoEvnt.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int ano, int mes, int dia) {
                calendarioTemp.set(ano, mes, dia);
                data.setText(dia+"/"+(mes+1)+"/"+ano);
            }
        }, calendarioTemp.get(Calendar.YEAR), calendarioTemp.get(Calendar.MONTH), calendarioTemp.get(Calendar.DAY_OF_MONTH));

        data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarioUser.show();
            }
        });
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
        acao = intencao.getIntExtra("acao", -1);

        ajustaOperacao();
        cadastrarEventos();
    }

    //metodo que auxilia na reutilização da activity, altera valores dos componentes reutilizaveis
    private void ajustaOperacao() {

        //Recuperar a data de hoje
        Calendar hoje = Calendar.getInstance();
        SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");
        data.setText(formatador.format(hoje.getTime()));

        switch (acao) {
            case 0:
                //entradas
                titulo.setText("Cadastro de Entrada");
                break;
            case 1:
                //saidas
                titulo.setText("Cadastro de Saída");
                break;
            case 2:
                //edicao de entradas
                titulo.setText("Edição de Entrada");
                break;
            case 3:
                //edicao de saidas
                titulo.setText("Edição de Saída");
                break;
            default:
        }
    }
}