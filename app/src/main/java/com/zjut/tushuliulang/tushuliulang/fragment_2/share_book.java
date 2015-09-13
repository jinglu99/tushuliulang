package com.zjut.tushuliulang.tushuliulang.fragment_2;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zjut.tushuliulang.tushuliulang.R;
import com.zjut.tushuliulang.tushuliulang.listadapter_share;
import com.zjut.tushuliulang.tushuliulang.net.BOOK_SHARE;
import com.zjut.tushuliulang.tushuliulang.net.getbookshares;
import com.zjut.tushuliulang.tushuliulang.widget.book_view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class share_book extends Fragment {
    private List<Map<String,Object>> list;
    private ListView listView;
    private boolean istop;
    private boolean isbotton = false;
    private boolean addedfooterlish = false;
    private SwipeRefreshLayout s;
    private BOOK_SHARE[] shares;
    private getbookshares getshares;

    private LinearLayout add_more;
    private View listfooter;

    private int DownY;
    private int LastY;

    private int mListViewFirstItem = 0;
    //listView中第一项的在屏幕中的位置
    private int mScreenY = 0;
    //是否向上滚动
    private boolean mIsScrollToUp = false;

    public share_book() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_share_book, container, false);
        s = (SwipeRefreshLayout) getActivity().findViewById(R.id.share_swipe);
        listView = (ListView) view.findViewById(R.id.share_book_listview);

        listfooter = inflater.inflate(R.layout.no_more_view,null,false);

        add_more = (LinearLayout) view.findViewById(R.id.add_more);
//        list=initlist();
//        listView.setAdapter(new listadapter_share(getActivity(),list));
        // Inflate the layout for this fragment
        istopped();

//        listView.addFooterView(listfooter);
        new getshare().execute();
        return view;
    }


    public boolean istopped()
    {
//        boolean b = false;
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (firstVisibleItem == 0) {
                    Log.e("s", "top");
                    istop = true;
                    s.setEnabled(true);
                } else {
                    s.setEnabled(false);
                }
                if (firstVisibleItem + visibleItemCount == totalItemCount) {
                    Log.e("s", "bottom");
                    istop = false;
                    isbotton = true;

                    //底部 加载更多TE

                } else {
                    isbotton = false;
                }
            }

        });




        return istop;
    }


    class getshare extends AsyncTask<String,String,String>
    {
        List<Map<String, Object>> l = new ArrayList<Map<String, Object>>();
        @Override
        protected String doInBackground(String... params) {

            getshares = new getbookshares();


            if(getshares.fetch()) {
                shares = getshares.getShares();

                for (int n = 0; n < shares.length; n++) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    if (n!=shares.length) {
                        map.put("a", shares[n].book_name);
                        map.put("b", shares[n].intro);
                        map.put("i", R.drawable.abc_ab_share_pack_mtrl_alpha);
                        map.put("type","0");
                    }
                    else
                    {
                        map.put("a", "null");
                        map.put("b", "null");
                        map.put("i", R.drawable.abc_ab_share_pack_mtrl_alpha);
                        map.put("type","1");
                    }

                    l.add(map);


                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            listView.setAdapter(new listadapter_share(getActivity(),l));


//            book_view book = new book_view(getActivity());
//            for(int n=0;n<shares.length;n++)
//            {
//                book_view books = new book_view(getActivity());
//                books.setContent(shares[n]);
//                list.addView(books);
//            }
        }
    }

}



