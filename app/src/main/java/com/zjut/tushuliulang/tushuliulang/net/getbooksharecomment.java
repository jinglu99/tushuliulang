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
public class getbooksharecomment {
    private String input = "";
    private String url = "";
    private boolean result;

    private String tmp ="";
    private InputStream is;

    private COMMENT[] comments;
    public getbooksharecomment(String input,int k)
    {
        this.input = input;

        switch (k)
        {
            case 0:
                url = TSLLURL.getBooksharecomment_book;
                break;
            case 1:
                url = TSLLURL.getBooksharecomment_ppt;
                break;
        }
    }
    public boolean fetch()
    {
        connect();
        regexp();

        return result;
    }

    private void connect() {
        List<BasicNameValuePair> gets = new LinkedList<>();
        gets.add(new BasicNameValuePair("shareid",input));

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

        Pattern pattern_founded = Pattern.compile("<founded>(.*)</founded>");
        Matcher matcher_founded = pattern_founded.matcher(tmp);
        if (matcher_founded.find())
        {
            int n = Integer.parseInt(matcher_founded.group(1));
            comments = new COMMENT[n];
            n=0;

            Pattern pattern_comments = Pattern.compile("<comments>([\\s\\S]*?)</comments>");
            Matcher matcher_comments = pattern_comments.matcher(tmp);
            while(matcher_comments.find())
            {
                String str = matcher_comments.group(1);
                comments[n] = new COMMENT();

                Pattern pattern_order = Pattern.compile("<order>(.*)</order>");
                Matcher matcher_order = pattern_order.matcher(str);
                matcher_order.find();
                comments[n].shareid = matcher_order.group(1);

                Pattern pattern_code = Pattern.compile("<code>(.*)</code>");
                Matcher matcher_code = pattern_code.matcher(str);
                matcher_code.find();
                comments[n].code = matcher_code.group(1);

                Pattern pattern_stuid = Pattern.compile("<stuid>(.*)</stuid>");
                Matcher matcher_stuid = pattern_stuid.matcher(str);
                matcher_stuid.find();
                comments[n].stuid = matcher_stuid.group(1);

                Pattern pattern_date = Pattern.compile("<date>(.*)</date>");
                Matcher matcher_date = pattern_date.matcher(str);
                matcher_date.find();
                comments[n].date = matcher_date.group(1);

                Pattern pattern_comment = Pattern.compile("<comment>(.*)</comment>");
                Matcher matcher_comment = pattern_comment.matcher(str);
                matcher_comment.find();
                comments[n].comment = matcher_comment.group(1);

                result = true;
                n++;

            }
        }
        else
        {
            result = false;
        }
    }
    public COMMENT[] getComments()
    {
        return comments;
    }
}
