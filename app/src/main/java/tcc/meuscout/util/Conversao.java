package tcc.meuscout.util;

import android.annotation.SuppressLint;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Conversao {

    public static Date StringParaData(String pDate, String formato) {
        Date date = null;
        SimpleDateFormat formatter = new SimpleDateFormat(formato, Locale.getDefault());

        try {
            date = formatter.parse(pDate);
            return date;
        } catch (ParseException var8) {
            var8.printStackTrace();
            return date;
        } finally {
            ;
        }
    }

    public static Date StringParaData(String pDate) {
        Date date = null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        try {
            date = formatter.parse(pDate);
            return date;
        } catch (ParseException var7) {
            var7.printStackTrace();
            return date;
        } finally {
            ;
        }
    }

    public static String DataParaString(Date pDate, String formato) {
        DateFormat dateFormatNeeded = new SimpleDateFormat(formato);
        return dateFormatNeeded.format(pDate);
    }

    public static String DataParaString(Date pDate) {
        String data = "";
        if (pDate != null) {
            DateFormat formatter = DateFormat.getDateInstance(2, Locale.getDefault());
            data = formatter.format(pDate);
        }

        return data;
    }

    @SuppressLint({"SimpleDateFormat"})
    public static String dataTimeParaString(Date pDate) {
        String data = null;
        if (pDate != null) {
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            data = formatter.format(pDate);
        }

        return data;
    }

    public String dataTimeParaString2(Date pDate) {
        String data = null;
        if (pDate != null) {
            DateFormat dateFormatNeeded = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            data = dateFormatNeeded.format(pDate);
        }

        return data;
    }

    public static String dayDataTimeParaString(Date pDate) {
        String data = null;
        DateFormat formatter = DateFormat.getDateTimeInstance(0, 2, Locale.getDefault());
        data = formatter.format(pDate);
        return data;
    }

    public static String getDataAtual() {
        Date date = new Date();
        return date.toString();
    }

    public static byte[] gerarHash(String frase, String algoritmo) {
        try {
            MessageDigest md = MessageDigest.getInstance(algoritmo);
            md.update(frase.getBytes());
            return md.digest();
        } catch (NoSuchAlgorithmException var3) {
            return null;
        }
    }

    public static String stringHexa(byte[] bytes) {
        StringBuilder s = new StringBuilder();

        for(int i = 0; i < bytes.length; ++i) {
            int parteAlta = (bytes[i] >> 4 & 15) << 4;
            int parteBaixa = bytes[i] & 15;
            if (parteAlta == 0) {
                s.append('0');
            }

            s.append(Integer.toHexString(parteAlta | parteBaixa));
        }

        return s.toString();
    }
    /*
    public static LatLng convertToLatLng(String location) {
        try {
            String[] splitLoc = location.split(",");
            Double lat = Double.valueOf(splitLoc[0]);
            Double lng = Double.valueOf(splitLoc[1]);
            return new LatLng(lat, lng);
        } catch (Exception var4) {
            return new LatLng(0.0D, 0.0D);
        }
    }

     */
}
