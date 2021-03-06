package com.example.cheli.tunelapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Hist_soli_pend extends AppCompatActivity {
    String list_soli;
    String vectorSoliPend[];
    String vectorSoli[];
    private ListView listTags;
    private List<cls_list_hist_soli_pend> pagos;
    private cls_list_hist_soli_pend_adap ListPagoAdapter;
    String tipoDoc;
    String l;
    String lugares;
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_hist_soli_pend);
        listTags = (ListView) findViewById(R.id.lsvw_pagos);
        pagos = new ArrayList<>();
        pagos.clear();
        list_soli = getIntent().getStringExtra("listSoliPend");
        lugares=getIntent().getStringExtra("lugares");
        try {
            cargasListPendientes();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ImageView img = (ImageView) findViewById(R.id.img_regresar);
        img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Hist_soli_pend.this, Menu.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                finish();
            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Intent intent = new Intent(Hist_soli_pend.this, Menu.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        finish();
        return super.onKeyDown(keyCode, event);
    }


    public void cargasListPendientes() throws IOException,JSONException{
    vectorSoliPend=list_soli.split("\\*");
        if (vectorSoliPend.length > 0)
    {
        for (Integer i=0;i<(vectorSoliPend.length);i++)
        {
            vectorSoli=vectorSoliPend[i].split("\\?");
            if (vectorSoli.length > 0) {
                Log.e("",vectorSoli[1]);
                if (vectorSoli.length==3) {
                    pagos.add(new cls_list_hist_soli_pend(vectorSoli[0],"Fecha: "+vectorSoli[1], vectorSoli[0], "Estado: " + vectorSoli[2]));
                }else{

                }
            }

        }
        ListPagoAdapter = new cls_list_hist_soli_pend_adap(Hist_soli_pend.this,pagos);
        listTags.setAdapter(ListPagoAdapter);
    }

    registerForContextMenu(listTags);
}

    public void solicitudTag(View view){
    Bundle bundle = new Bundle();
    bundle.putString("codigo_trama", "9-1");
    bundle.putString("lugares", lugares);
    Intent i = new Intent(Hist_soli_pend.this, Paso_Soli_Tag_Cli.class);
    i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
    i.putExtras(bundle);
    startActivity(i);
}
}
