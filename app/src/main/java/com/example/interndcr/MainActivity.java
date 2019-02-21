package com.example.interndcr;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity{
    //Button variable declare
    private Button submit;

   //spinner variable declare
    public Spinner productGroupSpinner;
    public Spinner literatureSpinner;
    public Spinner physicianSpinner;
    public Spinner giftSpinner;

    private final int jsoncode = 1;
    private static ProgressDialog mProgressDialog;
    private ArrayList<ModelData> modelDataArrayList;

    //spinner item variable to hold json value
    private ArrayList<String> product_group = new ArrayList<String>();
    private ArrayList<String> literature = new ArrayList<String>();
    private ArrayList<String> sample = new ArrayList<String>();
    private ArrayList<String> gift = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //spinner variable pointing
        productGroupSpinner = findViewById(R.id.productGroupSpinner);
        literatureSpinner = findViewById(R.id.literatureSpinner);
        physicianSpinner = findViewById(R.id.physicianSpinner);
        giftSpinner = findViewById(R.id.giftSpinner);

        //loadJSON() called to get json data as String
        loadJSON();
        submit = findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Done!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private void loadJSON(){

        showSimpleProgressDialog(this, "Loading...","Fetching Json",false);

        new AsyncTask<Void, Void, String>() {
            String data = "";
            @Override
            protected String doInBackground(Void... voids) {
                try {
                    URL url = new URL("https://raw.githubusercontent.com/appinion-dev/intern-dcr-data/master/data.json");
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String line = "";
                    while (line != null){
                        line = bufferedReader.readLine();
                        data = data + line;
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.d("data",data);
                //full json data get and send as String
                return data;
            }//loadJSON function ended

            protected void onPostExecute(String result) {
                //do something with response
                Log.d("newwwss",result);
                onTaskCompleted(result,jsoncode);
            }
        }.execute();
    }
    public void onTaskCompleted(String response, int serviceCode) {
        Log.d("responsejson", response);
        switch (serviceCode) {
            case jsoncode:

                if (isSuccess(response)) {
                    removeSimpleProgressDialog();  //will remove progress dialog

                    modelDataArrayList = parseInfo(response);

                    // adding Array to the  model data
                    for (int i = 0; i < modelDataArrayList.size(); i++){
                        product_group.add(modelDataArrayList.get(i).getproduct_group());
                        literature.add(modelDataArrayList.get(i).getliterature());
                        sample.add(modelDataArrayList.get(i).getsample());
                        gift.add(modelDataArrayList.get(i).getgift());
                    }
//setting simple_spinner_dropdown_item
                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item,product_group);
                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                    productGroupSpinner.setAdapter(spinnerArrayAdapter);

                    ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item,literature);
                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                    literatureSpinner.setAdapter(spinnerArrayAdapter2);

                    ArrayAdapter<String> spinnerArrayAdapter3 = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item,sample);
                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                    physicianSpinner.setAdapter(spinnerArrayAdapter3);

                    ArrayAdapter<String> spinnerArrayAdapter4 = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item,gift);
                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                    giftSpinner.setAdapter(spinnerArrayAdapter4);

                }else {
                    Toast.makeText(MainActivity.this, getErrorCode(response), Toast.LENGTH_SHORT).show();
                }
        }
    }//onTaskCompleted ended

    public ArrayList<ModelData> parseInfo(String response) {
        Log.d("response", response);
        ArrayList<ModelData> allDataArrayList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(response);

            // fatching data from parsed json
            JSONArray dataArray1 = jsonObject.getJSONArray("product_group_list");
            JSONArray dataArray2 = jsonObject.getJSONArray("literature_list");
            JSONArray dataArray3 = jsonObject.getJSONArray("physician_sample_list");
            JSONArray dataArray4 = jsonObject.getJSONArray("gift_list");
            Log.d("gfd", String.valueOf(dataArray1));
            for (int i = 0; i < dataArray1.length(); i++) {
                ModelData allData = new ModelData();
                allData.setproduct_group(dataArray1.getJSONObject(i).getString("product_group"));
                Log.d("gfd", dataArray1.getJSONObject(i).getString("product_group"));
                allData.setliterature(dataArray2.getJSONObject(i).getString("literature"));
                allData.setsample(dataArray3.getJSONObject(i).getString("sample"));
                allData.setgift(dataArray4.getJSONObject(i).getString("gift"));
                Log.d("gfd", dataArray4.getJSONObject(i).getString("gift"));
                allDataArrayList.add(allData);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return allDataArrayList;
    }
//isSuccess is checking weather json data fetching possible or not
    public boolean isSuccess(String response) {
        Log.d("gfd","res:"+ response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray dataArray1 = jsonObject.getJSONArray("product_group_list");
            Log.d("gfd", "issucccess trying3");
            String exmp= dataArray1.getJSONObject(0).getString("product_group");

            Log.d("gfd", exmp);
            if (exmp.equals("PredniSONE")) {
                return true;
            } else {

                return false;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }
//most of the json link has a message object
//  this json data have no such thing
// but it does't make any issue, code is working fine
    public String getErrorCode(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            return jsonObject.getString("message");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "No data";
    }
//when every thing worked fine, ProgressDialog removed
    public static void removeSimpleProgressDialog() {
        try {
            if (mProgressDialog != null) {
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                    mProgressDialog = null;
                }
            }
        } catch (IllegalArgumentException ie) {
            ie.printStackTrace();

        } catch (RuntimeException re) {
            re.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//when json not loading in spinner yet!
    public static void showSimpleProgressDialog(Context context, String title,
                                                String msg, boolean isCancelable) {
        try {
            if (mProgressDialog == null) {
                mProgressDialog = ProgressDialog.show(context, title, msg);
                mProgressDialog.setCancelable(isCancelable);
            }

            if (!mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }

        } catch (IllegalArgumentException ie) {
            ie.printStackTrace();
        } catch (RuntimeException re) {
            re.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
