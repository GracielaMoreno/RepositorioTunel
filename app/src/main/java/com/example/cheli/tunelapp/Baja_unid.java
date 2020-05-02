package com.example.cheli.tunelapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONException;

import java.io.IOException;

public class Baja_unid extends AppCompatActivity {
TextView tv1;
Button btnPin;
Button btnEnviar;
String codigoUsuario;
String codigoVehiculo;
String Vehiculos;
String numTag;
String Placa;
TextView textInfoVehi;
String pin;
String nombreUsuario;
String correo;
String cut_propietario;
String tipoDoc;
String l;
    private cls_conexion ob;
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_baja_unid);
        tv1=findViewById(R.id.editTextpin);
        btnPin=findViewById(R.id.btnPin);
        btnEnviar=findViewById(R.id.btnEnviar);
        tv1.setVisibility(View.GONE);
        btnEnviar.setEnabled(false);
        textInfoVehi=findViewById(R.id.textInfoVehi);
        try {
            obtenerDato();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        btnPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if (isOnline(getApplicationContext()) == true) {
                if (cut_propietario.equals("CLIENTE")){
                String envia= getString(R.string.cm_envio_pin_correo)+","+codigoUsuario+","+codigoVehiculo;
                ob.conectar();
                ob.enviar(envia);
                ob.cerrar();
                String TramaRecida = ob.cadena.toString();
                //String TramaRecida = "1|mensaje";

                String[] vectorTramaRecibida = TramaRecida.split("\\|");
                String CodRespuesta=vectorTramaRecibida[0];
                if (vectorTramaRecibida.length==2) {
                    if (CodRespuesta.equals("1")) {
                        String Mensaje = vectorTramaRecibida[1];
                        Toast.makeText(Baja_unid.this, Mensaje, Toast.LENGTH_LONG).show();
                        tv1.setVisibility(View.VISIBLE);
                        btnEnviar.setEnabled(true);
                    }
                    if (CodRespuesta.equals("2")) {
                        String Mensaje = vectorTramaRecibida[1];
                        Toast.makeText(Baja_unid.this, Mensaje, Toast.LENGTH_LONG).show();
                    }
                    if (CodRespuesta.equals("3")) {
                        String Mensaje = vectorTramaRecibida[1];
                        Toast.makeText(Baja_unid.this, Mensaje, Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), R.string.g_error_servidor, Toast.LENGTH_LONG).show();
                }
            }
            if (cut_propietario.equals("EPMMOP")){
                AlertDialog.Builder builder = new AlertDialog.Builder(Baja_unid.this,R.style.CustomDialog);
                builder.setTitle("Informativo");
                builder.setMessage("Para realizar este proceso debe acercarse a los centros de atención")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                Intent i = new Intent(Baja_unid.this,Menu.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("Vehiculos", Vehiculos);
                                bundle.putString("correo", correo);
                                bundle.putString("nombre", nombreUsuario);
                                bundle.putString("cl_codigo", codigoUsuario);
                                bundle.putString("cut_propietario", cut_propietario);
                                bundle.putString("tipoDoc",tipoDoc);
                                bundle.putString("cedula",l);
                                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                i.putExtras(bundle);
                                startActivity(i);
                                finish();
                            }
                        });
                builder.create().show();
            }

                    }else{
                        Toast.makeText(getApplicationContext(), R.string.g_error_internet, Toast.LENGTH_LONG).show();
                    }}catch (Exception e){
                    Toast.makeText(getApplicationContext(), R.string.g_error_global, Toast.LENGTH_LONG).show();
                }

            }

        });
    btnEnviar.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
    if (tv1.getText().toString().equals("")){
        Toast.makeText(Baja_unid.this, "Ingrese el PIN enviado al correo", Toast.LENGTH_LONG).show();
    }else {
        try{
            if (isOnline(getApplicationContext()) == true) {
        pin=tv1.getText().toString();
        String envia = getString(R.string.cm_verificar_pin_correo) + "," + codigoUsuario + "," + codigoVehiculo + "," + pin;
        ob.conectar();
        ob.enviar(envia);
        ob.cerrar();
        String TramaRecida = ob.cadena.toString();
        //String TramaRecida = "1|mensaje|1-PTJ560-1210123456-CHEVROLET AVEO 2012-20*2-PTR2589-1210123457-KIA SPORTAGE R2 2013-10";

        String[] vectorTramaRecibida = TramaRecida.split("\\|");
        String CodRespuesta = vectorTramaRecibida[0];
        if (vectorTramaRecibida.length==3){
        if (CodRespuesta.equals("1")) {
            String Mensaje = vectorTramaRecibida[1];
            final String Vehiculos1 = vectorTramaRecibida[2];
            AlertDialog.Builder builder = new AlertDialog.Builder(Baja_unid.this,R.style.CustomDialog);
            builder.setTitle("Informativo");
            builder.setMessage(Mensaje)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Toast.makeText(Baja_unid.this, Mensaje, Toast.LENGTH_LONG).show();
                            Intent i = new Intent(Baja_unid.this, Menu.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("Vehiculos", Vehiculos1);
                            bundle.putString("correo", correo);
                            bundle.putString("nombre", nombreUsuario);
                            bundle.putString("cl_codigo", codigoUsuario);
                            bundle.putString("tipoDoc",tipoDoc);
                            bundle.putString("cedula",l);
                            i.putExtras(bundle);
                            startActivity(i);
                            finish();
                        }
                    });
            builder.create().show();

        }}else{
            Toast.makeText(getApplicationContext(), R.string.g_error_servidor, Toast.LENGTH_LONG).show();

        }
                if (vectorTramaRecibida.length==2){
        if (CodRespuesta.equals("2")) {
            String Mensaje = vectorTramaRecibida[1];
            Toast.makeText(Baja_unid.this, Mensaje, Toast.LENGTH_LONG).show();
        }
        if (CodRespuesta.equals("3")) {
            String Mensaje = vectorTramaRecibida[1];
            Toast.makeText(Baja_unid.this, Mensaje, Toast.LENGTH_LONG).show();
        }
        if (CodRespuesta.equals("4")) {
            String Mensaje = vectorTramaRecibida[1];
            Toast.makeText(Baja_unid.this, Mensaje, Toast.LENGTH_LONG).show();
        }}else{
                    Toast.makeText(getApplicationContext(), R.string.g_error_servidor, Toast.LENGTH_LONG).show();
                }

    }else{
                Toast.makeText(getApplicationContext(), R.string.g_error_internet, Toast.LENGTH_LONG).show();
            }}catch (Exception e){
            Toast.makeText(getApplicationContext(), R.string.g_error_global, Toast.LENGTH_LONG).show();
        }


    }
}
    });
        ImageView img = (ImageView) findViewById(R.id.img_regresar);
        img.setOnClickListener(new View.OnClickListener(){
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

    public void navegacion()throws IOException,JSONException{
    Intent intent = new Intent(Baja_unid.this, Menu.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
    Bundle bundle1=new Bundle();
    bundle1.putString("Vehiculos", Vehiculos);
    bundle1.putString("correo", correo);
    bundle1.putString("nombre", nombreUsuario);
    bundle1.putString("cl_codigo", codigoUsuario);
    bundle1.putString("tipoDoc",tipoDoc);
    bundle1.putString("cedula",l);
    intent.putExtras(bundle1);
    startActivity(intent);
    finish();
}
    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }
    public void obtenerDato()throws IOException,JSONException{
        ob=new cls_conexion(getString(R.string.servidor_tramas), 8200);
        tipoDoc= getIntent().getStringExtra("tipoDoc");
        l= getIntent().getStringExtra("cedula");
        codigoUsuario=getIntent().getStringExtra("cl_codigo");
        codigoVehiculo=getIntent().getStringExtra("cut_codigo");
        nombreUsuario=getIntent().getStringExtra("nombre");
        correo=getIntent().getStringExtra("correo");
        Placa=getIntent().getStringExtra("placa");
        Vehiculos=getIntent().getStringExtra("Vehiculos");
        numTag=getIntent().getStringExtra("numTag");
        cut_propietario=getIntent().getStringExtra("cut_propietario");
        textInfoVehi.setText("YO,"+nombreUsuario+" deseo dar de baja al "+numTag+" pertenieciente al vehiculo con placa "+Placa);

    }
}
