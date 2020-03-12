package com.example.cheli.tunelapp;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Portada extends AppCompatActivity {
    Contador contador;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portada);
        contador = new Contador(3000,1000);

        contador.start ();
    }
    public class Contador extends CountDownTimer{

        public Contador(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            Bundle bundle = new Bundle();
            bundle.putString("CodigoUsuario" ,"1");
            bundle.putString("NombreUsuario" ,"1");
            bundle.putString("Urbanizacion" ,"1");
            Intent i = new Intent(Portada.this, Login.class );
            i.putExtras(bundle);
            startActivity(i);
            finish();

        }

        @Override
        public void onTick(long millisUntilFinished) {

        }

    }
}
