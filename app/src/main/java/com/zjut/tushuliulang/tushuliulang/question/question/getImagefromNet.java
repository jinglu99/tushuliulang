package com.zjut.tushuliulang.tushuliulang.question.question;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Ben on 2015/7/30.
 */
public class getImagefromNet {
    String url;
    public getImagefromNet(String url)
    {
        this.url = url;
    }
    public Bitmap image()  {
        byte[] data = new byte[0];
        try {
            data = getImage(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);  //生成位图
        return bitmap;

    }
        private static byte[] getImage(String path) throws IOException {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");   //设置请求方法为GET
            conn.setReadTimeout(5*1000);    //设置请求过时时间为5秒
            InputStream inputStream = conn.getInputStream();   //通过输入流获得图片数据
            byte[] data = readInputStream(inputStream);     //获得图片的二进制数据
            return data;
    }

        /*
         * 从数据流中获得数据
         */
        private static  byte[] readInputStream(InputStream inputStream) throws IOException {
            byte[] buffer = new byte[1024];
            int len = 0;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            while((len = inputStream.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            bos.close();
            return bos.toByteArray();

        }


}
