package com.example.cheli.tunelapp;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import Service.Service;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
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
    private final String CARPETA_RAIZ="TestApp/";
    private final String RUTA_IMG=CARPETA_RAIZ+"fotos";
    private  static final int COD_F=20;
    String path1="";
    Service service;
    private static final int PICK_IMAGE = 100;
    public String path="5bfa7b687ac3fc497a07fb2108d597854.jpg";
    private ImageView foto_matricula;
    private ImageView foto_cedula;
    private Button btn_hacerfoto;
    private Button btn_Galeria;
    private Button btn_Envio;
    int tipo=-1;
    boolean estado=false;
    boolean estado2=false;
    Bitmap bmp;
    Bitmap bm;
    String envia="";
    private String codigoUsuario = "";
    private String codigovehiculos = "";
    private String TramaRecida;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actu_plac_foto);
        setContentView(R.layout.activity_actu_plac_foto);
        validarPermiso();
        foto_matricula = findViewById(R.id.img_matricula);
        foto_cedula = findViewById(R.id.img_cedula);
        btn_hacerfoto =this.findViewById(R.id.btnMatricula);
        btn_Galeria =this.findViewById(R.id.btnCedula);
        btn_Envio =this.findViewById(R.id.btnContinuar2);
        btn_Envio.setEnabled(false);
        btn_hacerfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipo=0;
                final CharSequence[] options = {"Tomar Foto", "Elegir de Galeria", "Cancelar"};
                final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(Actu_plac_foto.this);
                builder.setTitle("Elige una opción");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (options[which] == "Tomar Foto") {
                            usarCamara();
                        } else if (options[which] == "Elegir de Galeria") {
                            abrirGaleria();
                        } else if (options[which] == "Canelar") {
                            dialog.dismiss();
                        }

                    }
                });
                builder.show();
            }
        });

        btn_Galeria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipo=1;
                final CharSequence[] options = {"Tomar Foto", "Elegir de Galeria", "Cancelar"};
                final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(Actu_plac_foto.this);
                builder.setTitle("Elige una opción");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (options[which] == "Tomar Foto") {
                            usarCamara();
                        } else if (options[which] == "Elegir de Galeria") {
                            abrirGaleria();
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
                enviarImagen(bytes.toByteArray());
                ByteArrayOutputStream bytes2 = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 90, bytes2);
                enviarImagen(bytes2.toByteArray());
                codigoUsuario=getIntent().getStringExtra("cl_codigo");
                codigovehiculos=getIntent().getStringExtra("cut_codigo");
                envia= "1," + codigoUsuario + "," + codigovehiculos;
                //ob.conectar();
                //ob.enviar(envia);
                //ob.cerrar();
                //Log.e("envia",envia);
                //TramaRecida = ob.cadena.toString();

                Toast.makeText(getApplicationContext(), "Sus documentos han sido enviados!", Toast.LENGTH_LONG).show();
            }
        });

    }

    public void abrirGaleria() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);

    }
    public void enviarImagen(byte[] imageBytes) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Service.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", "image.jpg", reqFile);
        Service service = retrofit.create(Service.class);
        service.agregarImagen(body).enqueue(new Callback<Imagen>() {
            @Override
            public void onResponse(Call<Imagen> call, Response<Imagen> response) {
                if (response.isSuccessful()) {
                    path = response.body().getImagePath();
                    Toast.makeText(getApplicationContext(), "Imagen guardada con éxito!", Toast.LENGTH_LONG).show();
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
                Toast.makeText(getApplicationContext(), "" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        int id;
        if (resultCode == Activity.RESULT_OK ) {

            switch (requestCode){
                case PICK_IMAGE:
                    try {
                        Uri selectedImage = data.getData();
                        InputStream imageStream = getContentResolver().openInputStream(selectedImage);
                        bmp = BitmapFactory.decodeStream(imageStream);
                        if(tipo==0){
                            foto_matricula.setImageBitmap(bmp);}
                        else{
                            foto_cedula.setImageBitmap(bmp);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    estado=true;

                    if (estado==true&&estado2==true){
                        btn_Envio.setEnabled(true);
                    }
                    break;
                case COD_F:
                    MediaScannerConnection.scanFile(this, new String[]{path1}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path1, Uri uri) {
                                    Log.i("Ruta ","Path1: "+path1);
                                }
                            });
                    bm=BitmapFactory.decodeFile(path1);
                    if(tipo==0){
                        foto_matricula.setImageBitmap(bm);}
                    else{
                        foto_cedula.setImageBitmap(bm);
                    }

                    estado2=true;
                    if (estado==true&&estado2==true){
                        btn_Envio.setEnabled(true);
                    }
                    break;

            }


        }


    }
    public void usarCamara(){
        String nombreImg="";
        File fi=new File(Environment.getExternalStorageDirectory(),RUTA_IMG);
        boolean creada=fi.exists();
        if(!creada){
            creada=fi.mkdirs();
        }
        if(creada){
            Long generar=System.currentTimeMillis()/1000;
            nombreImg=generar.toString()+".jpg";
        }
        path1=Environment.getExternalStorageDirectory()+File.separator+RUTA_IMG+File.separator+nombreImg;
        File imagen=new File(path1);
        Intent i=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri imageUri = FileProvider.getUriForFile(Actu_plac_foto.this, "com.example.cheli.webftp.provider", imagen);
        i.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(i,COD_F);

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
        final AlertDialog.Builder ao=new AlertDialog.Builder(Actu_plac_foto.this);
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
        AlertDialog.Builder diag=new AlertDialog.Builder(Actu_plac_foto.this);
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
}
