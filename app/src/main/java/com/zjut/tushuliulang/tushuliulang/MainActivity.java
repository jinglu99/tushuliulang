package com.zjut.tushuliulang.tushuliulang;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjut.tushuliulang.tushuliulang.activities.add_book_share;
import com.zjut.tushuliulang.tushuliulang.activities.lendInfo;
import com.zjut.tushuliulang.tushuliulang.activities.search_activity;
import com.zjut.tushuliulang.tushuliulang.question.ask.AskAcitivity;
import com.zjut.tushuliulang.tushuliulang.backoperate.GetInfoFromFile;
import com.zjut.tushuliulang.tushuliulang.backoperate.createDirectory;
import com.zjut.tushuliulang.tushuliulang.fragment.library_f;
import com.zjut.tushuliulang.tushuliulang.fragment.mycollection_f;
import com.zjut.tushuliulang.tushuliulang.fragment.share_f;
import com.zjut.tushuliulang.tushuliulang.net.*;

import com.zjut.tushuliulang.tushuliulang.bookshare.BOOK_SHARE;
import com.zjut.tushuliulang.tushuliulang.bookshare.getbookshares;
import com.zjut.tushuliulang.tushuliulang.question.question.Frame_questions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private Fragment mContent;
    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private share_f share;
    private library_f library;
    private Frame_questions frame_questions;
    private mycollection_f mycollection;
    private int currentItem = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initfragment();
        setContentView(R.layout.activity_main);



        //自动登陆
        auto_login();

//        initfragment();
        //注册广播
        initbroadcast();

        //第一次打开 创建文件夹
        createDirectory file = new createDirectory();
        file.create();

        //测试专用 无意义
//        test();


        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        onSectionAttached(0);
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));


    }



    private void initfragment() {
        share = new share_f();
        frame_questions = new Frame_questions();
        library = new library_f();
        mycollection = new mycollection_f();
//        mContent;
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        switch(position) {
            case 0:
                currentItem = 0;
                switchfragment(share);
                break;
            case 1:
                currentItem = 1;
                switchfragment(frame_questions);
                break;
            case 2:
                currentItem = 2;
                switchfragment(library);
                break;
            case 3:
                currentItem = 3;
               switchfragment(mycollection);
        }
        onSectionAttached(position);
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 0:
                mTitle = getString(R.string.title_section1);
                break;
            case 1:
                mTitle = getString(R.string.title_section2);
                break;
            case 2:
                mTitle = getString(R.string.title_section3);
                break;
            case 3:
                mTitle = getString(R.string.title_section4);
                break;

        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.main_search) {
            Intent intent = new Intent(this, search_activity.class);
            startActivity(intent);
            return true;
        }
            else if (id == R.id.main_add)
        {
            switch (currentItem){
                case 0:
                    Intent intent = new Intent(this,add_book_share.class);
                    startActivity(intent);
                    return true;
                case 1:
                    Intent intent_ask = new Intent(this,AskAcitivity.class);
                    startActivityForResult(intent_ask, 1);
                    return true;
                case 2:

                    Intent intent_lendinfo = new Intent(this,lendInfo.class);
                    startActivityForResult(intent_lendinfo, 1);
                    return true;
                case 3:
                    break;
            }

        }

        return super.onOptionsItemSelected(item);
    }



    public void switchfragment(Fragment fragment)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if(mContent==null)
        {
            fragmentManager.beginTransaction().add(R.id.container,fragment).commit();
            mContent = fragment;

        }
        else
        {
            if(fragment.isAdded())
            {
                fragmentManager.beginTransaction().hide(mContent).show(fragment).commit();
                mContent = fragment;
            }
            else
            {
                fragmentManager.beginTransaction().hide(mContent).add(R.id.container,fragment).commit();
                mContent = fragment;
            }
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static Fragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);

            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }

    }

    public void initbroadcast()
    {
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(final Context context, Intent intent) {
                String s = intent.getAction();

                switch (s)
                {
                    case "logined":
                        Bitmap bitmap = null;
                       STU_INFO stu_info = GetInfoFromFile.getinfo();
                        ((TextView)findViewById(R.id.username_tv)).setText(stu_info.UserName);

                        new getImage().execute();
                        Downlaod.FileDownload(TSLLURL.picurl+ stu_info.Id + ".jpg", "/tushuliulang/data/", 2);


//                        try {
//                            FileInputStream image = new FileInputStream
//                                    (Environment.getExternalStorageDirectory().getPath()+"/tushuliulang/data/"+stu_info.Id+".jpg");
//                            bitmap = BitmapFactory.decodeStream(image);
//                        } catch (FileNotFoundException e) {
//                            e.printStackTrace();
//                        }
//
//                        if (bitmap!=null) {
//                            ((ImageView) findViewById(R.id.userimage)).setImageBitmap(bitmap);
//                        }

//                        ((LinearLayout)findViewById(R.id.changestuinfo)).setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                // 跳转到个人信息activity
//                                Intent intent = new Intent(context,changestuinfo_activity.class);
//                                startActivity(intent);
//                            }
//                        });
                        break;
                    case "quit":

                        ((TextView)findViewById(R.id.username_tv)).setText("未登陆");
                        ((ImageView) findViewById(R.id.userimage)).setImageResource(R.drawable.lzy);

                }
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("logined");
        intentFilter.addAction("quit");
        registerReceiver(broadcastReceiver,intentFilter);
    }
    private void auto_login()
    {
        File file = new File(Environment.getExternalStorageDirectory().getPath()+"/tushuliulang/data/info.db");

        if(file.exists())
        {
            Bitmap bitmap = null;
            STU_INFO stu_info = GetInfoFromFile.getinfo();
            ((TextView)findViewById(R.id.username_tv)).setText(stu_info.UserName);

            try {
                FileInputStream image = new FileInputStream
                        (Environment.getExternalStorageDirectory().getPath()+"/tushuliulang/data/"+stu_info.Id+".jpg");
                bitmap = BitmapFactory.decodeStream(image);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            if (bitmap!=null) {
                ((ImageView) findViewById(R.id.userimage)).setImageBitmap(bitmap);
            }

//            ((LinearLayout)findViewById(R.id.changestuinfo)).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    // 跳转到个人信息activity
//                    Intent intent = new Intent(MainActivity.this,changestuinfo_activity.class);
//                    startActivity(intent);
//                }
//            });
        }


    }

    //专供测试使用
    private void test() {
    new publish().execute();
    }

    class publish extends AsyncTask<String,String,String>
    {
        BOOK_SHARE[] bkshare;
        getbookshares bookshares;

        @Override
        protected String doInBackground(String... params) {
//            bkshare = new BOOK_SHARE();
//            bkshare.owner = "201419630314";
//            bkshare.book_name="第一日";
//            bkshare.isbn = "978-7-5404-6924-5";
//            bkshare.phone="17816874920";
//            bkshare.qq = "741763560";

            bookshares = new getbookshares();
            if(bookshares.fetch())
            {
//                bkshare = publish.getShare();
            }
            return null;
        }
    }

    class getImage extends AsyncTask<String,String,String>
    {
        private String url = TSLLURL.picurl + GetInfoFromFile.getinfo().Id + ".jpg";
        getImagefromNet image;
        Bitmap bitmap;

        @Override
        protected String doInBackground(String... params) {
            image = new getImagefromNet(url);
            bitmap = image.image();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            if(bitmap!=null)
            {
                ((ImageView) findViewById(R.id.userimage)).setImageBitmap(bitmap);
            }
            super.onPostExecute(s);
        }
    }
}
