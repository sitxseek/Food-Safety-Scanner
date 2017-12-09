package itp341.guo.yangzong.foodsafetyscanner.model;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangzong on 11/12/17.
 */

public class AllergySingleton {
    private List<Allergy> mAllergies;
    private List<String> mIngredients;
    private Context mContext;

    private static AllergySingleton sSingleton;

    private AllergySingleton(Context c) {
        mContext = c;
        mAllergies = new ArrayList<>();
        mIngredients = new ArrayList<>();
    }

    public static AllergySingleton get(Context c) {
        if (sSingleton == null) {
            sSingleton = new AllergySingleton(c);
        }
        return sSingleton;
    }

    public void setmAllergies(List<Allergy> mAllergies) {
        this.mAllergies = new ArrayList<>(mAllergies);
    }

    public void setmIngredients(List<String> mIngredients) {
        this.mIngredients = new ArrayList<>(mIngredients);
    }

    public int getNumAllergies() {
        return mAllergies.size();
    }

    public List<Allergy> getAllAllergies() {
        return mAllergies;
    }

    public Allergy getAllergy(int i) {
        if (i >= 0 && i < mAllergies.size()) {
            return mAllergies.get(i);
        }
        else {
            return null;
        }
    }

    public void addAllergy(Allergy allergy) {
        mAllergies.add(allergy);
    }

    public void removeAllergy(int index) {
        if (index >= 0 && index < mAllergies.size()) {
            mAllergies.remove(index);
        }
    }
    public void updateAllergy(int index, Allergy a) {
        if (index >= 0 && index < mAllergies.size()){
            mAllergies.set(index, a);
        }

    }
    public void addIngredient(String ingredient) {
        mIngredients.add(ingredient);
    }

    public List<String> getIngredients() {
        return mIngredients;
    }

}
