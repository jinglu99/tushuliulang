package com.zjut.tushuliulang.tushuliulang.fragment;


import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zjut.tushuliulang.tushuliulang.MainActivity;
import com.zjut.tushuliulang.tushuliulang.R;
import com.zjut.tushuliulang.tushuliulang.net.Change_Info;
import com.zjut.tushuliulang.tushuliulang.net.STU_INFO;
import com.zjut.tushuliulang.tushuliulang.net.Search;
import com.zjut.tushuliulang.tushuliulang.net.login;


/**
 * A simple {@link Fragment} subclass.
 */
public class mycollection_f extends Fragment {

        String s="no";
        private TextView textView;
    //侧滑栏
    private static final String ARG_SECTION_NUMBER = "section_number";

    //侧滑栏
    public static Fragment newInstance(int sectionNumber) {
        share_f fragment = new share_f();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);

        return fragment;
    }

    //侧滑栏
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    //侧滑栏
    public mycollection_f() {
        int a;
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_mycollection_f, container, false);
        textView = (TextView) v.findViewById(R.id.testttt);

//        textView.setText(s);
         new threadnet().execute();
        // Inflate the layout for this fragment
        return v;
    }
    class threadnet extends AsyncTask<String,String,String>
    {
//        TextView textView1;
        login l;
        Change_Info c;
        String st="";
        STU_INFO stu_info;
        @Override
        protected String doInBackground(String... params) {

            Search s = new Search("数据");
            s.fetch();

             return "";
        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);

        }
    }

}
