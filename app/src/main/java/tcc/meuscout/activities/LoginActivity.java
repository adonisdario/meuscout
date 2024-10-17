package tcc.meuscout.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import tcc.meuscout.R;
import tcc.meuscout.database.ControleBanco;
import tcc.meuscout.firebase.ConfiguracaoFirebase;
import tcc.meuscout.model.Usuario;
import tcc.meuscout.util.Alerta;
import tcc.meuscout.util.Base64Custom;
import tcc.meuscout.util.Constantes;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao;
    private EditText login, senha;
    private Button entrar;
    private Usuario usuario;
    private ProgressBar mProgressBar;
    private ConstraintLayout layout;
    private final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        inicializarComponentes();
        try {
            entrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String user = login.getText().toString();
                    String password = senha.getText().toString();
                    if (user.equals("") || user.isEmpty()) { //O user não pode ser vazio ou nulo
                        Snackbar.make(view, "Digite seu login!", Snackbar.LENGTH_SHORT).show();

                    } else if (password.equals("") || password.isEmpty()) { //A senha não pode ser vazia ou nula
                        Snackbar.make(view, "Digite sua senha!", Snackbar.LENGTH_SHORT).show();

                    } else {
                        exibirProgress(true);
                        user = user.trim();
                        usuario = new Usuario(user, password);
                        usuario.setLogado("S");
                        realizaLogin(usuario.getEmail(), usuario.getSenha()); //Chama o método para fazer o login
                    }
                }
            });
        } catch (Exception e) {
            trataExcecao(e);
        }
    }

    private void realizaLogin(String email, String senha) {
        try {
            autenticacao = ConfiguracaoFirebase.getAutenticacao(); //Chama o método que retorna o atributo estático para a autenticação do Firebase
            autenticacao.signInWithEmailAndPassword(email, senha) //Faz o login no Firebase a partir do email e senha passados
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            exibirProgress(false);
                            if (task.isSuccessful()) {
                                Log.i("CreateUser", "Login bem sucedido");
                                try {
                                    recuperaUsuarioLogado();
                                } catch (Exception e) {
                                    trataExcecao(e);
                                }


                            } else { //Login nao foi bem sucedido
                                try {
                                    throw task.getException();
                                } catch (FirebaseAuthInvalidCredentialsException e) {
                                    Alerta.exibeSnackbarCurto(layout, "Credenciais inválidas!");
                                } catch (FirebaseAuthInvalidUserException e) {
                                    Alerta.exibeSnackbarCurto(layout, "Email não cadastrado!");
                                } catch (Exception e) {
                                    trataExcecao(e);
                                }
                                Log.i("CreateUser", "Erro: Login não realizado.");

                            }
                        }
                    });
        } catch (Exception e) {
            trataExcecao(e);
        }
    }

    private void exibirProgress(boolean exibir) {
        mProgressBar.setVisibility(exibir ? View.VISIBLE : View.GONE);
        entrar.setVisibility(exibir ? View.INVISIBLE : View.VISIBLE);
    }

    private void recuperaUsuarioLogado() throws Exception {
        Usuario usu = ControleBanco.getInstance().recuperaUsuarioLogado(this);
        if (usu.getLogado() == null)
            recuperaUsuarioFirebase();
        else {
            Toast.makeText(LoginActivity.this, "Login bem sucedido", Toast.LENGTH_SHORT).show();
            finish();
            Intent intent = new Intent(LoginActivity.this, MenuDrawerActivity.class);
            startActivity(intent);
        }
    }

    private void recuperaUsuarioFirebase() {
        String idUsuario = Base64Custom.codificar(usuario.getEmail());
        DatabaseReference usuariosFB = reference.child("usuarios").child(idUsuario).child("usuario");
        usuariosFB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    String nome, email, cidade, perna, dtNascimento,
                            posicao, sPosicao, time, sTime, timeAdv, numPos;
                    nome = snapshot.child("nome").getValue().toString();
                    usuario.setNome(nome);
                    cidade = snapshot.child("cidade").getValue().toString();
                    usuario.setCidade(cidade);
                    email = snapshot.child("email").getValue().toString();
                    usuario.setEmail(email);
                    perna = snapshot.child("perna").getValue().toString();
                    usuario.setPerna(perna);
                    dtNascimento = snapshot.child("data_nascimento").getValue().toString();
                    usuario.setData_nascimento(dtNascimento);
                    posicao = (snapshot.child("posicao_nome").getValue().toString());
                    usuario.setPosicao_nome(posicao);
                    sPosicao = (snapshot.child("posicao_sigla").getValue().toString());
                    usuario.setPosicao_sigla(sPosicao);
                    numPos = snapshot.child("posicao_num").getValue().toString();
                    usuario.setPosicao_num(Integer.parseInt(numPos));
                    if (snapshot.child("time_nome").getValue() != null) {
                        time = snapshot.child("time_nome").getValue().toString();
                        usuario.setTime_nome(time);
                        sTime = snapshot.child("time_sigla").getValue().toString();
                        usuario.setTime_sigla(sTime);
                    }
                    // Campos que não são enviados para o firebase, e portanto, não é possível recuperá-los
                    usuario.setLogado("S");
                    usuario.setFirebase_id(Base64Custom.codificar(usuario.getEmail()));
                    int a = ControleBanco.getInstance().atualizarUsuario(LoginActivity.this, usuario);
                    if (a == 0) {
                        int b = ControleBanco.getInstance().inserirUsuario(LoginActivity.this, usuario);
                        if (b == 0) {
                            View view = findViewById(android.R.id.content);
                            Alerta.exibeSnackbarCurto(view, Constantes.MSG_GENERICA_ERRO);
                            return;
                        }
                    }
                    Toast.makeText(LoginActivity.this, "Login bem sucedido", Toast.LENGTH_SHORT).show();
                    finish();
                    Intent intent = new Intent(LoginActivity.this, MenuDrawerActivity.class);
                    startActivity(intent);

                } catch (Exception e) {
                    trataExcecao(e);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void inicializarComponentes() {
        entrar = findViewById(R.id.loginAct_btnEntrar);
        login = findViewById(R.id.loginAct_edtLogin);
        senha = findViewById(R.id.loginAct_edtSenha);
        mProgressBar = findViewById(R.id.progressBar);
        layout = findViewById(R.id.layout_login);
    }

    private void trataExcecao(Exception e) {
        e.printStackTrace();
        View view = findViewById(android.R.id.content);
        Alerta.exibeSnackbarCurto(view, Constantes.MSG_GENERICA_ERRO);
    }
}