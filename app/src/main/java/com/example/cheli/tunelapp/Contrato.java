package com.example.cheli.tunelapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;

public class Contrato extends AppCompatActivity {
    String codigoUsuario = "";
    String codigovehiculos = "";
    String CodigoTrama = "";
    String st_Codigo = "";
    String lug_ret_tag = "";
    CheckBox chkRecoCred;
    Button btnEnviar;
    String direccion;
    String codi_post;
    private cls_conexion ob;
    String referencia;
    String lugares;
    String cedula;
    EditText txtPin;
    String pin;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_contrato);
        chkRecoCred = (CheckBox) findViewById(R.id.chk_acep_term);
        chkRecoCred.setChecked(false);
        txtPin = findViewById(R.id.editTextpin);
        txtPin.setVisibility(View.GONE);

        try {
            obtenerDatos();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        btnEnviar = findViewById(R.id.btnEnviar);
        btnEnviar.setEnabled(false);
        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (chkRecoCred.isChecked()) {
                    try {
                        Enviar();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Debe aceptar los terminos y condiciones", Toast.LENGTH_LONG);
                }

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

    public void EnviarPin(View view) throws IOException, JSONException {
        txtPin.setVisibility(View.VISIBLE);
        btnEnviar.setEnabled(true);
        if (CodigoTrama.equals(getString(R.string.cm_contrato_camb_vehi))) {
            try {

                if (isOnline(this) == true) {
                    pin = txtPin.getText().toString();
                    String enviar = getString(R.string.cm_contrato_envio_pin) + "," + codigoUsuario + ",2,"+codigovehiculos;
                    ob.conectar();
                    ob.enviar(enviar);
                    ob.cerrar();
                    String TramaRecibida = ob.cadena.toString();
                    //String TramaRecibida = "1|La solicitud de cambio de vehiculo ha sido enviada correctamente";
                    String vectorRecibido[] = TramaRecibida.split("\\|");
                    String CodigoRespuesta = vectorRecibido[0];
                    if (CodigoRespuesta.equals("1")) {
                        String Mensaje = vectorRecibido[1];
                        AlertDialog.Builder builder = new AlertDialog.Builder(Contrato.this, R.style.CustomDialog);
                        builder.setTitle("Informativo");
                        builder.setMessage(Mensaje);
                        builder.setNegativeButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        builder.create().show();
                    }
                    if (CodigoRespuesta.equals("2")) {
                        String Mensaje = vectorRecibido[1];
                        AlertDialog.Builder builder = new AlertDialog.Builder(Contrato.this, R.style.CustomDialog);
                        builder.setTitle("Informativo");
                        builder.setMessage(Mensaje);
                        builder.setNegativeButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        builder.create().show();
                    }
                    if (CodigoRespuesta.equals("3")) {
                        String Mensaje = vectorRecibido[1];
                        Toast.makeText(getApplicationContext(), Mensaje, Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(this, R.string.g_error_internet, Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Toast.makeText(this, R.string.g_error_global, Toast.LENGTH_LONG).show();
            }
        }

        if (CodigoTrama.equals(getString(R.string.cm_contrato_comp_tag_cl_antiguo))) {
            try {
                if (isOnline(this) == true) {
                    String enviar = getString(R.string.cm_contrato_envio_pin) + "," + cedula + ",1," + st_Codigo;
                    ob.conectar();
                    ob.enviar(enviar);
                    ob.cerrar();
                    String TramaRecibida = ob.cadena.toString();
                    //String TramaRecibida = "1|mensaje";
                    String vectorRecibido[] = TramaRecibida.split("\\|");
                    String CodigoRespuesta = vectorRecibido[0];
                    if (vectorRecibido.length == 2) {
                        if (CodigoRespuesta.equals("1")) {
                            String Mensaje = vectorRecibido[1];
                            AlertDialog.Builder builder = new AlertDialog.Builder(Contrato.this, R.style.CustomDialog);
                            builder.setTitle("Informativo");
                            builder.setMessage(Mensaje);
                            builder.setNegativeButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            builder.create().show();

                        }
                        if (CodigoRespuesta.equals("2")) {
                            String Mensaje = vectorRecibido[1];
                            AlertDialog.Builder builder = new AlertDialog.Builder(Contrato.this, R.style.CustomDialog);
                            builder.setTitle("Informativo");
                            builder.setMessage(Mensaje);
                            builder.setNegativeButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            builder.create().show();
                        }
                        if (CodigoRespuesta.equals("3")) {
                            String Mensaje = vectorRecibido[1];
                            Toast.makeText(getApplicationContext(), Mensaje, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(this, R.string.g_error_servidor, Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(this, R.string.g_error_internet, Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Toast.makeText(this, R.string.g_error_global, Toast.LENGTH_LONG).show();
            }
        }
        if (CodigoTrama.equals(getString(R.string.cm_contrato_comp_tag_cl_nuevo))) {

            try {
                if (isOnline(this) == true) {
                    cedula = getIntent().getStringExtra("cedula");
                    String enviar = getString(R.string.cm_contrato_envio_pin) + "," + cedula + ",1," + st_Codigo;
                    ob.conectar();
                    ob.enviar(enviar);
                    ob.cerrar();
                    String TramaRecibida = ob.cadena.toString();
                    //String TramaRecibida = "1|mensaje";
                    String vectorRecibido[] = TramaRecibida.split("\\|");
                    String CodigoRespuesta = vectorRecibido[0];
                    if (vectorRecibido.length == 2) {
                        if (CodigoRespuesta.equals("1")) {
                            String Mensaje = vectorRecibido[1];
                            AlertDialog.Builder builder = new AlertDialog.Builder(Contrato.this, R.style.CustomDialog);
                            builder.setTitle("Informativo");
                            builder.setMessage(Mensaje);
                            builder.setNegativeButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            builder.create().show();

                        }
                        if (CodigoRespuesta.equals("2")) {
                            String Mensaje = vectorRecibido[1];
                            AlertDialog.Builder builder = new AlertDialog.Builder(Contrato.this, R.style.CustomDialog);
                            builder.setTitle("Informativo");
                            builder.setMessage(Mensaje);
                            builder.setNegativeButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            builder.create().show();
                        }
                        if (CodigoRespuesta.equals("3")) {
                            String Mensaje = vectorRecibido[1];
                            Toast.makeText(getApplicationContext(), Mensaje, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(this, R.string.g_error_servidor, Toast.LENGTH_LONG).show();
                    }


                } else {
                    Toast.makeText(this, R.string.g_error_internet, Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Toast.makeText(this, R.string.g_error_global, Toast.LENGTH_LONG).show();
            }
        }
    }

    public void Enviar() throws IOException, JSONException {
        if (CodigoTrama.equals(getString(R.string.cm_contrato_camb_vehi))) {
            try {

                if (isOnline(this) == true) {
                    pin = txtPin.getText().toString();
                    String enviar = getString(R.string.cm_contrato_camb_vehi) + "," + codigoUsuario + "," + codigovehiculos + "," + pin;
                    ob.conectar();
                    ob.enviar(enviar);
                    ob.cerrar();
                    String TramaRecibida = ob.cadena.toString();
                    //String TramaRecibida = "1|La solicitud de cambio de vehiculo ha sido enviada correctamente";
                    String vectorRecibido[] = TramaRecibida.split("\\|");
                    String CodigoRespuesta = vectorRecibido[0];
                    if (CodigoRespuesta.equals("1")) {
                        String Mensaje = vectorRecibido[1];
                        AlertDialog.Builder builder = new AlertDialog.Builder(Contrato.this, R.style.CustomDialog);
                        builder.setTitle("Informativo");
                        builder.setMessage(Mensaje)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        Bundle bundle = new Bundle();
                                        bundle.putString("lugares", lugares);
                                        Intent i = new Intent(Contrato.this, Menu.class);
                                        i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                        i.putExtras(bundle);
                                        startActivity(i);
                                        finish();
                                    }
                                });
                        builder.create().show();
                    }
                    if (CodigoRespuesta.equals("2")) {
                        String Mensaje = vectorRecibido[1];
                        AlertDialog.Builder builder = new AlertDialog.Builder(Contrato.this, R.style.CustomDialog);
                        builder.setTitle("Informativo");
                        builder.setMessage(Mensaje)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                       /* Intent i = new Intent(Contrato.this, Login.class);
                                        i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                        startActivity(i);
                                        finish();*/
                                    }
                                });
                        builder.create().show();
                    }
                    if (CodigoRespuesta.equals("3")) {
                        String Mensaje = vectorRecibido[1];
                        Toast.makeText(getApplicationContext(), Mensaje, Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(this, R.string.g_error_internet, Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Toast.makeText(this, R.string.g_error_global, Toast.LENGTH_LONG).show();
            }
        }

        if (CodigoTrama.equals(getString(R.string.cm_contrato_comp_tag_cl_antiguo))) {
            try {
                if (isOnline(this) == true) {
                    pin = txtPin.getText().toString();
                    direccion = getIntent().getStringExtra("direccion");
                    codi_post = getIntent().getStringExtra("codi_post");
                    referencia = getIntent().getStringExtra("referencia");
                    lug_ret_tag = getIntent().getStringExtra("lug_ret_tag");
                    String enviar = getString(R.string.cm_contrato_comp_tag_cl_antiguo) + "," + codigoUsuario + "," + st_Codigo + "," + lug_ret_tag + "," + direccion + "," + codi_post + "," + referencia + "," + pin;
                    Log.e("enviar",enviar);
                    ob.conectar();
                    ob.enviar(enviar);
                    ob.cerrar();
                    String TramaRecibida = ob.cadena.toString();
                    //String TramaRecibida = "1|mensaje";
                    String vectorRecibido[] = TramaRecibida.split("\\|");
                    String CodigoRespuesta = vectorRecibido[0];
                    if (vectorRecibido.length == 2) {
                        if (CodigoRespuesta.equals("1")) {
                            String Mensaje = vectorRecibido[1];
                            AlertDialog.Builder builder = new AlertDialog.Builder(Contrato.this, R.style.CustomDialog);
                            builder.setTitle("Informativo");
                            builder.setMessage(Mensaje)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            Intent i = new Intent(Contrato.this, Menu.class);
                                            i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                            startActivity(i);
                                            finish();
                                        }
                                    });
                            builder.create().show();


                        }
                        if (CodigoRespuesta.equals("2")) {
                            String Mensaje = vectorRecibido[1];
                            AlertDialog.Builder builder = new AlertDialog.Builder(Contrato.this, R.style.CustomDialog);
                            builder.setTitle("Informativo");
                            builder.setMessage(Mensaje)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            /*Intent i = new Intent(Contrato.this, Menu.class);
                                            i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                            startActivity(i);
                                            finish();*/
                                        }
                                    });
                            builder.create().show();
                        }
                        if (CodigoRespuesta.equals("3")) {
                            String Mensaje = vectorRecibido[1];
                            Toast.makeText(getApplicationContext(), Mensaje, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(this, R.string.g_error_servidor, Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(this, R.string.g_error_internet, Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Toast.makeText(this, R.string.g_error_global, Toast.LENGTH_LONG).show();
            }
        }
        if (CodigoTrama.equals(getString(R.string.cm_contrato_comp_tag_cl_nuevo))) {

            try {
                if (isOnline(this) == true) {
                    pin = txtPin.getText().toString();
                    direccion = getIntent().getStringExtra("direccion");
                    codi_post = getIntent().getStringExtra("codi_post");
                    referencia = getIntent().getStringExtra("referencia");
                    lug_ret_tag = getIntent().getStringExtra("lug_ret_tag");
                    String enviar = getString(R.string.cm_contrato_comp_tag_cl_nuevo) + "," + st_Codigo + "," + lug_ret_tag + "," + direccion + "," + codi_post + "," + referencia + "," + pin;
                    Log.e("enviar",enviar);
                    ob.conectar();
                    ob.enviar(enviar);
                    ob.cerrar();
                    String TramaRecibida = ob.cadena.toString();
                    //String TramaRecibida = "1|mensaje";
                    String vectorRecibido[] = TramaRecibida.split("\\|");
                    String CodigoRespuesta = vectorRecibido[0];
                    if (vectorRecibido.length == 2) {
                        if (CodigoRespuesta.equals("1")) {
                            String Mensaje = vectorRecibido[1];
                            AlertDialog.Builder builder = new AlertDialog.Builder(Contrato.this, R.style.CustomDialog);
                            builder.setTitle("Informativo");
                            builder.setMessage(Mensaje)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            Intent i = new Intent(Contrato.this, Login.class);
                                            i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                            startActivity(i);
                                            finish();
                                        }
                                    });
                            builder.create().show();

                        }
                        if (CodigoRespuesta.equals("2")) {
                            String Mensaje = vectorRecibido[1];
                            AlertDialog.Builder builder = new AlertDialog.Builder(Contrato.this, R.style.CustomDialog);
                            builder.setTitle("Informativo");
                            builder.setMessage(Mensaje)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                           /* Intent i = new Intent(Contrato.this, Login.class);
                                            i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                            startActivity(i);
                                            finish();*/
                                        }
                                    });
                            builder.create().show();
                        }
                        if (CodigoRespuesta.equals("3")) {
                            String Mensaje = vectorRecibido[1];
                            Toast.makeText(getApplicationContext(), Mensaje, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(this, R.string.g_error_servidor, Toast.LENGTH_LONG).show();
                    }


                } else {
                    Toast.makeText(this, R.string.g_error_internet, Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Toast.makeText(this, R.string.g_error_global, Toast.LENGTH_LONG).show();
            }
        }
    }

    public void navegacion() throws IOException, JSONException {
        if (CodigoTrama.equals(getString(R.string.cm_contrato_camb_vehi))) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Contrato.this, R.style.CustomDialog);
            builder.setTitle("Telepeaje");
            builder.setMessage("Desea abandonar el proceso? " + "Nota:Si abandona el proceso no se registrara su requerimiento.");

            builder.setPositiveButton("SALIR", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Intent intent = new Intent(Contrato.this, Menu.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    finish();

                }
            });

            builder.setNegativeButton("CONTINUAR", null);
            Dialog dialog = builder.create();
            dialog.show();
        }
        if (CodigoTrama.equals(getString(R.string.cm_contrato_comp_tag_cl_antiguo))) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Contrato.this, R.style.CustomDialog);
            builder.setTitle("Telepeaje");
            builder.setMessage("Desea abandonar el proceso? " + "Nota:Si abandona el proceso no se registrara su requerimiento.");

            builder.setPositiveButton("SALIR", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Contrato.this, Menu.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    finish();
                }

            });
            builder.setNegativeButton("CONTINUAR", null);
            Dialog dialog = builder.create();
            dialog.show();
        }
        if (CodigoTrama.equals(getString(R.string.cm_contrato_comp_tag_cl_nuevo))) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Contrato.this, R.style.CustomDialog);
            builder.setTitle("Telepeaje");
            builder.setMessage("Desea abandonar el proceso? " + "Nota:Si abandona el proceso no se registrara su requerimiento.");

            builder.setPositiveButton("SALIR", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Intent i = new Intent(Contrato.this, Login.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(i);
                    finish();

                }
            });
            builder.setNegativeButton("CONTINUAR", null);
            Dialog dialog = builder.create();
            dialog.show();
        }
    }

    public void obtenerDatos() throws IOException, JSONException {
        try {
            if (isOnline(this) == true) {
                ob = new cls_conexion(getString(R.string.servidor_tramas), 8200);
                codigovehiculos = getIntent().getStringExtra("cut_codigo");
                CodigoTrama = getIntent().getStringExtra("codigo_trama");
                lug_ret_tag = getIntent().getStringExtra("lug_ret_tag");
                st_Codigo = getIntent().getStringExtra("st_codigo");
                direccion = getIntent().getStringExtra("direccion");
                codi_post = getIntent().getStringExtra("codi_post");
                referencia = getIntent().getStringExtra("referencia");
                lugares = getIntent().getStringExtra("lugares");
                SharedPreferences preferencias = getSharedPreferences("datos", Context.MODE_PRIVATE);
                codigoUsuario = preferencias.getString("cl_codigo", "");
                cedula = preferencias.getString("cedula", "");
            } else {
                Toast.makeText(this, R.string.g_error_internet, Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, R.string.g_error_global, Toast.LENGTH_LONG).show();
        }
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }

    }

