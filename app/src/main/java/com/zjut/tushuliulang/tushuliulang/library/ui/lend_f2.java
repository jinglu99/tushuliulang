package com.zjut.tushuliulang.tushuliulang.library.ui;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zjut.tushuliulang.tushuliulang.R;
import com.zjut.tushuliulang.tushuliulang.backoperate.GetInfoFromFile;
import com.zjut.tushuliulang.tushuliulang.bookshare.getBookShareInfo;
import com.zjut.tushuliulang.tushuliulang.library.net.BORROW_INFO;
import com.zjut.tushuliulang.tushuliulang.library.net.LENDINFO;
import com.zjut.tushuliulang.tushuliulang.library.net.getBorrowInfo;
import com.zjut.tushuliulang.tushuliulang.library.net.getLends;
import com.zjut.tushuliulang.tushuliulang.listadapter_share;
import com.zjut.tushuliulang.tushuliulang.net.getBookInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class lend_f2 extends Fragment {

    private SwipeRefreshLayout refreshLayout ;
    private ListView listView;
    private View view;

    private LENDINFO[] lendinfos;
    public lend_f2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_lend_f2, container, false);
        initwidget();

        new getinfo().execute();
        return view;
    }

    private void initwidget() {
        listView = (ListView) view.findViewById(R.id.lend_f2_lv);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.lend_f2_refresh);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Intent intent = new Intent(getActivity(),book_info_library.class);
                    intent.putExtra("type","share");
                    intent.putExtra("code",lendinfos[position].lender);

                    startActivity(intent);

            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new getinfo().execute();
            }
        });

    }

    class getinfo extends AsyncTask<String,String,String>
    {
        private getLends getlends;
        private boolean result = false;


        List<Map<String, Object>> l = new ArrayList<Map<String, Object>>();
        @Override
        protected String doInBackground(String... params) {
            getlends = new getLends(GetInfoFromFile.getinfo().Id);
            if (getlends.fetch())
            {
                lendinfos = getlends.getLendInfo();
                if(lendinfos!=null)
                {
                    for(int n =0 ;n<lendinfos.length;n++)
                    {

                            getBookShareInfo bookShareInfo = new getBookShareInfo(lendinfos[n].lender);
                            if (bookShareInfo.fetch().equals("true"))
                            {
                                lendinfos[n].name = bookShareInfo.getShareinfo().book_name;
                                lendinfos[n].intro = bookShareInfo.getShareinfo().intro;

                                Map<String, Object> map = new HashMap<String, Object>();
                                map.put("a", lendinfos[n].name);
                                map.put("b", lendinfos[n].intro);
                                map.put("i", lendinfos[n].lender);
                                map.put("type","share");
                                l.add(map);
                            }

                    }
                    result = true;
                }
            }
            else
            {
                result = false;
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            listView.setAdapter(new listadapter_share(getActivity(),l));

            refreshLayout.setRefreshing(false);

            super.onPostExecute(s);
        }
    }


}
