package br.com.ibm.cadeiabatch.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Categoria {
	
	@Id
	private long id;
    private String descricao;
    
    @JsonIgnore
    @ManyToMany(mappedBy = "categorias")
    private List<Usuario> usuarios = new ArrayList<>();
    
    public Categoria() {
    	
    }
    
	public Categoria(long id, String descricao) {
		super();
		this.id = id;
		this.descricao = descricao;
	}

	public void setDescricao(String descricao){
        this.descricao = descricao;
    }

    public String getDescricao(){
        return this.descricao;
    }
    
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}
	
}