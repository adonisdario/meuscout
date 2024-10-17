package tcc.meuscout.model;

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

import tcc.meuscout.activities.CadastroActivity;
import tcc.meuscout.database.ControleBanco;
import tcc.meuscout.firebase.ConfiguracaoFirebase;
import tcc.meuscout.fragments.EnderecoFragment;
import tcc.meuscout.fragments.TimeFragment;
import tcc.meuscout.util.Alerta;
import tcc.meuscout.util.Base64Custom;
import tcc.meuscout.util.Constantes;

public class Endereco implements Serializable, Parcelable {
    private String nomelocal, endereco;
    private Integer id;
    private String firebase_id, usuario_id_firebase;
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private static final String tabelaFirebase = "enderecos", tabelaUsuarios = "usuarios";

    public Endereco(String nomelocal, String endereco) {
        this.nomelocal = nomelocal;
        this.endereco = endereco;
    }

    public Endereco() {

    }

    protected Endereco(Parcel in) {
        nomelocal = in.readString();
        endereco = in.readString();
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        firebase_id = in.readString();
        usuario_id_firebase = in.readString();
    }

    public static final Creator<Endereco> CREATOR = new Creator<Endereco>() {
        @Override
        public Endereco createFromParcel(Parcel in) {
            return new Endereco(in);
        }

        @Override
        public Endereco[] newArray(int size) {
            return new Endereco[size];
        }
    };

    public void cadastrarEnderecoFirebase(Usuario usuario, Activity activity, String dataAtual) { // Este método salva o objeto time no BD Firebase
        this.setFirebase_id(Base64Custom.codificar(dataAtual));
        DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseDatabase()
                .child("usuarios").child(usuario.getFirebase_id()).child(tabelaFirebase).child(this.firebase_id);
        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    cadastrarEnderecoBanco(activity);
                    EnderecoFragment.getInstance().retornarTela(Constantes.INSERIR);
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

    public void atualizarEnderecoFirebase(FragmentActivity activity) {
        DatabaseReference usuarioFB = reference.child("usuarios").child(this.getUsuario_id_firebase())
                .child(tabelaFirebase).child(this.getFirebase_id());
        usuarioFB.child("nomelocal").setValue(getNomelocal());
        usuarioFB.child("endereco").setValue(getEndereco());
        DatabaseReference ref = usuarioFB.child("endereco");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    atualizarEnderecoBancoVoltar(activity);
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
        ref.setValue(getEndereco());
    }

    public void removerTimeFirebase(Activity activity) {
        DatabaseReference timeFB = reference.child("usuarios").child(this.getUsuario_id_firebase())
                .child(tabelaFirebase).child(this.getFirebase_id());
        timeFB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    removerEnderecoBanco(activity);
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

    public Endereco cadastrarEnderecoBanco(Activity activity) throws Exception {
        this.setId(ControleBanco.getInstance().inserirEndereco(activity, this));
        return this;
    }

    public Endereco cadastrarEnderecoBanco(FragmentActivity activity) throws Exception {
        this.setId(ControleBanco.getInstance().inserirEndereco(activity, this));
        return this;
    }

    public int atualizarEnderecoBanco(CadastroActivity activity) throws Exception {
        return ControleBanco.getInstance().atualizarEndereco(activity, this);
    }

    public int atualizarEnderecoBancoVoltar(FragmentActivity activity) throws Exception {
        int a = ControleBanco.getInstance().atualizarEndereco(activity, this);
        EnderecoFragment.getInstance().retornarTela(Constantes.EDITAR);
        return a;
    }

    public int atualizarEnderecoBanco(FragmentActivity activity) throws Exception {
        int a = ControleBanco.getInstance().atualizarEndereco(activity, this);
        return a;
    }

    public int atualizarEnderecoBanco(Activity activity) throws Exception {
        int a = ControleBanco.getInstance().atualizarEndereco(activity, this);
        return a;
    }

    public int removerEnderecoBanco(Activity activity) throws Exception {
        return ControleBanco.getInstance().removerEndereco(activity, this);
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

    public String getNomelocal() {
        return nomelocal;
    }

    public void setNomelocal(String nomelocal) {
        this.nomelocal = nomelocal;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    @Exclude
    public String getFirebase_id() {
        return firebase_id;
    }

    public void setFirebase_id(String firebase_id) {
        this.firebase_id = firebase_id;
    }

    @Override
    public String toString() {
        return nomelocal + " - " + endereco;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nomelocal);
        dest.writeString(endereco);
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
