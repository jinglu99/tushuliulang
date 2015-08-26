package com.zjut.tushuliulang.tushuliulang.backoperate;

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

    private String string;

    public SaveToFile(String dir,String string)
    {
        this.dir = dir;

        this.string = string;
    }

    public void save()
    {
        try {
            File f = new File(dir);
            f.createNewFile();
            FileOutputStream file = new FileOutputStream(f);

            file.write(string.getBytes());
            file.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
