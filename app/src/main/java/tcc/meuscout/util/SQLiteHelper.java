package tcc.meuscout.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {
    private String[] scriptSQLCreate;
    private String[] scriptSQLDelete;
    private String[][] scripSQLUpdate;

    public SQLiteHelper(Context context, String nomeBanco, int versaoBanco, String[] scriptSQLCreate, String[] scriptSQLDelete, String[][] scripSQLUpdate) {
        super(context, nomeBanco, (SQLiteDatabase.CursorFactory)null, versaoBanco);
        this.scriptSQLCreate = scriptSQLCreate;
        this.scriptSQLDelete = scriptSQLDelete;
        this.scripSQLUpdate = scripSQLUpdate;
    }

    public void onCreate(SQLiteDatabase db) {
        int qtdeScripts = this.scriptSQLCreate.length;

        for(int i = 0; i < qtdeScripts; ++i) {
            String sql = this.scriptSQLCreate[i];
            db.execSQL(sql);
        }

    }

    public void onUpgrade(SQLiteDatabase db, int versaoAntiga, int novaVersao) {
        int qtdeScripts = this.scripSQLUpdate.length;

        for(int i = versaoAntiga + 1; i <= novaVersao; ++i) {
            for(int j = 0; j < this.scripSQLUpdate[i].length; ++j) {
                String sql = this.scripSQLUpdate[i][j];
                if (!sql.isEmpty()) {
                    db.execSQL(sql);
                }
            }
        }

        this.onCreate(db);
    }
}