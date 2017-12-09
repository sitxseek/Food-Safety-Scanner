package itp341.guo.yangzong.foodsafetyscanner;


import android.app.Activity;
import android.content.Context;
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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import itp341.guo.yangzong.foodsafetyscanner.model.Allergy;
import itp341.guo.yangzong.foodsafetyscanner.model.AllergySingleton;
import itp341.guo.yangzong.foodsafetyscanner.model.Severity;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreateFragment extends Fragment {
    public static final String ARGS_POSITION = "args_position";
    private int position;
    private EditText allergyName;
    private Spinner severity;
    private EditText ingredient;
    private Button addIngredient;
    private ListView ingredientList;
    private Button save;
    private Button delete;
    private List<String> ingredients;
    private Severity sev;

    public CreateFragment() {
        // Required empty public constructor
    }

    public static CreateFragment newInstance(int position) {
        Bundle args = new Bundle();
        args.putInt(ARGS_POSITION, position);

        CreateFragment f = new CreateFragment();
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();

        position = args.getInt(ARGS_POSITION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_create, container, false);
        allergyName = (EditText) v.findViewById(R.id.allergy_name);
        severity = (Spinner) v.findViewById(R.id.severity);
        ingredient = (EditText) v.findViewById(R.id.ingredient);
        addIngredient = (Button) v.findViewById(R.id.ingredient_button);
        save = (Button) v.findViewById(R.id.save_button);
        delete = (Button) v.findViewById(R.id.delete_button);
        ingredientList = (ListView) v.findViewById(R.id.ingredient_list);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity().getApplicationContext(),
                R.array.severity_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        severity.setAdapter(adapter);

        ingredientList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ingredients.remove(i);
                ((BaseAdapter) ingredientList.getAdapter()).notifyDataSetChanged();
            }
        });
        ingredientList.setEmptyView(v.findViewById(R.id.empty_list_item));

        severity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    sev = Severity.LIFE_THREATENING;
                } else if (i == 1) {
                    sev = Severity.MODERATE;
                } else if (i == 2) {
                    sev = Severity.MILD;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        addIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ingredients.add(ingredient.getText().toString().toLowerCase());
                ((BaseAdapter) ingredientList.getAdapter()).notifyDataSetChanged();
                ingredient.setText("");
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveAndClose();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAndClose();
            }
        });
        // check if this is an existing or new listing
        if (position != -1) {
            Allergy a = AllergySingleton.get(getActivity()).getAllergy(position);
            if (a != null) {
                loadData(a);
            }
        }
        else { //ADDING A new record
            ingredients = new ArrayList<>();
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                    getActivity(),
                    android.R.layout.simple_list_item_1,
                    ingredients);
            ingredientList.setAdapter(arrayAdapter);
            delete.setVisibility(View.GONE);
        }
        return v;
    }
    // load previous data
    private void loadData(Allergy a) {
        ingredients = a.getIngredients();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                ingredients);
        ingredientList.setAdapter(arrayAdapter);
        allergyName.setText(a.getName());
        if (a.getSeverity().equals(Severity.LIFE_THREATENING)) {
            severity.setSelection(0);
        } else if (a.getSeverity().equals(Severity.MODERATE)) {
            severity.setSelection(1);
        } else {
            severity.setSelection(2);
        }
    }

    private void saveAndClose() {
        Allergy a = new Allergy(allergyName.getText().toString(), ingredients, sev);
        if (position != -1) {       //UPDATING existing shop
            a.setChecked(AllergySingleton.get(getActivity()).getAllergy(position).isChecked());
            AllergySingleton.get(getActivity()).updateAllergy(position, a);
        }
        else {      //ADDING new record
            AllergySingleton.get(getActivity()).addAllergy(a);
        }
        // add to preferences
        SharedPreferences preferences = getActivity().getSharedPreferences(MainActivity.PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(AllergySingleton.get(getActivity()).getAllAllergies());
        prefsEditor.putString(MainActivity.ALLERGIES, json);
        prefsEditor.commit();

        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    private void deleteAndClose() {
        //call the singleton remove method
        //finish the activity with OK
        if (position != -1) {
            AllergySingleton.get(getActivity()).removeAllergy(position);
            // remove from preferences
            SharedPreferences preferences = getActivity().getSharedPreferences(MainActivity.PREF_FILE_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor prefsEditor = preferences.edit();
            Gson gson = new Gson();
            String json = gson.toJson(AllergySingleton.get(getActivity()).getAllAllergies());
            prefsEditor.putString(MainActivity.ALLERGIES, json);
            prefsEditor.commit();

            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
        }
    }
}
