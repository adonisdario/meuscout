package tcc.meuscout.util;

import android.util.Base64;

public class Base64Custom {

    public static String codificar(String texto) {
        return Base64.encodeToString(texto.getBytes(), Base64.DEFAULT).trim();
    }

    public static String decodificar(String texto) {
        return new String(Base64.decode(texto, Base64.DEFAULT));
    }

}
