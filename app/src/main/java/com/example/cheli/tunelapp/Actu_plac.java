package com.example.cheli.tunelapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import org.json.JSONException;

import java.io.IOException;

public class Actu_plac extends AppCompatActivity {
String codigoUsuario="";
String codigoVehiculo="";
String Vehiculos="";
String nombre="";
String correo;
String tipoDoc;
String l;
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actu_plac);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        try {
            obtenerDatos();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ImageView img = (ImageView) findViewById(R.id.img_regresar);
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

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            try {
                navegacion();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void btn_continuar(View view)throws IOException, JSONException {
        Intent i = new Intent(Actu_plac.this, Actu_plac_foto.class );
        Bundle bundle = new Bundle();
        bundle.putString("cl_codigo" ,codigoUsuario);
        bundle.putString("cut_codigo" ,codigoVehiculo);
        bundle.putString("Vehiculos", Vehiculos);
        bundle.putString("codigo_trama" ,getString(R.string.cm_contrato_camb_vehi));
        bundle.putString("nombre", nombre);
        bundle.putString("correo",correo);
        i.putExtras(bundle);
        startActivity(i);
        finish();
    }

    public void navegacion()throws IOException,JSONException{
        Intent intent = new Intent(Actu_plac.this, Menu.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        Bundle bundle1=new Bundle();
        bundle1.putString("Vehiculos", Vehiculos);
        bundle1.putString("nombre", nombre);
        bundle1.putString("cl_codigo", codigoUsuario);
        bundle1.putString("correo",correo);
        bundle1.putString("tipoDoc",tipoDoc);
        bundle1.putString("cedula",l);
        intent.putExtras(bundle1);
        startActivity(intent);
        finish();
    }

    public void obtenerDatos()throws IOException,JSONException{
        codigoUsuario=getIntent().getStringExtra("cl_codigo");
        codigoVehiculo=getIntent().getStringExtra("cut_codigo");
        Vehiculos=getIntent().getStringExtra("Vehiculos");
        nombre=getIntent().getStringExtra("nombre");
        correo=getIntent().getStringExtra("correo");
        tipoDoc= getIntent().getStringExtra("tipoDoc");
        l= getIntent().getStringExtra("cedula");
    }
}


