package com.example.cheli.tunelapp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.TestLooperManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.annotation.SuppressLint;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class Pago extends AppCompatActivity {
    private ListView listTags;
    private List<cls_list_pago> pagos;
    private String[] vectorPagos;
    private String[] vectorPago;
    TextView valorsinIva;
    TextView valorConIva;
    TextView valorIva;
    CheckBox chkTerCond;
    private cls_conexion ob;
    TextView porcIva;
    TextView valorTotal;
    private cls_list_pago_adap ListPagoAdapter;
    String cl_codigo;
    String Mensaje;
    String tramaPago;
    String mensaje ;
    String cv_codigos;
    String valorTotal1;
    String valorConIva1;
    String valorSinIva;
    String valorIva1;
    String porcIva1;
    String Usuario;
    String correo;
    TextView indicacion1;
    String tipoDoc;
    String l;
    String vehiculos;
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_pago);
        chkTerCond=findViewById(R.id.chk_acep_term);
        chkTerCond.setChecked(false);
        try{
            if (isOnline(this) == true) {
        ob=new cls_conexion(getString(R.string.servidor_tramas), 8200);
        listTags=(ListView)findViewById(R.id.lsvw_pagos);
        valorsinIva=findViewById(R.id.sinIVA);
        valorConIva=findViewById(R.id.conIVA);
        valorIva=findViewById(R.id.importeIVA);
        porcIva=findViewById(R.id.porcenIVA);
        valorTotal=findViewById(R.id.totalPago);
        indicacion1 = (TextView)findViewById(R.id.textTerm);
        pagos = new ArrayList<>();
        pagos.clear();
        listTags.setAdapter(null);
        tipoDoc= getIntent().getStringExtra("tipoDoc");
        l= getIntent().getStringExtra("cedula");
        String content = "Para continuar con el pago debe aceptar los terminos y condiciones.Si desea ver los terminos y condiciones ";
        indicacion1.setText(content);
        SpannableString btn_soli_cont = makeLinkSpan("dar click aquí", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString("valorTotal",valorTotal1);
                bundle.putString("valorConIva",valorConIva1);
                bundle.putString("valorSinIva",valorSinIva);
                bundle.putString("valorIva",valorIva1);
                bundle.putString("porcIva",porcIva1);
                bundle.putString("nombre",Usuario);
                bundle.putString("pagos",tramaPago);
                bundle.putString("cl_codigo",cl_codigo);
                bundle.putString("correo",correo);
                bundle.putString("idRuta","pago");
                bundle.putString("cv_codigos",cv_codigos);
                bundle.putString("tipoDoc",tipoDoc);
                bundle.putString("cedula",l);
                Intent inten = new Intent(Pago.this, Term_Cond.class );
                inten.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                inten.putExtras(bundle);
                startActivity(inten);
                finish();
            }
        });


        indicacion1.append(btn_soli_cont);
        makeLinksFocusable(indicacion1);

        mensaje =getIntent().getStringExtra("mensaje");
        cv_codigos =getIntent().getStringExtra("cv_codigos");
        valorTotal1 =getIntent().getStringExtra("valorTotal");
        valorConIva1 =getIntent().getStringExtra("valorConIva");
        valorSinIva =getIntent().getStringExtra("valorSinIva");
        valorIva1 =getIntent().getStringExtra("valorIva");
        porcIva1 =getIntent().getStringExtra("porcIva");
        cl_codigo=getIntent().getStringExtra("cl_codigo");
        tramaPago =getIntent().getStringExtra("pagos");
        Usuario=getIntent().getStringExtra("nombre");
        correo=getIntent().getStringExtra("correo");
        vehiculos = getIntent().getStringExtra("Vehiculos");
        valorsinIva.setText(valorSinIva);
        valorConIva.setText(valorConIva1);
        valorIva.setText(valorIva1);
        porcIva.setText(porcIva1);
        valorTotal.setText(valorTotal1);
        valorTotal=findViewById(R.id.totalPago);


        vectorPagos=tramaPago.split("\\*");
        if (vectorPagos.length > 0)
        {
            for (Integer i=0;i<(vectorPagos.length);i++)
            {
                vectorPago=vectorPagos[i].split("\\?");
                if (vectorPago.length > 0) {
                    pagos.add(new cls_list_pago( vectorPago[0], "   " + vectorPago[2],vectorPago[1],"Concepto: " + vectorPago[3], "Valor: " + vectorPago[4]+" Iva: "+vectorPago[5]+" Valor Total: "+vectorPago[6], "Vehiculo: " + vectorPago[9], "Fecha: " + vectorPago[8]));

                }

            }
            ListPagoAdapter = new cls_list_pago_adap(Pago.this,pagos);
            listTags.setAdapter(ListPagoAdapter);
        }

        registerForContextMenu(listTags);
        ImageView img = (ImageView) findViewById(R.id.img_regresar);
        img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Pago.this, Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                finish();
            }
        });}else{
            Toast.makeText(this, R.string.g_error_internet, Toast.LENGTH_LONG).show();
        }}catch (Exception e){
            Toast.makeText(this, R.string.g_error_global, Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Intent intent = new Intent(Pago.this, Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

public void btn_continuar(View view)throws IOException, JSONException{
        try{
    Intent inten = new Intent(Pago.this, Preg_frec.class );
    Bundle bundle=new Bundle();
    inten.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
    bundle.putString("pagos",tramaPago);
    bundle.putString("valorTotal",valorTotal1);
    bundle.putString("valorConIva",valorConIva1);
    bundle.putString("valorSinIva",valorSinIva);
    bundle.putString("valorIva",valorIva1);
    bundle.putString("porcIva",porcIva1);
    bundle.putString("nombre",Usuario);
    bundle.putString("correo",correo);
    bundle.putString("idRuta","pago");
    bundle.putString("cl_codigo",cl_codigo);
    bundle.putString("cv_codigos",cv_codigos);
    bundle.putString("tipoDoc",tipoDoc);
    bundle.putString("cedula",l);
    bundle.putString("codigoTrama",getString(R.string.cm_hist_pagos));
    bundle.putString("cv_codigos",cv_codigos);
    bundle.putString("Vehiculos" ,vehiculos);
    inten.putExtras(bundle);
    startActivity(inten);
    finish();}catch (Exception e){
            Toast.makeText(this, R.string.g_error_global, Toast.LENGTH_LONG).show();
        }
}
    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }
public void pago(View view) throws IOException, JSONException {
        try{
            if (isOnline(this) == true) {
    if (chkTerCond.isChecked()){
        String login = "c79b9e59a7e8ae75e753e0b00515e624";
        String tranKey = "MQ463Hc4A3az8jVq";


    String envia= getString(R.string.cm_report_ini_pago)+","+cl_codigo+","+cv_codigos+","+valorTotal1;
    ob.conectar();
    ob.enviar(envia);
    ob.cerrar();
    String TramaRecida = ob.cadena.toString();

    //String TramaRecida = "1|mensaje|123456|2020-04-22T17:50:49-05:00|"+login+"|"+tranKey;
     String[] vector_sesion=TramaRecida.split("\\|");
    String codigoRespuesta=vector_sesion[0];
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
            String tipoDoc1 =tipoDoc ;
            //String vectorNombre[] = Usuario.split(" ");
            String nUsuario = Usuario;
            String apUsuario = "";
            String email = correo;
            String locale = "es_EC";
            String description = "Pago Tunel Guayasamin";
            String currency = "USD";
            String total = valorTotal1;
            String UrlRetorno="http://200.105.229.50/sgt_tele_quito/pages/proc/sgt_estado_pago_app.aspx";
            //String expiration="2020-04-10T17:50:49-05:00";
            double amount = Double.parseDouble(valorIva1);
            Double base = Double.parseDouble(valorSinIva);

            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");

            RequestBody body = RequestBody.create(mediaType, "{\"auth\": {\r\n\"login\": \"" + login1 + "\",\r\n\"seed\": \"" + seed + "\",\r\n\"nonce\": \"" + nonce + "\",\r\n\"tranKey\": \"" + trankey1 + "\"\r\n},\r\n\"locale\": \"" + locale + "\",\r\n\"buyer\": {\r\n\"document\": \"" + ncedula + "\",\r\n\"documentType\": \"" + tipoDoc1 + "\",\r\n\"name\": \"" + nUsuario + "\",\r\n\"surname\": \"" + apUsuario + "\",\r\n\"email\": \"" + email + "\"\r\n},\"payment\":{\"reference\":\"" + reference + "\",\"description\":\"" + description + "\",\"amount\":{\"currency\":\"" + currency + "\",\"total\":\"" + total + "\",\"taxes\":[{\"kind\":\"valueAddedTax\",\"amount\":" + amount + ",\"base\":" + base + "}]},\r\n\"allowPartial\": false\r\n},\r\n\"expiration\": \"" + expiration + "\",\r\n\"returnUrl\": \""+UrlRetorno+"\",\r\n\"skipResult\": false,\r\n\"userAgent\": \"Chrome/52.0.2743.82\",\r\n\"ipAddress\": \"127.0.0.1\"\r\n}");

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
                String Tramaenvio = getString(R.string.cm_report_sesion) +"," + cl_codigo+ "," + reference +","+requestId;
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
                        bundle.putString("idRuta","pago");
                        bundle.putString("login",login1);
                        bundle.putString("seed",seed);
                        bundle.putString("nonce",nonce);
                        bundle.putString("trnakey",trankey1);
                        bundle.putString("sesion",requestId);
                        bundle.putString("referencia",reference);
                        bundle.putString("cl_codigo",cl_codigo);
                        bundle.putString("Vehiculos" ,vehiculos);
                        bundle.putString("nombre",Usuario);
                        bundle.putString("correo",correo);
                        Intent i = new Intent(Pago.this, Web_View_Pago.class);
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
    }else {
        Toast.makeText(this, getString(R.string.g_error_servidor), Toast.LENGTH_LONG);
    }
    if (codigoRespuesta.equals("2")) {
        if (vector_sesion.length == 2) {
            Mensaje = vector_sesion[1];
            Toast.makeText(this, Mensaje, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, getString(R.string.g_error_servidor), Toast.LENGTH_LONG).show();
        }
    }}else{
        Toast.makeText(this, "Para continuar con el pago se debe aceptar terminos y condiciones", Toast.LENGTH_LONG).show();

    }}else{
        Toast.makeText(this, R.string.g_error_internet, Toast.LENGTH_LONG).show();
    }
        }catch (Exception e){
            Toast.makeText(this, R.string.g_error_global, Toast.LENGTH_LONG).show();
        }
    }




    public  void list_Hist_Pagos(View view){
        try {
            if (isOnline(this) == true) {
        String envia= getString(R.string.cm_hist_pagos)+","+cl_codigo;
        ob.conectar();
        ob.enviar(envia);
        ob.cerrar();
        String TramaRecida = ob.cadena.toString();

        //String TramaRecida = "1|mensaje|121020406080?PTJ-560?80 pasadas?20/01/2020?Fybeca?OK?Factura:006-021-000008967*121020406080?PTJ-560?20/01/2020?80 pasadas?Fybeca?Factura:006-021-000008967?Pendientes";

        //String TramaRecida = "1|Tiene valores pendientes|205*206*207|$96.8|$80|$15|$1.81|12%|205-123456789-PBC6530-Recarga-$80-$0.00-2020/03/12-006_021_000005896-CHEVROLET AVEO ROJO*206-123456789-PBC6530-Recarga-$80-$0.00-2020/03/12-006_021_000005896-CHEVROLET AVEO ROJO*206-123456789-PBC6530-Tag-$15-$1.80-2020/03/12-006_021_000005896-CHEVROLET AVEO ROJO";
        String[] vector_hist_Pagos=TramaRecida.split("\\|");
        String codigoRespuesta=vector_hist_Pagos[0];
        if (codigoRespuesta.equals("1")) {
            if (vector_hist_Pagos.length == 3) {
                String mensaje = vector_hist_Pagos[1];
                String hist_pagos = vector_hist_Pagos[2];
                //Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
                Bundle bundle=new Bundle();
                bundle.putString("valorTotal",valorTotal1);
                bundle.putString("valorConIva",valorConIva1);
                bundle.putString("valorSinIva",valorSinIva);
                bundle.putString("valorIva",valorIva1);
                bundle.putString("porcIva",porcIva1);
                bundle.putString("nombre",Usuario);
                bundle.putString("hist_pagos",hist_pagos);
                bundle.putString("pagos",tramaPago);
                bundle.putString("codigoTrama",getString(R.string.cm_hist_pagos));
                bundle.putString("idRuta","pago");
                bundle.putString("cl_codigo",cl_codigo);
                bundle.putString("correo",correo);
                bundle.putString("cv_codigos",cv_codigos);
                bundle.putString("tipoDoc",tipoDoc);
                bundle.putString("cedula",l);
                bundle.putString("Vehiculos" ,vehiculos);
                Intent inten = new Intent(Pago.this, Hist_pago.class );
                inten.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                inten.putExtras(bundle);
                startActivity(inten);
                finish();

            }else{
            Toast.makeText(this, getString(R.string.g_error_servidor), Toast.LENGTH_LONG).show();
        }

        if (codigoRespuesta.equals("2")) {
            if (vector_hist_Pagos.length == 2) {
                Mensaje = vector_hist_Pagos[1];
                Toast.makeText(this, Mensaje, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, getString(R.string.g_error_servidor), Toast.LENGTH_LONG).show();
            }
        }
            if (codigoRespuesta.equals("3")) {
                String Mensaje;
                if (vector_hist_Pagos.length == 2) {
                    Mensaje = vector_hist_Pagos[1];
                    Toast.makeText(this, Mensaje, Toast.LENGTH_LONG).show();


                } else {
                    Toast.makeText(this, getString(R.string.g_error_servidor), Toast.LENGTH_LONG).show();
                }
            }

    }}else{
                Toast.makeText(this, R.string.g_error_internet, Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
            Toast.makeText(this, R.string.g_error_global, Toast.LENGTH_LONG).show();
        }
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
    private SpannableString makeLinkSpan(CharSequence text, View.OnClickListener listener)                 {
        SpannableString link = new SpannableString(text);
        link.setSpan(new Login.ClickableString(listener), 0, text.length(),
                SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
        return link;
    }

}
