package com.zjut.tushuliulang.tushuliulang.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

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
    TextView wrong_info;
    CheckBox checkBox;

    String username;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);


        initwidget();


    }

    private void initwidget() {
        et_username = (EditText) findViewById(R.id.et_user);
        et_password = (EditText) findViewById(R.id.et_password);
        bt_login = (Button) findViewById(R.id.bt_login);
        wrong_info = (TextView) findViewById(R.id.wrong_info);
        checkBox = (CheckBox) findViewById(R.id.rememberinfo);

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

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.bt_login:
                username = et_username.getText().toString();
                password = et_password.getText().toString();
                if (checkBox.isChecked()==true)
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
        boolean wrong = false;


        @Override
        protected String doInBackground(String... params) {
          if((!username.equals(""))&&(!password.equals("")))
          {
              login l = new login(username,password);



              if (l.fetch())
              {
                  try {
                      Thread.sleep(5000);
                  } catch (InterruptedException e) {
                      e.printStackTrace();
                  }
                  finish();
                  Intent intent = new Intent();
                  intent.setAction("logined");
                  sendBroadcast(intent);
              }
              else
              {
                  wrong = true;
              }
          }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(wrong==true)
            {
                wrong_info.setVisibility(View.VISIBLE);
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
