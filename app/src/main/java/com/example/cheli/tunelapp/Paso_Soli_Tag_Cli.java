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
    String lugares;
    String cl_codigo;
    String tipoDoc;
    String l;
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paso__soli__tag__cli);
        lugares=getIntent().getStringExtra("lugares");
        btn_continuar=findViewById(R.id.btnContinuar);
        img=findViewById(R.id.img_regresar);
        btn_continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("codigo_trama", "9-1");
                bundle.putString("lugares", lugares);
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
    bundle3.putString("lugares", lugares);
    Intent intent = new Intent(Paso_Soli_Tag_Cli.this, Menu.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
    intent.putExtras(bundle3);
    startActivity(intent);
    finish();

}

}
