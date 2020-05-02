package Modelo;

import java.io.Serializable;

public class Pasadas implements Serializable {
    private String codigo;
    private String valor;
    private String texto;


    public Pasadas(){}

    public Pasadas(String codigo,String valor,String texto){
        setCodigo(codigo);
        setTexto(texto);
        setValor(valor);
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    @Override
    public String toString() {
        return  "  "+valor+" por el valor de $" +texto ;
    }
}
