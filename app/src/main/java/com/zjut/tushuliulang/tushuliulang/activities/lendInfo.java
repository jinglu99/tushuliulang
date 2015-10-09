package com.zjut.tushuliulang.tushuliulang.activities;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zjut.tushuliulang.tushuliulang.R;
import com.zjut.tushuliulang.tushuliulang.backoperate.GetInfoFromFile;
import com.zjut.tushuliulang.tushuliulang.bookshare.getBookShareInfo;
import com.zjut.tushuliulang.tushuliulang.library.net.*;
import com.zjut.tushuliulang.tushuliulang.library.ui.*;
import com.zjut.tushuliulang.tushuliulang.listadapter_comment;
import com.zjut.tushuliulang.tushuliulang.net.GetStuInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class lendInfo extends ActionBarActivity {

    private Context context = this;
    private LENDINFO[] lendinfos;
    private GetStuInfo getStuInfo;
    private ListView listView;
    private android.support.v7.app.ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lend_info);

        listView = (ListView) findViewById(R.id.lendinfo_lv);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                LayoutInflater factory = LayoutInflater.from(context);
                final View ownerinfo = factory.inflate(R.layout.alterdialog_ownerinfo, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("联系信息");
                builder.setView(ownerinfo);

                TextView name = (TextView) ownerinfo.findViewById(R.id.owner_owner);
                TextView phone = (TextView) ownerinfo.findViewById(R.id.owner_phone);
                TextView qq = (TextView) ownerinfo.findViewById(R.id.owner_qq);
                TextView email = (TextView) ownerinfo.findViewById(R.id.owner_email);

                name.setText(getStuInfo.getStu_info().UserName);
                if (!getStuInfo.getStu_info().Phone.equals("0"))
                    phone.setText(getStuInfo.getStu_info().Phone);
                else
                    phone.setText("无");

                    qq.setText("无");
                if (getStuInfo.getStu_info().Email.equals(""))
                    email.setText(getStuInfo.getStu_info().Email);
                else
                    email.setText("无");

                builder.setPositiveButton("借阅", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("是否确认");

                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                                new BorrowBook().execute(String.valueOf(position));
                            }
                        });
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                            }
                        });
                        builder.create().show();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {}});
                builder.create().show();
            }
        });


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("删除");

                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        new delete().execute(String.valueOf(position));
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });
                builder.create().show();
                return false;
            }
        });
        new getInfo().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lend_info, menu);
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
        if (id==android.R.id.home)
        {
            finish();
        }
        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    class getInfo extends AsyncTask<String,String,String>
    {
        private getLendInfo lendInfo;

        private boolean result = false;
        private List<Map<String,Object>> l;

        @Override
        protected String doInBackground(String... params) {
            lendInfo = new getLendInfo(GetInfoFromFile.getinfo().Id);
            if(lendInfo.fetch())
            {
                lendinfos = lendInfo.getLendinfos();
                result = true;

                l= new ArrayList<Map<String, Object>>();
                for(int n = 0;n<lendinfos.length; n++)
                {
                    String name = lendinfos[n].lender;

                    getStuInfo = new GetStuInfo(lendinfos[n].lender);
                    if (getStuInfo.fetch())
                    {
                        name = getStuInfo.getStu_info().UserName;
                    }

                    Map<String,Object> m = new HashMap<String,Object>();
                    m.put("stuid",lendinfos[n].lender);
                    m.put("username",name);

                    m.put("date",lendinfos[n].time);

                    l.add(m);
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (result)
            {

                listView.setAdapter(new listadapter_lendinfo(context, l));
            }
            super.onPostExecute(s);
        }
    }

    class BorrowBook extends AsyncTask<String,String,String> {
        private borrowBook borrow;
        private boolean result = false;
        getBookShareInfo bookShareInfo;
        private String code = "";
        private String codeincode="";

        private deleteLendInfo delete;
        @Override
        protected String doInBackground(String... params) {

            int n = Integer.parseInt(params[0]);
            bookShareInfo = new getBookShareInfo(lendinfos[n].lender);
            if (bookShareInfo.fetch().equals("true"))
            {
                code = bookShareInfo.getShareinfo().code;
                codeincode = bookShareInfo.getShareinfo().codeincode;
            }

            borrow = new borrowBook(lendinfos[n].lender,code,codeincode,lendinfos[n].lender,"2");
            if (borrow.fetch())
            {
                result = true;
                delete = new deleteLendInfo(lendinfos[n].lender,lendinfos[n].lender);
              delete.deleteinfo();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            if (result)
            {
                Toast.makeText(context,"借书成功！",Toast.LENGTH_SHORT).show();
            }
            else {
                if (borrow.getError().equals("book_unavailable"))
                {
                    Toast.makeText(context,"此书已被借！",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(context,"出现未知错误！",Toast.LENGTH_SHORT).show();
                }
            }
            new getInfo().execute();
            super.onPreExecute();
        }
    }
    class delete extends AsyncTask<String,String,String>
    {
        private deleteLendInfo delete;
        private boolean result = false;
        @Override
        protected String doInBackground(String... params) {
            int n = Integer.parseInt(params[0]);
            delete = new deleteLendInfo(lendinfos[n].lender,lendinfos[n].lender);
            if(delete.deleteinfo())
            {
                result = true;
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (result)
            {
                Toast.makeText(context,"删除成功！",Toast.LENGTH_SHORT).show();
                new getInfo().execute();
            }
            else
            {
                Toast.makeText(context,"删除失败！",Toast.LENGTH_SHORT).show();
            }

            super.onPostExecute(s);
        }
    }
}
