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
import com.zjut.tushuliulang.tushuliulang.activities.Book_share_info;
import com.zjut.tushuliulang.tushuliulang.backoperate.GetInfoFromFile;
import com.zjut.tushuliulang.tushuliulang.library.net.BORROW_INFO;
import com.zjut.tushuliulang.tushuliulang.library.net.getBorrowInfo;
import com.zjut.tushuliulang.tushuliulang.bookshare.*;
import com.zjut.tushuliulang.tushuliulang.listadapter_share;
import com.zjut.tushuliulang.tushuliulang.net.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class borrow_f2 extends Fragment {

    private SwipeRefreshLayout refreshLayout ;
    private ListView listView;
    private View view;

    private BORROW_INFO[] borrow_infos;
    public borrow_f2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_borrow_f2, container, false);
        initwidget();

        new getinfo().execute();
        return view;
    }

    private void initwidget() {
        listView = (ListView) view.findViewById(R.id.borrow_f2_lv);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.borrow_f2_refresh);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (borrow_infos[position].share==true)
                {
                    Intent intent = new Intent(getActivity(),book_info_library.class);
                    intent.putExtra("type","share");
                    intent.putExtra("code",borrow_infos[position].shareid);
                    intent.putExtra("codeincode","");

                    startActivity(intent);
                }
                else
                {
                    Intent intent = new Intent(getActivity(),book_info_library.class);
                    intent.putExtra("type","notshare");
                    intent.putExtra("code",borrow_infos[position].code);
                    intent.putExtra("codeincode",borrow_infos[position].codeincode);

                    startActivity(intent);
                }
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
        private getBorrowInfo getborrowinfo;
        private boolean result = false;


        List<Map<String, Object>> l = new ArrayList<Map<String, Object>>();
        @Override
        protected String doInBackground(String... params) {
            getborrowinfo = new getBorrowInfo(GetInfoFromFile.getinfo().Id);
            if (getborrowinfo.fetch())
            {
                borrow_infos = getborrowinfo.getCollections();
                if(borrow_infos!=null)
                {
                    for(int n =0 ;n<borrow_infos.length;n++)
                    {
                        if(borrow_infos[n].share==true)
                        {
                            getBookShareInfo bookShareInfo = new getBookShareInfo(borrow_infos[n].shareid);
                            if (bookShareInfo.fetch().equals("true"))
                            {
                                borrow_infos[n].name = bookShareInfo.getShareinfo().book_name;
                                borrow_infos[n].intro = bookShareInfo.getShareinfo().intro;

                                Map<String, Object> map = new HashMap<String, Object>();
                                map.put("a", borrow_infos[n].name);
                                map.put("b", borrow_infos[n].intro);
                                map.put("i", borrow_infos[n].shareid);
                                map.put("type","share");
                                l.add(map);
                            }


                        }
                        else {
                            getBookInfo bookInfo = new getBookInfo(borrow_infos[n].code);
                            if (bookInfo.fetch())
                            {
                                borrow_infos[n].name = bookInfo.getShareinfo().name;
                                borrow_infos[n].intro = bookInfo.getShareinfo().intro;

                                Map<String, Object> map = new HashMap<String, Object>();
                                map.put("a", borrow_infos[n].name);
                                map.put("b", borrow_infos[n].intro);
                                map.put("i", borrow_infos[n].shareid);
                                map.put("type","notshare");
                                l.add(map);
                            }
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
