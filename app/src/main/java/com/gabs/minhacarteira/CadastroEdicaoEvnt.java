package com.gabs.minhacarteira;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import ferramentas.EventosDB;
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
                data.setText(dia + "/" + (mes + 1) + "/" + ano);
            }
        }, calendarioTemp.get(Calendar.YEAR), calendarioTemp.get(Calendar.MONTH), calendarioTemp.get(Calendar.DAY_OF_MONTH));

        data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarioUser.show();
            }
        });

        salvarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadastrarNovoEvento();
            }
        });
    }

    private void cadastrarNovoEvento() {

        String nome = this.nome.getText().toString();
        double valor = Double.parseDouble(this.valor.getText().toString());
        if (acao == 1 || acao == 3) {
            valor *= -1;
        }

        SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");
        String dataStr = this.data.getText().toString();
        try {
            Date diaEvento = formatador.parse(dataStr);
            //um novo calendario para setar a data limite (repeticao)
            Calendar dataLimite = Calendar.getInstance();
            dataLimite.setTime(calendarioTemp.getTime());

            //verificando se esse evento ira repetir por alguns meses(VOLTAR)
            if (repeteBtn.isChecked()) {
                //por enquanto estamos consif=derando apenas um mes

            }

            //setando ate o ultimo dia do mes limite
            dataLimite.set(Calendar.DAY_OF_MONTH, dataLimite.getActualMaximum(Calendar.DAY_OF_MONTH));

            Evento novoEvento = new Evento(nome, null, valor, new Date(), dataLimite.getTime(), diaEvento);

            //inserir esse evento no banco de dados
            EventosDB bd = new EventosDB(CadastroEdicaoEvnt.this);
            bd.inserirEvento(novoEvento);
            Toast.makeText(CadastroEdicaoEvnt.this, "Cadastro realizado com sucesso!", Toast.LENGTH_LONG).show();
            finish();
        } catch (ParseException ex) {
            System.err.println("Erro no formato da data...");
        }
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