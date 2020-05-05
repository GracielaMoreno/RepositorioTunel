package com.example.cheli.tunelapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import org.json.JSONException;

import java.io.IOException;

public class Preg_frec extends AppCompatActivity {
    String pagos;
    String valorTotal1;
    String valorConIva1;
    String valorSinIva;
    String valorIva1;
    String porcIva1;
    String cl_codigo;
    String idRuta;
    String Pasadas;
    String cut_codigo;
    String Placa;
    String tramaPago;
    String cv_codigos;
    String tipoDoc;
    String l;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_preg_frec);
        ImageView img = (ImageView) findViewById(R.id.img_regresar);
        pagos = getIntent().getStringExtra("pagos");
        valorTotal1 = getIntent().getStringExtra("valorTotal");
        valorConIva1 = getIntent().getStringExtra("valorConIva");
        valorSinIva = getIntent().getStringExtra("valorSinIva");
        valorIva1 = getIntent().getStringExtra("valorIva");
        porcIva1 = getIntent().getStringExtra("porcIva");
        Pasadas = getIntent().getStringExtra("Pasadas");
        idRuta = getIntent().getStringExtra("idRuta");
        cut_codigo = getIntent().getStringExtra("cut_codigo");
        cv_codigos = getIntent().getStringExtra("cv_codigos");
        Placa = getIntent().getStringExtra("placa");

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

    public void btn_continuar(View view) throws IOException, JSONException {
        navegacion();
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
        }
        return super.onKeyDown(keyCode, event);
    }

    public void navegacion() throws IOException, JSONException {
        if (idRuta.equals("recarga")) {
            Intent inten2 = new Intent(Preg_frec.this, Recarga.class);
            inten2.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            Bundle bundle = new Bundle();
            bundle.putString("Pasadas", Pasadas);
            bundle.putString("cut_codigo", cut_codigo);
            bundle.putString("placa", Placa);
            bundle.putString("idRuta", idRuta);

            inten2.putExtras(bundle);
            startActivity(inten2);
            finish();

        }
        if (idRuta.equals("pago")) {
            Intent inten2 = new Intent(Preg_frec.this, Pago.class);
            inten2.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            Bundle bundle = new Bundle();
            bundle.putString("pagos", pagos);
            bundle.putString("valorTotal", valorTotal1);
            bundle.putString("valorConIva", valorConIva1);
            bundle.putString("valorSinIva", valorSinIva);
            bundle.putString("valorIva", valorIva1);
            bundle.putString("porcIva", porcIva1);
            bundle.putString("idRuta", idRuta);
            bundle.putString("cv_codigos", cv_codigos);

            inten2.putExtras(bundle);
            startActivity(inten2);
            finish();
        }
    }

}

