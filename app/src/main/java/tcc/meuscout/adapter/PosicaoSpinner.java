package tcc.meuscout.adapter;

import androidx.annotation.NonNull;

import tcc.meuscout.model.Posicao;

public class PosicaoSpinner {
    private int id;
    private Posicao posicao;

    public PosicaoSpinner(int id , Posicao posicao){
        this.id = id;
        this.posicao = posicao;
    }

    public Posicao getPosicao() {
        return posicao;
    }

    public void setPosicao(Posicao posicao) {
        this.posicao = posicao;
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
        return posicao.getSigla() + " - " + posicao.getNome();
    }
}
