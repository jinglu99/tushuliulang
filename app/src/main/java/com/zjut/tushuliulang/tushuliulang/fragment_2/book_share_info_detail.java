package com.zjut.tushuliulang.tushuliulang.fragment_2;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.zjut.tushuliulang.tushuliulang.R;
import com.zjut.tushuliulang.tushuliulang.backoperate.GetInfoFromFile;
import com.zjut.tushuliulang.tushuliulang.net.GetStuInfo;
import com.zjut.tushuliulang.tushuliulang.net.STU_INFO;
import com.zjut.tushuliulang.tushuliulang.net.TSLLURL;
import com.zjut.tushuliulang.tushuliulang.bookshare.*;
import com.zjut.tushuliulang.tushuliulang.collection.net.*;
import com.zjut.tushuliulang.tushuliulang.library.net.*;

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
    private Button borrow_bt;
    private Button collect_bt;

    private BOOK_SHARE share;
    private STU_INFO owner;
    private boolean iscollected;
    private String shareid;

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
        borrow_bt = (Button) v.findViewById(R.id.book_share_borrow);
        collect_bt = (Button) v.findViewById(R.id.book_share_collect);

        borrow_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater factory = LayoutInflater.from(getActivity());
                final View ownerinfo = factory.inflate(R.layout.alterdialog_ownerinfo, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("联系信息");
                builder.setView(ownerinfo);

                TextView name = (TextView) ownerinfo.findViewById(R.id.owner_owner);
                TextView phone = (TextView) ownerinfo.findViewById(R.id.owner_phone);
                TextView qq = (TextView) ownerinfo.findViewById(R.id.owner_qq);
                TextView email = (TextView) ownerinfo.findViewById(R.id.owner_email);

                name.setText(owner.UserName);
                if (!share.phone.equals("0"))
                  phone.setText(share.phone);
                else
                    phone.setText("无");
                if (!share.qq.equals("0"))
                    qq.setText(share.qq);
                else
                    qq.setText("无");
                if (owner.Email!="")
                    email.setText(owner.Email);
                else
                    email.setText("无");

                builder.setPositiveButton("确认发送", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        new borrow().execute();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });

                builder.create().show();

//                new borrow().execute();
            }
        });
        collect_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!iscollected) {
                    new collecttion().execute();
                }
                else
                {
                    new deletecollection().execute();

                }

            }
        });
    }

    class getshareinfo extends AsyncTask<String,String,String>
    {
        private getBookShareInfo getBookShareInfo;
        private getCollection getcollection;
        private Intent intent;
        private String result = "";
        private GetStuInfo getStuInfo;
        private COLLECTION_INFO[] collection_infos;

        private DisplayImageOptions options;

        @Override
        protected String doInBackground(String... params) {
            intent = getActivity().getIntent();
            shareid = intent.getStringExtra("order");

            getBookShareInfo = new getBookShareInfo(shareid);
            result = getBookShareInfo.fetch();
            share = getBookShareInfo.getShareinfo();

            getStuInfo = new GetStuInfo(share.owner);
            getStuInfo.fetch();
            owner = getStuInfo.getStu_info();

            getcollection = new getCollection(GetInfoFromFile.getinfo().Id);
            getcollection.fetch();
            collection_infos = getcollection.getCollections();



            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (result.equals("true"))
            {


                bookname_tv.setText(share.book_name);
                date_tv.setText(share.date);
                intro_tv.setText(share.intro);

                for (int n =0 ; n<collection_infos.length ; n++)
                {
                    if (collection_infos[n].k.equals("1")&&collection_infos[n].code.equals(shareid))
                    {
                        collect_bt.setText("取消收藏");
                        iscollected = true;
                    }
                }

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
    class collecttion extends AsyncTask<String,String,String>
    {
        private Intent intent;
        private collect c;

        private boolean result;
        @Override
        protected String doInBackground(String... params) {


            c = new collect("1",shareid, GetInfoFromFile.getinfo().Id);
            if (c.upload())
            {
                result = true;
            }
            else
            {
                result = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (result==true)
            {
                collect_bt.setText("取消收藏");
                iscollected = true;
            }
            super.onPostExecute(s);
        }
    }
    class borrow extends AsyncTask<String,String,String>
    {
        private sendLendMessage lend;
        private boolean result = false;

        private Intent intent;

        @Override
        protected String doInBackground(String... params) {

            lend = new sendLendMessage(GetInfoFromFile.getinfo().Id,shareid);
            if (lend.sendMessage())
            {
                result = true;
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (result==true)
            {
                borrow_bt.setEnabled(false);

            }
            super.onPostExecute(s);
        }
    }
    class deletecollection extends AsyncTask<String,String,String>
    {
        private deleteCollection delete;
        private boolean result = false;
        @Override
        protected String doInBackground(String... params) {
            delete = new deleteCollection("1",shareid,GetInfoFromFile.getinfo().Id);
            if (delete.delete())
            {
                result = true;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (result)
            {
                collect_bt.setText("收藏");
                iscollected = false;
            }
            super.onPostExecute(s);
        }
    }

}
