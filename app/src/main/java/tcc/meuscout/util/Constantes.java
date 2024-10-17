package tcc.meuscout.util;

public abstract class Constantes {

    public final static String NOME_PASTA_SISTEMA = "/Meu Scout";
    public final static String DATABASE_NAME = "MEUSCOUT.db";
    public final static String CAMINHO_BD = "data/data/tcc.meuscout/databases/";
    public final static String CAMINHO_BD_FILE = "/data/tcc.meuscouta/databases/" + DATABASE_NAME;

    public final static String FORMATO_DATA_HORA_SISTEMA = "yyyy/MM/dd HH:mm";
    public final static String FORMATO_DATA_HORA = "dd/MM/yyyy HH:mm";
    public final static String FORMATO_DATA_HORA_SEG = "dd/MM/yyyy HH:mm:ss";
    public static final String FORMATO_DATA = "dd/MM/yyyy";
    public static final String FORMATO_HORA = "HH:mm";
    public final static String MSG_GENERICA_ERRO = "Um erro inesperado ocorreu.";
    public final static String MSG_ERRO_CONEXAO = "Um erro de conexão ocorreu.";
    public static final String INSERIR = "INSERIR";
    public static final String EDITAR = "EDITAR";
    public static final String EXCLUIR = "EXCLUIR";
    public static final String VISUALIZAR = "VISUALIZAR";
    public static final String STRING_OK = "OK";
    public static final String PARCELABLE_OBJ = "PARCELABLE_OBJ";
    public static final String SERIALIZABLE_OBJ = "SERIALIZABLE_OBJ";
    public static final String OPERACAO = "OPERACAO";
    public static final String ADVERSARIO = "ADVERSARIO";
    public static final String TABELA_USUARIOS = "usuarios";
    public static final String TABELA_PARTIDAS = "partidas";
    public static final String RESULT = "RESULT";

    public static final String ORDEM_VITORIAS = "ORDER BY VITORIAS DESC, DERROTAS ASC, EMPATES DESC, GOLSFEITOS DESC, ASSISTENCIAS DESC, " +
            "DEFESAS DESC, DESARMES DESC, FINALCERTAS DESC, CORTES DESC, BLOQUEIOS DESC";
    public static final String ORDEM_EMPATES = "ORDER BY EMPATES DESC, VITORIAS DESC, DERROTAS ASC, GOLSFEITOS DESC, ASSISTENCIAS DESC, " +
            "DEFESAS DESC, DESARMES DESC, FINALCERTAS DESC, CORTES DESC, BLOQUEIOS DESC";
    public static final String ORDEM_DERROTAS = "ORDER BY DERROTAS DESC, EMPATES DESC, VITORIAS ASC, GOLSFEITOS DESC, ASSISTENCIAS DESC, " +
            "DEFESAS DESC, DESARMES DESC, FINALCERTAS DESC, CORTES DESC, BLOQUEIOS DESC";

    public static final int SEARCH_LENGTH = 2;

    public static final int REQUESTCODE_100 = 100;
    public static final int REQUESTCODE_200 = 200;
    public static final int REQUESTCODE_300 = 300;
    public static final int REQUESTCODE_400 = 400;
    public static final int REQUESTCODE_500 = 500;
    public static final int REQUESTCODE_600 = 600;
    public static final int REQUESTCODE_700 = 700;
    public static final int REQUESTCODE_800 = 800;
    public static final int REQUESTCODE_900 = 900;
}
