package ferramentas;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

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

    public void atualizaEvento() {

    }

    public void buscaEvento() {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //ficara parado até a atualização da funcionalidade de upgrate
    }
}
