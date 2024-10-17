package tcc.meuscout.util;

import java.text.SimpleDateFormat;

public class DataCustom { //Classe usada para retornar a data atual

    public static String dataAtual() {
        long date = System.currentTimeMillis(); //Recebe a data no formato long
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy"); //Edita a formatação da data
        String dataString = simpleDateFormat.format(date); //Formata a data para a formatação selecionada
        return dataString;
    }

    public static String dataFormatada(String data) { //Pega a data no formato dd/MM/yyyy e retorna MMyyyy
        String retorno[] = data.split("/"); //Divide a string e adiciona em um array os caracteres até "/"
        String dia = retorno[0]; //Dia dd
        String mes = retorno[1]; //Mês MM
        String ano = retorno[2]; //Ano yyyy
        return mes + ano;
    }
}
