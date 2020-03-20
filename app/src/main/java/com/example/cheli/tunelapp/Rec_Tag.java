package com.example.cheli.tunelapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class Rec_Tag extends AppCompatActivity {
    private String cl_codigo;
    private String cut_codigo;
    private String Pasadas;
    private String [] vectorPasadas;
    Spinner var_spnPasadas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rec__tag);
        cl_codigo=getIntent().getStringExtra("cl_codigo");
        cut_codigo=getIntent().getStringExtra("cut_codigo");
        Pasadas=getIntent().getStringExtra("Pasadas");
        vectorPasadas=Pasadas.split("-");
        var_spnPasadas.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item_global,vectorPasadas));
    }
}
