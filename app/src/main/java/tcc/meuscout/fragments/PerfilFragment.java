package tcc.meuscout.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;

import tcc.meuscout.R;
import tcc.meuscout.activities.CadastroActivity;
import tcc.meuscout.database.ControleBanco;
import tcc.meuscout.model.Partida;
import tcc.meuscout.model.Usuario;
import tcc.meuscout.util.Alerta;
import tcc.meuscout.util.Constantes;
import tcc.meuscout.util.Conversao;

public class PerfilFragment extends Fragment {

    private final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private Usuario usuario;
    private ProgressBar progressBar;
    private TextView txtNome, txtEmail, txtCidade, txtPerna, txtDtNascimento,
            txtPosicao, txtTime, txtGols, txtAssists, txtPenCobrados;
    private LinearLayout layoutInfos, layoutAtaque;
    private ImageView imagem;
    private String TAG = "PerfilFragment";
    private static PerfilFragment instance;
    private View view1;

    public static PerfilFragment newInstance() {
        instance = new PerfilFragment();
        return instance;
    }

    public static PerfilFragment getInstance() {
        if (instance == null)
            instance = new PerfilFragment();
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);
        view1 = container.getRootView();
        inicializarComponentes(view);
        setHasOptionsMenu(true);
        carregaDadosPerfil();
        return view;
    }

    private void carregaDadosPerfil() {
        try {
            usuario = ControleBanco.getInstance().recuperaUsuarioLogado(getActivity());
            DatabaseReference usuariosFB = reference.child("usuarios").child(usuario.getFirebase_id()).child("usuario");
            usuariosFB.addValueEventListener(new ValueEventListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String nome, email, cidade, perna, dtNascimento,
                            posicao, sPosicao, time, sTime, numPos;
                    try {
                        nome = snapshot.child("nome").getValue().toString();
                        txtNome.setText(nome);
                        usuario.setNome(nome);
                        cidade = snapshot.child("cidade").getValue().toString();
                        txtCidade.setText(cidade);
                        usuario.setCidade(cidade);
                        email = snapshot.child("email").getValue().toString();
                        txtEmail.setText(email);
                        usuario.setEmail(email);
                        perna = snapshot.child("perna").getValue().toString();
                        if (perna.equals("Ambidestro"))
                            txtPerna.setText(perna);
                        else
                            txtPerna.setText("Perna " + perna);
                        usuario.setPerna(perna);
                        dtNascimento = snapshot.child("data_nascimento").getValue().toString();
                        Calendar currentDate = Calendar.getInstance();
                        Date date = currentDate.getTime();
                        Date nasc = Conversao.StringParaData(dtNascimento, "dd/MM/yyyy");
                        int idade = date.getYear() - nasc.getYear();
                        nasc.setYear(date.getYear());
                        if (date.before(nasc))
                            idade--;
                        txtDtNascimento.setText(dtNascimento + " (" + idade + " anos)");
                        usuario.setData_nascimento(dtNascimento);
                        posicao = (snapshot.child("posicao_nome").getValue().toString());
                        usuario.setPosicao_nome(posicao);
                        sPosicao = (snapshot.child("posicao_sigla").getValue().toString());
                        usuario.setPosicao_sigla(sPosicao);
                        numPos = snapshot.child("posicao_num").getValue().toString();
                        usuario.setPosicao_num(Integer.parseInt(numPos));
                        txtPosicao.setText(posicao + " - " + sPosicao);
                        txtPosicao.setBackgroundResource(defineCorPosicao(numPos));
                        if (snapshot.child("time_nome").getValue() != null) {
                            time = snapshot.child("time_nome").getValue().toString();
                            usuario.setTime_nome(time);
                            sTime = snapshot.child("time_sigla").getValue().toString();
                            usuario.setTime_sigla(sTime);
                            txtTime.setText(time + " - " + sTime);
                            usuario.setTime_id_firebase(usuario.getFirebase_id());
                        } else {
                            txtTime.setText(" - ");
                        }
                        carregaNumeros();
                        infoNaFrente(true);
                        usuariosFB.removeEventListener(this);
                    } catch (Exception e) {
                        progressBar.setVisibility(View.GONE);
                        Alerta.exibeSnackbarCurto(getView(), Constantes.MSG_GENERICA_ERRO);
                        e.printStackTrace();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } catch (Exception e) {
            Alerta.exibeSnackbarCurto(view1, Constantes.MSG_GENERICA_ERRO);
            e.printStackTrace();
        }
    }

    private void carregaNumeros() throws Exception {
        Partida partida = ControleBanco.getInstance().recuperaPartidaSomatorio(getActivity(), usuario);
        if (partida != null) {
            txtGols.setText(partida.getGolsFeitos() + "");
            txtAssists.setText(partida.getAssistencias() + "");
            txtPenCobrados.setText((partida.getPenaltiAc() + partida.getPenaltiEr()) + "");
            layoutAtaque.setVisibility(View.VISIBLE);
        }
    }

    private int defineCorPosicao(String pos) {
        switch (pos) {
            case "1":
                return R.color.Cor_Goleiro;
            case "2":
                return R.color.Cor_Defensor;
            case "3":
                return R.color.Cor_Meia;
            default:
                return R.color.Cor_Atacante;
        }
    }


    private void inicializarComponentes(View view) {
        txtNome = view.findViewById(R.id.txt_perfil_nome);
        txtCidade = view.findViewById(R.id.txt_perfil_cidade);
        txtEmail = view.findViewById(R.id.txt_perfil_email);
        txtPosicao = view.findViewById(R.id.txt_perfil_posicao);
        txtTime = view.findViewById(R.id.txt_perfil_time);
        txtDtNascimento = view.findViewById(R.id.txt_perfil_idade);
        txtPerna = view.findViewById(R.id.txt_perfil_perna);
        layoutInfos = view.findViewById(R.id.layout_perfil_infos);
        imagem = view.findViewById(R.id.img_perfil);
        progressBar = view.findViewById(R.id.progress_perfil);
        txtGols = view.findViewById(R.id.txt_perfil_gols);
        txtAssists = view.findViewById(R.id.txt_perfil_assists);
        txtPenCobrados = view.findViewById(R.id.txt_perfil_penCobrados);
        layoutAtaque = view.findViewById(R.id.layout_perfil_ataque);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        //if (operacao.equalsIgnoreCase(ConstantesFrota._VISUALIZAR))
        //menu.findItem(R.id.cadastrar).setVisible(false);
        inflater.inflate(R.menu.orz_menu_actionbar_editar, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.editar:
                Intent it = new Intent(getContext(), CadastroActivity.class);
                it.putExtra(Constantes.OPERACAO, Constantes.EDITAR);
                startActivityForResult(it, Constantes.REQUESTCODE_100);
                return true;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            if (requestCode == Constantes.REQUESTCODE_100 && resultCode == Activity.RESULT_OK) {
                infoNaFrente(false);
                carregaDadosPerfil();
            }
        } catch (Exception e) {
            progressBar.setVisibility(View.GONE);
            Alerta.exibeSnackbarCurto(getView(), Constantes.MSG_GENERICA_ERRO);
            Alerta.gravarExcecao(TAG, e);
            e.printStackTrace();
        }
    }

    private void infoNaFrente(boolean valor) {
        if (valor) {
            imagem.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            layoutInfos.setVisibility(View.VISIBLE);
        } else {
            imagem.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            layoutInfos.setVisibility(View.GONE);
        }
    }
}