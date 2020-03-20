package com.example.cheli.tunelapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.http.OPTIONS;

public class Actu_traj extends AppCompatActivity {
    TextView var_texto;
    TextView var_texto1;
    RadioButton var_rbtnTarjCred;
    RadioButton var_rbtnCuenBanc;
    Spinner var_spnTipoCuen;
    Spinner var_spnTipoTarj;
    Spinner var_spnMes;
    Spinner var_spnAnio;
    Spinner var_spnBanco;
    Spinner var_spnBancTarj;
    Spinner var_spnTarjeta;
    Spinner var_spnPasadas;
    TabHost var_th;
    ScrollView pepito;
    EditText var_txtCuenta;
    EditText var_txtCuentaBan;
    InputMethodManager imm;
    private String opcion="1";
    private String cut_codigo="";
    private String cl_codigo="";
    boolean valid = false;
    private String[] vectorBancos;
    private String[] vectorTrajetas;
    private String[] vectorPasadas;
    private String nombreUsuario="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actu_traj);
        cl_codigo=getIntent().getStringExtra("cl_codigo");
        cut_codigo=getIntent().getStringExtra("cut_codigo");
        nombreUsuario=getIntent().getStringExtra("nombre");
        String Bancos=getIntent().getStringExtra("Bancos");
        String Tarjetas=getIntent().getStringExtra("Tarjetas");
        String Pasadas=getIntent().getStringExtra("Pasadas");
        vectorBancos=Bancos.split("-");
        vectorTrajetas=Tarjetas.split("-");
        vectorPasadas=Pasadas.split("-");
        String[] lenguajes = {"Cuenta Corriente","Cuenta de Ahorros"};
        String[] mes = {"01","02","03","04","05","06","07","08","09","10","11","12"};
        String[] anio = {"2019","2020","2021","2022","2023","2024","2025","2026","2027"};
        var_texto=(TextView)findViewById(R.id.lblNota1);
        var_texto1=(TextView)findViewById(R.id.lblNota4);
        var_txtCuenta=(EditText) findViewById(R.id.txtCuenta);
        var_txtCuentaBan=(EditText) findViewById(R.id.txtCuenBanc);

        var_th =(TabHost) findViewById(R.id.th);
        var_spnTipoCuen =(Spinner) findViewById(R.id.spn_tipo_cuen);
        var_spnTipoTarj =(Spinner) findViewById(R.id.spn_tipo_tarj);
        var_spnMes =(Spinner) findViewById(R.id.spn_mes);
        var_spnAnio =(Spinner) findViewById(R.id.spn_anio);
        var_spnTarjeta =(Spinner) findViewById(R.id.spn_tipo_tarj);
        var_spnBanco =(Spinner) findViewById(R.id.spn_banco);
        var_spnBancTarj =(Spinner) findViewById(R.id.spn_banc_tarj);
        var_spnPasadas =(Spinner) findViewById(R.id.spn_pasadas);


        var_spnTarjeta.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item_global,vectorTrajetas));
        var_spnMes.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item_global,mes));
        var_spnAnio.setAdapter(new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_item_global,anio));
        var_spnTipoCuen.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item_global,lenguajes));
        var_spnBanco.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item_global,vectorBancos));
        var_spnBancTarj.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item_global,vectorBancos));
        var_spnPasadas.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item_global,vectorPasadas));

        var_texto.setText("Con el propósito de recargar mi tag, Yo "+nombreUsuario+" autorizo a la EPMMOP, a ordenar en mi nombre, el débito de mi Tarjeta de Crédito detallada a continuación.");
        var_texto1.setText("Con el propósito de recargar mi tag, Yo "+nombreUsuario+" autorizo a la EPMMOP, a ordenar en mi nombre, el débito de mi Cuenta Bancaria detallada a continuación.");

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
                opcion=tabId;
                Log.i("AndroidTabsDemo", "Pulsada pestaña: " + tabId);
            }
        });


        ImageView img = (ImageView) findViewById(R.id.img_regresar);
        img.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent intent = new Intent( Actu_traj.this, List_tags.class );
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                finish();
            }
        });


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Intent intent = new Intent( Actu_traj.this, List_tags.class );
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void btn_continuar(View view){
        Log.e("error",var_txtCuentaBan.getText().toString());
        Log.e("error",var_txtCuenta.getText().toString());
       if (opcion == "1"){

           seleccionarTarjetaCredito();

       }else{
            seleccionarCuentaBancaria();
        }
    }

    public void seleccionarTarjetaCredito() {
        String text1 = var_spnTipoTarj.getSelectedItem().toString();
        String text2 = var_spnBancTarj.getSelectedItem().toString();
        String text3 = var_spnMes.getSelectedItem().toString();
        String text4 = var_spnAnio.getSelectedItem().toString();
        String text5 = var_txtCuenta.getText().toString();
        String text6 = var_spnPasadas.getSelectedItem().toString();
        if(text5.equals("")){
            Toast.makeText(this, "Todos los campos son obligatorios, por favor ingrese sus datos", Toast.LENGTH_LONG).show();

        }else {

            String TramaEnviar = "4," + cl_codigo + "," + cut_codigo + "," + text1 + "," + text2 + "," + text3 + "," + text4 + "," + text5 + "," + text6;
            TextView mensaje = (TextView) findViewById(R.id.lblMensaje);
            Toast.makeText(this, "Cambio de forma de pago realizado satisfactoriamente.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Actu_traj.this, List_tags.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            finish();
            Log.e("TARJETA DE CREDITO", TramaEnviar);
        }


    }
    public void seleccionarCuentaBancaria(){

        String text1 = var_spnTipoCuen.getSelectedItem().toString();
        String text2 = var_spnBanco.getSelectedItem().toString();
        String text4 = var_txtCuentaBan.getText().toString();
        String text3 = var_spnPasadas.getSelectedItem().toString();


        if(text4.equals("")){
            Toast.makeText(this, "Todos los campos son obligatorios, por favor ingrese sus datos", Toast.LENGTH_LONG).show();

        }else {

            String TramaEnviar = "3," + cl_codigo + "," + cut_codigo + "," + text1 + "," + text2 + "," + text4 + "," + text3;
            TextView mensaje = (TextView) findViewById(R.id.lblMensaje);
            Toast.makeText(this, "Cambio de forma de pago realizado satisfactoriamente.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Actu_traj.this, List_tags.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            finish();
            Log.e("CUENTA BANCARIA", TramaEnviar);

        }
    }


}
