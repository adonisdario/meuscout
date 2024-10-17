package tcc.meuscout.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

import tcc.meuscout.R;
import tcc.meuscout.database.ControleBanco;
import tcc.meuscout.model.Time;
import tcc.meuscout.model.Usuario;
import tcc.meuscout.util.Alerta;
import tcc.meuscout.util.Base64Custom;
import tcc.meuscout.util.Constantes;

public class TimeFragment extends Fragment {

    private static TimeFragment instance;
    private TextInputLayout edtNome, edtSigla;
    private String operacao;
    private Time time;
    private boolean adversario;
    private Usuario usuario;
    private View view;
    private Calendar mCurrentDate;
    private String dataAtual;
    private int mDia, mMes, mAno, mHora, mMinuto, mSegundo;

    public static TimeFragment newInstance() {
        instance = new TimeFragment();
        return instance;
    }

    public static TimeFragment getInstance() {
        if (instance == null)
            instance = new TimeFragment();
        return instance;
    }

    private void getDataCorrente() {
        mCurrentDate = Calendar.getInstance();
        mDia = mCurrentDate.get(Calendar.DAY_OF_MONTH);
        mMes = mCurrentDate.get(Calendar.MONTH);
        mAno = mCurrentDate.get(Calendar.YEAR);
        mHora = mCurrentDate.get(Calendar.HOUR_OF_DAY);
        mMinuto = mCurrentDate.get(Calendar.MINUTE);
        mSegundo = mCurrentDate.get(Calendar.SECOND);
        //Preencher os componentes qdo for um novo apontamento
        dataAtual = (
                (mAno) + "/" +
                ((mMes + 1) < 10 ? "0" + (mMes + 1) : (mMes + 1)) + "/" +
                (mDia < 10 ? "0" + mDia : mDia) +
                mHora + ":" + mMinuto + ":" + mSegundo
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_time, container, false);
        setHasOptionsMenu(true);
        iniciarComponente(view);
        getDataCorrente();
        try {
            usuario = ControleBanco.getInstance().recuperaUsuarioLogado(getActivity());
            Intent intent = getActivity().getIntent();
            if (intent != null) {
                if (intent.hasExtra(Constantes.PARCELABLE_OBJ))
                    time = (Time) intent.getSerializableExtra(Constantes.PARCELABLE_OBJ);

                if (intent.hasExtra(Constantes.OPERACAO))
                    operacao = intent.getStringExtra(Constantes.OPERACAO);

                if (intent.hasExtra(Constantes.ADVERSARIO))
                    adversario = intent.getBooleanExtra(Constantes.ADVERSARIO, false);
            }

            if (operacao != null)
                if (operacao.equals(Constantes.EDITAR)) {
                    time = TimeListFragment.getInstance().retornaTime();
                    carregarDados();
                } else {
                    time = new Time();
                }

        } catch (Exception e) {
            Alerta.exibeSnackbarCurto(view, Constantes.MSG_GENERICA_ERRO);
            e.printStackTrace();
        }

        return view;
    }

    private void carregarDados() {
        if (time != null) {
            edtNome.getEditText().setText(time.getNome());
            edtSigla.getEditText().setText(time.getSigla());
        }
    }

    private void executar() {
        String nome = edtNome.getEditText().getText().toString().trim();
        String sigla = edtSigla.getEditText().getText().toString().trim();
        time.setNome(nome);
        time.setSigla(sigla);
        time.setTime_usuario("N");
        if (operacao.equals(Constantes.INSERIR)) {
            time.setUsuario_id_firebase(usuario.getFirebase_id());
            time.cadastrarTimeFirebase(usuario, getActivity(), dataAtual);
        } else {
            time.atualizarTimeFirebase(getActivity());
        }
    }

    public void retornarTela(String operacao) {
        Toast.makeText(getContext(), "Time cadastrado!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        if (operacao.equals(Constantes.EDITAR))
            TimeListFragment.getInstance().carregaListaTimes(usuario, getActivity());
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    private boolean validarDados(View v) {
        int cont = 0;

        if (validarSigla(v)) cont++;
        if (validarTime(v)) cont++;

        return cont == 2;
    }

    private boolean validarSigla(View v) {
        if (edtSigla.getEditText().getText().toString().length() < 2) {
            Snackbar.make(v, "Sigla do time muito pequena.", Snackbar.LENGTH_SHORT).show();
            edtSigla.setError("Mínimo 2");
            return false;
        }
        edtSigla.setError(null);
        return true;
    }

    private boolean validarTime(View v) {
        if (edtNome.getEditText().getText().toString().length() < 2) {
            Snackbar.make(v, "Nome do time muito pequeno.", Snackbar.LENGTH_SHORT).show();
            edtNome.setError("Mínimo 3");
            return false;
        }
        edtNome.setError(null);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                break;
            case R.id.cadastrar:
                if (validarDados(view))
                    executar();
                break;
            default:
                break;
        }
        return true;
    }

    private void iniciarComponente(View v) {
        edtNome = v.findViewById(R.id.edt_time_nomeTime);
        edtSigla = v.findViewById(R.id.edt_time_sigla);
    }
}