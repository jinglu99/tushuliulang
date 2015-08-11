package com.zjut.tushuliulang.tushuliulang.net;

import android.util.Log;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2015/8/9 0009.
 */
public class Downlaod {

    //注意：需要设置权限
    //1.读取sd卡权限
    //2.internet权限


    //第一参数为下载路径，第二个参数为线程数
    public static void FileDownload(final String path , final String savedir,final int threadNum) {

        Thread t = new Thread(){
            @Override
            public void run() {
                URL url;
                try {
                    url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setReadTimeout(3000);
                    conn.setConnectTimeout(3000);

                    Log.e("MY", "connect");
                    if (conn.getResponseCode() == 200) {
                        long length = conn.getContentLength();
                        long size = length / threadNum;
                        for (int i = 0; i < threadNum; i++) {
                            long startIndex = i * size;
                            long endIndex = (i + 1) * size;

                            if (i == threadNum - 1) {
                                endIndex = length - 1;
                            }

                            Log.e("MY", "子线程下载");
                            new download_thread(startIndex, endIndex, path,savedir).start();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();

    }

}