package com.zjut.tushuliulang.tushuliulang.net.question.question;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.zjut.tushuliulang.tushuliulang.R;
import com.zjut.tushuliulang.tushuliulang.backoperate.GetInfoFromFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Administrator on 2015/9/3 0003.
 */
public class RespondActivity extends ActionBarActivity {

    private EditText et_respond_question ;

    private final int Code_Success = 1;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == Code_Success){
                Toast.makeText(RespondActivity.this, "发表成功", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_respond);

        et_respond_question = (EditText) findViewById(R.id.et_respond_question);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_respond, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.Upload){
            //上传回答
           // String respond = et_respond_question.getText().toString();
            //弹框提示
            Upload_Pop_Up();
            return true;
        }else if (id == R.id.home){
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 发表按钮弹框提示
     */
    public void Upload_Pop_Up(){
        new AlertDialog.Builder(this).setTitle("发表提示")//设置对话框标题

                .setMessage("确定发表吗？")//设置显示的内容

                .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮


                    @Override

                    public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                        //这里是发表设置，需要与服务器交互，提交数据

                        //判断是否为空
                        if (TextUtils.isEmpty(et_respond_question.getText())) {
                            //内容为空弹框提示
                            Null_Pop_Up();
                        } else {
                            String respond = et_respond_question.getText().toString();
                            String questionID = (String) getIntent().getExtras().get("questionID");

                            File file = new File(Environment.getExternalStorageDirectory().getPath() + "/tushuliulang/data/info.db");
                            if (!file.exists()) {
                                //请登录弹框
                                Toast.makeText(RespondActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                String studentID = GetInfoFromFile.getinfo().Id;
                                //通过get提交数据
                                SubMitData(respond, studentID, questionID);
                            }
                        }

                    }

                }).setNegativeButton("返回", new DialogInterface.OnClickListener() {//添加返回按钮


            @Override

            public void onClick(DialogInterface dialog, int which) {//响应事件
                Log.e("MY", " 不发表哦");
            }

        }).show();//在按键响应事件中显示此对话框
    }

    /**
     * 内容为空弹框提示
     */
    public void Null_Pop_Up(){
        new AlertDialog.Builder(this).setTitle("空内容提示")
                .setMessage("请确定已在回答栏输入你的内容")
                .setPositiveButton("确定", null)
                .show();
    }
    /**
     * get提交数据
     */
    public void SubMitData(final String respond , final String studentID,final String questionID){
        Log.e("MY", "准备提交回答的所有数据");
        Thread t = new Thread(){
            @Override
            public void run() {
                String path = "http://120.24.242.211/tushu/answer.php"+"?respond="+
                        URLEncoder.encode(respond)+"&studentID="+ URLEncoder.encode(studentID)+
                        "&questionID="+ URLEncoder.encode(questionID);
                //String text;
                try {
                    URL url = new URL(path);
                    HttpURLConnection conn= (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setReadTimeout(5000);
                    conn.setConnectTimeout(5000);

                    if(conn.getResponseCode() == 200){
                        InputStream is = conn.getInputStream();
                        byte[] b = new byte[1024];
                        int len = 0;
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        while ((len = is.read(b))!= -1){
                            baos.write(b,0,len);
                        }
                        String text = new String(baos.toByteArray());
                        Log.e("MY", "所提交的问题的xml内容查看：" + text);

                        //发送消息，吐司弹框
                        Message message = Message.obtain();
                        message.what = Code_Success;
                        handler.sendMessage(message);

                        //最好在服务器判断学号正确性，然后再返回数据是否成功
                        Log.e("MY", "成功提交回答的数据");

                        finish();
                        Intent intent = new Intent();
                        intent.setAction("uploadanswersuccessfully");
                        sendBroadcast(intent);

                    }
                } catch (Exception e) {

                    Log.e("MY", "提交回答的数据失败");
                    e.printStackTrace();
                }
            }
        };
        t.start();

    }
}
