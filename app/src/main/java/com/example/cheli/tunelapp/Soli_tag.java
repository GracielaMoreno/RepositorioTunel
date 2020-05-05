package com.example.cheli.tunelapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
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
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;

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

public class Soli_tag extends AppCompatActivity {
    EditText textcedula;
    EditText textNombre;
    EditText textApellido;
    EditText textDomicilio;
    EditText textTelFijo;
    EditText textTelMovil;
    EditText textCorreo;
    Button btn_Enviar;
    Button btnCedula;
    ImageView imgCedula;
    int tipo = -1;
    private final String CARPETA_RAIZ = "TestApp/";
    private final String RUTA_IMG = CARPETA_RAIZ + "fotos";
    private static final int COD_F = 20;
    String path1 = "";
    Bitmap bmp;
    private static final int PICK_IMAGE = 100;
    public String path = "";
    ImageView img;
    boolean valid = false;
    String lugares;
    private cls_conexion ob;
    Spinner var_spnTipoDoc;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_soli_tag);

        try {
            if (isOnline(this) == true) {
                ob = new cls_conexion(getString(R.string.servidor_tramas), 8200);
                textcedula = findViewById(R.id.editCedula);
                textNombre = findViewById(R.id.editNombre);
                textApellido = findViewById(R.id.editApellido);
                textDomicilio = findViewById(R.id.editDirecc);
                textTelFijo = findViewById(R.id.editTelefono1);
                textTelMovil = findViewById(R.id.editTelefono2);
                textCorreo = findViewById(R.id.editCorreo);
                btn_Enviar = findViewById(R.id.btnEnviar);
                btn_Enviar.setEnabled(false);
                imgCedula = findViewById(R.id.imageCedula);
                imgCedula.setVisibility(View.GONE);
                img = findViewById(R.id.img_regresar);
                btnCedula = findViewById(R.id.btnCedula);
                String[] TipoDoc = {"CEDULA", "RUC", "PASAPORTE"};
                var_spnTipoDoc = (Spinner) findViewById(R.id.spinnerTipoDoc);
                var_spnTipoDoc.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item_global, TipoDoc));
                btnCedula.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipo = 0;
                        final CharSequence[] options = {"Tomar Foto", "Elegir de Galeria", "Cancelar"};
                        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(Soli_tag.this, R.style.CustomDialog);
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
                btn_Enviar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String cedula = textcedula.getText().toString();
                        String nombre = textNombre.getText().toString();
                        String apellido = textApellido.getText().toString();
                        String direccion = textDomicilio.getText().toString();
                        String telefono1 = textTelFijo.getText().toString();
                        String telefono2 = textTelMovil.getText().toString();
                        String correo = textCorreo.getText().toString();
                        int tipoDoc = var_spnTipoDoc.getSelectedItemPosition() + 1;
                        ;
                        if (!validarCampos()) {
                            Toast.makeText(getApplicationContext(), "Verifique que todos los campos esten llenos correctamente", Toast.LENGTH_LONG).show();

                        } else {
                            try {
                                llenarLugares();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                if (isOnline(getApplicationContext()) == true) {
                                    String enviar = getString(R.string.cm_soli_tag) + "," + cedula + "," + tipoDoc + "," + nombre + "," + apellido + "," + direccion + "," + telefono1 + "," + telefono2 + "," + correo;
                                    ob.conectar();
                                    ob.enviar(enviar);
                                    ob.cerrar();
                                    String TramaRecibida = ob.cadena.toString();
                                    //String TramaRecibida="1|1001|URL";
                                    String vectorRecibido[] = TramaRecibida.split("\\|");
                                    String CodigoRespuesta = vectorRecibido[0];
                                    if (vectorRecibido.length == 4) {
                                        if (CodigoRespuesta.equals("1")) {
                                            String st_codigo = vectorRecibido[2];
                                            String urlFotos = vectorRecibido[3];
                                            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                                            bmp.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                                            String name1 = "c-" + st_codigo;
                                            try {
                                                enviarImagen(bytes.toByteArray(), name1, urlFotos);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            Bundle bundle = new Bundle();
                                            bundle.putString("st_codigo", st_codigo);
                                            bundle.putString("codigo_trama", getString(R.string.cm_contrato_comp_tag_cl_nuevo));
                                            bundle.putString("lugares", lugares);
                                            bundle.putString("url", urlFotos);
                                            Log.e("", urlFotos);
                                            Intent i = new Intent(Soli_tag.this, Soli_tag_matr.class);
                                            i.putExtras(bundle);
                                            startActivity(i);

                                            finish();

                                        }
                                    } else {
                                        Toast.makeText(getApplicationContext(), R.string.g_error_servidor, Toast.LENGTH_LONG).show();
                                    }
                                    if (vectorRecibido.length == 2) {
                                        if (CodigoRespuesta.equals("2")) {
                                            String Mensaje = vectorRecibido[1];
                                            Toast.makeText(getApplicationContext(), Mensaje, Toast.LENGTH_LONG).show();
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
                });
                img.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(Soli_tag.this, Paso_Soli_Tag_No_Cli.class);
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
            Intent intent = new Intent(Soli_tag.this, Paso_Soli_Tag_No_Cli.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
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
        Uri imageUri = FileProvider.getUriForFile(Soli_tag.this, "com.example.cheli.webftp.provider", imagen);
        i.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(i, COD_F);

    }

    public void enviarImagen(byte[] imageBytes, String name, String Url) throws IOException, JSONException {
        try {
            if (isOnline(this) == true) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Url)
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

                            Toast.makeText(getApplicationContext(), "Error al guardar la imagen!", Toast.LENGTH_LONG).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<Imagen> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Vuelva a cargar las imagenes. Gracias", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(Soli_tag.this, Soli_tag.class);
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
                        bmp = BitmapFactory.decodeStream(imageStream);
                        imgCedula.setImageBitmap(bmp);
                        imgCedula.setVisibility(View.VISIBLE);
                        btn_Enviar.setEnabled(true);

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
                    bmp = BitmapFactory.decodeFile(path1);

                    imgCedula.setImageBitmap(bmp);
                    imgCedula.setVisibility(View.VISIBLE);
                    btn_Enviar.setEnabled(true);
            }

        }


    }

    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }

    public boolean validarCampos() {

        if (var_spnTipoDoc.getSelectedItem().toString().equals("CEDULA")) {
            if (textcedula.getText().length() == 10) {
                textcedula.setError(null);
                valid = true;
            } else {
                textcedula.setError("Ingrese un número correcto de cedula");
                valid = false;
            }
        }
        if (var_spnTipoDoc.getSelectedItem().toString().equals("RUC")) {
            if (textcedula.getText().length() == 13) {
                textcedula.setError(null);
                valid = true;
            } else {
                textcedula.setError("Ingrese un número correcto de RUC");
                valid = false;
            }
        }

        if (textTelFijo.getText().equals("")) {
            textTelFijo.setError("Ingrese un número de teléfono");
            valid = false;
        } else {
            textTelFijo.setError(null);
            valid = true;
        }
        if (textTelMovil.getText().equals("")) {
            textTelMovil.setError("Ingrese un número de celular");
            valid = false;
        } else {
            textTelMovil.setError(null);
            valid = true;
        }
        if (textTelFijo.getText().length() == 7) {
            textTelFijo.setError(null);
            valid = true;
        } else {
            textTelFijo.setError("Ingrese un número de teléfono");
            valid = false;

        }
        if (textTelMovil.getText().length() == 10) {
            textTelMovil.setError(null);
            valid = true;
        } else {
            textTelMovil.setError("Ingrese un número de celular");
            valid = false;

        }
        if (!textNombre.getText().toString().matches("[a-zA-Z ]+")) {
            textNombre.setError("Ingrese su nombre sin caracteres especiales");
            valid = false;
        } else {
            textNombre.setError(null);
            valid = true;
        }
        if (!textApellido.getText().toString().matches("[a-zA-Z ]+")) {
            textApellido.setError("Ingrese su apellido sin caracteres especiales");
            valid = false;
        } else {
            textApellido.setError(null);
            valid = true;
        }
        if (textDomicilio.getText().equals("")) {
            textDomicilio.setError("Ingrese la dirección domiciliaria");
            valid = false;
        } else {
            textDomicilio.setError(null);
            valid = true;
        }


        if (textCorreo.getText().toString().isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(textCorreo.getText().toString()).matches()) {
            textCorreo.setError("Ingrese un correo válido");
            valid = false;
        } else {
            textCorreo.setError(null);
            valid = true;
        }

        return valid;
    }

    public void llenarLugares() throws IOException, JSONException {
        try {
            if (isOnline(this) == true) {
                String envia = getString(R.string.cm_list_lug_tag) + ",0";
                ob.conectar();
                ob.enviar(envia);
                ob.cerrar();
                String TramaRecibida = ob.cadena.toString();
                //String TramaRecibida="1|1?Scala*2?Zona Norte*3?Domicilio";
                String[] vectorTramaRecibida = TramaRecibida.split("\\|");
                String CodigoResp = vectorTramaRecibida[0];
                if (vectorTramaRecibida.length == 2) {
                    if (CodigoResp.equals("1")) {
                        lugares = vectorTramaRecibida[1];
                    }
                    if (CodigoResp.equals("2")) {
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

}

