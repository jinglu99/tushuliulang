package com.zjut.tushuliulang.tushuliulang.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.zjut.tushuliulang.tushuliulang.R;

/**
 * Created by zz on 2015/9/25.
 */
public class changestuinfo_activity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changestu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //随便加了个MENU
        getMenuInflater().inflate(R.menu.menu_activity_questions_enter, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
