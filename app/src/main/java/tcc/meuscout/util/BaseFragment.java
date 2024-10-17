package tcc.meuscout.util;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

import tcc.meuscout.R;

public abstract class BaseFragment extends Fragment implements View.OnClickListener, android.content.DialogInterface.OnClickListener {
    private String CTG_LOG = "BASE_ACT";
    private String pastaSistema;
    private View view;

    public BaseFragment() {
    }

    @Nullable
    public View getView() {
        return this.view;
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = null;

        try {
            view = inflater.inflate(this.getFragmentLayout(), container, false);
            this.view = view;
            this.inicializar();
        } catch (Exception var6) {
            Alerta.exibeSnackbarLongo(container.getRootView(), Constantes.MSG_GENERICA_ERRO);
            Log.e(this.CTG_LOG, "Erro: ", var6);
        }

        return view;
    }

    protected abstract int getFragmentLayout() throws Exception;

    protected abstract void inicializar() throws Exception;

    public void onClick(DialogInterface dialog, int which) {
    }

    public void onClick(View v) {
    }

    protected boolean validarDados() throws Exception {
        return false;
    }

    protected void executar() throws Exception {
    }
    /*
    protected void gravar() {
        try {
            if (this.validarDados()) {
                this.executar();
            }
        } catch (CustomException var2) {
            this.showDialog("Erro", var2.getMensagem(), string.string_ok);
            if (var2.getMensagem().toString() != "") {
                this.gravarExcecao(var2, this.getPastaSistema());
                Log.e(this.CTG_LOG, "Erro: ", var2);
            }
        } catch (Exception var3) {
            this.showDialog("Erro", "Um erro inesperado ocorreu.", string.string_ok);
            this.gravarExcecao(new CustomException("Erro", var3.getMessage(), this.getClass().getSimpleName(), "gravar"), this.getPastaSistema());
            Log.e(this.CTG_LOG, "Erro: ", var3);
        }

    }

    protected void gravarExcecao(Exception e, String pastaSistema) {
        RotinasGerais.gravaLog(e, this.getContext(), pastaSistema);
        Log.e(this.CTG_LOG, "Erro: ", e);
    }

     */

    protected boolean isNetworkAvailable() {
        @SuppressLint("WrongConstant")
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getContext().getSystemService("connectivity");
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public String getPastaSistema() {
        return this.pastaSistema;
    }

    public void setPastaSistema(String pastaSistema) {
        this.pastaSistema = pastaSistema;
    }

    public static void exibeToastLongo(Context ctx, String msg) {
        Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
    }

    public static void exibeToastCurto(Context ctx, String msg) {
        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
    }

    public static void exibeDialog(Context context, String title, String msg, int botao) {
        AlertDialog.Builder alertaBuilder = new AlertDialog.Builder(context);
        alertaBuilder.setTitle(title);
        alertaBuilder.setMessage(msg);
        alertaBuilder.setCancelable(false);
        alertaBuilder.setPositiveButton(botao, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog alerta = alertaBuilder.create();
        alerta.show();
    }

    public static void exibeSnackbarCurto(View v, String msg) {
        Snackbar.make(v, msg, Snackbar.LENGTH_SHORT).show();
    }

    public static void exibeSnackbarLongo(View v, String msg) {
        Snackbar.make(v, msg, Snackbar.LENGTH_LONG).show();
    }
}
