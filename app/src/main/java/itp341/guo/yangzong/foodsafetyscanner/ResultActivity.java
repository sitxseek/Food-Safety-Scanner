package itp341.guo.yangzong.foodsafetyscanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.vision.barcode.Barcode;

/**
 * Created by yangzong on 11/14/17.
 */

public class ResultActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent i = getIntent();
        Barcode barcode = (Barcode) i.getParcelableExtra(ScanFragment.BARCODE);

        FragmentManager fm = getSupportFragmentManager();
        Fragment f = fm.findFragmentById(R.id.fragment_container);

        if (f == null) {
            f = ResultFragment.newInstance(barcode);
        }
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, f);
        fragmentTransaction.commit();
    }
}
