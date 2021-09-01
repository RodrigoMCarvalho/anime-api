package com.rodrigo.anime.model;

import lombok.Builder;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Entity
@Table(name = "anime")
@Builder
public class Anime implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty(message = "Nome obrigatório")
    private String nome;
    private Long ano;

    public Anime(Long id, String nome, Long ano) {
        this.id = id;
        this.nome = nome;
        this.ano = ano;
    }

    public Anime(String name) {
        this.nome = name;
    }

    public Anime() {
    }

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
