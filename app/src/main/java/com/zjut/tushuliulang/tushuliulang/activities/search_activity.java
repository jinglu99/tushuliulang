package com.zjut.tushuliulang.tushuliulang.activities;


//书籍搜索界面

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.zjut.tushuliulang.tushuliulang.R;
import com.zjut.tushuliulang.tushuliulang.net.BOOK_INFO;
import com.zjut.tushuliulang.tushuliulang.net.Search;
import com.zjut.tushuliulang.tushuliulang.widget.book_recommend_in_search_activity;
import com.zjut.tushuliulang.tushuliulang.widget.book_view;
import com.zjut.tushuliulang.tushuliulang.widget.can_not_find;

public class search_activity extends ActionBarActivity implements View.OnClickListener {

    LinearLayout search_layout;
    LinearLayout search_recommend;
    EditText search_edit;
    Button search_button;
    ActionBar actionBar;

    Context context;




    public search_activity() {
        context = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_activity);

        search_recommend = (LinearLayout) this.findViewById(R.id.search_recommemd);
        search_layout = (LinearLayout) this.findViewById(R.id.search_result);
        search_edit = (EditText) this.findViewById(R.id.search_edit);
        search_button = (Button) this.findViewById(R.id.search_button);



//书籍推荐ui显示
        for(int n = 0 ; n<3;n++)
        {
            book_recommend_in_search_activity book_recommend = new book_recommend_in_search_activity(this);
//            book_recommend.setcontent("",null,"",null);
            search_recommend.addView(book_recommend);
        }



        search_button.setOnClickListener(this);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.search_button) {

            if (!search_edit.getText().toString().equals("")) {
                new searchtask().execute(search_edit.getText().toString());
            }
        }

    }
    class searchtask extends AsyncTask<String,String,String>
    {
        BOOK_INFO[] books;
        Search search;
        boolean founded;
        @Override
        protected String doInBackground(String... params) {
           if(params[0].equals(""))
           {
               founded = false;
           }
            else
           {
               search = new Search(params[0]);
               founded = search.fetch();
           }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            search_layout.removeAllViews();
            search_layout.setBackgroundResource(R.drawable.dialog_background_mtrl_mult);
            if(founded)
            {

                books = search.returnresult();
                for (int n = 0; n < books.length; n++)
                {

                    book_view book = new book_view(context);
                    search_layout.setGravity(Gravity.NO_GRAVITY);
                    book.setContent(books[n]);
                    search_layout.addView(book);
                }
            }
            else
            {
                can_not_find view = new can_not_find(context);
                search_layout.addView(view);
            }
        }
    }
}
