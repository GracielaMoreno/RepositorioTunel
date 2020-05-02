package com.example.cheli.tunelapp;

/**
 * Created by Procelec on 5/7/2019.
 */

public class cls_list_tags {
    private String Campo1;
    private String Campo2;
    private String Campo3;
    private String Campo4;
    private String Campo5;
    private String Campo6;

    public cls_list_tags()
    {
    }

    public cls_list_tags(String campo1, String campo2, String campo3, String campo4, String campo5,String campo6)
    {
        setCampo1(campo1);
        setCampo2(campo2);
        setCampo3(campo3);
        setCampo4(campo4);
        setCampo5(campo5);
        setCampo6(campo6);
    }

    public String getCampo1() {
        return Campo1;
    }
    public String getCampo2() {
        return Campo2;
    }
    public String getCampo3() {
        return Campo3;
    }
    public String getCampo4() {
        return Campo4;
    }
    public String getCampo5() {
        return Campo5;
    }
    public String getCampo6() {
        return Campo6;
    }

    public void setCampo1(String campo1) {
        Campo1 = campo1;
    }
    public void setCampo2(String campo2) {
        Campo2 = campo2;
    }
    public void setCampo3(String campo3) {
        Campo3 = campo3;
    }
    public void setCampo4(String campo4) {
        Campo4 = campo4;
    }
    public void setCampo5(String campo5) {
        Campo5 = campo5;
    }
    public void setCampo6(String campo6) {
        Campo6 = campo6;
    }

}
