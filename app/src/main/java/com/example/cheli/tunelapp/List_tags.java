package com.example.cheli.tunelapp;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.TransactionTooLargeException;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class List_tags extends Activity {

    private ListView listTags;
    private String cliente;
    private ListView lsvw_tags;

    private List<cls_list_tags> placa;

    private String nombreUsuario;
    private String codigoUsuario = "";
    private String vehiculos = "";
    private String[] vectorVehiculos;
    private String[] vectorVehiculo;
    private cls_list_tags_adap ListTagsAdapter;
    private String Bancos;
    private String Trajetas;
    private String Pasadas;
    private String cut_codigo="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_tags);
        Llenar();
        ImageView img = (ImageView) findViewById(R.id.img_regresar);
        img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent( List_tags.this, Login.class );
                //intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                finish();
            }
        });

        listTags=(ListView)findViewById(R.id.lsvw_tags);

        placa = new ArrayList<>();
        placa.clear();

        //variables pasadas
        codigoUsuario=getIntent().getStringExtra("CodigoUsuario");
        nombreUsuario=getIntent().getStringExtra("NombreUsuario");
        vehiculos=getIntent().getStringExtra("Vehiculos");

        TextView cliente = (TextView) findViewById(R.id.txtCliente);
        cliente.setText(nombreUsuario);

       vectorVehiculos=vehiculos.split("\\*");
        if (vectorVehiculos.length > 0)
        {
            for (Integer i=0;i<(vectorVehiculos.length);i++)
            {
                vectorVehiculo=vectorVehiculos[i].split("\\-");
                if (vectorVehiculo.length > 0) {
                  placa.add(new cls_list_tags( vectorVehiculo[0], "   " + vectorVehiculo[1],"TAG:" +  vectorVehiculo[2],"VEHICULO:" + vectorVehiculo[3], "SALDO:" + vectorVehiculo[4]));
                    Log.e("vec","cut_codigo"+vectorVehiculo[0] +"placa"+ vectorVehiculo[1]+"TAG:" +  vectorVehiculo[2]);
                }

            }
            ListTagsAdapter = new cls_list_tags_adap(List_tags.this,placa);
            listTags.setAdapter(ListTagsAdapter);
        }

        registerForContextMenu(listTags);
        listTags.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               //vehiculos.getItemAtPosition(position).
                Toast.makeText(List_tags.this, "Mantenga presionado el item para ver opciones", Toast.LENGTH_LONG).show();

            }
        });
listTags.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        String listChoice =  ListTagsAdapter.getItem(position).toString();
        cls_list_tags p = (cls_list_tags) listTags.getItemAtPosition(position);
        cut_codigo= p.getCampo1();
       // Log.i("Click", "click en el elemento " +);
        return false;
    }
});
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Intent intent = new Intent( List_tags.this, Login.class );
            //intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
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
                Bundle bundle = new Bundle();
                bundle.putString("cl_codigo" ,codigoUsuario);
                bundle.putString("cut_codigo" ,cut_codigo);
                Intent i = new Intent(List_tags.this, Rec_Tag.class );
                i.putExtras(bundle);
                startActivity(i);
                return true;
                //editNote(info.id);
            case R.id.opcion_2:
                //
                Bundle bundle2 = new Bundle();
                bundle2.putString("cl_codigo" ,codigoUsuario);
                bundle2.putString("cut_codigo" ,cut_codigo);
                bundle2.putString("Pasadas" ,Pasadas);
                Intent inten = new Intent(List_tags.this, Actu_plac.class );
                inten.putExtras(bundle2);
                startActivity(inten);
                return true;
            case R.id.opcion_3:
                Bundle bundle1 = new Bundle();
                bundle1.putString("cl_codigo" ,codigoUsuario);
                bundle1.putString("cut_codigo" ,cut_codigo);
                bundle1.putString("nombre" ,nombreUsuario);
                bundle1.putString("Bancos" ,Bancos);
                bundle1.putString("Tarjetas" ,Trajetas);
                bundle1.putString("Pasadas" ,Pasadas);
                Intent i2 = new Intent(List_tags.this,Actu_traj.class );
                i2.putExtras(bundle1);
                startActivity(i2);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }


public void Llenar(){
    String envia= "2";
    //ob.conectar();
    //ob.enviar(envia);
    //ob.cerrar();
    //TramaRecida = ob.cadena.toString();
   String TramaRecida = "Pichincha-Produbanco-Pacifico-Guayaquil-Austro-Bolivariano-Internancional-General Rumiñahui|Visa-Mastercard-Diners-American Expres-Discover|100 Pasadas por $28,50-200 Pasadas por $57";

    String[] vectorTramaRecibida = TramaRecida.split("\\|");
    Bancos=vectorTramaRecibida[0];
    Trajetas=vectorTramaRecibida[1];
    Pasadas=vectorTramaRecibida[2];

}

}
