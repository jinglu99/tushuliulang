package com.zjut.tushuliulang.tushuliulang.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zjut.tushuliulang.tushuliulang.R;
import com.zjut.tushuliulang.tushuliulang.backoperate.GetInfoFromFile;
import com.zjut.tushuliulang.tushuliulang.backoperate.SaveToFile;
import com.zjut.tushuliulang.tushuliulang.net.STU_INFO;
import com.zjut.tushuliulang.tushuliulang.net.login;


public class login_activity extends ActionBarActivity implements View.OnClickListener{

    ActionBar actionBar;
    EditText et_username;
    EditText et_password;
    Button bt_login;
    CheckBox checkBox;
    TextView tv_explaination;

    String username;
    String password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);


        initwidget();

    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 1){
                Toast.makeText(login_activity.this,"请输入账号和密码",Toast.LENGTH_SHORT).show();
            }
            else if (msg.what == 2){
                Toast.makeText(login_activity.this, "登录成功", Toast.LENGTH_SHORT).show();
            }
        }
    };
    private void initwidget() {
        et_username = (EditText) findViewById(R.id.et_user);
        et_password = (EditText) findViewById(R.id.et_password);
        bt_login = (Button) findViewById(R.id.bt_login);
        checkBox = (CheckBox) findViewById(R.id.rememberinfo);
        tv_explaination = (TextView) findViewById(R.id.tv_explaination);

        bt_login.setOnClickListener(this);


        getremember();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login_activity, menu);
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
        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     *监听 教务系统？
     */
    public void explaination(View view){
        //显示说明
        tv_explaination.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.bt_login:
                username = et_username.getText().toString();
                password = et_password.getText().toString();
                if(username.equals("") || password.equals("")){
                    //发送内容为空消息
                    Message message = Message.obtain();
                    message.what = 1;
                    handler.sendMessage(message);

                }else if (checkBox.isChecked()==true)
                {
                    String remember = "<username>"+username+"</username>"+"<password>"+password+"</password>";

                    SaveToFile saveToFile = new SaveToFile
                            (Environment.getExternalStorageDirectory().getPath()+"/tushuliulang/data/mem.db",remember);
                    saveToFile.save();
                }
                new login_function().execute();
                break;

        }
    }
    private class login_function extends AsyncTask<String,String,String>
    {
        String wrong = "";


        @Override
        protected String doInBackground(String... params) {
          if((!username.equals(""))&&(!password.equals("")))
          {
              login l = new login(username,password);

              String message= l.fetch();
              Log.e("MY", "message:"+message);

              if (message.equals("登陆成功"))
              {
                  Log.e("MY", "进来吧");

                  finish();

                  Message message1 = Message.obtain();
                  message1.what = 2;
                  handler.sendMessage(message1);
                  Intent intent = new Intent();
                  intent.setAction("logined");
                  sendBroadcast(intent);
              }
              else if(message.equals("网络错误"))
              {
                  wrong = "网络错误";
              }
              else if(message.equals("账号密码错误")){
                  wrong = "账号密码错误";
              }
          }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.e("MY", "wrong:"+wrong);
            if(wrong.equals("账号密码错误"))
            {
                Log.e("MY", "准备土司1");
                Toast.makeText(login_activity.this, "账号或密码错误", Toast.LENGTH_SHORT).show();
            }else if (wrong.equals("网络错误")){
                Log.e("MY", "准备土司2");
                Toast.makeText(login_activity.this, "网络错误", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void getremember()
    {
        STU_INFO stu_info = GetInfoFromFile.getlogininfo();
        if(!stu_info.Id.equals(""))
        {
            et_username.setText(stu_info.Id);
        }
        if(!stu_info.password.equals(""))
        {
            et_password.setText(stu_info.password);
        }

    }
}
