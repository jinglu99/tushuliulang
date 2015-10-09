package com.zjut.tushuliulang.tushuliulang.activities;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.zjut.tushuliulang.tushuliulang.R;
import com.zjut.tushuliulang.tushuliulang.bookshare.BOOK_SHARE;
import com.zjut.tushuliulang.tushuliulang.net.BOOK_INFO;
import com.zjut.tushuliulang.tushuliulang.net.STU_INFO;
import com.zjut.tushuliulang.tushuliulang.net.*;
import com.zjut.tushuliulang.tushuliulang.widget.*;

public class book_info_search extends ActionBarActivity {

    private Context context = this;
    private String code = "";

    private ImageView bookpic;
    private TextView bookname_tv;
    private TextView editor_tv;
    private TextView press_tv;
    private TextView intro_tv;
    private LinearLayout layout;
    private android.support.v7.app.ActionBar actionBar;



    private BOOK_SHARE share;
    private STU_INFO owner;
    private boolean iscollected;
    private String shareid;
    private BOOK_INFO book_info;
    private BOOK_SHARE[] book_shares;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info_search);

        initwidget();

        Intent intent = getIntent();
        code = intent.getStringExtra("code");

        new getinfo().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_book_info_search, menu);
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
        if (id == android.R.id.home)
        {
            finish();
        }
        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    private void initwidget() {
        bookpic = (ImageView) findViewById(R.id.book_info_search_iv);
        bookname_tv = (TextView) findViewById(R.id.book_info_search_bookname);
        editor_tv = (TextView)findViewById(R.id.book_info_search_editor);
        press_tv = (TextView) findViewById(R.id.book_info_search_press);
        intro_tv = (TextView) findViewById(R.id.book_info_search_intro);
        layout = (LinearLayout) findViewById(R.id.book_info_search_layout);
    }

    class getinfo extends AsyncTask<String,String,String>
    {
        private getBookInfo getBookInfo;
        private getBookAvailable getBookAvailable;
        private boolean result = false;

        private DisplayImageOptions options;
        @Override
        protected String doInBackground(String... params) {
            getBookInfo = new getBookInfo(code);
            if (getBookInfo.fetch())
            {
                book_info = getBookInfo.getShareinfo();
                result = true;
            }

            getBookAvailable = new getBookAvailable(code);
            if (getBookAvailable.fetch())
            {
                book_shares = getBookAvailable.getBook_shares();

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (result)
            {

                bookname_tv.setText(book_info.name);
                editor_tv.setText(book_info.editor);
                press_tv.setText(book_info.press);
                intro_tv.setText(book_info.intro);

                String url = TSLLURL.bookimage + code + ".jpg";
                initOptions();
                ImageLoader.getInstance().displayImage(url, bookpic, options);


                if (book_shares!=null)
                {
                    for (int n = 0 ; n< book_shares.length ; n++)
                    {
                        book_in_available view = new book_in_available(context);
                        view.setcontent(book_shares[n].owner,book_shares[n].number_order
                        );
                        layout.addView(view);
                    }
                }
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
