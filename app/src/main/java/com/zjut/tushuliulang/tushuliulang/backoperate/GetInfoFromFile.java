package com.zjut.tushuliulang.tushuliulang.backoperate;

import android.app.AlertDialog;
import android.os.Environment;
import android.util.Log;

import com.zjut.tushuliulang.tushuliulang.net.STU_INFO;

import org.apache.http.util.EncodingUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Ben on 2015/8/24.
 */
public class GetInfoFromFile
{

        public static STU_INFO getinfo () {
        String tmp = null;
        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/tushuliulang/data/info.db");
            if (!file.exists())
            {
                return null;
            }
        try {
            FileInputStream inputStream = new FileInputStream(file);
            int length = inputStream.available();
            byte[] bytes = new byte[length];

            inputStream.read(bytes);

            tmp = EncodingUtils.getString(bytes, "utf-8");

            inputStream.close();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        STU_INFO stu_info = new STU_INFO();
        Pattern p = Pattern.compile("<login>true</login>");
        Matcher m = p.matcher(tmp);
        if (m.find()) {

            Pattern pattern;
            Matcher matcher;

            pattern = Pattern.compile("<name>(.*)</name>");
            matcher = pattern.matcher(tmp);
            if (matcher.find())
                stu_info.Name = matcher.group(1);

            pattern = Pattern.compile("<stu_id>(.*)</stu_id>");
            matcher = pattern.matcher(tmp);
            if (matcher.find())
                stu_info.Id = matcher.group(1);

            pattern = Pattern.compile("<username>(.*)</username>");
            matcher = pattern.matcher(tmp);
            if (matcher.find())
                stu_info.UserName = matcher.group(1);

            pattern = Pattern.compile("<college>(.*)</college>");
            matcher = pattern.matcher(tmp);
            if (matcher.find())
                stu_info.college = matcher.group(1);

            pattern = Pattern.compile("<class>(.*)</class>");
            matcher = pattern.matcher(tmp);
            if (matcher.find())
                stu_info.Class = matcher.group(1);

            pattern = Pattern.compile("<grade>(.*)</grade>");
            matcher = pattern.matcher(tmp);
            if (matcher.find())
                stu_info.Grade = matcher.group(1);

            pattern = Pattern.compile("<motto>(.*)</motto>");
            matcher = pattern.matcher(tmp);
            if (matcher.find())
                stu_info.Motto = matcher.group(1);

            pattern = Pattern.compile("<phone>(.*)</phone>");
            matcher = pattern.matcher(tmp);
            if (matcher.find())
                stu_info.Phone = matcher.group(1);

            pattern = Pattern.compile("<email>(.*)</email>");
            matcher = pattern.matcher(tmp);
            if (matcher.find())
                stu_info.Email = matcher.group(1);

            pattern = Pattern.compile("<sex>(.*)</sex>");
            matcher = pattern.matcher(tmp);
            if (matcher.find())
                stu_info.Sex = matcher.group(1);

            pattern = Pattern.compile("<major>(.*)</major>");
            matcher = pattern.matcher(tmp);
            if(matcher.find())
                stu_info.major = matcher.group(1);



        }

        return stu_info;
    }


    public static STU_INFO getlogininfo()
    {
        STU_INFO stu_info = new STU_INFO();
        String tmp = null;
        File file = new File(Environment.getExternalStorageDirectory().getPath()+"/tushuliulang/data/mem.db");
        if (file.exists()) {
            try {
                FileInputStream inputStream = new FileInputStream(file);
                int length = inputStream.available();
                byte[] bytes = new byte[length];

                inputStream.read(bytes);

                tmp = EncodingUtils.getString(bytes, "utf-8");

                inputStream.close();


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            Pattern pattern;
            Matcher matcher;

            pattern = Pattern.compile("<username>(.*)</username>");
            matcher = pattern.matcher(tmp);
            if (matcher.find())
                stu_info.Id = matcher.group(1);

            pattern = Pattern.compile("<password>(.*)</password>");
            matcher = pattern.matcher(tmp);
            if (matcher.find())
                stu_info.password = matcher.group(1);


        }

        return stu_info;
    }


}
