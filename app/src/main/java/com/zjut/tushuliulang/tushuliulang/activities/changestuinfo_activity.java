package com.zjut.tushuliulang.tushuliulang.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.zjut.tushuliulang.tushuliulang.R;
import com.zjut.tushuliulang.tushuliulang.backoperate.*;

import com.zjut.tushuliulang.tushuliulang.net.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by zz on 2015/9/25.
 */
public class changestuinfo_activity extends ActionBarActivity{

    private EditText name;
    private EditText username;
    private EditText college;
    private EditText stuclass;
    private EditText grade;
    private EditText motto;
    private EditText phone;
    private EditText email;
    private EditText sex;
    private EditText major;
    private ImageView imageView;
    private Button exit;

    private int mod = 0;
    private STU_INFO stuinfo;

    private String imagedir = "";

    private static int RESULT_LOAD_IMAGE = 1;
    private static int RESULT_CAMERA=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changestu);

        initwidget();
    }

    private void initwidget() {
        name = (EditText) findViewById(R.id.name);
        username = (EditText) findViewById(R.id.username);
        college = (EditText) findViewById(R.id.college);
        stuclass = (EditText) findViewById(R.id.stuclass);
        grade = (EditText) findViewById(R.id.grade);
        motto = (EditText) findViewById(R.id.motto);
        phone = (EditText) findViewById(R.id.phone);
        email = (EditText) findViewById(R.id.email);
        sex = (EditText) findViewById(R.id.sex);
        major = (EditText) findViewById(R.id.major);
        imageView = (ImageView) findViewById(R.id.changeinfopic);
        exit = (Button) findViewById(R.id.quit);

        imageView.setFocusable(true);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mod == 1) {
                    CharSequence[] items = {"本地", "相机"};
                    AlertDialog dialog = new AlertDialog.Builder(changestuinfo_activity.this)
                            .setItems(items, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case 0:
                                            Intent i = new Intent(
                                                    Intent.ACTION_PICK,
                                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                                            startActivityForResult(i, RESULT_LOAD_IMAGE);
                                            break;
                                        case 1:
                                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                            startActivityForResult(intent, RESULT_CAMERA);

                                    }

                                }
                            }).create();
                    Window window = dialog.getWindow();
                    window.setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置

                    dialog.show();

                }
            }
        });


        name.setEnabled(false);

        username.setEnabled(false);

        college.setEnabled(false);

        stuclass.setEnabled(false);

        grade.setEnabled(false);

        motto.setEnabled(false);

        phone.setEnabled(false);

        email.setEnabled(false);

        sex.setEnabled(false);

        major.setEnabled(false);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(Environment.getExternalStorageDirectory().getPath()+"/tushuliulang/data/info.db");

                if(file.exists())
                {
                    file.delete();
                    Intent intent = new Intent();
                    intent.setAction("quit");
                    sendBroadcast(intent);
                    finish();

                }
            }
        });



        stuinfo = GetInfoFromFile.getinfo();
        name.setText(stuinfo.Name);
        username.setText(stuinfo.UserName);
        college.setText(stuinfo.college);
        motto.setText(stuinfo.Motto);
        grade.setText(stuinfo.Grade);
        stuclass.setText(stuinfo.Class);
        phone.setText(stuinfo.Phone);
        email.setText(stuinfo.Email);
        sex.setText(stuinfo.Sex);
        major.setText(stuinfo.major);

        Bitmap bitmap = null;
        try {
            FileInputStream image = new FileInputStream
                    (Environment.getExternalStorageDirectory().getPath()+"/tushuliulang/data/"+stuinfo.Id+".jpg");
            bitmap = BitmapFactory.decodeStream(image);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (bitmap!=null) {
            imageView.setImageBitmap(bitmap);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();


            imagedir = Environment.getExternalStorageDirectory().getPath() + "/tushuliulang/data/" +
                    stuinfo.Id + ".jpg";

            Copyfile.copyFile(picturePath,imagedir);

            imageView.setImageBitmap(BitmapFactory.decodeFile(imagedir));

        }
        else if(requestCode == RESULT_CAMERA&& resultCode==RESULT_OK&&data!=null)
        {
            String sdStatus = Environment.getExternalStorageState();
            if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
                Log.i("TestFile",
                        "SD card is not avaiable/writeable right now.");
                return;
            }
            String name = stuinfo.Id + ".jpg";
//                        Toast.makeText(this, name, Toast.LENGTH_LONG).show();
            Bundle bundle = data.getExtras();
            Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式

            FileOutputStream b = null;
            //???????????????????????????????为什么不能直接保存在系统相册位置呢？？？？？？？？？？？？

            String fileName = Environment.getExternalStorageDirectory().getPath()+"/tushuliulang/data/"+name;

            try {
                b = new FileOutputStream(fileName);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    b.flush();
                    b.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            imageView.setImageBitmap(bitmap);// 将图片显示在ImageView里
            imagedir = fileName;

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //随便加了个MENU
        getMenuInflater().inflate(R.menu.menu_activity_questions_enter, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id)
        {
            case R.id.answer:
                if (mod == 0) {
                    name.setEnabled(true);

                    username.setEnabled(true);

                    college.setEnabled(true);

                    stuclass.setEnabled(true);

                    grade.setEnabled(true);

                    motto.setEnabled(true);

                    phone.setEnabled(true);

                    email.setEnabled(true);

                    sex.setEnabled(true);

                    major.setEnabled(true);

                    exit.setVisibility(View.INVISIBLE);

                    mod =1;
                }
                else
                {


                    stuinfo.Name = name.getText().toString();
                    stuinfo.UserName = username.getText().toString();
                    stuinfo.college = college.getText().toString();
                    stuinfo.Class = stuclass.getText().toString();
                    stuinfo.Grade = grade.getText().toString();
                    stuinfo.major = major.getText().toString();
                    stuinfo.Phone = phone.getText().toString();
                    stuinfo.Email = email.getText().toString();
                    stuinfo.Sex = sex.getText().toString();
                    stuinfo.Motto = motto.getText().toString();


                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    LayoutInflater factory = LayoutInflater.from(this);
                    final View textEntryView = factory.inflate(R.layout.alterdialog_password, null);
//                    builder.setIcon(R.drawable.icontsll);
                    builder.setTitle("验证密码");
                    builder.setView(textEntryView);
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                         EditText p = (EditText) textEntryView.findViewById(R.id.p_tv);
                            stuinfo.password = p.getText().toString();
                            new changge().execute();
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                        }
                    });
                    builder.create().show();

                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    class changge extends AsyncTask<String,String,String>
    {
        private Change_Info change_info;
        private boolean result = false;
        @Override
        protected String doInBackground(String... params) {
            change_info = new Change_Info(stuinfo.Id,stuinfo.password,stuinfo);
            String str = "<login>true</login>\n" +
                    "\t\t\t<stu>\n" +
                    "\t\t\t<name>"+stuinfo.Name+"</name>\n" +
                    "\t\t\t<username>"+stuinfo.UserName+"</username>\n" +
                    "\t\t\t<college>"+stuinfo.college+"</college>\n" +
                    "\t\t\t<major>"+stuinfo.major+"</major>\n" +
                    "\t\t\t<class>"+stuinfo.Class+"</class>\n" +
                    "\t\t\t<grade>"+stuinfo.Grade+"</grade>\n" +
                    "\t\t\t<motto>"+stuinfo.Motto+"</motto>\n" +
                    "\t\t\t<phone>"+stuinfo.Phone+"</phone>\n" +
                    "\t\t\t<email>"+stuinfo.Email+"</email>\n" +
                    "\t\t\t<sex>"+stuinfo.Sex+"</sex>\n" +
                    "\t\t\t<pic></pic>\n" +
                    "\t\t\t<stu_id>"+stuinfo.Id+"</stu_id></stu>\n";

            if (change_info.Uploadinfo()) {
                change_info.uploadpic();

                SaveToFile sa = new SaveToFile(Environment.getExternalStorageDirectory().getPath()+"/tushuliulang/data/info.db",
                        str   );
                sa.save();

                Intent intent = new Intent();
                intent.setAction("logined");
                sendBroadcast(intent);
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

           if (result) {
               mod = 0;

               imageView.setFocusable(true);

               name.setEnabled(false);

               username.setEnabled(false);

               college.setEnabled(false);

               stuclass.setEnabled(false);

               grade.setEnabled(false);

               motto.setEnabled(false);

               phone.setEnabled(false);

               email.setEnabled(false);

               sex.setEnabled(false);

               major.setEnabled(false);

               exit.setVisibility(View.VISIBLE);
               Toast.makeText(changestuinfo_activity.this,"修改成功",Toast.LENGTH_SHORT).show();
               super.onPostExecute(s);
           }
            else
           {
               Toast.makeText(changestuinfo_activity.this,"账号或密码错误",Toast.LENGTH_SHORT).show();
           }
        }
    }

}

