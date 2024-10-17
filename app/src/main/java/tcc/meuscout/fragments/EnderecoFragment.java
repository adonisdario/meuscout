package tcc.meuscout.fragments;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import tcc.meuscout.R;
import tcc.meuscout.database.ControleBanco;
import tcc.meuscout.model.Endereco;
import tcc.meuscout.model.Usuario;
import tcc.meuscout.util.Alerta;
import tcc.meuscout.util.Constantes;

public class EnderecoFragment extends Fragment {

    private static EnderecoFragment instance;
    private TextInputLayout edtNomeLocal, edtEndereco;
    private String operacao;
    private Endereco endereco;
    private Usuario usuario;
    private View view;
    private Calendar mCurrentDate;
    private String dataAtual;
    private int mDia, mMes, mAno, mHora, mMinuto, mSegundo;

    public static EnderecoFragment newInstance() {
        instance = new EnderecoFragment();
        return instance;
    }

    public static EnderecoFragment getInstance() {
        if (instance == null)
            instance = new EnderecoFragment();
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
        view = inflater.inflate(R.layout.fragment_endereco, container, false);
        setHasOptionsMenu(true);
        iniciarComponente(view);
        getDataCorrente();
        try {
            usuario = ControleBanco.getInstance().recuperaUsuarioLogado(getActivity());
            Intent intent = getActivity().getIntent();
            if (intent != null) {
                if (intent.hasExtra(Constantes.PARCELABLE_OBJ))
                    endereco = (Endereco) intent.getSerializableExtra(Constantes.PARCELABLE_OBJ);

                if (intent.hasExtra(Constantes.OPERACAO))
                    operacao = intent.getStringExtra(Constantes.OPERACAO);

            }

            if (operacao != null)
                if (operacao.equals(Constantes.EDITAR)) {
                    endereco = EnderecoListFragment.getInstance().retornaEndereco();
                    carregarDados();
                } else {
                    endereco = new Endereco();
                }

        } catch (Exception e) {
            Alerta.exibeSnackbarCurto(view, Constantes.MSG_GENERICA_ERRO);
            e.printStackTrace();
        }

        return view;
    }

    private void carregarDados() {
        if (endereco != null) {
            edtNomeLocal.getEditText().setText(endereco.getNomelocal());
            edtEndereco.getEditText().setText(endereco.getEndereco());
        }
    }

    private void executar() {
        String nome = edtNomeLocal.getEditText().getText().toString().trim();
        String enderecoReal = edtEndereco.getEditText().getText().toString().trim();
        endereco.setNomelocal(nome);
        endereco.setEndereco(enderecoReal);
        if (operacao.equals(Constantes.INSERIR)) {
            endereco.setUsuario_id_firebase(usuario.getFirebase_id());
            endereco.cadastrarEnderecoFirebase(usuario, getActivity(), dataAtual);
        } else {
            endereco.atualizarEnderecoFirebase(getActivity());
        }
    }

    public void retornarTela(String operacao) {
        Toast.makeText(getContext(), "Endereço cadastrado!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.putExtra(Constantes.RESULT, (Parcelable) endereco);
        if (operacao.equals(Constantes.EDITAR))
            EnderecoListFragment.getInstance().carregaListaEndereco(usuario, getActivity());
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    private boolean validarDados(View v) {
        int cont = 0;

        if (validarNomeLocal(v)) cont++;
        if (validarEndereco(v)) cont++;

        return cont == 2;
    }

    private boolean validarNomeLocal(View v) {
        if (edtEndereco.getEditText().getText().toString().isEmpty()) {
            Snackbar.make(v, "Digite o nome do Local!", Snackbar.LENGTH_SHORT).show();
            edtEndereco.setError("Obrigatório");
            return false;
        }
        edtEndereco.setError(null);
        return true;
    }

    private boolean validarEndereco(View v) {
        // Validar se o endereço realmente existe
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        if (edtEndereco != null && !edtEndereco.getEditText().getText().toString().trim().isEmpty()) {
            try {
                List<Address> listaEndereco =
                        geocoder.getFromLocationName(edtEndereco.getEditText().getText().toString().trim(), 1);
                if (listaEndereco != null && listaEndereco.size() > 0) {
                    /* Address atributos:
                     * addressLines=[0:"Rua Isaac Salazar, 102 - Tamarineira, Recife - PE, 52050-160, Brazil"],
                     * */
                    Address endereco = listaEndereco.get(0);
                    edtEndereco.getEditText().setText(endereco.getAddressLine(0));
                } else {
                    edtEndereco.setError("Inválido");
                    Alerta.exibeSnackbarLongo(v, "Endereço Inválido!");
                    return false;
                }
            } catch (Exception e) {
                Alerta.exibeSnackbarCurto(v, Constantes.MSG_GENERICA_ERRO);
                e.printStackTrace();
                return false;
            }
        } else {
            Snackbar.make(v, "Digite o Endereço do Local!", Snackbar.LENGTH_SHORT).show();
            edtEndereco.setError("Obrigatório");
            return false;
        }
        edtEndereco.setError(null);
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
        edtNomeLocal = v.findViewById(R.id.edt_endereco_nomeLocal);
        edtEndereco = v.findViewById(R.id.edt_endereco_endereco);
    }
}