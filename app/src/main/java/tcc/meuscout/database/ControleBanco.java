package tcc.meuscout.database;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;

import java.util.List;

import tcc.meuscout.model.Endereco;
import tcc.meuscout.model.Partida;
import tcc.meuscout.model.Posicao;
import tcc.meuscout.model.Ranking;
import tcc.meuscout.model.Time;
import tcc.meuscout.model.Usuario;
import tcc.meuscout.util.Alerta;
import tcc.meuscout.util.Constantes;

public class ControleBanco {

    private static ControleBanco instance;
    private String TAG = "Controle_Banco";
    private SQLiteDatabase database;

    public static ControleBanco getInstance() {
        if (instance == null) {
            instance = new ControleBanco();
        }
        return instance;
    }


    public void iniciarBD(Context contexto, Activity activity) {
        try {
            /*
            //contexto.deleteDatabase("FROTA.db");//Usa-se para atualizar o banco, e depois de usado uma vez, comenta-se
            RepositorioBanco.setContext(contexto);
            RepositorioBanco.setDATABASE_VERSION(contexto.getPackageManager()
                    .getPackageInfo(contexto.getPackageName(), 0).versionCode);
            RepositorioBanco.setCaminhoDb(Constantes.CAMINHO_BD);
            RepositorioBanco.setDbName(Constantes.DATABASE_NAME);
            //BaseRepositorio.setDbName("teste.db"); //"paliativo"
            RepositorioBanco.setPastaSistema(Constantes.NOME_PASTA_SISTEMA);
            //Aqui eh onde tem o Script
            RepositorioBanco.setScriptCreate(ScriptBanco.SCRIPT_CREATE);
             */
            database = contexto.openOrCreateDatabase(Constantes.DATABASE_NAME, Context.MODE_PRIVATE, null);
            for (int i = 0; i < ScriptBanco.SCRIPT_CREATE.length; i++)
                database.execSQL(ScriptBanco.SCRIPT_CREATE[i]);


            if (!existeRanking(activity))
                for (int i = 0; i < ScriptBanco.SCRIPT_RANKING.length; i++)
                    database.execSQL(ScriptBanco.SCRIPT_RANKING[i]);


        } catch (Exception e) {
            gravarExcecao(e, activity, Constantes.MSG_GENERICA_ERRO);
        }
    }

    /** MÉTODOS PARA INSERIR OBJETOS NO BANCO **/

    public int inserirUsuario(Activity activity, Usuario usuario) throws Exception {
        try {
            return new UsuarioBanco().inserir(usuario, database);
        } catch (Exception e) {
            gravarExcecao(e, activity, "Erro ao inserir Usuário.");
        }
        return -1;
    }

    public int inserirTime(Activity activity, Time time) throws Exception {
        try {
            return new TimeBanco().inserir(time, database);
        } catch (Exception e) {
            gravarExcecao(e, activity, "Erro ao inserir Time.");
        }
        return -1;
    }

    public int inserirEndereco(Activity activity, Endereco endereco) throws Exception {
        try {
            return new EnderecoBanco().inserir(endereco, database);
        } catch (Exception e) {
            gravarExcecao(e, activity, "Erro ao inserir Endereco.");
        }
        return -1;
    }

    public void inserirPosicao(Activity activity, Posicao posicao) throws Exception {
        try {
            new PosicaoBanco().inserirPosicao(posicao, database);
        } catch (Exception e) {
            gravarExcecao(e, activity, "Erro ao inserir Posição.");
        }
    }

    public int inserirPartida(Activity activity, Partida partida) throws Exception {
        try {
            return new PartidaBanco().inserir(partida, database);
        } catch (Exception e) {
            gravarExcecao(e, activity, "Erro ao inserir Partida.");
        }
        return -1;
    }

    /** MÉTODOS PARA RECUPERAR VALORES NO BANCO **/

    public Integer recuperaIdUsuario(Activity activity) throws Exception {
        try {
            return new UsuarioBanco().recuperaIdUsuario(database);
        } catch (Exception e) {
            gravarExcecao(e, activity, "Erro ao recuperar o ID do Usuário.");
        }
        return null;
    }

    public String recuperaPartidaMaisPosicao(Activity activity, Usuario usuario, String anoMesMin, String anoMesMax) throws Exception {
        try {
            return new PartidaBanco().recuperaPartidaMaisPosicao(database, usuario, anoMesMin, anoMesMax);
        } catch (Exception e) {
            gravarExcecao(e, activity, "Erro ao recuperar Partidas.");
        }
        return null;
    }
    public String recuperaPartidaMaisPosicaoSec(Activity activity, Usuario usuario, String anoMesMin, String anoMesMax) throws Exception {
        try {
            return new PartidaBanco().recuperaPartidaMaisPosicaoSec(database, usuario, anoMesMin, anoMesMax);
        } catch (Exception e) {
            gravarExcecao(e, activity, "Erro ao recuperar Partidas.");
        }
        return null;
    }
    public String recuperaPartidaMaisLugar(Activity activity, Usuario usuario, String anoMesMin, String anoMesMax) throws Exception {
        try {
            return new PartidaBanco().recuperaPartidaMaisLugar(database, usuario, anoMesMin, anoMesMax);
        } catch (Exception e) {
            gravarExcecao(e, activity, "Erro ao recuperar Partidas.");
        }
        return null;
    }
    public String recuperaPartidaMaisAtividade(Activity activity, Usuario usuario, String anoMesMin, String anoMesMax) throws Exception {
        try {
            return new PartidaBanco().recuperaPartidaMaisAtividade(database, usuario, anoMesMin, anoMesMax);
        } catch (Exception e) {
            gravarExcecao(e, activity, "Erro ao recuperar Partidas.");
        }
        return null;
    }
    public Partida recuperaPartidaSomatorio(Activity activity, Usuario usuario) throws Exception {
        try {
            return new PartidaBanco().recuperaPartidaSomatorio(database, usuario);
        } catch (Exception e) {
            gravarExcecao(e, activity, "Erro ao recuperar Partidas.");
        }
        return null;
    }

    /** MÉTODOS PARA RECUPERAR OBJETOS NO BANCO **/

    public Usuario recuperaUsuarioLogado(Activity activity) throws Exception {
        try {
            return new UsuarioBanco().recuperaUsuarioLogado(database);
        } catch (Exception e) {
            gravarExcecao(e, activity, "Erro ao recuperar o Usuário.");
        }
        return null;
    }

    public Time recuperaTimePrincipalUsuario(Activity activity, Usuario usuario) throws Exception {
        try {
            return new TimeBanco().recuperaTimePrincipalUsuario(database, usuario);
        } catch (Exception e) {
            gravarExcecao(e, activity, "Erro ao recuperar o Time.");
        }
        return null;
    }

    public Time recuperaTimePorId(Activity activity, Usuario usuario, String id) throws Exception {
        try {
            return new TimeBanco().recuperaTimePorId(database, usuario, id);
        } catch (Exception e) {
            gravarExcecao(e, activity, "Erro ao recuperar o Time.");
        }
        return null;
    }

    public Endereco recuperaEnderecoPorId(Activity activity, Usuario usuario, String id) throws Exception {
        try {
            return new EnderecoBanco().recuperaEnderecoPorId(database, usuario, id);
        } catch (Exception e) {
            gravarExcecao(e, activity, "Erro ao recuperar o Endereço.");
        }
        return null;
    }

    public boolean existeRanking(Activity activity) throws Exception {
        try {
            return new RankingBanco().existeRanking(database);
        } catch (Exception e) {
            gravarExcecao(e, activity, "Erro ao recuperar os Rankings.");
        }
        return false;
    }

    public List<Ranking> reuperaRankings(Activity activity, String posicao_sigla) throws Exception {
        try {
            return new RankingBanco().recuperaRankings(database, posicao_sigla);
        } catch (Exception e) {
            gravarExcecao(e, activity, "Erro ao recuperar os Rankings.");
        }
        return null;
    }

    public List<Endereco> recuperaListaEndereco(Activity activity, Usuario usuario) throws Exception {
        try {
            return new EnderecoBanco().recuperaListaEndereco(database, usuario);
        } catch (Exception e) {
            gravarExcecao(e, activity, "Erro ao recuperar os Endereços.");
        }
        return null;
    }

    public List<Time> recuperaListaTimes(Activity activity, Usuario usuario) throws Exception {
        try {
            return new TimeBanco().recuperaListaTimes(database, usuario);
        } catch (Exception e) {
            gravarExcecao(e, activity, "Erro ao recuperar os Times.");
        }
        return null;
    }

    public List<Time> recuperaListaTimesUsuario(Activity activity, Usuario usuario) throws Exception {
        try {
            return new TimeBanco().recuperaListaTimesUsuario(database, usuario);
        } catch (Exception e) {
            gravarExcecao(e, activity, "Erro ao recuperar os Times.");
        }
        return null;
    }

    public List<Time> recuperaTimesAdversarios(Activity activity, Usuario usuario) throws Exception {
        try {
            return new TimeBanco().recuperaListaTimesAdversarios(database, usuario);
        } catch (Exception e) {
            gravarExcecao(e, activity, "Erro ao recuperar os Times adversários.");
        }
        return null;
    }

    public List<Posicao> recuperaPosicoes(Activity activity, Usuario usuario) throws Exception {
        try {
            return new PosicaoBanco().recuperaPosicoes(database, usuario);
        } catch (Exception e) {
            gravarExcecao(e, activity, "Erro ao recuperar as Posições do jogador.");
        }
        return null;
    }

    public List<Partida> recuperaPartidas(Activity activity, Usuario usuario) throws Exception {
        try {
            return new PartidaBanco().recuperaPartidas(database, usuario);
        } catch (Exception e) {
            gravarExcecao(e, activity, "Erro ao recuperar as Partidas.");
        }
        return null;
    }

    public List<Partida> recuperaPartidasMes(Activity activity, Usuario usuario, String anoMes) throws Exception {
        try {
            return new PartidaBanco().recuperaPartidasMes(database, usuario, anoMes);
        } catch (Exception e) {
            gravarExcecao(e, activity, "Erro ao recuperar as Partidas.");
        }
        return null;
    }

    public Partida recuperaPartidaMaisResultado(Activity activity, Usuario usuario, String anoMesMin, String anoMesMax, String ordem) throws Exception {
        try {
            return new PartidaBanco().recuperaPartidaMaisResultado(database, usuario, anoMesMin, anoMesMax, ordem);
        } catch (Exception e) {
            gravarExcecao(e, activity, "Erro ao recuperar Partidas.");
        }
        return null;
    }


    /** MÉTODOS PARA ATUALIZAR OBJETOS NO BANCO **/

    public int atualizarUsuario(Activity activity, Usuario usuario) throws Exception {
        try {
            return new UsuarioBanco().atualizar(usuario, database);
        } catch (Exception e) {
            gravarExcecao(e, activity, "Erro ao atualizar Usuário.");
        }
        return -1;
    }

    public int atualizarTime(Activity activity, Time time) throws Exception {
        try {
            return new TimeBanco().atualizarTime(time, database);
        } catch (Exception e) {
            gravarExcecao(e, activity, "Erro ao atualizar Time.");
        }
        return -1;
    }

    public int atualizarEndereco(Activity activity, Endereco endereco) throws Exception {
        try {
            return new EnderecoBanco().atualizarEndereco(endereco, database);
        } catch (Exception e) {
            gravarExcecao(e, activity, "Erro ao atualizar Endereco.");
        }
        return -1;
    }

    public int atualizarPartida(Activity activity, Partida partida) throws Exception {
        try {
            return new PartidaBanco().atualizar(partida, database);
        } catch (Exception e) {
            gravarExcecao(e, activity, "Erro ao atualizar Partida.");
        }
        return -1;
    }

    /** MÉTODOS PARA REMOVER OBJETOS NO BANCO **/


    public int removerTime(Activity activity, Time time) throws Exception {
        try {
            return new TimeBanco().removerTime(time, database);
        } catch (Exception e) {
            gravarExcecao(e, activity, "Erro ao remover Time.");
        }
        return -1;
    }

    public int removerEndereco(Activity activity, Endereco endereco) throws Exception {
        try {
            return new EnderecoBanco().removerTime(endereco, database);
        } catch (Exception e) {
            gravarExcecao(e, activity, "Erro ao remover Endereco.");
        }
        return -1;
    }

    public int removerPartida(Activity activity, Partida partida) throws Exception {
        try {
            return new PartidaBanco().remover(partida, database);
        } catch (Exception e) {
            gravarExcecao(e, activity, "Erro ao remover Partida.");
        }
        return -1;
    }

    public int removerTimeUsuario(Activity activity, Usuario usuario) throws Exception {
        try {
            return new TimeBanco().removerTimeUsuario(usuario, database);
        } catch (Exception e) {
            gravarExcecao(e, activity, "Erro ao remover Time.");
        }
        return 0;
    }

    /** OUTROS MÉTODOS PARA CONSULTA DO BANCO **/

    public boolean existePartidaTime(Activity activity, Usuario usuario, Time time) throws Exception {
        try {
            return new PartidaBanco().existePartidaTime(database, usuario, time);
        } catch (Exception e) {
            gravarExcecao(e, activity, "Erro ao remover Time.");
        }
        return false;
    }

    public boolean existePartidaEndereco(Activity activity, Usuario usuario, Endereco endereco) throws Exception {
        try {
            return new PartidaBanco().existePartidaEndereco(database, usuario, endereco);
        } catch (Exception e) {
            gravarExcecao(e, activity, "Erro ao remover Endereço.");
        }
        return false;
    }



    private void gravarExcecao(Exception e, Activity activity, String msg) {
        e.printStackTrace();
        View view = activity.findViewById(android.R.id.content);
        Alerta.exibeSnackbarCurto(view, msg);
    }


}
