package com.zjut.tushuliulang.tushuliulang.net;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;

/**
 * Created by Ben on 2015/8/7.
 */
public class SaveToFile
{

    private String  dir;
    private InputStream inputStream;
    private String string;

    public SaveToFile(String dir,String string,InputStream inputStream)
    {
        this.dir = dir;
        this.inputStream = inputStream;
        this.string = string;
    }

    public void save()
    {
        try {
            File f = new File(dir);
            f.createNewFile();
            FileOutputStream file = new FileOutputStream(f);
            byte b[] = new byte[1024];
            while(inputStream.read(b)!=-1)
            {
                file.write(b);
            }
//            file.write(string.getBytes());
            file.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
