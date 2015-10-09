package com.zjut.tushuliulang.tushuliulang.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.zjut.tushuliulang.tushuliulang.R;
import com.zjut.tushuliulang.tushuliulang.activities.book_info_search;
import com.zjut.tushuliulang.tushuliulang.net.BOOK_INFO;
import com.zjut.tushuliulang.tushuliulang.bookshare.BOOK_SHARE;
import com.zjut.tushuliulang.tushuliulang.net.TSLLURL;

//书籍搜索界面显示结果的view

/**
 * Created by Ben on 2015/8/18.
 */
public class book_view extends LinearLayout {
    TextView tv;
    LinearLayout linearLayout ;
    Context context;
    ImageView imageView;
    TextView title;
    TextView content;

    String number="";

    private DisplayImageOptions options;
    public book_view(Context context) {

       this(context,null);

    }
    public book_view(Context context, AttributeSet attrs) {
       this(context,attrs,0);
    }
    public book_view(final Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        linearLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.listviewadapter, this, true);

        imageView = (ImageView) findViewById(R.id.iii);
        title = (TextView) findViewById(R.id.title);
        content = (TextView) findViewById(R.id.text);



    }
    public void setContent(BOOK_INFO book_info)
    {
        if(book_info.bitmap!=null)
        {
            imageView.setImageBitmap(book_info.bitmap);
        }
        title.setText(book_info.name);
        content.setText(book_info.intro);

        String url = TSLLURL.bookimage + book_info.code+".jpg";
        initOptions();
        ImageLoader.getInstance().displayImage(url, imageView, options);

        number = book_info.code;

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,book_info_search.class);
                intent.putExtra("code",number);

                context.startActivity(intent);
            }
        });
    }

    public void setContent(BOOK_SHARE book_share)
    {
        if(book_share.bitmap!=null)
        {
            imageView.setImageBitmap(book_share.bitmap);
        }
        title.setText(book_share.book_name);
        content.setText(book_share.intro);

        number = book_share.number_order;
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

