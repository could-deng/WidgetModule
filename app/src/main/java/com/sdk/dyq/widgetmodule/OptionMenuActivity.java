package com.sdk.dyq.widgetmodule;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

/**
 * BaseActivity
 */

public class OptionMenuActivity extends AppCompatActivity {
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent();
        switch (item.getItemId()){
            case R.id.MainActivity:
                intent.setClass(OptionMenuActivity.this,MainActivity.class);
                startActivity(intent);
                break;
            case R.id.ProgressTestActivity:
                intent.setClass(OptionMenuActivity.this,ProgressTestActivity.class);
                startActivity(intent);
                break;
            case R.id.WaveViewActivity:
                intent.setClass(OptionMenuActivity.this,WaveViewActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }
}
