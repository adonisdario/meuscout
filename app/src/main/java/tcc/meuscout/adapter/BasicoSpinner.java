package tcc.meuscout.adapter;

import androidx.annotation.NonNull;

public class BasicoSpinner {
    private int id;
    private String nome;

    public BasicoSpinner(int id , String nome){
        this.id = id;
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    @Override
    public String toString() {
        return nome;
    }
}
