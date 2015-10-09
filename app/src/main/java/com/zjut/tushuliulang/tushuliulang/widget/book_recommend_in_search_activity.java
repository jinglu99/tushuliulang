package com.zjut.tushuliulang.tushuliulang.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.zjut.tushuliulang.tushuliulang.R;

/**
 * Created by Ben on 2015/8/21.
 *
 * 书籍推荐控件   setcontent()设置显示内容
 */
public class book_recommend_in_search_activity extends LinearLayout {

    private ImageView image1;
    private ImageView image2;
    private TextView name1;
    private TextView name2;

    private DisplayImageOptions options;


    public book_recommend_in_search_activity(Context context) {
        this(context, null);
    }



    public book_recommend_in_search_activity(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public book_recommend_in_search_activity(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.book_recommend_in_search_activity, this, true);


        image1 = (ImageView) findViewById(R.id.image1);
        image2 = (ImageView) findViewById(R.id.image2);
        name1 = (TextView) findViewById(R.id.book_name1);
        name2 = (TextView) findViewById(R.id.book_name2);

    }


    public void setcontent(String name1,String bitmap1,String name2,String bitmap2)
    {

        this.name1.setText(name1);
        initOptions();
        ImageLoader.getInstance().displayImage(bitmap1, image1, options);


        this.name2.setText(name2);
        ImageLoader.getInstance().displayImage(bitmap2, image2, options);
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
