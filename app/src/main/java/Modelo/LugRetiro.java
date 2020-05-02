package Modelo;

public class LugRetiro {
    String cod_lug;
    String desc_retiro;

    public LugRetiro(){}

    public LugRetiro(String cod_lug,String  esc_retiro){
        setCod_lug(cod_lug);
        setDesc_retiro( esc_retiro);

    }
    public String getCod_lug() {
        return cod_lug;
    }

    public void setCod_lug(String cod_lug) {
        this.cod_lug = cod_lug;
    }

    public String getDesc_retiro() {
        return desc_retiro;
    }

    public void setDesc_retiro(String desc_retiro) {
        this.desc_retiro = desc_retiro;
    }

    @Override
    public String toString() {
        return desc_retiro;
    }
}
