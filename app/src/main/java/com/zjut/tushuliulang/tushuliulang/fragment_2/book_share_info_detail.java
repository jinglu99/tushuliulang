package com.zjut.tushuliulang.tushuliulang.fragment_2;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.style.TtsSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.zjut.tushuliulang.tushuliulang.R;
import com.zjut.tushuliulang.tushuliulang.net.TSLLURL;
import com.zjut.tushuliulang.tushuliulang.net.bookshare.*;

/**
 * A simple {@link Fragment} subclass.
 */
public class book_share_info_detail extends Fragment {

    private View view;

    private ImageView bookpic;
    private TextView bookname_tv;
    private TextView editor_tv;
    private TextView press_tv;
    private TextView date_tv;
    private TextView intro_tv;


    public book_share_info_detail() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_book_share_info, container, false);

        initwidget(view);

        new getshareinfo().execute();

        return view;
    }

    private void initwidget(View v) {
        bookpic = (ImageView) v.findViewById(R.id.book_share_info_iv);
        bookname_tv = (TextView) v.findViewById(R.id.book_share_info_bookname);
        editor_tv = (TextView) v.findViewById(R.id.book_share_info_editor);
        press_tv = (TextView) v.findViewById(R.id.book_share_info_press);
        date_tv = (TextView) v.findViewById(R.id.book_share_info_date);
        intro_tv = (TextView) v.findViewById(R.id.book_share_info_intro);
    }

    class getshareinfo extends AsyncTask<String,String,String>
    {
        private getBookShareInfo getBookShareInfo;
        private Intent intent;
        private String shareid = "";
        private String result = "";
        private BOOK_SHARE share;
        private DisplayImageOptions options;

        @Override
        protected String doInBackground(String... params) {
            intent = getActivity().getIntent();
            shareid = intent.getStringExtra("order");

            getBookShareInfo = new getBookShareInfo(shareid);
            result = getBookShareInfo.fetch();


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (result.equals("true"))
            {
                share = getBookShareInfo.getShareinfo();

                bookname_tv.setText(share.book_name);
                date_tv.setText(share.date);
                intro_tv.setText(share.intro);

                String url = TSLLURL.bookshareimg + shareid + ".jpg";

                initOptions();
                ImageLoader.getInstance().displayImage(url, bookpic, options);


            }
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
