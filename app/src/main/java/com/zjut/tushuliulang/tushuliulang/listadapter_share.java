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
 * Created by Ben on 2015/7/3.
 */
public class listadapter_share extends BaseAdapter
{   private List<Map<String,Object>> data;
    private LayoutInflater layoutinflater ;
    private Context context;
    private View convert;
    public class module
    {
        public ImageView image;
        public TextView title;
        public TextView text;
    }
    public listadapter_share(Context context, List<Map<String, Object>> data)
    {
        this.context = context;
        this.data = data;
        this.layoutinflater = LayoutInflater.from(context);

    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String type = (String) data.get(position).get("type");
        if (type.equals("0")) {
            module m = null;
            if (convertView == null) {
                m = new module();
                convertView = layoutinflater.inflate(R.layout.listviewadapter, null);
                m.image = (ImageView) convertView.findViewById(R.id.iii);
                m.title = (TextView) convertView.findViewById(R.id.title);
                m.text = (TextView) convertView.findViewById(R.id.text);


                convertView.setTag(m);

            } else {
                m = (module) convertView.getTag();
            }
            m.title.setText((String) data.get(position).get("a"));
            m.text.setText((String) data.get(position).get("b"));

        }
        else if(type.equals("1"))
        {
            module m = null;
            if (convertView == null)
            {
                m = new module();
                convertView = layoutinflater.inflate(R.layout.no_more_view, null);
                convertView.setTag(m);

            } else {
                m = (module) convertView.getTag();
//                convertView = layoutinflater.inflate(R.layout.no_more_view, null);
            }

        }
        else
        {
            convertView = layoutinflater.inflate(R.layout.more_view, null);
        }

        return convertView;
    }

}
