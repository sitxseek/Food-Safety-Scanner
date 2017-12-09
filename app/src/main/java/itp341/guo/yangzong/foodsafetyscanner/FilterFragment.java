package itp341.guo.yangzong.foodsafetyscanner;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.util.List;

import itp341.guo.yangzong.foodsafetyscanner.model.Allergy;
import itp341.guo.yangzong.foodsafetyscanner.model.AllergySingleton;


/**
 * A simple {@link Fragment} subclass.
 */
public class FilterFragment extends Fragment {
    public static final String EXTRA_POSITION = "extra_position";
    private ListView filters;
    private List<Allergy> allergyList;
    private Button addButton;
    private TextView ingredients;
    private AllergyListAdapter adapter;
    public FilterFragment() {
        // Required empty public constructor
    }

    public static FilterFragment newInstance() {
        Bundle args = new Bundle();

        FilterFragment f = new FilterFragment();
        f.setArguments(args);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_filter, container, false);
        filters = (ListView) v.findViewById(R.id.filters);
        filters.setEmptyView(v.findViewById(R.id.empty_list_item));
        allergyList = AllergySingleton.get(getContext()).getAllAllergies();
        ingredients = (TextView) v.findViewById(R.id.ingredients);
        adapter = new AllergyListAdapter(getActivity(), R.layout.layout_list_filter, allergyList);
        filters.setAdapter(adapter);

        filters.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent i = new Intent(getActivity(), CreateActivity.class);
                i.putExtra(EXTRA_POSITION, position);
                startActivityForResult(i,0);
            }
        });

        addButton = (Button) v.findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), CreateActivity.class);
                startActivityForResult(i, 0);
            }
        });
        return v;
    }

    private class AllergyListAdapter extends ArrayAdapter<Allergy> {
        public AllergyListAdapter(Context c, int resId, List<Allergy> allergies) {
            super(c, resId, allergies);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(
                        R.layout.layout_list_filter,
                        null
                );
            }
            CheckBox checkbox = (CheckBox) convertView.findViewById(R.id.checkbox);
            TextView allergyName = (TextView) convertView.findViewById(R.id.allergy_name);
            TextView severity = (TextView) convertView.findViewById(R.id.severity);
            TextView ingredients = (TextView) convertView.findViewById(R.id.ingredients);
            final Allergy allergy = getItem(position);

            checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if (compoundButton.isPressed()) {
                        if (isChecked) {
                            //AllergySingleton.get(getActivity()).addSelected(allergy);
                            allergy.setChecked(true);
                        } else {
                            //AllergySingleton.get(getActivity()).removeSelected(allergy);
                            allergy.setChecked(false);
                        }

                        SharedPreferences preferences2 = getActivity().getSharedPreferences(MainActivity.PREF_FILE_NAME, Context.MODE_PRIVATE);
                        SharedPreferences.Editor prefsEditor2 = preferences2.edit();
                        Gson gson2 = new Gson();
                        String json2 = gson2.toJson(AllergySingleton.get(getActivity()).getAllAllergies());
                        prefsEditor2.putString(MainActivity.ALLERGIES, json2);
                        prefsEditor2.commit();
                    }
                }
            });
            checkbox.setChecked(allergy.isChecked());

            allergyName.setText(allergy.getName());
            severity.setText(allergy.getSeverity().toString());
            StringBuilder sb = new StringBuilder();
            // ingredients list
            String prefix = "";
            for (int i = 0; i < allergy.getIngredients().size(); i++) {
                sb.append(prefix);
                prefix = ", ";
                sb.append(allergy.getIngredients().get(i));
            }
            ingredients.setText(sb.toString());
            return convertView;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        adapter.notifyDataSetChanged();
    }
}
