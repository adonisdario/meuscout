package tcc.meuscout.model;

import java.io.Serializable;

public class Posicao implements Serializable {
    private String nome, sigla;
    private int num;
    private Integer usuario_id, id;

    public Posicao(String nome, String sigla, int num) {
        this.nome = nome;
        this.sigla = sigla;
        this.num = num;
    }

    public Posicao() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public Integer getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(Integer usuario_id) {
        this.usuario_id = usuario_id;
    }

}
