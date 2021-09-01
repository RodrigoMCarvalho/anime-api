package com.rodrigo.anime.dto;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

public class AnimeDTO implements Serializable {

    private Long id;
    @NotEmpty(message = "Nome obrigatório")
    private String nome;
    private Long ano;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Long getAno() {
        return ano;
    }

    public void setAno(Long ano) {
        this.ano = ano;
    }
}
