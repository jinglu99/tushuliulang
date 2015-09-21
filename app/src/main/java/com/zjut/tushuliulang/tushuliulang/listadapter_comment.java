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
public class listadapter_comment extends BaseAdapter {

    private LayoutInflater inflater;
    private Context context;
    private View view;
    private List<Map<String, Object>> list;


    public class module {
        ImageView image;
        TextView title;
        TextView text;
        TextView date;
    }

    public listadapter_comment(Context context, List<Map<String, Object>> list) {
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
            convertView = inflater.inflate(R.layout.listadapter_comment, null);
            m.image = (ImageView) convertView.findViewById(R.id.listadapter_comment_image);
            m.text = (TextView) convertView.findViewById(R.id.listadapter_comment_comment);
            m.title = (TextView) convertView.findViewById(R.id.listadapter_comment_title);
            m.date = (TextView) convertView.findViewById(R.id.listadapter_comment_date);
            convertView.setTag(m);
        }
        else {
            m = (module) convertView.getTag();
        }
        m.title.setText((String) list.get(position).get("stuid"));
        m.text.setText((String) list.get(position).get("comment"));
        m.date.setText((String) list.get(position).get("date"));
//        m.image.setImageBitmap((Bitmap)list.get(position).get("i"));
        return convertView;
    }

}
