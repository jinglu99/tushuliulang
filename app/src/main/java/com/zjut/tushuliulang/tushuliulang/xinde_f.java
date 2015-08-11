package com.zjut.tushuliulang.tushuliulang;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class xinde_f extends Fragment {

    private View v;
    private List<Map<String,Object>> list;
    private ListView listView;

    public xinde_f() {

        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_xinde_f, container, false);
        listView = (ListView) v.findViewById(R.id.xinde_lisview);

        list = initlist();
        listView.setAdapter(new listadapter_xinde(getActivity(),list));
        // Inflate the layout for this fragment
        return v;
    }

    private List<Map<String, Object>> initlist() {

        List<Map<String,Object>> l = new ArrayList<Map<String, Object>>();
        for(int n = 0;n<10; n++)
        {
           Map<String,Object> m = new HashMap<String,Object>();
            m.put("i","asdf");
            m.put("d",R.drawable.abc_ab_share_pack_mtrl_alpha);
            l.add(m);
        }
        return l;
    }


}
