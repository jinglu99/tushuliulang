package com.zjut.tushuliulang.tushuliulang.net;

/**
 * Created by Ben on 2015/8/11.
 */
public class BOOK_INFO {
    public simple simpleinfo;
    public BOOK_INFO()
    {
        simpleinfo = new simple();

    }
     class simple
    {
        public String name="";
        public String press="";
        public String code="";
        public String intro ="";
    }

}
