package com.zjut.tushuliulang.tushuliulang.net;

import android.graphics.Bitmap;

/**
 * Created by Ben on 2015/8/11.
 */
public class BOOK_INFO {

    public simple simpleinfo;
    public BOOK_INFO()
    {
        simpleinfo = new simple();
        int n =0;
    }

    public class simple
    {

        public String name="";
        public String press="";
        public String code="";
        public String intro ="";
        public Bitmap bitmap = null;
    }
}
