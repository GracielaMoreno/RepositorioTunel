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
import android.text.method.TextKeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import Modelo.Bancos;
import Modelo.Pasadas;
import Modelo.Tarjettas;

public class Actu_debi_auto_camb extends AppCompatActivity {
    TextView var_texto;
    TextView var_texto1;
    TextView var_texto2;
    TextView var_texto3;
    Spinner var_spnTipoCuen;
    Spinner var_spnTipoTarj;
    Spinner var_spnMes;
    Spinner var_spnAnio;
    Spinner var_spnBanco;
    Spinner var_spnBancTarj;
    Spinner var_spnTarjeta;
    Spinner var_spnPasadas;
    Spinner var_spnPasadas2;
    TabHost var_th;
    ScrollView pepito;
    EditText var_txtCuenta;
    EditText var_txtCuentaBan;
    EditText var_codigo;
    InputMethodManager imm;
    private String opcion="1";
    private String cut_codigo="";
    private String cl_codigo="";
    boolean valid = false;
    private String[] vectorBancos;
    private String[] vectorTrajetas;
    private String[] vectorPasadas;
    private String nombreUsuario="";
    String Vehiculos;
    String correo;
    private cls_conexion ob;
    String cod_banco;
    String cod_numPasadas;
    String cod_numPasadas2;
    String cod_tarj;
    String cod_bancoTARJ;
    String tipoDoc;
    String l;
    String Bancos;
    String Tarjetas;
    String Pasadas;
    String[] lenguajes;
    String[] mes;
    String[] anio;
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_actu_debi_auto_camb);
        try{
            if (isOnline(this) == true) {
        var_texto = (TextView) findViewById(R.id.lblNota1);
        var_texto1 = (TextView) findViewById(R.id.lblNota4);
        var_texto2 = (TextView) findViewById(R.id.lblNota8);
        var_texto3 = (TextView) findViewById(R.id.lblNota15);
        var_txtCuenta = (EditText) findViewById(R.id.txtCuenta);
        var_txtCuentaBan = (EditText) findViewById(R.id.txtCuenBanc);

        var_th = (TabHost) findViewById(R.id.th);
        var_spnTipoCuen = (Spinner) findViewById(R.id.spn_tipo_cuen);
        var_spnTipoTarj = (Spinner) findViewById(R.id.spn_tipo_tarj);
        var_spnMes = (Spinner) findViewById(R.id.spn_mes);
        var_spnAnio = (Spinner) findViewById(R.id.spn_anio);
        var_spnTarjeta = (Spinner) findViewById(R.id.spn_tipo_tarj);
        var_spnBanco = (Spinner) findViewById(R.id.spn_banco);
        var_spnBancTarj = (Spinner) findViewById(R.id.spn_banc_tarj);
        var_spnPasadas = (Spinner) findViewById(R.id.spn_pasadas);
        var_spnPasadas2 = (Spinner) findViewById(R.id.spn_pasadas2);

        try {
            llenar_spinner();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        var_texto.setText("Con el propósito de recargar mi tag, Yo " + nombreUsuario + " autorizo a la EPMMOP, a ordenar en mi nombre, el débito de mi Tarjeta de Crédito detallada a continuación.");
        var_texto1.setText("Con el propósito de recargar mi tag, Yo " + nombreUsuario + " autorizo a la EPMMOP, a ordenar en mi nombre, el débito de mi Cuenta Bancaria detallada a continuación.");
        var_texto2.setText("Este débito se realizará cada vez que su saldo sea menor a 20 pasadas y la factura electrónica generada llegará a su correo " + correo);
        var_texto3.setText("Este débito se realizará cada vez que su saldo sea menor a 20 pasadas y la factura electrónica generada llegará a su correo " + correo);

        var_th.setup();
        TabHost.TabSpec TS1 = var_th.newTabSpec("Tab1");
        TS1.setIndicator("Tarjeta de Crédito");
        TS1.setContent(R.id.tab1);
        var_th.addTab(TS1);


        TabHost.TabSpec TS2 = var_th.newTabSpec("Tab2");
        TS2.setIndicator("Cuenta Bancaria");
        TS2.setContent(R.id.tab2);
        var_th.addTab(TS2);

        var_th.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                opcion = tabId;
                Log.i("AndroidTabsDemo", "Pulsada pestaña: " + tabId);
            }
        });


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
    }else{
        Toast.makeText(this, R.string.g_error_internet, Toast.LENGTH_LONG).show();
    }}catch (Exception e){
        Toast.makeText(this, R.string.g_error_global, Toast.LENGTH_LONG).show();
        }
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

    public void btn_continuar(View view) throws IOException, JSONException{

            try {
                seleccionarTarjetaCredito();
            }catch (IOException e){
                e.printStackTrace();
            }


    }
    public void btn_continuar2(View view) throws IOException, JSONException{

        try {
                seleccionarCuentaBancaria();
        }catch (IOException e){
            e.printStackTrace();
        }

    }
    public void seleccionarTarjetaCredito()  throws IOException, JSONException {

        try{
            if (isOnline(this) == true) {
        String mes = var_spnMes.getSelectedItem().toString();
        String anio = var_spnAnio.getSelectedItem().toString();
        String numTarjeta = var_txtCuenta.getText().toString();


        if (numTarjeta.equals("")) {
            Toast.makeText(this, "Todos los campos son obligatorios, por favor ingrese sus datos ", Toast.LENGTH_LONG).show();
        } else {

            String TramaEnviar = getString(R.string.cm_auto_debi_auto_camb_credito) + "," + cl_codigo + "," + cut_codigo + "," + cod_bancoTARJ + "," + cod_tarj + "," + mes + "," + anio + "," + numTarjeta + "," + cod_numPasadas;
            //TextView mensaje = (TextView) findViewById(R.id.lblMensaje);
            Log.e("tarjeta", TramaEnviar);
            ob.conectar();
            ob.enviar(TramaEnviar);
            ob.cerrar();
            String TramaRecida = ob.cadena.toString();

            //String TramaRecida = "1|mensaje";
            //String TramaRecida = "1|Tiene valores pendientes|205*206*207|$96.8|$80|$15|$1.81|12%|205-123456789-PBC6530-Recarga-$80-$0.00-2020/03/12-006_021_000005896-CHEVROLET AVEO ROJO*206-123456789-PBC6530-Recarga-$80-$0.00-2020/03/12-006_021_000005896-CHEVROLET AVEO ROJO*206-123456789-PBC6530-Tag-$15-$1.80-2020/03/12-006_021_000005896-CHEVROLET AVEO ROJO";
            String[] vector_respuesta = TramaRecida.split("\\|");
            String codigoRespuesta = vector_respuesta[0];
            String Mensaje;
            if (codigoRespuesta.equals("1")) {
                Mensaje = vector_respuesta[1];
                AlertDialog.Builder builder = new AlertDialog.Builder(Actu_debi_auto_camb.this, R.style.CustomDialog);
                builder.setTitle("Informativo");
                builder.setMessage(Mensaje)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                Intent intent = new Intent(Actu_debi_auto_camb.this, Menu.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                Bundle bundle1 = new Bundle();
                                bundle1.putString("Vehiculos", Vehiculos);
                                bundle1.putString("correo", correo);
                                bundle1.putString("nombre", nombreUsuario);
                                bundle1.putString("cl_codigo", cl_codigo);
                                bundle1.putString("cedula", l);
                                bundle1.putString("tipoDoc", tipoDoc);
                                intent.putExtras(bundle1);
                                startActivity(intent);
                                finish();

                            }
                        });
                builder.create().show();
            }
            if (codigoRespuesta.equals("2")) {

                if (vector_respuesta.length == 2) {
                    Mensaje = vector_respuesta[1];
                    Toast.makeText(this, Mensaje, Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(this, getString(R.string.g_error_servidor), Toast.LENGTH_LONG).show();
                }
            }

        }

        {

}  }else{
                Toast.makeText(this, R.string.g_error_internet, Toast.LENGTH_LONG).show();
            }}catch (Exception e){
            Toast.makeText(this, R.string.g_error_global, Toast.LENGTH_LONG).show();
        }
    }

    public void seleccionarCuentaBancaria() throws IOException, JSONException {
        try{
            if (isOnline(this) == true) {
        String tipoCuenta = var_spnTipoCuen.getSelectedItem().toString();
        String numCuenta = var_txtCuentaBan.getText().toString();

        if (numCuenta.equals("")) {
            Toast.makeText(this, "Todos los campos son obligatorios, por favor ingrese sus datos", Toast.LENGTH_LONG).show();

        } else {

            String TramaEnviar = getString(R.string.cm_auto_debi_auto_camb_cuenta) + "," + cl_codigo + "," + cut_codigo + "," + cod_banco + "," + tipoCuenta+ "," + numCuenta + "," + cod_numPasadas2;

            ob.conectar();
            ob.enviar(TramaEnviar);
            ob.cerrar();
            String TramaRecida = ob.cadena.toString();
            Log.e("cuenta", TramaEnviar);
           // String TramaRecida = "1|mensaje";
            String[] vector_respuesta = TramaRecida.split("\\|");
            String codigoRespuesta = vector_respuesta[0];
            String Mensaje;
            if (codigoRespuesta.equals("1")) {
                Mensaje = vector_respuesta[1];
                AlertDialog.Builder builder = new AlertDialog.Builder(Actu_debi_auto_camb.this,R.style.CustomDialog);
                builder.setTitle("Informativo");
                builder.setMessage(Mensaje)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                Intent intent = new Intent(Actu_debi_auto_camb.this, Menu.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                Bundle bundle1 = new Bundle();
                                bundle1.putString("Vehiculos", Vehiculos);
                                bundle1.putString("correo", correo);
                                bundle1.putString("nombre", nombreUsuario);
                                bundle1.putString("cl_codigo", cl_codigo);
                                bundle1.putString("cedula",l);
                                bundle1.putString("tipoDoc",tipoDoc);
                                intent.putExtras(bundle1);
                                startActivity(intent);
                                finish();

                            }
                        });
                builder.create().show();
            }
            if (codigoRespuesta.equals("2")) {

                if (vector_respuesta.length == 2) {
                    Mensaje = vector_respuesta[1];
                    Toast.makeText(this, Mensaje, Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(this, getString(R.string.g_error_servidor), Toast.LENGTH_LONG).show();
                }
            }
        }
            }else{
                Toast.makeText(this, R.string.g_error_internet, Toast.LENGTH_LONG).show();
            }}catch (Exception e){
            Toast.makeText(this, R.string.g_error_global, Toast.LENGTH_LONG).show();
        }
    }

    public void llenar_spinner()throws IOException,JSONException{
        ob = new cls_conexion(getString(R.string.servidor_tramas), 8200);
        cl_codigo = getIntent().getStringExtra("cl_codigo");
        correo = getIntent().getStringExtra("correo");
        cut_codigo = getIntent().getStringExtra("cut_codigo");
        nombreUsuario = getIntent().getStringExtra("nombre");
        Bancos = getIntent().getStringExtra("Bancos");
        Tarjetas = getIntent().getStringExtra("Tarjetas");
        Pasadas = getIntent().getStringExtra("Pasadas");
        Vehiculos = getIntent().getStringExtra("Vehiculos");
        tipoDoc = getIntent().getStringExtra("tipoDoc");
        l = getIntent().getStringExtra("cedula");
        lenguajes = new String[]{"Corriente", "Ahorros"};
        mes = new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
        anio = new String[]{"2019", "2020", "2021", "2022", "2023", "2024", "2025", "2026", "2027"};
        var_spnMes.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item_global, mes));
        var_spnAnio.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item_global, anio));
        var_spnTipoCuen.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item_global, lenguajes));
        vectorBancos = Bancos.split("\\*");
        vectorTrajetas = Tarjetas.split("\\*");
        vectorPasadas = Pasadas.split("\\*");
Log.e("pasadas",Pasadas);
        final ArrayList<Modelo.Pasadas> list = new ArrayList<Modelo.Pasadas>();
        String vectorpasada[];
        if (vectorPasadas.length > 0) {
            for (Integer i = 0; i < (vectorPasadas.length); i++) {
                vectorpasada = vectorPasadas[i].split("\\?");
                if (vectorpasada.length > 0) {
                    list.add(new Pasadas(vectorpasada[0], vectorpasada[1], vectorpasada[2]));
                }

            }
            ArrayAdapter<Modelo.Pasadas> adapter = new ArrayAdapter<Modelo.Pasadas>(this, R.layout.spinner_item_global, list);
            var_spnPasadas.setAdapter(adapter);
            var_spnPasadas2.setAdapter(adapter);
        }

        var_spnPasadas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String p = var_spnPasadas.getItemAtPosition(position).toString();
                cod_numPasadas = list.get(position).getCodigo().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        var_spnPasadas2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String p = var_spnPasadas2.getItemAtPosition(position).toString();
                cod_numPasadas2 = list.get(position).getCodigo().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        final ArrayList<Tarjettas> listTarj = new ArrayList<Tarjettas>();
        String vectortarjeta[];
        if (vectorTrajetas.length > 0) {
            for (Integer i = 0; i < (vectorTrajetas.length); i++) {
                vectortarjeta = vectorTrajetas[i].split("\\?");
                if (vectortarjeta.length > 0) {
                    listTarj.add(new Tarjettas(vectortarjeta[0], vectortarjeta[1]));
                }

            }
            ArrayAdapter<Tarjettas> adapterTarj = new ArrayAdapter<Tarjettas>(this, R.layout.spinner_item_global, listTarj);
            var_spnTarjeta.setAdapter(adapterTarj);
        }

        var_spnTarjeta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String p = var_spnTarjeta.getItemAtPosition(position).toString();
                cod_tarj = listTarj.get(position).getCod_tarj().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        final ArrayList<Modelo.Bancos> listBancos = new ArrayList<Modelo.Bancos>();
        String vectorbanco[];
        if (vectorBancos.length > 0) {
            for (Integer i = 0; i < (vectorBancos.length); i++) {
                vectorbanco = vectorBancos[i].split("\\?");
                if (vectorbanco.length > 0) {
                    listBancos.add(new Bancos(vectorbanco[0], vectorbanco[1]));
                }

            }
            ArrayAdapter<Modelo.Bancos> adapterbanco = new ArrayAdapter<Modelo.Bancos>(this, R.layout.spinner_item_global, listBancos);
            var_spnBanco.setAdapter(adapterbanco);
            var_spnBancTarj.setAdapter(adapterbanco);
        }

        var_spnBanco.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String p = var_spnBanco.getItemAtPosition(position).toString();
                cod_banco = listBancos.get(position).getCod_banco().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        var_spnBancTarj.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String p = var_spnBancTarj.getItemAtPosition(position).toString();
                cod_bancoTARJ = listBancos.get(position).getCod_banco().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    public void navegacion()throws IOException,JSONException{
        Intent intent = new Intent(Actu_debi_auto_camb.this, Menu.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        Bundle bundle1=new Bundle();
        bundle1.putString("Vehiculos", Vehiculos);
        bundle1.putString("correo", correo);
        bundle1.putString("nombre", nombreUsuario);
        bundle1.putString("cl_codigo", cl_codigo);
        bundle1.putString("cedula",l);
        bundle1.putString("tipoDoc",tipoDoc);
        intent.putExtras(bundle1);
        startActivity(intent);
        finish();
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }
}
