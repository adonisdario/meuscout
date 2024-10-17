package tcc.meuscout.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import tcc.meuscout.model.Partida;
import tcc.meuscout.model.Ranking;
import tcc.meuscout.model.Time;
import tcc.meuscout.model.Usuario;

public class RankingBanco {

    private final String tabela = "RANKING";

    private ContentValues valores(Ranking ranking) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("POS_SIGLA", ranking.getPos_sigla());
        contentValues.put("RANK", ranking.getRank());
        contentValues.put("GOLS90", ranking.getGols90());
        contentValues.put("ASS90", ranking.getAss90());
        contentValues.put("CH90", ranking.getCh90());
        contentValues.put("CHCER90", ranking.getChcer90());
        contentValues.put("CHCERPCT", ranking.getChcerpct());
        contentValues.put("CHGOL", ranking.getChgol());
        contentValues.put("CHCHERGOL", ranking.getChcergol());
        contentValues.put("PENPCT", ranking.getPenpct());
        contentValues.put("DES90", ranking.getDes90());
        contentValues.put("COR90", ranking.getCor90());
        contentValues.put("BLOQ90", ranking.getBloq90());
        contentValues.put("FALSOF", ranking.getFalsof());
        contentValues.put("FALCOM", ranking.getFalcom());
        contentValues.put("JGSCA", ranking.getJgsca());
        contentValues.put("JGSCV", ranking.getJgscv());
        contentValues.put("DEFPCT", ranking.getDefpct());
        contentValues.put("CHCER90GOL", ranking.getChcer90gol());
        contentValues.put("GOLSOF90", ranking.getGolsof90());
        contentValues.put("CS", ranking.getCs());
        contentValues.put("DEF90", ranking.getDef90());
        contentValues.put("PENDEFPCT", ranking.getPendefpct());
        return contentValues;
    }

    public int inserir(Ranking ranking, SQLiteDatabase database) {
        return (int) database.insertOrThrow(
                tabela,
                null,
                valores(ranking)
        );
    }

    public int atualizar(Ranking ranking, SQLiteDatabase database) throws Exception {
        String WHERE = "FIREBASE_ID = ?";
        String argumentos[] = {String.valueOf(ranking.getId())};

        return database.update(
                tabela,
                valores(ranking),
                WHERE,
                argumentos);
    }

    public int remover(Ranking ranking, SQLiteDatabase database) throws Exception {
        return database.delete(tabela, "ID=?", new String[]{
                String.valueOf(ranking.getId())});
    }

    @SuppressLint("Range")
    public boolean existeRanking(SQLiteDatabase database) throws Exception {
        List<Ranking> lista = new ArrayList<>();

        String SQL = "SELECT * FROM RANKING LIMIT 1;";
        Cursor cursor = database.rawQuery(SQL, null);
        cursor.moveToFirst(); //Posiciona o cursor no início da tabela
        while (!cursor.isAfterLast()) {
            lista.add(percorreLista(cursor));
            cursor.moveToNext();
        }
        return lista.size() > 0;
    }

    @SuppressLint("Range")
    public List<Ranking> recuperaRankings(SQLiteDatabase database, String posicao_sigla) throws Exception {
        List<Ranking> lista = new ArrayList<>();

        String SQL = "SELECT * FROM RANKING ";
        String Where = "";
        if (posicao_sigla != null)
            Where = " WHERE POS_SIGLA = '" + posicao_sigla + "'";
        Cursor cursor = database.rawQuery(SQL + Where, null);
        cursor.moveToFirst(); //Posiciona o cursor no início da tabela
        while (!cursor.isAfterLast()) {
            lista.add(percorreLista(cursor));
            cursor.moveToNext();
        }
        return lista;
    }

    @SuppressLint("Range")
    private Ranking percorreLista(Cursor cursor) {
        Ranking ranking = new Ranking();
        ranking.setId(cursor.getInt(cursor.getColumnIndex("ID")));
        ranking.setPos_sigla(cursor.getString(cursor.getColumnIndex("POS_SIGLA")));
        ranking.setRank(cursor.getString(cursor.getColumnIndex("RANK")));
        ranking.setGols90(cursor.getDouble(cursor.getColumnIndex("GOLS90")));
        ranking.setAss90(cursor.getDouble(cursor.getColumnIndex("ASS90")));
        ranking.setCh90(cursor.getDouble(cursor.getColumnIndex("CH90")));
        ranking.setChcer90(cursor.getDouble(cursor.getColumnIndex("CHCER90")));
        ranking.setChcerpct(cursor.getDouble(cursor.getColumnIndex("CHCERPCT")));
        ranking.setChgol(cursor.getDouble(cursor.getColumnIndex("CHGOL")));
        ranking.setChcergol(cursor.getDouble(cursor.getColumnIndex("CHCHERGOL")));
        ranking.setPenpct(cursor.getDouble(cursor.getColumnIndex("PENPCT")));
        ranking.setDes90(cursor.getDouble(cursor.getColumnIndex("DES90")));
        ranking.setCor90(cursor.getDouble(cursor.getColumnIndex("COR90")));
        ranking.setBloq90(cursor.getDouble(cursor.getColumnIndex("BLOQ90")));
        ranking.setFalsof(cursor.getDouble(cursor.getColumnIndex("FALSOF")));
        ranking.setFalcom(cursor.getDouble(cursor.getColumnIndex("FALCOM")));
        ranking.setJgsca(cursor.getDouble(cursor.getColumnIndex("JGSCA")));
        ranking.setJgscv(cursor.getDouble(cursor.getColumnIndex("JGSCV")));
        ranking.setDefpct(cursor.getDouble(cursor.getColumnIndex("DEFPCT")));
        ranking.setChcer90gol(cursor.getDouble(cursor.getColumnIndex("CHCER90GOL")));
        ranking.setGolsof90(cursor.getDouble(cursor.getColumnIndex("GOLSOF90")));
        ranking.setCs(cursor.getDouble(cursor.getColumnIndex("CS")));
        ranking.setDef90(cursor.getDouble(cursor.getColumnIndex("DEF90")));
        ranking.setPendefpct(cursor.getDouble(cursor.getColumnIndex("PENDEFPCT")));
        return ranking;
    }

}
