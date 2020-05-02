package com.example.cheli.tunelapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.json.JSONException;

import java.io.IOException;

public class Paso_Soli_Tag_Cli extends AppCompatActivity {
    Button btn_continuar;
    ImageView img;
    String nombre;
    String correo;
    String vehiculos;
    String lugares;
    String cl_codigo;
    String tipoDoc;
    String l;
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paso__soli__tag__cli);
        vehiculos = getIntent().getStringExtra("Vehiculos");
        lugares=getIntent().getStringExtra("lugares");
        nombre=getIntent().getStringExtra("nombre");
        cl_codigo=getIntent().getStringExtra("cl_codigo");
        correo = getIntent().getStringExtra("correo");
        tipoDoc= getIntent().getStringExtra("tipoDoc");
        l= getIntent().getStringExtra("cedula");
        btn_continuar=findViewById(R.id.btnContinuar);
        img=findViewById(R.id.img_regresar);
        btn_continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("codigo_trama", "9-1");
                bundle.putString("Vehiculos", vehiculos);
                bundle.putString("lugares", lugares);
                bundle.putString("nombre", nombre);
                bundle.putString("cl_codigo", cl_codigo);
                bundle.putString("correo", correo);
                bundle.putString("tipoDoc",tipoDoc);
                bundle.putString("cedula",l);
                Intent i = new Intent(Paso_Soli_Tag_Cli.this, Soli_tag_matr.class);
                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                i.putExtras(bundle);
                startActivity(i);
                finish();
            }
        });

        img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    navegacion();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        try {
            navegacion();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return super.onKeyDown(keyCode, event);
    }
public void navegacion()throws IOException, JSONException {
    Bundle bundle3 = new Bundle();
    bundle3.putString("Vehiculos", vehiculos);
    bundle3.putString("Vehiculos", vehiculos);
    bundle3.putString("nombre", nombre);
    bundle3.putString("cl_codigo", cl_codigo);
    bundle3.putString("correo", correo);
    bundle3.putString("tipoDoc",tipoDoc);
    bundle3.putString("lugares", lugares);
    bundle3.putString("cedula",l);
    Intent intent = new Intent(Paso_Soli_Tag_Cli.this, Menu.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
    intent.putExtras(bundle3);
    startActivity(intent);
    finish();

}

}
