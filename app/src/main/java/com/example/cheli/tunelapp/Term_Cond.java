package com.example.cheli.tunelapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

public class Term_Cond extends AppCompatActivity {
    String valorTotal1;
    String valorConIva1;
    String valorSinIva;
    String valorIva1;
    String porcIva1;
    String nombre;
    String correo;
    String cl_codigo;
    String pagos;
    String idRuta;
    String cv_codigos;
    String tipoDoc;
    String l;
    String Pasadas;
    String cut_codigo;
    String Placa;
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term__cond);
        Placa=getIntent().getStringExtra("placa");
        pagos =getIntent().getStringExtra("pagos");
        valorTotal1 =getIntent().getStringExtra("valorTotal");
        valorConIva1 =getIntent().getStringExtra("valorConIva");
        valorSinIva =getIntent().getStringExtra("valorSinIva");
        valorIva1 =getIntent().getStringExtra("valorIva");
        porcIva1 =getIntent().getStringExtra("porcIva");
        idRuta=getIntent().getStringExtra("idRuta");
        cut_codigo=getIntent().getStringExtra("cut_codigo");
        cv_codigos =getIntent().getStringExtra("cv_codigos");
        ImageView img = (ImageView) findViewById(R.id.img_regresar);
        img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                navegacio();
            }
        });
        Pasadas=getIntent().getStringExtra("Pasadas");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        navegacio();
        return super.onKeyDown(keyCode, event);
    }
    public void navegacio(){
        if (idRuta.equals("pago")) {
            Intent intent = new Intent(Term_Cond.this, Pago.class );
            Bundle bundle = new Bundle();
            bundle.putString("pagos",pagos);
            bundle.putString("valorTotal",valorTotal1);
            bundle.putString("valorConIva",valorConIva1);
            bundle.putString("valorSinIva",valorSinIva);
            bundle.putString("valorIva",valorIva1);
            bundle.putString("porcIva",porcIva1);
            bundle.putString("cv_codigos",cv_codigos);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        }
        if (idRuta.equals("recarga")){
            Intent intent = new Intent(Term_Cond.this, Recarga.class );
            Bundle bundle = new Bundle();
            bundle.putString("Pasadas",Pasadas);
            bundle.putString("cut_codigo",cut_codigo);
            bundle.putString("placa",Placa);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        }
    }
}
