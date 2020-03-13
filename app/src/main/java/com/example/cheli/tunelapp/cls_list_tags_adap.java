package com.example.cheli.tunelapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Procelec on 5/7/2019.
 */

public class cls_list_tags_adap extends BaseAdapter {

    private Context mContext;
    private List<cls_list_tags> mListtags;

    public cls_list_tags_adap(Context mContext, List<cls_list_tags> mListTags) {
        this.mContext = mContext;
        this.mListtags = mListTags;
    }

    @Override
    public int getCount() {
        return mListtags.size();
    }

    @Override
    public Object getItem(int posicion) {
        return mListtags.get(posicion);
    }

    @Override
    public long getItemId(int posicion) {
        return posicion;
    }

    @Override
    public View getView(int position, View contentView, ViewGroup parent) {
        View itemView = View.inflate(mContext, R.layout.activity_temp_list, null);

        TextView Placa = (TextView) itemView.findViewById(R.id.txtCampo2);
        TextView Tag = (TextView) itemView.findViewById(R.id.txtCampo3);
        TextView Modelo = (TextView) itemView.findViewById(R.id.txtCampo4);
        TextView Saldo = (TextView) itemView.findViewById(R.id.txtCampo5);


        itemView.setTag(mListtags.get(position).getCampo1());

        Placa.setText(mListtags.get(position).getCampo2());
        Tag.setText(mListtags.get(position).getCampo3());
        Modelo.setText(mListtags.get(position).getCampo4());
        Saldo.setText(mListtags.get(position).getCampo5());

        return itemView;
    }
}