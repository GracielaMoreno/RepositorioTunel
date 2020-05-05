package com.example.cheli.tunelapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Message;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.StringReader;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Web_View_Pago extends AppCompatActivity {
    String nombre;
    String correo;
    String cl_codigo;
    String idRuta = "";
    String tipoDoc;
    String l;
    Dialog dialog;
    String login;
    String seed;
    String nonce;
    String trankey;
    String sesion;
    WebView myWebView;
    private cls_conexion ob;
    String referencia;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_web__view__pago);
        myWebView = (WebView) findViewById(R.id.webview);
        try {
            obtenerData();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            try {

                consul_status();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        return super.onKeyDown(keyCode, event);
    }


    public void consul_status() throws IOException, JSONException {
        //myWebView.setVisibility(View.GONE);
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\r\n\"auth\": {\r\n\"login\": \"" + login + "\",\r\n\"seed\": \"" + seed + "\",\r\n\"nonce\": \"" + nonce + "\",\r\n\"tranKey\": \"" + trankey + "\"\r\n}\r\n}");
        Request request = new Request.Builder()
                .url("https://test.placetopay.ec/redirection/api/session/" + sesion)
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Cookie", "__cfduid=d87f96a81a02f42378eb433425a26693f1588123761")
                .build();
        Response response = client.newCall(request).execute();
        JSONObject jObject = new JSONObject(response.body().string());
        String Status = jObject.getString("status"); //funciona
        JSONObject jObject1 = new JSONObject(Status);
        String message = jObject1.getString("message");
        final String StatusTransaccion = jObject1.getString("status");
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomDialog);

        builder.setTitle("Estado del pago");
        builder.setMessage("Referencia No." + referencia + ": " + message)
                .setPositiveButton("Seguir", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (idRuta.equals("recarga")) {
                            String envia = getString(R.string.cm_actu_estado) + "," + cl_codigo + "," + referencia + "," + sesion + "," + StatusTransaccion + "," + 1;
                            ob.conectar();
                            ob.enviar(envia);
                            ob.cerrar();
                            String TramaRecida = ob.cadena.toString();
                            String[] vector_rep_sesion = TramaRecida.split("\\|");
                            String codRespuesta = vector_rep_sesion[0];
                            if (codRespuesta.equals("1")) {
                                try {
                                    navegacion();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (codRespuesta.equals("2")) {

                                try {
                                    navegacion();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                        if (idRuta.equals("pago")) {
                            String envia = getString(R.string.cm_actu_estado) + "," + cl_codigo + "," + referencia + "," + sesion + "," + StatusTransaccion + "," + 2;
                            ob.conectar();
                            ob.enviar(envia);
                            ob.cerrar();
                            String TramaRecida = ob.cadena.toString();
                            String[] vector_rep_sesion = TramaRecida.split("\\|");
                            String codRespuesta = vector_rep_sesion[0];
                            if (codRespuesta.equals("1")) {
                                try {
                                    navegacion();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (codRespuesta.equals("2")) {
                                try {
                                    navegacion();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                    }
                });
        builder.create().show();
    }

    public void navegacion() throws IOException, JSONException {
        Bundle bundle = new Bundle();
        Intent i = new Intent(Web_View_Pago.this, Menu.class);
        i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        i.putExtras(bundle);
        startActivity(i);
        finish();

    }

    public void obtenerData() throws IOException, JSONException {

        ob = new cls_conexion(getString(R.string.servidor_tramas), 8200);
        String processUrl = getIntent().getStringExtra("processUrl");
        idRuta = getIntent().getStringExtra("idRuta");

        obtenervariablesSesion();

        login = getIntent().getStringExtra("login");
        seed = getIntent().getStringExtra("seed");
        nonce = getIntent().getStringExtra("nonce");
        trankey = getIntent().getStringExtra("trnakey");
        sesion = getIntent().getStringExtra("sesion");
        referencia = getIntent().getStringExtra("referencia");
        WebSettings webSettings = myWebView.getSettings();
        myWebView.setWebViewClient(new WebViewClient());
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        // Cargamos la web
        myWebView.loadUrl(processUrl);
    }

    public void obtenervariablesSesion() {

        SharedPreferences preferencias = getSharedPreferences("datos", Context.MODE_PRIVATE);
        cl_codigo = preferencias.getString("cl_codigo", "");

    }
}


