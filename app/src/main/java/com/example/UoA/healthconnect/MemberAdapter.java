package com.example.UoA.healthconnect;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Nancy on 8/29/14.
 */
public class MemberAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<Member> result;
    private static LayoutInflater inflater=null;
    private long accountId;

    public MemberAdapter(Activity a, ArrayList<Member> d, long accountId) {
        activity = a;
        result=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.accountId = accountId;
    }

    public int getCount() {
        return result.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.member_list_row, null);
            holder.text = (TextView)convertView.findViewById(R.id.userName); // username
            holder.text1 = (TextView)convertView.findViewById(R.id.Role); // role
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        Member member = result.get(position);

        if(member.getUserName() == null ||
                member.getUserName().compareTo("")==0) {
            if(accountId == member.getAccountId()) {
                holder.text.setTextColor(Color.parseColor("#9d0c0f"));
                holder.text.setText(member.getEmail() + "(Me)");
            }
            else
                holder.text.setText(member.getEmail());
        }
        else {
            if(accountId == member.getAccountId()) {
                holder.text.setTextColor(Color.parseColor("#9d0c0f"));
                holder.text.setText(member.getUserName() + "(Me)");
            }
            else
                holder.text.setText(member.getUserName());
        }
        holder.text1.setText(member.getRole().getName());

        return convertView;
    }

    static class ViewHolder {
        TextView text;
        TextView text1;
    }

    public synchronized   void refreshAdapter() {
        notifyDataSetChanged();
    }
}
