package com.zjut.tushuliulang.tushuliulang.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjut.tushuliulang.tushuliulang.R;
import com.zjut.tushuliulang.tushuliulang.net.BOOK_INFO;


//书籍搜索界面显示结果的view
import java.util.zip.Inflater;

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
    public book_view(Context context) {

       this(context,null);

    }
    public book_view(Context context, AttributeSet attrs) {
       this(context,attrs,0);
    }
    public book_view(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        linearLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.listviewadapter,this,true);

        imageView = (ImageView) findViewById(R.id.iii);
        title = (TextView) findViewById(R.id.title);
        content = (TextView) findViewById(R.id.text);
    }
    public void setContent(BOOK_INFO book_info)
    {
        if(book_info.simpleinfo.bitmap!=null)
        {
            imageView.setImageBitmap(book_info.simpleinfo.bitmap);
        }
        title.setText(book_info.simpleinfo.name);
        content.setText(book_info.simpleinfo.intro);
    }



}

