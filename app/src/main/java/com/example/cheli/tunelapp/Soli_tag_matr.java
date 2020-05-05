package com.example.cheli.tunelapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import Modelo.Imagen;
import Modelo.Pasadas;
import Service.Service;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Soli_tag_matr extends AppCompatActivity {
    String codigo_trama;
    String cl_codigo;
    Button btn_enviar;
    Button btn_matri1;
    Button btn_matri2;
    Button btn_matri3;
    String st_codigo;
    int tipo = -1;
    ImageView imgMatr1;
    ImageView imgMatr2;
    ImageView imgMatr3;
    private final String CARPETA_RAIZ = "TestApp/";
    private final String RUTA_IMG = CARPETA_RAIZ + "fotos";
    private static final int COD_F = 20;
    String path1 = "";
    Service service;
    Bitmap bmp;
    Bitmap bmp1;
    Bitmap bmp2;
    ImageView img;
    String lugares;
    AlertDialog dialog;
    private static final int PICK_IMAGE = 100;
    public String path = "";
    Bundle bundle = new Bundle();
    private cls_conexion ob;
    String cod_lug;

    int id;
    String url;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_soli_tag_matr);
        try {
            if (isOnline(this) == true) {
                ob = new cls_conexion(getString(R.string.servidor_tramas), 8200);
                SharedPreferences preferencias = getSharedPreferences("datos", Context.MODE_PRIVATE);
                cl_codigo = preferencias.getString("cl_codigo", "");
                codigo_trama = getIntent().getStringExtra("codigo_trama");
                st_codigo = getIntent().getStringExtra("st_codigo");
                lugares = getIntent().getStringExtra("lugares");
                url = getIntent().getStringExtra("url");

                btn_enviar = findViewById(R.id.btnEnviar);
                btn_matri1 = findViewById(R.id.btnMatricula1);
                btn_matri2 = findViewById(R.id.btnMatricula2);
                btn_matri3 = findViewById(R.id.btnMatricula3);
                imgMatr1 = findViewById(R.id.imageMatri1);
                imgMatr1.setVisibility(View.GONE);
                imgMatr2 = findViewById(R.id.imageMatri2);
                imgMatr2.setVisibility(View.GONE);
                imgMatr3 = findViewById(R.id.imageMatri3);
                imgMatr3.setVisibility(View.GONE);
                img = findViewById(R.id.img_regresar);
                btn_matri1.setEnabled(true);
                btn_matri2.setEnabled(false);
                btn_matri3.setEnabled(false);
                btn_enviar.setEnabled(false);
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
                btn_matri1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipo = 0;
                        final CharSequence[] options = {"Tomar Foto", "Elegir de Galeria", "Cancelar"};
                        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(Soli_tag_matr.this, R.style.CustomDialog);
                        builder.setTitle("Elige una opción");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (options[which] == "Tomar Foto") {
                                    try {
                                        usarCamara();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else if (options[which] == "Elegir de Galeria") {
                                    try {
                                        abrirGaleria();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else if (options[which] == "Canelar") {
                                    dialog.dismiss();
                                }

                            }
                        });
                        builder.show();
                    }
                });
                btn_matri2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipo = 1;
                        final CharSequence[] options = {"Tomar Foto", "Elegir de Galeria", "Cancelar"};
                        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(Soli_tag_matr.this, R.style.CustomDialog);
                        builder.setTitle("Elige una opción");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (options[which] == "Tomar Foto") {
                                    try {
                                        usarCamara();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else if (options[which] == "Elegir de Galeria") {
                                    try {
                                        abrirGaleria();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else if (options[which] == "Canelar") {
                                    dialog.dismiss();
                                }

                            }
                        });
                        builder.show();
                    }
                });
                btn_matri3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipo = 2;
                        final CharSequence[] options = {"Tomar Foto", "Elegir de Galeria", "Cancelar"};
                        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(Soli_tag_matr.this, R.style.CustomDialog);
                        builder.setTitle("Elige una opción");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (options[which] == "Tomar Foto") {
                                    try {
                                        usarCamara();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else if (options[which] == "Elegir de Galeria") {
                                    try {
                                        abrirGaleria();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else if (options[which] == "Canelar") {
                                    dialog.dismiss();
                                }

                            }
                        });
                        builder.show();
                    }
                });
                btn_enviar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            ListRetiroTag();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

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
        try {
            navegacion();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return super.onKeyDown(keyCode, event);
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }

    public void abrirGaleria() throws IOException, JSONException {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);

    }

    public void usarCamara() throws IOException, JSONException {
        String nombreImg = "";
        File fi = new File(Environment.getExternalStorageDirectory(), RUTA_IMG);
        boolean creada = fi.exists();
        if (!creada) {
            creada = fi.mkdirs();
        }
        if (creada) {
            Long generar = System.currentTimeMillis() / 1000;
            nombreImg = generar.toString() + ".jpg";
        }
        path1 = Environment.getExternalStorageDirectory() + File.separator + RUTA_IMG + File.separator + nombreImg;
        File imagen = new File(path1);
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri imageUri = FileProvider.getUriForFile(Soli_tag_matr.this, "com.example.cheli.webftp.provider", imagen);
        i.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(i, COD_F);

    }


    public void enviarImagen(byte[] imageBytes, String name) throws IOException, JSONException {
        try {
            if (isOnline(this) == true) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(url)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                RequestBody reqFile = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes);
                MultipartBody.Part body = MultipartBody.Part.createFormData("image", name + ".jpg", reqFile);
                Service service = retrofit.create(Service.class);
                service.agregarImagenSolicitud(body).enqueue(new Callback<Imagen>() {
                    @Override
                    public void onResponse(Call<Imagen> call, Response<Imagen> response) {
                        if (response.isSuccessful()) {
                            path = response.body().getImagePath();
                            Toast.makeText(getApplicationContext(), "Imagen guardada con éxito!", Toast.LENGTH_LONG).show();
                        } else {

                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                Toast.makeText(getApplicationContext(), "Error de coneccion" + jObjError.toString(), Toast.LENGTH_LONG).show();
                                //Intent i = new Intent(Soli_tag_matr.this, Soli_tag_matr.class );
                                //startActivity(i);
                                finish();
                            } catch (Exception e) {
                                //Intent i = new Intent(Soli_tag_matr.this, Soli_tag_matr.class );
                                // startActivity(i);
                                // finish();
                                Toast.makeText(getApplicationContext(), "Error de conección" + e.getMessage(), Toast.LENGTH_LONG).show();
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<Imagen> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Subir nuevamente las imagenes gracias!!", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(Soli_tag_matr.this, Soli_tag_matr.class);
                        startActivity(i);
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == Activity.RESULT_OK) {

            switch (requestCode) {
                case PICK_IMAGE:
                    try {
                        Uri selectedImage = data.getData();
                        InputStream imageStream = getContentResolver().openInputStream(selectedImage);
                        // bmp = BitmapFactory.decodeStream(imageStream);
                        if (tipo == 0) {
                            bmp = BitmapFactory.decodeStream(imageStream);
                            imgMatr1.setImageBitmap(bmp);
                            btn_matri2.setEnabled(true);
                            btn_enviar.setEnabled(true);
                            imgMatr1.setVisibility(View.VISIBLE);
                            id = 1;
                        }
                        if (tipo == 1) {
                            bmp1 = BitmapFactory.decodeStream(imageStream);
                            imgMatr2.setImageBitmap(bmp1);
                            btn_matri3.setEnabled(true);
                            btn_enviar.setEnabled(true);
                            imgMatr2.setVisibility(View.VISIBLE);
                            id = 2;
                        }
                        if (tipo == 2) {
                            bmp2 = BitmapFactory.decodeStream(imageStream);
                            imgMatr3.setImageBitmap(bmp2);
                            btn_enviar.setEnabled(true);
                            imgMatr3.setVisibility(View.VISIBLE);
                            id = 3;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
                case COD_F:
                    MediaScannerConnection.scanFile(this, new String[]{path1}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path1, Uri uri) {

                                }
                            });

                    if (tipo == 0) {
                        bmp = BitmapFactory.decodeFile(path1);
                        imgMatr1.setImageBitmap(bmp);
                        btn_matri2.setEnabled(true);
                        btn_enviar.setEnabled(true);
                        imgMatr1.setVisibility(View.VISIBLE);
                        id = 1;
                    }
                    if (tipo == 1) {
                        bmp1 = BitmapFactory.decodeFile(path1);
                        imgMatr2.setImageBitmap(bmp1);
                        btn_matri3.setEnabled(true);
                        btn_enviar.setEnabled(true);
                        imgMatr2.setVisibility(View.VISIBLE);
                        id = 2;
                    }
                    if (tipo == 2) {
                        bmp2 = BitmapFactory.decodeFile(path1);
                        imgMatr3.setImageBitmap(bmp2);
                        btn_enviar.setEnabled(true);
                        imgMatr3.setVisibility(View.VISIBLE);
                        id = 3;
                    }
                    break;


            }

        }


    }

    public void envio_matriculas(String st_codigoenvio) throws IOException, JSONException {
        if (id == 1) {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
            String name1 = "m-" + st_codigo;

            enviarImagen(bytes.toByteArray(), name1);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (id == 2) {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            ByteArrayOutputStream bytes1 = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
            bmp1.compress(Bitmap.CompressFormat.JPEG, 90, bytes1);
            String name1 = "m1-" + st_codigoenvio;
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            enviarImagen(bytes.toByteArray(), name1);
            String name2 = "m2-" + st_codigo;
            enviarImagen(bytes1.toByteArray(), name2);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (id == 3) {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            ByteArrayOutputStream bytes1 = new ByteArrayOutputStream();
            ByteArrayOutputStream bytes2 = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
            bmp1.compress(Bitmap.CompressFormat.JPEG, 90, bytes1);
            bmp2.compress(Bitmap.CompressFormat.JPEG, 90, bytes2);
            String name1 = "m1-" + st_codigoenvio;
            enviarImagen(bytes.toByteArray(), name1);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String name2 = "m2-" + st_codigoenvio;
            enviarImagen(bytes1.toByteArray(), name2);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String name3 = "m3-" + st_codigoenvio;
            enviarImagen(bytes2.toByteArray(), name3);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void ListRetiroTag() throws IOException, JSONException {

        String[] vectorLugares = lugares.split("\\*");
        AlertDialog.Builder mbuilder = new AlertDialog.Builder(Soli_tag_matr.this, R.style.CustomDialog);
        View mView = getLayoutInflater().inflate(R.layout.list_lug_tag, null);
        mbuilder.setMessage("Seleccione el lugar en el cual desea retirar su TAG");
        mbuilder.setTitle("Lugares de retiro de TAG");
        Button btnsolicitar = (Button) mView.findViewById(R.id.btnContinuarLug);
        final EditText editText = mView.findViewById(R.id.editTextDireccion);
        final EditText editText1 = mView.findViewById(R.id.editTextCodPostal);
        final EditText editText2 = mView.findViewById(R.id.editTextCodReferencia);
        editText.setVisibility(View.GONE);
        editText1.setVisibility(View.GONE);
        editText2.setVisibility(View.GONE);
        final Spinner spn_lug_tag = (Spinner) mView.findViewById(R.id.spn_tipo_lugares);


        final ArrayList<Modelo.LugRetiro> listLug = new ArrayList<Modelo.LugRetiro>();
        String vectorLug[];
        if (vectorLugares.length > 0) {
            for (Integer i = 0; i < (vectorLugares.length); i++) {
                vectorLug = vectorLugares[i].split("\\?");
                if (vectorLug.length > 0) {
                    listLug.add(new Modelo.LugRetiro(vectorLug[0], vectorLug[1]));

                }

            }
            ArrayAdapter<Modelo.LugRetiro> adapter = new ArrayAdapter<Modelo.LugRetiro>(this, R.layout.spinner_item_global, listLug);
            spn_lug_tag.setAdapter(adapter);
        }

        spn_lug_tag.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String p = spn_lug_tag.getItemAtPosition(position).toString();
                cod_lug = listLug.get(position).getCod_lug().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        btnsolicitar.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                try {
                                                    if (isOnline(getApplicationContext()) == true) {
                                                        if (codigo_trama.equals(getString(R.string.cm_contrato_comp_tag_cl_nuevo))) {
                                                            Bundle bundle = new Bundle();
                                                            bundle.putString("st_codigo", st_codigo);
                                                            bundle.putString("codigo_trama", getString(R.string.cm_contrato_comp_tag_cl_nuevo));

                                                            if (cod_lug.equals("3")) {
                                                                editText.setVisibility(View.VISIBLE);
                                                                editText1.setVisibility(View.VISIBLE);
                                                                editText2.setVisibility(View.VISIBLE);
                                                                if (!editText.getText().toString().equals("") || !editText1.getText().toString().equals("")) {
                                                                    bundle.putString("lug_ret_tag", cod_lug);
                                                                    String direccion = editText.getText().toString();
                                                                    bundle.putString("direccion", direccion);
                                                                    String cod_postal = editText1.getText().toString();
                                                                    bundle.putString("codi_post", cod_postal);
                                                                    String referencia = editText2.getText().toString();
                                                                    bundle.putString("referencia", referencia);
                                                                    bundle.putString("lugares", lugares);
                                                                    try {
                                                                        envio_matriculas(st_codigo);
                                                                    } catch (IOException e) {
                                                                        e.printStackTrace();
                                                                    } catch (JSONException e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                    Intent i = new Intent(Soli_tag_matr.this, Contrato.class);
                                                                    i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                                                    i.putExtras(bundle);
                                                                    dialog.cancel();
                                                                    startActivity(i);
                                                                    finish();
                                                                } else {
                                                                    Toast.makeText(getApplicationContext(), "Ingrese la dirección Domiciliaria o el codigo postal", Toast.LENGTH_LONG).show();
                                                                }
                                                            } else {
                                                                bundle.putString("lug_ret_tag", cod_lug);
                                                                try {
                                                                    envio_matriculas(st_codigo);
                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                } catch (JSONException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                Intent i = new Intent(Soli_tag_matr.this, Contrato.class);
                                                                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                                                i.putExtras(bundle);
                                                                dialog.cancel();
                                                                startActivity(i);
                                                                finish();
                                                            }


                                                        }
                                                        if (codigo_trama.equals(getString(R.string.cm_contrato_comp_tag_cl_antiguo))) {

                                                            String enviar = getString(R.string.cm_soli_tag_matr) + "," + cl_codigo;
                                                            ob.conectar();
                                                            ob.enviar(enviar);
                                                            ob.cerrar();
                                                            String TramaRecibida = ob.cadena.toString();
                                                            //String TramaRecibida="1|1001|url";
                                                            String vectorRecibido[] = TramaRecibida.split("\\|");
                                                            String CodigoRespuesta = vectorRecibido[0];
                                                            if (vectorRecibido.length == 4) {
                                                                if (CodigoRespuesta.equals("1")) {
                                                                    st_codigo = vectorRecibido[2];
                                                                    url = vectorRecibido[3];
                                                                    bundle.putString("st_codigo", st_codigo);
                                                                    bundle.putString("codigo_trama", getString(R.string.cm_contrato_comp_tag_cl_antiguo));

                                                                    if (cod_lug.equals("3")) {
                                                                        editText.setVisibility(View.VISIBLE);
                                                                        editText1.setVisibility(View.VISIBLE);
                                                                        editText2.setVisibility(View.VISIBLE);
                                                                        if (!editText.getText().toString().equals("")) {
                                                                            bundle.putString("lug_ret_tag", cod_lug);
                                                                            String direccion = editText.getText().toString();
                                                                            bundle.putString("direccion", direccion);
                                                                            String cod_postal = editText1.getText().toString();
                                                                            bundle.putString("codi_post", cod_postal);
                                                                            bundle.putString("st_codigo", st_codigo);
                                                                            try {
                                                                                envio_matriculas(st_codigo);
                                                                            } catch (IOException e) {
                                                                                e.printStackTrace();
                                                                            } catch (JSONException e) {
                                                                                e.printStackTrace();
                                                                            }
                                                                            String referencia = editText2.getText().toString();
                                                                            bundle.putString("referencia", referencia);
                                                                            Intent i = new Intent(Soli_tag_matr.this, Contrato.class);
                                                                            i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                                                            i.putExtras(bundle);
                                                                            dialog.cancel();
                                                                            startActivity(i);
                                                                            finish();
                                                                        } else {
                                                                            Toast.makeText(getApplicationContext(), "Ingrese la dirección Domiciliaria o el codigo postal", Toast.LENGTH_LONG).show();
                                                                        }
                                                                    } else {

                                                                        bundle.putString("st_codigo", st_codigo);
                                                                        bundle.putString("codigo_trama", getString(R.string.cm_contrato_comp_tag_cl_antiguo));
                                                                        bundle.putString("lug_ret_tag", cod_lug);
                                                                        bundle.putString("st_codigo", st_codigo);
                                                                        try {
                                                                            envio_matriculas(st_codigo);
                                                                        } catch (IOException e) {
                                                                            e.printStackTrace();
                                                                        } catch (JSONException e) {
                                                                            e.printStackTrace();
                                                                        }
                                                                        Intent i = new Intent(Soli_tag_matr.this, Contrato.class);
                                                                        i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                                                        i.putExtras(bundle);
                                                                        dialog.cancel();
                                                                        startActivity(i);
                                                                        finish();
                                                                    }
                                                                }
                                                            } else {
                                                                Toast.makeText(getApplicationContext(), R.string.g_error_servidor, Toast.LENGTH_LONG).show();
                                                            }

                                                            if (vectorRecibido.length == 2) {
                                                                if (CodigoRespuesta.equals("2")) {
                                                                    String Mensaje = vectorRecibido[1];
                                                                    Toast.makeText(getApplicationContext(), Mensaje, Toast.LENGTH_LONG).show();
                                                                }
                                                            } else {
                                                                Toast.makeText(getApplicationContext(), R.string.g_error_servidor, Toast.LENGTH_LONG).show();
                                                            }

                                                        }

                                                    } else {
                                                        Toast.makeText(getApplicationContext(), R.string.g_error_internet, Toast.LENGTH_LONG).show();
                                                    }
                                                } catch (Exception e) {
                                                    Toast.makeText(getApplicationContext(), R.string.g_error_global, Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        }


        );
        mbuilder.setView(mView);
        dialog = mbuilder.create();
        dialog.show();

    }

    public void navegacion() throws IOException, JSONException {
        if (codigo_trama.equals(getString(R.string.cm_contrato_comp_tag_cl_antiguo))) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Soli_tag_matr.this, R.style.CustomDialog);
            builder.setTitle("Telepeaje");
            builder.setMessage("Desea abandonar el proceso de solicitud de tag?");
            builder.setPositiveButton("SALIR", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Bundle bundle3 = new Bundle();
                    Intent intent = new Intent(Soli_tag_matr.this, Menu.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    intent.putExtras(bundle3);
                    startActivity(intent);
                    finish();
                }
            });
            builder.setNegativeButton("CONTINUAR", null);
            Dialog dialog = builder.create();
            dialog.show();
        }


        if (codigo_trama.equals(getString(R.string.cm_contrato_comp_tag_cl_nuevo))) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Soli_tag_matr.this, R.style.CustomDialog);
            builder.setTitle("Telepeaje");
            builder.setMessage("Desea abandonar el proceso de solicitud de tag?");
            builder.setPositiveButton("SALIR", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Soli_tag_matr.this, Login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    finish();
                }
            });
            builder.setNegativeButton("CONTINUAR", null);
            Dialog dialog = builder.create();
            dialog.show();
        }
    }
}
