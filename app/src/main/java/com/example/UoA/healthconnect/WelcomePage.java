package com.example.UoA.healthconnect;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class WelcomePage extends ActionBarActivity {

    TextView welcomeText;
    String emailId;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);

        welcomeText = (TextView)findViewById(R.id.WelcomeText);
        SharedPreferences pref = getSharedPreferences(getString(R.string.loginPref), Context.MODE_PRIVATE);
        userName = pref.getString("userName", "");
        if(userName == null || userName.compareTo("")==0) {
            emailId = pref.getString("email","");
            welcomeText.setText("Welcome " + emailId);
        } else {
            welcomeText.setText("Welcome " + userName);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.welcome_page, menu);
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

    public void goToManageGroup(View v) {
        Intent intent = new Intent(this,ManageGroup.class);
        startActivity(intent);
    }
}
