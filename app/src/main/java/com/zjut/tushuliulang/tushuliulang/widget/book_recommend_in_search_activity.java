package com.zjut.tushuliulang.tushuliulang.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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


    public void setcontent(String name1,Bitmap bitmap1,String name2,Bitmap bitmap2)
    {
        this.name1.setText(name1);
        image1.setImageBitmap(bitmap1);

        this.name2.setText(name2);
        image2.setImageBitmap(bitmap2);
    }
}
