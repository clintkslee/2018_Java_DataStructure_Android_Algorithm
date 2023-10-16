package org.androidtown.test1;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListviewAdapter extends BaseAdapter{
    private LayoutInflater inflater;
    private ArrayList<Listviewitem> data;
    private int layout;
    public ListviewAdapter(Context context, int layout, ArrayList<Listviewitem> data){
        this.inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data=data;
        this.layout=layout;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Listviewitem getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(layout, parent, false);
        }
        Listviewitem listviewitem = data.get(position);
        if(listviewitem==null)
             Log.d("error46", "46");
        ImageView icon = (ImageView) convertView.findViewById(R.id.imageview);
        icon.setImageResource(listviewitem.getIcon());
        TextView name = (TextView) convertView.findViewById(R.id.textview);
        name.setText(listviewitem.getName());
        Log.d("error41", "1");
        return convertView;
    }
    public void upDateItemList(ArrayList<Listviewitem> newList)
    {

        this.data = newList;
        Log.d("update?",String.valueOf(newList.size()));
        notifyDataSetChanged();
    }

}
