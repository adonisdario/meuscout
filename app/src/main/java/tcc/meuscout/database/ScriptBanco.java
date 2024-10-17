package tcc.meuscout.database;

public class ScriptBanco {

    public static String[] SCRIPT_CREATE = new String[]{

            //TODO -- USUARIO
            " CREATE TABLE IF NOT EXISTS [USUARIO] ("
                    + " [ID] INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + " [FIREBASE_ID] VARCHAR(1000), "
                    + " [NOME] VARCHAR2(50), "
                    + " [EMAIL] VARCHAR2(100), "
                    + " [CIDADE] VARCHAR2(50), "
                    + " [PERNA] VARCHAR2(10), "
                    + " [POSICAO_NOME] VARCHAR2(100), "
                    + " [POSICAO_SIGLA] VARCHAR2(3), "
                    + " [POSICAO_NUM] NUMBER(1), "
                    + " [TIME_ID_FIREBASE] VARCHAR2(1000), "
                    + " [TIME_NOME] VARCHAR2(100), "
                    + " [TIME_SIGLA] VARCHAR2(3), "
                    + " [TIME_ADVERSARIO] CHAR(1), "
                    + " [LOGADO] CHAR(1), "
                    + " [DATA_NASCIMENTO] CHAR(10) ); ",

            //TODO -- PARTIDA
            " CREATE TABLE IF NOT EXISTS [PARTIDA] ("
                    + " [ID] INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + " [FIREBASE_ID] VARCHAR(1000), "
                    + " [GOLSFEITOS] INTEGER, "
                    + " [GOLSPD] INTEGER, "
                    + " [GOLSPE] INTEGER, "
                    + " [GOLSCA] INTEGER, "
                    + " [GOLSOU] INTEGER, "
                    + " [PENALTIAC] INTEGER, "
                    + " [PENALTIER] INTEGER, "
                    + " [GOLSSOFRIDOS] INTEGER, "
                    + " [GOLSSOFRIDOSPEN] INTEGER, "
                    + " [ASSISTENCIAS] INTEGER, "
                    + " [DEFESAS] INTEGER, "
                    + " [DEFESASPEN] INTEGER, "
                    + " [DESARMES] INTEGER, "
                    + " [BLOQUEIOS] INTEGER, "
                    + " [CORTES] INTEGER, "
                    + " [ERROS] INTEGER, "
                    + " [FINALTOTAL] INTEGER, "
                    + " [FINALERRADAS] INTEGER, "
                    + " [FINALCERTAS] INTEGER, "
                    + " [FINALTRAVE] INTEGER, "
                    + " [FINALBLOQ] INTEGER, "
                    + " [CARTOESAMA] INTEGER, "
                    + " [CARTOESVERM] INTEGER, "
                    + " [CARTOESAZUL] INTEGER, "
                    + " [FALTASREC] INTEGER, "
                    + " [FALTASCOM] INTEGER, "
                    + " [QTDPARTIDAS] INTEGER, "
                    + " [VITORIAS] INTEGER, "
                    + " [EMPATES] INTEGER, "
                    + " [DERROTAS] INTEGER, "
                    + " [PLACARCASA] INTEGER, "
                    + " [PLACARFORA] INTEGER, "
                    + " [NOTAIND] NUMBER(1), "
                    + " [NOTAPART] NUMBER(1), "
                    + " [DURACAO] INTEGER, "
                    + " [POSICAO_NOME] VARCHAR2(100), "
                    + " [POSICAO_SIGLA] VARCHAR2(10), "
                    + " [POSICAO_NUM] NUMBER(1), "
                    + " [POSICAOSEC_NOME] VARCHAR2(100), "
                    + " [POSICAOSEC_SIGLA] VARCHAR2(10), "
                    + " [POSICAOSEC_NUM] NUMBER(1), "
                    + " [TIME_ID_FIREBASE] VARCHAR2(1000) CONSTRAINT [FK_TIMEADV_PARTIDA] REFERENCES [TIME]([FIREBASE_ID]), "
                    + " [TIME_ADV_ID_FIREBASE] VARCHAR2(1000) CONSTRAINT [FK_TIME_PARTIDA] REFERENCES [TIME]([FIREBASE_ID]), "
                    + " [USUARIO_ID_FIREBASE] VARCHAR2(1000) CONSTRAINT [FK_USUARIO_PARTIDA] REFERENCES [USUARIO]([FIREBASE_ID]), "
                    + " [NOMELOCAL] VARCHAR2(50), "
                    + " [ENDERECO] VARCHAR2(50), "
                    + " [TIPOREGISTRO] VARCHAR2(50), "
                    + " [DATA] VARCHAR2(50) ); ",

            //TODO -- TIME
            " CREATE TABLE IF NOT EXISTS [TIME] ("
                    + " [ID] INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + " [FIREBASE_ID] VARCHAR(1000), "
                    + " [USUARIO_ID_FIREBASE] VARCHAR2(1000) CONSTRAINT [FK_USUARIO_TIME] REFERENCES [USUARIO]([FIREBASE_ID]), "
                    + " [NOME] VARCHAR2(100), "
                    + " [SIGLA] VARCHAR2(3), "
                    + " [TIME_USUARIO] CHAR(1) ); ",

            //TODO -- POSICAO
            " CREATE TABLE IF NOT EXISTS [POSICAO] ("
                    + " [ID] INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + " [FIREBASE_ID] VARCHAR(1000), "
                    + " [USUARIO_ID_FIREBASE] VARCHAR2(1000) CONSTRAINT [FK_USUARIO_POSICAO] REFERENCES [USUARIO]([FIREBASE_ID]), "
                    + " [NOME] VARCHAR2(100), "
                    + " [SIGLA] VARCHAR2(10), "
                    + " [NUM] INTEGER ); ",

            //TODO -- ENDERECO
            " CREATE TABLE IF NOT EXISTS [ENDERECO] ("
                    + " [ID] INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + " [FIREBASE_ID] VARCHAR(1000), "
                    + " [USUARIO_ID_FIREBASE] VARCHAR2(1000) CONSTRAINT [FK_USUARIO_POSICAO] REFERENCES [USUARIO]([FIREBASE_ID]), "
                    + " [NOMELOCAL] VARCHAR(200), "
                    + " [ENDERECO] VARCHAR2(1000) ); ",

            //TODO -- RANKING
            " CREATE TABLE IF NOT EXISTS [RANKING] ("
                    + " [ID] INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + " [POS_SIGLA] VARCHAR2(3), "
                    + " [RANK] VARCHAR2(30), "
                    + " [GOLS90] NUMBER(4), "
                    + " [ASS90] NUMBER(4), "
                    + " [CH90] NUMBER(4), "
                    + " [CHCER90] NUMBER(4), "
                    + " [CHCERPCT] NUMBER(4), "
                    + " [CHGOL] NUMBER(4), "
                    + " [CHCHERGOL] NUMBER(4), "
                    + " [PENPCT] NUMBER(4), "
                    + " [DES90] NUMBER(4), "
                    + " [COR90] NUMBER(4), "
                    + " [BLOQ90] NUMBER(4), "
                    + " [FALSOF] NUMBER(4), "
                    + " [FALCOM] NUMBER(4), "
                    + " [JGSCA] NUMBER(4), "
                    + " [JGSCV] NUMBER(4), "
                    + " [DEFPCT] NUMBER(4), "
                    + " [CHCER90GOL] NUMBER(4), "
                    + " [GOLSOF90] NUMBER(4), "
                    + " [CS] NUMBER(4), "
                    + " [DEF90] NUMBER(4), "
                    + " [PENDEFPCT] NUMBER(4)  ); ",


    };

    public static String[] SCRIPT_RANKING = new String[]{

            " INSERT INTO RANKING (POS_SIGLA, RANK, GOLS90, ASS90, CH90, CHCER90, CHCERPCT, CHGOL, CHCHERGOL, PENPCT," +
                    " DES90, COR90, BLOQ90, FALSOF, FALCOM, JGSCA, JGSCV, DEFPCT, CHCER90GOL, GOLSOF90, CS, DEF90, PENDEFPCT) " +
                    " VALUES ('ATA', 'Diamante', 1.31, 0.56, 5.36, 2.31, 61.4, 2.49, 1.09, 96, 2.08, 1.96, 2.41, 4.67, 3.53, 2.18, 12.66, 0, 0, 0, 0, 0, 0)," +
                    " ('ATA', 'Ouro 3', 1.30, 0.55, 5.35, 2.30, 61.3, 2.50, 1.10, 95, 2.07, 1.95, 2.40, 4.66, 3.52, 2.19, 12.67, 0, 0, 0, 0, 0, 0)," +
                    " ('ATA', 'Ouro 2', 1.01, 0.48, 4.59, 1.99, 57.4, 2.97, 1.28, 90, 1.96, 1.69, 2.22, 3.94, 3.18, 2.49, 13.70, 0, 0, 0, 0, 0, 0)," +
                    " ('ATA', 'Ouro 1', 0.80, 0.40, 3.92, 1.62, 53.1, 3.41, 1.47, 80, 1.84, 1.49, 2.04, 3.44, 2.68, 2.75, 14.80, 0, 0, 0, 0, 0, 0)," +
                    " ('ATA', 'Prata 3', 0.71, 0.35, 3.61, 1.48, 50.5, 3.70, 1.62, 74, 1.64, 1.35, 1.78, 3.14, 2.41, 3.01, 15.68, 0, 0, 0, 0, 0, 0)," +
                    " ('ATA', 'Média', 0.60, 0.29, 3.17, 1.30, 46.9, 4.45, 1.89, 67, 1.54, 1.21, 1.68, 2.70, 2.00, 3.88, 21.80, 0, 0, 0, 0, 0, 0)," +
                    " ('ATA', 'Prata 2', 0.56, 0.27, 3.00, 1.23, 45.5, 4.50, 1.98, 70, 1.44, 1.13, 1.49, 2.56, 1.90, 3.80, 22.50, 0, 0, 0, 0, 0, 0)," +
                    " ('ATA', 'Prata 1', 0.49, 0.23, 2.71, 1.12, 43.4, 5.20, 2.16, 65, 1.36, 1.04, 1.43, 2.26, 1.58, 4.72, 26.48, 0, 0, 0, 0, 0, 0)," +
                    " ('ATA', 'Bronze 3', 0.46, 0.22, 2.61, 1.07, 42.5, 5.49, 2.25, 60, 1.27, 0.94, 1.37, 2.16, 1.45, 5.17, 27.43, 0, 0, 0, 0, 0, 0)," +
                    " ('ATA', 'Bronze 2', 0.43, 0.21, 2.51, 1.05, 41.4, 5.77, 2.35, 50, 1.20, 0.84, 1.35, 2.07, 1.34, 5.61, 28.38, 0, 0, 0, 0, 0, 0)," +
                    " ('ATA', 'Bronze 1', 0.42, 0.20, 2.45, 1.04, 40.9, 5.89, 2.40, 40, 1.11, 0.73, 1.29, 2.04, 1.29, 6.27, 29.33, 0, 0, 0, 0, 0, 0)," +
                    " ('ATA', 'Sem Rank', 0.40, 0.15, 2.35, 1.00, 39.5, 6.00, 2.45, 34, 1.00, 0.65, 1.20, 2.00, 1.25, 6.80, 30.00, 0, 0, 0, 0, 0, 0)," +
                    " ('ATA', 'Ruim', 0.20, 0.08, 1.18, 0.50, 19.8, 12.00, 4.90, 17, 0.5, 0.33, 0.6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)," +
                    " ('ATA', 'Esperado', 3, 3, 3, 3, 3, 3, 3, 3, 1, 1, 1, 3, 1, 1, 1, 0, 0, 0, 0, 0, 0)," +
                    " ('CA', 'Esperado', 4, 1, 4, 4, 4, 4, 4, 3, 1, 2, 1, 3, 1, 1, 1, 0, 0, 0, 0, 0, 0)," +
                    " ('PD/PE', 'Esperado', 3, 3, 3, 3, 3, 3, 3, 3, 1, 1, 1, 3, 1, 1, 1, 0, 0, 0, 0, 0, 0)," +
                    " ('SA', 'Esperado', 3, 3, 3, 3, 3, 3, 3, 3, 1, 1, 1, 3, 1, 1, 1, 0, 0, 0, 0, 0, 0);",

            " INSERT INTO RANKING (POS_SIGLA, RANK, GOLS90, ASS90, CH90, CHCER90, CHCERPCT, CHGOL, CHCHERGOL, PENPCT," +
                    " DES90, COR90, BLOQ90, FALSOF, FALCOM, JGSCA, JGSCV, DEFPCT, CHCER90GOL, GOLSOF90, CS, DEF90, PENDEFPCT) " +
                    " VALUES ('MEI', 'Diamante', 0.38, 0.45, 1.87, 0.84, 57.4, 2.92, 1.12, 96, 3.33, 9.96, 3.56, 3.03, 2.89, 1.69, 13.44, 0, 0, 0, 0, 0, 0)," +
                    " ('MEI', 'Ouro 3', 0.37, 0.44, 1.86, 0.83, 57.3, 2.93, 1.13, 95, 3.32, 9.95, 3.55, 3.02, 2.88, 1.70, 13.45, 0, 0, 0, 0, 0, 0)," +
                    " ('MEI', 'Ouro 2', 0.26, 0.39, 1.66, 0.65, 50.3, 4.02, 1.46, 90, 3.14, 8.75, 3.21, 2.49, 2.53, 1.94, 14.85, 0, 0, 0, 0, 0, 0)," +
                    " ('MEI', 'Ouro 1', 0.23, 0.28, 1.52, 0.60, 47.8, 4.43, 1.64, 80, 2.91, 7.85, 2.93, 2.06, 2.20, 2.19, 16.90, 0, 0, 0, 0, 0, 0)," +
                    " ('MEI', 'Prata 3', 0.19, 0.24, 1.38, 0.55, 45.3, 4.83, 1.82, 74, 2.73, 7.37, 2.75, 1.94, 2.05, 2.40, 18.96, 0, 0, 0, 0, 0, 0)," +
                    " ('MEI', 'Média', 0.16, 0.18, 1.03, 0.40, 39.5, 6.66, 2.60, 67, 2.49, 6.71, 2.52, 1.61, 1.79, 2.80, 24.70, 0, 0, 0, 0, 0, 0)," +
                    " ('MEI', 'Prata 2', 0.15, 0.17, 0.93, 0.38, 39.7, 6.50, 2.55, 70, 2.39, 6.42, 2.42, 1.52, 1.74, 2.84, 25.95, 0, 0, 0, 0, 0, 0)," +
                    " ('MEI', 'Prata 1', 0.13, 0.11, 0.68, 0.26, 33.7, 8.42, 3.38, 65, 2.24, 6.04, 2.28, 1.28, 1.52, 3.20, 30.41, 0, 0, 0, 0, 0, 0)," +
                    " ('MEI', 'Bronze 3', 0.11, 0.10, 0.61, 0.24, 31.5, 9.21, 3.63, 60, 2.19, 5.90, 2.22, 1.22, 1.45, 3.34, 32.05, 0, 0, 0, 0, 0, 0)," +
                    " ('MEI', 'Bronze 2', 0.10, 0.08, 0.54, 0.21, 29.3, 10.00, 3.87, 50, 2.14, 5.77, 2.19, 1.07, 1.37, 3.51, 33.68, 0, 0, 0, 0, 0, 0)," +
                    " ('MEI', 'Bronze 1', 0.09, 0.07, 0.40, 0.16, 25, 11.72, 4.69, 40, 2.12, 5.73, 2.18, 1.01, 1.35, 3.57, 35.53, 0, 0, 0, 0, 0, 0)," +
                    " ('MEI', 'Sem Rank', 0.07, 0.05, 0.30, 0.10, 20, 12, 4.75, 34, 2.10, 5.65, 2.15, 0.95, 1.30, 3.65, 37.00, 0, 0, 0, 0, 0, 0)," +
                    " ('MEI', 'Ruim', 0.04, 0.03, 0.15, 0.05, 10, 6, 9.50, 17, 1.05, 2.83, 1.08, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)," +
                    " ('MEI', 'Esperado', 2, 2, 2, 2, 3, 2, 2, 3, 3, 3, 3, 3, 2, 2, 2, 0, 0, 0, 0, 0, 0)," +
                    " ('MAT', 'Esperado', 3, 4, 3, 3, 4, 4, 4, 3, 1, 2, 1, 3, 1, 1, 1, 0, 0, 0, 0, 0, 0)," +
                    " ('MD/ME', 'Esperado', 3, 4, 3, 3, 3, 3, 3, 3, 1, 2, 1, 3, 1, 1, 1, 0, 0, 0, 0, 0, 0)," +
                    " ('MC', 'Esperado', 1, 1, 2, 2, 3, 2, 2, 3, 3, 3, 3, 3, 2, 2, 2, 0, 0, 0, 0, 0, 0)," +
                    " ('VOL', 'Esperado', 1, 1, 1, 1, 3, 1, 1, 3, 4, 4, 4, 3, 3, 3, 3, 0, 0, 0, 0, 0, 0);",

            " INSERT INTO RANKING (POS_SIGLA, RANK, GOLS90, ASS90, CH90, CHCER90, CHCERPCT, CHGOL, CHCHERGOL, PENPCT," +
                    " DES90, COR90, BLOQ90, FALSOF, FALCOM, JGSCA, JGSCV, DEFPCT, CHCER90GOL, GOLSOF90, CS, DEF90, PENDEFPCT) " +
                    " VALUES ('DEF', 'Diamante', 0.38, 0.45, 1.87, 0.84, 57.4, 2.92, 1.12, 96, 3.33, 9.96, 3.56, 3.03, 2.89, 1.69, 13.44, 0, 0, 0, 0, 0, 0)," +
                    " ('DEF', 'Ouro 3', 0.37, 0.44, 1.87, 0.83, 57.3, 2.93, 1.13, 95, 3.32, 9.95, 3.55, 3.02, 2.88, 1.70, 13.45, 0, 0, 0, 0, 0, 0)," +
                    " ('DEF', 'Ouro 2', 0.26, 0.39, 1.66, 0.65, 50.3, 4.02, 1.46, 90, 3.14, 8.75, 3.21, 2.49, 2.53, 1.94, 14.85, 0, 0, 0, 0, 0, 0)," +
                    " ('DEF', 'Ouro 1', 0.23, 0.28, 1.52, 0.60, 47.8, 4.43, 1.64, 80, 2.91, 7.85, 2.93, 2.06, 2.20, 2.19, 16.90, 0, 0, 0, 0, 0, 0)," +
                    " ('DEF', 'Prata 3', 0.19, 0.24, 1.38, 0.55, 45.3, 4.83, 1.82, 74, 2.73, 7.37, 2.75, 1.94, 2.05, 2.40, 18.96, 0, 0, 0, 0, 0, 0)," +
                    " ('DEF', 'Média', 0.16, 0.18, 1.03, 0.40, 39.5, 6.66, 2.60, 67, 2.49, 6.71, 2.52, 1.61, 1.79, 2.80, 24.70, 0, 0, 0, 0, 0, 0)," +
                    " ('DEF', 'Prata 2', 0.15, 0.17, 0.93, 0.38, 39.7, 6.50, 2.55, 70, 2.39, 6.42, 2.42, 1.52, 1.74, 2.84, 25.95, 0, 0, 0, 0, 0, 0)," +
                    " ('DEF', 'Prata 1', 0.13, 0.11, 0.68, 0.26, 33.7, 8.42, 3.38, 65, 2.24, 6.04, 2.28, 1.28, 1.52, 3.20, 30.41, 0, 0, 0, 0, 0, 0)," +
                    " ('DEF', 'Bronze 3', 0.11, 0.10, 0.61, 0.24, 31.5, 9.21, 3.63, 60, 2.19, 5.90, 2.22, 1.22, 1.45, 3.34, 32.05, 0, 0, 0, 0, 0, 0)," +
                    " ('DEF', 'Bronze 2', 0.10, 0.08, 0.54, 0.21, 29.3, 10.00, 3.87, 50, 2.14, 5.77, 2.19, 1.07, 1.37, 3.51, 33.68, 0, 0, 0, 0, 0, 0)," +
                    " ('DEF', 'Bronze 1', 0.09, 0.07, 0.40, 0.16, 25.0, 11.72, 4.69, 40, 2.12, 5.73, 2.18, 1.01, 1.35, 3.57, 35.53, 0, 0, 0, 0, 0, 0)," +
                    " ('DEF', 'Sem Rank', 0.07, 0.05, 0.30, 0.10, 20.0, 12.00, 4.75, 34, 2.10, 5.65, 2.15, 0.95, 1.30, 3.65, 37.00, 0, 0, 0, 0, 0, 0)," +
                    " ('DEF', 'Ruim', 0.04, 0.03, 0.15, 0.05, 10.0, 6.00, 2.38, 17, 1.05, 2.83, 1.08, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)," +
                    " ('DEF', 'Esperado', 1, 1, 1, 1, 1, 1, 1, 3, 4, 4, 4, 2, 4, 4, 3, 0, 0, 0, 0, 0, 0)," +
                    " ('ZAG', 'Esperado', 1, 1, 1, 1, 1, 1, 1, 3, 4, 4, 4, 2, 4, 4, 3, 0, 0, 0, 0, 0, 0)," +
                    " ('LD/LE', 'Esperado', 1, 2, 1, 1, 1, 1, 1, 3, 3, 3, 3, 3, 3, 3, 1, 0, 0, 0, 0, 0, 0)," +
                    " ('AD/AE', 'Esperado', 1, 2, 1, 1, 1, 1, 1, 3, 3, 3, 3, 3, 3, 3, 1, 0, 0, 0, 0, 0, 0);",

            " INSERT INTO RANKING (POS_SIGLA, RANK, GOLS90, ASS90, CH90, CHCER90, CHCERPCT, CHGOL, CHCHERGOL, PENPCT," +
                    " DES90, COR90, BLOQ90, FALSOF, FALCOM, JGSCA, JGSCV, DEFPCT, CHCER90GOL, GOLSOF90, CS, DEF90, PENDEFPCT) " +
                    " VALUES ('GOL', 'Diamante', 0, 0, 0, 0, 0, 0, 0, 96, 0, 1.56, 0, 0, 0, 0, 0, 79.5, 2.28, 0.63, 53.4, 4.22, 55.7)," +
                    " ('GOL', 'Ouro 3', 0, 0, 0, 0, 0, 0, 0, 95, 0, 1.55, 0, 0, 0, 0, 0, 79.4, 2.29, 0.64, 53.3, 4.23, 55.6)," +
                    " ('GOL', 'Ouro 2', 0, 0, 0, 0, 0, 0, 0, 90, 0, 1.32, 0, 0, 0, 0, 0, 76.0, 2.52, 0.77, 49.1, 3.82, 50.4)," +
                    " ('GOL', 'Ouro 1', 0, 0, 0, 0, 0, 0, 0, 80, 0, 1.08, 0, 0, 0, 0, 0, 72.1, 2.95, 0.95, 40.7, 3.39, 36.0)," +
                    " ('GOL', 'Prata 3', 0, 0, 0, 0, 0, 0, 0, 74, 0, 0.95, 0, 0, 0, 0, 0, 70.5, 3.19, 1.08, 36.0, 3.15, 32.2)," +
                    " ('GOL', 'Média', 0, 0, 0, 0, 0, 0, 0, 67, 0, 0.74, 0, 0, 0, 0, 0, 66.8, 3.88, 1.34, 27.8, 2.70, 18.8)," +
                    " ('GOL', 'Prata 2', 0, 0, 0, 0, 0, 0, 0, 70, 0, 0.69, 0, 0, 0, 0, 0, 66.9, 3.79, 1.38, 26.7, 2.60, 20)," +
                    " ('GOL', 'Prata 1', 0, 0, 0, 0, 0, 0, 0, 65, 0, 0.53, 0, 0, 0, 0, 0, 63.2, 4.57, 1.60, 19.5, 2.24, 5.7)," +
                    " ('GOL', 'Bronze 3', 0, 0, 0, 0, 0, 0, 0, 60, 0, 0.46, 0, 0, 0, 0, 0, 61.3, 4.88, 1.70, 16.5, 2.05, 4)," +
                    " ('GOL', 'Bronze 2', 0, 0, 0, 0, 0, 0, 0, 50, 0, 0.35, 0, 0, 0, 0, 0, 58.2, 5.40, 1.96, 13.0, 1.71, 3)," +
                    " ('GOL', 'Bronze 1', 0, 0, 0, 0, 0, 0, 0, 40, 0, 0.26, 0, 0, 0, 0, 0, 55.6, 6.08, 2.30, 10.5, 1.48, 2)," +
                    " ('GOL', 'Sem Rank', 0, 0, 0, 0, 0, 0, 0, 34, 0, 0.15, 0, 0, 0, 0, 0, 50, 6.70, 3.00, 7, 1.25, 1)," +
                    " ('GOL', 'Ruim', 0, 0, 0, 0, 0, 0, 0, 17, 0, 0, 0, 0, 0, 0, 0, 25, 7.50, 3.70, 3.5, 0, 0.5);"
    };

    public static String[] SCRIPT_UPDATE = new String[]{
            ""
    };
}
