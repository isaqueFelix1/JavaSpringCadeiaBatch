package br.com.ibm.cadeiabatch.entity;

public class PontoGrafico{

    private String data;

    private int info;

    private int warn;

    private int erro;

    private int total;

    public String getData(){
        return this.data;
    }

    public int getInfo(){
        return this.info;
    }

    public int getWarn(){
        return this.warn;
    }

    public int getErro(){
        return this.erro;
    }

    public int getTotal(){
        return this.total;
    }

    public void setData(String data){
        this.data = data;
    }

    public void setInfo(int info){
        this.info = info;
    }

    public void setWarn(int warn){
        this.warn = warn;
    }

    public void setErro(int erro){
        this.erro = erro;
    }

    public void setTotal(int total){
        this.total = total;
    }
    

}