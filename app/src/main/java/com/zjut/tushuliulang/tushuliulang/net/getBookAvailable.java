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
 * Created by Ben on 2015/10/8.
 */
public class getBookAvailable
{
    private String tmp = "";
    private String url = TSLLURL.getbookavailable;
    private InputStream is;
    private boolean result = false;

    private String code = "";
    private BOOK_SHARE[] book_shares;

    public getBookAvailable(String code)
    {
        this.code = code;
    }

    public boolean fetch()
    {
        connect();
        regexp();

        return result;
    }

    private void connect() {
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

    private void regexp() {
        Pattern pattern_total = Pattern.compile("<found>([\\s\\S]*)</found>");
        Matcher matcher_total = pattern_total.matcher(tmp);
        if (matcher_total.find())
        {
            int n = Integer.parseInt(matcher_total.group(1));
            book_shares = new BOOK_SHARE[n];
            n=0;

            Pattern pattern_bookshare = Pattern.compile("<book>([\\s\\S]*?)</book>");
            Matcher matcher_bookshare = pattern_bookshare.matcher(tmp);
            while(matcher_bookshare.find())
            {
                book_shares[n] = new BOOK_SHARE();

                String str = matcher_bookshare.group(1);

                Pattern pattern_owner = Pattern.compile("<owner>(.*)</owner>");
                Matcher matcher_owner = pattern_owner.matcher(str);
                matcher_owner.find();
                book_shares[n].owner = matcher_owner.group(1);

                Pattern pattern_order = Pattern.compile("<shareid>(.*)</shareid>");
                Matcher matcher_order = pattern_order.matcher(str);
                matcher_order.find();
                book_shares[n].number_order = matcher_order.group(1);



                Pattern pattern_codeincode = Pattern.compile("<codeincode>(.*)</codeincode>");
                Matcher matcher_codeincode = pattern_codeincode.matcher(str);
                matcher_codeincode.find();
                book_shares[n].intro = matcher_codeincode.group(1);

                result = true;
                n++;
            }

        }

    }

    public BOOK_SHARE[] getBook_shares()
    {
        return book_shares;
    }
}
