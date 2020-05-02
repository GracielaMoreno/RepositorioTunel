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
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Hist_pago extends AppCompatActivity {
    private ListView listTags;
    private List<cls_list_hist_pago> histPagos;
    private String[] vectorHistPagos;
    private String[] vectorHistPago;
    TextView valorTotal;
    private cls_list_hist_pago_adap ListPagoAdapter;
    String cl_codigo;
    String pagos;
    String tramaPago;
    String Vehiculos;
    String valorTotal1;
    String valorConIva1;
    String valorSinIva;
    String valorIva1;
    String porcIva1;
    String nombre;
    String correo;
    String idRuta;
    String cv_codigos;
    String tipoDoc;
    String l;
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_hist_pago);
        listTags=(ListView)findViewById(R.id.lsvw_pagos);
        histPagos = new ArrayList<>();
        histPagos.clear();
        listTags.setAdapter(null);

        try {
            obtenerDato();
            llenarListaHisPago();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ImageView img = (ImageView) findViewById(R.id.img_regresar);
        img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    navegacio();
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
            navegacio();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void navegacio()throws IOException, JSONException {
        if (idRuta.equals("pago")) {
            Intent intent = new Intent(Hist_pago.this, Pago.class );
            Bundle bundle = new Bundle();
            bundle.putString("pagos",pagos);
            bundle.putString("valorTotal",valorTotal1);
            bundle.putString("valorConIva",valorConIva1);
            bundle.putString("valorSinIva",valorSinIva);
            bundle.putString("valorIva",valorIva1);
            bundle.putString("porcIva",porcIva1);
            bundle.putString("nombre",nombre);
            bundle.putString("cl_codigo",cl_codigo);
            bundle.putString("correo",correo);
            bundle.putString("idRuta",idRuta);
            bundle.putString("cv_codigos",cv_codigos);
            bundle.putString("tipoDoc",tipoDoc);
            bundle.putString("cedula",l);
            bundle.putString("Vehiculos",Vehiculos);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        }
        if (idRuta.equals("recarga")){
            Intent intent = new Intent(Hist_pago.this, Menu.class );
            Bundle bundle = new Bundle();
            bundle.putString("Vehiculos",Vehiculos);
            bundle.putString("nombre",nombre);
            bundle.putString("correo",correo);
            bundle.putString("cl_codigo",cl_codigo);
            bundle.putString("idRuta",idRuta);
            bundle.putString("tipoDoc",tipoDoc);
            bundle.putString("cedula",l);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        }
    }

    public void obtenerDato()throws IOException,JSONException{
        tipoDoc= getIntent().getStringExtra("tipoDoc");
        l= getIntent().getStringExtra("cedula");
        pagos =getIntent().getStringExtra("pagos");
        Vehiculos =getIntent().getStringExtra("Vehiculos");
        valorTotal1 =getIntent().getStringExtra("valorTotal");
        valorConIva1 =getIntent().getStringExtra("valorConIva");
        valorSinIva =getIntent().getStringExtra("valorSinIva");
        valorIva1 =getIntent().getStringExtra("valorIva");
        porcIva1 =getIntent().getStringExtra("porcIva");
        cl_codigo=getIntent().getStringExtra("cl_codigo");
        idRuta=getIntent().getStringExtra("idRuta");
        nombre=getIntent().getStringExtra("nombre");
        correo=getIntent().getStringExtra("correo");
        cv_codigos =getIntent().getStringExtra("cv_codigos");
    }

    public void llenarListaHisPago()throws IOException,JSONException{
        valorTotal=findViewById(R.id.totalPago);
        tramaPago =getIntent().getStringExtra("hist_pagos");
        vectorHistPagos =tramaPago.split("\\*");
        if (vectorHistPagos.length > 0)
        {
            for (Integer i = 0; i<(vectorHistPagos.length); i++)
            {
                vectorHistPago = vectorHistPagos[i].split("\\?");
                if (vectorHistPago.length > 0) {
                    if (vectorHistPago.length == 7) {
                        histPagos.add(new cls_list_hist_pago(vectorHistPago[0],vectorHistPago[1], vectorHistPago[0], "Valor: $" + vectorHistPago[2],"Fecha: " + vectorHistPago[3], "Medio de pago: " + vectorHistPago[4], "Estado: " + vectorHistPago[5],vectorHistPago[6]));

                    } else{

                    }
                }

            }
            ListPagoAdapter = new cls_list_hist_pago_adap(Hist_pago.this,histPagos);
            listTags.setAdapter(ListPagoAdapter);
        }

        registerForContextMenu(listTags);
    }
}

