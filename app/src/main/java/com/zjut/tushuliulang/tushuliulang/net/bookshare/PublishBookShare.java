package com.zjut.tushuliulang.tushuliulang.net.bookshare;

import android.util.Log;

import com.zjut.tushuliulang.tushuliulang.net.TSLLURL;
import com.zjut.tushuliulang.tushuliulang.net.bookshare.BOOK_SHARE;

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
 * Created by Ben on 2015/9/2.
 */
public class PublishBookShare
{
    private BOOK_SHARE share;
    private String tmp = "";
    private String url = TSLLURL.publishbookshare;
    private boolean result = false;
    private InputStream is;


    public PublishBookShare(BOOK_SHARE book_share)
    {
        share = book_share;
    }

    public boolean add()
    {
        connect();
        regexp();

        return result;
    }

    private void regexp() {
        Pattern success = Pattern.compile("<success>true</success>");
        Matcher matcher_success = success.matcher(tmp);
        if (matcher_success.find())
        {
            result = true;

            Pattern number = Pattern.compile("<number>(.*)</number>");
            Matcher matcher_number = number.matcher(tmp);

            matcher_number.find();
            share.number_order = matcher_number.group(1);

        }
    }

    private void connect() {


        List<BasicNameValuePair> gets = new LinkedList<>();
        gets.add(new BasicNameValuePair("owner",share.owner));
        gets.add(new BasicNameValuePair("bookname",share.book_name));
        gets.add(new BasicNameValuePair("isbn",share.isbn));
        gets.add(new BasicNameValuePair("phone",share.phone));
        gets.add(new BasicNameValuePair("qq",share.qq));
        gets.add(new BasicNameValuePair("intro",share.intro));


        String get = URLEncodedUtils.format(gets, "UTF-8");
//        HttpGet getmethod = new HttpGet(TSLLURL.search + '?' + get);


        try {
            //得到HttpClient对象
            HttpClient getClient = new DefaultHttpClient();
            //得到HttpGet对象
            HttpGet request = new HttpGet(url + "?" +
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
    public BOOK_SHARE getShare()
    {
        return share;
    }

}
