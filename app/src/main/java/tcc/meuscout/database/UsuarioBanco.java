package tcc.meuscout.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import tcc.meuscout.model.Usuario;

public class UsuarioBanco {


    private ContentValues valores(Usuario usuario) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("FIREBASE_ID", usuario.getFirebase_id());
        contentValues.put("NOME", usuario.getNome());
        contentValues.put("EMAIL", usuario.getEmail());
        contentValues.put("CIDADE", usuario.getCidade());
        contentValues.put("PERNA", usuario.getPerna());
        contentValues.put("POSICAO_NOME", usuario.getPosicao_nome());
        contentValues.put("POSICAO_SIGLA", usuario.getPosicao_sigla());
        contentValues.put("POSICAO_NUM", usuario.getPosicao_num());
        contentValues.put("TIME_ID_FIREBASE", usuario.getTime_id_firebase());
        contentValues.put("TIME_NOME", usuario.getTime_nome());
        contentValues.put("TIME_SIGLA", usuario.getTime_sigla());
        contentValues.put("LOGADO", usuario.getLogado());
        contentValues.put("DATA_NASCIMENTO", usuario.getData_nascimento());
        return contentValues;
    }

    public int inserir(Usuario usuario, SQLiteDatabase database) {
        return (int) database.insertOrThrow(
                "USUARIO",
                null,
                valores(usuario)
        );
    }

    public int atualizar(Usuario usuario, SQLiteDatabase database) throws Exception {
        String WHERE = "FIREBASE_ID = ?";
        String argumentos[] = {String.valueOf(usuario.getFirebase_id())};

        return database.update(
                "USUARIO",
                valores(usuario),
                WHERE,
                argumentos);
    }

    @SuppressLint("Range")
    public Integer recuperaIdUsuario(SQLiteDatabase database) throws Exception {
        Integer id = null;
        String SQL = "SELECT * FROM USUARIO WHERE LOGADO = 'S'";
        Cursor cursor = database.rawQuery(SQL, null);
        cursor.moveToFirst(); //Posiciona o cursor no início da tabela
        while (!cursor.isAfterLast()) {
            id = cursor.getInt(cursor.getColumnIndex("ID"));
            cursor.moveToNext();
        }
        return id;
    }

    @SuppressLint("Range")
    public Usuario recuperaUsuarioLogado(SQLiteDatabase database) throws Exception {
        Usuario usuario = new Usuario();
        String SQL = "SELECT * FROM USUARIO WHERE LOGADO = 'S'";
        Cursor cursor = database.rawQuery(SQL, null);
        cursor.moveToFirst(); //Posiciona o cursor no início da tabela
        while (!cursor.isAfterLast()) {
            usuario.setId(cursor.getInt(cursor.getColumnIndex("ID")));
            usuario.setFirebase_id(cursor.getString(cursor.getColumnIndex("FIREBASE_ID")));
            usuario.setNome(cursor.getString(cursor.getColumnIndex("NOME")));
            usuario.setEmail(cursor.getString(cursor.getColumnIndex("EMAIL")));
            usuario.setCidade(cursor.getString(cursor.getColumnIndex("CIDADE")));
            usuario.setPerna(cursor.getString(cursor.getColumnIndex("PERNA")));
            usuario.setPosicao_nome(cursor.getString(cursor.getColumnIndex("POSICAO_NOME")));
            usuario.setPosicao_sigla(cursor.getString(cursor.getColumnIndex("POSICAO_SIGLA")));
            usuario.setPosicao_num(cursor.getInt(cursor.getColumnIndex("POSICAO_NUM")));
            usuario.setTime_id_firebase(cursor.getString(cursor.getColumnIndex("TIME_ID_FIREBASE")));
            usuario.setTime_nome(cursor.getString(cursor.getColumnIndex("TIME_NOME")));
            usuario.setTime_sigla(cursor.getString(cursor.getColumnIndex("TIME_SIGLA")));
            usuario.setLogado(cursor.getString(cursor.getColumnIndex("LOGADO")));
            usuario.setData_nascimento(cursor.getString(cursor.getColumnIndex("DATA_NASCIMENTO")));
            cursor.moveToNext();
        }
        return usuario;
    }


}
