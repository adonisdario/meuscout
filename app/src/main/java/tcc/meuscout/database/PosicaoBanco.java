package tcc.meuscout.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import tcc.meuscout.model.Partida;
import tcc.meuscout.model.Posicao;
import tcc.meuscout.model.Time;
import tcc.meuscout.model.Usuario;

public class PosicaoBanco {


    /**
     * Funções para inserir objetos no banco de dados
     *
     * @param posicao O objeto a ser inserido
     * @return Retorna o novo Id gerado
     * @throws Exception
     */

    public void inserirPosicao(Posicao posicao, SQLiteDatabase database) throws Exception {
        database.execSQL("INSERT INTO POSICAO (" +
                "USUARIO_ID, NOME, SIGLA, NUM) " +
                "VALUES (" +
                posicao.getUsuario_id() + ", " +
                "'" + posicao.getNome() + "', " +
                "'" + posicao.getSigla() + "', " +
                posicao.getNum() +
                ");"
        );
    }

    @SuppressLint("Range")
    public List<Posicao> recuperaPosicoes(SQLiteDatabase database, Usuario usuario) throws Exception {
        List<Posicao> listaPosicao = new ArrayList<>();

        String SQL = "SELECT * FROM POSICAO" +
                " WHERE USUARIO_ID = " + usuario.getId();
        Cursor cursor = database.rawQuery(SQL, null);
        cursor.moveToFirst(); //Posiciona o cursor no início da tabela
        while (!cursor.isAfterLast()) {
            Posicao posicao = new Posicao();
            posicao.setId(cursor.getInt(cursor.getColumnIndex("ID")));
            posicao.setUsuario_id(cursor.getInt(cursor.getColumnIndex("USUARIO_ID")));
            posicao.setNome(cursor.getString(cursor.getColumnIndex("NOME")));
            posicao.setSigla(cursor.getString(cursor.getColumnIndex("SIGLA")));
            posicao.setNum(cursor.getInt(cursor.getColumnIndex("NUM")));
            listaPosicao.add(posicao);
            cursor.moveToNext();
        }
        return listaPosicao;
    }

    public int atualizarPosicao(Posicao usuario, SQLiteDatabase database) throws Exception {
        ContentValues contentValues = new ContentValues();
        contentValues.put("USUARIO_ID", usuario.getUsuario_id());
        contentValues.put("NOME", usuario.getNome());
        contentValues.put("SIGLA", usuario.getSigla());
        contentValues.put("NUM", usuario.getNum());

        String WHERE = "ID = ?";
        String argumentos[] = {String.valueOf(usuario.getId())};

        return database.update("POSICAO", contentValues, WHERE, argumentos);
    }

}
