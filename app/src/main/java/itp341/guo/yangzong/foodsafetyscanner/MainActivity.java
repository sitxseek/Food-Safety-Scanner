package itp341.guo.yangzong.foodsafetyscanner;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import itp341.guo.yangzong.foodsafetyscanner.model.Allergy;
import itp341.guo.yangzong.foodsafetyscanner.model.AllergySingleton;

public class MainActivity extends AppCompatActivity {
    public static final String PREF_FILE_NAME = "PrefFile";
    public static final String ALLERGIES = "Allergies";
    public static final String INGREDIENTS = "Ingredients";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // retrieve saved singleton
        SharedPreferences preferences = getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        //preferences.edit().clear().commit();

        Gson gson = new Gson();
        String allergies = preferences.getString(ALLERGIES, "");
        String ingredients = preferences.getString(INGREDIENTS, "");

        Type typeIngredient = new TypeToken<List<String>>() {}.getType();
        List<String> mIngredients = gson.fromJson(ingredients, typeIngredient);

        Type typeAllergy = new TypeToken<List<Allergy>>() {}.getType();
        List<Allergy> mAllergies = gson.fromJson(allergies, typeAllergy);

        if (mIngredients != null) {
            AllergySingleton.get(this).setmIngredients(mIngredients);
        }
        if (mAllergies != null) {
            AllergySingleton.get(this).setmAllergies(mAllergies);
        }

        FragmentManager fm = getSupportFragmentManager();
        Fragment f = fm.findFragmentById(R.id.fragment_container);

        if (f == null) {
            f = MainFragment.newInstance();
        }
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, f);
        fragmentTransaction.commit();
    }
}
