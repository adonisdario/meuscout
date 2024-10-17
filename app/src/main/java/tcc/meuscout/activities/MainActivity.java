package tcc.meuscout.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;

import tcc.meuscout.R;
import tcc.meuscout.database.ControleBanco;
import tcc.meuscout.firebase.ConfiguracaoFirebase;
import tcc.meuscout.util.Constantes;

public class MainActivity extends IntroActivity {

    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        ControleBanco.getInstance().iniciarBD(getApplicationContext(), this);


        setButtonBackVisible(false);
        setButtonNextVisible(false);

        addSlide(new FragmentSlide.Builder()
                .background(R.color.Cor_Primaria)
                .fragment(R.layout.intro_1)
                .build()
        );

        addSlide(new FragmentSlide.Builder()
                .background(R.color.Cor_Primaria)
                .fragment(R.layout.intro_2)
                .build()
        );

        addSlide(new FragmentSlide.Builder()
                .background(R.color.Cor_Primaria)
                .fragment(R.layout.intro_3)
                .canGoForward(false)
                .canGoBackward(false)
                .build()
        );

    }

    @Override
    protected void onStart() { // No onStart() chama o método para saber se o usuário ainda está conectado
        super.onStart();
        verificarLogin();
    }

    public void btCadastrar(View view) { //Abre a nova Activity para cadastramento
        Intent intent = new Intent(this, CadastroActivity.class);
        intent.putExtra(Constantes.OPERACAO, Constantes.INSERIR);
        startActivity(intent);
    }

    public void btLogin(View view) { //Abre a Activity para o login
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void verificarLogin() { //Verifica se o usuário ainda está logado no Firebase
        autenticacao = ConfiguracaoFirebase.getAutenticacao(); //Chama o método que retorna o atributo estático para a autenticação do Firebase
        //autenticacao.signOut();
        if (autenticacao.getCurrentUser() != null) {
            Log.i("CreateUser", "Usuário logado. Abrindo tela do App");
            Intent intent = new Intent(MainActivity.this, MenuDrawerActivity.class);
            startActivity(intent);
        } else {
            Log.i("CreateUser", "Usuário não logado!");
        }
    }
}