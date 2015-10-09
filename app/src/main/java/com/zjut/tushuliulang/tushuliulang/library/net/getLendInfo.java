package com.zjut.tushuliulang.tushuliulang.library.net;

import android.util.Log;

import com.zjut.tushuliulang.tushuliulang.bookshare.getBookShareInfo;
import com.zjut.tushuliulang.tushuliulang.collection.net.COLLECTION_INFO;
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
 * Created by Ben on 2015/10/6.
 */
public class getLendInfo
{
    private String tmp = "";
    private InputStream is;
    private boolean result;
    private String url = TSLLURL.getlendinfo;

    private String stuid = "";
    private LENDINFO[] lendinfos;

    public getLendInfo(String stuid)
    {
        this.stuid = stuid;
    }
    public boolean fetch()
    {
        connect();
        regexp();

        return result;
    }

    private void connect() {
        List<BasicNameValuePair> gets = new LinkedList<>();
        gets.add(new BasicNameValuePair("stuid", stuid));

        String get = URLEncodedUtils.format(gets, "UTF-8");
//        HttpGet getmethod = new HttpGet(url + '?' + get);

        InputStream is = null;

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




            tmp = sb.toString();


            is.close();
        } catch (Exception e) {
//                    return "Fail to convert net stream!";
        }
    }

    private void regexp() {

        Pattern pattern_total = Pattern.compile("<found>([\\s\\S]*)</found>");
        Matcher matcher_total = pattern_total.matcher(tmp);
        if (matcher_total.find())
        {
            int n = Integer.parseInt(matcher_total.group(1));
            lendinfos = new LENDINFO[n];

            n=0;
            result = false;

            Pattern pattern_lend = Pattern.compile("<lend>([\\s\\S]*?)</lend>");
            Matcher matcher_lend = pattern_lend.matcher(tmp);
            while(matcher_lend.find())
            {
                lendinfos[n] = new LENDINFO();

                String str = matcher_lend.group(1);

                Pattern pattern_shareid = Pattern.compile("<shareid>(.*)</shareid>");
                Matcher matcher_shareid = pattern_shareid.matcher(str);
                matcher_shareid.find();
                lendinfos[n].shareid = matcher_shareid.group(1);

                Pattern pattern_lender = Pattern.compile("<lender>(.*)</lender>");
                Matcher matcher_lender = pattern_lender.matcher(str);
                matcher_lender.find();
                lendinfos[n].lender = matcher_lender.group(1);


                Pattern pattern_time= Pattern.compile("<time>(.*)</time>");
                Matcher matcher_time = pattern_time.matcher(str);
                matcher_time.find();
                lendinfos[n].time = matcher_time.group(1);


                result = true;
                n++;
            }

        }
    }
    public LENDINFO[] getLendinfos()
    {
        return lendinfos;
    }
}
