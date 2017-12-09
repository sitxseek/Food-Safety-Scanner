package itp341.guo.yangzong.foodsafetyscanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by yangzong on 11/12/17.
 */

public class CreateActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();
        Fragment f = fm.findFragmentById(R.id.fragment_container);

        Intent i = getIntent();
        int position = i.getIntExtra(FilterFragment.EXTRA_POSITION, -1);

        if (f == null) {
            f = CreateFragment.newInstance(position);
        }
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, f);
        fragmentTransaction.commit();
    }
}
