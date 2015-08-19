package com.zjut.tushuliulang.tushuliulang.fragment_2;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.zjut.tushuliulang.tushuliulang.R;
import com.zjut.tushuliulang.tushuliulang.listadapter_share;

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
    private SwipeRefreshLayout s;
    public share_book() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_share_book, container, false);
        s = (SwipeRefreshLayout) getActivity().findViewById(R.id.share_swipe);
        listView = (ListView) view.findViewById(R.id.share_book_listview);
        list=initlist();
        listView.setAdapter(new listadapter_share(getActivity(),list));
        // Inflate the layout for this fragment
        istopped();
        return view;
    }

    private List<Map<String, Object>> initlist() {
        List<Map<String,Object>> l = new ArrayList<Map<String, Object>>();
        for(int n = 0 ; n<=10; n++)
        {
            Map<String,Object>  map = new HashMap<String,Object>();
            map.put("a","asdf");
            map.put("i",R.drawable.abc_ab_share_pack_mtrl_alpha);
            l.add(map);
        }
        return l;

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

                if(firstVisibleItem ==  0)
                {
                    Log.e("s","top");
                    istop = true;
                    s.setEnabled(true);
                }
                else
                {
                    s.setEnabled(false);
                }
                if(firstVisibleItem+visibleItemCount == totalItemCount)
                {
                    Log.e("s","bottom");
                    istop = false;
                    //底部 加载更多TE
                }
            }
        });
        return istop;
    }


}



