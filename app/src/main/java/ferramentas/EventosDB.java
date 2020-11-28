package ferramentas;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

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

    public void inserirEvento() {

        try (SQLiteDatabase db = this.getWritableDatabase()) {

            String sql = "INSERT into evento(nome, valor) VALUES ('evento1', 89)";
            db.execSQL(sql);

        } catch (SQLiteException ex) {

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
