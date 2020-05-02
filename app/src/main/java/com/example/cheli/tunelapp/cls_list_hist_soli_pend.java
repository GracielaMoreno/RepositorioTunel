package com.example.cheli.tunelapp;

import java.util.List;

public class cls_list_hist_soli_pend {
    private String Campo1;
    private String Campo2;
    private String Campo3;
    private String Campo4;


    public cls_list_hist_soli_pend ()
    {
    }

    public cls_list_hist_soli_pend (String campo1, String campo2, String campo3, String campo4)
    {
        setCampo1(campo1);
        setCampo2(campo2);
        setCampo3(campo3);
        setCampo4(campo4);

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

}
