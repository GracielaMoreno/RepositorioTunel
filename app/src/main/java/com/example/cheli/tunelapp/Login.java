package com.example.cheli.tunelapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

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
    String Valor= "";
    private String[] vectorUrbanizacion;
    public static final String SHARED_PREFS = "N/A";
    private int seleccion = 0;
    String correo="";
    String Mensaje;
    String Usuario;
    Dialog dialog;
    String tipoDoc;
String passEncrip;
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_login);
        validarPermiso();
        //dialog.dismiss();
        try{
            if (isOnline(this) == true) {
        indicacion1 = (TextView)findViewById(R.id.lblIndicacion1);
        indicacion2 = (TextView)findViewById(R.id.lblIndicacion2);
        chkRecoCred = (CheckBox)findViewById(R.id.chk_reco_cred);
        String content = "Si ya es cliente del Telepeaje, utilice la clave del sistema Web para ingresar a esta app. Si olvidó la clave, ";
        indicacion1.setText(content);
        SpannableString btn_soli_cont = makeLinkSpan("solicítela aquí", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Soli_cont();

            }
        });
        indicacion1.append(btn_soli_cont);
        makeLinksFocusable(indicacion1);
        String content2 = "Si no dispone de tag o requiere otro, ";
        indicacion2.setText(content2);
        SpannableString btn_soli_tag = makeLinkSpan("solicítelo aquí", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Login.this,R.style.CustomDialog);
                builder.setTitle("Solicitud de Tag");
                builder.setMessage("Si realizó una solicitud y desea conocer el estado puede comunicarse a 022480374, 022460000 o 022928155 o al correo telepeaje-tunel-guayasamin@procelec.ec");
                builder.setPositiveButton("CONTINUAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Bundle bundle = new Bundle();
                        Intent i = new Intent(Login.this, Paso_Soli_Tag_No_Cli.class );
                        i.putExtras(bundle);
                        startActivity(i);
                        finish();
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("CANCELAR", null);
                dialog=builder.create();
                dialog.show();

            }
        });
        indicacion2.append(btn_soli_tag);
        makeLinksFocusable(indicacion2);

        //Instrucción Para permitir Escucha con Proxy
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
        ob=new cls_conexion(getString(R.string.servidor_tramas), 8200);

        txtLogin = (EditText)findViewById(R.id.txtLogin);
        txtPass = (EditText)findViewById(R.id.txtPass);
        chkRecoCred = (CheckBox)findViewById(R.id.chk_reco_cred);
        cargarCredenciales();
            }else{
                Toast.makeText(this, R.string.g_error_internet, Toast.LENGTH_LONG).show();
            }}catch (Exception e){
            Toast.makeText(this, R.string.g_error_global, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.CustomDialog);
            // Esto es lo que hace mi botón al pulsar ir a atrás
            //Toast.makeText(this, "Quiero salir", Toast.LENGTH_LONG).show();
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
            dialog=builder.create();
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

    private SpannableString makeLinkSpan(CharSequence text, View.OnClickListener listener){
        SpannableString link = new SpannableString(text);
        link.setSpan(new ClickableString(listener), 0, text.length(),
                SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
        return link;
    }

    static class ClickableString extends ClickableSpan {
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
    public void btn_enviar(View view){
        try {

        String CodigoResp;

        String CodigoUsuario;
        String TramaRecida;


        l=txtLogin.getText().toString();
        p=txtPass.getText().toString();
        if (l.equals("") || p.equals("")){
            Toast.makeText(this, "Todos los campos son obligatorios, por favor ingrese sus datos", Toast.LENGTH_LONG).show();
        }
        else {
            //Enviar y Recibir Trama de Login
            if (isOnline(this) == true) {
                Encriptar(p);

                envia = getString(R.string.cm_login_menu_ingreso) + "," + l + "," + passEncrip;
                ob.conectar();
                ob.enviar(envia);
                ob.cerrar();

                TramaRecida = ob.cadena.toString();

                String[] vectorTramaRecibida = TramaRecida.split("\\|");
                CodigoResp = vectorTramaRecibida[0];
                if (CodigoResp.equals("")) {
                    Toast.makeText(this, R.string.g_error_servidor, Toast.LENGTH_LONG).show();
                } else {
                    //usuario con una sola urbanizaion con esas credenciales
                    if (chkRecoCred.isChecked()) {
                        guardar_credenciales(l, p);
                    }
                    if (CodigoResp.equals("1")) {
                        if (vectorTramaRecibida.length == 8) {
                            Mensaje = vectorTramaRecibida[1];
                            Valor = vectorTramaRecibida[2];
                            CodigoUsuario = vectorTramaRecibida[3];
                            tipoDoc = vectorTramaRecibida[4];
                            Usuario = vectorTramaRecibida[5];
                            correo = vectorTramaRecibida[6];
                            vehiculos = vectorTramaRecibida[7];
                            Toast.makeText(this, Mensaje + " " + Usuario, Toast.LENGTH_LONG).show();
                            ver_list_tags(CodigoUsuario, Usuario, vehiculos);
                        } else {
                            Toast.makeText(this, R.string.g_error_servidor, Toast.LENGTH_LONG).show();
                        }
                    }
                    if (CodigoResp.equals("2")) {
                        if (vectorTramaRecibida.length == 8) {
                            Mensaje = vectorTramaRecibida[1];
                            Valor = vectorTramaRecibida[2];
                            CodigoUsuario = vectorTramaRecibida[3];
                            tipoDoc = vectorTramaRecibida[4];
                            Usuario = vectorTramaRecibida[5];
                            correo = vectorTramaRecibida[6];
                            vehiculos = vectorTramaRecibida[7];
                            Toast.makeText(this, Mensaje, Toast.LENGTH_LONG).show();
                            envia = getString(R.string.cm_login_menu_list_pagos) + "," + CodigoUsuario;
                            ob.conectar();
                            ob.enviar(envia);
                            ob.cerrar();
                            TramaRecida = ob.cadena.toString();
                            listPagos(TramaRecida, CodigoUsuario);


                        } else {
                            Toast.makeText(this, R.string.g_error_servidor, Toast.LENGTH_LONG).show();
                        }
                    }
                    if (CodigoResp.equals("3")) {
                        if (vectorTramaRecibida.length == 8) {
                            Mensaje = vectorTramaRecibida[1];
                            Valor = vectorTramaRecibida[2];
                            CodigoUsuario = vectorTramaRecibida[3];
                            tipoDoc = vectorTramaRecibida[4];
                            Usuario = vectorTramaRecibida[5];
                            correo = vectorTramaRecibida[6];
                            vehiculos = vectorTramaRecibida[7];
                            Toast.makeText(this, Mensaje, Toast.LENGTH_LONG).show();
                            envia = getString(R.string.cm_login_menu_list_pagos) + "," + CodigoUsuario;
                            ob.conectar();
                            ob.enviar(envia);
                            ob.cerrar();
                            TramaRecida = ob.cadena.toString();
                            listPagos(TramaRecida, CodigoUsuario);


                        } else {
                            Toast.makeText(this, R.string.g_error_servidor, Toast.LENGTH_LONG).show();
                        }
                    }
                    if (CodigoResp.equals("4")) {
                        if (vectorTramaRecibida.length == 2) {
                            Mensaje = vectorTramaRecibida[1];
                            Toast.makeText(this, Mensaje, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(this, R.string.g_error_servidor, Toast.LENGTH_LONG).show();
                        }
                    }
                    if (CodigoResp.equals("5")) {
                        if (vectorTramaRecibida.length == 2) {
                            Mensaje = vectorTramaRecibida[1];
                            Toast.makeText(this, Mensaje, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(this, R.string.g_error_servidor, Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }else{
                Toast.makeText(this, R.string.g_error_internet, Toast.LENGTH_LONG).show();
            }
        }}catch (Exception e)
        {
            Toast.makeText(this, R.string.g_error_global, Toast.LENGTH_LONG).show();
        }
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }

    public void ver_list_tags(String codigoUsuario, String nombreUsuario, String vehiculos) {
        try{
        Bundle bundle = new Bundle();
        bundle.putString("cl_codigo" ,codigoUsuario);
        bundle.putString("nombre" ,nombreUsuario);
        bundle.putString("Vehiculos" ,vehiculos);
        bundle.putString("correo",correo);
        bundle.putString("tipoDoc",tipoDoc);
        bundle.putString("cedula",l);

        Intent i = new Intent(Login.this, Menu.class );
        i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        i.putExtras(bundle);
        startActivity(i);
        finish();
        }catch (Exception e){
            Toast.makeText(this, R.string.g_error_global, Toast.LENGTH_LONG).show();
        }
    }

    public void Soli_cont() {
        try {
            if (isOnline(this) == true) {
                AlertDialog.Builder mbuilder = new AlertDialog.Builder(Login.this, R.style.CustomDialog);
                View mView = getLayoutInflater().inflate(R.layout.soli_cont, null);

                mbuilder.setMessage("Ingrese cedula o RUC");
                mbuilder.setTitle("Solicitud de Clave");
                mbuilder.setNegativeButton("Salir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                final EditText editCedula = (EditText) mView.findViewById(R.id.CedulaSoli);
                Button btnsolicitar = (Button) mView.findViewById(R.id.btnSoliClave);
                btnsolicitar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (editCedula.getText().toString().equals("")) {
                            Toast.makeText(getApplicationContext(), "Todos los campos son obligatorios, por favor ingrese sus datos", Toast.LENGTH_LONG).show();

                        } else {
//                  String enviar=cedula.getText().toString();
                            String cedula = editCedula.getText().toString();
                            envia = getString(R.string.cm_soli_cont) + "," + cedula;
                            ob.conectar();
                            ob.enviar(envia);
                            ob.cerrar();
                            String TramaRecibida = ob.cadena.toString();
                            // String TramaRecibida="1|Su contraseña ha sido enviada a al correo registrado xtapia@procelec.ec";
                            //String TramaRecibida="1|Cliente no existe";

                            String vectorRecibido[] = TramaRecibida.split("\\|");
                            String codRespuesta = vectorRecibido[0];
                            if (vectorRecibido.length==2){
                            if (codRespuesta.equals("1")) {
                                Mensaje = vectorRecibido[1];
                                dialog.dismiss();
                                AlertDialog.Builder builder = new AlertDialog.Builder(Login.this, R.style.CustomDialog);
                                builder.setTitle("Informativo");
                                builder.setMessage(Mensaje)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {

                                            }
                                        });
                                builder.create().show();
                            }
                            if (codRespuesta.equals("2")) {
                                Mensaje = vectorRecibido[1];
                                Toast.makeText(getApplicationContext(), Mensaje, Toast.LENGTH_LONG).show();
                            }
                            if (codRespuesta.equals("3")) {
                                Mensaje = vectorRecibido[1];
                                Toast.makeText(getApplicationContext(), Mensaje, Toast.LENGTH_LONG).show();
                            }
                            if (codRespuesta.equals("4")) {
                                Mensaje = vectorRecibido[1];
                                Toast.makeText(getApplicationContext(), Mensaje, Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(getApplicationContext(), R.string.g_error_servidor, Toast.LENGTH_LONG).show();
                            }
                            }

                        }
                    }
                });
                mbuilder.setView(mView);
                dialog = mbuilder.create();
                dialog.show();
            }else{
                Toast.makeText(this, R.string.g_error_internet, Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
            Toast.makeText(this, R.string.g_error_global, Toast.LENGTH_LONG).show();
        }
    }

    private boolean validarPermiso(){
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M){
            return true;
        }
        if((checkSelfPermission( Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED) && (checkSelfPermission( Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED)){
            return true;
        }
        if((shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) || (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE))){
            cargarDialogo();
        }else{
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA},100);
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResult){
        super.onRequestPermissionsResult(requestCode, permissions, grantResult);
        if(requestCode==100){
            if(grantResult.length==2 && grantResult[0]==PackageManager.PERMISSION_GRANTED
                    && grantResult[1]==PackageManager.PERMISSION_GRANTED){
                Log.i("Mensaje", "PERMISOS VALIDADOS");
            }else{
                permisosManuales();
            }
        }
    }

    private void permisosManuales() {
        final CharSequence[] opciones={"Si","No"};
        final AlertDialog.Builder ao=new AlertDialog.Builder(Login.this,R.style.CustomDialog);
        ao.setTitle("Permitir manualmente?");
        ao.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(opciones[which].equals("Si")){
                    Intent intent=new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri=Uri.fromParts("package", getPackageName(),null);
                    intent.setData(uri);
                    startActivity(intent);
                }else{
                    dialog.dismiss();
                }
            }
        });
        ao.show();
    }

    private void cargarDialogo() {
        AlertDialog.Builder diag=new AlertDialog.Builder(Login.this,R.style.CustomDialog);
        diag.setTitle("Permisos");
        diag.setMessage("Conceda los permisos");
        diag.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},100);
                }
            }
        });
        diag.show();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public void guardar_credenciales(String usuario,String password){
        try{
            SharedPreferences preferencias=getSharedPreferences("usuario", Context.MODE_PRIVATE);

            SharedPreferences.Editor editor=preferencias.edit();
            editor.putString("usuario", usuario);
            editor.putString("clave", password);

            editor.commit();


        }
        catch (Exception e) {
            Toast.makeText(this, R.string.g_error_global, Toast.LENGTH_LONG).show();
        }


    }

    public  void listPagos(String TramaRespuesta,String cl_codigo) {
        try {
            if (isOnline(this) == true) {
                //String TramaRecida = "1|Tiene valores pendientes|205*206*207|$96.8|$80|$15|$1.81|12%|205-123456789-PBC6530-Recarga-$80-$0.00-2020/03/12-006_021_000005896-CHEVROLET AVEO ROJO*206-123456789-PBC6530-Recarga-$80-$0.00-2020/03/12-006_021_000005896-CHEVROLET AVEO ROJO*206-123456789-PBC6530-Tag-$15-$1.80-2020/03/12-006_021_000005896-CHEVROLET AVEO ROJO";
                String[] vectorPagos = TramaRespuesta.split("\\|");
                String codigoRespuesta = vectorPagos[0];
                if (codigoRespuesta.equals("1")) {
                    if (vectorPagos.length == 9) {
                        String mensaje = vectorPagos[1];
                        String cv_codigos = vectorPagos[2];
                        String valorTotal = vectorPagos[3];
                        String valorConIva = vectorPagos[4];
                        String valorSinIva = vectorPagos[5];
                        String valorIva = vectorPagos[6];
                        String porcIva = vectorPagos[7];
                        String pagos = vectorPagos[8];
                        envia = getString(R.string.cm_login_menu_pag_pend_place) + "," + cl_codigo;
                        ob.conectar();
                        ob.enviar(envia);
                        ob.cerrar();
                        String TramaRecida = ob.cadena.toString();
                        //String TramaRecida="1|mensaje de recargas pendientes en place to pay";
                        String[] vectorTramaRecibida = TramaRecida.split("\\|");
                        String CodigoResp = vectorTramaRecibida[0];
                        String mensaje1 = vectorTramaRecibida[1];
                        if (vectorTramaRecibida.length == 2) {
                            if (CodigoResp.equals("1")) {
                                crearMensaje(mensaje1, mensaje, cv_codigos, valorTotal, valorConIva, valorSinIva, valorIva, porcIva, pagos, cl_codigo);
                            }
                            if (CodigoResp.equals("2")) {
                                crearMensaje(mensaje1, mensaje, cv_codigos, valorTotal, valorConIva, valorSinIva, valorIva, porcIva, pagos, cl_codigo);

                            }
                            if (CodigoResp.equals("3")) {
                                Toast.makeText(this, Mensaje, Toast.LENGTH_LONG).show();
                            }

                        } else {
                            Toast.makeText(this, R.string.g_error_servidor, Toast.LENGTH_LONG).show();
                        }
                    }

                } else {
                    Toast.makeText(this, R.string.g_error_servidor, Toast.LENGTH_LONG).show();
                }

                if (codigoRespuesta.equals("2")) {
                    if (vectorPagos.length == 2) {
                        Mensaje = vectorPagos[1];
                        Toast.makeText(this, Mensaje, Toast.LENGTH_LONG).show();
                        ver_list_tags(cl_codigo,Usuario,vehiculos);

                    } else {
                        Toast.makeText(this, R.string.g_error_servidor, Toast.LENGTH_LONG).show();
                    }
                }
                if (codigoRespuesta.equals("3")) {
                    if (vectorPagos.length == 3) {
                        Mensaje = vectorPagos[1];
                        Toast.makeText(this, Mensaje, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, R.string.g_error_servidor, Toast.LENGTH_LONG).show();
                    }
                }
            }else{
                Toast.makeText(this, R.string.g_error_internet, Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
            Toast.makeText(this, R.string.g_error_global, Toast.LENGTH_LONG).show();
        }
    }


    private void crearMensaje(final String MensajeCodResp, final String mensaje1, final String cv_codigos, final String valorTotal, final String valorConIva, final String valorSinIva, final String valorIva, final String porcIva, final String pagos, final String cl_codigo){
   try{
    AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.CustomDialog);
    builder.setTitle("Transacciones pendientes");
    builder.setMessage(MensajeCodResp)
            .setPositiveButton("Seguir", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Bundle bundle = new Bundle();
                    bundle.putString("mensaje" ,mensaje1);
                    bundle.putString("cv_codigos" ,cv_codigos);
                    bundle.putString("valorTotal" ,valorTotal);
                    bundle.putString("valorConIva",valorConIva);
                    bundle.putString("valorSinIva" ,valorSinIva);
                    bundle.putString("valorIva",valorIva);
                    bundle.putString("porcIva" ,porcIva);
                    bundle.putString("cl_codigo",cl_codigo);
                    bundle.putString("pagos",pagos);
                    bundle.putString("nombre",Usuario);
                    bundle.putString("correo",correo);
                    bundle.putString("tipoDoc",tipoDoc);
                    bundle.putString("cedula",l);
                    bundle.putString("Vehiculos" ,vehiculos);
                    Intent i = new Intent(Login.this, Pago.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    i.putExtras(bundle);
                    startActivity(i);
                    finish();
                    dialog.dismiss();
                }
            })
            .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            });
    builder.create().show();
   }catch (Exception e){
       Toast.makeText(this, R.string.g_error_global, Toast.LENGTH_LONG).show();

   }
}

    private void cargarCredenciales(){
        try{
        SharedPreferences preferencias=getSharedPreferences("usuario", Context.MODE_PRIVATE);
        String user="";
        user=preferencias.getString("usuario","");
        String pass="";
        pass=preferencias.getString("clave","");
        if (!user.equals("") && !pass.equals("")) {
            txtLogin.setText(user);
            txtPass.setText(pass);
        }}catch (Exception e){
            Toast.makeText(this, R.string.g_error_global, Toast.LENGTH_LONG).show();
        }
    }

    public void Encriptar(String p){
        String name = p;
        char a1 = (char) (name.charAt(0)+1);
        char a3 = (char) (name.charAt(1)+2);
        char a5 = (char) (name.charAt(2)+3);
        char a7 = (char) (name.charAt(3)+4);
        char a9 = (char) (name.charAt(4)+5);
        char a11 = (char) (name.charAt(5)+6);
        passEncrip=""+a1+a3+a5+a7+a9+a11;
        System.out.println("The number is : "+a1+a3+a5+a7+a9+a11); // the value is 97


    }
}



