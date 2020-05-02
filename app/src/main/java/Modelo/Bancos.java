package Modelo;

public class Bancos {
    String cod_banco;

    String desc_banc;


    public Bancos(){}

    public Bancos(String cod_banco,String  desc_banc){
        setCod_banco(cod_banco);
        setDesc_banc( desc_banc);

    }

    public String getCod_banco() {
        return cod_banco;
    }

    public void setCod_banco(String cod_banco) {
        this.cod_banco = cod_banco;
    }

    public String getDesc_banc() {
        return desc_banc;
    }

    public void setDesc_banc(String desc_banc) {
        this.desc_banc = desc_banc;
    }

    @Override
    public String toString() {
        return desc_banc;
    }
}
