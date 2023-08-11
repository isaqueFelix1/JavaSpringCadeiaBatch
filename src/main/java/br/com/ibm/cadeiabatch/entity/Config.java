package br.com.ibm.cadeiabatch.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Config {
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
    private String nomeConfig;
    
    private String valorConfig;

    public void setNomeConfig(String nomeConfig){
        this.nomeConfig = nomeConfig;
    }

    public void setValorConfig(String valorConfig){
        this.valorConfig = valorConfig;
    }

    public String getNomeConfig(){
        return this.nomeConfig;
    }

    public String getValorConfig(){
        return this.valorConfig;
    }
	
}