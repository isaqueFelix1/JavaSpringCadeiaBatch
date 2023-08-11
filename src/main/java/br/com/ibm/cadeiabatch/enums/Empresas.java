package br.com.ibm.cadeiabatch.enums;

public enum Empresas {
    PORTOSEGURO(0), CMIG(1);

    public int valor;

    Empresas(int i) {
        valor = i;
    }

    public static String getName(int valor){
        switch (valor){
            case 0:
                return "PORTOSEGURO";
            case 1:
                return "CMIG";
        }
        return "";
    }
}
