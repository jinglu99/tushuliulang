package com.zjut.tushuliulang.tushuliulang.net;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Ben on 2015/9/21.
 */
public class upload_book_share_comment {
    private String url ;
    private boolean result = false;
    private String shareid="";
    private String stuid = "";
    private String comment = "";

    private String tmp = "";
    private InputStream is;


    public upload_book_share_comment(COMMENT comment,int k)
    {
        this.shareid = comment.shareid;
        this.stuid = comment.stuid;
        this.comment = comment.comment;

        switch (k)
        {
            case 0:
                url = TSLLURL.booksharecomment_book;
                break;
            case 1:
                url = TSLLURL.booksharecomment_ppt;
                break;

        }
    }
    public boolean fetch()
    {
        connect();
        regexp();

        return result;
    }

    private void regexp() {

        Pattern pattern = Pattern.compile("<result>true</result>");
        Matcher matcher = pattern.matcher(tmp);
        if (matcher.find())
        {
            result = true;
        }
        else
        {
            result = false;
        }
    }

    private void connect() {
        List<BasicNameValuePair> gets = new LinkedList<>();

        gets.add(new BasicNameValuePair("shareid",shareid));
        gets.add(new BasicNameValuePair("stuid",stuid));
        gets.add(new BasicNameValuePair("comment",comment));



        String get = URLEncodedUtils.format(gets, "UTF-8");
//        HttpGet getmethod = new HttpGet(TSLLURL.search + '?' + get);


        try {
            //得到HttpClient对象
            HttpClient getClient = new DefaultHttpClient();
            //得到HttpGet对象
            HttpGet request;

                request = new HttpGet(url + "?" +
                        get);

            //客户端使用GET方式执行请教，获得服务器端的回应response
            HttpResponse response = getClient.execute(request);
            //判断请求是否成功
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                Log.i("tag", "请求服务器端成功");
                //获得输入流
                HttpEntity entity = response.getEntity();
                is = entity.getContent();

                //关闭输入流

            } else {
                Log.i("tag", "请求服务器端失败");
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e("wonrg", "wrong");

            e.printStackTrace();
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();

            tmp = sb.toString();
        } catch (Exception e) {
        }
    }
}
