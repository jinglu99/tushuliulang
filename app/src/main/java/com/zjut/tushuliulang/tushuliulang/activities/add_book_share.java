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
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.zjut.tushuliulang.tushuliulang.R;
import com.zjut.tushuliulang.tushuliulang.backoperate.GetInfoFromFile;
import com.zjut.tushuliulang.tushuliulang.net.BOOK_SHARE;
import com.zjut.tushuliulang.tushuliulang.net.PublishBookShare;
import com.zjut.tushuliulang.tushuliulang.net.STU_INFO;
import com.zjut.tushuliulang.tushuliulang.net.UploadFile;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

public class add_book_share extends ActionBarActivity  {
    private ImageView imageView;
    private EditText et_book_name;
    private EditText et_isbn;
    private EditText et_intro;
    private EditText et_phone;
    private EditText et_qq;

    private ActionBar actionBar;

    private String imagedir = "";

    private static int RESULT_LOAD_IMAGE = 1;
    private static int RESULT_CAMERA=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book_share);

        et_book_name = (EditText) findViewById(R.id.add_book_share_book_name);
        et_intro = (EditText) findViewById(R.id.add_book_share_intro);
        et_phone = (EditText) findViewById(R.id.add_book_share_phone);
        et_isbn = (EditText) findViewById(R.id.add_book_share_isbn);
        et_qq = (EditText) findViewById(R.id.add_book_share_qq);
        imageView = (ImageView) findViewById(R.id.add_book_share_pic);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               CharSequence[] items = {"本地","相机"};
                AlertDialog dialog = new AlertDialog.Builder(add_book_share.this)
                            .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which)
                                {
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
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_book_share, menu);
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
        if (id == R.id.add) {
            add_book_share();
            return true;
        }
        if (id == android.R.id.home) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
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


            imagedir = picturePath;
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        }
        else if(requestCode == RESULT_CAMERA&& resultCode==RESULT_OK&&data!=null)
        {
            String sdStatus = Environment.getExternalStorageState();
                        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
                            Log.i("TestFile",
                                    "SD card is not avaiable/writeable right now.");
                            return;
                        }
                        String name = new DateFormat().format("yyyyMMdd_HHmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
//                        Toast.makeText(this, name, Toast.LENGTH_LONG).show();
                        Bundle bundle = data.getExtras();
                        Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式

                        FileOutputStream b = null;
                       //???????????????????????????????为什么不能直接保存在系统相册位置呢？？？？？？？？？？？？

                        String fileName = Environment.getExternalStorageDirectory().getPath()+"/tushuliulang/image/"+name;

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
    public void add_book_share()
    {
        STU_INFO stu_info = GetInfoFromFile.getinfo();

        if (stu_info==null)
        {
            Toast.makeText(this,"请先登录",Toast.LENGTH_SHORT).show();
            return;
        }

        BOOK_SHARE book_share = new BOOK_SHARE();
        book_share.book_name = et_book_name.getText().toString();
        book_share.isbn = et_isbn.getText().toString();
        book_share.intro = et_intro.getText().toString();
        book_share.phone = et_phone.getText().toString();
        book_share.qq = et_qq.getText().toString();
        book_share.owner = GetInfoFromFile.getinfo().Id;


        if(!book_share.book_name.equals("")&&!book_share.isbn.equals(""))
        {
            if(!book_share.qq.equals("")||!book_share.phone.equals(""))
            {
                if (book_share.qq.equals(""))
                {
                    book_share.qq = "0";
                }
                if (book_share.phone.equals(""))
                {
                    book_share.phone="0";
                }
                if (imagedir!="")
                {
                    book_share.imagedir = imagedir;
                    new publish().execute(book_share);
                }
                else
                {
                    AlertDialog dialog = new AlertDialog.Builder(add_book_share.this).setMessage("必须上传图片").create();
                    dialog.show();
                }
            }
            else
            {
                AlertDialog dialog = new AlertDialog.Builder(add_book_share.this).setMessage("手机或QQ必填其一").create();
                dialog.show();
            }
        }
        else
        {
            AlertDialog dialog = new AlertDialog.Builder(add_book_share.this).setMessage("书名或ISBN不能为空").create();
            dialog.show();
        }
    }

    class publish extends AsyncTask<BOOK_SHARE,String,String>
    {
        PublishBookShare publishBookShare;
        UploadFile uploadFile;

        boolean added = false;

        @Override
        protected String doInBackground(BOOK_SHARE... params) {
            publishBookShare = new PublishBookShare(params[0]);
            if(added = publishBookShare.add())
            {
                uploadFile = new UploadFile(params[0].imagedir,publishBookShare.getShare().number_order+".jpg",2);
                uploadFile.uploadFile();
                if(uploadFile.getResult().equals(""))
                {
                    added = false;
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (added)
            {
                finish();
            }
            else
            {
                AlertDialog dialog = new AlertDialog.Builder(add_book_share.this).setMessage("亲，发生了不可预知的错误").create();
                dialog.show();
            }
            super.onPostExecute(s);
        }
    }

}
