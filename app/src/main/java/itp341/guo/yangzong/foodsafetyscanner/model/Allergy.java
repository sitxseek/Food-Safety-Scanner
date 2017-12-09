package itp341.guo.yangzong.foodsafetyscanner.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangzong on 11/12/17.
 */

public class Allergy {
    private String name;
    private List<String> ingredients;
    private Severity severity;
    private boolean isChecked;

    public Allergy(String name, List<String> ingredients, Severity severity) {
        this.name = name;
        this.ingredients = new ArrayList<String>(ingredients);
        this.severity = severity;
        isChecked = false;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void addIngredient(String ingredient) {
        ingredients.add(ingredient);
    }
    public List<String> getIngredients() {
        return ingredients;
    }
    public void setSeverity(Severity severity) {
        this.severity = severity;
    }
    public Severity getSeverity() {
        return this.severity;
    }
    public boolean isChecked() {
        return isChecked;
    }
    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
