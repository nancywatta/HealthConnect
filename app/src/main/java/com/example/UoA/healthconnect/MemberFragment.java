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
import android.widget.ListView;
import android.widget.Toast;

import com.example.UoA.healthconnect.ResponseModel.HCMessage;

import org.json.JSONObject;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Nancy on 8/29/14.
 * Class for Tab1 "Member"
 */
public class MemberFragment extends Fragment implements
        MyAlertDialogWIndow.AlertPositiveListener {

    private static final String TAG = MemberFragment.class.getSimpleName();
    Context context;
    ListView list;
    long groupId;
    long accountId;
    MemberAdapter adapter;
    ArrayList<Member> memberArray = new ArrayList<Member>();
    Member memberDetails;

    public MemberFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View dataView = inflater.inflate(R.layout.fragment_members,
                container, false);

        context = container.getContext();
        list = (ListView) dataView.findViewById(R.id.list);

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {

                /** Instantiating the DialogFragment */
                MyAlertDialogWIndow alert = new MyAlertDialogWIndow();

                Bundle args = new Bundle();
                args.putString("message", "Do you want to remove "
                                +  memberArray.get(pos).getEmail() + " from Group?");
                alert.setArguments(args);

                /** Opening the dialog window */
                //alert.show(fm, "Alert_Dialog");
                alert.show(getFragmentManager(), "Alert_Dialog");
                memberDetails = memberArray.get(pos);

                return true;
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                /** Instantiating the DialogFragment */
                MyAlertDialogWIndow alert = new MyAlertDialogWIndow();

                Bundle args = new Bundle();
                args.putString("message", "Do you want to remove "
                        +  memberArray.get(position).getEmail() + " from Group?");
                alert.setArguments(args);

                /** Opening the dialog window */
                alert.show(getFragmentManager(), "Alert_Dialog");
                memberDetails = memberArray.get(position);
            }
        });

        groupId = ManageUsers.getGroupId();
        accountId = ManageUsers.getAccountId();

        getMembers();

        return dataView;
    }

    private void getMembers() {
        String url = "http://" + getString(R.string.IPAddress) +
                ":8080/HealthConnect/Group/showMembers";

        String parameters = "accountId=" + accountId + "&groupId=" + groupId;

        // url , from where the members are fetched
        url = url + "?" + parameters;

        Log.d(TAG, "url: " + url);

        // Instantiating DownloadTask to get members from Group service
        // in a non-ui thread
        DownloadTask downloadTask = new DownloadTask();

        // Start downloading the members
        downloadTask.execute(url);
    }

    /**
     * A class, to download members from Group webservice
     */
    private class DownloadTask extends AsyncTask<String, Integer, String> {

        String data = null;

        // Invoked by execute() method of this object
        @Override
        protected String doInBackground(String... url) {
            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d(TAG, e.toString());
                e.printStackTrace();
            }
            return data;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(String result) {

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
        try {
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
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            br.close();

        } catch (Exception e) {
            Log.d("Exception while downloading url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }

        return data;
    }

    /**
     * A class to parse the members in non-ui thread
     */
    class ParserTask extends AsyncTask<String, Integer, ArrayList<Member>> {

        JSONObject jObject;

        // Invoked by execute() method of this object
        @Override
        protected ArrayList<Member> doInBackground(String... jsonData) {

            ArrayList<Member> members = null;
            JSONParser parser = new JSONParser();

            try {
                jObject = new JSONObject(jsonData[0]);

                /** Getting the parsed data as an ArrayList */
                members = parser.memberParse(jObject);

            } catch (Exception e) {
                Log.d(TAG, e.toString());
                e.printStackTrace();
            }
            return members;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(ArrayList<Member> memberList) {
            if (memberList == null || memberList.size() < 1) {
                Toast.makeText(context, "No Members Added", Toast.LENGTH_SHORT).show();
                return;
            }

            memberArray = memberList;

            adapter = new MemberAdapter(getActivity(), memberList, accountId);
            list.setAdapter(adapter);
        }
    }

    /**
     * Defining button click listener for the OK button of the alert dialog window
     */
    @Override
    public void onPositiveClick(boolean isDeleteSet) {

    }

    public void deleteMember() {
        new HttpRequestTask().execute();
        Log.d(TAG, "deleteMember in Fragment");
    }

    private class HttpRequestTask extends AsyncTask<Void, Void, HCMessage> {
        @Override
        protected HCMessage doInBackground(Void... params) {
            try {
                final String url = "http://" + getString(R.string.IPAddress) +
                        ":8080/HealthConnect/service/group/deleteMember";
                Log.d(TAG, "url: " + url);
                RestTemplate restTemplate = new RestTemplate();

                HttpMessageConverter formHttpMessageConverter = new FormHttpMessageConverter();
                HttpMessageConverter stringHttpMessageConverternew = new StringHttpMessageConverter();

                restTemplate.getMessageConverters().add(formHttpMessageConverter);
                restTemplate.getMessageConverters().add(stringHttpMessageConverternew);

                MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
                map.add("accountId", Long.toString(accountId));
                map.add("groupId", Long.toString(groupId));
                map.add("memberId", Long.toString(memberDetails.getAccountId()));

                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

                HCMessage message = restTemplate.postForObject(url, map, HCMessage.class);
                return message;
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(HCMessage message) {

            if(message.getError() != null) {
                Toast.makeText(context, message.getError(), Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(context, message.getResponse().toString(), Toast.LENGTH_SHORT).show();
            getMembers();
        }

    }

}
