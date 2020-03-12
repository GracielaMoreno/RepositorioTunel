package com.example.cheli.tunelapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.ClickableSpan;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    TextView indicacion1;
    TextView indicacion2;
    private cls_conexion ob;
    EditText txtLogin,txtPass;
    CheckBox chkRecoCred;
    TextView lblUsuario;
    TextView lblContrasenia;

    ImageButton btnAdmit;
    String envia="";
    String l="";
    String p="";
    String vehiculos = "";
    private String[] vectorUrbanizacion;
    public static final String SHARED_PREFS = "N/A";
    private int seleccion = 0;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        indicacion1 = (TextView)findViewById(R.id.lblIndicacion1);
        indicacion2 = (TextView)findViewById(R.id.lblIndicacion2);

        String content = "Si ya es cliente del Telepeaje, utilice la clave del sistema Web para ingresar a esta app. Si olvidó la clave, ";
        indicacion1.setText(content);
        SpannableString link = makeLinkSpan("solicítela aquí", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //---------------------------------Intent i = new Intent(Login.this, Soli_clav.class );
                Intent i = new Intent(Login.this, Portada.class );
                startActivity(i);
                finish();
            }
        });
        indicacion1.append(link);
        makeLinksFocusable(indicacion1);

        String content2 = "Si no dispone de tag o requiere otro, ";
        indicacion2.setText(content2);
        SpannableString link2 = makeLinkSpan("solicítelo aquí", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //--------------------------------Intent i = new Intent(Login.this, Soli_tag.class );
                Intent i = new Intent(Login.this, Portada.class );
                startActivity(i);
                finish();
            }
        });
        indicacion2.append(link2);
        makeLinksFocusable(indicacion2);

        //Instrucción Para permitir Escucha con Proxy
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
        ob=new cls_conexion(getString(R.string.servidor_tramas), 7901);

        txtLogin = (EditText)findViewById(R.id.txtLogin);
        txtPass = (EditText)findViewById(R.id.txtPass);
        chkRecoCred = (CheckBox)findViewById(R.id.chk_reco_cred);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            // Esto es lo que hace mi botón al pulsar ir a atrás
            //Toast.makeText(this, "Quiero salir", Toast.LENGTH_LONG).show();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Telepeaje");
            builder.setMessage("Desea salir del aplicativo?");
            builder.setPositiveButton("SALIR", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    int p = android.os.Process.myPid();
                    android.os.Process.killProcess(p);
                }
            });
            builder.setNegativeButton("CANCELAR", null);
            Dialog dialog = builder.create();
            dialog.show();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    private void makeLinksFocusable(TextView tv) {
        MovementMethod m = tv.getMovementMethod();
        if ((m == null) || !(m instanceof LinkMovementMethod)) {
            if (tv.getLinksClickable()) {
                tv.setMovementMethod(LinkMovementMethod.getInstance());
            }
        }
    }

    private SpannableString makeLinkSpan(CharSequence text, View.OnClickListener listener)                 {
        SpannableString link = new SpannableString(text);
        link.setSpan(new ClickableString(listener), 0, text.length(),
                SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
        return link;
    }

    private static class ClickableString extends ClickableSpan {
        private View.OnClickListener mListener;
        public ClickableString(View.OnClickListener listener) {
            mListener = listener;
        }
        @Override
        public void onClick(View v) {
            mListener.onClick(v);
        }
    }

    //borra campos de ingreso de credenciales
    public void limpiar(View view){
        txtLogin.setText("");
        txtPass.setText("");
        chkRecoCred.setChecked(false);
    }

    //solo cuando sabe usuario y clave desde el boton ingresar
    public void ingresar(View view){
        String CodigoResp;
        String Mensaje;
        String Usuario;
        String CodigoUsuario;
        String TramaRecida;

        l=txtLogin.getText().toString();
        p=txtPass.getText().toString();
        if (l.equals("") || p.equals("")){
            Toast.makeText(this, "Todos los campos son obligatorios, por favor ingrese sus datos", Toast.LENGTH_LONG).show();
        }
        else{
            //Enviar y Recibir Trama de Login
            envia= "0," + l + "," + p + ",F";
            //ob.conectar();
            //ob.enviar(envia);
            //ob.cerrar();

            //TramaRecida = ob.cadena.toString();
            TramaRecida = "1|Bienvenido|Ximena Tapia|100|1-PTJ560-1210123456-CHEVROLET AVEO 2012-20*2-PTR2589-1210123457-KIA SPORTAGE R2 2013-10*3-IBQ2563-1210123458-KIA SORENTE 2018-24*4-TZA0987-1210123458-KIA SORENTE 2018-24*5-PTJ565-1210123458-KIA SORENTE 2018-24*6-PTJ566-1210123458-KIA SORENTE 2018-24*7-PTJ567-1210123458-KIA SORENTE 2018-24*8-PTJ568-1210123458-KIA SORENTE 2018-24*9-PTJ569-1210123458-KIA SORENTE 2018-24*10-PTJ510-1210123458-KIA SORENTE 2018-24";

            String[] vectorTramaRecibida = TramaRecida.split("\\|");
            CodigoResp=vectorTramaRecibida[0];
            Mensaje=vectorTramaRecibida[1];
            Usuario=vectorTramaRecibida[2];
            CodigoUsuario=vectorTramaRecibida[3];
            vehiculos = vectorTramaRecibida[4];

            Toast.makeText(this, Mensaje + " " + Usuario, Toast.LENGTH_LONG).show();

            //usuario con una sola urbanizaion con esas credenciales
            if (CodigoResp.equals("1")){
                //usuario selecciona guardar credenciales
                //if (chkRecoCred.isChecked()){
                //  guardar(view);
                //}
                limpiar(view);
                //verificar(CodigoUsuario, Usuario);
                ver_list_tags(CodigoUsuario, Usuario, vehiculos);
            }
        }
    }

    //boton de acceso a la urbanizacion 1
    //boton de acceso a la urbanizacion 2
    //boton de acceso a la urbanizacion 3
    //boton de acceso a la urbanizacion 4

    public void ver_list_tags(String codigoUsuario, String nombreUsuario, String vehiculos)
    {
        Bundle bundle = new Bundle();
        bundle.putString("CodigoUsuario" ,codigoUsuario);
        bundle.putString("NombreUsuario" ,nombreUsuario);
        bundle.putString("Vehiculos" ,vehiculos);
        //------------------------Intent i = new Intent(Login.this, List_tags.class );
        Intent i = new Intent(Login.this, Portada.class );
        i.putExtras(bundle);
        startActivity(i);
        finish();
    }

}
