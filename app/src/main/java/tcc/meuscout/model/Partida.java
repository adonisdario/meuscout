package tcc.meuscout.model;

import android.app.Activity;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.Date;

import tcc.meuscout.activities.CadastroActivity;
import tcc.meuscout.database.ControleBanco;
import tcc.meuscout.firebase.ConfiguracaoFirebase;
import tcc.meuscout.fragments.PartidaTabFragment;
import tcc.meuscout.fragments.TimeFragment;
import tcc.meuscout.util.Alerta;
import tcc.meuscout.util.Constantes;
import tcc.meuscout.util.Conversao;

public class Partida implements Serializable, Parcelable {
    private int golsFeitos, golsPD, golsPE, golsCA, golsOU, golsSofridos, golsSofridosPen,
            assistencias, defesas, finalTotal, finalErradas, finalCertas, finalTrave, finalBloq,
            cartoesAma, cartoesVerm, cartoesAzul, faltasRec, faltasCom,
            qtdPartidas, notaInd, notaPart, duracao, desarmes, cortes, bloqueios, erros,
            penaltiAc, penaltiEr, posicao_num, posicaosec_num, placarCasa, placarFora,
            vitorias, empates, derrotas, defesasPen;
    private String nomeLocal, endereco, tipoRegistro, firebase_id;
    private Integer id;
    private String time_id_firebase, time_adv_id_firebase, usuario_id_firebase;
    private String data, posicao_nome, posicao_sigla, posicaosec_nome, posicaosec_sigla;
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();


    public Partida(Parcel in) {
        golsFeitos = in.readInt();
        golsPD = in.readInt();
        golsPE = in.readInt();
        golsCA = in.readInt();
        golsOU = in.readInt();
        golsSofridos = in.readInt();
        golsSofridosPen = in.readInt();
        assistencias = in.readInt();
        defesas = in.readInt();
        finalTotal = in.readInt();
        finalErradas = in.readInt();
        finalCertas = in.readInt();
        finalTrave = in.readInt();
        finalBloq = in.readInt();
        cartoesAma = in.readInt();
        cartoesVerm = in.readInt();
        cartoesAzul = in.readInt();
        faltasRec = in.readInt();
        faltasCom = in.readInt();
        qtdPartidas = in.readInt();
        notaInd = in.readInt();
        notaPart = in.readInt();
        duracao = in.readInt();
        desarmes = in.readInt();
        cortes = in.readInt();
        bloqueios = in.readInt();
        erros = in.readInt();
        penaltiAc = in.readInt();
        penaltiEr = in.readInt();
        posicao_num = in.readInt();
        posicaosec_num = in.readInt();
        placarCasa = in.readInt();
        placarFora = in.readInt();
        vitorias = in.readInt();
        empates = in.readInt();
        derrotas = in.readInt();
        defesasPen = in.readInt();
        nomeLocal = in.readString();
        endereco = in.readString();
        tipoRegistro = in.readString();
        firebase_id = in.readString();
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        time_id_firebase = in.readString();
        time_adv_id_firebase = in.readString();
        usuario_id_firebase = in.readString();
        data = in.readString();
        posicao_nome = in.readString();
        posicao_sigla = in.readString();
        posicaosec_nome = in.readString();
        posicaosec_sigla = in.readString();
    }

    public static final Creator<Partida> CREATOR = new Creator<Partida>() {
        @Override
        public Partida createFromParcel(Parcel in) {
            return new Partida(in);
        }

        @Override
        public Partida[] newArray(int size) {
            return new Partida[size];
        }
    };

    public Partida() {

    }

    public void cadastrarPartidaFirebase(Usuario usuario, Activity activity) { // Este método salva o objeto time no BD Firebase
        Date d = new Date();
        String dataC = Conversao.DataParaString(d, Constantes.FORMATO_DATA_HORA_SISTEMA);

       String data = dataC.substring(0, 4) + dataC.substring(5, 7) + dataC.substring(8, 10) +
                dataC.substring(11, 13) + dataC.substring(14, 16);
        /*//FORMATO CERTO
        String id = getData().substring(0, 4) + getData().substring(5, 7) + getData().substring(8, 10) +
                getData().substring(11, 13) + getData().substring(14, 16);*/
        /*//FORMATO "ERRADO"
        String dia = getData().substring(0, 2);
        String mes = getData().substring(3, 5);
        String ano = getData().substring(6, 10);
        String hora = post.getData().substring(11, 13);
        String minuto = post.getData().substring(14, 16);*/
        setFirebase_id(data);
        DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseDatabase()
                .child("usuarios").child(usuario.getFirebase_id()).child("partidas").child(getFirebase_id());
        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    cadastrarPartidaBanco(activity);
                    Toast.makeText(activity.getApplicationContext(), "Partida cadastrada!", Toast.LENGTH_SHORT).show();
                    activity.finish();
                    firebase.removeEventListener(this);
                } catch (Exception e) {
                    e.printStackTrace();
                    View view = activity.findViewById(android.R.id.content);
                    Alerta.exibeSnackbarCurto(view, Constantes.MSG_GENERICA_ERRO);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                View view = activity.findViewById(android.R.id.content);
                Alerta.exibeSnackbarCurto(view, "Erro na conexão do Firebase.");
            }
        });
        firebase.setValue(this);
    }

    public void atualizarPartidaFirebase(Activity activity, Usuario usuario, String operacao) {
        removerPartidaFirebase(activity, usuario, operacao);
    }

    public void removerPartidaFirebase(Activity activity, Usuario usuario, String operacao) {
        View view = activity.findViewById(android.R.id.content);
        DatabaseReference partidaFB = reference.child("usuarios").child(this.getUsuario_id_firebase())
                .child("partidas").child(this.getFirebase_id());
        partidaFB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    int a = removerPartidaBanco(activity);
                    if (operacao.equals(Constantes.EDITAR))
                        cadastrarPartidaFirebase(usuario, activity);
                    partidaFB.removeEventListener(this);

                } catch (Exception e) {
                    e.printStackTrace();
                    Alerta.exibeSnackbarCurto(view, Constantes.MSG_GENERICA_ERRO);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Alerta.exibeSnackbarCurto(view, "Um erro de conexão ocorreu");
            }
        });
        partidaFB.removeValue();
    }

    public Partida cadastrarPartidaBanco(Activity activity) throws Exception {
        this.setId(ControleBanco.getInstance().inserirPartida(activity, this));
        return this;
    }

    public int atualizarPartidaBanco(Activity activity) throws Exception {
        int a = ControleBanco.getInstance().atualizarPartida(activity, this);
        return a;
    }

    public int atualizarPartidaBancoVoltar(FragmentActivity activity) throws Exception {
        int a = ControleBanco.getInstance().atualizarPartida(activity, this);
        //PartidaFragment.getInstance().retornarTela();
        return a;
    }

    public int removerPartidaBanco(Activity activity) throws Exception {
        int a = ControleBanco.getInstance().removerPartida(activity, this);
        return a;
    }

    public int getDefesasPen() {
        return defesasPen;
    }

    public void setDefesasPen(int defesasPen) {
        this.defesasPen = defesasPen;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirebase_id() {
        return firebase_id;
    }

    public void setFirebase_id(String firebase_id) {
        this.firebase_id = firebase_id;
    }

    @Exclude
    public String getUsuario_id_firebase() {
        return usuario_id_firebase;
    }

    public void setUsuario_id_firebase(String usuario_id_firebase) {
        this.usuario_id_firebase = usuario_id_firebase;
    }

    public int getVitorias() {
        return vitorias;
    }

    public void setVitorias(int vitorias) {
        this.vitorias = vitorias;
    }

    public int getEmpates() {
        return empates;
    }

    public void setEmpates(int empates) {
        this.empates = empates;
    }

    public int getDerrotas() {
        return derrotas;
    }

    public void setDerrotas(int derrotas) {
        this.derrotas = derrotas;
    }

    public int getPlacarCasa() {
        return placarCasa;
    }

    public void setPlacarCasa(int placarCasa) {
        this.placarCasa = placarCasa;
    }

    public int getPlacarFora() {
        return placarFora;
    }

    public void setPlacarFora(int placarFora) {
        this.placarFora = placarFora;
    }

    public int getGolsSofridosPen() {
        return golsSofridosPen;
    }

    public void setGolsSofridosPen(int golsSofridosPen) {
        this.golsSofridosPen = golsSofridosPen;
    }

    public int getErros() {
        return erros;
    }

    public void setErros(int erros) {
        this.erros = erros;
    }

    public int getBloqueios() {
        return bloqueios;
    }

    public void setBloqueios(int bloqueios) {
        this.bloqueios = bloqueios;
    }

    public int getDesarmes() {
        return desarmes;
    }

    public void setDesarmes(int desarmes) {
        this.desarmes = desarmes;
    }

    public int getCortes() {
        return cortes;
    }

    public void setCortes(int cortes) {
        this.cortes = cortes;
    }

    public int getFinalBloq() {
        return finalBloq;
    }

    public void setFinalBloq(int finalBloq) {
        this.finalBloq = finalBloq;
    }

    public int getCartoesAzul() {
        return cartoesAzul;
    }

    public void setCartoesAzul(int cartoesAzul) {
        this.cartoesAzul = cartoesAzul;
    }

    public int getGolsFeitos() {
        return golsFeitos;
    }

    public void setGolsFeitos(int golsFeitos) {
        this.golsFeitos = golsFeitos;
    }

    public int getGolsPD() {
        return golsPD;
    }

    public void setGolsPD(int golsPD) {
        this.golsPD = golsPD;
    }

    public int getGolsPE() {
        return golsPE;
    }

    public void setGolsPE(int golsPE) {
        this.golsPE = golsPE;
    }

    public int getGolsCA() {
        return golsCA;
    }

    public void setGolsCA(int golsCA) {
        this.golsCA = golsCA;
    }

    public int getGolsOU() {
        return golsOU;
    }

    public void setGolsOU(int golsOU) {
        this.golsOU = golsOU;
    }

    public int getGolsSofridos() {
        return golsSofridos;
    }

    public void setGolsSofridos(int golsSofridos) {
        this.golsSofridos = golsSofridos;
    }

    public int getAssistencias() {
        return assistencias;
    }

    public void setAssistencias(int assistencias) {
        this.assistencias = assistencias;
    }

    public int getDefesas() {
        return defesas;
    }

    public void setDefesas(int defesas) {
        this.defesas = defesas;
    }

    public int getFinalTotal() {
        return finalTotal;
    }

    public void setFinalTotal(int finalTotal) {
        this.finalTotal = finalTotal;
    }

    public int getFinalErradas() {
        return finalErradas;
    }

    public void setFinalErradas(int finalErradas) {
        this.finalErradas = finalErradas;
    }

    public int getFinalCertas() {
        return finalCertas;
    }

    public void setFinalCertas(int finalCertas) {
        this.finalCertas = finalCertas;
    }

    public int getFinalTrave() {
        return finalTrave;
    }

    public void setFinalTrave(int finalTrave) {
        this.finalTrave = finalTrave;
    }

    public int getCartoesAma() {
        return cartoesAma;
    }

    public void setCartoesAma(int cartoesAma) {
        this.cartoesAma = cartoesAma;
    }

    public int getCartoesVerm() {
        return cartoesVerm;
    }

    public void setCartoesVerm(int cartoesVerm) {
        this.cartoesVerm = cartoesVerm;
    }

    public int getFaltasRec() {
        return faltasRec;
    }

    public void setFaltasRec(int faltasRec) {
        this.faltasRec = faltasRec;
    }

    public int getFaltasCom() {
        return faltasCom;
    }

    public void setFaltasCom(int faltasCom) {
        this.faltasCom = faltasCom;
    }

    public int getQtdPartidas() {
        return qtdPartidas;
    }

    public void setQtdPartidas(int qtdPartidas) {
        this.qtdPartidas = qtdPartidas;
    }

    public int getNotaInd() {
        return notaInd;
    }

    public void setNotaInd(int notaInd) {
        this.notaInd = notaInd;
    }

    public int getNotaPart() {
        return notaPart;
    }

    public void setNotaPart(int notaPart) {
        this.notaPart = notaPart;
    }

    public int getDuracao() {
        return duracao;
    }

    public void setDuracao(int duracao) {
        this.duracao = duracao;
    }

    public int getPosicao_num() {
        return posicao_num;
    }

    public void setPosicao_num(int posicao_num) {
        this.posicao_num = posicao_num;
    }

    public int getPosicaosec_num() {
        return posicaosec_num;
    }

    public void setPosicaosec_num(int posicaosec_num) {
        this.posicaosec_num = posicaosec_num;
    }

    public String getPosicao_nome() {
        return posicao_nome;
    }

    public void setPosicao_nome(String posicao_nome) {
        this.posicao_nome = posicao_nome;
    }

    public String getPosicao_sigla() {
        return posicao_sigla;
    }

    public void setPosicao_sigla(String posicao_sigla) {
        this.posicao_sigla = posicao_sigla;
    }

    public String getPosicaosec_nome() {
        return posicaosec_nome;
    }

    public void setPosicaosec_nome(String posicaosec_nome) {
        this.posicaosec_nome = posicaosec_nome;
    }

    public String getPosicaosec_sigla() {
        return posicaosec_sigla;
    }

    public void setPosicaosec_sigla(String posicaosec_sigla) {
        this.posicaosec_sigla = posicaosec_sigla;
    }

    public String getNomeLocal() {
        return nomeLocal;
    }

    public void setNomeLocal(String nomeLocal) {
        this.nomeLocal = nomeLocal;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTipoRegistro() {
        return tipoRegistro;
    }

    public void setTipoRegistro(String tipoRegistro) {
        this.tipoRegistro = tipoRegistro;
    }

    public String getTime_id_firebase() {
        return time_id_firebase;
    }

    public void setTime_id_firebase(String time_id_firebase) {
        this.time_id_firebase = time_id_firebase;
    }

    public String getTime_adv_id_firebase() {
        return time_adv_id_firebase;
    }

    public void setTime_adv_id_firebase(String time_adv_id_firebase) {
        this.time_adv_id_firebase = time_adv_id_firebase;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getPenaltiAc() {
        return penaltiAc;
    }

    public void setPenaltiAc(int penaltiAc) {
        this.penaltiAc = penaltiAc;
    }

    public int getPenaltiEr() {
        return penaltiEr;
    }

    public void setPenaltiEr(int penaltiEr) {
        this.penaltiEr = penaltiEr;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(golsFeitos);
        dest.writeInt(golsPD);
        dest.writeInt(golsPE);
        dest.writeInt(golsCA);
        dest.writeInt(golsOU);
        dest.writeInt(golsSofridos);
        dest.writeInt(golsSofridosPen);
        dest.writeInt(assistencias);
        dest.writeInt(defesas);
        dest.writeInt(finalTotal);
        dest.writeInt(finalErradas);
        dest.writeInt(finalCertas);
        dest.writeInt(finalTrave);
        dest.writeInt(finalBloq);
        dest.writeInt(cartoesAma);
        dest.writeInt(cartoesVerm);
        dest.writeInt(cartoesAzul);
        dest.writeInt(faltasRec);
        dest.writeInt(faltasCom);
        dest.writeInt(qtdPartidas);
        dest.writeInt(notaInd);
        dest.writeInt(notaPart);
        dest.writeInt(duracao);
        dest.writeInt(desarmes);
        dest.writeInt(cortes);
        dest.writeInt(bloqueios);
        dest.writeInt(erros);
        dest.writeInt(penaltiAc);
        dest.writeInt(penaltiEr);
        dest.writeInt(posicao_num);
        dest.writeInt(posicaosec_num);
        dest.writeInt(placarCasa);
        dest.writeInt(placarFora);
        dest.writeInt(vitorias);
        dest.writeInt(empates);
        dest.writeInt(derrotas);
        dest.writeInt(defesasPen);
        dest.writeString(nomeLocal);
        dest.writeString(endereco);
        dest.writeString(tipoRegistro);
        dest.writeString(firebase_id);
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeString(time_id_firebase);
        dest.writeString(time_adv_id_firebase);
        dest.writeString(usuario_id_firebase);
        dest.writeString(data);
        dest.writeString(posicao_nome);
        dest.writeString(posicao_sigla);
        dest.writeString(posicaosec_nome);
        dest.writeString(posicaosec_sigla);
    }

    public String keyToString(Activity activity, Usuario usuario) throws Exception {
        String placar = "", sTime = "", sTimeAdv;
        if (getTipoRegistro().equals("Dia de Jogos"))
            placar = vitorias + "V " + empates + "E " + derrotas + "D";
        else if (getTipoRegistro().equals("Patida Única")) {
            placar = placarCasa + "x" + placarFora;
            if (getTime_id_firebase() != null && !getTime_id_firebase().isEmpty()) {
                sTime = ControleBanco.getInstance().recuperaTimePorId(activity, usuario, getTime_id_firebase()).getSigla();
                placar = sTime + " " + placar;
                if (getTime_adv_id_firebase() != null && !getTime_adv_id_firebase().isEmpty()) {
                    sTimeAdv = ControleBanco.getInstance().recuperaTimePorId(activity, usuario, getTime_adv_id_firebase()).getSigla();
                    placar = placar + " " + sTimeAdv;
                }
            }
        }
        String chave = nomeLocal +
                " - " + endereco +
                " - " + tipoRegistro +
                " - " + posicao_nome +
                " - " + posicao_sigla +
                " - " + posicaosec_nome +
                " - " + placar +
                " - " + duracao + "min " +
                " - " + (golsFeitos != 0 ? "gols" : "") +
                " - " + (penaltiAc != 0 ? "penalti" : "") +
                " - " + (penaltiEr != 0 ? "penalti" : "") +
                " - " + (assistencias != 0 ? "assistencias" : "") +
                " - " + (desarmes != 0 ? "desarmes" : "") +
                " - " + (cortes != 0 ? "cortes" : "") +
                " - " + (bloqueios != 0 ? "bloqueios" : "") +
                " - " + (defesas != 0 ? "defesas" : "") +
                " - " + (defesasPen != 0 ? "defesas de penalti" : "") +
                " - " + (tipoRegistro.equals("Dia de Jogos") ? (vitorias > derrotas ? "vitorias" : "") : "") +
                " - " + (tipoRegistro.equals("Dia de Jogos") ? (vitorias < derrotas ? "derrotas" : "") : "") +
                " - " + (tipoRegistro.equals("Dia de Jogos") ? (vitorias == derrotas ? "empates" : "") : "") +
                " - " + (tipoRegistro.equals("Partida Única") ? (placarCasa > placarFora ? "vitorias" : "") : "") +
                " - " + (tipoRegistro.equals("Partida Única") ? (placarCasa < placarFora ? "derrotas" : "") : "") +
                " - " + (tipoRegistro.equals("Partida Única") ? (placarCasa == placarFora ? "empates" : "") : "") +
                " - " + data;
        return chave;
    }
}
