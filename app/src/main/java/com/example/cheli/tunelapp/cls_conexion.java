package com.example.cheli.tunelapp;

import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;


public class cls_conexion extends AsyncTask<Void, Void, Void> {
public String dstAddress;
public int dstPort;
public Socket socket = null;

public String cadena = "";

        cls_conexion(String ip,int p){
        dstAddress= ip;
        dstPort = p;

        }

public void cerrar(){
        try{
        socket.close();

        } catch (IOException e) {
        e.printStackTrace();
        }
        }

public void conectar() {
        try{
        socket = new Socket(dstAddress, dstPort);
        socket.setReceiveBufferSize(13000);
        socket.setSendBufferSize(13000);
        int buffer=socket.getReceiveBufferSize();
        } catch (IOException e) {
        e.printStackTrace();
        }finally{
        System.out.print("finally");
        }
        return;
        }

    @Override
    protected Void doInBackground(Void... arg0) {
            return null;
            }


public void enviar (String dstEnviar){
        try {
        PrintWriter outWriter = new PrintWriter(socket.getOutputStream(), true);
        System.out.println("Wait");

        outWriter.println(dstEnviar);
        cadena="";


                byte[] buffer = new byte[130000];
                int bytesRead;

                ByteArrayOutputStream recibido = new ByteArrayOutputStream(130000);
                Thread.sleep(1000);
                InputStream inputStream = socket.getInputStream();

                    try {
                        bytesRead = inputStream.read(buffer,0,5000);
                        recibido.write(buffer, 0, bytesRead);
                        cadena=cadena+recibido.toString("UTF-8");
                    }catch (IOException e){
                        e.printStackTrace();
                    }



                System.out.println(cadena);


        } catch (IOException e) {
        e.printStackTrace();
        System.out.println("No puedo enviar datos " + e.toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
}


}
