package br.com.ibm.cadeiabatch.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Empresa {

    @Id
    private Long id;
    private String nome_empresa;

    public Empresa() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome_empresa() {
        return nome_empresa;
    }

    public void setNome_empresa(String nome_empresa) {
        this.nome_empresa = nome_empresa;
    }
}
