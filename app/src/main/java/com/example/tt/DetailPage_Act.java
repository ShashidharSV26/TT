package com.example.tt;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DetailPage_Act extends AppCompatActivity {

    TextView lang_Txt;
    ListView lang_list;
    GridView langgrid;
    ArrayAdapter adapter;
    String receivedText;
    JSONObject obj;

    JSONArray messageArray;

    List<DetailsModel> detailList;
    RequestQueue requestQueue;
    private static final String JSON_URL = "http://192.168.2.254/efe/getAllContent.do";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_page);

        ActionBar actionBar=getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);

        langgrid=findViewById(R.id.langGrid);
//        lang_list = findViewById(R.id.langList);
        lang_Txt=findViewById(R.id.language);
        Intent intent = getIntent();
        receivedText = intent.getStringExtra("maintitle");
        if(receivedText.equals("videos")){
            Log.d("DetailPage","**********videos selected*******");

        }
        actionBar.setTitle(receivedText.toUpperCase());
        requestQueue = Volley.newRequestQueue(this);
        detailList = new ArrayList<>();
        loadLanguageList();

        
        langgrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for(int i=0; i<=detailList.size();i++){
                    if(position == i){
                        String selected_lang=detailList.get(i).toString();
                        Toast.makeText(getApplicationContext(),"You are selecting "+ selected_lang +" Language.",Toast.LENGTH_SHORT).show();
                        Log.d("DetailsPage","selecteditem=========="+detailList.get(i));
                        Intent intent = new Intent(getApplicationContext(), MovieList_Act.class);
                        intent.putExtra("selectedMainItem",receivedText);
                        intent.putExtra("selectedLanguage",detailList.get(i).toString());
                        intent.putExtra("languageArray",messageArray.toString());
                        intent.putExtra("language",i);
                        startActivity(intent);
                    }
                }
            }
        });

    }

    private void loadLanguageList() {

        StringRequest request = new StringRequest(Request.Method.GET, JSON_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    obj = new JSONObject(response);
                    JSONObject messobj = obj.getJSONObject("message");
                    messageArray = messobj.getJSONArray(receivedText);


                    for (int i = 0; i < messageArray.length(); i++) {
                        JSONObject  languageObj = messageArray.getJSONObject(i);

                        String language = languageObj.keys().next();
                        Log.d("langobject",language);
                        DetailsModel model = new DetailsModel(language);
                        detailList.add(model);
                    }
                    if(detailList.size()>0) {
                        adapter = new DetailsAdapter(getApplicationContext(), detailList);
//                        lang_list.setAdapter(adapter);
                        langgrid.setAdapter(adapter);
                    }else{
                        lang_Txt.setText("No Data Available");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetailPage_Act.this, "No Data Available For Selected option", Toast.LENGTH_LONG).show();
            }
        });

        requestQueue.add(request);

    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}