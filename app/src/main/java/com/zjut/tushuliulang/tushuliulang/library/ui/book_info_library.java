package com.zjut.tushuliulang.tushuliulang.library.ui;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;


import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.zjut.tushuliulang.tushuliulang.R;
import com.zjut.tushuliulang.tushuliulang.library.net.getDate;
import com.zjut.tushuliulang.tushuliulang.net.*;
import com.zjut.tushuliulang.tushuliulang.bookshare.*;

public class book_info_library extends ActionBarActivity {

    private Intent intent;
    private String type = "";
    private String code = "";
    private String codeincode = "";

    private TextView tv_name;
    private TextView tv_editor;
    private TextView tv_press;
    private TextView tv_date;
    private TextView tv_intro;
    private TextView tv_d1;
    private TextView tv_d2;
    private ImageView imageView;

    private android.support.v7.app.ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info_library);

        init();

        new getinfo().execute();
    }

    private void init() {
        intent = getIntent();
        type = intent.getStringExtra("type");
        code = intent.getStringExtra("code");
        codeincode = intent.getStringExtra("codeincode");

        tv_d1 = (TextView) findViewById(R.id.d1);
        tv_d2 = (TextView) findViewById(R.id.d2);
        tv_name = (TextView) findViewById(R.id.library_info_bookname);
        tv_editor = (TextView) findViewById(R.id.library_info_editor);
        tv_date = (TextView) findViewById(R.id.library_info_date);
        tv_intro = (TextView) findViewById(R.id.library_info_intro);
        tv_press = (TextView) findViewById(R.id.library_info_press);
        imageView = (ImageView) findViewById(R.id.library_info_iv);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_book_info_library, menu);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if(id == android.R.id.home)
        {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
    class getinfo extends AsyncTask<String,String,String>
    {

        private getBookShareInfo bookShareInfo;
        private getBookInfo bookInfo;
        private BOOK_INFO book_info;
        private BOOK_SHARE book_share;
        private boolean result = false;
        private String c = code;

        private getDate date;

        private DisplayImageOptions options;
        @Override
        protected String doInBackground(String... params) {

            date = new getDate(code,codeincode);
            if (type.equals("share"))
            {


                bookShareInfo = new getBookShareInfo(code);
                if (bookShareInfo.fetch().equals("true"))
                {
                    book_share = bookShareInfo.getShareinfo();
                    code = book_share.code;

                    date = new getDate(book_share.code,book_share.codeincode);
                    result = true;
                }
            }

                bookInfo = new getBookInfo(code);
                if (bookInfo.fetch())
                {
                    book_info = bookInfo.getShareinfo();
                    result = true;
                }

            if(!date.fetch())
                result = false;
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (result)
            {
                initOptions();
                if (type.equals("share"))
                {
                    tv_name.setText(book_share.book_name);
                    tv_editor.setText(book_info.editor);
                    tv_press.setText(book_info.press);
                    tv_intro.setText(book_share.intro);
                    tv_date.setText(book_share.date);

                    String url = TSLLURL.bookshareimg + c + ".jpg";

                    ImageLoader.getInstance().displayImage(url, imageView, options);
                }
                else
                {
                    tv_name.setText(book_info.name);
                    tv_editor.setText(book_info.editor);
                    tv_press.setText(book_info.press);
                    tv_intro.setText(book_info.intro);
                    String url = TSLLURL.bookimage + c + ".jpg";

                    ImageLoader.getInstance().displayImage(url, imageView, options);
                }

                tv_d1.setText(date.getd1());
                tv_d2.setText(date.getd2());
            }

            super.onPostExecute(s);
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
}
