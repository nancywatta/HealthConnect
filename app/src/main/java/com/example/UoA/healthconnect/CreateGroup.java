package com.example.UoA.healthconnect;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class CreateGroup extends ActionBarActivity {

    private static final String TAG = CreateGroup.class.getSimpleName();
    private RoleSpinAdapter adapter;
    Spinner roleSpinner;
    long accountId;
    long roleId;
    Button saveButton;
    EditText groupName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        groupName = (EditText)findViewById(R.id.et_email_invite_user);
        saveButton = (Button)findViewById(R.id.button_invite_user);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    SaveInDB();
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });

        SharedPreferences pref = getSharedPreferences(getString(R.string.loginPref), Context.MODE_PRIVATE);
        accountId = pref.getLong("accountId",0);

        roleSpinner = (Spinner)findViewById(R.id.spinner_role);
        getRoles();
    }

    private void getRoles() {
        String url = "http://" + getString(R.string.IPAddress) +
                ":8080/HealthConnect/Dictionary/showRoles";

        Log.d(TAG, "url: " + url);

        // Instantiating DownloadTask to get roles from Dictionary service
        // in a non-ui thread
        DownloadTask downloadTask = new DownloadTask();

        // Start downloading the groups
        downloadTask.execute(url);
    }

    /** A class, to download roles from Dictionary webservice */
    private class DownloadTask extends AsyncTask<String, Integer, String> {

        String data = null;

        // Invoked by execute() method of this object
        @Override
        protected String doInBackground(String... url) {
            try{
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d(TAG, e.toString());
                e.printStackTrace();
            }
            return data;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(String result){

            // Instantiating ParserTask which parses the json data from Dictionary webservice
            // in a non-ui thread
            ParserTask parserTask = new ParserTask();

            // Start parsing the groups in JSON format
            // Invokes the "doInBackground()" method of the class ParseTask
            parserTask.execute(result);
        }
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);
            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while( ( line = br.readLine()) != null){
                sb.append(line);
            }

            data = sb.toString();
            br.close();

        }catch(Exception e){
            Log.d("Exception while downloading url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }

        return data;
    }

    /** A class to parse the roles in non-ui thread */
    class ParserTask extends AsyncTask<String, Integer, ArrayList<Dictionary>>{

        JSONObject jObject;

        // Invoked by execute() method of this object
        @Override
        protected ArrayList<Dictionary> doInBackground(String... jsonData) {

            ArrayList<Dictionary> roles = null;
            JSONParser parser = new JSONParser();

            try{
                jObject = new JSONObject(jsonData[0]);

                /** Getting the parsed data as a an ArrayList */
                roles = parser.roleParse(jObject);

            }catch(Exception e){
                Log.d(TAG,e.toString());
                e.printStackTrace();
            }
            return roles;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(ArrayList<Dictionary> roleList){
            if(roleList == null || roleList.size() <1) {
                Toast.makeText(CreateGroup.this, "No Roles Available", Toast.LENGTH_SHORT).show();
                return;
            }

            adapter = new RoleSpinAdapter(CreateGroup.this,
                    android.R.layout.simple_spinner_item,
                    roleList);

            roleSpinner.setAdapter(adapter);
            roleSpinner.setAdapter(adapter);

            roleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view,
                                           int position, long id) {
                    roleId = adapter.getItem(position).getId();
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapter) {  }
            });
        }
    }

    public void SaveInDB() {

        if(groupName.getText().toString().compareTo("")==0) {
            Toast.makeText(this, "Enter Group Name", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "http://" + getString(R.string.IPAddress) +
                ":8080/HealthConnect/Group/createGroup";

        String parameters = "accountId=" + accountId + "&groupName=" + groupName.getText().toString()
                + "&roleId=" + roleId;

        // url , to save user in Database
        url = url + "?" + parameters;

        Log.d(TAG, "url: " + url);

        // Instantiating InviteUserBackgroundTask to save member from Group service
        // in a non-ui thread
        CreateGroupTask createGroupTask = new CreateGroupTask(CreateGroup.this);

        createGroupTask.execute(url);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.create_group, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
