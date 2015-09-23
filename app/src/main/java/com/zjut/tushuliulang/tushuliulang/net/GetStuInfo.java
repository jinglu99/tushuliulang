package com.zjut.tushuliulang.tushuliulang.net;

import android.os.Environment;
import android.util.Log;

import com.zjut.tushuliulang.tushuliulang.backoperate.SaveToFile;

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
public class GetStuInfo
{
    private String url = TSLLURL.getstuinfo;
    private InputStream is;
    private String tmp="";
    private STU_INFO stu_info;
    private String stu_id;
    private boolean result;
    public GetStuInfo(String stu_id)
    {
        this.stu_id = stu_id;
    }
    public boolean fetch()
    {
        connect();

        regexp();

        return result;
    }

    private void connect() {
        List<BasicNameValuePair> gets = new LinkedList<>();
        gets.add(new BasicNameValuePair("stuid", stu_id));


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


    private void regexp()
    {
        Pattern p = Pattern.compile("<result>true</result>");
        Matcher m = p.matcher(tmp);
        if(m.find())
        {
            stu_info = new STU_INFO();


            Pattern pattern ;
            Matcher matcher;

            pattern = Pattern.compile("<name>(.*)</name>");
            matcher = pattern.matcher(tmp);
            if(matcher.find())
                stu_info.Name = matcher.group(1);

            pattern = Pattern.compile("<username>(.*)</username>");
            matcher = pattern.matcher(tmp);
            if(matcher.find())
                stu_info.UserName = matcher.group(1);

            pattern = Pattern.compile("<college>(.*)</college>");
            matcher = pattern.matcher(tmp);
            if(matcher.find())
                stu_info.college = matcher.group(1);

            pattern = Pattern.compile("<class>(.*)</class>");
            matcher = pattern.matcher(tmp);
            if(matcher.find())
                stu_info.Class = matcher.group(1);

            pattern = Pattern.compile("<grade>(.*)</grade>");
            matcher = pattern.matcher(tmp);
            if(matcher.find())
                stu_info.Grade = matcher.group(1);

            pattern = Pattern.compile("<motto>(.*)</motto>");
            matcher = pattern.matcher(tmp);
            if(matcher.find())
                stu_info.Motto = matcher.group(1);

            pattern = Pattern.compile("<phone>(.*)</phone>");
            matcher = pattern.matcher(tmp);
            if(matcher.find())
                stu_info.Phone = matcher.group(1);

            pattern = Pattern.compile("<email>(.*)</email>");
            matcher = pattern.matcher(tmp);
            if(matcher.find())
                stu_info.Email = matcher.group(1);

            pattern = Pattern.compile("<sex>(.*)</sex>");
            matcher = pattern.matcher(tmp);
            if(matcher.find())
                stu_info.Sex = matcher.group(1);

//                pattern = Pattern.compile("<pic>(.*)</pic>");
//                matcher = pattern.matcher(tmp);
//                if(matcher.find())
//                    stu_info.image = new getImagefromNet(PICurl+"201419630314.jpg").image();


            stu_info.Id = stu_id;
            result = true;




        }
        else
           result = false;

    }

    public STU_INFO getStu_info()
    {
        return stu_info;
    }
}
