package tcc.meuscout.model;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;

import tcc.meuscout.R;
import tcc.meuscout.activities.CadastroActivity;
import tcc.meuscout.database.ControleBanco;
import tcc.meuscout.firebase.ConfiguracaoFirebase;
import tcc.meuscout.fragments.TimeFragment;
import tcc.meuscout.util.Alerta;
import tcc.meuscout.util.Base64Custom;
import tcc.meuscout.util.Constantes;

public class Time implements Serializable, Parcelable {
    private String nome, sigla;
    private String time_usuario;
    private Integer id;
    private String firebase_id, usuario_id_firebase;
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    public Time(String nome, String sigla, String time_usuario) {
        this.nome = nome;
        this.sigla = sigla;
        this.time_usuario = time_usuario;
    }

    public Time() {

    }

    protected Time(Parcel in) {
        nome = in.readString();
        sigla = in.readString();
        time_usuario = in.readString();
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        firebase_id = in.readString();
        usuario_id_firebase = in.readString();
    }

    public static final Creator<Time> CREATOR = new Creator<Time>() {
        @Override
        public Time createFromParcel(Parcel in) {
            return new Time(in);
        }

        @Override
        public Time[] newArray(int size) {
            return new Time[size];
        }
    };

    public void cadastrarTimeFirebase(Usuario usuario, CadastroActivity activity, String dataAtual) { // Este método salva o objeto time no BD Firebase
        this.setFirebase_id(Base64Custom.codificar(dataAtual));
        DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseDatabase()
                .child("usuarios").child(usuario.getFirebase_id()).child("times").child(this.firebase_id);
        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    cadastrarTimeBanco(activity);
                    activity.entrarNoApp();
                    firebase.removeEventListener(this);
                } catch (Exception e) {
                    e.printStackTrace();
                    View view = activity.findViewById(android.R.id.content);
                    Alerta.exibeSnackbarCurto(view, Constantes.MSG_GENERICA_ERRO);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        firebase.setValue(this);
    }


    public void cadastrarTimeFirebase(Usuario usuario, Activity activity, String dataAtual) { // Este método salva o objeto time no BD Firebase
        this.setFirebase_id(Base64Custom.codificar(dataAtual));
        DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseDatabase()
                .child("usuarios").child(usuario.getFirebase_id()).child("times").child(this.firebase_id);
        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    cadastrarTimeBanco(activity);
                    TimeFragment.getInstance().retornarTela(Constantes.INSERIR);
                    firebase.removeEventListener(this);
                } catch (Exception e) {
                    e.printStackTrace();
                    View view = activity.findViewById(android.R.id.content);
                    Alerta.exibeSnackbarCurto(view, Constantes.MSG_GENERICA_ERRO);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        firebase.setValue(this);
    }

    public void atualizarTimeFirebase(CadastroActivity activity) {
        DatabaseReference usuarioFB = reference.child("usuarios").child(this.getUsuario_id_firebase())
                .child("times").child(this.getFirebase_id());
        usuarioFB.child("nome").setValue(getNome());
        usuarioFB.child("time_usuario").setValue(getTime_usuario());
        DatabaseReference ref = usuarioFB.child("sigla");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    activity.atualizarInfos2Firebase();
                    ref.removeEventListener(this);
                } catch (Exception e) {
                    e.printStackTrace();
                    View view = activity.findViewById(android.R.id.content);
                    Alerta.exibeSnackbarCurto(view, Constantes.MSG_GENERICA_ERRO);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ref.setValue(getSigla());
    }

    public void atualizarTimeFirebase(FragmentActivity activity) {
        DatabaseReference usuarioFB = reference.child("usuarios").child(this.getUsuario_id_firebase()).child("times")
                .child(this.getFirebase_id());
        usuarioFB.child("nome").setValue(getNome());
        usuarioFB.child("time_usuario").setValue(getTime_usuario());
        DatabaseReference ref = usuarioFB.child("sigla");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    atualizarTimeBancoVoltar(activity);
                    ref.removeEventListener(this);
                } catch (Exception e) {
                    e.printStackTrace();
                    View view = activity.findViewById(android.R.id.content);
                    Alerta.exibeSnackbarCurto(view, Constantes.MSG_GENERICA_ERRO);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ref.setValue(getSigla());
    }

    public void removerTimeFirebase(CadastroActivity activity) {
        DatabaseReference timeFB = reference.child("usuarios").child(this.getUsuario_id_firebase()).child("times")
                .child(this.getFirebase_id());
        timeFB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    activity.atualizarInfos3Firebase();
                    timeFB.removeEventListener(this);

                } catch (Exception e) {
                    e.printStackTrace();
                    View view = activity.findViewById(android.R.id.content);
                    Alerta.exibeSnackbarCurto(view, Constantes.MSG_GENERICA_ERRO);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        timeFB.removeValue();
    }

    public void removerTimeFirebase(Activity activity) {
        DatabaseReference timeFB = reference.child("usuarios").child(this.getUsuario_id_firebase()).child("times")
                .child(this.getFirebase_id());
        timeFB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    removerTimeBanco(activity);
                    timeFB.removeEventListener(this);

                } catch (Exception e) {
                    e.printStackTrace();
                    View view = activity.findViewById(android.R.id.content);
                    Alerta.exibeSnackbarCurto(view, Constantes.MSG_GENERICA_ERRO);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        timeFB.removeValue();
    }

    public Time cadastrarTimeBanco(Activity activity) throws Exception {
        this.setId(ControleBanco.getInstance().inserirTime(activity, this));
        return this;
    }

    public Time cadastrarTimeBanco(FragmentActivity activity) throws Exception {
        this.setId(ControleBanco.getInstance().inserirTime(activity, this));
        return this;
    }

    public int atualizarTimeBanco(CadastroActivity activity) throws Exception {
        return ControleBanco.getInstance().atualizarTime(activity, this);
    }

    public int atualizarTimeBancoVoltar(FragmentActivity activity) throws Exception {
        int a = ControleBanco.getInstance().atualizarTime(activity, this);
        TimeFragment.getInstance().retornarTela(Constantes.EDITAR);
        return a;
    }

    public int atualizarTimeBanco(FragmentActivity activity) throws Exception {
        int a = ControleBanco.getInstance().atualizarTime(activity, this);
        return a;
    }

    public int atualizarTimeBanco(Activity activity) throws Exception {
        int a = ControleBanco.getInstance().atualizarTime(activity, this);
        return a;
    }

    public int removerTimeBanco(Activity activity) throws Exception {
        return ControleBanco.getInstance().removerTime(activity, this);
    }

    @Exclude
    public String getUsuario_id_firebase() {
        return usuario_id_firebase;
    }

    public void setUsuario_id_firebase(String usuario_id_firebase) {
        this.usuario_id_firebase = usuario_id_firebase;
    }

    @Exclude
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    @Exclude
    public String getFirebase_id() {
        return firebase_id;
    }

    public void setFirebase_id(String firebase_id) {
        this.firebase_id = firebase_id;
    }

    public String getTime_usuario() {
        return time_usuario;
    }

    public void setTime_usuario(String time_usuario) {
        this.time_usuario = time_usuario;
    }

    @Override
    public String toString() {
        return nome + " - " + sigla;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nome);
        dest.writeString(sigla);
        dest.writeString(time_usuario);
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeString(firebase_id);
        dest.writeString(usuario_id_firebase);
    }
}
