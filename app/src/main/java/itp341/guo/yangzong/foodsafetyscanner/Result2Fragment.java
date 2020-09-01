package itp341.guo.yangzong.foodsafetyscanner;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import itp341.guo.yangzong.foodsafetyscanner.model.Allergy;
import itp341.guo.yangzong.foodsafetyscanner.model.AllergySingleton;
import itp341.guo.yangzong.foodsafetyscanner.model.Severity;


/**
 * A simple {@link Fragment} subclass.
 */
public class Result2Fragment extends Fragment {
    public static final String QUERY = "query";
    private String query;
    private TextView safe;
    private TextView resultText;
    private ImageView safeImage;
    private Button back;
    private ProgressBar indeterminateBar;

    public Result2Fragment() {
        // Required empty public constructor
    }

    public static Result2Fragment newInstance(String query) {
        Bundle args = new Bundle();
        args.putString(QUERY, query);

        Result2Fragment f = new Result2Fragment();
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();

        query = args.getString(QUERY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_result2, container, false);
        indeterminateBar = (ProgressBar) v.findViewById(R.id.indeterminateBar);
        safe = (TextView) v.findViewById(R.id.safe_label);
        safe.setVisibility(View.INVISIBLE);
        resultText = (TextView) v.findViewById(R.id.result_text);
        resultText.setVisibility(View.INVISIBLE);
        safeImage = (ImageView) v.findViewById(R.id.allergy_image);
        safeImage.setVisibility(View.INVISIBLE);
        back = (Button) v.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), MainActivity.class);
                startActivity(i);
            }
        });

        Log.d("query_value", query);
        String url = "https://api.nutritionix.com/v1_1/item?id=" + query + "&fields=nf_ingredient_statement&appId=15f0aa3f&appKey=db3ad5644db1c988917872c4423f5631";
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        checkSafety(response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        indeterminateBar.setVisibility(View.GONE);
                        safe.setVisibility(View.VISIBLE);
                        safe.setText(getString(R.string.not_found));
                    }
                });

        queue.add(jsObjRequest);
        return v;
    }

    public void checkSafety(JSONObject object) {
        SpannableString str = null;
        String brandName = null;
        String itemName = null;
        String ingredientsString = null;
        try {
            ingredientsString = object.getString("nf_ingredient_statement");
            System.out.println("HELLO" + ingredientsString);
            itemName = object.getString("item_name");
            brandName = object.getString("brand_name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //List<String> ingredients = Arrays.asList(ingredientsString.split("\\s*,\\s*"));
//        Pattern pt = Pattern.compile("[^a-zA-Z0-9\\s]");
//        Matcher match= pt.matcher(ingredientsString);
//        while(match.find())
//        {
//            String s= match.group();
//            ingredientsString=ingredientsString.replaceAll("\\"+s, "");
//        }
        ingredientsString = ingredientsString.replaceAll("[()]", "");
        List<String> ingredients = Arrays.asList(ingredientsString.split("[^\\w']+"));
        for (int i = 0; i < ingredients.size(); i++) System.out.println(ingredients.get(i));
        Log.d("ingredientsString", ingredientsString);
        //List<String> ingredients = new ArrayList<String>(Arrays.asList(ingredientsString.split(" ")));
        List<String> contains = new ArrayList<String>();
        List<Allergy> allergies = new ArrayList<Allergy>();
        for (int i = 0; i < ingredients.size(); i++) {
            if (AllergySingleton.get(getActivity()).getIngredients().contains(ingredients.get(i).toLowerCase()) && !contains.contains(ingredients.get(i))) {
                System.out.println("pooper");
                contains.add(ingredients.get(i));
            }
            for (int j = 0; j < AllergySingleton.get(getActivity()).getAllAllergies().size(); j++) {
                if (AllergySingleton.get(getActivity()).getAllergy(j).isChecked() && AllergySingleton.get(getActivity()).getAllergy(j).getIngredients().contains(ingredients.get(i).toLowerCase()) && !allergies.contains(AllergySingleton.get(getActivity()).getAllergy(j))) {
                    allergies.add(AllergySingleton.get(getActivity()).getAllergy(j));
                    Log.d("hola", "got here");
                }
            }
        }
        // set warning labels
        if (ingredients.size() == 0) {
            safe.setText(getString(R.string.maybe_safe));
            safeImage.setImageResource(R.mipmap.minus_round);
            resultText.setText(R.string.no_ingredient);
        } else if (contains.size() == 0 && allergies.size() == 0) {
            safe.setText(getString(R.string.safe));
            safeImage.setImageResource(R.mipmap.check_round);
            str = new SpannableString(brandName + " " + itemName + " " + getString(R.string.not_contain));
            str.setSpan(new StyleSpan(Typeface.BOLD), 0, brandName.length() + itemName.length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            resultText.setText(str);
        } else {
            safe.setText(getString(R.string.maybe_safe));
            safeImage.setImageResource(R.mipmap.minus_round);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < contains.size(); i++) {
                sb.append(contains.get(i));
                sb.append(", ");
            }
            Log.d("size", Integer.toString(allergies.size()));
            for (int i = 0; i < allergies.size(); i++) {
                if (allergies.get(i).getSeverity().equals(Severity.LIFE_THREATENING)) {
                    safeImage.setImageResource(R.mipmap.block_round);
                    safe.setText(getString(R.string.not_safe));
                }
                sb.append(allergies.get(i).getName());
                sb.append(", ");
            }
            str = new SpannableString(brandName + " " + itemName + " contains: " + sb.toString().substring(0, sb.toString().length() - 2) + ".");
            str.setSpan(new StyleSpan(Typeface.BOLD), 0, brandName.length() + itemName.length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            resultText.setText(str);
        }

        // set visibility

        indeterminateBar.setVisibility(View.GONE);
        safe.setVisibility(View.VISIBLE);
        resultText.setVisibility(View.VISIBLE);
        safeImage.setVisibility(View.VISIBLE);
    }

}
