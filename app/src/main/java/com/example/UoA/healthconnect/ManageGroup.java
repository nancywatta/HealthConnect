package com.example.UoA.healthconnect;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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

public class ManageGroup extends ActionBarActivity {

    private static final String TAG = ManageGroup.class.getSimpleName();
    long accountId=1;
    private Spinner spinner;
    private GroupSpinAdapter adapter;
    long groupId;
    String groupName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage_group);

        spinner = (Spinner) findViewById(R.id.spinner_group);

        getGroups();
	}

    private void getGroups() {
        String url = "http://192.168.1.6:8080/HealthConnect/Group/showGroups";

        String parameters = "accountId=" + accountId;

        // url , from where the groups are fetched
        url = url + "?" + parameters;

        Log.d(TAG, "url: " + url);

        // Instantiating DownloadTask to get groups from Group service
        // in a non-ui thread
        DownloadTask downloadTask = new DownloadTask();

        // Start downloading the groups
        downloadTask.execute(url);
    }

    /** A class, to download Groups from Group webservice */
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

            // Instantiating ParserTask which parses the json data from Group webservice
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
            Log.d(TAG, e.toString());
            e.printStackTrace();
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }

        return data;
    }

    /** A class to parse the Groups in non-ui thread */
    class ParserTask extends AsyncTask<String, Integer, ArrayList<Group>>{

        JSONObject jObject;

        // Invoked by execute() method of this object
        @Override
        protected ArrayList<Group> doInBackground(String... jsonData) {

            ArrayList<Group> groups = null;
            JSONParser parser = new JSONParser();

            try{
                jObject = new JSONObject(jsonData[0]);

                /** Getting the parsed data as a an ArrayList */
                groups = parser.groupParse(jObject);

            }catch(Exception e){
                Log.d(TAG,e.toString());
                e.printStackTrace();
            }
            return groups;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(ArrayList<Group> groupList){
            if(groupList == null || groupList.size() <1) {
                Toast.makeText(getBaseContext(), "No Groups Created", Toast.LENGTH_SHORT).show();
                return;
            }

            adapter = new GroupSpinAdapter(ManageGroup.this,
                    android.R.layout.simple_spinner_item,
                    groupList);

            spinner.setAdapter(adapter);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view,
                                           int position, long id) {

                    groupId = adapter.getItem(position).getGroupId();
                    groupName = adapter.getItem(position).getGroupName();

                }
                @Override
                public void onNothingSelected(AdapterView<?> adapter) {  }
            });
        }
    }


    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.manage_group, menu);
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
	  public void onClickBtnNewCustomer(View view)
	    {
	    	Intent intent = new Intent(this,ManageUsers.class);
            intent.putExtra("GROUPID", groupId);
            intent.putExtra("ACCOUNTID", accountId);
            intent.putExtra("GROUPNAME", groupName);
	    	startActivity(intent);
	    	
	    }
}
