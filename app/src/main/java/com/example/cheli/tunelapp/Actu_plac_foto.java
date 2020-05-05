package com.example.cheli.tunelapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import Service.Service;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import Modelo.Imagen;
import Service.Service;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Actu_plac_foto extends AppCompatActivity {
    private final String CARPETA_RAIZ = "TestApp/";
    private final String RUTA_IMG = CARPETA_RAIZ + "fotos";
    private static final int COD_F = 20;
    String path1 = "";
    private static final int PICK_IMAGE = 100;
    public String path = "";
    private ImageView foto_matricula;
    private ImageView foto_cedula;
    private Button btn_matricula;
    private Button btn_cedula;
    private Button btn_Envio;
    int tipo = -1;
    Bitmap bmp;
    Bitmap bm;
    private String codigoUsuario = "";
    private String codigovehiculo = "";
    ImageView img;
    String nombre;
    String correo;
    String tipoDoc;
    String l;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_actu_plac_foto);
        //validarPermiso();

        try {
            if (isOnline(this) == true) {
                foto_matricula = findViewById(R.id.img_matricula);
                foto_matricula.setVisibility(View.GONE);
                foto_cedula = findViewById(R.id.img_cedula);
                foto_cedula.setVisibility(View.GONE);
                btn_matricula = this.findViewById(R.id.btnMatricula);
                btn_cedula = this.findViewById(R.id.btnCedula);
                btn_cedula.setEnabled(false);
                btn_Envio = this.findViewById(R.id.btnContinuar2);
                btn_Envio.setEnabled(false);
                try {
                    obtenerDatos();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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

                btn_matricula.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipo = 0;
                        final CharSequence[] options = {"Tomar Foto", "Elegir de Galeria", "Cancelar"};
                        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(Actu_plac_foto.this, R.style.CustomDialog);
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

                btn_cedula.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipo = 1;
                        final CharSequence[] options = {"Tomar Foto", "Elegir de Galeria", "Cancelar"};
                        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(Actu_plac_foto.this, R.style.CustomDialog);
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
                                } else if (options[which] == "Cancelar") {
                                    dialog.dismiss();
                                }

                            }
                        });
                        builder.show();
                    }
                });
                btn_Envio.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        bmp.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                        String name1 = "c-" + codigoUsuario + "-" + codigovehiculo;
                        try {

                            enviarImagen(bytes.toByteArray(), name1);
                            Thread.sleep(2000);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        ByteArrayOutputStream bytes2 = new ByteArrayOutputStream();
                        bm.compress(Bitmap.CompressFormat.JPEG, 90, bytes2);
                        String name = "m-" + codigoUsuario + "-" + codigovehiculo;
                        try {

                            enviarImagen(bytes2.toByteArray(), name);
                            Thread.sleep(2000);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        try {
                            ver_contrato();
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

    public void ver_contrato() throws IOException, JSONException {

        //Toast.makeText(getApplicationContext(), "Imagen guardada con éxito!", Toast.LENGTH_LONG).show();
        Bundle bundle = new Bundle();
        bundle.putString("cut_codigo", codigovehiculo);
        bundle.putString("codigo_trama", getString(R.string.cm_contrato_camb_vehi));
        Intent i = new Intent(Actu_plac_foto.this, Contrato.class);
        i.putExtras(bundle);
        startActivity(i);
        finish();
    }

    public void abrirGaleria() throws IOException, JSONException {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);

    }

    public void enviarImagen(byte[] imageBytes, String name) throws IOException, JSONException {
        try {
            if (isOnline(this) == true) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Service.URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                RequestBody reqFile = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes);
                MultipartBody.Part body = MultipartBody.Part.createFormData("image", name + ".jpg", reqFile);
                Service service = retrofit.create(Service.class);
                service.agregarImagenCambio(body).enqueue(new Callback<Imagen>() {
                    @Override
                    public void onResponse(Call<Imagen> call, Response<Imagen> response) {
                        if (response.isSuccessful()) {
                            path = response.body().getImagePath();
                            //Toast.makeText(getApplicationContext(), "Imagen guardada con éxito!", Toast.LENGTH_LONG).show();
                        } else {
                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                Toast.makeText(getApplicationContext(), jObjError.toString(), Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Imagen> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Vuelva a cargar las imagenes. Gracias", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(Actu_plac_foto.this, Actu_plac_foto.class);
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

        int id;
        if (resultCode == Activity.RESULT_OK) {

            switch (requestCode) {
                case PICK_IMAGE:
                    try {
                        Uri selectedImage = data.getData();
                        InputStream imageStream = getContentResolver().openInputStream(selectedImage);

                        if (tipo == 0) {
                            bmp = BitmapFactory.decodeStream(imageStream);
                            foto_matricula.setImageBitmap(bmp);
                            btn_cedula.setEnabled(true);
                            foto_matricula.setVisibility(View.VISIBLE);
                        }

                        if (tipo == 1) {
                            bm = BitmapFactory.decodeStream(imageStream);
                            foto_cedula.setImageBitmap(bm);
                            btn_Envio.setEnabled(true);
                            foto_cedula.setVisibility(View.VISIBLE);
                        }

                        break;
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
                        foto_matricula.setImageBitmap(bmp);
                        btn_cedula.setEnabled(true);
                        foto_matricula.setVisibility(View.VISIBLE);
                    }
                    if (tipo == 1) {
                        bm = BitmapFactory.decodeFile(path1);
                        foto_cedula.setImageBitmap(bm);
                        btn_Envio.setEnabled(true);
                        foto_cedula.setVisibility(View.VISIBLE);
                    }

                    break;
            }


        }


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
        Uri imageUri = FileProvider.getUriForFile(Actu_plac_foto.this, "com.example.cheli.webftp.provider", imagen);
        i.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(i, COD_F);

    }

    public void navegacion() throws IOException, JSONException {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomDialog);
            builder.setTitle("Telepeaje");
            builder.setMessage("Desea abandonar el proceso de cambio de vehiculo?");
            builder.setPositiveButton("SALIR", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Actu_plac_foto.this, Menu.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    finish();
                }
            });
            builder.setNegativeButton("CONTINUAR", null);
            Dialog dialog = builder.create();
            dialog.show();
        } catch (Exception e) {
            Toast.makeText(this, R.string.g_error_global, Toast.LENGTH_LONG).show();
        }
    }

    public void obtenerDatos() throws IOException, JSONException {
        try {
            SharedPreferences preferencias = getSharedPreferences("datos", Context.MODE_PRIVATE);
            codigoUsuario = preferencias.getString("cl_codigo", "");
            codigovehiculo = getIntent().getStringExtra("cut_codigo");

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