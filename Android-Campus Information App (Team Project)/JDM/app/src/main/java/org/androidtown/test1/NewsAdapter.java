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

/**
 * Created by over_ on 2018-12-14.
 */

class NewsAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<NewsInfo> arr;
    private int layout;

    public NewsAdapter(Context context, int layout, ArrayList<NewsInfo> arr) {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.arr = arr;
        this.layout = layout;
    }
    @Override
    public int getCount() {
        return arr.size();
    }

    @Override
    public NewsInfo getItem(int position) {
        return arr.get(position);
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
        Log.d("현규adapter48","1");
        NewsInfo newsInfo = arr.get(position);
        TextView headlineTextView = (TextView) convertView.findViewById(R.id.headlineTextView);
        headlineTextView.setText(newsInfo.getHeadline());
        TextView dateTextView = (TextView) convertView.findViewById(R.id.dateTextView);
        dateTextView.setText(newsInfo.getDate());
        return convertView;

    }
}