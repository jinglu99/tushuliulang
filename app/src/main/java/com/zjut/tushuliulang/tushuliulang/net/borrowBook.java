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
 * Created by Ben on 2015/9/28.
 */
public class borrowBook
{
    private String tmp="";
    private InputStream is;
    private boolean result = false;
    private String error="";

    private String stuid;
    private String code;
    private String codeincode;
    private String shareid;
    private String date;

    public borrowBook(String stuid,String code,String codeincode,String shareid,String date)
    {
        this.stuid = stuid;
        this.code = code;
        this.codeincode = codeincode;
        this.shareid = shareid;
        this.date = date;
    }

    public boolean fetch()
    {
        connect();
        regexp();

        return result;
    }

    private void connect()
    {
        List<BasicNameValuePair> gets = new LinkedList<>();
        gets.add(new BasicNameValuePair("stuid", stuid));
        gets.add(new BasicNameValuePair("code",code));
        gets.add(new BasicNameValuePair("codeincode",codeincode));
        gets.add(new BasicNameValuePair("shareid",shareid));
        gets.add(new BasicNameValuePair("date",date));


        String get = URLEncodedUtils.format(gets, "UTF-8");
//        HttpGet getmethod = new HttpGet(url + '?' + get);

        InputStream is = null;

        try {
            //得到HttpClient对象
            HttpClient getClient = new DefaultHttpClient();


            //得到HttpGet对象
            HttpGet request = new HttpGet(TSLLURL.search + "?" +
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




            tmp = sb.toString();


            is.close();
        } catch (Exception e) {
//                    return "Fail to convert net stream!";
        }


    }

    private void regexp()
    {
        Pattern pattern_result = Pattern.compile("<result>(.*)</result>");
        Matcher matcher_result = pattern_result.matcher(tmp);
        if (matcher_result.find())
        {
            String r = matcher_result.group(1);
            if (r.equals("true"))
            {
                result = true;
            }
            else
            {
                result = false;
            }

            Pattern pattern_error = Pattern.compile("<error>(.*)</error>");
            Matcher matcher_error =  pattern_error.matcher(tmp);
            if (matcher_error.find())
            {
                error = matcher_error.group(1);
            }
        }
        else
        {
            result = false;
            error = "no_network_connection";
        }
    }

    public String getError()
    {
        return error;
    }
}
