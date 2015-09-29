package com.zjut.tushuliulang.tushuliulang.net;

import android.os.Environment;
import android.util.Log;

import com.zjut.tushuliulang.tushuliulang.backoperate.SaveToFile;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Ben on 2015/7/28.
 */
public class login {
    String url = TSLLURL.login;
    String PICurl = "http://120.24.242.211:80/tushu/pic/";
    String username;
    String password;
    STU_INFO stu_info;
    String r="";
    InputStream is = null;
    String tmp="";
    String numenter = null;


    public login() {
        stu_info = new STU_INFO();
    }

    public login(String username, String password) {
        this.password = password;
        this.username = username;
        stu_info = new STU_INFO();
    }


    public void auto_login()
    {
        File file = new File(Environment.getExternalStorageDirectory().getPath()+"/tushuliulang/info.txt");
        if(file.exists())
        {

        }
    }
    public String fetch() {
        connect();
        regexp();
        return numenter;
    }

   private void connect() {
        List<BasicNameValuePair> gets = new LinkedList<>();
        gets.add(new BasicNameValuePair("username", username));
        gets.add(new BasicNameValuePair("password", password));

        String get = URLEncodedUtils.format(gets, "UTF-8");
//        HttpGet getmethod = new HttpGet(url + '?' + get);


        try {
            //得到HttpClient对象
            HttpClient getClient = new DefaultHttpClient();
            //得到HttpGet对象
            HttpPost request = new HttpPost(url);
            //客户端使用GET方式执行请教，获得服务器端的回应response
            UrlEncodedFormEntity post = new UrlEncodedFormEntity(gets);
            request.setEntity(post);
            HttpResponse response = getClient.execute(request);
            //判断请求是否成功
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                Log.i("tag", "请求服务器端成功");
                //获得输入流
                HttpEntity entity = response.getEntity();
                is = entity.getContent();

                //账号密码错误
                Log.e("MY", "账号密码错误");
                numenter = "账号密码错误";

                //关闭输入流

            } else {
                Log.i("tag", "请求服务器端失败");
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e("网络错误", "wrong");
            //网络错误
            numenter = "网络错误";
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
    private STU_INFO regexp()
    {
       Pattern p = Pattern.compile("<login>true</login>");
       Matcher m = p.matcher(tmp);
        if(m.find())
        {

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

                pattern = Pattern.compile("<major>(.*)</major>");
                matcher = pattern.matcher(tmp);
                if(matcher.find())
                    stu_info.major = matcher.group(1);


                stu_info.Id = username;


            SaveToFile s = new SaveToFile(Environment.getExternalStorageDirectory().getPath()+"/tushuliulang/data/info.db",
                    tmp);
            s.save();

                    numenter = "登陆成功";
                   return stu_info;


        }

            return new STU_INFO();

    }
    public STU_INFO returns()
    {
        return stu_info;
    }



}
