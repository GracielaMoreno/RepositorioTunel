package com.example.cheli.tunelapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class Actu_plac extends AppCompatActivity {
String codigoUsuario="";
String codigoVehiculo="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actu_plac);
        codigoUsuario=getIntent().getStringExtra("cl_codigo");
        codigoVehiculo=getIntent().getStringExtra("cut_codigo");
        ImageView img = (ImageView) findViewById(R.id.img_regresar);
        img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Intent intent = new Intent( Actu_plac.this, List_tags.class );
                Intent intent = new Intent( Actu_plac.this, Login.class );
                Bundle bundle = new Bundle();
                bundle.putString("cl_codigo" ,codigoUsuario);
                bundle.putString("cut_codigo" ,codigoVehiculo);
                Intent i = new Intent(Actu_plac.this, Actu_plac_foto.class );
                i.putExtras(bundle);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                finish();
            }
        });

    }

    public void btn_continuar(View view) {
        Intent i = new Intent(Actu_plac.this, Actu_plac_foto.class );
        Log.e("",codigoUsuario+"cut"+codigoVehiculo);

        startActivity(i);
        finish();
    }
}


