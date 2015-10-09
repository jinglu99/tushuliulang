package com.zjut.tushuliulang.tushuliulang.net;

import android.util.Log;

import com.zjut.tushuliulang.tushuliulang.bookshare.BOOK_SHARE;

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
 * Created by Ben on 2015/10/6.
 */
public class getBookInfo
{
    private String url = TSLLURL.getBookinfo;
    private InputStream is;
    private String tmp = "";
    private String code;
    private boolean result = false;
    private BOOK_INFO book_info;

    public getBookInfo(String code)
    {
        this.code = code;
    }
    public boolean fetch()
    {
        conect();
        regexp();

        return result;
    }

    private void conect() {
        List<BasicNameValuePair> gets = new LinkedList<>();
        gets.add(new BasicNameValuePair("code",code));

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

    private void regexp()
    {
        Pattern pattern_result = Pattern.compile("<found>(.*)</found>");
        Matcher matcher_result = pattern_result.matcher(tmp);
        if (matcher_result.find())
        {

            String r = matcher_result.group(1);
            if (r.equals("true")) {

                book_info = new BOOK_INFO();

                Pattern pattern_name = Pattern.compile("<name>(.*)</name>");
                Matcher matcher_name = pattern_name.matcher(tmp);
                matcher_name.find();
                book_info.name = matcher_name.group(1);


                Pattern pattern_intro = Pattern.compile("<intro>(.*)</intro>");
                Matcher matcher_intro = pattern_intro.matcher(tmp);
                matcher_intro.find();
                book_info.intro = matcher_intro.group(1);

                result = true;

            }
            else
            {
                result = false;
            }
        }
    }

    public BOOK_INFO getShareinfo()
    {
        return book_info;
    }
}
