package com.example.cheli.tunelapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class cls_list_hist_soli_pend_adap extends BaseAdapter {
    private Context mContext;
    private List<cls_list_hist_soli_pend> mListpago;

    public cls_list_hist_soli_pend_adap(Context mContext, List<cls_list_hist_soli_pend> mListpago) {
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
        View itemView = View.inflate(mContext, R.layout.activity_temp_list_hist_soli_pend, null);

        TextView Tag = (TextView) itemView.findViewById(R.id.txtCampo2);
        TextView Placa = (TextView) itemView.findViewById(R.id.txtCampo3);
        TextView Concepto = (TextView) itemView.findViewById(R.id.txtCampo4);


        itemView.setTag(mListpago.get(position).getCampo1());

        Placa.setText(mListpago.get(position).getCampo2());
        Tag.setText(mListpago.get(position).getCampo3());
        Concepto.setText(mListpago.get(position).getCampo4());

        return itemView;
    }
}
