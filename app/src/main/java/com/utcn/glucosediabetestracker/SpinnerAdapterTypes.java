package com.utcn.glucosediabetestracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class SpinnerAdapterTypes extends ArrayAdapter<Types> {

     LayoutInflater layoutInflater;
    public SpinnerAdapterTypes(@NonNull Context context, int resource, @NonNull List<Types> types) {
        super(context, resource, types);
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View rowView =layoutInflater.inflate(R.layout.types_spinner, null, true);
        Types types = getItem(position);
        TextView textView = (TextView)rowView.findViewById(R.id.typeTextView);
        ImageView imageView = (ImageView)rowView.findViewById(R.id.typesIcon);
        textView.setText(types.getType());
        imageView.setImageResource(types.getImage());
        return rowView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null)
            convertView =layoutInflater.inflate(R.layout.types_spinner, parent, false);
        Types types = getItem(position);
        TextView textView = (TextView)convertView.findViewById(R.id.typeTextView);
        ImageView imageView = (ImageView)convertView.findViewById(R.id.typesIcon);
        textView.setText(types.getType());
        imageView.setImageResource(types.getImage());
        return convertView;
    }
}
