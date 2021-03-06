package com.gabs.minhacarteira;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

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
    private Spinner mesesRepete;


    /*
    0 - Cadastra entrada
    1 - Cadastra saida
    2 - Edita entrada
    3 - Edita saida
     */
    private int acao = -1;
    private Evento eventoSelect;
    private String nomeFoto;

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
        mesesRepete = (Spinner) findViewById(R.id.mesesCadastroSpinner);

        Intent intencao = getIntent();
        acao = intencao.getIntExtra("acao", -1);

        ajustaOperacao();
        cadastrarEventos();
        confuguraSpinner();
    }

    private void confuguraSpinner() {
        List<String> meses = new ArrayList<String>();

        //Apenas 24 meses de repeticao
        for (int i = 1; i <= 24; i++) {
            meses.add(i + "");
        }

        ArrayAdapter<String> listaAdapter = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item, meses);

        mesesRepete.setAdapter(listaAdapter);
        mesesRepete.setEnabled(false);
    }

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

        repeteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (repeteBtn.isChecked()) {
                    mesesRepete.setEnabled(true);
                } else {
                    mesesRepete.setEnabled(false);
                }
            }
        });

        salvarBtn.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                if (acao < 2) {
                    //cadastra um novo evento no banco de dados
                    cadastrarNovoEvento();
                } else {
                    //realiza um update no evento existente
                    updateEvento();
                }
            }
        });


        cancelarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (acao < 2) {
                    //termina e execucao de uma act e volta a anterior caso a ação seja de cancelamento
                    finish();
                } else {
                    //chama o metodo de delete do evento no banco de dados
                }
            }
        });

        fotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraAtc = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                startActivityForResult(cameraAtc, 100);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Bitmap imagemUser = (Bitmap) data.getExtras().get("data");
            foto.setImageBitmap(imagemUser);
            foto.setBackground(null);

            salvarImagem(imagemUser);
        }
    }

    private void salvarImagem(Bitmap imagemSalva) {
        Random gerador = new Random();
        Date instante = new Date();

        //definindo o nome da imagem (arquivo)
        String nome = gerador.nextInt() + "" + instante.getTime() + ".png";

        nomeFoto = nome;

        File sd = Environment.getExternalStorageDirectory();
        File fotoArquivo = new File(sd, nome);

        //armazenando no dispositivo
        try {
            FileOutputStream gravador = new FileOutputStream(fotoArquivo);
            imagemSalva.compress(Bitmap.CompressFormat.PNG, 100, gravador);
            gravador.flush();
            gravador.close();

        } catch (Exception ex) {
            System.err.println("Erro ao armazenar a imagem");
            ex.printStackTrace();
        }
    }

    //metodo chamado durante a edicao de algum evento
    private void carregarImagem() {
        if (nomeFoto != null) {
            File sd = Environment.getExternalStorageDirectory();
            File arquivoLeitura = new File(sd, nomeFoto);

            try {

                FileInputStream leitor = new FileInputStream(arquivoLeitura);
                Bitmap img = BitmapFactory.decodeStream(leitor);

                foto.setImageBitmap(img);
                foto.setBackground(null);

            } catch (Exception ex) {
                System.err.println("Erro na leitura da imagem!");
                ex.printStackTrace();
            }
        }
    }

    //Metodo de atualizacao de um evento
    public void updateEvento() {
        //setando o nome e o valor do evento selecionado
        eventoSelect.setNome(nome.getText().toString());
        eventoSelect.setValor(Double.parseDouble(this.valor.getText().toString()));
        //se a acao == 3, esta seno realizado um evento de saida, ou seja, o valor tem que ser multiplicado por -1
        if (acao == 3) {
            eventoSelect.setValor(eventoSelect.getValor() * -1);
        }

        eventoSelect.setOcorreu(calendarioTemp.getTime());
        //um novo calendario para setar a data limite (repeticao)
        Calendar dataLimite = Calendar.getInstance();
        dataLimite.setTime(calendarioTemp.getTime());

        //verificando se esse evento ira repetir por alguns meses(VOLTAR)
        if (repeteBtn.isChecked()) {
            //definindo o mes limite
            String mesLimite = (String) mesesRepete.getSelectedItem();
            dataLimite.add(Calendar.MONTH, Integer.parseInt(mesLimite));
        }

        //setando ate o ultimo dia do mes limite
        dataLimite.set(Calendar.DAY_OF_MONTH, dataLimite.getActualMaximum(Calendar.DAY_OF_MONTH));
        eventoSelect.setValida(dataLimite.getTime());
        eventoSelect.setCaminhoFoto(nomeFoto);

        EventosDB db = new EventosDB(CadastroEdicaoEvnt.this);
        db.updateEvento(eventoSelect);
        finish();
    }

    private void cadastrarNovoEvento() {

        String nome = this.nome.getText().toString();
        double valor = Double.parseDouble(this.valor.getText().toString());
        if (acao == 1 || acao == 3) {
            valor *= -1;
        }

        //SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");
        // dataStr = this.data.getText().toString();
        // try {
        Date diaEvento = calendarioTemp.getTime();
        //um novo calendario para setar a data limite (repeticao)
        Calendar dataLimite = Calendar.getInstance();
        dataLimite.setTime(calendarioTemp.getTime());

        //verificando se esse evento ira repetir por alguns meses(VOLTAR)
        if (repeteBtn.isChecked()) {
            //definindo o mes limite
            String mesLimite = (String) mesesRepete.getSelectedItem();
            dataLimite.add(Calendar.MONTH, Integer.parseInt(mesLimite));

        }

        //setando ate o ultimo dia do mes limite
        dataLimite.set(Calendar.DAY_OF_MONTH, dataLimite.getActualMaximum(Calendar.DAY_OF_MONTH));

        Evento novoEvento = new Evento(nome, nomeFoto, valor, new Date(), dataLimite.getTime(), diaEvento);

        //inserir esse evento no banco de dados
        EventosDB bd = new EventosDB(CadastroEdicaoEvnt.this);
        bd.inserirEvento(novoEvento);
        Toast.makeText(CadastroEdicaoEvnt.this, "Cadastro realizado com sucesso!", Toast.LENGTH_LONG).show();
        finish();
        //} catch (ParseException ex) {
        System.err.println("Erro no formato da data...");
        //}
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
                ajustaEdicao();
                break;
            case 3:
                //edicao de saidas
                titulo.setText("Edição de Saída");
                ajustaEdicao();
                break;
            default:
        }
    }

    private void ajustaEdicao() {
        cancelarBtn.setText("Excluir");
        salvarBtn.setText("Atualizar");

        int id = Integer.parseInt(getIntent().getStringExtra("id"));
        if (id != 0) {
            EventosDB db = new EventosDB(CadastroEdicaoEvnt.this);
            eventoSelect = db.buscaEventoID(id);

            //carregar as informacoes nos campos de edicao
            SimpleDateFormat formatador = new SimpleDateFormat("dd/mm/yyyy");

            nome.setText(eventoSelect.getNome());
            valor.setText(eventoSelect.getValor() + "");
            data.setText(formatador.format(eventoSelect.getOcorreu()));

            //carregando o nome da foto e exibindo logo em seguida
            nomeFoto = eventoSelect.getCaminhoFoto();
            carregarImagem();

            Calendar dataValida = Calendar.getInstance();
            dataValida.setTime(eventoSelect.getValida());

            Calendar dataOcorreu = Calendar.getInstance();
            dataOcorreu.setTime(eventoSelect.getOcorreu());

             repeteBtn.setChecked(dataValida.get(Calendar.MONTH) != dataOcorreu.get(Calendar.MONTH) ? true : false);
            if (repeteBtn.isChecked()) {
                mesesRepete.setEnabled(true);
                mesesRepete.setSelection(dataValida.get(Calendar.MONTH) - dataOcorreu.get(Calendar.MONTH) - 1);
            }
        }
    }
}

