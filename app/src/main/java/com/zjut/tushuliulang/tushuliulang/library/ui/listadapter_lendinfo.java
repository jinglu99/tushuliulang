package com.zjut.tushuliulang.tushuliulang.library.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.zjut.tushuliulang.tushuliulang.net.TSLLURL;
import com.zjut.tushuliulang.tushuliulang.R;

import java.util.List;
import java.util.Map;

/**
 * Created by Ben on 2015/7/27.
 */
public class listadapter_lendinfo extends BaseAdapter {

    private LayoutInflater inflater;
    private Context context;
    private View view;
    private List<Map<String, Object>> list;
    private DisplayImageOptions options;


    public class module {
        ImageView image;
        TextView name;
        TextView date;

    }

    public listadapter_lendinfo(Context context, List<Map<String, Object>> list) {
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
            convertView = inflater.inflate(R.layout.listadapter_lendinfo, null);
            m.image = (ImageView) convertView.findViewById(R.id.listadapter_lendinfo_image);
            m.name = (TextView) convertView.findViewById(R.id.listadapter_lendinfo_name);
            m.date = (TextView) convertView.findViewById(R.id.listadapter_lendinfo_date);

            convertView.setTag(m);
        }
        else {
            m = (module) convertView.getTag();
        }
        String url = TSLLURL.picurl + list.get(position).get("stuid") +".jpg";

        initOptions();
        ImageLoader.getInstance().displayImage(url, m.image, options);

        m.name.setText((String) list.get(position).get("username"));
        m.date.setText((String) list.get(position).get("date"));



        return convertView;
    }

    private void initOptions() {
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_menu_gallery) //设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.drawable.ic_menu_gallery)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.ic_menu_gallery)  //设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型
                .resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
                .displayer(new RoundedBitmapDisplayer(20))//是否设置为圆角，弧度为多少
                .displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间
                .build();//构建完成
    }
}

