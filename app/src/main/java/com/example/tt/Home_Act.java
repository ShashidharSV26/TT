package com.example.tt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Instrumentation;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.Key;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Home_Act extends AppCompatActivity {

    private static final String JSON_URL = "http://192.168.2.254/efe/getAllContent.do";

    RequestQueue requestQueue;

    JSONObject mainobj;



    List<String> subObjectList;
    ArrayList<HomeModel> subobjlist=new ArrayList<>();
    RecyclerView recyclerView;

    int code;
    Home_Act_adapter homeActAdapter;
    int [] homeIcons={R.drawable.movies,R.drawable.video,R.drawable.comedy,R.drawable.cartoon1,R.drawable.trailers};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        startService(new Intent(this, MyPlayerService.class));
        IntentFilter filter = new IntentFilter(MyPlayerService.BROADCAST_ACTION);
        registerReceiver(messageReceiver, filter);

        getSupportActionBar().setTitle("HOME");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.home);

        recyclerView=findViewById(R.id.homerecycleview);

        requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, JSON_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    mainobj = new JSONObject(response);
                    Log.d("Home_Act","mainobj=========="+mainobj);

                    JSONObject messageObject = mainobj.getJSONObject("message");
                    subObjectList = new ArrayList<>();

                    Iterator<String> keys = messageObject.keys();
                    int i=-1;
                    while (keys.hasNext()) {
                        String key = keys.next();
                        subObjectList.add(key);
                        subobjlist.add(new HomeModel(key,homeIcons[++i]));
                    }
                    subobjlist.add(new HomeModel("About",R.drawable.about));
                    subobjlist.add(new HomeModel("Safety",R.drawable.safety));
                    Log.d("Home_Act","subObjectList=========="+subObjectList.toString());
//                    // Print or use the subObjectList as desired
//                    for (String subObjects : subObjectList) {
//                        Log.d("Home_Act","subObjectList=========="+subObjects);
//                    }
                    homeActAdapter=new Home_Act_adapter(getApplicationContext(),subobjlist);
                    recyclerView.setHasFixedSize(true);
                    RecyclerView.LayoutManager layoutManager=new GridLayoutManager(getApplicationContext(),4);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(homeActAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Home_Act.this, "No Data Available For Selected option", Toast.LENGTH_LONG).show();
            }
        });

        requestQueue.add(request);
    }

    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this)
                .setTitle("Close App")
                .setMessage("Do you want to close the app?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(MyPlayerService.BROADCAST_ACTION)) {

                int keycode=intent.getIntExtra("code",0);
                doAction(keycode);
            }
        }
    };

void doAction(int keycode){
    new Thread(new Runnable() {
        @Override
        public void run() {
                Instrumentation inst = new Instrumentation();
                    inst.sendKeyDownUpSync(keycode);
        }
    }).start();
}

    @Override
protected void onDestroy() {
    super.onDestroy();
    unregisterReceiver(messageReceiver);
}
}