package com.example.cheli.tunelapp;

import android.os.AsyncTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;

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
        System.out.print("Conectado...");
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
        //Thread.sleep(15000);
        outWriter.println(dstEnviar);
        byte[] buffer = new byte[1024];
        int bytesRead;
        ByteArrayOutputStream recibido = new ByteArrayOutputStream(1024);
        InputStream inputStream = socket.getInputStream();

        bytesRead = inputStream.read(buffer);
        recibido.write(buffer, 0, bytesRead);
        cadena=recibido.toString("UTF-8");
        System.out.println(cadena);
        } catch (IOException e) {
        e.printStackTrace();
        System.out.println("No puedo enviar datos " + e.toString());
        }
        }
}
