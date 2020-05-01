package com.tppa.morsecode;

import android.content.Context;
import android.graphics.ColorSpace;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class AlphabetAdapter extends BaseAdapter {


    private final Context context;
    private final ArrayMap<String, String> alphabet ;

    public AlphabetAdapter(Context context, ArrayMap<String, String> alphabet){
        this.context = context;
        this.alphabet = alphabet;
    }

    @Override
    public int getCount(){
        return alphabet.size();
    }

    @Override
    public long getItemId(int position){
        return 0;
    }
    @Override
    public Object getItem(int position){
        return null;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        final String character = alphabet.keyAt(position);
        final String morseCode = alphabet.get(character);

        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.alphabet__grid__item, null);
        }

        final TextView characterTextView = convertView.findViewById(R.id.character);
        final TextView morseCodeForCharacter = convertView.findViewById(R.id.morse_code_for_character);

        morseCodeForCharacter.setText(morseCode);
        characterTextView.setText(character);

        return convertView;
    }



}
