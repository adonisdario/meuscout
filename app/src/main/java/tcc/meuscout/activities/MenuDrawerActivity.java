package tcc.meuscout.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tcc.meuscout.R;
import tcc.meuscout.database.ControleBanco;
import tcc.meuscout.firebase.ConfiguracaoFirebase;
import tcc.meuscout.fragments.PartidaListFragment;
import tcc.meuscout.fragments.PerfilFragment;
import tcc.meuscout.fragments.RelatorioFragment;
import tcc.meuscout.model.Partida;
import tcc.meuscout.model.Time;
import tcc.meuscout.model.Usuario;
import tcc.meuscout.util.Alerta;
import tcc.meuscout.util.Constantes;
import tcc.meuscout.util.Conversao;

public class MenuDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getAutenticacao();
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private Toolbar mToolbar;
    private NavigationView mNavigationView;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private Dialog dialog;
    private Usuario usuario;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            usuario = ControleBanco.getInstance().recuperaUsuarioLogado(MenuDrawerActivity.this);
            setContentView(R.layout.activity_menu_drawer);
            mToolbar = findViewById(R.id.toolbar);
            setSupportActionBar(mToolbar);
            mToolbar.setOverflowIcon(getDrawable(R.drawable.ic_popupmenu_drawer));
            mDrawerLayout = findViewById(R.id.drawer);
            mNavigationView = findViewById(R.id.navigationView);
            mNavigationView.setNavigationItemSelectedListener(this);
            mActionBarDrawerToggle = new ActionBarDrawerToggle(
                    this, mDrawerLayout, mToolbar, R.string.string_open, R.string.string_close);
            mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);
            mActionBarDrawerToggle.setDrawerIndicatorEnabled(true);
            mActionBarDrawerToggle.syncState();
            verificarPermissoes();

            dialog = new Dialog(MenuDrawerActivity.this);
            dialog.setContentView(R.layout.dialog_baixar_dados);
            dialog.setCancelable(false);
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            int width = metrics.widthPixels;
            int height = metrics.heightPixels;
            //dialog.getWindow().setLayout((6 * width)/7, (4 * height)/5);
            //dialog.getWindow().setLayout((6 * width) / 7, height / 4);
            dialog.create();
            dialog.show();
            carregaListaTimes(usuario, MenuDrawerActivity.this);
            //Fragment default
            mFragmentManager = getSupportFragmentManager();
            mFragmentTransaction = mFragmentManager.beginTransaction();
            mFragmentTransaction.replace(R.id.fg1, PartidaListFragment.newInstance(false));
            setTitle(R.string.txt_lista_titulo);
            mFragmentTransaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            Alerta.exibeDialog(this, "Erro", Constantes.MSG_GENERICA_ERRO,
                    R.string.string_ok);
        }
    }

    private void sair() {
        new AlertDialog.Builder(this)
                .setTitle("Deseja sair da sua conta?")
                .setPositiveButton("Sim", (dialog, which) -> {
                    autenticacao.signOut();
                    usuario.setLogado("N");
                    try {
                        ControleBanco.getInstance().atualizarUsuario(MenuDrawerActivity.this, usuario);
                    } catch (Exception e) {
                        View view = findViewById(android.R.id.content);
                        Alerta.exibeSnackbarLongo(view, Constantes.MSG_GENERICA_ERRO);
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(MenuDrawerActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finishAffinity();
                    //super.onDestroy();
                })
                .setNegativeButton("Não", (dialog, which) -> {
                    dialog.dismiss();
                }).show();
    }

    @Override
    public void onBackPressed() {
        String titulo = (String) getTitle();
        if (titulo != null && !titulo.equals("Lista de Partidas")) {
            mFragmentManager = getSupportFragmentManager();
            mFragmentTransaction = mFragmentManager.beginTransaction();
            mFragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
            mFragmentTransaction.replace(R.id.fg1, PartidaListFragment.newInstance(true));
            setTitle(R.string.txt_lista_titulo);
            mFragmentTransaction.commit();
        } else {
            sair();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_perfil:
                mFragmentManager = getSupportFragmentManager();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                mFragmentTransaction.replace(R.id.fg1, PerfilFragment.newInstance());
                setTitle(R.string.txt_perfil_titulo);
                mFragmentTransaction.commit();
                closeDrawer();
                return true;

            case R.id.nav_relatorio:
                mFragmentManager = getSupportFragmentManager();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                mFragmentTransaction.replace(R.id.fg1, RelatorioFragment.newInstance());
                mFragmentTransaction.commit();
                setTitle(R.string.txt_relatorio_titulo);
                closeDrawer();
                return true;

            case R.id.nav_lista:
                mFragmentManager = getSupportFragmentManager();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                mFragmentTransaction.replace(R.id.fg1, PartidaListFragment.newInstance(true));
                mFragmentTransaction.commit();
                setTitle(R.string.txt_lista_titulo);
                closeDrawer();
                return true;

            case R.id.sair:
                sair();
                return true;
            default:
                return false;

        }

    }

    private void closeDrawer() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START))
            mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    /*Verificação das permissões em tempo de execução*/
    private void verificarPermissoes() {
        int Permission_all = 1;
        String[] permissions = {
                Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA, Manifest.permission.READ_PHONE_STATE

        };
        if (!hasPermissions(this, permissions)) {
            ActivityCompat.requestPermissions(this, permissions, Permission_all);
        }

    }

    /*Verificação das permissões em tempo de execução*/
    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED)
                    return false;
            }
        }
        return true;
    }

    private void carregaListaTimes(Usuario usuario, MenuDrawerActivity activity) {
        try {
            View view1 = activity.findViewById(android.R.id.content);
            DatabaseReference usuariosFB = reference.child("usuarios").child(usuario.getFirebase_id())
                    .child("times");
            usuariosFB.addValueEventListener(new ValueEventListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                            Time post = postSnapshot.getValue(Time.class);
                            post.setFirebase_id(postSnapshot.getKey());
                            post.setUsuario_id_firebase(usuario.getFirebase_id());
                            if (post.atualizarTimeBanco(activity) < 1)
                                post.cadastrarTimeBanco(activity);
                        }
                        carregaListaPartida(usuario, activity);
                        usuariosFB.removeEventListener(this);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Alerta.exibeDialog(activity.getApplicationContext(), "Erro", Constantes.MSG_GENERICA_ERRO,
                                R.string.string_ok);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Alerta.exibeSnackbarCurto(view1, Constantes.MSG_ERRO_CONEXAO);
                    if (dialog != null && dialog.isShowing())
                        dialog.dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Alerta.exibeDialog(activity.getApplicationContext(), "Erro", Constantes.MSG_GENERICA_ERRO,
                    R.string.string_ok);
        }
    }

    private void carregaListaPartida(Usuario usuario, MenuDrawerActivity activity) {
        try {
            View view1 = activity.findViewById(android.R.id.content);
            DatabaseReference usuariosFB = reference.child("usuarios").child(usuario.getFirebase_id())
                    .child("partidas");
            usuariosFB.addValueEventListener(new ValueEventListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                            Partida post = postSnapshot.getValue(Partida.class);
                            post.setUsuario_id_firebase(usuario.getFirebase_id());

                            if (post.atualizarPartidaBanco(activity) < 1)
                                post.cadastrarPartidaBanco(activity);

                        }
                        dialog.dismiss();
                        PartidaListFragment.getInstance().atualizaLista();
                        usuariosFB.removeEventListener(this);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Alerta.exibeDialog(activity.getApplicationContext(), "Erro", Constantes.MSG_GENERICA_ERRO,
                                R.string.string_ok);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Alerta.exibeSnackbarCurto(view1, Constantes.MSG_ERRO_CONEXAO);
                    if (dialog != null && dialog.isShowing())
                        dialog.dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Alerta.exibeDialog(activity.getApplicationContext(), "Erro", Constantes.MSG_GENERICA_ERRO,
                    R.string.string_ok);
        }
    }

}
