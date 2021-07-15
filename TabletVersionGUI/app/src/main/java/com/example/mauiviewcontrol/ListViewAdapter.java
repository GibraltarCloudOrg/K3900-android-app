package com.example.mauiviewcontrol;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListViewAdapter  extends ArrayAdapter<String> {
    public ListViewAdapter(Context context, int id, ArrayList<String> items){
        super(context, id, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View v=super.getView(position, convertView, parent);
        ((TextView)v).setTextColor(getColor((position%5)));
        return v;
    }

    private int getColor(int position){
        if(position==0)
            return (Color.GREEN);
        if(position==1)
            return (Color.RED);
        if(position==2)
            return (Color.BLUE);
        if(position==3)
            return (Color.CYAN);
            //return (Color.YELLOW);
        if(position==4)
            return (Color.MAGENTA);
        return Color.BLACK;
    }
}
