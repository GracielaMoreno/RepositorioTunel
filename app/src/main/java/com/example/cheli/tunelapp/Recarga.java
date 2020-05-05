package com.example.cheli.tunelapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Modelo.Pasadas;
import Service.Service;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Retrofit;

public class Recarga extends AppCompatActivity {
    private String cl_codigo;
    private String cut_codigo;
    private String Pasadas;
    private String Placa;
    private String[] vectorPasadas;
    Spinner var_spnPasadas;
    TextView var_texto;
    ImageView img;
    String nombre;
    String correo;
    String Mensaje;
    private cls_conexion ob;
    String cod_pasada;
    String valor_pago;
    String desc_rec;
    String tipoDoc;
    String l;
    TextView indicacion1;
    CheckBox chkTerCond;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_recarga);
        ob = new cls_conexion(getString(R.string.servidor_tramas), 8200);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        obtenervariablesSesion();
        chkTerCond = findViewById(R.id.chk_acep_term);
        chkTerCond.setChecked(false);
        cut_codigo = getIntent().getStringExtra("cut_codigo");
        Pasadas = getIntent().getStringExtra("Pasadas");
        Placa = getIntent().getStringExtra("placa");
        vectorPasadas = Pasadas.split("\\*");
        var_spnPasadas = (Spinner) findViewById(R.id.spn_pasadas);
        var_texto = (TextView) findViewById(R.id.lblNota1);
        indicacion1 = (TextView) findViewById(R.id.textTerm);

        String content = "Para continuar con el pago debe aceptar los terminos y condiciones.Si desea ver los terminos y condiciones ";
        indicacion1.setText(content);
        SpannableString btn_soli_cont = makeLinkSpan("dar click aquí", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    btn_Term();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        indicacion1.append(btn_soli_cont);
        makeLinksFocusable(indicacion1);

        final ArrayList<Modelo.Pasadas> list = new ArrayList<Modelo.Pasadas>();
        String vectorpasada[];
        if (vectorPasadas.length > 0) {
            for (Integer i = 0; i < (vectorPasadas.length); i++) {
                vectorpasada = vectorPasadas[i].split("\\?");
                if (vectorpasada.length > 0) {
                    list.add(new Modelo.Pasadas(vectorpasada[0], vectorpasada[1], vectorpasada[2]));

                }

            }
            ArrayAdapter<Modelo.Pasadas> adapter = new ArrayAdapter<Modelo.Pasadas>(this, R.layout.spinner_item_global, list);
            var_spnPasadas.setAdapter(adapter);
        }

        var_spnPasadas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String p = var_spnPasadas.getItemAtPosition(position).toString();
                cod_pasada = list.get(position).getCodigo().toString();
                valor_pago = list.get(position).getTexto().toString();
                desc_rec = list.get(position).getValor().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        var_texto.setText("La recarga se realizará a la placa Nro." + Placa);
        img = findViewById(R.id.img_regresar);
        img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Recarga.this, Menu.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        Intent intent = new Intent(Recarga.this, Menu.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        finish();
        return super.onKeyDown(keyCode, event);
    }


    public void pagoReC(View view) throws IOException, JSONException {
        try {
            if (isOnline(this) == true) {
                if (chkTerCond.isChecked()) {
                    String login = "c79b9e59a7e8ae75e753e0b00515e624";
                    String tranKey = "MQ463Hc4A3az8jVq";


                    // String envia= getString(R.string.cm_report_ini_pago)+","+cl_codigo+","+cv_codigos+","+valorTotal1;
                    String envia = getString(R.string.cm_report_ini_rec) + "," + cl_codigo + "," + cut_codigo + "," + cod_pasada;

                    ob.conectar();
                    ob.enviar(envia);
                    ob.cerrar();
                    String TramaRecida = ob.cadena.toString();

                    //String TramaRecida = "1|mensaje|123456|2020-04-22T17:50:49-05:00|"+login+"|"+tranKey;
                    String[] vector_sesion = TramaRecida.split("\\|");
                    String codigoRespuesta = vector_sesion[0];
                    if (codigoRespuesta.equals("1")) {
                        if (vector_sesion.length == 6) {
                            String reference = vector_sesion[2];
                            String expiration = vector_sesion[3];
                            String login3 = vector_sesion[4];
                            String trankey4 = vector_sesion[5];
                            cls_autenticacion auth = new cls_autenticacion(login3, trankey4);
                            String login1 = auth.getLogin();
                            String nonce = auth.getNonce();
                            String seed = auth.getSeed();
                            String trankey1 = auth.getTranKey();
                            String ncedula = l;
                            String tipoDoc1 = tipoDoc;
                            //String vectorNombre[] = Usuario.split(" ");
                            String nUsuario = nombre;
                            String apUsuario = "";
                            String email = correo;
                            String locale = "es_EC";
                            String description = "Pago Tunel Guayasamin";
                            String currency = "USD";
                            String total = valor_pago;
                            String UrlRetorno = "http://200.105.229.50/sgt_tele_quito/pages/proc/sgt_estado_pago_app.aspx";
                            //String expiration="2020-04-10T17:50:49-05:00";
                            double amount = 0;
                            Double base = 100.00;

                            OkHttpClient client = new OkHttpClient().newBuilder()
                                    .build();
                            MediaType mediaType = MediaType.parse("application/json");

                            RequestBody body = RequestBody.create(mediaType, "{\"auth\": {\r\n\"login\": \"" + login1 + "\",\r\n\"seed\": \"" + seed + "\",\r\n\"nonce\": \"" + nonce + "\",\r\n\"tranKey\": \"" + trankey1 + "\"\r\n},\r\n\"locale\": \"" + locale + "\",\r\n\"buyer\": {\r\n\"document\": \"" + ncedula + "\",\r\n\"documentType\": \"" + tipoDoc1 + "\",\r\n\"name\": \"" + nUsuario + "\",\r\n\"surname\": \"" + apUsuario + "\",\r\n\"email\": \"" + email + "\"\r\n},\"payment\":{\"reference\":\"" + reference + "\",\"description\":\"" + description + "\",\"amount\":{\"currency\":\"" + currency + "\",\"total\":\"" + total + "\",\"taxes\":[{\"kind\":\"valueAddedTax\",\"amount\":" + amount + ",\"base\":" + base + "}]},\r\n\"allowPartial\": false\r\n},\r\n\"expiration\": \"" + expiration + "\",\r\n\"returnUrl\": \"" + UrlRetorno + "\",\r\n\"skipResult\": false,\r\n\"userAgent\": \"Chrome/52.0.2743.82\",\r\n\"ipAddress\": \"127.0.0.1\"\r\n}");

                            Request request = new Request.Builder()
                                    .url("https://test.placetopay.ec/redirection/api/session")
                                    .method("POST", body)
                                    .addHeader("Content-Type", "application/json")
                                    .addHeader("Cookie", "__cfduid=d769f36cd51363148afc8a7cb5401a1311585489914")
                                    .build();
                            okhttp3.Response response = client.newCall(request).execute();
                            JSONObject jObject = new JSONObject(response.body().string());
                            String Status = jObject.getString("status"); //funciona
                            JSONObject jObject1 = new JSONObject(Status);
                            if (jObject1.getString("status").equals("OK")) {
                                String processURL = jObject.getString("processUrl"); //funciona
                                String requestId = jObject.getString("requestId");
                                String Tramaenvio = getString(R.string.cm_report_ini_ses_rec) + "," + cl_codigo + "," + cut_codigo + "," + reference + "," + requestId;
                                ob.conectar();
                                ob.enviar(Tramaenvio);
                                ob.cerrar();
                                String TramaSesion = ob.cadena.toString();

                                // String TramaSesion = "1|transaccion exitosa";

                                //String TramaRecida = "1|Tiene valores pendientes|205*206*207|$96.8|$80|$15|$1.81|12%|205-123456789-PBC6530-Recarga-$80-$0.00-2020/03/12-006_021_000005896-CHEVROLET AVEO ROJO*206-123456789-PBC6530-Recarga-$80-$0.00-2020/03/12-006_021_000005896-CHEVROLET AVEO ROJO*206-123456789-PBC6530-Tag-$15-$1.80-2020/03/12-006_021_000005896-CHEVROLET AVEO ROJO";
                                String[] vector_rep_sesion = TramaSesion.split("\\|");
                                String codRespuesta = vector_rep_sesion[0];
                                if (codRespuesta.equals("1")) {
                                    if (vector_rep_sesion.length == 2) {
                                        Bundle bundle = new Bundle();
                                        bundle.putString("processUrl", processURL);
                                        bundle.putString("requestId", requestId);
                                        bundle.putString("idRuta", "pago");
                                        bundle.putString("login", login1);
                                        bundle.putString("seed", seed);
                                        bundle.putString("nonce", nonce);
                                        bundle.putString("trnakey", trankey1);
                                        bundle.putString("sesion", requestId);
                                        bundle.putString("referencia", reference);
                                        Intent i = new Intent(Recarga.this, Web_View_Pago.class);
                                        i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                        i.putExtras(bundle);
                                        startActivity(i);
                                        finish();
                                    } else {
                                        Toast.makeText(this, getString(R.string.g_error_servidor), Toast.LENGTH_LONG);
                                    }


                                } else {
                                    Toast.makeText(this, getString(R.string.g_error_servidor), Toast.LENGTH_LONG);
                                }
                                if (codRespuesta.equals("2")) {
                                    if (vector_rep_sesion.length == 2) {
                                        Mensaje = vector_rep_sesion[1];
                                        Toast.makeText(this, Mensaje, Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(this, getString(R.string.g_error_servidor), Toast.LENGTH_LONG).show();
                                    }
                                }
                            } else {
                                Toast.makeText(this, getString(R.string.g_error_servidor), Toast.LENGTH_LONG);
                            }

                        }
                    } else {
                        Toast.makeText(this, getString(R.string.g_error_servidor), Toast.LENGTH_LONG);
                    }
                    if (codigoRespuesta.equals("2")) {
                        if (vector_sesion.length == 2) {
                            Mensaje = vector_sesion[1];
                            Toast.makeText(this, Mensaje, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(this, getString(R.string.g_error_servidor), Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    Toast.makeText(this, "Para continuar con el pago se debe aceptar terminos y condiciones", Toast.LENGTH_LONG).show();

                }
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

    public void btn_continuar(View view) throws IOException, JSONException {
        Intent inten = new Intent(Recarga.this, Preg_frec.class);
        Bundle bundle = new Bundle();
        inten.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        bundle.putString("idRuta", "recarga");
        bundle.putString("Pasadas", Pasadas);
        bundle.putString("cut_codigo", cut_codigo);
        bundle.putString("placa", Placa);
        inten.putExtras(bundle);
        startActivity(inten);
        finish();
    }

    public void btn_Term() throws IOException, JSONException {
        Intent inten = new Intent(Recarga.this, Term_Cond.class);
        Bundle bundle = new Bundle();
        inten.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        bundle.putString("idRuta", "recarga");
        bundle.putString("Pasadas", Pasadas);
        bundle.putString("cut_codigo", cut_codigo);
        bundle.putString("placa", Placa);
        inten.putExtras(bundle);
        startActivity(inten);
        finish();
    }

    private void makeLinksFocusable(TextView tv) {
        MovementMethod m = tv.getMovementMethod();
        if ((m == null) || !(m instanceof LinkMovementMethod)) {
            if (tv.getLinksClickable()) {
                tv.setMovementMethod(LinkMovementMethod.getInstance());
            }
        }
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

    private SpannableString makeLinkSpan(CharSequence text, View.OnClickListener listener) {
        SpannableString link = new SpannableString(text);
        link.setSpan(new Login.ClickableString(listener), 0, text.length(),
                SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
        return link;
    }

    public void obtenervariablesSesion() {

        SharedPreferences preferencias = getSharedPreferences("datos", Context.MODE_PRIVATE);
        cl_codigo = preferencias.getString("cl_codigo", "");
        nombre = preferencias.getString("nombre", "");
        correo = preferencias.getString("correo", "");
        tipoDoc = preferencias.getString("tipoDoc", "");
        l = preferencias.getString("cedula", "");

    }
}