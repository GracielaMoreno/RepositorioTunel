package Modelo;

public class Tarjettas {
    String cod_tarj;

    String desc_tarj;

    public Tarjettas(){}

    public Tarjettas(String cod_tarj,String  desc_tarj){
        setCod_tarj(cod_tarj);
        setDesc_tarj( desc_tarj);

    }

    public String getCod_tarj() {
        return cod_tarj;
    }

    public void setCod_tarj(String cod_tarj) {
        this.cod_tarj = cod_tarj;
    }

    public String getDesc_tarj() {
        return desc_tarj;
    }

    public void setDesc_tarj(String desc_tarj) {
        this.desc_tarj = desc_tarj;
    }

    @Override
    public String toString() {
        return  desc_tarj;
    }
}
