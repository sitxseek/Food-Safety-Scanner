package itp341.guo.yangzong.foodsafetyscanner;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.List;

import itp341.guo.yangzong.foodsafetyscanner.model.AllergySingleton;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends Fragment {
//    private Button filterButton;
    private ImageButton addButton;
    private EditText ingredientText;
    private String ingredient;
    private ListView ingredientsList;
    private List<String> ingredients;
    public ListFragment() {
        // Required empty public constructor
    }

    public static ListFragment newInstance() {
        Bundle args = new Bundle();

        ListFragment f = new ListFragment();
        f.setArguments(args);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_list, container, false);
//        filterButton = (Button) v.findViewById(R.id.filter_button);
//        filterButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(getActivity(), FilterActivity.class);
//                startActivityForResult(i, 1);
//            }
//        });
        addButton = (ImageButton) v.findViewById(R.id.button_add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ingredient != null && ingredient.length() > 0) {
                    AllergySingleton.get(getActivity()).addIngredient(ingredient);
                    ingredientText.setText("");
                    ((BaseAdapter) ingredientsList.getAdapter()).notifyDataSetChanged();
                    // saving to preferences
                    SharedPreferences preferences = getActivity().getSharedPreferences(
                            MainActivity.PREF_FILE_NAME, Context.MODE_PRIVATE);
                    SharedPreferences.Editor prefsEditor = preferences.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(AllergySingleton.get(getActivity()).getIngredients());
                    prefsEditor.putString(MainActivity.INGREDIENTS, json);
                    prefsEditor.apply();
                }
            }
        });
        ingredientText = (EditText) v.findViewById(R.id.ingredient_text);
        ingredientText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ingredient = charSequence.toString().toLowerCase();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ingredientsList = (ListView) v.findViewById(R.id.ingredient_list);
        ingredients = AllergySingleton.get(getActivity()).getIngredients();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                ingredients);
        ingredientsList.setAdapter(arrayAdapter);
        ingredientsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ingredients.remove(i);
                ((BaseAdapter) ingredientsList.getAdapter()).notifyDataSetChanged();
                // update preferences
                SharedPreferences preferences = getActivity().getSharedPreferences(MainActivity.PREF_FILE_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor prefsEditor = preferences.edit();
                Gson gson = new Gson();
                String json = gson.toJson(AllergySingleton.get(getActivity()).getIngredients());
                prefsEditor.putString(MainActivity.INGREDIENTS, json);
                prefsEditor.commit();
            }
        });
        ingredientsList.setEmptyView(v.findViewById(R.id.empty_list_item));
        return v;
    }

}
