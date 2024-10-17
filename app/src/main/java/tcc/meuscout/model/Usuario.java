package tcc.meuscout.model;

import android.view.View;

import androidx.annotation.NonNull;

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
import tcc.meuscout.util.Alerta;
import tcc.meuscout.util.Constantes;

public class Usuario implements Serializable {
    private String nome, email, senha, firebase_id, cidade, perna;
    private String posicao_nome, posicao_sigla, time_nome, time_sigla;
    private int posicao_num;
    private String data_nascimento, logado, time_id_firebase;
    private Integer id;
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    public Usuario() {

    }

    public Usuario(String email, String senha) {
        this.email = email;
        this.senha = senha;
    }


    public void cadastrarUsuarioFirebase() { // Este método salva o objeto usuário no BD Firebase
        DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseDatabase();
        firebase.child("usuarios").child(this.firebase_id).child("usuario").setValue(this);
    }

    public void atualizarUsuarioFirebase(CadastroActivity activity) {
        DatabaseReference usuarioFB = reference.child("usuarios").child(getFirebase_id()).child("usuario");
        usuarioFB.child("cidade").setValue(getCidade());
        usuarioFB.child("data_nascimento").setValue(getData_nascimento());
        usuarioFB.child("email").setValue(getEmail());
        usuarioFB.child("nome").setValue(getNome());
        usuarioFB.child("perna").setValue(getPerna());
        usuarioFB.child("posicao_nome").setValue(getPosicao_nome());
        usuarioFB.child("posicao_num").setValue(getPosicao_num());
        usuarioFB.child("posicao_sigla").setValue(getPosicao_sigla());
        usuarioFB.child("time_nome").setValue(getTime_nome());
        usuarioFB.child("time_id_firebase").setValue(getTime_id_firebase());
        DatabaseReference ref = usuarioFB.child("time_sigla");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    activity.atualizarInfosFirebase();
                    ref.removeEventListener(this);
                } catch (Exception e) {
                    e.printStackTrace();
                    View view = activity.findViewById(android.R.id.content);
                    Alerta.exibeSnackbarCurto(view, Constantes.MSG_GENERICA_ERRO);
                }
                ref.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ref.setValue(getTime_sigla());
    }

    public Usuario cadastrarUsuarioBanco(CadastroActivity activity) throws Exception {
        this.setId(ControleBanco.getInstance().inserirUsuario(activity, this));
        return this;
    }

    public int atualizarUsuarioBanco(CadastroActivity activity) throws Exception {
        return ControleBanco.getInstance().atualizarUsuario(activity, this);
    }

    public String getTime_id_firebase() {
        return time_id_firebase;
    }

    public void setTime_id_firebase(String time_id_firebase) {
        this.time_id_firebase = time_id_firebase;
    }

    @Exclude
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPosicao_nome() {
        return posicao_nome;
    }

    public void setPosicao_nome(String posicao_nome) {
        this.posicao_nome = posicao_nome;
    }

    public String getTime_nome() {
        return time_nome;
    }

    public void setTime_nome(String time_nome) {
        this.time_nome = time_nome;
    }

    @Exclude
    public String getFirebase_id() {
        return firebase_id;
    }

    public void setFirebase_id(String firebase_id) {
        this.firebase_id = firebase_id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getPerna() {
        return perna;
    }

    public void setPerna(String perna) {
        this.perna = perna;
    }

    public String getData_nascimento() {
        return data_nascimento;
    }

    public void setData_nascimento(String data_nascimento) {
        this.data_nascimento = data_nascimento;
    }

    @Exclude
    public String getLogado() {
        return logado;
    }

    public void setLogado(String logado) {
        this.logado = logado;
    }

    public String getPosicao_sigla() {
        return posicao_sigla;
    }

    public void setPosicao_sigla(String posicao_sigla) {
        this.posicao_sigla = posicao_sigla;
    }

    public String getTime_sigla() {
        return time_sigla;
    }

    public void setTime_sigla(String time_sigla) {
        this.time_sigla = time_sigla;
    }

    public int getPosicao_num() {
        return posicao_num;
    }

    public void setPosicao_num(int posicao_num) {
        this.posicao_num = posicao_num;
    }
}
