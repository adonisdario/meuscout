package tcc.meuscout.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import tcc.meuscout.model.Endereco;
import tcc.meuscout.model.Partida;
import tcc.meuscout.model.Time;
import tcc.meuscout.model.Usuario;
import tcc.meuscout.util.Constantes;

public class PartidaBanco {

    private ContentValues valores(Partida partida) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("GOLSFEITOS", partida.getGolsFeitos());
        contentValues.put("FIREBASE_ID", partida.getFirebase_id());
        contentValues.put("GOLSPD", partida.getGolsPD());
        contentValues.put("GOLSPE", partida.getGolsPE());
        contentValues.put("GOLSCA", partida.getGolsCA());
        contentValues.put("GOLSOU", partida.getGolsOU());
        contentValues.put("PENALTIAC", partida.getPenaltiAc());
        contentValues.put("PENALTIER", partida.getPenaltiEr());
        contentValues.put("GOLSSOFRIDOS", partida.getGolsSofridos());
        contentValues.put("GOLSSOFRIDOSPEN", partida.getGolsSofridosPen());
        contentValues.put("ASSISTENCIAS", partida.getAssistencias());
        contentValues.put("DEFESAS", partida.getDefesas());
        contentValues.put("DEFESASPEN", partida.getDefesasPen());
        contentValues.put("DESARMES", partida.getDesarmes());
        contentValues.put("BLOQUEIOS", partida.getBloqueios());
        contentValues.put("CORTES", partida.getCortes());
        contentValues.put("ERROS", partida.getErros());
        contentValues.put("FINALTOTAL", partida.getFinalTotal());
        contentValues.put("FINALERRADAS", partida.getFinalErradas());
        contentValues.put("FINALCERTAS", partida.getFinalCertas());
        contentValues.put("FINALTRAVE", partida.getFinalTrave());
        contentValues.put("FINALBLOQ", partida.getFinalBloq());
        contentValues.put("CARTOESAMA", partida.getCartoesAma());
        contentValues.put("CARTOESVERM", partida.getCartoesVerm());
        contentValues.put("CARTOESAZUL", partida.getCartoesAzul());
        contentValues.put("FALTASREC", partida.getFaltasRec());
        contentValues.put("FALTASCOM", partida.getFaltasCom());
        contentValues.put("QTDPARTIDAS", partida.getQtdPartidas());
        contentValues.put("NOTAIND", partida.getNotaInd());
        contentValues.put("NOTAPART", partida.getNotaPart());
        contentValues.put("DURACAO", partida.getDuracao());
        contentValues.put("POSICAO_NOME", partida.getPosicao_nome());
        contentValues.put("POSICAO_SIGLA", partida.getPosicao_sigla());
        contentValues.put("POSICAO_NUM", partida.getPosicao_num());
        contentValues.put("POSICAOSEC_NOME", partida.getPosicaosec_nome());
        contentValues.put("POSICAOSEC_SIGLA", partida.getPosicaosec_sigla());
        contentValues.put("POSICAOSEC_NUM", partida.getPosicaosec_num());
        contentValues.put("PLACARCASA", partida.getPlacarCasa());
        contentValues.put("PLACARFORA", partida.getPlacarFora());
        contentValues.put("TIME_ID_FIREBASE", partida.getTime_id_firebase());
        contentValues.put("TIME_ADV_ID_FIREBASE", partida.getTime_adv_id_firebase());
        contentValues.put("USUARIO_ID_FIREBASE", partida.getUsuario_id_firebase());
        contentValues.put("NOMELOCAL", partida.getNomeLocal());
        contentValues.put("ENDERECO", partida.getEndereco());
        contentValues.put("TIPOREGISTRO", partida.getTipoRegistro());
        contentValues.put("DATA", partida.getData());
        contentValues.put("VITORIAS", partida.getVitorias());
        contentValues.put("EMPATES", partida.getEmpates());
        contentValues.put("DERROTAS", partida.getDerrotas());
        return contentValues;
    }

    public int inserir(Partida partida, SQLiteDatabase database) {
        return (int) database.insertOrThrow(
                "PARTIDA",
                null,
                valores(partida)
        );
    }

    public int atualizar(Partida partida, SQLiteDatabase database) throws Exception {
        String WHERE = "FIREBASE_ID = ?";
        String argumentos[] = {String.valueOf(partida.getFirebase_id())};

        return database.update(
                "PARTIDA",
                valores(partida),
                WHERE,
                argumentos);
    }

    public int remover(Partida partida, SQLiteDatabase database) throws Exception {
        return database.delete("PARTIDA", "USUARIO_ID_FIREBASE=? AND FIREBASE_ID=?", new String[]{
                partida.getUsuario_id_firebase(), partida.getFirebase_id()
        });
    }

    @SuppressLint("Range")
    public List<Partida> recuperaPartidasMes(SQLiteDatabase database, Usuario usuario, String mesAno) throws Exception {
        List<Partida> lista = new ArrayList<>();

        String SQL = "SELECT * FROM PARTIDA" +
                " WHERE USUARIO_ID_FIREBASE = '" + usuario.getFirebase_id() + "'" +
                " AND DATA BETWEEN ''" + mesAno + "/00'" + "AND '" + mesAno + "/31'";
        Cursor cursor = database.rawQuery(SQL, null);
        cursor.moveToFirst(); //Posiciona o cursor no início da tabela
        while (!cursor.isAfterLast()) {
            lista.add(percorreLista(cursor));
            cursor.moveToNext();
        }
        return lista;
    }

    @SuppressLint("Range")
    public List<Partida> recuperaPartidas(SQLiteDatabase database, Usuario usuario) throws Exception {
        List<Partida> lista = new ArrayList<>();

        String SQL = "SELECT * FROM PARTIDA" +
                " WHERE USUARIO_ID_FIREBASE = '" + usuario.getFirebase_id() + "'" +
                " ORDER BY DATA DESC"
                /*" ORDER BY DATE(substr(DATA,7,4) " +
                "||'-' " +
                "||substr(DATA,4,2) " +
                "||'/' " +
                "||substr(DATA,1,2)), ID DESC"*/;
        Cursor cursor = database.rawQuery(SQL, null);
        cursor.moveToFirst(); //Posiciona o cursor no início da tabela
        while (!cursor.isAfterLast()) {
            lista.add(percorreLista(cursor));
            cursor.moveToNext();
        }
        return lista;
    }

    @SuppressLint("Range")
    public Partida recuperaSomaNumeros(SQLiteDatabase database, Usuario usuario) throws Exception {
        Partida partida = new Partida();

        String SQL = "SELECT \n" +
                "    SUM(GOLSFEITOS) AS GOLSFEITOS, SUM(GOLSPD) AS GOLSPD, SUM(GOLSPE) AS GOLSPE, SUM(GOLSCA) AS GOLSCA, SUM(GOLSOU) AS GOLSOU,\n" +
                "    SUM(PENALTIAC) AS PENALTIAC, SUM(PENALTIER) AS PENALTIER, SUM(GOLSSOFRIDOS) AS GOLSSOFRIDOS, SUM(GOLSSOFRIDOSPEN) AS GOLSSOFRIDOSPEN,\n" +
                "    SUM(ASSISTENCIAS) AS ASSISTENCIAS, SUM(DEFESAS) AS DEFESAS, SUM(DEFESASPEN) AS DEFESASPEN, SUM(BLOQUEIOS) AS BLOQUEIOS, SUM(CORTES) AS CORTES,\n" +
                "    SUM(ERROS) AS ERROS, SUM(FINALTOTAL) AS FINALTOTAL, SUM(FINALERRADAS) AS FINALERRADAS, SUM(FINALCERTAS) AS FINALCERTAS, SUM(FINALTRAVE) AS FINALTRAVE,\n" +
                "    SUM(FINALBLOQ) AS FINALBLOQ, SUM(CARTOESAMA) AS CARTOESAMA, SUM(CARTOESVERM) AS CARTOESVERM, SUM(CARTOESAZUL) AS CARTOESAZUL, SUM(FALTASREC) AS FALTASREC,\n" +
                "    SUM(FALTASCOM) AS FALTASCOM, SUM(QTDPARTIDAS) AS QTDPARTIDAS, SUM(VITORIAS) AS VITORIAS, SUM(EMPATES) AS EMPATES, SUM(DERROTAS) AS DERROTAS,\n" +
                "    AVG(NOTAIND) AS NOTAIND, AVG(NOTAPART) AS NOTAPART, AVG(DURACAO) AS DURACAO \n" +
                "FROM PARTIDA WHERE USUARIO_ID_FIREBASE = '" + usuario.getFirebase_id() + "'";
        Cursor cursor = database.rawQuery(SQL, null);
        cursor.moveToFirst(); //Posiciona o cursor no início da tabela
        while (!cursor.isAfterLast()) {
            partida = (percorreLista(cursor));
        }
        return partida;
    }

    @SuppressLint("Range")
    public Partida recuperaPartidaMaisResultado(SQLiteDatabase database, Usuario usuario,
                                                String anoMesMin, String anoMesMax, String ordem) {
        Partida partida = new Partida();

        String SQL = "SELECT * FROM PARTIDA " +
                " WHERE DATA BETWEEN '" + anoMesMin + "' AND '" + anoMesMax + "' " +
                " AND USUARIO_ID_FIREBASE = '" + usuario.getFirebase_id() + "'" +
                ordem + " LIMIT 1;";
        Cursor cursor = database.rawQuery(SQL, null);
        cursor.moveToFirst(); //Posiciona o cursor no início da tabela
        if (!cursor.isAfterLast()) {
            partida = percorreLista(cursor);
        }
        return partida;
    }

    @SuppressLint("Range")
    public String recuperaPartidaMaisPosicao(SQLiteDatabase database, Usuario usuario,
                                             String anoMesMin, String anoMesMax) throws Exception {
        Partida partida = new Partida();
        String SQL = "SELECT POSICAO_NOME, POSICAO_SIGLA, COUNT(POSICAO_SIGLA) AS qtd FROM PARTIDA\n" +
                " WHERE USUARIO_ID_FIREBASE = '" + usuario.getFirebase_id() + "'" +
                " AND DATA BETWEEN '" + anoMesMin + "' AND '" + anoMesMax + "' " +
                " GROUP BY POSICAO_SIGLA ORDER BY qtd DESC LIMIT 1;";
        Cursor cursor = database.rawQuery(SQL, null);
        cursor.moveToFirst(); //Posiciona o cursor no início da tabela
        if (!cursor.isAfterLast()) {
            //partida = percorreLista(cursor);
            //break;
            partida.setPosicao_nome(cursor.getString(cursor.getColumnIndex("POSICAO_NOME")));
            partida.setPosicao_sigla(cursor.getString(cursor.getColumnIndex("POSICAO_SIGLA")));
        }
        return partida.getPosicao_sigla() + " - " + partida.getPosicao_nome();
    }

    @SuppressLint("Range")
    public String recuperaPartidaMaisPosicaoSec(SQLiteDatabase database, Usuario usuario,
                                                String anoMesMin, String anoMesMax) throws Exception {
        Partida partida = new Partida();
        String SQL = "SELECT POSICAOSEC_NOME, POSICAOSEC_SIGLA, COUNT(POSICAOSEC_SIGLA) AS qtd FROM PARTIDA\n" +
                " WHERE USUARIO_ID_FIREBASE = '" + usuario.getFirebase_id() + "'" +
                " AND DATA BETWEEN '" + anoMesMin + "' AND '" + anoMesMax + "' " +
                " GROUP BY POSICAOSEC_SIGLA ORDER BY qtd DESC LIMIT 1;";
        Cursor cursor = database.rawQuery(SQL, null);
        cursor.moveToFirst(); //Posiciona o cursor no início da tabela
        if (!cursor.isAfterLast()) {
            //partida = percorreLista(cursor);
            //break;
            partida.setPosicaosec_nome(cursor.getString(cursor.getColumnIndex("POSICAOSEC_NOME")));
            partida.setPosicaosec_sigla(cursor.getString(cursor.getColumnIndex("POSICAOSEC_SIGLA")));
        }
        if (partida.getPosicaosec_nome() != null)
            return partida.getPosicaosec_sigla() + " - " + partida.getPosicaosec_nome();
        return "-";
    }

    @SuppressLint("Range")
    public String recuperaPartidaMaisLugar(SQLiteDatabase database, Usuario usuario,
                                           String anoMesMin, String anoMesMax) throws Exception {
        Partida partida = new Partida();
        String SQL = "SELECT NOMELOCAL, COUNT(NOMELOCAL) AS qtd FROM PARTIDA\n" +
                " WHERE USUARIO_ID_FIREBASE = '" + usuario.getFirebase_id() + "'" +
                " AND DATA BETWEEN '" + anoMesMin + "' AND '" + anoMesMax + "' " +
                " GROUP BY NOMELOCAL ORDER BY qtd DESC LIMIT 1;";
        Cursor cursor = database.rawQuery(SQL, null);
        cursor.moveToFirst(); //Posiciona o cursor no início da tabela
        if (!cursor.isAfterLast()) {
            //partida = percorreLista(cursor);
            //break;
            partida.setNomeLocal(cursor.getString(cursor.getColumnIndex("NOMELOCAL")));
        }
        if (partida.getNomeLocal() != null)
            return partida.getNomeLocal();
        return "-";
    }

    @SuppressLint("Range")
    public Partida recuperaPartidaSomatorio(SQLiteDatabase database, Usuario usuario) throws Exception {
        Partida partida = new Partida();
        String SQL = "SELECT SUM(GOLSFEITOS) AS GOLSFEITOS, SUM(ASSISTENCIAS) AS ASSISTENCIAS, SUM(PENALTIAC) AS PENALTIAC, " +
                " SUM(PENALTIER) AS PENALTIER" +
                " FROM PARTIDA " +
                " WHERE USUARIO_ID_FIREBASE = '" + usuario.getFirebase_id() + "'" +
                " AND TIPOREGISTRO != 'TREINO'";
        Cursor cursor = database.rawQuery(SQL, null);
        cursor.moveToFirst(); //Posiciona o cursor no início da tabela
        if (!cursor.isAfterLast()) {
            partida = percorreListaSoma(cursor);
            //break;;
        }
        return partida;
    }


    @SuppressLint("Range")
    public String recuperaPartidaMaisAtividade(SQLiteDatabase database, Usuario usuario,
                                               String anoMesMin, String anoMesMax) throws Exception {
        Partida partida = new Partida();
        String SQL = "SELECT TIPOREGISTRO, COUNT(TIPOREGISTRO) AS qtd FROM PARTIDA\n" +
                " WHERE USUARIO_ID_FIREBASE = '" + usuario.getFirebase_id() + "'" +
                " AND DATA BETWEEN '" + anoMesMin + "' AND '" + anoMesMax + "' " +
                " GROUP BY TIPOREGISTRO ORDER BY qtd DESC LIMIT 1;";
        Cursor cursor = database.rawQuery(SQL, null);
        cursor.moveToFirst(); //Posiciona o cursor no início da tabela
        if (!cursor.isAfterLast()) {
            //partida = percorreLista(cursor);
            //break;
            partida.setTipoRegistro(cursor.getString(cursor.getColumnIndex("TIPOREGISTRO")));
        }
        if (partida.getTipoRegistro() != null)
            return partida.getTipoRegistro();
        return "-";
    }

    public boolean existePartidaTime(SQLiteDatabase database, Usuario usuario, Time time) throws Exception {
        List<Time> lista = new ArrayList<>();

        String SQL = "SELECT * FROM PARTIDA" +
                " WHERE USUARIO_ID_FIREBASE = '" + usuario.getFirebase_id() + "'" +
                " AND TIME_ID_FIREBASE = '" + time.getFirebase_id() + "'" +
                " OR TIME_ADV_ID_FIREBASE = '" + time.getFirebase_id() + "'";

        Cursor cursor = database.rawQuery(SQL, null);
        cursor.moveToFirst(); //Posiciona o cursor no início da tabela
        while (!cursor.isAfterLast()) {
            lista.add(time);
            cursor.moveToNext();
        }
        return lista.size() != 0;
    }

    public boolean existePartidaEndereco(SQLiteDatabase database, Usuario usuario, Endereco endereco) throws Exception {
        List<Endereco> lista = new ArrayList<>();

        String SQL = "SELECT * FROM PARTIDA" +
                " WHERE USUARIO_ID_FIREBASE = '" + usuario.getFirebase_id() + "'" +
                " AND NOMELOCAL = '" + endereco.getNomelocal() + "'";

        Cursor cursor = database.rawQuery(SQL, null);
        cursor.moveToFirst(); //Posiciona o cursor no início da tabela
        while (!cursor.isAfterLast()) {
            lista.add(endereco);
            cursor.moveToNext();
        }
        return lista.size() != 0;
    }

    @SuppressLint("Range")
    private Partida percorreLista(Cursor cursor) {
        Partida partida = new Partida();
        partida.setId(cursor.getInt(cursor.getColumnIndex("ID")));
        partida.setFirebase_id(cursor.getString(cursor.getColumnIndex("FIREBASE_ID")));
        partida.setUsuario_id_firebase(cursor.getString(cursor.getColumnIndex("USUARIO_ID_FIREBASE")));
        partida.setGolsFeitos(cursor.getInt(cursor.getColumnIndex("GOLSFEITOS")));
        partida.setGolsPD(cursor.getInt(cursor.getColumnIndex("GOLSPD")));
        partida.setGolsPE(cursor.getInt(cursor.getColumnIndex("GOLSPE")));
        partida.setGolsCA(cursor.getInt(cursor.getColumnIndex("GOLSCA")));
        partida.setGolsOU(cursor.getInt(cursor.getColumnIndex("GOLSOU")));
        partida.setPenaltiAc(cursor.getInt(cursor.getColumnIndex("PENALTIAC")));
        partida.setPenaltiEr(cursor.getInt(cursor.getColumnIndex("PENALTIER")));
        partida.setGolsSofridos(cursor.getInt(cursor.getColumnIndex("GOLSSOFRIDOS")));
        partida.setGolsSofridosPen(cursor.getInt(cursor.getColumnIndex("GOLSSOFRIDOSPEN")));
        partida.setAssistencias(cursor.getInt(cursor.getColumnIndex("ASSISTENCIAS")));
        partida.setDefesas(cursor.getInt(cursor.getColumnIndex("DEFESAS")));
        partida.setDefesasPen(cursor.getInt(cursor.getColumnIndex("DEFESASPEN")));
        partida.setDesarmes(cursor.getInt(cursor.getColumnIndex("DESARMES")));
        partida.setBloqueios(cursor.getInt(cursor.getColumnIndex("BLOQUEIOS")));
        partida.setCortes(cursor.getInt(cursor.getColumnIndex("CORTES")));
        partida.setErros(cursor.getInt(cursor.getColumnIndex("ERROS")));
        partida.setFinalTotal(cursor.getInt(cursor.getColumnIndex("FINALTOTAL")));
        partida.setFinalErradas(cursor.getInt(cursor.getColumnIndex("FINALERRADAS")));
        partida.setFinalCertas(cursor.getInt(cursor.getColumnIndex("FINALCERTAS")));
        partida.setFinalTrave(cursor.getInt(cursor.getColumnIndex("FINALTRAVE")));
        partida.setFinalBloq(cursor.getInt(cursor.getColumnIndex("FINALBLOQ")));
        partida.setCartoesAma(cursor.getInt(cursor.getColumnIndex("CARTOESAMA")));
        partida.setCartoesVerm(cursor.getInt(cursor.getColumnIndex("CARTOESVERM")));
        partida.setCartoesAzul(cursor.getInt(cursor.getColumnIndex("CARTOESAZUL")));
        partida.setFaltasRec(cursor.getInt(cursor.getColumnIndex("FALTASREC")));
        partida.setFaltasCom(cursor.getInt(cursor.getColumnIndex("FALTASCOM")));
        partida.setQtdPartidas(cursor.getInt(cursor.getColumnIndex("QTDPARTIDAS")));
        partida.setNotaInd(cursor.getInt(cursor.getColumnIndex("NOTAIND")));
        partida.setNotaPart(cursor.getInt(cursor.getColumnIndex("NOTAPART")));
        partida.setDuracao(cursor.getInt(cursor.getColumnIndex("DURACAO")));
        partida.setPosicao_nome(cursor.getString(cursor.getColumnIndex("POSICAO_NOME")));
        partida.setPosicao_sigla(cursor.getString(cursor.getColumnIndex("POSICAO_SIGLA")));
        partida.setPosicao_num(cursor.getInt(cursor.getColumnIndex("POSICAO_NUM")));
        partida.setPosicaosec_nome(cursor.getString(cursor.getColumnIndex("POSICAOSEC_NOME")));
        partida.setPosicaosec_sigla(cursor.getString(cursor.getColumnIndex("POSICAOSEC_SIGLA")));
        partida.setPosicaosec_num(cursor.getInt(cursor.getColumnIndex("POSICAOSEC_NUM")));
        partida.setPlacarCasa(cursor.getInt(cursor.getColumnIndex("PLACARCASA")));
        partida.setPlacarFora(cursor.getInt(cursor.getColumnIndex("PLACARFORA")));
        partida.setTime_id_firebase(cursor.getString(cursor.getColumnIndex("TIME_ID_FIREBASE")));
        partida.setTime_adv_id_firebase(cursor.getString(cursor.getColumnIndex("TIME_ADV_ID_FIREBASE")));
        partida.setUsuario_id_firebase(cursor.getString(cursor.getColumnIndex("USUARIO_ID_FIREBASE")));
        partida.setNomeLocal(cursor.getString(cursor.getColumnIndex("NOMELOCAL")));
        partida.setEndereco(cursor.getString(cursor.getColumnIndex("ENDERECO")));
        partida.setTipoRegistro(cursor.getString(cursor.getColumnIndex("TIPOREGISTRO")));
        partida.setData(cursor.getString(cursor.getColumnIndex("DATA")));
        partida.setVitorias(cursor.getInt(cursor.getColumnIndex("VITORIAS")));
        partida.setEmpates(cursor.getInt(cursor.getColumnIndex("EMPATES")));
        partida.setDerrotas(cursor.getInt(cursor.getColumnIndex("DERROTAS")));
        return partida;
    }

    @SuppressLint("Range")
    private Partida percorreListaSoma(Cursor cursor) {
        Partida partida = new Partida();
        partida.setGolsFeitos(cursor.getInt(cursor.getColumnIndex("GOLSFEITOS")));
        /*partida.setGolsPD(cursor.getInt(cursor.getColumnIndex("GOLSPD")));
        partida.setGolsPE(cursor.getInt(cursor.getColumnIndex("GOLSPE")));
        partida.setGolsCA(cursor.getInt(cursor.getColumnIndex("GOLSCA")));
        partida.setGolsOU(cursor.getInt(cursor.getColumnIndex("GOLSOU")));*/
        partida.setPenaltiAc(cursor.getInt(cursor.getColumnIndex("PENALTIAC")));
        partida.setPenaltiEr(cursor.getInt(cursor.getColumnIndex("PENALTIER")));
        /*partida.setGolsSofridos(cursor.getInt(cursor.getColumnIndex("GOLSSOFRIDOS")));
        partida.setGolsSofridosPen(cursor.getInt(cursor.getColumnIndex("GOLSSOFRIDOSPEN")));*/
        partida.setAssistencias(cursor.getInt(cursor.getColumnIndex("ASSISTENCIAS")));
        /*partida.setDefesas(cursor.getInt(cursor.getColumnIndex("DEFESAS")));
        partida.setDefesasPen(cursor.getInt(cursor.getColumnIndex("DEFESASPEN")));
        partida.setDesarmes(cursor.getInt(cursor.getColumnIndex("DESARMES")));
        partida.setBloqueios(cursor.getInt(cursor.getColumnIndex("BLOQUEIOS")));
        partida.setCortes(cursor.getInt(cursor.getColumnIndex("CORTES")));
        partida.setErros(cursor.getInt(cursor.getColumnIndex("ERROS")));
        partida.setFinalTotal(cursor.getInt(cursor.getColumnIndex("FINALTOTAL")));
        partida.setFinalErradas(cursor.getInt(cursor.getColumnIndex("FINALERRADAS")));
        partida.setFinalCertas(cursor.getInt(cursor.getColumnIndex("FINALCERTAS")));
        partida.setFinalTrave(cursor.getInt(cursor.getColumnIndex("FINALTRAVE")));
        partida.setFinalBloq(cursor.getInt(cursor.getColumnIndex("FINALBLOQ")));
        partida.setCartoesAma(cursor.getInt(cursor.getColumnIndex("CARTOESAMA")));
        partida.setCartoesVerm(cursor.getInt(cursor.getColumnIndex("CARTOESVERM")));
        partida.setCartoesAzul(cursor.getInt(cursor.getColumnIndex("CARTOESAZUL")));
        partida.setFaltasRec(cursor.getInt(cursor.getColumnIndex("FALTASREC")));
        partida.setFaltasCom(cursor.getInt(cursor.getColumnIndex("FALTASCOM")));
        partida.setQtdPartidas(cursor.getInt(cursor.getColumnIndex("QTDPARTIDAS")));
        partida.setNotaInd(cursor.getInt(cursor.getColumnIndex("NOTAIND")));
        partida.setNotaPart(cursor.getInt(cursor.getColumnIndex("NOTAPART")));
        partida.setDuracao(cursor.getInt(cursor.getColumnIndex("DURACAO")));
        partida.setVitorias(cursor.getInt(cursor.getColumnIndex("VITORIAS")));
        partida.setEmpates(cursor.getInt(cursor.getColumnIndex("EMPATES")));
        partida.setDerrotas(cursor.getInt(cursor.getColumnIndex("DERROTAS")));*/
        return partida;
    }

}
