package com.example.cheli.tunelapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Menu extends AppCompatActivity {
    AlertDialog dialog;
    private ListView listTags;
    private TextView cliente;
    private List<cls_list_tags> placa;
    private cls_list_tags_adap ListTagsAdapter;
    private String nombreUsuario;
    private String cl_codigo = "";
    private String vehiculos = "";
    private String[] vectorVehiculos;
    private String[] vectorVehiculo;
    private cls_conexion ob;
    private String Bancos;
    private String Trajetas;
    private String Pasadas;
    private String cut_codigo = "";
    private String placaRecarga = "";
    private String numTag = "";
    private String PasadasRec = "";
    private String cut_propietario;

    String lugares;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        try {
            if (isOnline(this) == true) {
                cliente = (TextView) findViewById(R.id.txtCliente);
                ob = new cls_conexion(getString(R.string.servidor_tramas), 8200);
                listTags = (ListView) findViewById(R.id.lsvw_tags);
                cliente.setText("");

                placa = new ArrayList<>();
                placa.clear();
                listTags.setAdapter(null);

                //variables pasadas
                obtenerDatos();
                RecargarListaTags();

                ImageView imgRecargar = findViewById(R.id.imgRecargar);
                imgRecargar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RecargarListaTags();
                    }
                });

                ImageView img = (ImageView) findViewById(R.id.img_regresar);
                img.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                            Intent intent = new Intent(Menu.this, Login.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                            startActivity(intent);
                            finish();

                    }
                });
            } else {
                Toast.makeText(this, R.string.g_error_internet, Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, R.string.g_error_global, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Intent intent = new Intent(Menu.this, Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_tags_menu, menu);

    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.opcion_1:
                try {
                    if (isOnline(this) == true) {
                        String envia = getString(R.string.cm_list_pasadas_rec) + ",0";
                        ob.conectar();
                        ob.enviar(envia);
                        ob.cerrar();
                        String TramaRecidalist = ob.cadena.toString();
                        //String TramaRecidalist = "1|1-100 Pasadas-28*2-200 Pasadas-57";

                        String[] vectorTramaPasadas = TramaRecidalist.split("\\|");
                        String CodRespuesta = vectorTramaPasadas[0];
                        if (vectorTramaPasadas.length == 2) {
                            if (CodRespuesta.equals("1")) {

                                PasadasRec = vectorTramaPasadas[1];
                                crearMensaje();

                            }
                            if (CodRespuesta.equals("2")) {
                                String Mensaje = vectorTramaPasadas[1];
                                Toast.makeText(Menu.this, Mensaje, Toast.LENGTH_LONG).show();
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
                return true;
            //editNote(info.id);
            case R.id.opcion_2:
                try {
                    if (isOnline(this) == true) {
                        String enviaInfo = getString(R.string.cm_menu_soli_tag_matri_verificar) + "," + cl_codigo + "," + cut_codigo;
                        ob.conectar();
                        ob.enviar(enviaInfo);
                        ob.cerrar();
                        String TramaRecibida = ob.cadena.toString();
                        //String TramaRecibida = "2|tiene solicitudes vigentes";
                        String[] vectorTramaRecibida = TramaRecibida.split("\\|");
                        String CodigoResp = vectorTramaRecibida[0];

                        if (vectorTramaRecibida.length == 2) {
                            if (CodigoResp.equals("1")) {
                                String Mensaje = vectorTramaRecibida[1];
                                AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomDialog);
                                builder.setTitle("Transacciones pendientes");
                                builder.setMessage(Mensaje)
                                        .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {

                                            }
                                        });
                                builder.create().show();

                                // Toast.makeText(getApplicationContext(), Mensaje, Toast.LENGTH_LONG).show();
                            }
                            if (CodigoResp.equals("2")) {
                                String Mensaje1 = vectorTramaRecibida[1];
                                AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomDialog);
                                builder.setTitle("Transacciones pendientes");
                                builder.setMessage(Mensaje1)
                                        .setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                Bundle bundle2 = new Bundle();
                                                bundle2.putString("cut_codigo", cut_codigo);
                                                Intent inten = new Intent(Menu.this, Actu_plac.class);
                                                inten.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                                inten.putExtras(bundle2);
                                                startActivity(inten);
                                                finish();
                                            }
                                        })
                                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {

                                            }
                                        });
                                builder.create().show();

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
                return true;
            case R.id.opcion_3:
                try {
                    if (isOnline(this) == true) {
                        Llenar();
                        String tramaenvio = getString(R.string.cm_menu_actu_debi_auto) + "," + cl_codigo + "," + cut_codigo;
                        ob.conectar();
                        ob.enviar(tramaenvio);
                        ob.cerrar();
                        String TramaRecibi = ob.cadena.toString();
                        // String TramaRecibi = "1|Suforma de pago actual de efectivo desea continuar con el proceso";
                        String[] vectorTramaRecibi = TramaRecibi.split("\\|");
                        String CodResp = vectorTramaRecibi[0];
                        if (vectorTramaRecibi.length == 2) {
                            if (CodResp.equals("1")) {
                                String Mensaje = vectorTramaRecibi[1];
                                auto_debi_auto(Mensaje);
                            }
                            if (CodResp.equals("2")) {
                                String Mensaje = vectorTramaRecibi[1];
                                auto_debi_auto(Mensaje);
                                // Toast.makeText(Menu.this, Mensaje, Toast.LENGTH_LONG).show();
                            }
                            if (CodResp.equals("3")) {
                                String Mensaje = vectorTramaRecibi[1];
                                Toast.makeText(Menu.this, Mensaje, Toast.LENGTH_LONG).show();
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
                return true;

            case R.id.opcion_4:
                try {
                    Bundle bundle3 = new Bundle();
                    bundle3.putString("cut_codigo", cut_codigo);
                    bundle3.putString("placa", placaRecarga);
                    bundle3.putString("numTag", numTag);
                    bundle3.putString("cut_propietario", cut_propietario);

                    Intent i3 = new Intent(Menu.this, Baja_unid.class);
                    i3.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    i3.putExtras(bundle3);
                    startActivity(i3);
                } catch (Exception e) {
                    Toast.makeText(this, R.string.g_error_global, Toast.LENGTH_LONG).show();
                }
                return true;
            case R.id.opcion_5:
                try {
                    RecargarListaTags();
                } catch (Exception e) {
                    Toast.makeText(this, R.string.g_error_global, Toast.LENGTH_LONG).show();
                }
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }

    public void Llenar() {
        try {
            if (isOnline(this) == true) {
                String envia = getString(R.string.cm_auto_debi_auto) + "," + cl_codigo;
                ob.conectar();
                ob.enviar(envia);
                ob.cerrar();
                String TramaRecida = ob.cadena.toString();
                //String TramaRecida = "1|mensaje|1-Pichincha*2-Produbanco*3-Pacifico*4-Guayaquil*5-Austro*6-Bolivariano*7-Internancional*8-General Rumiñahui|1-Visa*2-Mastercard*3-Diners*4-American Expres*5-Discover|1-100 Pasadas por $28*2-200 Pasadas por $57";

                String[] vectorTramaRecibida = TramaRecida.split("\\|");

                String CodRespuesta = vectorTramaRecibida[0];

                if (CodRespuesta.equals("1")) {
                    if (vectorTramaRecibida.length == 5) {
                        String Mensaje = vectorTramaRecibida[1];
                        Bancos = vectorTramaRecibida[2];
                        Trajetas = vectorTramaRecibida[3];
                        Pasadas = vectorTramaRecibida[4];
                    } else {
                        Toast.makeText(this, R.string.g_error_servidor, Toast.LENGTH_LONG).show();
                    }
                }
                if (CodRespuesta.equals("2")) {
                    if (vectorTramaRecibida.length == 2) {
                        String Mensaje = vectorTramaRecibida[1];
                        Toast.makeText(Menu.this, Mensaje, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, R.string.g_error_servidor, Toast.LENGTH_LONG).show();
                    }
                }
            } else {
                Toast.makeText(this, R.string.g_error_internet, Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, R.string.g_error_global, Toast.LENGTH_LONG).show();
        }
    }

    public void auto_debi_auto(String Mensaje) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomDialog);
            builder.setTitle("Información");
            builder.setMessage(Mensaje);
            // Set up the buttons
            builder.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("cut_codigo", cut_codigo);
                    bundle1.putString("Bancos", Bancos);
                    bundle1.putString("Tarjetas", Trajetas);
                    bundle1.putString("Pasadas", Pasadas);
                    Intent i2 = new Intent(Menu.this, Actu_debi_auto_camb.class);
                    i2.putExtras(bundle1);
                    i2.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(i2);
                }
            });
            builder.setNegativeButton("Salir", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.create();
            builder.show();
        } catch (Exception e) {
            Toast.makeText(this, R.string.g_error_global, Toast.LENGTH_LONG).show();
        }
    }

    public void Continuar(View view) {
        try {
            if (isOnline(this) == true) {
                String enviarTrama = getString(R.string.cm_menu_list_soli_pend) + "," + cl_codigo;
                ob.conectar();
                ob.enviar(enviarTrama);
                ob.cerrar();
                String TramaRespuesta = ob.cadena.toString();

                String[] vectorTramaRespuesta = TramaRespuesta.split("\\|");
                String CodigoRespuesta = vectorTramaRespuesta[0];
                if (CodigoRespuesta.equals("1")) {
                    //String Mensaje = vectorTramaRespuesta[1];
                    String listSolPen = vectorTramaRespuesta[1];
                    //Toast.makeText(Menu.this, Mensaje, Toast.LENGTH_LONG).show();
                    CrearDialogListSolicitudPen(listSolPen);
                }
                //ir a solitag matri
                if (CodigoRespuesta.equals("2")) {
                    String Mensaje = vectorTramaRespuesta[1];
                    //Toast.makeText(Menu.this, Mensaje, Toast.LENGTH_LONG).show();
                    String envia = getString(R.string.cm_menu_tag_lugares) + ",0";
                    ob.conectar();
                    ob.enviar(envia);
                    ob.cerrar();
                    String TramaRecibida = ob.cadena.toString();
                    //String TramaRecibida = "1|1-Scala*2-Zona Norte*3-Domicilio";
                    String[] vectorTramaRecibida = TramaRecibida.split("\\|");
                    String CodigoResp = vectorTramaRecibida[0];
                    if (vectorTramaRecibida.length == 2) {
                        if (CodigoResp.equals("1")) {
                            String lugares = vectorTramaRecibida[1];
                            Bundle bundle = new Bundle();
                            bundle.putString("codigo_trama", "9-1");
                            bundle.putString("lugares", lugares);
                            Intent i = new Intent(Menu.this, Paso_Soli_Tag_Cli.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                            i.putExtras(bundle);
                            startActivity(i);
                            finish();

                        }
                        if (CodigoResp == "2") {
                            String Mensajenum = vectorTramaRecibida[1];
                            Toast.makeText(getApplicationContext(), Mensajenum, Toast.LENGTH_LONG).show();


                        }
                    } else {
                        Toast.makeText(this, R.string.g_error_servidor, Toast.LENGTH_LONG).show();
                    }
                }
                if (CodigoRespuesta.equals("3")) {
                    String Mensaje = vectorTramaRespuesta[1];
                    Toast.makeText(Menu.this, Mensaje, Toast.LENGTH_LONG).show();

                }
            } else {
                Toast.makeText(this, R.string.g_error_internet, Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, R.string.g_error_global, Toast.LENGTH_LONG).show();
        }

    }

    public void list_Hist_Pagos(View view) {
        try {
            if (isOnline(this) == true) {
                String envia = getString(R.string.cm_menu_hist_pagos) + "," + cl_codigo;
                ob.conectar();
                ob.enviar(envia);
                ob.cerrar();
                String TramaRecida = ob.cadena.toString();
                //String TramaRecida = "1|mensaje|101?121020406080?PTJ-560?20/01/2020?80 pasadas?Fybeca?Factura:006-021-000008967?OK*101?121020406080?PTJ-560?20/01/2020?80 pasadas?Fybeca?Factura:006-021-000008967?Pendientes";
                // String TramaRecida = "2|Tiene valores pendientes";
                String[] vector_hist_Pagos = TramaRecida.split("\\|");
                String codigoRespuesta = vector_hist_Pagos[0];
                if (codigoRespuesta.equals("1")) {
                    if (vector_hist_Pagos.length == 3) {
                        String hist_pagos = vector_hist_Pagos[2];
                        Bundle bundle = new Bundle();
                        bundle.putString("hist_pagos", hist_pagos);
                        bundle.putString("codigoTrama", getString(R.string.cm_menu_hist_pagos));
                        bundle.putString("idRuta", "recarga");
                        Intent inten = new Intent(Menu.this, Hist_pago.class);
                        inten.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        inten.putExtras(bundle);
                        startActivity(inten);
                        finish();

                    } else {
                        Toast.makeText(this, R.string.g_error_servidor, Toast.LENGTH_LONG).show();
                    }
                }
                if (codigoRespuesta.equals("2")) {
                    String Mensaje;
                    if (vector_hist_Pagos.length == 2) {
                        Mensaje = vector_hist_Pagos[1];
                        //Toast.makeText(this, Mensaje, Toast.LENGTH_LONG).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomDialog);
                        builder.setTitle("Historial de pagos");
                        builder.setMessage(Mensaje);
                        // Set up the buttons
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        builder.create();
                        builder.show();

                    } else {
                        Toast.makeText(this, R.string.g_error_servidor, Toast.LENGTH_LONG).show();
                    }
                }
                if (codigoRespuesta.equals("3")) {
                    String Mensaje;
                    if (vector_hist_Pagos.length == 2) {
                        Mensaje = vector_hist_Pagos[1];
                        Toast.makeText(this, Mensaje, Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(this, R.string.g_error_servidor, Toast.LENGTH_LONG).show();
                    }
                }
            } else {
                Toast.makeText(this, R.string.g_error_internet, Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, R.string.g_error_global, Toast.LENGTH_LONG).show();
        }
    }

    private void crearMensaje() {
        try {
            if (isOnline(this) == true) {
                String envia = getString(R.string.cm_buscar_rec_pend) + "," + cl_codigo;
                ob.conectar();
                ob.enviar(envia);
                ob.cerrar();
                String TramaRecidalist = ob.cadena.toString();
                //String TramaRecidalist = "1|Tiene 2 recargas pendientes realizadas por place to pay";

                String[] vectorTramaPasadas = TramaRecidalist.split("\\|");
                String CodRespuesta = vectorTramaPasadas[0];
                if (vectorTramaPasadas.length == 2) {
                    String Mensaje = vectorTramaPasadas[1];
                    if (CodRespuesta.equals("1")) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomDialog);
                        builder.setTitle("Transacciones pendientes");
                        builder.setMessage(Mensaje)
                                .setPositiveButton("Seguir", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Bundle bundle = new Bundle();
                                        bundle.putString("cut_codigo", cut_codigo);
                                        bundle.putString("Pasadas", PasadasRec);
                                        bundle.putString("placa", placaRecarga);
                                        bundle.putString("numTag", numTag);
                                        Intent i = new Intent(Menu.this, Recarga.class);
                                        i.putExtras(bundle);
                                        startActivity(i);
                                        finish();
                                    }
                                })
                                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                    }
                                });
                        builder.create().show();

                    }
                    if (CodRespuesta.equals("2")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomDialog);
                        builder.setTitle("Transacciones pendientes");
                        builder.setMessage(Mensaje)
                                .setPositiveButton("Seguir", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Bundle bundle = new Bundle();
                                        bundle.putString("cut_codigo", cut_codigo);
                                        bundle.putString("Pasadas", PasadasRec);
                                        bundle.putString("placa", placaRecarga);
                                        bundle.putString("numTag", numTag);
                                        Intent i = new Intent(Menu.this, Recarga.class);
                                        i.putExtras(bundle);
                                        startActivity(i);
                                        finish();
                                    }
                                })
                                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                    }
                                });
                        builder.create().show();

                    }
                    if (CodRespuesta.equals("3")) {

                        Mensaje = vectorTramaPasadas[1];
                        Toast.makeText(Menu.this, Mensaje, Toast.LENGTH_LONG).show();

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

    public void CrearDialogListSolicitudPen(String soliPendientes) {
        try {
            if (isOnline(this) == true) {
                String envia = getString(R.string.cm_menu_tag_lugares) + ",0";
                ob.conectar();
                ob.enviar(envia);
                ob.cerrar();
                String TramaRecibida = ob.cadena.toString();
                //String TramaRecibida = "1|1-Scala*2-Zona Norte*3-Domicilio";
                String[] vectorTramaRecibida = TramaRecibida.split("\\|");
                String CodigoResp = vectorTramaRecibida[0];
                if (vectorTramaRecibida.length == 2) {
                    if (CodigoResp.equals("1")) {
                        String lugares = vectorTramaRecibida[1];
                        Bundle bundle = new Bundle();
                        bundle.putString("codigo_trama", "9-1");
                        bundle.putString("lugares", lugares);
                        bundle.putString("listSoliPend", soliPendientes);
                        Intent i = new Intent(Menu.this, Hist_soli_pend.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        i.putExtras(bundle);
                        startActivity(i);
                        finish();

                    }
                    if (CodigoResp == "2") {
                        String Mensaje = vectorTramaRecibida[1];
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

    public void RecargarListaTags() {

        listTags.setAdapter(null);
        placa.clear();
        try {
            if (isOnline(this) == true) {
                String envia = getString(R.string.cm_menu_actualizar_tags) + "," + cl_codigo;
                ob.conectar();
                ob.enviar(envia);
                ob.cerrar();
                String TramaRecibida = ob.cadena.toString();
                //String TramaRecibida = "1|mensaje|789?EPMMOP?PGQ0818?5752?KIA 2.9L DSL 2003 BLANCO?0*39976?EPMMOP?PGQ818?1211011320?KIA SPORTAGE 2003 BLANCO?18";
                String[] vectorTramaRecibida = TramaRecibida.split("\\|");
                String CodigoResp = vectorTramaRecibida[0];

                String Mensaje = vectorTramaRecibida[1];
                String vehiculos = vectorTramaRecibida[2];

                if (CodigoResp.equals("1")) {

                    vectorVehiculos = vehiculos.split("\\*");

                    if (vectorVehiculos.length > 0) {
                        for (Integer i = 0; i < (vectorVehiculos.length); i++) {
                            vectorVehiculo = vectorVehiculos[i].split("\\?");
                            if (vectorVehiculo.length > 0) {
                                if (vectorVehiculo.length == 6) {
                                    placa.add(new cls_list_tags(vectorVehiculo[0], vectorVehiculo[2], "Tag:" + vectorVehiculo[3], "Vehiculo:" + vectorVehiculo[4], "Saldo: " + vectorVehiculo[5] + " pasadas", vectorVehiculo[1]));
                                } else {

                                }
                            }

                        }
                        ListTagsAdapter = new cls_list_tags_adap(Menu.this, placa);
                        listTags.setAdapter(ListTagsAdapter);
                        // Toast.makeText(getApplicationContext(), Mensaje, Toast.LENGTH_LONG).show();
                    }
                }
                if (CodigoResp == "2") {
                    Mensaje = vectorTramaRecibida[1];
                    Toast.makeText(getApplicationContext(), Mensaje, Toast.LENGTH_LONG).show();


                }
                if (CodigoResp == "3") {
                    Mensaje = vectorTramaRecibida[1];
                    Toast.makeText(getApplicationContext(), Mensaje, Toast.LENGTH_LONG).show();


                }
            } else {
                Toast.makeText(this, R.string.g_error_internet, Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, R.string.g_error_global, Toast.LENGTH_LONG).show();
        }

    }

    public void obtenerDatos() throws IOException, JSONException {
        try {
            obtenervariablesSesion();
            lugares = getIntent().getStringExtra("lugares");

            cliente.setText(nombreUsuario);

            vectorVehiculos = vehiculos.split("\\*");

            if (vectorVehiculos.length > 0) {
                for (Integer i = 0; i < (vectorVehiculos.length); i++) {
                    vectorVehiculo = vectorVehiculos[i].split("\\?");
                    if (vectorVehiculo.length > 0) {
                        if (vectorVehiculo.length == 6) {
                            placa.add(new cls_list_tags(vectorVehiculo[0], vectorVehiculo[2], "Tag:" + vectorVehiculo[3], "Vehiculo:" + vectorVehiculo[4], "Saldo: " + vectorVehiculo[5] + " pasadas", vectorVehiculo[1]));
                        } else {

                        }
                    }

                }
                ListTagsAdapter = new cls_list_tags_adap(Menu.this, placa);
                listTags.setAdapter(ListTagsAdapter);
            }

            registerForContextMenu(listTags);

            listTags.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //vehiculos.getItemAtPosition(position).
                    Toast.makeText(Menu.this, "Mantenga presionado el item para ver opciones", Toast.LENGTH_LONG).show();

                }
            });
            listTags.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                    cls_list_tags p = (cls_list_tags) listTags.getItemAtPosition(position);
                    cut_codigo = p.getCampo1();
                    placaRecarga = p.getCampo2();
                    numTag = p.getCampo3();
                    cut_propietario = p.getCampo6();
                    Log.i("Click", "click en el elemento " + placaRecarga + "numTag" + numTag + "lista" + p.getCampo3() + "jkl:" + p.getCampo4() + "mm:" + p.getCampo5() + "ghjk" + p.getCampo6());
                    return false;
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, R.string.g_error_global, Toast.LENGTH_LONG).show();
        }
    }

    public void obtenervariablesSesion() {

        SharedPreferences preferencias = getSharedPreferences("datos", Context.MODE_PRIVATE);
        cl_codigo = preferencias.getString("cl_codigo", "");
        nombreUsuario = preferencias.getString("nombre", "");
        vehiculos = preferencias.getString("Vehiculos", "");

    }
}