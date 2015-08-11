package com.zjut.tushuliulang.tushuliulang.net;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2015/8/9 0009.
 */
public class download_thread extends Thread {

    long startIndex;
    long endIndex;
    String path;
    String savedir;

    public download_thread(long startIndex, long endIndex, String path, String savedir) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.path = path;
        this.savedir = savedir;
    }

    @Override
    public void run() {
        try {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setReadTimeout(3000);
            conn.setConnectTimeout(3000);
            conn.setRequestProperty("Range", "bytes="+startIndex+"-"+endIndex);
            if(conn.getResponseCode() == 206) {
                InputStream is = conn.getInputStream();
                //第一个参数为存放位置（此处为sd卡），第二个参数为文件名
                File file = new File(Environment.getExternalStorageDirectory().getPath()+savedir,Spilt(path));
                Log.e("MY", Environment.getExternalStorageDirectory().getPath());
                RandomAccessFile raf = new RandomAccessFile(file,"rwd");

                raf.seek(startIndex);

                byte[] b =new byte[1024];
                int len = 0;
                int total =0;
                while((len=is.read(b))!=-1) {
                    raf.write(b,0,len);
                    total+=len;
                }
                Log.e("MY", "本次下载了：" + total);
                raf.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //文件名
    public String Spilt(String path){
        int idx = path.lastIndexOf("/");
        return path.substring(idx + 1, path.length());
    }
}
