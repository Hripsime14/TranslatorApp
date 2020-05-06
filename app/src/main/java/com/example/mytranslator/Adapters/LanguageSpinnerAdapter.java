package com.example.mytranslator.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import java.util.List;

public class LanguageSpinnerAdapter extends ArrayAdapter<String> {
    private List<String> items;
    private Context context;
    private int resource;
    private int textViewResourceId;

    public LanguageSpinnerAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<String> objects) {
        super(context, resource, textViewResourceId, objects);
        items = objects;
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
    }


    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource, parent, false);
        }
        TextView textView = convertView.findViewById(textViewResourceId);
        Log.d("MyTranslatorLog", "getDropDownView: item " + position + " = " + items.get(position));
        textView.setText(items.get(position));
        return convertView;
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getCount() {
        return items.size();
    }
}
