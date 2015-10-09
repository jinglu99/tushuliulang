package com.zjut.tushuliulang.tushuliulang.fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjut.tushuliulang.tushuliulang.R;
import com.zjut.tushuliulang.tushuliulang.backoperate.GetInfoFromFile;
import com.zjut.tushuliulang.tushuliulang.net.Change_Info;
import com.zjut.tushuliulang.tushuliulang.net.STU_INFO;
import com.zjut.tushuliulang.tushuliulang.net.Search;
import com.zjut.tushuliulang.tushuliulang.net.login;
import com.zjut.tushuliulang.tushuliulang.collection.net.*;
import com.zjut.tushuliulang.tushuliulang.widget.*;


/**
 * A simple {@link Fragment} subclass.
 */
public class mycollection_f extends Fragment {

    String s="no";
    private TextView textView;
    private LinearLayout layout;
    private SwipeRefreshLayout refreshLayout;

    public mycollection_f() {
        int a;
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_mycollection_f, container, false);
        layout = (LinearLayout) v.findViewById(R.id.collection_layout);
        refreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.refresh_collection_f);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                layout.removeAllViews();
                new getcollections().execute();
            }
        });

        new getcollections().execute();
        return v;
    }
    class getcollections extends AsyncTask<String,String,String>
    {
        private getCollection getcollection;
        private COLLECTION_INFO[] collectionInfo;
        @Override
        protected String doInBackground(String... params) {

            getcollection = new getCollection(GetInfoFromFile.getinfo().Id);
            if (getcollection.fetch())
            {
                collectionInfo = getcollection.getCollections();
            }
             return "";
        }

        @Override
        protected void onPostExecute(String s) {
            boolean f = false;
            if (collectionInfo!=null) {
                f = collectionInfo.length % 2 != 0;


            for(int n = 0 ; n < collectionInfo.length/2;n++)
            {

                    collection_widget view = new collection_widget(getActivity());
                    view.setcontent(Integer.parseInt(collectionInfo[n*2].k),collectionInfo[n*2].code,collectionInfo[n*2].name,
                            Integer.parseInt(collectionInfo[n*2+1].k),collectionInfo[n*2+1].code,collectionInfo[n*2+1].name );
                layout.setGravity(Gravity.NO_GRAVITY);
                layout.addView(view);

            }

            if (f)
            {
                collection_widget view = new collection_widget(getActivity());
                view.setcontent(Integer.parseInt(collectionInfo[collectionInfo.length-1].k),collectionInfo[collectionInfo.length-1].code,collectionInfo[collectionInfo.length-1].name,
                      3,"",null );
                layout.setGravity(Gravity.NO_GRAVITY);
                layout.addView(view);
            }
            }
            refreshLayout.setRefreshing(false);
            super.onPostExecute(s);

        }
    }

}
