package tcc.meuscout.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import tcc.meuscout.model.Time;
import tcc.meuscout.model.Usuario;

public class TimeBanco {


    private ContentValues valores(Time time) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("FIREBASE_ID", time.getFirebase_id());
        contentValues.put("USUARIO_ID_FIREBASE", time.getUsuario_id_firebase());
        contentValues.put("NOME", time.getNome());
        contentValues.put("SIGLA", time.getSigla());
        contentValues.put("TIME_USUARIO", time.getTime_usuario());
        return contentValues;
    }

    public int inserir(Time time, SQLiteDatabase database) {
        return (int) database.insertOrThrow(
                "TIME",
                null,
                valores(time));
    }

    public int atualizarTime(Time time, SQLiteDatabase database) throws Exception {
        String WHERE = "FIREBASE_ID = ?";
        String argumentos[] = {String.valueOf(time.getFirebase_id())};

        return database.update(
                "TIME",
                valores(time),
                WHERE,
                argumentos);
    }

    public int removerTime(Time time, SQLiteDatabase database) throws Exception {
        return database.delete("TIME", "USUARIO_ID_FIREBASE=? AND FIREBASE_ID=?", new String[]{
               time.getUsuario_id_firebase(), time.getFirebase_id()
        });
    }

    public int removerTimeUsuario(Usuario usuario, SQLiteDatabase database) throws Exception {
        return database.delete(
                "TIME",
                "USUARIO_ID=? AND TIME_ID_FIREBASE  =?",
                new String[]{
                        String.valueOf(usuario.getId()),
                        String.valueOf(usuario.getTime_nome()),
                        usuario.getTime_id_firebase()
                }
        );
    }

    @SuppressLint("Range")
    public Time recuperaTimePrincipalUsuario(SQLiteDatabase database, Usuario usuario) throws Exception {
        Time time = new Time();
        String SQL = "SELECT * FROM TIME" +
                " WHERE USUARIO_ID_FIREBASE = '" + usuario.getFirebase_id() + "'" +
                " AND TIME_USUARIO = 'S'";
        Cursor cursor = database.rawQuery(SQL, null);
        cursor.moveToFirst(); //Posiciona o cursor no início da tabela
        while (!cursor.isAfterLast()) {
            time = percorreLista(cursor);
            cursor.moveToNext();
        }
        return time;
    }

    @SuppressLint("Range")
    public Time recuperaTimePorId(SQLiteDatabase database, Usuario usuario, String firebaseId) throws Exception {
        Time time = new Time();
        String SQL = "SELECT * FROM TIME" +
                " WHERE USUARIO_ID_FIREBASE = '" + usuario.getFirebase_id() + "'" +
                " AND FIREBASE_ID = '" + firebaseId + "'";
        Cursor cursor = database.rawQuery(SQL, null);
        cursor.moveToFirst(); //Posiciona o cursor no início da tabela
        while (!cursor.isAfterLast()) {
            time = percorreLista(cursor);
            cursor.moveToNext();
        }
        return time;
    }

    @SuppressLint("Range")
    public List<Time> recuperaListaTimes(SQLiteDatabase database, Usuario usuario) throws Exception {
        List<Time> listaTime = new ArrayList<>();

        String SQL = "SELECT * FROM TIME" +
                " WHERE USUARIO_ID_FIREBASE = '" + usuario.getFirebase_id() + "'" +
                " ORDER BY TIME_USUARIO DESC, NOME ASC";
        Cursor cursor = database.rawQuery(SQL, null);
        cursor.moveToFirst(); //Posiciona o cursor no início da tabela
        while (!cursor.isAfterLast()) {
            listaTime.add(percorreLista(cursor));
            cursor.moveToNext();
        }
        return listaTime;
    }

    @SuppressLint("Range")
    public List<Time> recuperaListaTimesUsuario(SQLiteDatabase database, Usuario usuario) throws Exception {
        List<Time> listaTime = new ArrayList<>();

        String SQL = "SELECT * FROM TIME" +
                " WHERE USUARIO_ID_FIREBASE = '" + usuario.getFirebase_id() + "'" +
                " AND ADVERSARIO = 'N'";
        Cursor cursor = database.rawQuery(SQL, null);
        cursor.moveToFirst(); //Posiciona o cursor no início da tabela
        while (!cursor.isAfterLast()) {
            listaTime.add(percorreLista(cursor));
            cursor.moveToNext();
        }
        return listaTime;
    }


    @SuppressLint("Range")
    public List<Time> recuperaListaTimesAdversarios(SQLiteDatabase database, Usuario usuario) throws Exception {
        List<Time> listaTime = new ArrayList<>();

        String SQL = "SELECT * FROM TIME" +
                " WHERE USUARIO_ID_FIREBASE = '" + usuario.getFirebase_id() + "'" +
                " AND ADVERSARIO = 'S'";
        Cursor cursor = database.rawQuery(SQL, null);
        cursor.moveToFirst(); //Posiciona o cursor no início da tabela
        while (!cursor.isAfterLast()) {
            listaTime.add(percorreLista(cursor));
            cursor.moveToNext();
        }
        return listaTime;
    }

    @SuppressLint("Range")
    private Time percorreLista(Cursor cursor) {
        Time time = new Time();
        time.setId(cursor.getInt(cursor.getColumnIndex("ID")));
        time.setFirebase_id(cursor.getString(cursor.getColumnIndex("FIREBASE_ID")));
        time.setUsuario_id_firebase(cursor.getString(cursor.getColumnIndex("USUARIO_ID_FIREBASE")));
        time.setNome(cursor.getString(cursor.getColumnIndex("NOME")));
        time.setSigla(cursor.getString(cursor.getColumnIndex("SIGLA")));
        time.setTime_usuario(cursor.getString(cursor.getColumnIndex("TIME_USUARIO")));
        return time;
    }


}
