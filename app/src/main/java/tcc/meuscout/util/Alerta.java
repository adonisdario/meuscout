package tcc.meuscout.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.google.android.material.snackbar.Snackbar;

public abstract class Alerta {

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

    public static void gravarExcecao(String TAG, Exception e) {
        Log.e(TAG, e.getMessage());
        e.printStackTrace();
    }
}
