package com.zjut.tushuliulang.tushuliulang.fragment_2;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zjut.tushuliulang.tushuliulang.R;
import com.zjut.tushuliulang.tushuliulang.listadapter_comment;
import com.zjut.tushuliulang.tushuliulang.net.*;
import com.zjut.tushuliulang.tushuliulang.backoperate.*;
import com.zjut.tushuliulang.tushuliulang.bookshare.getbooksharecomment;
import com.zjut.tushuliulang.tushuliulang.bookshare.upload_book_share_comment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class book_share_comment extends Fragment implements View.OnClickListener,SwipeRefreshLayout.OnRefreshListener {
    private View v;

    private ListView comment_lv;
    private EditText ed;
    private Button upload;
    private FrameLayout layout;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView  tv;

    private List<Map<String,Object>> list;

    private COMMENT[] comments;

    private Intent intent;
    private String order="";
    COMMENT comment;
    public book_share_comment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_book_share_comment, container, false);

        intent = getActivity().getIntent();
        order = intent.getStringExtra("order");

        comment_lv = (ListView) v.findViewById(R.id.book_share_info_comment_listview);
        ed = (EditText) v.findViewById(R.id.book_share_info_comment_ed);
        upload = (Button) v.findViewById(R.id.book_share_info_comment_upload_bt);
        upload.setOnClickListener(this);
        layout = (FrameLayout) v.findViewById(R.id.book_share_comment_layout);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.book_share_comment_swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(this);

        tv = new TextView(getActivity());
        tv.setText("暂无评论");
        tv.setGravity(Gravity.CENTER);
        layout.addView(tv);

        new get().execute();

        return v;
    }

    @Override
    public void onClick(View v) {
         comment= new COMMENT();
        comment.shareid = order;
        comment.stuid = GetInfoFromFile.getinfo().Id;
        comment.comment = ed.getText().toString();
        if (comment.comment.equals(""))
        {
            return;
        }


        new upload().execute();
    }

    @Override
    public void onRefresh() {
        new get().execute();
    }

    class get extends AsyncTask<String,String,String>
    {

        private getbooksharecomment getcomments;
        private GetStuInfo getStuInfo;
        private boolean isget = false;
        private List<Map<String,Object>> l;

        @Override
        protected String doInBackground(String... params) {
            getcomments = new getbooksharecomment(order,0);
            if (getcomments.fetch())
            {
                comments = getcomments.getComments();
                isget = true;



                l= new ArrayList<Map<String, Object>>();
                for(int n = 0;n<comments.length; n++)
                {
                    String name = comments[n].stuid;

                    getStuInfo = new GetStuInfo(comments[n].stuid);
                    if (getStuInfo.fetch())
                    {
                        name = getStuInfo.getStu_info().UserName;
                    }

                    Map<String,Object> m = new HashMap<String,Object>();
                    m.put("stuid",comments[n].stuid);
                    m.put("username",name);
                    m.put("comment",comments[n].comment);
                    m.put("date",comments[n].date);

                    l.add(m);
                }
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            if (isget)
            {
                tv.setVisibility(View.INVISIBLE);
                comment_lv.setAdapter(new listadapter_comment(getActivity(), l));
            }
            else
            {
                tv.setVisibility(View.VISIBLE);
            }
            swipeRefreshLayout.setRefreshing(false);


        }
    }

    class upload extends AsyncTask<String,String,String>
    {
        private upload_book_share_comment upload;
        boolean isupload = false;
        @Override
        protected String doInBackground(String... params) {

            upload = new upload_book_share_comment(comment,0);
            if (upload.fetch())
            {
                isupload = true;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (isupload)
            {
                ed.setText("");
                new get().execute();
            }
        }
    }
}
