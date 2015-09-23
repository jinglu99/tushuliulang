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
public class getbookshares
{
    private String tmp = "";
    private String url = TSLLURL.getbookshares;
    private InputStream is;
    private BOOK_SHARE[] shares;
    private boolean result = false;
    private boolean parameter = false;
    private String input;
    private String next="";

    public getbookshares()
    {
        parameter = false;
    }
    public getbookshares(String input)
    {
        parameter = true;
        this.input = input;
    }

    public boolean fetch()
    {
        connect();
        regexp();

        return result;
    }

    private void connect() {
        List<BasicNameValuePair> gets = new LinkedList<>();
       if (parameter)
       {
           gets.add(new BasicNameValuePair("order",input));
       }



        String get = URLEncodedUtils.format(gets, "UTF-8");
//        HttpGet getmethod = new HttpGet(TSLLURL.search + '?' + get);


        try {
            //得到HttpClient对象
            HttpClient getClient = new DefaultHttpClient();
            //得到HttpGet对象
            HttpGet request;
           if (parameter)
           {
                request = new HttpGet(url + "?" +
                       get);
           }
            else
           {
                request = new HttpGet(url);
           }
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
        Pattern pattern_total = Pattern.compile("<total>([\\s\\S]*)</total>");
        Matcher matcher_total = pattern_total.matcher(tmp);
        if (matcher_total.find())
        {
            int n = Integer.parseInt(matcher_total.group(1));
            shares = new BOOK_SHARE[n];
            n=0;

            Pattern pattern_bookshare = Pattern.compile("<bookshare>([\\s\\S]*?)</bookshare>");
            Matcher matcher_bookshare = pattern_bookshare.matcher(tmp);
            while(matcher_bookshare.find())
            {
                shares[n] = new BOOK_SHARE();

                String str = matcher_bookshare.group(1);

                Pattern pattern_owner = Pattern.compile("<owner>(.*)</owner>");
                Matcher matcher_owner = pattern_owner.matcher(str);
                matcher_owner.find();
                shares[n].owner = matcher_owner.group(1);

                Pattern pattern_bookname = Pattern.compile("<bookname>(.*)</bookname>");
                Matcher matcher_bookname = pattern_bookname.matcher(str);
                matcher_bookname.find();
                shares[n].book_name = matcher_bookname.group(1);

                Pattern pattern_order = Pattern.compile("<numberorder>(.*)</numberorder>");
                Matcher matcher_order = pattern_order.matcher(str);
                matcher_order.find();
                shares[n].number_order = matcher_order.group(1);

                Pattern pattern_date = Pattern.compile("<date>(.*)</date>");
                Matcher matcher_date = pattern_date.matcher(str);
                matcher_date.find();
                shares[n].date = matcher_owner.group(1);

                Pattern pattern_intro = Pattern.compile("<intro>(.*)</intro>");
                Matcher matcher_intro = pattern_intro.matcher(str);
                matcher_intro.find();
                shares[n].intro = matcher_intro.group(1);

//                getImagefromNet getImagefromNet = new getImagefromNet(TSLLURL.bookshareimg+shares[n].number_order+".jpg");
//                shares[n].bitmap = getImagefromNet.image();

                result = true;
                n++;
            }
            Pattern pattern_next = Pattern.compile("<gets>(.*)</gets>");
            Matcher matcher_next = pattern_next.matcher(tmp);
            if (matcher_next.find())
            {
                next = matcher_next.group(1);
            }
        }


    }
    public BOOK_SHARE[] getShares()
    {
        return  shares;
    }
    public String getnext()
    {
        return next;
    }
}
