package com.example.android.miwok;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;


public class MiwokAdapter extends ArrayAdapter<ItemMiwok> {
    int temaColor;
    public MiwokAdapter(@NonNull Context context, @NonNull List<ItemMiwok> objects, int temaColor) {
        super(context, 0, objects);
        this.temaColor = temaColor;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_miwok_layout,parent,false);
        }
        LinearLayout lytTexto = convertView.findViewById(R.id.lytTexto);
        lytTexto.setBackgroundResource(temaColor);
        ImageView imagen =convertView.findViewById(R.id.imagen);
        TextView txtPalabra = convertView.findViewById(R.id.txtPalabra);
        TextView txtDescripcion = convertView.findViewById(R.id.txtDescripcion);

        if (getItem(position).getImagenRecurso() == 0) imagen.setVisibility(View.GONE);
        else imagen.setImageResource(getItem(position).getImagenRecurso());

        txtPalabra.setText(getItem(position).getPalabra());
        txtDescripcion.setText(getItem(position).getDescripcion());
        return convertView;
    }


}
