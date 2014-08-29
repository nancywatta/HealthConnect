package com.example.UoA.healthconnect;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Nancy on 8/29/14.
 */
public class JSONParser {

    public ArrayList<Group> groupParse(JSONObject jObject) {
        ArrayList<Group> groups = new ArrayList<Group>() ;
        JSONArray jResults = null;
        try{
            jResults = jObject.getJSONArray("groups");
            for(int i=0;i<jResults.length();i++) {
                JSONObject jResult = jResults.getJSONObject(i);

                Group group = new Group(jResult.getLong("id"), jResult.getString("groupname"));

                groups.add(group);
            }

        }catch ( JSONException e) {
            e.printStackTrace();
        }catch (Exception e){
        }

        return groups;
    }

    public ArrayList<Member> memberParse(JSONObject jObject) {
        ArrayList<Member> members = new ArrayList<Member>() ;
        JSONArray jResults = null;
        try{
            jResults = jObject.getJSONArray("members");
            for(int i=0;i<jResults.length();i++) {
                JSONObject jResult = jResults.getJSONObject(i);
                JSONObject jRole = jResult.getJSONObject("role");

                Dictionary role = new Dictionary(jRole.getLong("id"),
                        jRole.getString("type"), jRole.getString("value"), jRole.getString("name"),
                        jRole.getString("description"));

                Member member = new Member(jResult.getLong("id"), jResult.getString("email"),
                        jResult.getString("username"), role);

                members.add(member);
            }

        }catch ( JSONException e) {
            e.printStackTrace();
        }catch (Exception e){
        }

        return members;
    }

    public ArrayList<Dictionary> roleParse(JSONObject jObject) {
        ArrayList<Dictionary> roles = new ArrayList<Dictionary>() ;
        JSONArray jResults = null;
        try{
            jResults = jObject.getJSONArray("roles");
            for(int i=0;i<jResults.length();i++) {
                JSONObject jResult = jResults.getJSONObject(i);

                Dictionary role = new Dictionary(jResult.getLong("id"), jResult.getString("name"));

                roles.add(role);
            }

        }catch ( JSONException e) {
            e.printStackTrace();
        }catch (Exception e){
        }

        return roles;
    }

}
