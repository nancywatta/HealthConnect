package com.example.UoA.healthconnect;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Nancy on 8/29/14.
 * Class for Tab3 "Invite User"
 */
public class InviteFragment extends Fragment {

    private static final String TAG = InviteFragment.class.getSimpleName();
    Context context;
    long groupId;
    long accountId;
    long roleId;
    String groupName;
    TextView groupText;
    TextView emailText;
    private RoleSpinAdapter adapter;
    Spinner roleSpinner;
    Button saveButton;

    public InviteFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View dataView = inflater.inflate(R.layout.fragment_invite_tab,
                container, false);

        context = container.getContext();
        groupId = ManageUsers.getGroupId();
        accountId = ManageUsers.getAccountId();
        groupName = ManageUsers.getGroupName();

        groupText = (TextView) dataView.findViewById(R.id.GroupText);
        emailText = (TextView) dataView.findViewById(R.id.et_email_invite_user);
        saveButton = (Button)dataView.findViewById(R.id.button_invite_user);
        groupText.setText(groupName);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    SaveInDB();
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });

        roleSpinner = (Spinner) dataView.findViewById(R.id.spinner_role);

        getRoles();

        return dataView;
    }

    private void getRoles() {
        String url = "http://192.168.1.6:8080/HealthConnect/Dictionary/showRoles";

        String parameters = "accountId=" + accountId + "&groupId=" + groupId;

        // url , from where the groups are fetched
        url = url + "?" + parameters;

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
                Toast.makeText(getActivity(), "No Roles Available", Toast.LENGTH_SHORT).show();
                return;
            }

            adapter = new RoleSpinAdapter(getActivity(),
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

        if(emailText.getText().toString().compareTo("")==0) {
            Toast.makeText(getActivity(), "Enter Email Id", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "http://192.168.1.6:8080/HealthConnect/Group/inviteUser";

        String parameters = "accountId=" + accountId + "&groupId=" + groupId
                + "&emailId=" + emailText.getText().toString() + "&roleId=" + roleId;

        // url , to save user in Database
        url = url + "?" + parameters;

        Log.d(TAG, "url: " + url);

        // Instantiating InviteUserBackgroundTask to save member from Group service
        // in a non-ui thread
        InviteUserBackgroundTask userBackgroundTask = new InviteUserBackgroundTask(getActivity());

        userBackgroundTask.execute(url);
    }
}
