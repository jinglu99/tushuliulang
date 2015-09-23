package com.zjut.tushuliulang.tushuliulang;

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

    private DisplayImageOptions options;
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

            String url = TSLLURL.bookshareimg + data.get(position).get("i") + ".jpg";
            initOptions();
            ImageLoader.getInstance().displayImage(url, m.image, options);
            m.title.setText((String) data.get(position).get("a"));
            m.text.setText((String) data.get(position).get("b"));
//            m.image.setImageBitmap((Bitmap)data.get(position).get("i"));
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
