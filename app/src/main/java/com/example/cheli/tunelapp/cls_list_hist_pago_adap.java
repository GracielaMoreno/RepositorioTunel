package com.example.cheli.tunelapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class cls_list_hist_pago_adap extends BaseAdapter{
    private Context mContext;
    private List<cls_list_hist_pago> mListpago;

    public cls_list_hist_pago_adap(Context mContext, List<cls_list_hist_pago> mListpago) {
        this.mContext = mContext;
        this.mListpago = mListpago;
    }

    @Override
    public int getCount() {
        return mListpago.size();
    }

    @Override
    public Object getItem(int posicion) {
        return mListpago.get(posicion);
    }

    @Override
    public long getItemId(int posicion) {
        return posicion;
    }

    @Override
    public View getView(int position, View contentView, ViewGroup parent) {
        View itemView = View.inflate(mContext, R.layout.activity_temp_list_hist_pago, null);

        TextView Tag = (TextView) itemView.findViewById(R.id.txtCampo2);
        TextView Placa = (TextView) itemView.findViewById(R.id.txtCampo9);
        TextView No_pasadas = (TextView) itemView.findViewById(R.id.txtCampo3);
        TextView Fecha = (TextView) itemView.findViewById(R.id.txtCampo4);
        TextView Medio_pag= (TextView) itemView.findViewById(R.id.txtCampo5);
       TextView Estado_rec= (TextView) itemView.findViewById(R.id.txtCampo10);
        TextView NoFactura= (TextView) itemView.findViewById(R.id.txtCampo11);


        itemView.setTag(mListpago.get(position).getCampo1());

        Placa.setText(mListpago.get(position).getCampo2());
        Tag.setText(mListpago.get(position).getCampo3());
        No_pasadas.setText(mListpago.get(position).getCampo4());
        Fecha.setText(mListpago.get(position).getCampo5());
        Medio_pag.setText(mListpago.get(position).getCampo6());
        Estado_rec.setText(mListpago.get(position).getCampo7());
        NoFactura.setText(mListpago.get(position).getCampo8());
        return itemView;
    }
}
