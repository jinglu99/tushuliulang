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
 * Created by Ben on 2015/8/2.
 */
public class Change_Info {
    private String stu_id;
    private String password;
    private STU_INFO stu_info;
    private String error = "";



    private String posturl = TSLLURL.changeinfo;

    private InputStream is;
    private String result;

    public Change_Info(String stu_id, String password, STU_INFO stu_info) {
        this.stu_id = stu_id;
        this.password = password;
        this.stu_info = stu_info;
    }

    /* 上传文件至Server的方法 */
    public void uploadpic()
    {

        UploadFile uploadFile = new UploadFile(Environment.getExternalStorageDirectory()+
                "/tushuliulang/data/"+stu_id+".jpg"
                ,stu_id+".jpg",1);
        uploadFile.uploadFile();
        result = uploadFile.getResult();
    }

    public boolean Uploadinfo() {


        List<BasicNameValuePair> gets = new LinkedList<>();
        gets.add(new BasicNameValuePair("stuid", stu_id));
        gets.add(new BasicNameValuePair("password", password));
        gets.add(new BasicNameValuePair("name", stu_info.Name));
        gets.add(new BasicNameValuePair("username", stu_info.UserName));
        gets.add(new BasicNameValuePair("college", stu_info.college));
        gets.add(new BasicNameValuePair("class", stu_info.Class));
        gets.add(new BasicNameValuePair("grade", stu_info.Grade));
        gets.add(new BasicNameValuePair("motto", stu_info.Motto));
        gets.add(new BasicNameValuePair("phone", stu_info.Phone));
        gets.add(new BasicNameValuePair("email", stu_info.Email));
        gets.add(new BasicNameValuePair("sex", stu_info.Sex));



        String get = URLEncodedUtils.format(gets, "UTF-8");
//        HttpGet getmethod = new HttpGet(TSLLURL.search + '?' + get);


        try {
            //得到HttpClient对象
            HttpClient getClient = new DefaultHttpClient();
            //得到HttpGet对象
            HttpGet request = new HttpGet(posturl + "?" +
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

            result = sb.toString();




        } catch (Exception e) {
        }

        Pattern pattern_info = Pattern.compile("<result_info>(.*)</result_info>");
        Matcher matcher_ifno = pattern_info.matcher(result);
        if (matcher_ifno.find())
        {
            String r = matcher_ifno.group(1);
            if (r.equals("true"))
            {
                error = "no_error";
                return true;
            }
            else
            {
                error = "inputwrong";
            }
        }
        else
        {
            error = "no_network_connection";
        }
        return false;
    }

    public String getresult()
    {
        return result;
    }
    public String getError(){return error;}
}
