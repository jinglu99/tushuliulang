package com.zjut.tushuliulang.tushuliulang.backoperate;

import android.os.Environment;

import java.io.File;

/**
 * Created by Ben on 2015/8/24.
 */
public class createDirectory {
    public boolean create()  {
        String rootdir = Environment.getExternalStorageDirectory().toString() + "/tushuliulang/";
        String datadir = rootdir+"data/";
        String picdir = rootdir+"image/";
        String collectiondir = rootdir+"favorite/";

        File root = new File(rootdir);
        File data = new File(datadir);
        File pic = new File(picdir);
        File collection = new File(collectiondir);

        if (!root.exists())
        {
            root.mkdir();
        }
        if(!data.exists())
        {
            data.mkdir();
        }
        if(!pic.exists())
        {
            pic.mkdir();

        }
        if (!collection.exists())
        {
            collection.mkdir();
        }

        return true;
    }

}
