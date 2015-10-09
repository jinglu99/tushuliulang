package com.zjut.tushuliulang.tushuliulang.splash;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;

import com.zjut.tushuliulang.tushuliulang.MainActivity;
import com.zjut.tushuliulang.tushuliulang.R;
import com.zjut.tushuliulang.tushuliulang.net.TSLLURL;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Created by Administrator on 2015/8/11 0011.
 */
public class activity_Splash extends Activity {
    private TextView tv_version;
    String path = TSLLURL.getupdate;

    private String mversionName;
    private String mdescription;
    private int mversionCode;
    private String mdownload;

    private final int Code_Dialog = 1;
    private final int Code_Error_URL = 2;
    private final int Code_Error_Internet = 3;
    private final int Code_Error_JSON = 4;

    static int current;

    private boolean iscancle = false;

    ProgressDialog progressDialog;


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){
                case Code_Dialog:
                    dialog();
                    break;
                case Code_Error_JSON:
                    Toast.makeText(activity_Splash.this, "JSON解析失败", Toast.LENGTH_SHORT).show();
                    break;
                case Code_Error_Internet:
                    Toast.makeText(activity_Splash.this, "网络错误", Toast.LENGTH_SHORT).show();
                    break;
                case Code_Error_URL:
                    Toast.makeText(activity_Splash.this, "URL异常", Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        tv_version = (TextView) findViewById(R.id.tv_version);

        //获得本地版本名称
        tv_version.setText("版本号："+GetVersionName());

        //检测服务器版本号是否需要更新
        CheckVersion();
    }

    /**
     *获取本地版本名称
     */
    private String GetVersionName() {
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(),0);

            String versionName = packageInfo.versionName;

            Log.e("MY", "CurrentGetVersionName " + versionName);

            return versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     *获取本地版本号
     */
    private int GetVersionCode() {
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(),0);

            int versionCode = packageInfo.versionCode;


            Log.e("MY", "CurrentVersionCode " + versionCode);

            return versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return -1;
    }

    /**
     *检查服务器版本号与本地版本号是否匹配
     */
    private void CheckVersion() {

        final long startTime = System.currentTimeMillis();

        Thread t = new Thread(){
            @Override
            public void run() {
                Message message = Message.obtain();
                URL url ;
                HttpURLConnection conn = null;
                try {
                    url = new URL(path);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(3000);
                    conn.setReadTimeout(3000);

                    if(conn.getResponseCode() == 200) {

                        InputStream is = conn.getInputStream();

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();

                        byte[] b = new byte[1024];
                        int len = 0;

                        while ((len = is.read(b)) != -1) {
                            baos.write(b, 0, len);
                        }

                        String content = baos.toString();

                        Log.e("MY", "PHPVersion：" + content);

                        is.close();
                        baos.close();

                        //解析JSON
                        analysis_JSON(content,message);

                        //校验版本信息
                        if (GetVersionCode() < mversionCode) {

                            //弹出升级框
                            message.what = Code_Dialog;
                            handler.sendMessage(message);

                        } else {

                            long  endTime = System.currentTimeMillis();
                            long useTime = endTime - startTime;
                            //保证闪屏页时间为5秒

                            if(useTime < 2000){
                                Log.e("MY", "闪屏页面2秒睡眠开始");
                                try {
                                    Thread.sleep(2000 - useTime);
                                    Log.e("MY", "闪屏页面2秒睡眠结束");
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }

                            //进入主界面
                            skipMainActivity();
                        }
                    }
                } catch (MalformedURLException e) {
                    //URL异常
                    message.what =Code_Error_URL;
                    handler.sendMessage(message);
                    skipMainActivity();
                    e.printStackTrace();
                }
                catch (IOException e) {
                    //网络异常
                    message.what =Code_Error_Internet;
                    handler.sendMessage(message);
                    skipMainActivity();
                    e.printStackTrace();
                }
                finally {
                    if(conn!=null){
                        conn.disconnect();
                    }

                }
            }
        };
        t.start();
    }

    /**
     *解析JSON
     */
    private void analysis_JSON(String content,Message message){
        try {
            Log.e("MY", "JSON 解析开始");
            JSONObject json = new JSONObject(content);

            mversionName = json.getString("versionName");
            mdescription = json.getString("description");
            mversionCode = json.getInt("versionCode");
            mdownload = json.getString("downloadUrl");
            Log.e("MY", "JSON 解析结束");
        } catch (JSONException e) {
            //JSON异常
            Log.e("MY", "JSON 解析异常");
            message.what =Code_Error_JSON;
            handler.sendMessage(message);
            skipMainActivity();
            e.printStackTrace();
        }
    }


    /**
     * 升级对话框
     */
    private void dialog(){
        Log.e("MY", "进入升级对话框");
        AlertDialog.Builder build = new AlertDialog.Builder(this);
        build.setTitle("最新版本" + mversionName);
        build.setMessage(mdescription);
        build.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //下载资源
                APKDownload();
            }
        });

        build.setNegativeButton("等会更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //跳到主界面
                skipMainActivity();
            }
        });

        build.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                //返回键触发跳转主界面
                skipMainActivity();
            }
        });

        build.show(); // 弹框
    }

    /**
     * 跳转主界面
     */
    private void skipMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);

        startActivity(intent);

        //防止退出时进入splash界面或安装界面
        finish();
    }

    /**
     * 下载更新文件
     */
    private void APKDownload(){

        //通过进度条弹框显示进度
        Log.e("MY", "显示进度条弹框");
        progressDialog = new ProgressDialog(activity_Splash.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMessage("正在下载更新");
        progressDialog.show();
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                //禁止安装
                iscancle =true;
                //退出当前界面
                finish();
                //调到主界面
                skipMainActivity();
            }
        });

        //下载并安装apk
        Mythread t = new Mythread(mdownload, progressDialog){
            @Override
            public void run() {
                super.run();
            }
        };

        t.start();

    }

    //文件名
    public static String Spilt(String path){
        int idx = path.lastIndexOf("/");
        return path.substring(idx + 1, path.length());
    }

    /**
     *安装apk
     */
    private void installApk(File file) {
        Intent intent = new Intent();
        //执行动作
        intent.setAction(Intent.ACTION_VIEW);

        //安装后点打开会打开新版本
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        //执行的数据类型
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");

        //安装时点返回键处理
        startActivityForResult(intent, 0);

        //提示完成和打开
        android.os.Process.killProcess(android.os.Process.myPid());
    }


    /**
     *对安装返回键处理
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //返回主界面
        skipMainActivity();
        super.onActivityResult(requestCode, resultCode, data);
    }

    //自定义线程
    class Mythread extends Thread {

        private String path;
        private ProgressDialog progressDialog;

        public Mythread(String path, ProgressDialog progressDialog) {
            this.path = path;
            this.progressDialog = progressDialog;
        }

        @Override
        public void run() {
            File file = null;
            try {
                URL url = new URL(path);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setReadTimeout(5000);
                conn.setConnectTimeout(5000);
                if(conn.getResponseCode() == 200) {
                    int length = conn.getContentLength();

                    String updatadir = Environment.getExternalStorageDirectory().toString() + "/tushuliulang/update/";
                    File filedir = new File(updatadir);
                    //若是不存在此文件夹，创建此文件夹
                    if (!filedir.exists()){
                        filedir.mkdir();
                    }
                    file = new File(Environment.getExternalStorageDirectory().getPath() + "/tushuliulang/update",Spilt(mdownload));

                    //设置进度条最大值
                    progressDialog.setMax(length);
                    InputStream is = conn.getInputStream();
                    RandomAccessFile raf = new RandomAccessFile(file,"rwd");

                    byte[] b =new byte[1024];
                    int len = 0;
                    int total =0;
                    while((len=is.read(b))!=-1) {
                        raf.write(b,0,len);
                        total+=len;
                        current += len;

                        //设置进度条当前值
                        progressDialog.setProgress(current);
                    }
                    Log.e("MY", "本次APK下载了：" + total);
                    raf.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                //如果按了退出键，则停止安装
                if (!iscancle){
                    //安装apk
                    Log.e("MY", "准备安装APK");
                    //结束进度条
                    Log.e("MY", "进度条弹框结束");
                    progressDialog.dismiss();
                    installApk(file);
                    Log.e("MY", "APK安装完毕");
                }

            }
        }
    }
}


