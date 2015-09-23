package com.zjut.tushuliulang.tushuliulang.net.bookshare;

import android.util.Log;

import com.zjut.tushuliulang.tushuliulang.net.TSLLURL;

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
 * Created by Ben on 2015/9/23.
 */
public class getBookShareInfo
{
    private String url = TSLLURL.getBookShareInfo;
    private InputStream is;
    private String tmp = "";
    private String order;
    private String result = "";
    private BOOK_SHARE share;

    public getBookShareInfo(String order)
    {
        this.order = order;
    }
    public String fetch()
    {
        conect();
        if (result.equals(""))
          regexp();

        return result;
    }

    private void conect() {
        List<BasicNameValuePair> gets = new LinkedList<>();
        gets.add(new BasicNameValuePair("shareid",order));

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
                result = "no_connection";
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
        Pattern pattern_result = Pattern.compile("<result>(.*)</result>");
        Matcher matcher_result = pattern_result.matcher(tmp);
        if (matcher_result.find())
        {

            String r = matcher_result.group(1);
            if (r.equals("true")) {

                share = new BOOK_SHARE();

                Pattern pattern_owner = Pattern.compile("<owner>(.*)</owner>");
                Matcher matcher_owner = pattern_owner.matcher(tmp);
                matcher_owner.find();
                share.owner = matcher_owner.group(1);

                Pattern pattern_bookname = Pattern.compile("<bookname>(.*)</bookname>");
                Matcher matcher_bookname = pattern_bookname.matcher(tmp);
                matcher_bookname.find();
                share.book_name = matcher_bookname.group(1);

                Pattern pattern_isbn = Pattern.compile("<isbn>(.*)</isbn>");
                Matcher matcher_isbn = pattern_isbn.matcher(tmp);
                matcher_isbn.find();
                share.isbn = matcher_isbn.group(1);

                Pattern pattern_code = Pattern.compile("<code>(.*)</code>");
                Matcher matcher_code = pattern_code.matcher(tmp);
                matcher_code.find();
                share.code = matcher_code.group(1);

                Pattern pattern_codeincode = Pattern.compile("<codeincode>(.*)</codeincode>");
                Matcher matcher_codeincode = pattern_codeincode.matcher(tmp);
                matcher_codeincode.find();
                share.code = matcher_codeincode.group(1);

                Pattern pattern_available = Pattern.compile("<available>(.*)</available>");
                Matcher matcher_available = pattern_available.matcher(tmp);
                matcher_available.find();
                share.available = matcher_available.group(1);

                Pattern pattern_phone = Pattern.compile("<phone>(.*)</phone>");
                Matcher matcher_phone = pattern_phone.matcher(tmp);
                matcher_phone.find();
                share.phone = matcher_phone.group(1);

                Pattern pattern_qq = Pattern.compile("<qq>(.*)</qq>");
                Matcher matcher_qq = pattern_qq.matcher(tmp);
                matcher_qq.find();
                share.qq = matcher_qq.group(1);


                Pattern pattern_date = Pattern.compile("<date>(.*)</date>");
                Matcher matcher_date = pattern_date.matcher(tmp);
                matcher_date.find();
                share.date = matcher_date.group(1);

                Pattern pattern_intro = Pattern.compile("<intro>(.*)</intro>");
                Matcher matcher_intro = pattern_intro.matcher(tmp);
                matcher_intro.find();
                share.intro = matcher_intro.group(1);

                result = "true";

                }
                else
                {
                    result = r;
                }
        }
    }

    public BOOK_SHARE getShareinfo()
    {
        return share;
    }
}









