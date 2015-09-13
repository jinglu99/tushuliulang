package com.zjut.tushuliulang.tushuliulang;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

/**
 * Created by Ben on 2015/7/27.
 */
public class listadapter_xinde extends BaseAdapter {

    private LayoutInflater inflater;
    private Context context;
    private View view;
    private List<Map<String, Object>> list;


    public class module {
        ImageView image;
        TextView title;
        TextView text;
    }

    public listadapter_xinde(Context context, List<Map<String, Object>> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }



    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        module m = null;

        if (convertView == null) {
            m=new module();
            convertView = inflater.inflate(R.layout.listadapter_xinde, null);
            m.image = (ImageView) convertView.findViewById(R.id.listadapter_xinde_image);
            m.text = (TextView) convertView.findViewById(R.id.listadapter_xinde_text);
            m.title = (TextView) convertView.findViewById(R.id.listadapter_xinde_title);
            convertView.setTag(m);
        }
        return convertView;
    }

}
