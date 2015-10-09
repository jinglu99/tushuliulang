package com.zjut.tushuliulang.tushuliulang.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.zjut.tushuliulang.tushuliulang.R;
import com.zjut.tushuliulang.tushuliulang.activities.Book_share_info;
import com.zjut.tushuliulang.tushuliulang.net.TSLLURL;

/**
 * Created by Ben on 2015/10/8.
 */
public class book_in_available extends LinearLayout {

    private TextView name;
    private CircleImageView circleImageView;
    private Context context;

    private DisplayImageOptions options;

    public book_in_available(Context context) {
        this(context, null);
    }
    public book_in_available(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }



    public book_in_available(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.context = context;

        LayoutInflater.from(context).inflate(R.layout.book_in_available, this, true);

        circleImageView = (CircleImageView) findViewById(R.id.book_in_available_image);
        name = (TextView) findViewById(R.id.book_in_available_name);
    }

    public void setcontent(String stuid, final String shareid)
    {

        String url = TSLLURL.picurl + stuid+".jpg";
        this.name.setText(stuid);
        initOptions();
        ImageLoader.getInstance().displayImage(url, circleImageView, options);

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Book_share_info.class);
                intent.putExtra("order",shareid);

                context.startActivity(intent);
            }
        });
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
