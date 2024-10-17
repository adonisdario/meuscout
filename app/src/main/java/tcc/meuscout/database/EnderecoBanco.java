package tcc.meuscout.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import tcc.meuscout.model.Endereco;
import tcc.meuscout.model.Time;
import tcc.meuscout.model.Usuario;

public class EnderecoBanco {


    private ContentValues valores(Endereco endereco) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("FIREBASE_ID", endereco.getFirebase_id());
        contentValues.put("USUARIO_ID_FIREBASE", endereco.getUsuario_id_firebase());
        contentValues.put("NOMELOCAL", endereco.getNomelocal());
        contentValues.put("ENDERECO", endereco.getEndereco());
        return contentValues;
    }

    public int inserir(Endereco endereco, SQLiteDatabase database) {
        return (int) database.insertOrThrow(
                "ENDERECO",
                null,
                valores(endereco));
    }

    public int atualizarEndereco(Endereco endereco, SQLiteDatabase database) throws Exception {
        String WHERE = "FIREBASE_ID = ?";
        String argumentos[] = {String.valueOf(endereco.getFirebase_id())};

        return database.update(
                "ENDERECO",
                valores(endereco),
                WHERE,
                argumentos);
    }

    public int removerTime(Endereco endereco, SQLiteDatabase database) throws Exception {
        return database.delete("ENDERECO", "USUARIO_ID_FIREBASE=? AND FIREBASE_ID=?", new String[]{
               endereco.getUsuario_id_firebase(), endereco.getFirebase_id()
        });
    }

    @SuppressLint("Range")
    public Endereco recuperaEnderecoPorId(SQLiteDatabase database, Usuario usuario, String firebaseId) throws Exception {
        Endereco endereco = new Endereco();
        String SQL = "SELECT * FROM ENDERECO" +
                " WHERE USUARIO_ID_FIREBASE = '" + usuario.getFirebase_id() + "'" +
                " AND FIREBASE_ID = '" + firebaseId + "'";
        Cursor cursor = database.rawQuery(SQL, null);
        cursor.moveToFirst(); //Posiciona o cursor no início da tabela
        while (!cursor.isAfterLast()) {
            endereco = percorreLista(cursor);
            cursor.moveToNext();
        }
        return endereco;
    }

    @SuppressLint("Range")
    public List<Endereco> recuperaListaEndereco(SQLiteDatabase database, Usuario usuario) throws Exception {
        List<Endereco> listaTime = new ArrayList<>();

        String SQL = "SELECT * FROM ENDERECO" +
                " WHERE USUARIO_ID_FIREBASE = '" + usuario.getFirebase_id() + "'" +
                " ORDER BY NOMELOCAL ASC";
        Cursor cursor = database.rawQuery(SQL, null);
        cursor.moveToFirst(); //Posiciona o cursor no início da tabela
        while (!cursor.isAfterLast()) {
            listaTime.add(percorreLista(cursor));
            cursor.moveToNext();
        }
        return listaTime;
    }

    @SuppressLint("Range")
    private Endereco percorreLista(Cursor cursor) {
        Endereco endereco = new Endereco();
        endereco.setId(cursor.getInt(cursor.getColumnIndex("ID")));
        endereco.setFirebase_id(cursor.getString(cursor.getColumnIndex("FIREBASE_ID")));
        endereco.setUsuario_id_firebase(cursor.getString(cursor.getColumnIndex("USUARIO_ID_FIREBASE")));
        endereco.setNomelocal(cursor.getString(cursor.getColumnIndex("NOMELOCAL")));
        endereco.setEndereco(cursor.getString(cursor.getColumnIndex("ENDERECO")));
        return endereco;
    }


}
