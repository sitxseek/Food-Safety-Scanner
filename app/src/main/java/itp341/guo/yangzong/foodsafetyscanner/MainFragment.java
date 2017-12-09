package itp341.guo.yangzong.foodsafetyscanner;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import jp.wasabeef.blurry.Blurry;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {
    public static final String QUERY = "query";
    private Button textButton;
    private Button scanButton;
    private Button avoidButton;

    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance() {
        Bundle args = new Bundle();

        MainFragment f = new MainFragment();
        f.setArguments(args);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        textButton = (Button) v.findViewById(R.id.text_button);
        textButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Search By Item Name");

                final EditText input = new EditText(getActivity());
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton("Search", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendJSON(input.getText().toString());
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        scanButton = (Button) v.findViewById(R.id.scan_button);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), ScanActivity.class);
                startActivity(i);
            }
        });

        avoidButton = (Button) v.findViewById(R.id.avoid_button);
        avoidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), TabActivity.class);
                startActivity(i);
            }
        });

        return v;
    }

    public void sendJSON(String query) {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = "https://api.nutritionix.com/v1_1/search/" + query +"?results=0%3A1&cal_min=0&cal_max=50000&appId=15f0aa3f&appKey=ff47eadb8037bab107ad4a8827359f42";
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("hola2", "got here too");
                        String id = null;
                        try {
                            JSONArray a = response.getJSONArray("hits");
                            response = a.getJSONObject(0);
                            id = response.getString("_id");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Intent i = new Intent(getActivity(), Result2Activity.class);
                        i.putExtra(QUERY, id);
                        startActivity(i);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        queue.add(jsObjRequest);
    }

}
