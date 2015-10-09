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
 * Created by Ben on 2015/8/11.
 */
public class Search
{
    private String[] searcharray;
    private String search="";
    private String result="";
    private BOOK_INFO[] book;
    private boolean founded;


    public Search(String search)
    {
        searcharray=search.split(" ");
        for(String a : searcharray)
        {
            if(a.equals(""))
                continue;
            if (this.search.equals(""))
                this.search = a;
            else
                this.search+=("0tsll0"+a);
        }
    }
    public Boolean fetch() {
        connect();
        RegExp();
        return founded;
    }



    public void connect() {
        List<BasicNameValuePair> gets = new LinkedList<>();
        gets.add(new BasicNameValuePair("s", search));


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




            result = sb.toString();


            is.close();
        } catch (Exception e) {
//                    return "Fail to convert net stream!";
        }


    }
    private void RegExp() {

        Pattern pattern_founded =  Pattern.compile("<founded>(.*)</founded>");
        Matcher match_founded = pattern_founded.matcher(result);
        if(match_founded.find()) {
            int n = Integer.parseInt(match_founded.group(1));
            if (n == 0) {
                founded = false;
                return;
            } else {
                founded = true;
            }

            book = new BOOK_INFO[n];
            Pattern pattern_book = Pattern.compile("<book>([\\s\\S]*?)</book>");
            Matcher match_book = pattern_book.matcher(result);
            n = 0;
            while (match_book.find()) {
                String book_info = match_book.group(1);

                book[n] = new BOOK_INFO();


                Pattern p_name = Pattern.compile("<name>([\\s\\S]*)</name>");
                Matcher m_name = p_name.matcher(book_info);
                if (m_name.find()) {
                    book[n].name = m_name.group(1);
                }

                Pattern p_press = Pattern.compile("<press>([\\s\\S]*)</press>");
                Matcher m_press = p_press.matcher(book_info);
                if (m_press.find()) {
                    book[n].press = m_press.group(1);
                }

                Pattern p_code = Pattern.compile("<code>([\\s\\S]*)</code>");
                Matcher m_code = p_code.matcher(book_info);
                if (m_code.find())
                    book[n].code = m_code.group(1);


                Pattern p_intro = Pattern.compile("<intro>([\\s\\S]*)</intro>");
                Matcher m_intro = p_intro.matcher(book_info);
                if (m_intro.find())
                    book[n].intro = m_intro.group(1);

                n++;

            }
        }
        else
        {
            founded =false;
        }

    }
    public BOOK_INFO[] returnresult()
    {
        return book;
    }
}
