package tcc.meuscout.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import tcc.meuscout.util.Alerta;
import tcc.meuscout.util.SQLiteHelper;

public class RepositorioBanco {
    public static String LOG_TAG = "meuscout";
    private static String pastaSistema;
    public static SQLiteDatabase db;
    private static SQLiteHelper dbHelper;
    private static RepositorioBanco instance;
    private static int DATABASE_VERSION;
    private static String CTG_LOG = "REPOSITORIO_BANCO";
    protected static Context context;
    private static String[] scriptCreate;
    private static String[] scriptDrop;
    private static String[] scriptCargaDemonstracao;
    private static String[][] scriptUpdate;
    private static String dbName;
    private static String caminhoDb;
    public static String tipoCompilacao = "";

    protected RepositorioBanco() {
        if (db == null || db != null && !db.isOpen()) {
            this.openDatabase();
        }

    }

    public void executarSQL(String sql) {
        db.execSQL(sql);
    }

    public static boolean checkDataBase() {
        SQLiteDatabase checkDB = null;

        try {
            checkDB = SQLiteDatabase.openDatabase(caminhoDb + dbName, (SQLiteDatabase.CursorFactory)null, SQLiteDatabase.OPEN_READONLY);
            checkDB.close();
        } catch (SQLiteException var2) {
            Log.e(CTG_LOG, "Erro", var2);
        }

        return checkDB != null;
    }

    public void openDatabase() {
        this.closeDatabase();
        if (db != null && !db.isOpen()) {
            dbHelper = new SQLiteHelper(context, dbName, DATABASE_VERSION, scriptCreate, scriptDrop, scriptUpdate);
            db = dbHelper.getWritableDatabase();
        }

        boolean existDB = checkDataBase();
        if (!existDB || db == null) {
            dbHelper = new SQLiteHelper(context, dbName, DATABASE_VERSION, scriptCreate, scriptDrop, scriptUpdate);
            db = dbHelper.getWritableDatabase();
        }

        if (tipoCompilacao.equalsIgnoreCase("D")) {
            int qtdeScripts = scriptCargaDemonstracao.length;

            for(int i = 0; i < qtdeScripts; ++i) {
                String sql = scriptCargaDemonstracao[i];
                db.execSQL(sql);
            }
        }

        db = SQLiteDatabase.openDatabase(caminhoDb + dbName, (SQLiteDatabase.CursorFactory)null, 0);
        if (!db.isReadOnly()) {
            db.execSQL("PRAGMA foreign_keys = ON;");
        }

    }

    public void copyDatabase() throws IOException {
        InputStream myInput = context.getAssets().open(dbName);
        String outFileName = caminhoDb + dbName;
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];

        int length;
        while((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public void closeDatabase() {
        if (db != null) {
            db.close();
        }

        if (dbHelper != null) {
            dbHelper.close();
        }

    }

    public static RepositorioBanco getInstance() {
        if (instance == null) {
            instance = new RepositorioBanco();
        }

        return instance;
    }

    public void deleteDatabase() {
        try {
            this.closeDatabase();
            instance = null;
            File file = new File(caminhoDb + dbName + "-journal");
            if (file.exists()) {
                context.deleteFile(caminhoDb + dbName + "-journal");
            }

            if (context.deleteDatabase(dbName)) {
                Log.d(LOG_TAG, "deleteDatabase(): database deleted.");
            } else {
                Log.d(LOG_TAG, "deleteDatabase(): database NOT deleted.");
            }
        } catch (Exception var5) {
            Log.e(CTG_LOG, "Erro", var5);
            Alerta.gravarExcecao(CTG_LOG, var5);
        } finally {
            db = null;
            dbHelper = null;
        }

    }

    public static String[] getScriptCreate() {
        return scriptCreate;
    }

    public static void setScriptCreate(String[] scriptCreate) {
        RepositorioBanco.scriptCreate = scriptCreate;
    }

    public static String[][] getScriptUpdate() {
        return scriptUpdate;
    }

    public static void setScriptUpdate(String[][] scriptUpdate) {
        RepositorioBanco.scriptUpdate = scriptUpdate;
    }

    public static String[] getScriptDrop() {
        return scriptDrop;
    }

    public static void setScriptDrop(String[] scriptDrop) {
        RepositorioBanco.scriptDrop = scriptDrop;
    }

    public static String getDbName() {
        return dbName;
    }

    public static void setDbName(String dbName) {
        RepositorioBanco.dbName = dbName;
    }

    public static String getCaminhoDb() {
        return caminhoDb;
    }

    public static void setCaminhoDb(String caminhoDb) {
        RepositorioBanco.caminhoDb = caminhoDb;
    }

    public static String getPastaSistema() {
        return pastaSistema;
    }

    public static void setPastaSistema(String pastaSistema) {
        RepositorioBanco.pastaSistema = pastaSistema;
    }

    public static void setContext(Context c) {
        context = c;
    }

    public static int getDATABASE_VERSION() {
        return DATABASE_VERSION;
    }

    public static void setDATABASE_VERSION(int dATABASE_VERSION) {
        DATABASE_VERSION = dATABASE_VERSION;
    }

    public static String getTipoCompilacao() {
        return tipoCompilacao;
    }

    public static void setTipoCompilacao(String tipoCompilacao) {
        RepositorioBanco.tipoCompilacao = tipoCompilacao;
    }

    public static String[] getScriptCargaDemonstracao() {
        return scriptCargaDemonstracao;
    }

    public static void setScriptCargaDemonstracao(String[] scriptCargaDemonstracao) {
        RepositorioBanco.scriptCargaDemonstracao = scriptCargaDemonstracao;
    }
}