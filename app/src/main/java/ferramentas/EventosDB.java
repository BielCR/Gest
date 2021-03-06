package ferramentas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.util.Calendar;

import java.util.ArrayList;
import java.util.Date;

import modelo.Evento;

public class EventosDB extends SQLiteOpenHelper {

    public Context contexto;

    public EventosDB(Context conte) {
        super(conte, "evento", null, 1);
        contexto = conte;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String criaTb = "CREATE TABLE IF NOT EXISTS evento(id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT," +
                " valor REAL, imagem TEXT, dataocorreu DATE, datacadastro DATE, datavalida DATE)";

        db.execSQL(criaTb);
    }

    public void inserirEvento(Evento novoEvento) {

        try (SQLiteDatabase db = this.getWritableDatabase()) {

            ContentValues valores = new ContentValues();

            valores.put("nome", novoEvento.getNome());
            valores.put("valor", novoEvento.getValor());
            valores.put("imagem", novoEvento.getCaminhoFoto());
            valores.put("dataocorreu", novoEvento.getOcorreu().getTime());
            valores.put("datacadastro", new Date().getTime());
            valores.put("datavalida", novoEvento.getValida().getTime());

            db.insert("evento", null, valores);


        } catch (SQLiteException ex) {
            ex.printStackTrace();
        }

    }

    public void updateEvento(Evento eventoAtualizado) {
        try(SQLiteDatabase db = this.getWritableDatabase()){

            ContentValues valores = new ContentValues();
            valores.put("nome", eventoAtualizado.getNome());
            valores.put("valor", eventoAtualizado.getValor());
            valores.put("Imagem", eventoAtualizado.getCaminhoFoto());
            valores.put("dataocorreu",eventoAtualizado.getOcorreu().getTime());
            valores.put("datavalida", eventoAtualizado.getValida().getTime());

            db.update("evento", valores, "id = ?", new String[]{eventoAtualizado.getId()+""});

        }catch (SQLiteException ex){
            System.err.println("Erro na atualização do Banco de Dados");
            ex.printStackTrace();
        }
    }

    public Evento buscaEventoID(int idEvento) {

        //sql da busca pelo id recebido
        String sql = "SELECT * FROM evento WHERE id = " + idEvento;

        //realizando a busca no banco
        Evento result = null;

        //extraindo as informacoes
        try (SQLiteDatabase db = this.getWritableDatabase()) {

            Cursor tupla = db.rawQuery(sql, null);
            if (tupla.moveToFirst()) {

                String nome = tupla.getString(1);
                double valor = tupla.getDouble(2);
                String imagem = tupla.getString(3);
                Date dateOcorreu = new Date(tupla.getLong(4));
                Date dateCadastro = new Date(tupla.getLong(5));
                Date dateValida = new Date(tupla.getLong(6));

                if (valor < 0) {
                    valor *= -1;
                }

                result = new Evento(nome, imagem, valor, dateCadastro, dateValida, dateOcorreu, idEvento);
            }
        } catch (SQLiteException ex) {
            //mensagem de erro do banco de dados
            System.err.println("Erro na consulta ao banco pelo id");
            ex.printStackTrace();
        }

        return result;

    }

    public ArrayList<Evento> buscaEvento(int op, Calendar data) {

        ArrayList<Evento> resultado = new ArrayList<>();

        //definindo o primeiro dia do mês
        Calendar dia1 = Calendar.getInstance();
        dia1.setTime(data.getTime());
        dia1.set(Calendar.DAY_OF_MONTH, 1);
        dia1.set(Calendar.HOUR, -12);
        dia1.set(Calendar.MINUTE, 0);
        dia1.set(Calendar.SECOND, 0);

        //definindo o ultimo dia do mes
        Calendar ultimoDia = Calendar.getInstance();
        ultimoDia.setTime(data.getTime());
        ultimoDia.set(Calendar.DAY_OF_MONTH, ultimoDia.getActualMaximum(Calendar.DAY_OF_MONTH));
        ultimoDia.set(Calendar.HOUR, 11);
        ultimoDia.set(Calendar.MINUTE, 59);
        ultimoDia.set(Calendar.SECOND, 59);
        ultimoDia.set(Calendar.MILLISECOND, 999);


        String sql = "SELECT * FROM evento WHERE ((datavalida <= " + ultimoDia.getTime().getTime() +
                " AND datavalida >= " + dia1.getTime().getTime() + ") OR (dataocorreu <= " + ultimoDia.getTime().getTime() +
                " AND datavalida >= " + dia1.getTime().getTime() + "))";
        sql += " AND valor ";

        if (op == 0) {
            //entradas
            sql += ">= 0";
        } else {
            //saidas (valor negativo)
            sql += "<= 0";
        }


        try (SQLiteDatabase db = this.getWritableDatabase()) {

            Cursor tuplas = db.rawQuery(sql, null);

            //efetuar leitura das tuplas
            if (tuplas.moveToFirst()) {
                do {
                    int id = tuplas.getInt(0);
                    String nome = tuplas.getString(1);
                    double valor = tuplas.getDouble(2);
                    String imagem = tuplas.getString(3);
                    Date dateOcorreu = new Date(tuplas.getLong(4));
                    Date dateCadastro = new Date(tuplas.getLong(5));
                    Date dateValida = new Date(tuplas.getLong(6));

                    if (valor < 0) {
                        valor *= -1;
                    }

                    Evento temp = new Evento(nome, imagem, valor, dateCadastro, dateValida, dateOcorreu, id);
                    resultado.add(temp);

                } while (tuplas.moveToNext());
            }

        } catch (SQLiteException ex) {
            System.err.println("Ocorreu um erro na consulta do banco de dados");
            ex.printStackTrace();
        }
        return resultado;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //ficara parado até a atualização da funcionalidade de upgrate
    }
}
